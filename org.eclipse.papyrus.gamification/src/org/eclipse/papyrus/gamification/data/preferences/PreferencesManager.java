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

package org.eclipse.papyrus.gamification.data.preferences;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.google.gson.Gson;

/**
 * @author maximesavaryleblanc
 *
 */
public class PreferencesManager {

	private Preferences sub;
	private Gson gson;
	private Preferences generalPreferences;

	public PreferencesManager() {
		Preferences generalPreferences = ConfigurationScope.INSTANCE
				.getNode("org.eclipse.papyrus.gamification");

		this.sub = generalPreferences.node("game");
		this.gson = new Gson();
	}


	public void setVideoShown(String playerId) throws BackingStoreException {
		sub.put("videoShown-" + playerId, "1");
		System.out.println("Pref : " + generalPreferences);
		System.out.println("Sub : " + sub);
		// forces the application to save the preferences
		sub.flush();

		System.out.println("AFTERSOTRING : " + sub.get("videoShown-" + playerId, "0"));

	}

	public boolean hasVideoBeenDisplayed(String playerId) {
		return sub.get("videoShown-" + playerId, "0").equals("1");
	}
}
