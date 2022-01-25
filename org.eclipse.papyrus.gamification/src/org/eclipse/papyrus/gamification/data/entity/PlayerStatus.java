package org.eclipse.papyrus.gamification.data.entity;

public class PlayerStatus {
	int pointsNumber;
	int goldCoinsNumber;
	int currentLevel;

	public PlayerStatus(int pointsNumber, int goldCoinsNumber, int currentLevel) {
		this.pointsNumber = pointsNumber;
		this.goldCoinsNumber = goldCoinsNumber;
		this.currentLevel = currentLevel;
		System.out.println("Creating new player profile : " + pointsNumber + " - " + goldCoinsNumber + " - " + currentLevel);
	}

	public PlayerStatus computeProgression(PlayerStatus olderPlayerStatus) {
		return new PlayerStatus(this.pointsNumber - olderPlayerStatus.getPointsNumber(),
				this.goldCoinsNumber - olderPlayerStatus.getGoldCoinsNumber(),
				this.currentLevel - olderPlayerStatus.getCurrentLevel());
	}

	public int getPointsNumber() {
		return pointsNumber;
	}

	public int getGoldCoinsNumber() {
		return goldCoinsNumber;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	@Override
	public String toString() {
		return "PlayerStatus [pointsNumber=" + pointsNumber + ", goldCoinsNumber=" + goldCoinsNumber + ", currentLevel="
				+ currentLevel + "]";
	}


}
