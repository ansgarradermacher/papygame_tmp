/*****************************************************************************
 * Copyright (c) 2021 CEA LIST and others.
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

package org.eclipse.papyrus.gamification.view.common.swt;

import org.eclipse.papyrus.gamification.data.Logger;
import org.eclipse.papyrus.gamification.view.common.OsCheck;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Composite;

/**
 * @author maxim
 *
 */
public class Browser {

	private org.eclipse.swt.browser.Browser defaultBrowser;

	/**
	 * Constructor.
	 *
	 */
	public Browser(Composite parent) {
		defaultBrowser = new org.eclipse.swt.browser.Browser(parent, SWT.NONE);
		Logger.getInstance().logDebug(getClass(), "Browser", "Created browser for " + OsCheck.getOperatingSystemType());
	}


	public void setBounds(int x, int y, int width, int height) {
		defaultBrowser.setBounds(x, y, width, height);
	}

	public void setFocus() {
		defaultBrowser.setFocus();
	}

	public void addProgressListener(ProgressListener progressListener) {
		defaultBrowser.addProgressListener(progressListener);
	}

	public Object evaluate(String script) {
		return defaultBrowser.evaluate(script);
	}

	public Object execute(String script) {
		return defaultBrowser.execute(script);
	}

	public void setText(String content) {
		defaultBrowser.setText(content);
	}

	public String getText() {
		return defaultBrowser.getText();
	}


	/**
	 * @return the defaultBrowser
	 */
	public org.eclipse.swt.browser.Browser getDefaultBrowser() {
		return defaultBrowser;
	}
}
