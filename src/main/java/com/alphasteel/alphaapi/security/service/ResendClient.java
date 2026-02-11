package com.alphasteel.alphaapi.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jboss.logging.Logger;

/** Sends emails via Resend HTTP API. */
@ApplicationScoped
public class ResendClient {

  private static final Logger LOG = Logger.getLogger(ResendClient.class);
  private static final URI EMAILS_URI = URI.create("https://api.resend.com/emails");

  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  @Inject
  public ResendClient(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();
  }

  public String sendHtmlEmail(
      String apiKey,
      String from,
      String toEmail,
      String subject,
      String htmlContent
  ) {
    try {
      String payload = buildPayload(from, toEmail, subject, htmlContent);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(EMAILS_URI)
          .timeout(Duration.ofSeconds(20))
          .header("Authorization", "Bearer " + apiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(payload))
          .build();

      HttpResponse<String> response = httpClient.send(
          request, HttpResponse.BodyHandlers.ofString()
      );

      int status = response.statusCode();
      if (status < 200 || status >= 300) {
        LOG.errorf("Resend returned %d: %s", status, response.body());
        throw new IllegalStateException("Email provider error.");
      }

      return parseId(response.body());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Email provider request interrupted.", e);
    } catch (IOException e) {
      throw new IllegalStateException("Email provider request failed.", e);
    }
  }

  private String buildPayload(
      String from,
      String toEmail,
      String subject,
      String htmlContent
  ) throws IOException {
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put("from", from);
    payload.put("to", List.of(toEmail));
    payload.put("subject", subject);
    payload.put("html", htmlContent);
    return objectMapper.writeValueAsString(payload);
  }

  private String parseId(String body) throws IOException {
    @SuppressWarnings("unchecked")
    Map<String, Object> data = objectMapper.readValue(body, Map.class);
    Object id = data.get("id");
    return id == null ? "unknown" : id.toString();
  }
}
