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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.eclipse.papyrus.gamification.Preferences;
import org.eclipse.papyrus.gamification.data.Encryptor;
import org.eclipse.papyrus.gamification.data.Logger;
import org.eclipse.papyrus.gamification.data.di.RepositoryFactory;
import org.eclipse.papyrus.gamification.data.entity.Level;
import org.eclipse.papyrus.gamification.data.entity.LevelPerformed;
import org.eclipse.papyrus.gamification.data.entity.PlayerProfile;
import org.eclipse.papyrus.gamification.data.entity.SeriesPerformed;
import org.eclipse.papyrus.gamification.games.framework.LevelExecutor;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;
import org.eclipse.papyrus.gamification.view.common.DisplayableView;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.dashboard.JSLevelClicked.LevelClickedInterface;
import org.eclipse.swt.widgets.Display;

import com.google.gson.Gson;

/**
 * @author maximesavaryleblanc
 *
 */
public class DashboardView extends DisplayableView implements DashboardContract.View, LevelClickedInterface {

	private DashboardPresenter dashboardPresenter;
	private String login;
	private PlayerProfile currentPlayerProfile;

	public DashboardView(String login) {
		this.login = login;
	}

	@Override
	public void registerJavaScriptFunctions(Browser browser) {
		super.registerJavaScriptFunctions(browser);
		new JSLevelClicked(browser, this);
	}

	@Override
	public void displayPlayerProfile(PlayerProfile playerProfile) {
		this.currentPlayerProfile = playerProfile;

		System.out.println("DISPLAYPROFILE : " + playerProfile.toString());
		String playerProfileJSON = (new Gson()).toJson(playerProfile);
		System.out.println("displayPlayerProfile(" + playerProfileJSON + ");");
		callJSScript("displayPlayerProfile(" + playerProfileJSON + ");");

	}

	@Override
	public String getHtmlPath() {
		return "/html/newDashboard.html";
	}

