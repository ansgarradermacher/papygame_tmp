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

package org.eclipse.papyrus.gamification.games.hangman;

import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusAttribute;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClass;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClassDiagram;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusOperation;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusOperationParameter;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusType;
import org.eclipse.papyrus.gamification.modelutils.papyrus.copy.AttributeMask;
import org.eclipse.papyrus.gamification.modelutils.papyrus.copy.OperationMask;
import org.eclipse.papyrus.gamification.modelutils.papyrus.copy.OperationParameterMask;

/**
 * @author lepallec
 *
 */
public class ClassDiagramCopier extends org.eclipse.papyrus.gamification.modelutils.papyrus.copy.ClassDiagramCopier {

	// TODO : override other methods to suppress filtering during copy

	PapyrusClass fakeClass;
	Hangman hangman;

	public ClassDiagramCopier(PapyrusClassDiagram originDiagram, PapyrusClassDiagram targetDiagram) {
		super(originDiagram, targetDiagram);
	}

	public ClassDiagramCopier(PapyrusClassDiagram originalDiagram, PapyrusClassDiagram playerDiagram, Hangman hangman) {
		super(originalDiagram, playerDiagram);
		this.hangman = hangman;
	}

	@Override
	public void copy() {
		fakeClass = targetDiagram.addClass("XXFakeXX", 50, 50, 170, 100);
		fakeClass.setLineColor(16219144);
		fakeClass.setLineWidth(2);
		fakeClass.setBackgroundColor(16777215);
		fakeClass.hideNameCompartment();

		super.copy();

		this.hangman.setFakeElementsNumber(fakeClass.getAttributes().length + fakeClass.getOperations().length);


	}

	@Override
	protected void copyAttributesOfClasses() {

		for (PapyrusClass clazz : originDiagram.getClasses()) {
			PapyrusClass newClass = PapyrusClass.class.cast(originToTargetMap.get(clazz));
			for (PapyrusAttribute attribute : clazz.getAttributes()) {

				AttributeMask mask = new AttributeMask(attribute);

				PapyrusAttribute newAttribute;


				String name = mask.getFinalName();

				if (!mask.dontCopy()) {
					newAttribute = newClass.addAttribute(name);
				} else {
					newAttribute = fakeClass.addAttribute(name);
				}

				newAttribute.setLower(attribute.getLower());
				newAttribute.setUpper(attribute.getUpper());
				newAttribute.setDerived(attribute.isDerived());
				newAttribute.setOrdered(attribute.isOrdered());
				newAttribute.setUnique(attribute.isUnique());
				newAttribute.setVisibility(attribute.getVisibility());
				PapyrusType type = attribute.getType();
				PapyrusType newType = getCorrespondingType(type);
				newAttribute.setType(newType);

				map(attribute, newAttribute);

			}
		}
	}

	@Override
	protected void copyOperationsOfClasses() {

		for (PapyrusClass clazz : originDiagram.getClasses()) {
			PapyrusClass newClass = PapyrusClass.class.cast(originToTargetMap.get(clazz));
			for (PapyrusOperation operation : clazz.getOperations()) {

				OperationMask maskOp = new OperationMask(operation);

				String opName = maskOp.getFinalName();

				PapyrusOperation newOperation;

				if (!maskOp.dontCopy()) {
					newOperation = newClass.addOperation(opName);
				} else {
					newOperation = fakeClass.addOperation(opName);
				}


				for (PapyrusOperationParameter parameter : operation.getParameters()) {

					OperationParameterMask mask = new OperationParameterMask(parameter);

					String name = mask.getFinalName();
					PapyrusOperationParameter newParameter = newOperation.addParameter(name);

					newParameter.setLower(parameter.getLower());
					newParameter.setUpper(parameter.getUpper());
					newParameter.setDirection(parameter.getDirection());
					newParameter.setOrdered(parameter.isOrdered());
					newParameter.setUnique(parameter.isUnique());
					PapyrusType type = parameter.getType();
					PapyrusType newType = getCorrespondingType(type);
					newParameter.setType(newType);


				}

				map(operation, newOperation);


			}
		}

	}


}
