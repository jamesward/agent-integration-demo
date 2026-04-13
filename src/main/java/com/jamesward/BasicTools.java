package com.jamesward;


import com.jamesward.internal.AccountData;
import com.jamesward.internal.ManyTools;
import org.springaicommunity.tool.search.ToolSearchToolCallAdvisor;
import org.springaicommunity.tool.search.ToolSearcher;
import org.springaicommunity.tool.searcher.VectorToolSearcher;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BasicTools {

    @Tool(description = "Get the current date and time in the user's timezone")
    String getCurrentDateTime() {
        return LocalDateTime.now()
                .atZone(LocaleContextHolder.getTimeZone().toZoneId())
                .toString();
    }


    @Bean
    @Profile("basicTools")
    CommandLineRunner runDefault(ChatClient.Builder builder, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors) {
        return _ -> {
            var chatClient = builder.defaultAdvisors(loggingAdvisors).defaultTools(this).build();

            var resp = chatClient.prompt().user("what time is it?").call().content();

            IO.println(resp);
        };
    }

    @Bean
    @Profile("mcpTools")
    CommandLineRunner runMcp(ChatClient.Builder builder, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors, ToolCallbackProvider toolCallbackProvider) {
        return _ -> {
            var chatClient = builder.defaultAdvisors(loggingAdvisors).defaultToolCallbacks(toolCallbackProvider).defaultTools(this).build();

            var resp = chatClient.prompt().user("What is the latest version of the org.webjars:webjars-locator-lite library?").call().content();

            IO.println(resp);
        };
    }

    @Bean
    @Profile("toolSearch")
    CommandLineRunner toolSearch(ChatClient.Builder builder, EmbeddingModel embeddingModel, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors) {
        return _ -> {
            var vectorStore = SimpleVectorStore.builder(embeddingModel).build();

            var toolSearcher = new VectorToolSearcher(vectorStore);

            var toolSearchAdvisor = ToolSearchToolCallAdvisor.builder()
                    .toolSearcher(toolSearcher)
                    .maxResults(1)
                    .build();

            var advisors = new java.util.ArrayList<>(loggingAdvisors);
            advisors.removeIf(a -> a instanceof ToolCallAdvisor); // workaround for ToolCallAdvisor & ToolSearchTool incompatibility
            advisors.add(toolSearchAdvisor);

            var chatClient = builder
                    .defaultTools(new ManyTools())
                    .defaultAdvisors(advisors)
                    .build();

            var resp = chatClient.prompt().user("get me a random string 8 characters long").call().content();

            IO.println(resp);
        };
    }

}