	@Override
	public void start() {
		super.start();
		this.dashboardPresenter = new DashboardPresenter();
		this.dashboardPresenter.registerView(this);
		String input = "[{\n" +
				"  \"levels\": [\n" +
				"    {\n" +
				"      \"label\": \"Level 1bis\",\n" +
				"      \"modelPath\": \"/responses/Gamification/Library\",\n" +
				"      \"diagramName\": \"Level 6\",\n" +
				"      \"gameClass\": \"org.eclipse.papyrus.gamification.games.oyo.Oyo\",\n" +
				"      \"statement\": '            For this level, we want to model a simple system for handling payments with gift cards: we want to make customers being able to posses gift cards, and to pay with it.\n" +
				"<br><br>\n" +
				"            Create the class diagram for the following statement:\n" +
				"\n" +
				"            In our system, there could be two kinds of <p class=\"code\">Customers</p> : an <p class=\"code\">Individual</p> or a <p class=\"code\">Company</p>.\n" +
				"\n" +
				"            Every Customer has an <p class=\"code\">id</p>, a <p class=\"code\">registrationDate</p>, and a <p class=\"code\">phoneNumber</p>.\n" +
				"\n" +
				"            Individual has additional <p class=\"code\">firstName</p> and <p class=\"code\">lastName</p> and a Company has a <p class=\"code\">name</p> and a <p class=\"code\">companyRegistrationNumber</p>.\n" +
				"\n" +
				"            Each Customer can have <p class=\"important\">no or several</p> <p class=\"code\">GiftCard</p>.\n" +
				"\n" +
				"            A GiftCard has an <p class=\"code\">id</p>, a <p class=\"code\">balance</p>, and an <p class=\"code\">expirationDate</p>.\n" +
				"\n" +
				"            An Invoice can be paid with <p class=\"important\">zero or more</p> GiftCard and has an <p class=\"code\">amount</p> (a Double). An Invoice can have <p class=\"important\"></p> different state values : <p class=\"code\">PENDING</p>, <p class=\"code\">CANCELED</p>, <p class=\"code\">FAILED</p>, <p class=\"code\">PAID</p>.\n"
				+
				"\n" +
				"            <div class=\"clues\">\n" +
				"                <div class=\"clues-title\">Remember</div>\n" +
				"            Dates have type Date<br>\n" +
				"            Id often are Double<br>\n" +
				"            Specific numbers and names will be String (so 0 are not deleted for numbers).\n" +
				"            </div>',\n"
				+
				"      \"videoToShowUrl\": \"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"label\": \"Level 1\",\n" +
				"      \"modelPath\": \"/responses/Gamification/Library\",\n" +
				"      \"diagramName\": \"Level 5\",\n" +
				"      \"gameClass\": \"org.eclipse.papyrus.gamification.games.hangman.Hangman\",\n" +
				"      \"statement\": \"On this diagram, please apply the \\u003cb\\u003eAdapter design pattern\\u003c/b\\u003e.\\u003cBR\\u003e Each type of fighter has to be able to do front, side and roundhouse kicks.\\u003cBR\\u003e As in Taekwondo and Karate they respectively perform \\u003cb\\u003echagis\\u003c/b\\u003e and \\u003cb\\u003egeris\\u003c/b\\u003e, we need adapters for them to do \\u003cb\\u003ekicks\\u003c/b\\u003e.\\u003cBR\\u003e This level is associated to Hangman game. \\u003cb\\u003eSo DnD operations in their corresponding class\\u003c/b\\u003e.\",\n"
				+
				"      \"videoToShowUrl\": \"\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"label\": \"Level 3\",\n" +
				"      \"modelPath\": \"/responses/Gamification/Library\",\n" +
				"      \"diagramName\": \"Level 2\",\n" +
				"      \"gameClass\": \"org.eclipse.papyrus.gamification.games.hangman.Hangman\",\n" +
				"      \"statement\": \"Nothing to say\",\n" +
				"      \"videoToShowUrl\": \"\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"label\": \"Level 4\",\n" +
				"      \"modelPath\": \"/responses/Gamification/Library\",\n" +
				"      \"diagramName\": \"Level 4\",\n" +
				"      \"gameClass\": \"org.eclipse.papyrus.gamification.games.hangman.Hangman\",\n" +
				"      \"statement\": \"Nothing to say\",\n" +
				"      \"videoToShowUrl\": \"\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"label\": \"Level 5\",\n" +
				"      \"modelPath\": \"/responses/Gamification/Library\",\n" +
				"      \"diagramName\": \"Level 1\",\n" +
				"      \"gameClass\": \"org.eclipse.papyrus.gamification.games.hangman.Hangman\",\n" +
				"      \"statement\": \"Nothing to say\",\n" +
				"      \"videoToShowUrl\": \"\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"label\": \"Level 6\",\n" +
				"      \"modelPath\": \"/responses/Gamification/Library\",\n" +
				"      \"diagramName\": \"Level 2\",\n" +
				"      \"gameClass\": \"org.eclipse.papyrus.gamification.games.hangman.Hangman\",\n" +
				"      \"statement\": \"Nothing to say\",\n" +
				"      \"videoToShowUrl\": \"\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"label\": \"Level 7\",\n" +
				"      \"modelPath\": \"/responses/Gamification/Library\",\n" +
				"      \"diagramName\": \"Level 3\",\n" +
				"      \"gameClass\": \"org.eclipse.papyrus.gamification.games.hangman.Hangman\",\n" +
				"      \"statement\": \"Nothing to say\",\n" +
				"      \"videoToShowUrl\": \"\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"label\": \"Level 8\",\n" +
				"      \"modelPath\": \"/responses/Gamification/Library\",\n" +
				"      \"diagramName\": \"Level 4\",\n" +
				"      \"gameClass\": \"org.eclipse.papyrus.gamification.games.hangman.Hangman\",\n" +
				"      \"statement\": \"Nothing to say\",\n" +
				"      \"videoToShowUrl\": \"\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"name\": \"Master of Software Engineering\",\n" +
				"  \"seriesGameId\": \"5f087a49857aba00013178cd\"\n" +
				"},\n" +
				"  {\n" +
				"    \"levels\": [\n" +
				"      {\n" +
				"        \"label\": \"Level 1\",\n" +
				"        \"modelPath\": \"/responses/Gamification/Library\",\n" +
				"        \"diagramName\": \"Level 6\",\n" +
				"        \"gameClass\": \"org.eclipse.papyrus.gamification.games.oyo.Oyo\",\n" +
				"        \"statement\": \"On this diagram, please apply the \\u003cb\\u003eFactory design pattern\\u003c/b\\u003e.\\u003cBR\\u003e Each type of browser has its own Javascript parser. \\u003cBR\\u003e As you only need of a parser and a parse method, a factory aims at providing \\u003cBR\\u003esuch a parser whatever the underlying implementation/environment.\",\n"
				+
				"        \"videoToShowUrl\": \"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"label\": \"Level 1bis\",\n" +
				"        \"modelPath\": \"/responses/Gamification/Library\",\n" +
				"        \"diagramName\": \"Level 5\",\n" +
				"        \"gameClass\": \"org.eclipse.papyrus.gamification.games.hangman.Hangman\",\n" +
				"        \"statement\": \"On this diagram, please apply the \\u003cb\\u003eAdapter design pattern\\u003c/b\\u003e.\\u003cBR\\u003e Each type of fighter has to be able to do front, side and roundhouse kicks.\\u003cBR\\u003e As in Taekwondo and Karate they respectively perform \\u003cb\\u003echagis\\u003c/b\\u003e and \\u003cb\\u003egeris\\u003c/b\\u003e, we need adapters for them to do \\u003cb\\u003ekicks\\u003c/b\\u003e.\\u003cBR\\u003e This level is associated to Hangman game. \\u003cb\\u003eSo DnD operations in their corresponding class\\u003c/b\\u003e.\",\n"
				+
				"        \"videoToShowUrl\": \"\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"label\": \"Level 3\",\n" +
				"        \"modelPath\": \"/responses/Gamification/Library\",\n" +
				"        \"diagramName\": \"Level 2\",\n" +
				"        \"gameClass\": \"org.eclipse.papyrus.gamification.games.hangman.Hangman\",\n" +
				"        \"statement\": \"Nothing to say\",\n" +
				"        \"videoToShowUrl\": \"\"\n" +
				"      }\n" +
				"    ],\n" +
				"    \"name\": \"Ma serie a moi\",\n" +
				"    \"seriesGameId\": \"5f087a57857aba00013178cd\"\n" +
				"  }\n" +
				"]";
		// System.out.println("Series : " + input);
	}

