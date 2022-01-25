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

package org.eclipse.papyrus.gamification.data.di;

import static com.rollbar.notifier.config.ConfigBuilder.withAccessToken;

import org.apache.commons.codec.DecoderException;
import org.eclipse.papyrus.gamification.Preferences;
import org.eclipse.papyrus.gamification.data.Encryptor;
import org.eclipse.papyrus.gamification.data.api.AuthenticationInterceptor;
import org.eclipse.papyrus.gamification.data.api.GameManagerService;
import org.eclipse.papyrus.gamification.data.api.LoginService;
import org.eclipse.papyrus.gamification.data.preferences.PreferencesManager;
import org.eclipse.papyrus.gamification.data.repository.dashboard.DashboardDataRepository;
import org.eclipse.papyrus.gamification.data.repository.dashboard.DashboardLocalDataSource;
import org.eclipse.papyrus.gamification.data.repository.dashboard.DashboardRemoteDataSource;
import org.eclipse.papyrus.gamification.data.repository.level.LevelDataRepository;
import org.eclipse.papyrus.gamification.data.repository.level.LevelLocalDataSource;
import org.eclipse.papyrus.gamification.data.repository.level.LevelRemoteDataSource;
import org.eclipse.papyrus.gamification.data.repository.login.LoginDataRepository;
import org.eclipse.papyrus.gamification.data.repository.login.LoginRemoteDataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author maximesavaryleblanc
 *
 */
public class RepositoryFactory {

	private static DashboardDataRepository dashboardDataRepository;
	private static LevelDataRepository levelDataRepository;
	private static LoginDataRepository loginDataRepository;
	private static PreferencesManager preferencesManager;
	private static GameManagerService gameManagerService;
	private static LoginService loginService;
	private static Encryptor encryptor;
	private static Rollbar rollbar;

	private static Retrofit retrofit;


	public synchronized static DashboardDataRepository getDashboardDataRepository() {
		if (dashboardDataRepository == null) {
			dashboardDataRepository = new DashboardDataRepository(
					new DashboardLocalDataSource(getPreferencesManager()),
					new DashboardRemoteDataSource(getGameManagerService()));
		}
		return dashboardDataRepository;
	}

	public synchronized static LevelDataRepository getLevelDataRepository() {
		if (levelDataRepository == null) {
			levelDataRepository = new LevelDataRepository(
					new LevelLocalDataSource(getPreferencesManager()),
					new LevelRemoteDataSource(getGameManagerService()));
		}
		return levelDataRepository;
	}

	public synchronized static LoginDataRepository getLoginDataRepository() {
		if (loginDataRepository == null) {
			loginDataRepository = new LoginDataRepository(
					new LoginRemoteDataSource(getLoginService(), getEncryptor()));
		}
		return loginDataRepository;
	}

	public synchronized static Encryptor getEncryptor() {
		if (encryptor == null) {
			try {
				encryptor = new Encryptor();
			} catch (DecoderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return encryptor;
	}

	public synchronized static PreferencesManager getPreferencesManager() {
		if (preferencesManager == null) {
			preferencesManager = new PreferencesManager();
		}
		return preferencesManager;
	}

	public synchronized static GameManagerService getGameManagerService() {
		if (gameManagerService == null) {
			gameManagerService = getRetrofit().create(GameManagerService.class);
			// gameManagerService = new MockedService();
		}
		return gameManagerService;
	}

	public synchronized static LoginService getLoginService() {
		if (loginService == null) {
			loginService = getRetrofit().create(LoginService.class);
		}
		return loginService;
	}


	public synchronized static Retrofit getRetrofit() {
		if (retrofit == null) {

			AuthenticationInterceptor authInterceptor = new AuthenticationInterceptor(
					Credentials.basic(Preferences.AUTH_USERNAME, Preferences.AUTH_PASSWORD));
			HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
			interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
			OkHttpClient client = new OkHttpClient.Builder()
					.addInterceptor(interceptor)
					.addInterceptor(authInterceptor)
					.build();

			final Gson responseGson = new GsonBuilder()
					.setLenient()
					.enableComplexMapKeySerialization()
					.create();


			retrofit = new Retrofit.Builder()
					.baseUrl(Preferences.BASE_URL)
					.client(client)
					.addConverterFactory(GsonConverterFactory.create(responseGson))
					.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
					.build();
			return retrofit;
		}
		return retrofit;
	}


	public synchronized static Rollbar getRollbar() {
		if (rollbar == null) {

			Config config = withAccessToken("167c079ecbb84dbca2bed285dbd16d27")
					.environment("production")
					.codeVersion(Preferences.VERSION)
					.build();

			rollbar = Rollbar.init(config);
		}
		return rollbar;

	}


}
