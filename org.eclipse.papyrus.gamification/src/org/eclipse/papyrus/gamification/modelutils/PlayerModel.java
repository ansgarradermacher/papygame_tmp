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

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.commands.ICreationCommand;
import org.eclipse.papyrus.infra.architecture.commands.IModelCreationCommand;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ExtensionServicesRegistry;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.core.utils.DiResourceSet;
import org.eclipse.papyrus.infra.ui.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.junit.utils.EditorUtils;
import org.eclipse.papyrus.uml.diagram.clazz.CreateClassDiagramCommand;
import org.eclipse.papyrus.uml.diagram.clazz.UmlClassDiagramForMultiEditor;
import org.eclipse.papyrus.uml.diagram.common.commands.CreateUMLModelCommand;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.ui.IEditorPart;
import org.eclipse.uml2.uml.Model;

/**
 * @author lepallec
 *
 */
public class PlayerModel {

	private static final String NOTATION_SUFFIX = ".notation";
	private static final String MODEL_SUFFIX = ".uml";
	private static final String DIAGRAM_SUFFIX = ".di";

	private ModelSet modelSet;
	private Model model;
	private IFile modelPathDi;
	private IFile modelPathNotation;
	private IFile modelPathUml;
	private UmlClassDiagramForMultiEditor diagramEditor;
	private IEditorPart editor = null;

	// private static final String MODEL_NAME = "papygame/LevelProject";
	public static final String PROJECT_NAME = "papygame";

	static PlayerModel INSTANCE = null;

	/*
	 * public static PlayerModel getInstance() {
	 *
	 * if (INSTANCE == null) {
	 * INSTANCE = new PlayerModel(PROJECT_NAME, MODEL_NAME);
	 * }
	 *
	 * return INSTANCE;
	 * }
	 *
	 * public static void deleteInstance() {
	 * System.out.println("maman " + getInstance().editor);
	 * INSTANCE = null;
	 *
	 * }
	 */

	public PlayerModel(String modelName) {
		this(PROJECT_NAME, modelName);
		System.out.println("Creating player model with name : " + modelName);
	}

	@SuppressWarnings("deprecation")
	private PlayerModel(String projectPath, String modelPath) {
		try {
			modelSet = new DiResourceSet();
			IProject nouveauProjet = ResourcesPlugin.getWorkspace().getRoot().getProject(projectPath);

			if (!nouveauProjet.exists()) {
				nouveauProjet.create(new NullProgressMonitor());
				// nouveauProjet.delete(false, new NullProgressMonitor());
			}

			nouveauProjet.open(new NullProgressMonitor());

			String modelName = modelPath;

			modelPathDi = nouveauProjet.getFile(modelName + DIAGRAM_SUFFIX);

			modelSet.createsModels(modelPathDi);

			modelPathNotation = nouveauProjet.getFile(modelName + NOTATION_SUFFIX);
			modelPathUml = nouveauProjet.getFile(modelName + MODEL_SUFFIX);

			try {
				ServicesRegistry registry = new ExtensionServicesRegistry(
						org.eclipse.papyrus.infra.core.Activator.PLUGIN_ID);

				registry.add(ModelSet.class, Integer.MAX_VALUE, modelSet);
				registry.startRegistry();
			} catch (Exception ex) {
				// ignore
			}

			// create the UML model
			TransactionalEditingDomain ted = modelSet.getTransactionalEditingDomain();
			ted.getCommandStack().execute(new RecordingCommand(ted) {
				@Override
				protected void doExecute() {
					IModelCreationCommand creationCommand = new CreateUMLModelCommand();
					creationCommand.createModel(modelSet);
				}
			});

			// get the root UML Model
			UmlModel umlModel = (UmlModel) modelSet.getModel(UmlModel.MODEL_ID);
			model = (Model) umlModel.lookupRoot();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public Diagram createDiagram(String diagramName) {
		try {
			ICreationCommand diagramCreationCommand = new CreateClassDiagramCommand();
			Diagram diagram = diagramCreationCommand.createDiagram(modelSet, model, diagramName);
			modelSet.save(new NullProgressMonitor());
			return diagram;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public UmlClassDiagramForMultiEditor openInEditor() {
		try {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IFile diagramFile = modelPathDi;// root.getFile(new Path("/NouveauProjet/exercise/ProjetDeTest.di"));
			IMultiDiagramEditor multiEditor = EditorUtils.openPapyrusEditor(diagramFile);
			editor = multiEditor.getActiveEditor();
			editor.setFocus();
			diagramEditor = (UmlClassDiagramForMultiEditor) editor;
			return diagramEditor;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public ModelSet getModelSet() {
		return modelSet;
	}

	public static URI getUserProjectUri() {
		System.out.println("Looking for project " + PROJECT_NAME);
		return ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME).getLocationURI();
	}

}