	@Override
	public void onLevelClicked(String seriesLabel, String levelLabel) {
		// TODO : improve selection ?
		System.out.println("onLevelClicked(" + seriesLabel + "," + levelLabel + ")");
		Level levelToRun = null;

		// TODO DELETE
		// levelLabel = "GREEN BELT";
		for (SeriesPerformed seriesPerformed : currentPlayerProfile.getSeriesPerformed()) {
			System.out.println("Series Performed name : " + seriesPerformed.getName());
			if (seriesPerformed.getName().equals(seriesLabel)) {
				for (LevelPerformed level : seriesPerformed.getLevelsPerformed()) {
					System.out.println("level name : " + level.getLabel());
					if (level.getLabel().equals(levelLabel)) {
						LevelContext levelContext = new LevelContext(level, currentPlayerProfile, seriesPerformed);
						// LevelContext levelContext = null;
						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run() {
								try {
									LevelExecutor.getInstance().start(levelContext);
								} catch (Exception e) {
									e.printStackTrace();
									Logger.getInstance().logError(this.getClass(), e, "onLevelClicked");
								}
							}
						});
						// ViewManager.getInstance().displayView(new LevelView(levelContext));
						return;
					}
				}
			}
		}

	}


	@Override
	public void showVideo() {
		initiateVideo(Preferences.INTRO_VIDEO_URL);
		// TODO Auto-generated method stub
	}

	@Override
	public void skipVideo() {
		callJSScript("showMainContainer()");
	}



	@Override
	public void clearJavascriptFunctions(Browser browser) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onHtmlPageLoaded(Browser browser) {
		System.out.println("page loaded on dashboard");

		this.dashboardPresenter.getPlayerProfile(login);
		// this.dashboardPresenter.getVideoIntroductionState(login);
		skipVideo();
	}

	@Override
	public void onOpenLink(String url) {
		if ("questionnaire".equals(url)) {
			Encryptor encryptor = RepositoryFactory.getEncryptor();
			try {
				openLinkInExternalBrowser(Preferences.PAPY_QUESTIONNAIRE_URL + "?key=" +
						URLEncoder.encode(
								encryptor.encrypt(currentPlayerProfile.getPlayerId()),
								StandardCharsets.UTF_8.toString()));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not encode the URL key parameter");
				e.printStackTrace();
			}
		}
		if ("prize".equals(url)) {
			Encryptor encryptor = RepositoryFactory.getEncryptor();
			try {
				openLinkInExternalBrowser(Preferences.PAPY_PRIZE_URL + "?key=" +
						URLEncoder.encode(
								encryptor.encrypt(currentPlayerProfile.getPlayerId()),
								StandardCharsets.UTF_8.toString()));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not encode the URL key parameter");
				e.printStackTrace();
			}
		} else {
			openLinkInExternalBrowser(url);
		}
	}



}
