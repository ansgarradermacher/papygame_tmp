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

package org.eclipse.papyrus.gamification.games.framework.communication;

import org.eclipse.papyrus.gamification.data.entity.GameMetrics;
import org.eclipse.papyrus.gamification.data.entity.UmlDiagramSolution;

/**
 * @author maximesavaryleblanc
 *
 */
public interface OnGameEndedItf {

	void onGameEnded(GameMetrics gameMetrics);

	void onGameEnded(UmlDiagramSolution umlDiagramSolution);

}
