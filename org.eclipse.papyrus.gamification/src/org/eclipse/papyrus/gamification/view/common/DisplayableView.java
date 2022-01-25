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

package org.eclipse.papyrus.gamification.view.common;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.papyrus.gamification.Preferences;
import org.eclipse.papyrus.gamification.games.framework.communication.OnOpenLinkItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnVideoFinishedItf;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.common.swt.JSOpenLink;
import org.eclipse.papyrus.gamification.view.game.JSVideoFinished;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * @author maximesavaryleblanc
 *
 */
public abstract class DisplayableView implements OnVideoFinishedItf, OnOpenLinkItf {

	protected Browser browser;

	public void registerJavaScriptFunctions(Browser browser) {
		this.browser = browser;
		new JSVideoFinished(browser, this);
		new JSOpenLink(browser, this);
	}

	abstract public String getHtmlPath();

	public void start() {
		// Do Nothing
	};

	abstract public void clearJavascriptFunctions(Browser browser);

	public void stop() {
		// Do Nothing
	};

	/**
	 * @param browser
	 */
	abstract public void onHtmlPageLoaded(Browser browser);

	protected void initiateVideo(String videoUrl) {
		System.out.println("Video : " + videoUrl);
		if ((videoUrl == null) || (videoUrl.isEmpty())) {
			onVideoFinished();
		} else {
			callJSScript("showVideo(\"" + videoUrl + "\", \"" + Preferences.INTRO_VIDEO_PLACEHOLDER + "\");");
		}
	}


	@Override
	public void onVideoFinished() {
		// Do Nothing
	}

	protected Object callJSScript(String script, boolean isResultExpected) {
		try {
			if (isResultExpected) {
				browser.evaluate(script);
				System.out.println("Calling JS evaluate : " + script);
				System.out.println("Result is : " + browser.evaluate(script));

				return browser.evaluate(script);
			} else {
				System.out.println("Calling JS execute : " + script);
				return browser.execute(script);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : " + e);
		}
		return null;
	}

	protected Object callJSScript(String script) {
		return callJSScript(script, false);
	}


	@Override
	public void onOpenLink(String url) {
		openLinkInExternalBrowser(url);
	}

	protected void openLinkInExternalBrowser(String url) {
		try {
			PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(url));
		} catch (PartInitException | MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
