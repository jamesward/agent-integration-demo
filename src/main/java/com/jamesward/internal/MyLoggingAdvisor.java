package com.jamesward.internal;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.tool.ToolCallingChatOptions;

public class MyLoggingAdvisor implements BaseAdvisor {

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest request, AdvisorChain chain) {
        var out = ModelOptionsUtils.toJsonStringPrettyPrinter(request.prompt());
        System.out.println("\nREQUEST:\n" + out);

        if (request.prompt().getOptions() instanceof ToolCallingChatOptions toolOptions) {
            var tools = toolOptions.getToolCallbacks().stream().map(tc -> tc.getToolDefinition().name()).toList();
            IO.println("\nTOOLS: " + ModelOptionsUtils.toJsonString(tools));
        }

        return request;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse response, AdvisorChain chain) {
        var out = ModelOptionsUtils.toJsonStringPrettyPrinter(response.chatResponse());
        System.out.println("\nRESPONSE:\n" + out);
        return response;
    }

}
