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

package org.eclipse.papyrus.gamification.view.game;

import org.eclipse.papyrus.gamification.games.framework.communication.OnResumeToDashboardItf;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.common.swt.JavascriptFunction;


/**
 * @author maximesavaryleblanc
 *
 */
public class JSResumeToDashboard extends JavascriptFunction {

	Browser browser;
	OnResumeToDashboardItf onResumeToDashboardItf;

	public JSResumeToDashboard(Browser browser, OnResumeToDashboardItf onResumeToDashboardItf) {
		super(browser, "resumeToDashboard");
		this.browser = browser;
		this.onResumeToDashboardItf = onResumeToDashboardItf;
	}

	@Override
	public Object functionBody(Object[] args) {
		try {
			onResumeToDashboardItf.onResumeToDashboard();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

}