package org.eclipse.papyrus.gamification.data.api.response;

public class PlayerStatusJson {
	String playerId;
	String gameId;
	CustomDataJson customData;
	PointStateJson state;

	public String getPlayerId() {
		return playerId;
	}

	public String getGameId() {
		return gameId;
	}

	public CustomDataJson getCustomData() {
		return customData;
	}

	public PointStateJson getState() {
		return state;
	}

}
