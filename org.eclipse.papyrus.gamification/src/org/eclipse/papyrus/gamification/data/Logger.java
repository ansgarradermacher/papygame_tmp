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

package org.eclipse.papyrus.gamification.data;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.papyrus.gamification.data.di.RepositoryFactory;
import org.osgi.framework.FrameworkUtil;

/**
 * @author maximesavaryleblanc
 *
 */
public class Logger {

	private static Logger INSTANCE;
	private String pluginVersion;
	private String javaVersion;
	private String hostname = "Unknown";


	public static Logger getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Logger();
		}
		return INSTANCE;
	}

	private Logger() {
		pluginVersion = FrameworkUtil.getBundle(getClass()).getVersion().toString();
		javaVersion = System.getProperty("os.name") + "-" + System.getProperty("java.version");


		try {
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		} catch (UnknownHostException ex) {
			System.out.println("Hostname can not be resolved");
		}

	}

	public void logError(Class className, Throwable t, String methodName) {

		Map<String, Object> map = new HashMap<>();
		map.put("Version", pluginVersion);
		map.put("HostName", hostname);
		map.put("OS", System.getProperty("os.name"));
		map.put("Java", System.getProperty("java.version"));
		map.put("Origin", className.getName());
		map.put("Method", methodName);

		RepositoryFactory.getRollbar().critical(t, map);

	}

	public void logError(Class className, String t, String methodName) {

		Map<String, Object> map = new HashMap<>();
		map.put("Version", pluginVersion);
		map.put("HostName", hostname);
		map.put("OS", System.getProperty("os.name"));
		map.put("Java", System.getProperty("java.version"));
		map.put("Origin", className.getName());
		map.put("Method", methodName);

		RepositoryFactory.getRollbar().critical(t, map);

	}

	public void logDebug(Class className, Throwable t, String methodName) {
		Map<String, Object> map = new HashMap<>();
		map.put("Version", pluginVersion);
		map.put("HostName", hostname);
		map.put("OS", System.getProperty("os.name"));
		map.put("Java", System.getProperty("java.version"));
		map.put("Origin", className.getName());
		map.put("Method", methodName);

		RepositoryFactory.getRollbar().debug(t, map);

	}

	public void logDebug(Class className, String t, String methodName) {
		Map<String, Object> map = new HashMap<>();
		map.put("Version", pluginVersion);
		map.put("HostName", hostname);
		map.put("OS", System.getProperty("os.name"));
		map.put("Java", System.getProperty("java.version"));
		map.put("Origin", className.getName());
		map.put("Method", methodName);

		RepositoryFactory.getRollbar().debug(t, map);

	}

	public void logSequence(String sequenceName, String stepName) {
		Map<String, Object> map = new HashMap<>();
		map.put("Version", pluginVersion);
		map.put("HostName", hostname);
		map.put("OS", System.getProperty("os.name"));
		map.put("Java", System.getProperty("java.version"));
		map.put("Step", stepName);

		RepositoryFactory.getRollbar().debug(sequenceName, map);

	}

}
