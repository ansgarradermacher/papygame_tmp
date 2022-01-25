/*****************************************************************************
 * Copyright (c) 2020 CEA LIST and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   CEA LIST - Initial API and implementation
 *
 *****************************************************************************/

package org.eclipse.papyrus.gamification.games.framework.communication;

import org.eclipse.papyrus.gamification.data.Logger;
import org.eclipse.papyrus.gamification.data.di.RepositoryFactory;
import org.eclipse.papyrus.gamification.data.entity.GameMetrics;
import org.eclipse.papyrus.gamification.data.entity.GameScore;
import org.eclipse.papyrus.gamification.data.entity.UmlDiagramSolution;
import org.eclipse.papyrus.gamification.data.repository.level.LevelDataRepository;
import org.eclipse.papyrus.gamification.games.framework.communication.LevelContract.Executor;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;
import org.eclipse.papyrus.gamification.modelutils.ResponseModel;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.swt.schedulers.SwtSchedulers;

/**
 * @author Xavier Ze Pallec
 *
 */
public class LevelPresenter implements LevelContract.Presenter {

	private LevelContract.Executor executor;
	private CompositeDisposable compositeDisposable = new CompositeDisposable();
	private LevelDataRepository levelDataRepository;


	public LevelPresenter() {
		this.levelDataRepository = RepositoryFactory.getLevelDataRepository();
	}



	@Override
	public ResponseModel loadResponseModel(String modelPath, boolean isReponseModelInternal) {
		return levelDataRepository.loadResponseModel(modelPath, isReponseModelInternal);
	}



	@Override
	public void registerExecutor(Executor view) {
		this.executor = view;
	}



	@Override
	public void onShutdown() {

	}


	private void subscribeOnGameScoreObservable(Single<GameScore> gameScoreSingle) {
		compositeDisposable.add(
				gameScoreSingle
						.subscribeOn(Schedulers.io())
						.observeOn(SwtSchedulers.defaultDisplayThread())
						.subscribeWith(new DisposableSingleObserver<GameScore>() {

							@Override
							public void onError(Throwable error) {
								// TODO Auto-generated method stub
								error.printStackTrace();
								Logger.getInstance().logError(this.getClass(), error, "submitGame");
							}

							@Override
							public void onSuccess(GameScore gameScore) {
								// TODO Auto-generated method stub
								executor.onGameScoreReceived(gameScore);
							}

						}));
	}


	@Override
	public void submitGame(GameMetrics gameMetrics, LevelContext levelContext) {
		subscribeOnGameScoreObservable(levelDataRepository.submitGame(gameMetrics, levelContext));
	}



	/**
	 * @see org.eclipse.papyrus.gamification.games.framework.communication.LevelContract.Presenter#submitGame(org.eclipse.papyrus.gamification.data.entity.UmlDiagramSolution, org.eclipse.papyrus.gamification.games.framework.entity.LevelContext)
	 *
	 * @param umlDiagramSolution
	 * @param levelContext
	 */
	@Override
	public void submitGame(UmlDiagramSolution umlDiagramSolution, LevelContext levelContext) {
		subscribeOnGameScoreObservable(levelDataRepository.submitGame(umlDiagramSolution, levelContext));

	}


}
