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

package org.eclipse.papyrus.gamification.view;

import org.eclipse.papyrus.gamification.view.common.DisplayableView;
import org.eclipse.papyrus.gamification.view.common.ResourceAccess;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.dashboard.DashboardView;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPage;


public class ViewManager {

	private static ViewManager instance;
	private Browser browser;
	private DisplayableView displayedView;
	private String playerId;

	private boolean browserHasCompleted = false;
	private boolean viewIsNotifiedLoaded = false;
	private Object lock = new Object();
	private boolean isFirstComplete = true;

	public static ViewManager getInstance() {
		return instance;
	}

	public ViewManager(Browser browser) {
		this.browser = browser;
		instance = this;
		switchToDashboardDisplay();

		browser.addProgressListener(new ProgressListener() {

			@Override
			public void completed(ProgressEvent event) {
				if (isFirstComplete) {
					browser.execute("logBrowser()");
					isFirstComplete = false;
				}
				synchronized (lock) {
					if (!viewIsNotifiedLoaded) {
						// TODO Auto-generated method stub
						System.out.println("Browser has completed : " + event);
						displayedView.onHtmlPageLoaded(browser);

						browserHasCompleted = true;
						viewIsNotifiedLoaded = true;
					}
				}
			}

			@Override
			public void changed(ProgressEvent event) {
				synchronized (lock) {
					// TODO Auto-generated method stub
					System.out.println("Browser has changed : " + event);
					browserHasCompleted = false;
					viewIsNotifiedLoaded = false;
				}
			}
		});
	}

	public void displayView(DisplayableView viewToDisplay) {
		System.out.println("Step A - Registering JS");
		viewToDisplay.registerJavaScriptFunctions(browser);
		System.out.println("Step B - Load in browser");
		loadInBrowser(browser, viewToDisplay.getHtmlPath());
		System.out.println("Step C - Calling start from login");
		viewToDisplay.start();
		System.out.println("Step D - After start");

		if (displayedView != null) {
			displayedView.clearJavascriptFunctions(browser);
			displayedView.stop();
		}

		displayedView = viewToDisplay;

		synchronized (lock) {
			if (browserHasCompleted) {
				if (!viewIsNotifiedLoaded) {
					displayedView.onHtmlPageLoaded(browser);
					viewIsNotifiedLoaded = true;
				}
			}
		}

	}

	public void displayDashboard(String playerId) {
		this.playerId = playerId;
		switchToDashboardDisplay();
		displayView(new DashboardView(playerId));
	}

	public void resumeToDashboard() {
		displayDashboard(playerId);
	}

	private void loadInBrowser(Browser browser, String nameFile) {
		// System.out.println("Before loading in browser : " + browser + " / " + nameFile);
		String fileContent = ResourceAccess.getContentOfFileLocatedInTheJar(nameFile);
		browser.setText(fileContent);
		// System.out.println("Finished loading in browser : " + fileContent);
	}

	private void switchToDashboardDisplay() {
		WorkbenchPage page = (WorkbenchPage) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorReference[] editorReferences = page.getEditorReferences();
		System.out.println(editorReferences.length);

		// CLosing editors
		if (editorReferences.length != 0) {
			System.out.println("In editrr");
			for (IEditorReference ref : editorReferences) {
				System.out.println("Hangman name" + ref.getName());
				System.out.println("Hangman id" + ref.getId());
				System.out.println("Hangman title" + ref.getTitle());
				page.setPartState(ref, IWorkbenchPage.STATE_MINIMIZED);
				page.closeEditor(ref);
			}
		}

		// Closing views and maximizing current view
		System.out.println("view references" + page.getViewReferences());

		for (IViewReference ref : page.getViewReferences()) {
			System.out.println("->" + ref.getId());
			System.out.println("->" + ref.getPartName());
			System.out.println("->" + ref.getTitle());
			System.out.println("->" + ref.getView(false));
			System.out.println("------");

			if (ref != null) {
				if (!ref.getId().equals("org.eclipse.papyrus.gamification.view")) {
					System.out.println("Minimizing");
					page.setPartState(ref, IWorkbenchPage.STATE_MINIMIZED);
				}
			}
		}
		for (IViewReference ref : page.getViewReferences()) {
			System.out.println("->" + ref.getId());
			System.out.println("->" + ref.getPartName());
			System.out.println("->" + ref.getTitle());
			System.out.println("->" + ref.getView(false));
			System.out.println("------");

			if (ref != null) {
				if (ref.getId().equals("org.eclipse.papyrus.gamification.view")) {
					System.out.println("Maximizing");
					page.setPartState(ref, IWorkbenchPage.STATE_MAXIMIZED);
				}
			}
		}
	}
}
