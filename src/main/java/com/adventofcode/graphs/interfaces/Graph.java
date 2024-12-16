package com.adventofcode.graphs.interfaces;

import java.util.Collection;

import com.adventofcode.util.Pair;

public interface Graph<V> {

    boolean addVertex(V vertex);

    boolean removeVertex(V vertex);

    boolean addEdge(V source, V destination);

    boolean removeEdge(V source, V destination);

    Collection<V> vertices();

    Collection<Pair<V, V>> edges();

    Collection<V> neighbors(V vertex);

    int countVertices();

    int countEdges();

    boolean containsVertex(V vertex);

    boolean containsEdge(Pair<V, V> edge);
}