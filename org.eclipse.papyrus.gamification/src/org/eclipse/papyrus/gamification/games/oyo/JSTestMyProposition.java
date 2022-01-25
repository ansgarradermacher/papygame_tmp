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

package org.eclipse.papyrus.gamification.games.oyo;

import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.common.swt.JavascriptFunction;

/**
 * @author lepallec
 *
 */
public class JSTestMyProposition extends JavascriptFunction {

	Browser browser;
	TestMyPropositionInterface testPropositionItf;

	public interface TestMyPropositionInterface {
		public void userAsksToTest();
	}

	public JSTestMyProposition(Browser browser, TestMyPropositionInterface testPropositionItf) {
		super(browser, "testMyProposition");
		this.browser = browser;
		this.testPropositionItf = testPropositionItf;
	}

	@Override
	public Object functionBody(Object[] args) {
		try {
			testPropositionItf.userAsksToTest();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "";
	}

}
