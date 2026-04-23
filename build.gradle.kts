plugins {
    java
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    implementation(platform("org.springframework.ai:spring-ai-bom:2.0.0-M4"))
    runtimeOnly("org.springframework.boot:spring-boot-starter-jackson")
    implementation("org.springframework.ai:spring-ai-starter-model-bedrock")
    implementation("org.springframework.ai:spring-ai-starter-model-bedrock-converse")
    implementation("org.springframework.ai:spring-ai-starter-mcp-client")
    implementation("org.springframework.ai:spring-ai-starter-model-chat-memory")
    implementation("org.springframework.ai:spring-ai-advisors-vector-store")
    implementation("org.springframework.ai:spring-ai-rag")
    implementation("org.springaicommunity:spring-ai-agent-utils:0.7.0")
    implementation("org.springaicommunity:tool-search-tool:2.1.0")
    implementation("org.springaicommunity:tool-searcher-vectorstore:2.1.0")
    runtimeOnly("com.jamesward:pooch-palace:0.0.3")
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    standardInput = System.`in`
}

val profiles = listOf(
    "default",
    "structuredOutput",
    "streaming",
    "systemPrompt",
    "noTools",
    "noMemory",
    "inMemory",
    "ragDocuments",
    "ragDomain",
    "preloadSkill",
    "ondemandSkill",
    "skillsjars",
    "basicTools",
    "mcpTools",
    "toolSearch",
    "hitl",
)

for (profile in profiles) {
    tasks.register<org.springframework.boot.gradle.tasks.run.BootRun>(profile) {
        group = "application"
        description = "Runs with the $profile Spring profile"
        mainClass = "com.jamesward.Application"
        classpath = sourceSets["main"].runtimeClasspath
        standardInput = System.`in`
        systemProperty("spring.profiles.active", profile)
    }
    tasks.register<org.springframework.boot.gradle.tasks.run.BootRun>("${profile}Debug") {
        group = "application"
        description = "Runs with the $profile and debug Spring profiles"
        mainClass = "com.jamesward.Application"
        classpath = sourceSets["main"].runtimeClasspath
        standardInput = System.`in`
        systemProperty("spring.profiles.active", "$profile,debug")
    }
}
