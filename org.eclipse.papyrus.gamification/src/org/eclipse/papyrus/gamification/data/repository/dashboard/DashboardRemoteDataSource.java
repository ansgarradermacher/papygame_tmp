package org.eclipse.papyrus.gamification.data.repository.dashboard;

import java.util.List;

import org.eclipse.papyrus.gamification.data.api.GameManagerService;
import org.eclipse.papyrus.gamification.data.api.response.PlayerStatusJson;
import org.eclipse.papyrus.gamification.data.entity.Series;

import io.reactivex.Single;

public class DashboardRemoteDataSource {

	GameManagerService gameManagerService;


	public DashboardRemoteDataSource(GameManagerService gameManagerService) {
		this.gameManagerService = gameManagerService;
	}

	Single<List<Series>> getAvailableSeries() {
		System.out.println("--- Retrieving remote Available Series");
		return gameManagerService.getAvailableSeries("http://papygame.com/papyrus-plugin/json/availableSeriesFlow.json");
	}

	Single<PlayerStatusJson> getPlayerStatus(String gameId, String playerId) {
		System.out.println("--- Retrieving remote Player Status");
		return gameManagerService.getPlayerStatus(gameId, playerId);

	}

}
