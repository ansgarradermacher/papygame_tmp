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

import org.eclipse.papyrus.gamification.data.jsonmapper.UmlClassDiagram;

/**
 * @author maximesavaryleblanc
 *
 */
public class UmlDiagramSolution {
	protected UmlClassDiagram umlClassDiagram;
	protected int timeSpent;

	public UmlClassDiagram getUmlClassDiagram() {
		return umlClassDiagram;
	}

	public void setUmlClassDiagram(UmlClassDiagram umlClassDiagram) {
		this.umlClassDiagram = umlClassDiagram;
	}

	public int getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(int timeSpent) {
		this.timeSpent = timeSpent;
	}



}
