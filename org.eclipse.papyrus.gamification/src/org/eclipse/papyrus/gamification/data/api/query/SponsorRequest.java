package org.eclipse.papyrus.gamification.data.api.query;

public class SponsorRequest {

	String playerId;
	String gameId;

	public SponsorRequest(String playerId, String gameId) {
		super();
		this.playerId = playerId;
		this.gameId = gameId;
	}


}
