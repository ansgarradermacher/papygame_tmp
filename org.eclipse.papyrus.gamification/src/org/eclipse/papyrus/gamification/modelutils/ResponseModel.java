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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.gamification.Activator;
import org.eclipse.papyrus.infra.core.resource.ModelMultiException;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.junit.utils.DiagramUtils;
import org.eclipse.papyrus.junit.utils.ModelUtils;


/**
 * @author lepallec
 *
 */
public class ResponseModel {

	private static final String NOTATION_SUFFIX = ".notation";
	private static final String MODEL_SUFFIX = ".uml";
	private static final String DIAGRAM_SUFFIX = ".di";
	private ModelSet responsesModelSet;
	/*
	 * private String folder;
	 * private String baseFile;
	 */
	private static final String CACHE_FOLDER = "Gamification";
	private static final String CACHE_FILENAME_WITHOUT_EXT = "CacheModel";

	private static final String[] EXTENSIONS = { "uml", "notation", "di" };

	String pathFileNameWithoutExtension;

	public ResponseModel(String pathFileNameWithoutExtension, boolean isReponseModelInternal) {

		this.pathFileNameWithoutExtension = pathFileNameWithoutExtension;
		try {
			System.out.println("Loading response model with : " + pathFileNameWithoutExtension);
			// final URI notationUri = URI.createPlatformPluginURI(pathFileNameWithoutExtension + NOTATION_SUFFIX, false);
			// final URI umlUri = URI.createPlatformPluginURI(pathFileNameWithoutExtension + MODEL_SUFFIX, false);
			// final URI diagramUri = URI.createPlatformPluginURI(pathFileNameWithoutExtension + DIAGRAM_SUFFIX, false);

			// final URI notationUri = URI.createFileURI(getNotationFile());
			// final URI umlUri = URI.createFileURI(getUmlFile());
			// final URI diagramUri = URI.createFileURI(getDiagramFile());

			if (!isReponseModelInternal) {
				IProject nouveauProjet = ResourcesPlugin.getWorkspace().getRoot().getProject(PlayerModel.PROJECT_NAME);

				if (nouveauProjet.exists()) {
					try {
						nouveauProjet.open(new NullProgressMonitor());
					} catch (CoreException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			responsesModelSet = ModelUtils.loadModelSet(getUmlURI(isReponseModelInternal), true);

			responsesModelSet.loadModels(getNotationURI(isReponseModelInternal));
			responsesModelSet.loadModels(getDiagramURI(isReponseModelInternal));

			System.out.println("URL : " + getUmlURI(isReponseModelInternal));
			System.out.println("URL : " + getNotationURI(isReponseModelInternal));
			System.out.println("URL : " + getDiagramURI(isReponseModelInternal));

			EcoreUtil.resolveAll(responsesModelSet);

			org.eclipse.papyrus.infra.core.utils.DiResourceSet e;

		} catch (ModelMultiException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return
	 */
	private String cacheFolderAbsolutePath() {
		return new File(CACHE_FOLDER).getAbsolutePath();
	}

	public URI getNotationURI(boolean isReponseModelInternal) {
		if (isReponseModelInternal) {
			return URI.createPlatformPluginURI("/" + Activator.PLUGIN_ID + pathFileNameWithoutExtension + NOTATION_SUFFIX, false);
		}
		return URI.createFileURI(pathFileNameWithoutExtension + NOTATION_SUFFIX);
	}


	public URI getDiagramURI(boolean isReponseModelInternal) {
		if (isReponseModelInternal) {
			return URI.createPlatformPluginURI("/" + Activator.PLUGIN_ID + pathFileNameWithoutExtension + DIAGRAM_SUFFIX, false);
		}
		return URI.createFileURI(pathFileNameWithoutExtension + DIAGRAM_SUFFIX);
	}

	public URI getUmlURI(boolean isReponseModelInternal) {
		if (isReponseModelInternal) {
			return URI.createPlatformPluginURI("/" + Activator.PLUGIN_ID + pathFileNameWithoutExtension + MODEL_SUFFIX, false);
		}
		return URI.createFileURI(pathFileNameWithoutExtension + MODEL_SUFFIX);
	}

	/*
	 * public String getDiagramURI() {
	 * return "http://www.cristal.univ-lille.fr/miny/papyrus/responses/Library.di";
	 * // System.out.println(cacheFolderAbsolutePath() + "/" + CACHE_FILENAME_WITHOUT_EXT + DIAGRAM_SUFFIX);
	 * // return cacheFolderAbsolutePath() + "/" + CACHE_FILENAME_WITHOUT_EXT + DIAGRAM_SUFFIX;
	 * }
	 *
	 * public String getUmlURI() {
	 * return "http://www.cristal.univ-lille.fr/miny/papyrus/responses/Library.uml";
	 * // System.out.println(cacheFolderAbsolutePath() + "/" + CACHE_FILENAME_WITHOUT_EXT + MODEL_SUFFIX);
	 * // return cacheFolderAbsolutePath() + "/" + CACHE_FILENAME_WITHOUT_EXT + MODEL_SUFFIX;
	 * }
	 */
	public Diagram getDiagram(String diagramName) {
		System.out.println("ModelSet is " + responsesModelSet);

		System.out.println(DiagramUtils.getAllNotationDiagram(responsesModelSet, diagramName));
		return DiagramUtils.getNotationDiagram(responsesModelSet, diagramName);
	}

	public void loadInCache(String pathFileNameWithoutExtension) {

		File folder = new java.io.File(CACHE_FOLDER);
		folder.mkdir();
		for (String extension : EXTENSIONS) {
			File tempoFile = new java.io.File(CACHE_FOLDER + "/" + CACHE_FILENAME_WITHOUT_EXT + "." + extension);
			try {
				tempoFile.createNewFile();

				PrintWriter out = new PrintWriter(tempoFile);
				out.println(getContentOfFileLocatedInTheJar(pathFileNameWithoutExtension + "." + extension));
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public String getContentOfFileLocatedInTheJar(String nameFile) {
		InputStream in = ResponseModel.class.getResourceAsStream(nameFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder fileContent = new StringBuilder("");
		try {
			String currentLine = reader.readLine();
			while (currentLine != null) {
				fileContent.append(currentLine + "\n");
				currentLine = reader.readLine();
			}
			reader.close();
		} catch (IOException exception) {
			System.out.println(exception);
		}
		return fileContent.toString();
	}
}
