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

package org.eclipse.papyrus.gamification.view.login;

import org.eclipse.papyrus.gamification.data.Logger;
import org.eclipse.papyrus.gamification.data.di.RepositoryFactory;
import org.eclipse.papyrus.gamification.data.exception.WrongLoginPasswordException;
import org.eclipse.papyrus.gamification.data.repository.login.LoginDataRepository;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.swt.schedulers.SwtSchedulers;

/**
 * @author maximesavaryleblanc
 *
 */
public class LoginPresenter implements LoginContract.Presenter {

	private LoginDataRepository loginDataRepository;

	private CompositeDisposable compositeDisposable = new CompositeDisposable();
	private LoginContract.View view;


	public LoginPresenter() {
		loginDataRepository = RepositoryFactory.getLoginDataRepository();
	}


	@Override
	public void getPlayer(String login, String password) {
		compositeDisposable.add(
				loginDataRepository.getPlayer(login, password)
						.subscribeOn(Schedulers.io())
						.observeOn(SwtSchedulers.defaultDisplayThread())
						.subscribeWith(new DisposableMaybeObserver<String>() {


							@Override
							public void onComplete() {
								view.proposeAccountCreation();
							}

							@Override
							public void onSuccess(String playerId) {
								view.moveToDashboard(playerId);
							}

							@Override
							public void onError(Throwable throwable) {
								view.showError(throwable);
								if (!(throwable instanceof WrongLoginPasswordException)) {
									Logger.getInstance().logError(this.getClass(), throwable, "getPlayer");
								}
							}

						}));
	}



	@Override
	public void createAccount(String login, String password, String sponsor) {
		compositeDisposable.add(
				loginDataRepository.createAccount(login, password, sponsor)
						.subscribeOn(Schedulers.io())
						.observeOn(SwtSchedulers.defaultDisplayThread())
						.subscribeWith(new DisposableSingleObserver<String>() {

							@Override
							public void onSuccess(String playerId) {
								view.moveToDashboard(playerId);
							}

							@Override
							public void onError(Throwable throwable) {
								throwable.printStackTrace();
								view.showError(throwable);
								System.out.println("Got error : " + throwable.getMessage());
								if (!(throwable instanceof WrongLoginPasswordException)) {
									Logger.getInstance().logError(this.getClass(), throwable, "createAccount");
								}
							}

						}));
	}




	@Override
	public void registerView(org.eclipse.papyrus.gamification.view.login.LoginContract.View view) {
		this.view = view;
	}


}
