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

package org.eclipse.papyrus.gamification.modelutils.papyrus.copy;

import java.util.HashMap;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusAssociation;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusAssociationEnd;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusAttribute;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClass;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClassDiagram;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClassifier;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusGeneralization;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusOperation;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusOperationParameter;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusPrimitiveType;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusType;

/**
 * @author lepallec
 *
 */
public class ClassDiagramCopier {

	protected PapyrusClassDiagram originDiagram;
	protected PapyrusClassDiagram targetDiagram;
	protected HashMap<Object, Object> originToTargetMap = new HashMap<>();
	protected HashMap<Object, Object> targetToOriginMap = new HashMap<>();

	public ClassDiagramCopier() {

	}

	public ClassDiagramCopier(PapyrusClassDiagram originDiagram, PapyrusClassDiagram targetDiagram) {
		this.originDiagram = originDiagram;
		this.targetDiagram = targetDiagram;
	}

	public ClassDiagramCopier(Diagram originDiagram, DiagramEditPart targetDiagram, TransactionalEditingDomain transactionalEditingDomain) {
		this.originDiagram = new PapyrusClassDiagram(originDiagram);
		this.targetDiagram = new PapyrusClassDiagram(transactionalEditingDomain, targetDiagram);
	}

	public void map(Object origin, Object target) {
		originToTargetMap.put(origin, target);
		targetToOriginMap.put(target, origin);
	}

	public void copy() {
		initPrimitivesTypes();
		copyClassesExceptAttributesAndOperations();
		copyAttributesOfClasses();
		copyOperationsOfClasses();
		copyGeneralizations();
		copyAssociations();
	}

	protected void initPrimitivesTypes() {
		PapyrusPrimitiveType[] primitiveTypes = originDiagram.getPrimitiveTypes();
		for (PapyrusPrimitiveType primitiveType : primitiveTypes) {
			PapyrusPrimitiveType newPrimitiveType = targetDiagram.addPrimitive(primitiveType.getName());
			map(primitiveType, newPrimitiveType);
		}
	}

	protected void copyClassesExceptAttributesAndOperations() {
		for (PapyrusClass clazz : originDiagram.getClasses()) {
			ClassMask classMask = new ClassMask(clazz);
			if (!classMask.dontCopy()) {
				String className = classMask.getFinalName();

				if (classMask.isNameMentioned()) {
					className = "toDefine";
				}

				PapyrusClass newClass = targetDiagram.addClass(className, clazz.getX(), clazz.getY(), clazz.getWidth(), clazz.getHeight());
				if (!classMask.isAbstractMentioned()) {
					newClass.setAbstract(clazz.isAbstract());
				}

				map(clazz, newClass);

			}
		}
	}

	protected void copyAttributesOfClasses() {

		for (PapyrusClass clazz : originDiagram.getClasses()) {
			System.out.println("We have class : " + clazz);
			System.out.println("Map is : " + originToTargetMap);
			System.out.println("Origin to target :  : " + originToTargetMap.get(clazz));
			// TODO check Xavier
			if (originToTargetMap.get(clazz) != null) {
				PapyrusClass newClass = PapyrusClass.class.cast(originToTargetMap.get(clazz));
				ClassMask classMask = new ClassMask(newClass);

				if (!classMask.dontCopy()) {
					for (PapyrusAttribute attribute : clazz.getAttributes()) {

						AttributeMask mask = new AttributeMask(attribute);

						if (!mask.dontCopy()) {

							String name = mask.getFinalName();
							if (mask.isNameMentioned()) {
								name = "toDefine";
							}

							PapyrusAttribute newAttribute = newClass.addAttribute(name);

							if (!mask.isMultiplicityMentioned()) {
								newAttribute.setLower(attribute.getLower());
								newAttribute.setUpper(attribute.getUpper());
							}

							if (!mask.isDerivedMentioned()) {
								newAttribute.setDerived(attribute.isDerived());
							}

							if (!mask.isOrderedMentioned()) {
								newAttribute.setOrdered(attribute.isOrdered());
							}

							if (!mask.isUniqueMentioned()) {
								newAttribute.setUnique(attribute.isUnique());
							}

							if (!mask.isVisibilityMentioned()) {
								newAttribute.setVisibility(attribute.getVisibility());
							}
							if (!mask.isTypeMentioned()) {
								PapyrusType type = attribute.getType();
								PapyrusType newType = getCorrespondingType(type);
								newAttribute.setType(newType);
							}

							map(attribute, newAttribute);
						}
					}
				}
			}
		}
	}


