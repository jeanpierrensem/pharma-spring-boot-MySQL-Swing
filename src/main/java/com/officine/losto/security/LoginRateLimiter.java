package com.officine.losto.security;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class LoginRateLimiter {

	private static final int MAX_ATTEMPTS = 2;
	private static final long WINDOW_SECONDS = 300;

	private final Map<String, AttemptWindow> attempts = new ConcurrentHashMap<>();

	public boolean isBlocked(String key) {
		AttemptWindow window = attempts.get(key);
		if (window == null) {
			return false;
		}
		window.refreshIfExpired();
		return window.count >= MAX_ATTEMPTS;
	}

	public void recordFailure(String key) {
		attempts.compute(key, (k, existing) -> {
			AttemptWindow window = existing == null ? new AttemptWindow() : existing;
			window.refreshIfExpired();
			window.count++;
			return window;
		});
	}

	public void reset(String key) {
		attempts.remove(key);
	}

	private static final class AttemptWindow {
		private int count;
		private Instant windowStart = Instant.now();

		void refreshIfExpired() {
			if (Instant.now().isAfter(windowStart.plusSeconds(WINDOW_SECONDS))) {
				count = 0;
				windowStart = Instant.now();
			}
		}
	}
}
