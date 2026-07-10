package com.jamesward.internal;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.util.JacksonUtils;
import org.springframework.ai.util.JsonHelper;
import tools.jackson.databind.SerializationFeature;

public class MyLoggingAdvisor implements BaseAdvisor {

    // Spring AI 2.0.0 removed ModelOptionsUtils#toJsonString(PrettyPrinter);
    // JsonHelper is the replacement. Configure it with an indent-enabled mapper
    // (built from the Spring AI default mapper so all modules stay registered).
    private static final JsonHelper JSON = new JsonHelper(
            JacksonUtils.getDefaultJsonMapper()
                    .rebuild()
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .build());

    @Override
    public int getOrder() {
        return 999999999;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest request, AdvisorChain chain) {
        var out = JSON.toJson(request.prompt());
        System.out.println("\nREQUEST:\n" + out);

        if (request.prompt().getOptions() instanceof ToolCallingChatOptions toolOptions) {
            var toolCallbacks = toolOptions.getToolCallbacks();
            if (toolCallbacks != null) {
                var tools = toolCallbacks.stream().map(tc -> tc.getToolDefinition().name()).toList();
                if (!tools.isEmpty()) {
                    IO.println("\nTOOLS: " + JSON.toJson(tools));
                }
            }
        }

        return request;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse response, AdvisorChain chain) {
        var out = JSON.toJson(response.chatResponse());
        System.out.println("\nRESPONSE:\n" + out);
        return response;
    }

}
