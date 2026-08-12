package com.officine.client.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.officine.client.auth.SessionManager;
import com.officine.client.config.ClientConfig;
import com.officine.client.model.ApiErrorResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public final class ApiClient {

	private static ApiClient instance;

	private final HttpClient httpClient;
	private final ObjectMapper objectMapper;
	private final String baseUrl;
	private final SessionManager sessionManager = SessionManager.getInstance();

	private ApiClient() {
		this.httpClient = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(10))
				.build();
		this.objectMapper = new ObjectMapper()
				.registerModule(new JavaTimeModule())
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.baseUrl = ClientConfig.apiBaseUrl();
	}

	public static synchronized ApiClient getInstance() {
		if (instance == null) {
			instance = new ApiClient();
		}
		return instance;
	}

	public ObjectMapper mapper() {
		return objectMapper;
	}

	public <T> T get(String path, Class<T> responseType) {
		return send("GET", path, null, responseType, true);
	}

	public <T> T post(String path, Object body, Class<T> responseType) {
		return send("POST", path, body, responseType, true);
	}

	public <T> T postPublic(String path, Object body, Class<T> responseType) {
		return send("POST", path, body, responseType, false);
	}

	public void postNoContent(String path, Object body, boolean authenticated) {
		send("POST", path, body, Void.class, authenticated);
	}

	private <T> T send(String method, String path, Object body, Class<T> responseType, boolean authenticated) {
		try {
			HttpRequest.Builder builder = HttpRequest.newBuilder()
					.uri(URI.create(baseUrl + trimLeadingSlash(path)))
					.timeout(Duration.ofSeconds(20))
					.header("Accept", "application/json");
			if (body != null) {
				builder.header("Content-Type", "application/json");
			}
			if (authenticated) {
				Optional<String> token = sessionManager.accessToken();
				if (token.isEmpty()) {
					throw new ApiException(401, "Session expirée — reconnectez-vous");
				}
				builder.header("Authorization", "Bearer " + token.get());
			}
			builder.method(method, body == null
					? HttpRequest.BodyPublishers.noBody()
					: HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)));
			HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() >= 200 && response.statusCode() < 300) {
				if (responseType == Void.class || response.body() == null || response.body().isBlank()) {
					return null;
				}
				return objectMapper.readValue(response.body(), responseType);
			}
			throw toApiException(response.statusCode(), response.body());
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			throw new ApiException(0, "Requête interrompue");
		} catch (IOException ex) {
			throw new ApiException(0, "Erreur réseau : " + ex.getMessage());
		}
	}

	private ApiException toApiException(int status, String body) {
		try {
			ApiErrorResponse error = objectMapper.readValue(body, ApiErrorResponse.class);
			if (error.fieldErrors() != null && !error.fieldErrors().isEmpty()) {
				String details = error.fieldErrors().stream()
						.map(v -> v.field() + ": " + v.message())
						.reduce((a, b) -> a + " | " + b)
						.orElse(error.message());
				return new ApiException(status, details);
			}
			return new ApiException(status, error.message() != null ? error.message() : error.error());
		} catch (IOException ignored) {
			return new ApiException(status, body != null && !body.isBlank() ? body : "Erreur HTTP " + status);
		}
	}

	private static String trimLeadingSlash(String path) {
		return path.startsWith("/") ? path.substring(1) : path;
	}

	public Map<String, Object> getMap(String path) {
		return get(path, Map.class);
	}
}
