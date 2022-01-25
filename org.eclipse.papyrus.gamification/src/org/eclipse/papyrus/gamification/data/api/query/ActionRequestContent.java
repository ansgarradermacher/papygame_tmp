package org.eclipse.papyrus.gamification.data.api.query;

public class ActionRequestContent<T> {

	String actionId;
	String playerId;
	String gameId;
	String executionMoment = "2020-07-11T13:22:15.745Z";
	T data;

	public ActionRequestContent(String actionId) {
		super();
		this.actionId = actionId;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getExecutionMoment() {
		return executionMoment;
	}

	public void setExecutionMoment(String executionMoment) {
		this.executionMoment = executionMoment;
	}

	public T getData() {
		return data;
	}

	@Override
	public String toString() {
		return "ActionRequestContent [actionId=" + actionId + ", playerId=" + playerId + ", gameId=" + gameId
				+ ", executionMoment=" + executionMoment + ", data=" + data + "]";
	}

}
