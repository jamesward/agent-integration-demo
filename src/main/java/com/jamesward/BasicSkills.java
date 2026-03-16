package com.jamesward;

import org.springaicommunity.agent.tools.SkillsTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasicSkills {

    @Bean
    @Profile("preloadSkill")
    CommandLineRunner runPreloadSkill(ChatClient.Builder builder, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors) {
        return _ -> {
            var chatClient = builder.defaultAdvisors(loggingAdvisors).build();

            var dogSkill = new SystemMessage(new ClassPathResource("myskills/dog/SKILL.md"));

            var userMessage = new UserMessage("What does the Chihuahua say?");

            var resp = chatClient.prompt().messages(dogSkill, userMessage).call().content();

            IO.println(resp);
        };
    }

    @Bean
    @Profile("ondemandSkill")
    CommandLineRunner runOndemandSkill(ChatClient.Builder builder, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors) {
        return _ -> {
            var skillsTool = SkillsTool.builder().addSkillsResource(new ClassPathResource("myskills")).build();

            var chatClient = builder.defaultAdvisors(loggingAdvisors).defaultToolCallbacks(skillsTool).build();

            var resp = chatClient.prompt().user("What does the Chihuahua say?").call().content();

            IO.println(resp);
        };
    }

    @Bean
    @Profile("skillsjars")
    CommandLineRunner runSkillsJars(ChatClient.Builder builder, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors) {
        return _ -> {
            var skillsTool = SkillsTool.builder().addSkillsResource(new ClassPathResource("META-INF/skills")).build();

            var chatClient = builder.defaultAdvisors(loggingAdvisors).defaultToolCallbacks(skillsTool).build();

            var resp = chatClient.prompt().user("What does the Chihuahua say?").call().content();

            IO.println(resp);
        };
    }

}
