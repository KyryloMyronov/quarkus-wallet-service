package com.kyrylomyronov.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Mock for API/Service/Source/Cache that provides information about players.
 * Wallet service doesn't manage players.
 *
 */
@Component
@Qualifier(value = "playerApi")
public class PlayerApiMock {

	private Set<String> playerIds = new HashSet<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")); //Initializing for testing purposes

	public String findPlayer(String playerId) {
		return playerIds.contains(playerId) ? playerId : null;
	}

	public boolean newPlayer(String playerId) {
		return playerIds.add(playerId);
	}
}
