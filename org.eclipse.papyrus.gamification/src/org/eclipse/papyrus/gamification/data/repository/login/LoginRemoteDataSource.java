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

package org.eclipse.papyrus.gamification.data.repository.login;

import java.util.List;

import org.eclipse.papyrus.gamification.Preferences;
import org.eclipse.papyrus.gamification.data.Encryptor;
import org.eclipse.papyrus.gamification.data.api.LoginService;
import org.eclipse.papyrus.gamification.data.api.query.SponsorRequest;
import org.eclipse.papyrus.gamification.data.api.query.UserCreationRequest;
import org.eclipse.papyrus.gamification.data.api.response.GameJson;
import org.eclipse.papyrus.gamification.data.api.response.PlayerListJson;
import org.eclipse.papyrus.gamification.data.api.response.PlayerStatusJson;
import org.eclipse.papyrus.gamification.data.exception.LoginGameIsNotDefined;
import org.eclipse.papyrus.gamification.data.exception.WrongLoginPasswordException;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

/**
 * @author maximesavaryleblanc
 *
 */
public class LoginRemoteDataSource {

	private LoginService loginService;
	private Encryptor encryptor;



	public LoginRemoteDataSource(LoginService loginService, Encryptor encryptor) {
		super();
		this.loginService = loginService;
		this.encryptor = encryptor;
	}

	private Single<List<PlayerStatusJson>> getAllPlayers() {
		return loginService.getAllGames()
				.flatMap(new Function<List<GameJson>, SingleSource<List<PlayerStatusJson>>>() {

					@Override
					public SingleSource<List<PlayerStatusJson>> apply(List<GameJson> gameJsonList) throws Exception {
						System.out.println("Got all games : " + gameJsonList);
						for (GameJson gameJson : gameJsonList) {
							if ("LOGINGAME".equals(gameJson.getName())) {
								return loginService.getPlayersOfGame(gameJson.getId(), 2000)
										.map(new Function<PlayerListJson, List<PlayerStatusJson>>() {

											@Override
											public List<PlayerStatusJson> apply(PlayerListJson playerListJson) throws Exception {
												System.out.println("We got all players : " + playerListJson);
												return playerListJson.getContent();
											}

										});
							}
						}
						return Single.error(new LoginGameIsNotDefined());

					}
				});
	}

	public Maybe<String> getPlayer(String username, String password) {
		return getAllPlayers()
				.flatMapMaybe(new Function<List<PlayerStatusJson>, MaybeSource<String>>() {

					@Override
					public MaybeSource<String> apply(List<PlayerStatusJson> playerStatusJsonList) throws Exception {
						for (PlayerStatusJson playerStatusJson : playerStatusJsonList) {
							System.out.println("Player is : " + playerStatusJson.getPlayerId());
							if (playerStatusJson.getPlayerId().equals(username)) {
								String encryptedPassword = encryptor.encrypt(password);
								if (playerStatusJson.getCustomData().getPassword().equals(encryptedPassword)) {

									return Maybe.just(playerStatusJson.getPlayerId());
								} else {
									return Maybe.error(new WrongLoginPasswordException());
								}
							}
						}
						return Maybe.empty();
					}
				});
	}

	public Single<String> createAccount(String username, String password, String sponsor) {
		return loginService.getAllGames()
				.flatMapCompletable(new Function<List<GameJson>, CompletableSource>() {

					@Override
					public CompletableSource apply(List<GameJson> gameJsonList) throws Exception {
						System.out.println("Got all games : " + gameJsonList);
						for (GameJson gameJson : gameJsonList) {
							if ("LOGINGAME".equals(gameJson.getName())) {
								String encryptedPassword = encryptor.encrypt(password);
								System.out.println("Applying for game : " + gameJson.getName());
								UserCreationRequest userCreationRequest = new UserCreationRequest(
										username,
										gameJson.getId(),
										encryptedPassword);

								return loginService.createPlayerForGame(gameJson.getId(),
										username, userCreationRequest);

							}
						}
						return Completable.error(new LoginGameIsNotDefined());
					}
				})
				// TODO change !!!!
				.mergeWith(getSponsorSource(sponsor, Preferences.PAPYRUS_MODELS_ID, sponsor))
				.andThen(Single.just(username));
	}

	CompletableSource getSponsorSource(String sponsor, String gameId, String playerId) {
		if (sponsor != null) {
			if (!sponsor.isEmpty()) {
				SponsorRequest sponsorRequest = new SponsorRequest(playerId, gameId);
				return loginService.invitePlayer(gameId, Preferences.SPONSOR_ACTION_ID, sponsorRequest);
			}
		}
		return Completable.complete();
	}
}
