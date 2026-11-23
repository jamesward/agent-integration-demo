# agent-integration-demo

Spring AI example of AI agent integration patterns (inference API basics, chat memory, RAG, MCP client, tool search) on Amazon Bedrock. Not published.

Follow the `zen-of-projects` Skill (extract it with `./gradlew extractSkillsJars`); this file records
only project-specific facts and exceptions.

## Skills

`zen-of-projects`, `zen-of-james` (from `com.jamesward:skills`, extracted to the gitignored `.kiro/skills/`).

## MCP

`javadocs` (https://www.javadocs.dev/mcp), configured in `.mcp.json` / `.kiro/settings/mcp.json` and
approved in `.claude/settings.json`. Use its `get_latest_version` for version lookups and its
source/doc tools for API questions. In Claude Code its tools are deferred: load them with ToolSearch
(search `javadocs`).

## Build & test

- Full validation: `./gradlew build`.

## Maintenance routine

`.factory/MAINTENANCE.md` (weekly), following the `zen-of-projects` Skill.

## Exceptions to zen-of-projects

- `./gradlew bootRun` (and any task that calls Amazon Bedrock) needs AWS credentials and is paid: never run it in the maintenance routine.

## Held-back dependencies

- `spring-ai-bom` stays at 2.0.2: 2.1.0 deprecates `ChatMemory`, `MessageWindowChatMemory` and `MessageChatMemoryAdvisor` for removal in 3.0.0 (replacement: `SessionService` in `spring-ai-session`, context key `MemoryAdvisor.CONVERSATION_ID`), which fails the `-Werror` build. Migrating the chat memory examples (`BasicMemory`, `BasicTools`) is a behavior change that needs a human decision.
