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

package org.eclipse.papyrus.gamification.games.framework;

import org.eclipse.papyrus.gamification.data.entity.SeriesFactory;
import org.eclipse.papyrus.gamification.games.framework.entity.Game;
import org.eclipse.papyrus.gamification.games.framework.exception.GameLoadingException;

/**
 * @author maximesavaryleblanc
 *
 */
public class GameLoader {

	private static Class loadClass(String className) {
		ClassLoader classLoader = SeriesFactory.class.getClassLoader();
		try {
			return classLoader.loadClass(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Game loadGame(String className) throws GameLoadingException {
		try {
			Class gameClass = loadClass(className);
			Object instance;
			instance = gameClass.newInstance();
			return Game.class.cast(instance);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new GameLoadingException(className);
		}
	}

}
