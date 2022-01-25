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

package org.eclipse.papyrus.gamification.data.entity;

import java.util.List;

import com.google.gson.Gson;

/**
 * @author lepallec
 *
 */
public class SeriesFactory {

	static final String HANGMAN_CLASSNAME = "org.eclipse.papyrus.gamification.games.hangman.Hangman";
	static final String OYO_CLASSNAME = "org.eclipse.papyrus.gamification.games.oyo.Oyo";

	public static Series createBachelorSeries() {
		Series series = new Series("Bachelor of Software Engineering", "5f087a49857aba00013178cd");

		List<Level> levels = series.getLevels();
		/*
		 * levels.add(new Level("Level 1", OYO_CLASSNAME, "/responses/Gamification/Library", "Level 1", "Nothing to say", ""));
		 * levels.add(new Level("Level 2", OYO_CLASSNAME, "/responses/Gamification/Library", "Level 1", "Nothing to say", ""));
		 * levels.add(new Level("Level 3", HANGMAN_CLASSNAME, "/responses/Gamification/Library", "Level 2", "Nothing to say", ""));
		 * levels.add(new Level("Level 4", HANGMAN_CLASSNAME, "/responses/Gamification/Library", "Level 2", "Nothing to say", ""));
		 * levels.add(new Level("Level 5", HANGMAN_CLASSNAME, "/responses/Gamification/Library", "Level 2", "Nothing to say", ""));
		 */System.out.println(new Gson().toJson(series));
		return series;
	}

	public static Series createMasterSeries() {
		Series series = new Series("Master of Software Engineering", "5f087a57857aba00013178ce");

		List<Level> levels = series.getLevels();
		/*
		 * levels.add(new Level("Level 1", OYO_CLASSNAME, "/responses/Gamification/Library", "Level 2", // "Level 6",
		 * "On this diagram, please apply the <b>Factory design pattern</b>.<BR> Each type of browser has its own Javascript parser. <BR> As you only need of a parser and a parse method, a factory aims at providing <BR>such a parser whatever the underlying implementation/environment."
		 * ,
		 * "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"));
		 * levels.add(new Level("Level 1bis", HANGMAN_CLASSNAME, "/responses/Gamification/Library", "Level 5",
		 * "On this diagram, please apply the <b>Adapter design pattern</b>.<BR> Each type of fighter has to be able to do front, side and roundhouse kicks.<BR> As in Taekwondo and Karate they respectively perform <b>chagis</b> and <b>geris</b>, we need adapters for them to do <b>kicks</b>.<BR> This level is associated to Hangman game. <b>So DnD operations in their corresponding class</b>."
		 * ,
		 * ""));
		 * levels.add(new Level("Level 3", HANGMAN_CLASSNAME, "/responses/Gamification/Library", "Level 2", "Nothing to say", ""));
		 * levels.add(new Level("Level 4", HANGMAN_CLASSNAME, "/responses/Gamification/Library", "Level 4", "Nothing to say", ""));
		 * levels.add(new Level("Level 5", HANGMAN_CLASSNAME, "/responses/Gamification/Library", "Level 1", "Nothing to say", ""));
		 * levels.add(new Level("Level 6", HANGMAN_CLASSNAME, "/responses/Gamification/Library", "Level 2", "Nothing to say", ""));
		 * levels.add(new Level("Level 7", HANGMAN_CLASSNAME, "/responses/Gamification/Library", "Level 3", "Nothing to say", ""));
		 * levels.add(new Level("Level 8", HANGMAN_CLASSNAME, "/responses/Gamification/Library", "Level 4", "Nothing to say", ""));
		 */
		System.out.println((new Gson()).toJson(series));
		return series;
	}

}
