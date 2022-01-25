package org.eclipse.papyrus.gamification.data.api.query;

import org.eclipse.papyrus.gamification.data.api.response.CustomDataJson;

public class UserCreationRequest {

	String playerId;
	String gameId;
	CustomDataJson customData;

	public UserCreationRequest(String playerId, String gameId, String encryptedPassword) {
		super();
		this.playerId = playerId;
		this.gameId = gameId;
		this.customData = new CustomDataJson(encryptedPassword);
	}


}
