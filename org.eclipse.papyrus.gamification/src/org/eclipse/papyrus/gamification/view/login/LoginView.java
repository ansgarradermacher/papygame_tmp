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

import org.eclipse.papyrus.gamification.data.exception.WrongLoginPasswordException;
import org.eclipse.papyrus.gamification.view.ViewManager;
import org.eclipse.papyrus.gamification.view.common.DisplayableView;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.common.swt.JSPrint;
import org.eclipse.papyrus.gamification.view.login.JSSubmitLogin.LoginSubmitInterface;

/**
 * @author maximesavaryleblanc
 *
 */
public class LoginView extends DisplayableView implements LoginSubmitInterface, LoginContract.View {

	LoginPresenter loginPresenter;

	@Override
	public void registerJavaScriptFunctions(Browser browser) {
		super.registerJavaScriptFunctions(browser);
		new JSPrint(browser);
		new JSSubmitLogin(browser, this);
		new JSLogBrowserJava(browser);
	}

	@Override
	public String getHtmlPath() {
		return "/html/login2.html";
	}

	@Override
	public void onLoginSubmitted(String login, String password) {
		// TODO Auto-generated method stub
		System.out.println("Loging with " + login + " - " + password);
		System.out.println("Presenter is " + loginPresenter);
		loginPresenter.getPlayer(login, password);

	}

	@Override
	public void onCreateAccountSubmitted(String login, String password, String confirmPassword, String sponsor) {
		// TODO Auto-generated method stub
		System.out.println("Creating with " + login + " - " + password + " - " + confirmPassword);
		loginPresenter.createAccount(login, password, sponsor);

	}


	@Override
	public void moveToDashboard(String playerId) {
		// TODO Auto-generated method stub
		ViewManager.getInstance().displayDashboard(playerId);
	}

	@Override
	public void showError(Throwable throwable) {
		// TODO Auto-generated method stub
		/*
		 * if (throwable instanceof WrongLoginPasswordException) {
		 *
		 * callJSScript("showErrorMatchingPasswords()");
		 * }
		 */
		if (throwable instanceof WrongLoginPasswordException) {
			callJSScript("showErrorCredentialsIncorrect()");
		}
	}

	@Override
	public void proposeAccountCreation() {
		// TODO Auto-generated method stub
		callJSScript("proposeAccount()");

	}

	@Override
	public void start() {
		System.out.println("From START in LoginView");
		try {
			this.loginPresenter = new LoginPresenter();
			this.loginPresenter.registerView(this);
		} catch (Exception e) {
			System.out.println("Error in start() from LoginView");
			e.printStackTrace();
		}
	}

	@Override
	public void clearJavascriptFunctions(Browser browser) {
		// TODO

	}

	@Override
	public void stop() {
		// Do nothing
	}

	/**
	 * @see org.eclipse.papyrus.gamification.view.common.DisplayableView#onHtmlPageLoaded(org.eclipse.swt.browser.Browser)
	 *
	 * @param browser
	 */
	@Override
	public void onHtmlPageLoaded(Browser browser) {
		// TODO Auto-generated method stub
	}


}
