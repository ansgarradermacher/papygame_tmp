/*****************************************************************************
 * Copyright (c) 2019 CEA LIST.
 *
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Xavier Le Pallec (for CEA LIST) xlepallec@lilo.org - Bug 558456
 *
 *****************************************************************************/

package org.eclipse.papyrus.gamification.modelutils.papyrus.copy;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * This class is used to convert types between Java/Papyrus and Javascript/JSON.
 * As for AccessibleTypes, this class needs a class to have the context in which
 * looking for... a class always gives access to a model.
 *
 */
public class TypeHelper {

	private static final String LABEL_NO_CORRESPONDING_TYPE = ""; //$NON-NLS-1$
	private static final String LABEL_NULL_TYPE = ""; //$NON-NLS-1$
	static final String LABEL_STRING_TYPE = "String"; //$NON-NLS-1$
	static final String LABEL_BOOLEAN_TYPE = "Boolean"; //$NON-NLS-1$
	static final String LABEL_INTEGER_TYPE = "Integer"; //$NON-NLS-1$
	static final String LABEL_REAL_TYPE = "Real"; //$NON-NLS-1$
	static final String LABEL_SHORT_BOOLEAN_TYPE = "bool"; //$NON-NLS-1$
	static final String LABEL_SHORT_INTEGER_TYPE = "int"; //$NON-NLS-1$

	Resource resource;

	/**
	 * Instantiates the instance with the class/context
	 *
	 * @param clazz
	 *            the class which gives the context (its model)
	 */
	public TypeHelper(Diagram diagram) {
		this.resource = diagram.eResource();
	}

	/**
	 * Gets the type corresponding to the name (that can be a qualified name)
	 *
	 * @param typeName
	 *            (qualified) name of the type
	 * @return the real type (String if no type was found)
	 */
	public org.eclipse.uml2.uml.Type getTypeFromName(String typeName) {
		final PrimitiveType stringType = getPrimitiveType(LABEL_STRING_TYPE);
		PrimitiveType type = stringType;
		if (typeName != null) {
			String lowerTypeName = typeName.toLowerCase();
			if (lowerTypeName.equalsIgnoreCase(LABEL_SHORT_INTEGER_TYPE)
					|| lowerTypeName.equalsIgnoreCase(LABEL_INTEGER_TYPE)) {
				type = getPrimitiveType(LABEL_INTEGER_TYPE);
			}

			else if (lowerTypeName.equalsIgnoreCase(LABEL_STRING_TYPE)) {
				type = getPrimitiveType(LABEL_STRING_TYPE);
			}

			else if (lowerTypeName.equalsIgnoreCase(LABEL_SHORT_BOOLEAN_TYPE)
					|| lowerTypeName.equalsIgnoreCase(LABEL_BOOLEAN_TYPE)) {
				type = getPrimitiveType(LABEL_BOOLEAN_TYPE);
			}

			else if (lowerTypeName.equalsIgnoreCase(LABEL_REAL_TYPE)) {
				type = getPrimitiveType(LABEL_REAL_TYPE);
			}

		}
		return type;
	}

	/**
	 * Gets the name of a type given in parameter
	 *
	 * @param type
	 *            the type to get the corresponding name
	 * @return the corresponding name (can be qualified)
	 */
	public String getTypeNameForAGivenType(org.eclipse.uml2.uml.Type type) {
		if (type == null) {
			return LABEL_NULL_TYPE;
		}
		if (type.equals(getPrimitiveType(LABEL_INTEGER_TYPE))) {
			return LABEL_INTEGER_TYPE;
		}
		if (type.equals(getPrimitiveType(LABEL_STRING_TYPE))) {
			return LABEL_STRING_TYPE;
		}
		if (type.equals(getPrimitiveType(LABEL_BOOLEAN_TYPE))) {
			return LABEL_BOOLEAN_TYPE;
		}
		if (type.equals(getPrimitiveType(LABEL_REAL_TYPE))) {
			return LABEL_BOOLEAN_TYPE;
		}
		if (type instanceof org.eclipse.uml2.uml.Namespace) {
			return Namespace.class.cast(type).getQualifiedName();
		}
		return LABEL_NO_CORRESPONDING_TYPE;
	}

	/**
	 * Gets only primitive types for a given name
	 *
	 * @param primitiveTypeName
	 *            name of a given primitive type
	 * @return the primitive type
	 */
	public PrimitiveType getPrimitiveType(String primitiveTypeName) {
		org.eclipse.uml2.uml.Package umlLibrary = load(resource.getResourceSet(),
				URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));
		if (umlLibrary != null && (umlLibrary.getOwnedType(primitiveTypeName) instanceof PrimitiveType)) {
			return PrimitiveType.class.cast(umlLibrary.getOwnedType(primitiveTypeName));
		}
		return null;
	}

	/**
	 * Loads a package from a uri and a resourcesSet.
	 *
	 * @param resourceSet
	 *            resourceSet (get from a class)
	 * @param uri
	 *            (the name/uri of the package)
	 * @return the package
	 */
	protected static org.eclipse.uml2.uml.Package load(ResourceSet resourceSet, URI uri) {
		try {

			// Load the requested resource
			Resource resource = resourceSet.getResource(uri, true);
			// Get the first (should be only) package from it
			Object aPackage = EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
			if (aPackage instanceof org.eclipse.uml2.uml.Package) {
				return org.eclipse.uml2.uml.Package.class
						.cast(EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE));
			}

		} catch (WrappedException exception) {
			exception.printStackTrace();
		}
		return null;
	}
}
