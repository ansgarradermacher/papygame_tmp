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

package org.eclipse.papyrus.gamification.modelutils;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.gamification.data.entity.Level;
import org.eclipse.papyrus.gamification.games.framework.entity.Game;
import org.eclipse.papyrus.gamification.games.framework.entity.PapyrusContext;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClassDiagram;
import org.eclipse.papyrus.gamification.modelutils.papyrus.copy.ClassDiagramCopier;
import org.eclipse.papyrus.junit.utils.ModelUtils;
import org.eclipse.papyrus.uml.diagram.clazz.UmlClassDiagramForMultiEditor;
import org.eclipse.ui.PlatformUI;



/**
 * @author maximesavaryleblanc
 *
 */
public class ModelDisplayManager {

	public static PapyrusContext openAndGenerateLevelModel(ResponseModel responseModel, Game game, Level level, String projectName) {
		PlayerModel playerModel = new PlayerModel(projectName);


		// VIEW -----------------

		// create a sandbox diagram to play
		Diagram diagram = playerModel.createDiagram(level.getDiagramName());// level name


		// open the player model in Eclipse and in Papyrus

		UmlClassDiagramForMultiEditor diagramEditor = playerModel.openInEditor();

		// fetch the edit part to create elements in it
		DiagramEditPart diagramEditPart = ((IDiagramWorkbenchPart) diagramEditor).getDiagramEditPart();


		TransactionalEditingDomain transactionalEditingDomain = ModelUtils.getEditingDomain(playerModel.getModelSet());

		PapyrusClassDiagram playerDiagram = new PapyrusClassDiagram(transactionalEditingDomain, diagramEditPart);



		PapyrusContext papyrusContext = new PapyrusContext();

		papyrusContext.setPlayerDiagram(playerDiagram);

		if (responseModel != null) {
			// load the response diagram of this level
			Diagram levelDiagram = null;

			if ((level.getDiagramToLoadName() != null) && !level.getDiagramToLoadName().isEmpty()) {
				System.out.println("Diagram to load is : " + level.getDiagramToLoadName());
				levelDiagram = responseModel.getDiagram(level.getDiagramToLoadName());
			} else {
				levelDiagram = responseModel.getDiagram(level.getDiagramName());
			}
			PapyrusClassDiagram originalDiagram = new PapyrusClassDiagram(levelDiagram);
			ClassDiagramCopier diagramClassCopier = game.getClassDiagramCopier(originalDiagram, playerDiagram);
			diagramClassCopier.copy();
			papyrusContext.setDiagramClassCopier(diagramClassCopier);
			papyrusContext.setOriginalDiagram(originalDiagram);
		}




		return papyrusContext;
	}

	public static void saveEditor() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().saveAllEditors(false);
	}
}
