package org.eclipse.papyrus.gamification.data.repository.level;

import java.util.concurrent.Callable;

import org.eclipse.papyrus.gamification.data.api.response.CustomDataJson;
import org.eclipse.papyrus.gamification.data.api.response.LevelCompletedJson;
import org.eclipse.papyrus.gamification.data.api.response.PlayerStatusJson;
import org.eclipse.papyrus.gamification.data.entity.GameMetrics;
import org.eclipse.papyrus.gamification.data.entity.GameScore;
import org.eclipse.papyrus.gamification.data.entity.PlayerStatus;
import org.eclipse.papyrus.gamification.data.entity.UmlDiagramSolution;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;
import org.eclipse.papyrus.gamification.modelutils.ResponseModel;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class LevelDataRepository {

	private LevelLocalDataSource levelLocalDataSource;
	private LevelRemoteDataSource levelRemoteDataSource;

	public LevelDataRepository(LevelLocalDataSource levelLocalDataSource, LevelRemoteDataSource levelRemoteDataSource) {
		super();
		this.levelLocalDataSource = levelLocalDataSource;
		this.levelRemoteDataSource = levelRemoteDataSource;
	}

	public ResponseModel loadResponseModel(String modelPath, boolean isReponseModelInternal) {
		return levelLocalDataSource.loadResponseModel(modelPath, isReponseModelInternal);
	}

	public Single<GameScore> submitGame(UmlDiagramSolution umlDiagramSolution, LevelContext levelContext) {
		return getGameScoreSingle(umlDiagramSolution.getTimeSpent(), levelRemoteDataSource.submitGameResult(umlDiagramSolution, levelContext), levelContext);
	}

	public Single<GameScore> submitGame(GameMetrics gameMetrics, LevelContext levelContext) {
		return getGameScoreSingle(gameMetrics.getTimeSpent(), levelRemoteDataSource.submitGameResult(gameMetrics, levelContext), levelContext);
	}

	private Single<GameScore> getGameScoreSingle(int spentTime, Completable submitGameCompletable, LevelContext levelContext) {
		// Get previously stored PlayerStatus
		return levelRemoteDataSource.getPlayerStatus(levelContext.getSeries().getSeriesGameId(),
				levelContext.getPlayerProfile().getPlayerId())
				.flatMap(new Function<PlayerStatusJson, SingleSource<GameScore>>() {
					@Override
					public SingleSource<GameScore> apply(PlayerStatusJson oldPlayerStatus) throws Exception {
						// Remote submit game
						return submitGameCompletable
								.andThen(Completable.fromCallable(new Callable<Object>() {

									@Override
									public Object call() throws Exception {
										// TODO Auto-generated method stub
										Thread.sleep(2000);
										return "OK";
									}
								}))
								// Get new player status Remote
								.andThen(levelRemoteDataSource.getPlayerStatus(levelContext.getSeries().getSeriesGameId(),
										levelContext.getPlayerProfile().getPlayerId())
										.flatMap(new Function<PlayerStatusJson, SingleSource<GameScore>>() {

											@Override
											public SingleSource<GameScore> apply(PlayerStatusJson arg0) throws Exception {
												// TODO Auto-generated method stub
												PlayerStatus old = new PlayerStatus(
														oldPlayerStatus.getState().getPoints(),
														oldPlayerStatus.getState().getGoldCoins(),
														10);
												PlayerStatus newStatus = new PlayerStatus(
														arg0.getState().getPoints(),
														arg0.getState().getGoldCoins(),
														10);
												PlayerStatus progress = newStatus.computeProgression(old);
												GameScore gameScore = new GameScore();
												gameScore.setEarnedGoldCoins(progress.getGoldCoinsNumber());
												gameScore.setEarnedXP(progress.getPointsNumber());
												gameScore.setLevelLabel(levelContext.getLevel().getLabel());
												gameScore.setGameSuccess(arg0.getCustomData().isLastGameSuccess());
												gameScore.setTimeSpent(spentTime);
												gameScore.setIcon(levelContext.getLevel().getIcon());

												LevelCompletedJson levelCompletedJson = new LevelCompletedJson();
												levelCompletedJson.setLevel(-1);
												levelCompletedJson.setLevelLabel(levelContext.getLevel().getLabel());
												levelCompletedJson.setEarnedGoldCoins(gameScore.getEarnedGoldCoins());
												levelCompletedJson.setEarnedXP(gameScore.getEarnedXP());
												levelCompletedJson.setTimeToComplete(gameScore.getTimeSpent());
												levelCompletedJson.setMoveNumber(gameScore.getOkMoves());
												levelCompletedJson.setErrors(gameScore.getErrors());
												levelCompletedJson.setLogs("LOGS");


												CustomDataJson customDataJson = arg0.getCustomData();
												System.out.println("Custom were " + customDataJson.toString());
												customDataJson.getLevelsCompleted().add(levelCompletedJson);
												System.out.println("Custom are " + customDataJson.toString());
												if (arg0.getCustomData().isLastGameSuccess()) {
													return levelRemoteDataSource.setPlayerCustomData(
															levelContext.getSeries().getSeriesGameId(),
															levelContext.getPlayerProfile().getPlayerId(),
															customDataJson)
															.andThen(Single.just(gameScore));
												}
												return Single.just(gameScore);
											}

										}));
					}

				});
	}



}
