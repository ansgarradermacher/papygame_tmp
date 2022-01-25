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

package org.eclipse.papyrus.gamification.games.framework.entity;

import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClassDiagram;
import org.eclipse.papyrus.gamification.modelutils.papyrus.copy.ClassDiagramCopier;

/**
 * @author maximesavaryleblanc
 *
 */
public class PapyrusContext {

	private PapyrusClassDiagram originalDiagram;
	private PapyrusClassDiagram playerDiagram;
	private ClassDiagramCopier diagramClassCopier;

	/*
	 * private Diagram diagram;
	 * private TransactionalEditingDomain transactionalEditingDomain;
	 * private DiagramCopier diagramCopier;
	 * private ResponseModel repsonseModel;
	 * private PlayerModel playerModel;
	 */

	public PapyrusContext() {
		super();
	}

	public PapyrusClassDiagram getOriginalDiagram() {
		return originalDiagram;
	}

	public void setOriginalDiagram(PapyrusClassDiagram originalDiagram) {
		this.originalDiagram = originalDiagram;
	}

	public PapyrusClassDiagram getPlayerDiagram() {
		return playerDiagram;
	}

	public void setPlayerDiagram(PapyrusClassDiagram playerDiagram) {
		this.playerDiagram = playerDiagram;
	}

	public ClassDiagramCopier getDiagramClassCopier() {
		return diagramClassCopier;
	}

	public void setDiagramClassCopier(ClassDiagramCopier diagramClassCopier) {
		this.diagramClassCopier = diagramClassCopier;
	}


	/*
	 * 
	 * public Diagram getDiagram() {
	 * return diagram;
	 * }
	 * 
	 * public void setDiagram(Diagram diagram) {
	 * this.diagram = diagram;
	 * }
	 * 
	 * public TransactionalEditingDomain getTransactionalEditingDomain() {
	 * return transactionalEditingDomain;
	 * }
	 * 
	 * public void setTransactionalEditingDomain(TransactionalEditingDomain transactionalEditingDomain) {
	 * this.transactionalEditingDomain = transactionalEditingDomain;
	 * }
	 * 
	 * public DiagramCopier getDiagramCopier() {
	 * return diagramCopier;
	 * }
	 * 
	 * public void setDiagramCopier(DiagramCopier diagramCopier) {
	 * this.diagramCopier = diagramCopier;
	 * }
	 * 
	 * public ResponseModel getRepsonseModel() {
	 * return repsonseModel;
	 * }
	 * 
	 * public void setRepsonseModel(ResponseModel repsonseModel) {
	 * this.repsonseModel = repsonseModel;
	 * }
	 * 
	 * public PlayerModel getPlayerModel() {
	 * return playerModel;
	 * }
	 * 
	 * public void setPlayerModel(PlayerModel playerModel) {
	 * this.playerModel = playerModel;
	 * }
	 * 
	 * public Diagram getSourceDiagram() {
	 * return this.diagramCopier.getSourceDiagram();
	 * }
	 * 
	 * public Diagram getTargetDiagram() {
	 * return Diagram.class.cast(this.diagramCopier.getTargetDiagram().getModel());
	 * 
	 * }
	 */

}
