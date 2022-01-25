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

package org.eclipse.papyrus.gamification.modelutils.papyrus;

import java.util.Collection;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.Shape;

/**
 * @author lepallec
 *
 */
public class PapyrusGenericFactory<ElemClass, EditPartClass> {

	IElementType elementType;
	Class<?> containerClass = null;

	public PapyrusGenericFactory(IElementType elementType) {
		this.elementType = elementType;
	}

	public PapyrusGenericFactory(IElementType elementType, Class<?> containerClass) {
		this.elementType = elementType;
		this.containerClass = containerClass;
	}

	public EditPartClass createElement(DiagramEditPart diagramEditPart, GraphicalEditPart container) {
		return createElement(diagramEditPart, container, -1, -1, -1, -1);
	}

	public EditPartClass createElement(DiagramEditPart diagramEditPart, GraphicalEditPart container, int x,
			int y, int w, int h) {

		CreateViewRequest requestToCreateAPartition = CreateViewRequestFactory.getCreateShapeRequest(elementType,
				diagramEditPart.getDiagramPreferencesHint());
		if (x != -1) {
			requestToCreateAPartition.setLocation(new Point(x, y));
			requestToCreateAPartition.setSize(new Dimension(w, h));
		}

		GraphicalEditPart finalContainer = null;
		if (container == null) {
			finalContainer = diagramEditPart;
		} else if (containerClass == null) {
			finalContainer = container;
		} else {
			finalContainer = getContainerCompartment(container);
		}

		org.eclipse.gef.commands.Command commandToCreateAPartition = finalContainer.getCommand(requestToCreateAPartition);

		commandToCreateAPartition.execute();

		ElemClass newInstance = null;
		Collection returnValues = DiagramCommandStack.getReturnValues(commandToCreateAPartition);
		for (Object returnValue : returnValues) {
			if (returnValue instanceof CreateElementRequestAdapter) {
				CreateElementRequestAdapter adapter = CreateElementRequestAdapter.class.cast(returnValue);
				ElemClass newElement = convertToPapyrusElement(adapter.resolve());
				if (newElement != null) {
					newInstance = newElement;
				}
			}
		}
		if (newInstance != null) {
			for (Object child : finalContainer.getChildren()) {
				EditPart editPart = EditPart.class.cast(child);
				Shape shape = Shape.class.cast(editPart.getModel());
				if (shape.getElement().equals(newInstance)) {
					return (EditPartClass) editPart;
				}
			}
		}
		return null;
	}

	public ElemClass convertToPapyrusElement(Object o) {
		try {
			return (ElemClass) o;
		} catch (ClassCastException e) {
			return null;
		}
	}


	private CompartmentEditPart getContainerCompartment(GraphicalEditPart container) {
		for (Object child : container.getChildren()) {
			if (child.getClass().equals(containerClass)) {
				return CompartmentEditPart.class.cast(child);
			}
		}
		return null;
	}

}