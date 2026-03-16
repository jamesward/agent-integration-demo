package com.jamesward;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasicMemory {

    @Bean
    @Profile("noMemory")
    CommandLineRunner runNoMemory(ChatClient.Builder builder, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors) {
        return _ -> {
            var chatClient = builder.defaultAdvisors(loggingAdvisors).build();

            var resp1 = chatClient.prompt().user("My name is James").call().content();

            IO.println(resp1);

            var resp2 = chatClient.prompt().user("What is my name?").call().content();

            IO.println(resp2);
        };
    }

    @Bean
    @Profile("inMemory")
    CommandLineRunner runInMemory(ChatClient.Builder builder, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors) {
        return _ -> {
            var chatMemory = MessageWindowChatMemory.builder().maxMessages(10).build();

            var memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

            var advisors = new java.util.ArrayList<>(loggingAdvisors);
            advisors.add(memoryAdvisor);

            var chatClient = builder.defaultAdvisors(advisors).build();

            var resp1 = chatClient.prompt().user("My name is James").call().content();

            IO.println(resp1);

            var resp2 = chatClient.prompt().user("What is my name?").call().content();

            IO.println(resp2);
        };
    }

}
