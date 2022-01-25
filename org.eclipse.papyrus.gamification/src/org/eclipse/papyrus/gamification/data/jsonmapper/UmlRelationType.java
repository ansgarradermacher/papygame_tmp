package org.eclipse.papyrus.gamification.data.jsonmapper;

public enum UmlRelationType {
    DIRECTED_ASSOCIATION,
    UNDIRECTED_ASSOCIATION,
    COMPOSITE_ASSOCIATION, //Composition
    SHARED_ASSOCIATION, //Aggregation
    GENERALIZATION,
    INTERFACE_REALIZATION,
    DEPENDENCY,
    USAGE,
    UNKNOWN
}
