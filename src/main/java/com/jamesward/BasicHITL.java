package com.jamesward;

import com.jamesward.internal.AccountData;
import org.springaicommunity.agent.tools.AskUserQuestionTool;
import com.jamesward.internal.AccountData.AccountSelectionQuestionHandler;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasicHITL {

    @Bean
    @Profile("hitl")
    CommandLineRunner runHITL(ChatClient.Builder builder, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors, EmbeddingModel embeddingModel) {
        return _ -> {
            var askTool = AskUserQuestionTool.builder()
                    .questionHandler(new AccountSelectionQuestionHandler())
                    .build();

            var chatClient = builder
                    .defaultAdvisors(loggingAdvisors)
                    .defaultSystem(s -> s.text("""
                        You are a helpful banking assistant.
                        The user's accounts are:
                        {accounts}
                        Use this information to answer their questions.
                        """).param("accounts", AccountData.accountDocuments)
                    )
                    .defaultTools(askTool)
                    .build();

            var resp = chatClient.prompt().user("Get my account balance").call().content();

            IO.println(resp);
        };
    }

}
