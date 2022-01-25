package org.eclipse.papyrus.gamification.data.jsonmapper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Superclass for all UML elements, handles notably stereotype applications
 * @author ansgar
 */
public class UmlElement {
	private static final String BASE = "base_"; //$NON-NLS-1$

	/**
	 * list of applied stereotypes in string format (ready for serialization)
	 */
	protected List<String> appliedStereotypes;

	public void setStereotypeApplications(Element umlElement) {
		List<String> appliedStereotypes = new ArrayList<String>();
		for (Stereotype stereotype : umlElement.getAppliedStereotypes()) {
			String attributes = ""; //$NON-NLS-1$
			for (Property attribute : stereotype.getAllAttributes()) {
				String attributeName = attribute.getName();
				String attribString = ""; //$NON-NLS-1$
				if (attributeName != null && (!attributeName.startsWith(BASE))) {
					Object value = umlElement.getValue(stereotype, attribute.getName());
					if (value instanceof List<?>) {
						// currently unsupported.
					}
					else if (value != null) {
						Element base;
						if (value instanceof EObject) {
							// if value is a stereotype application and base is a named element, use it (name is easier to serialize)
							base = UMLUtil.getBaseElement((EObject) value);
							if (base instanceof NamedElement) {
								value = base;
							}
						}
						if (value instanceof NamedElement) {
							attribString = String.format("%s=%s", attributeName, ((NamedElement) value).getName()); //$NON-NLS-1$
						}
						else {
							attribString = String.format("%s=%s", attributeName, value.toString()); //$NON-NLS-1$
						}
					}
				}
				if (attributes.length() > 0 && attribString.length() > 0) {
					attributes += ", "; //$NON-NLS-1$
				}
				attributes += attribString;
			
			}
			String appliedStereotype = String.format("%s {%s}", stereotype.getQualifiedName(), attributes); //$NON-NLS-1$
			appliedStereotypes.add(appliedStereotype);
			System.err.println(appliedStereotype);
		}
	}
	
	public List<String> getAppliedStereotypes() {
		return appliedStereotypes;
	}
}
