package com.teampulse.backend.service.impl;

import com.teampulse.backend.entity.WeeklyReport;
import com.teampulse.backend.enums.ReportStatus;
import com.teampulse.backend.repository.WeeklyReportRepository;
import com.teampulse.backend.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private static final int CONTEXT_WEEKS = 8;
    private static final int MAX_REPORTS = 150;
    private static final int MAX_TOKENS = 1024;

    private static final String SYSTEM_PROMPT = """
            You are an assistant embedded in a team "Weekly Report Generator & Dashboard" tool.
            You are speaking with a MANAGER. You will be given a block of recent, submitted weekly
            reports from the team. Use only this provided data to answer questions.
            Do not invent team members, projects, or facts.

            If the answer is not available in the context, clearly say so.

            When asked for a summary, organize it as:
            1. Key completed work
            2. Recurring blockers
            3. Workload observations

            Keep responses concise, factual, and professional.
            """;
    private final WeeklyReportRepository reportRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${anthropic.api-key}")
    private String apiKey;

    @Value("${anthropic.model}")
    private String model;

    @Value("${anthropic.base-url}")
    private String baseUrl;


    @Override
    public String chat(String managerQuestion) {
        if (apiKey == null || apiKey.isBlank()) {
            return "AI assistant is not configured. Please configure the Anthropic API key.";
        }

        String reportContext = buildReportContext();

        String userPrompt =
                "Here are the team's recent submitted weekly reports:\n\n"
                        + reportContext
                        + "\n\n---\nManager's question:\n"
                        + managerQuestion;

        try {

            JsonNode response = webClient.post()
                    .uri(baseUrl)
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(buildRequestBody(userPrompt))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            return extractResponseText(response);

        } catch (Exception exception) {

            return "Unable to contact the AI service at the moment. Please try again later.";
        }
    }

    private String buildReportContext() {

        LocalDate fromDate = LocalDate.now().minusWeeks(CONTEXT_WEEKS);
        List<WeeklyReport> reports = reportRepository.search(
                null,
                null,
                fromDate,
                LocalDate.now().plusDays(7));

        StringBuilder contextBuilder = new StringBuilder();

        int reportCount = 0;
        for (WeeklyReport report : reports) {

            if (report.getStatus() != ReportStatus.SUBMITTED) {
                continue;
            }

            if (reportCount >= MAX_REPORTS) {
                break;
            }

            contextBuilder.append("- Member: ")
                    .append(report.getUser().getFullName())
                    .append(" | Project: ")
                    .append(report.getProject() != null
                            ? report.getProject().getName()
                            : "Unassigned")
                    .append(" | Week: ")
                    .append(report.getWeekStartDate())
                    .append(" to ")
                    .append(report.getWeekEndDate())
                    .append(" | Hours: ")
                    .append(report.getHoursWorked() != null
                            ? report.getHoursWorked()
                            : "N/A")
                    .append("\nCompleted: ")
                    .append(getSafeText(report.getTasksCompleted()))
                    .append("\nPlanned Next: ")
                    .append(getSafeText(report.getTasksPlannedNext()))
                    .append("\nBlockers: ")
                    .append(report.getBlockers() != null && !report.getBlockers().isBlank()
                            ? report.getBlockers()
                            : "None")
                    .append("\n\n");

            reportCount++;
        }

        if (contextBuilder.isEmpty()) {
            return "(No submitted reports in the last 8 weeks.)";
        }

        return contextBuilder.toString();
    }


    private Map<String, Object> buildRequestBody(String userPrompt) {

        return Map.of(
                "model", model,
                "max_tokens", MAX_TOKENS,
                "system", SYSTEM_PROMPT,
                "messages",
                List.of(
                        Map.of(
                                "role", "user",
                                "content", userPrompt
                        )
                )
        );
    }

    private String extractResponseText(JsonNode response) {

        if (response == null || !response.has("content")) {
            return "Sorry, I couldn't reach the AI service right now.";
        }

        StringBuilder responseText = new StringBuilder();

        for (JsonNode block : response.get("content")) {

            if ("text".equals(block.path("type").asText())) {
                responseText.append(block.path("text").asText());
            }
        }

        if (responseText.isEmpty()) {
            return "Sorry, I didn't receive a valid response from the AI service.";
        }

        return responseText.toString();
    }

    private String getSafeText(String text) {
        return text == null ? "" : text;
    }
}
