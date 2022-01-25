package org.eclipse.papyrus.gamification.data.repository.dashboard;

import java.util.List;

import org.eclipse.papyrus.gamification.data.api.response.LevelCompletedJson;
import org.eclipse.papyrus.gamification.data.api.response.PlayerStatusJson;
import org.eclipse.papyrus.gamification.data.entity.Level;
import org.eclipse.papyrus.gamification.data.entity.LevelPerformed;
import org.eclipse.papyrus.gamification.data.entity.PlayerProfile;
import org.eclipse.papyrus.gamification.data.entity.Series;
import org.eclipse.papyrus.gamification.data.entity.SeriesPerformed;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class DashboardDataRepository {

	private DashboardRemoteDataSource dashboardRemoteDataSource;
	private DashboardLocalDataSource dashboardLocalDataSource;

	public DashboardDataRepository(DashboardLocalDataSource dashboardLocalDataSource,
			DashboardRemoteDataSource dashboardRemoteDataSource) {
		this.dashboardLocalDataSource = dashboardLocalDataSource;
		this.dashboardRemoteDataSource = dashboardRemoteDataSource;
	}

	public Completable setVideoShown(String playerId) {
		return dashboardLocalDataSource.setVideoShown(playerId);

	}

	public Single<Boolean> hasVideoBeenShown(String playerId) {
		return dashboardLocalDataSource.hasVideoBeenShown(playerId);
	}

	public Single<PlayerProfile> getPlayerProfile(String playerId) {

		PlayerProfile playerProfile = new PlayerProfile(playerId);

		return dashboardRemoteDataSource.getAvailableSeries()
				.flatMapObservable(new Function<List<Series>, ObservableSource<Series>>() {
					@Override
					public ObservableSource<Series> apply(List<Series> availableSeries) throws Exception {
						return Observable.fromIterable(availableSeries);
					}
				})
				.flatMap(new Function<Series, ObservableSource<SeriesPerformed>>() {
					@Override
					public ObservableSource<SeriesPerformed> apply(Series series) throws Exception {
						return dashboardRemoteDataSource.getPlayerStatus(series.getSeriesGameId(), playerId)
								.map(new Function<PlayerStatusJson, SeriesPerformed>() {
									@Override
									public SeriesPerformed apply(PlayerStatusJson playerStatus) throws Exception {
										System.out.println("boolean is : " + playerStatus.getCustomData().isQuestionnaireFilled());
										playerProfile.computeQuestionnaireBoolean(playerStatus.getCustomData().isQuestionnaireFilled());
										playerProfile.addGoldCoins(playerStatus.getState().getGoldCoins());
										playerProfile.addXP(playerStatus.getState().getPoints());
										if (playerStatus.getCustomData() != null) {
											playerProfile.setInvitations(playerStatus.getCustomData().getInvitations());
										}
										return mapSeriesToSeriesPerformed(series, playerStatus);
									}
								}).toObservable();
					}
				})
				.toList()
				.map(new Function<List<SeriesPerformed>, PlayerProfile>() {

					@Override
					public PlayerProfile apply(List<SeriesPerformed> seriesPerformedList) throws Exception {
						for (SeriesPerformed seriesPerformed : seriesPerformedList) {

							playerProfile.getSeriesPerformed().add(seriesPerformed);
						}

						return playerProfile;
					}
				});
	}


	/**
	 * @param series
	 * @param playerStatus
	 * @return
	 */
	private SeriesPerformed mapSeriesToSeriesPerformed(Series series, PlayerStatusJson playerStatus) {
		SeriesPerformed seriesPerformed = new SeriesPerformed(series);
		for (Level level : series.getLevels()) {
			LevelPerformed levelPerformed = new LevelPerformed(level);
			if (playerStatus.getCustomData() != null) {
				for (LevelCompletedJson levelCompletedJson : playerStatus.getCustomData().getLevelsCompleted()) {
					if (playerStatus.getGameId().equals(series.getSeriesGameId())) {
						if (levelCompletedJson.getLevelLabel().equals(level.getLabel())) {
							levelPerformed.setDone(true);
							levelPerformed.setEarnedGoldCoins(levelCompletedJson.getEarnedGoldCoins());
							levelPerformed.setEarnedPoints(levelCompletedJson.getEarnedXP());
							break;
						}
					}
				}
			}
			seriesPerformed.getLevelsPerformed().add(levelPerformed);
		}
		return seriesPerformed;
	}

}