	protected void copyOperationsOfClasses() {

		for (PapyrusClass clazz : originDiagram.getClasses()) {
			if (originToTargetMap.get(clazz) != null) {

				PapyrusClass newClass = PapyrusClass.class.cast(originToTargetMap.get(clazz));
				ClassMask classMask = new ClassMask(newClass);

				if (!classMask.dontCopy()) {
					for (PapyrusOperation operation : clazz.getOperations()) {

						OperationMask maskOp = new OperationMask(operation);

						if (!maskOp.dontCopy()) {

							String opName = maskOp.getFinalName();
							if (maskOp.isNameMentioned()) {
								opName = "toDefine";
							}
							PapyrusOperation newOperation = newClass.addOperation(opName);

							for (PapyrusOperationParameter parameter : operation.getParameters()) {

								OperationParameterMask mask = new OperationParameterMask(parameter);

								if (!mask.dontCopy()) {

									String name = mask.getFinalName();
									if (mask.isNameMentioned()) {
										name = "toDefine";
									}
									PapyrusOperationParameter newParameter = newOperation.addParameter(name);

									if (!mask.isMultiplicityMentioned()) {
										newParameter.setLower(parameter.getLower());
										newParameter.setUpper(parameter.getUpper());
									}

									if (!mask.isDirectionMentioned()) {
										newParameter.setDirection(parameter.getDirection());
									}

									if (!mask.isOrderedMentioned()) {
										newParameter.setOrdered(parameter.isOrdered());
									}

									if (!mask.isUniqueMentioned()) {
										newParameter.setUnique(parameter.isUnique());
									}

									if (!mask.isTypeMentioned()) {
										PapyrusType type = parameter.getType();
										PapyrusType newType = getCorrespondingType(type);
										newParameter.setType(newType);
									}


								}

							}

							map(operation, newOperation);
						}
					}
				}
			}
		}

	}

	protected void copyGeneralizations() {
		for (PapyrusGeneralization generalization : originDiagram.getGeneralizations()) {
			PapyrusClassifier subclass = generalization.getSubclass();
			PapyrusClassifier superclass = generalization.getSuperclass();

			ClassMask mask = new ClassMask(PapyrusClass.class.cast(subclass));

			if (!mask.isSuperclassesMentioned()) {

				PapyrusClassifier newSubclass = PapyrusClassifier.class.cast(originToTargetMap.get(subclass));
				PapyrusClassifier newSuperclass = PapyrusClassifier.class.cast(originToTargetMap.get(superclass));

				try {
					PapyrusGeneralization newGeneralization = targetDiagram.addGeneralization(newSubclass, newSuperclass);

					map(generalization, newGeneralization);
				} catch (Exception e) {
					System.out.println("Error while creating generalization");
					e.printStackTrace();
				}
			}
		}

	}

	protected void copyAssociations() {
		for (PapyrusAssociation association : originDiagram.getAssociations()) {
			try {
				AssociationMask associationMask = new AssociationMask(association);

				if (!associationMask.dontCopy()) {

					String associationName = associationMask.getFinalName();
					if (associationMask.isNameMentioned()) {
						associationName = "toDefine";
					}
					System.out.println("Association irignal : " + association);
					PapyrusClassifier target = PapyrusClassifier.class.cast(originToTargetMap.get(association.getSourceEnd().getType()));
					PapyrusClassifier source = PapyrusClassifier.class.cast(originToTargetMap.get(association.getTargetEnd().getType()));

					try {
						PapyrusAssociation newAssociation = targetDiagram.addAssociation(associationName, source, target);

						PapyrusAssociationEnd[] ends = { association.getSourceEnd(), association.getTargetEnd() };
						PapyrusAssociationEnd[] newEnds = { newAssociation.getSourceEnd(), newAssociation.getTargetEnd() };
						System.out.println("Ends1 : " + ends);
						System.out.println("Ends2 : " + newEnds);

						for (int i = 0; i < ends.length; i++) {
							PapyrusAssociationEnd end = ends[i];
							PapyrusAssociationEnd newEnd = newEnds[i];

							AssociationEndMask mask = new AssociationEndMask(end);
							String associationEndName = mask.getFinalName();
							if (mask.isNameMentioned()) {
								associationEndName = "toDefine";
							}
							newEnd.setName(associationEndName);
							newEnd.transferAssociationType(end);


							if (!mask.isMultiplicityMentioned()) {
								newEnd.setLower(end.getLower());
								newEnd.setUpper(end.getUpper());
							}

							if (!mask.isDerivedMentioned()) {
								newEnd.setDerived(end.isDerived());
							}

							if (!mask.isOrderedMentioned()) {
								newEnd.setOrdered(end.isOrdered());
							}

							if (!mask.isUniqueMentioned()) {
								newEnd.setUnique(end.isUnique());
							}

							if (!mask.isVisibilityMentioned()) {
								newEnd.setUnique(end.isUnique());
							}

						}

						map(association, newAssociation);
					} catch (Exception e) {
						System.out.println("Error while creating association");
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}

	/**
	 * @param type
	 * @return
	 */
	protected PapyrusType getCorrespondingType(PapyrusType type) {
		PapyrusType correspondingType = PapyrusType.class.cast(originToTargetMap.get(type));
		return correspondingType;
	}

	public Object getCorrespondingOriginalElement(Object playerElement) {
		return targetToOriginMap.get(playerElement);
	}

	public Object getCorrespondingPlayerElement(Object originalElement) {
		return originToTargetMap.get(originalElement);
	}

}
