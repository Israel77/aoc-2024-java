package com.adventofcode.graphs.interfaces;

import java.util.Collection;

import com.adventofcode.util.Pair;

public interface WeightedGraph<V, C extends Comparable<C>> extends Graph<V> {

    boolean addEdge(V source, V destination, C cost);

    Collection<Pair<V, C>> getNeighborsWithCost(C cost);
}
