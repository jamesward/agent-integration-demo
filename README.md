Agent Integration Patterns
--------------------------

Spring AI based example for AI Agent integration patterns including:
- Inference API Basics
- Tools & MCP
- Agent Skills
- RAG
- Chat Memory
- Human in the Loop

1. [Create a Bedrock Bearer token](https://us-east-1.console.aws.amazon.com/bedrock/home?region=us-east-1#/api-keys/long-term/create)
2. Set the env var: `export AWS_BEARER_TOKEN_BEDROCK=YOUR_TOKEN`

These demos are all command-line based. Each one uses a different Spring profile.

Pick the pattern you want to try and then run it using the profile name, like:

```
./gradlew systemPrompt
```

To run with verbose logging, append `Debug` to the profile name, like:

```
./gradlew systemPromptDebug
```
