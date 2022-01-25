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

package org.eclipse.papyrus.gamification.view.dashboard;

import org.eclipse.papyrus.gamification.data.Logger;
import org.eclipse.papyrus.gamification.data.di.RepositoryFactory;
import org.eclipse.papyrus.gamification.data.entity.PlayerProfile;
import org.eclipse.papyrus.gamification.data.repository.dashboard.DashboardDataRepository;
import org.eclipse.papyrus.gamification.view.dashboard.DashboardContract.View;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.swt.schedulers.SwtSchedulers;

/**
 * @author maximesavaryleblanc
 *
 */
public class DashboardPresenter implements DashboardContract.Presenter {

	private DashboardDataRepository dashboardDataRepository;

	private CompositeDisposable compositeDisposable = new CompositeDisposable();
	private DashboardContract.View view;


	public DashboardPresenter() {
		dashboardDataRepository = RepositoryFactory.getDashboardDataRepository();
	}



	@Override
	public void getVideoIntroductionState(String playerId) {
		compositeDisposable.add(
				dashboardDataRepository.hasVideoBeenShown(playerId)
						.subscribeOn(Schedulers.io())
						.observeOn(SwtSchedulers.defaultDisplayThread())
						.subscribeWith(new DisposableSingleObserver<Boolean>() {

							@Override
							public void onError(Throwable throwable) {
								throwable.printStackTrace();
								System.out.println("Got error : " + throwable.getMessage());
								Logger.getInstance().logError(this.getClass(), throwable, "getVideoIntroductionState");
							}

							@Override
							public void onSuccess(Boolean hasVideoBeenShown) {
								if (!hasVideoBeenShown) {
									view.showVideo();
									setVideoShown(playerId);
								} else {
									view.skipVideo();
								}
							}
						}));
	}



	@Override
	public void getPlayerProfile(String login) {
		compositeDisposable.add(
				dashboardDataRepository.getPlayerProfile(login)
						.subscribeOn(Schedulers.io())
						.observeOn(SwtSchedulers.defaultDisplayThread())
						.subscribeWith(new DisposableSingleObserver<PlayerProfile>() {

							@Override
							public void onError(Throwable throwable) {
								throwable.printStackTrace();
								System.out.println("Got error : " + throwable.getMessage());
								Logger.getInstance().logError(this.getClass(), throwable, "getPlayerProfile");

							}

							@Override
							public void onSuccess(PlayerProfile playerProfile) {
								System.out.println("Got profile : " + playerProfile.toString());
								if (view != null) {
									view.displayPlayerProfile(playerProfile);
								}
							}

						}));
	}

	private void setVideoShown(String playerId) {
		compositeDisposable.add(
				dashboardDataRepository.setVideoShown(playerId)
						.subscribeOn(Schedulers.io())
						.observeOn(SwtSchedulers.defaultDisplayThread())
						.subscribeWith(new DisposableCompletableObserver() {


							@Override
							public void onError(Throwable throwable) {
								throwable.printStackTrace();
								System.out.println("Got error : " + throwable.getMessage());
								Logger.getInstance().logError(this.getClass(), throwable, "setVideoShown");

							}

							@Override
							public void onComplete() {
								System.out.println("Video has been registered as shown");
							}


						}));
	}

	@Override
	public void registerView(View view) {
		this.view = view;

	}

	@Override
	public void onShutdown() {
		// TODO Auto-generated method stub

	}

}
