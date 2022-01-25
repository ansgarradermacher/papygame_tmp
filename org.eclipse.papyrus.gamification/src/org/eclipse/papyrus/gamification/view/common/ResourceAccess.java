/*****************************************************************************
 * Copyright (c) 2019 CEA LIST.
 *
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Xavier Le Pallec (for CEA LIST) xlepallec@lilo.org - Bug 558456
 *
 *****************************************************************************/

package org.eclipse.papyrus.gamification.view.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import org.eclipse.papyrus.gamification.Activator;



/**
 * This class is just used to load files in the plug-in jar file. See the getContentOfFileLocatedInTheJar method.
 *
 */
public class ResourceAccess {

	private static final Logger LOGGER = Logger.getLogger(ResourceAccess.class.getName());

	private ResourceAccess() {

	}

	/**
	 * This methods loads a file in a the plug-in jar file.
	 *
	 * @param nameFile
	 *            the full name of the file.
	 * @return the content of the file.
	 */
	public static String getContentOfFileLocatedInTheJar(String nameFile) {

		URL url;
		StringBuilder fileContent = new StringBuilder("");

		URL url2;
		try {
			url2 = new URL("platform:/plugin/" + Activator.PLUGIN_ID + nameFile);
			InputStream inputStream = url2.openConnection().getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				fileContent.append(inputLine + "\n");
			}

			in.close();
			return fileContent.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
