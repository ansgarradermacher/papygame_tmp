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

/**
 * @author lepallec
 *
 */
public class Level {
	protected String label;
	protected String modelPath;
	protected String diagramName;
	protected String diagramToLoadName;
	protected String gameClass;
	protected String statement;
	protected String videoToShowUrl;
	protected String icon;

	public Level(String label, String gameClass, String modelPath, String diagramName, String diagramToLoadName, String statement, String videoToShowUrl, String icon) {
		this.label = label;
		this.gameClass = gameClass;
		this.modelPath = modelPath;
		this.diagramToLoadName = diagramToLoadName;
		this.diagramName = diagramName;
		this.statement = statement;
		this.videoToShowUrl = videoToShowUrl;
		this.icon = icon;
	}

	/**
	 * @return the statement
	 */
	public String getStatement() {
		return statement;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 *
	 * @return
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Level other = (Level) obj;
		if (label == null) {
			if (other.label != null) {
				return false;
			}
		} else if (!label.equals(other.label)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the modelFileName
	 */
	public String getModelPath() {
		return modelPath;
	}

	/**
	 * @return the diagramName
	 */
	public String getDiagramName() {
		return diagramName;
	}

	public String getGameClass() {
		return gameClass;
	}

	public String getVideoToShowUrl() {
		return videoToShowUrl;
	}

	public String getDiagramToLoadName() {
		return diagramToLoadName;
	}

	@Override
	public String toString() {
		return "Level [label=" + label + ", modelPath=" + modelPath + ", diagramName=" + diagramName + ", diagramToLoadName=" + diagramToLoadName + ", gameClass=" + gameClass + ", statement=" + statement + ", videoToShowUrl=" + videoToShowUrl + "]";
	}

	public String getIcon() {
		return icon;
	}


}

