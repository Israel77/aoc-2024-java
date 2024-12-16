package com.adventofcode.graphs.implementations;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.adventofcode.graphs.interfaces.Graph;
import com.adventofcode.util.Pair;

/**
 * Implementation of simple undirected, unweighted graph.
 */
public class SimpleGraph<V> implements Graph<V> {

    Set<V> _vertices = new HashSet<>();
    Map<V, Collection<V>> adjacencyList = new HashMap<>();

    @Override
    public boolean addVertex(V vertex) {

        adjacencyList.putIfAbsent(vertex, new HashSet<>());

        return _vertices.add(vertex);
    }

    @Override
    public boolean removeVertex(V vertex) {

        adjacencyList.remove(vertex);

        return _vertices.remove(vertex);
    }

    @Override
    public boolean addEdge(V source, V destination) {
        if (!_vertices.contains(source)
                || !_vertices.contains(destination)
                || adjacencyList.get(source).contains(destination)
                || adjacencyList.get(destination).contains(source))
            return false;

        return adjacencyList.get(source).add(destination) && adjacencyList.get(destination).add(source);
    }

    @Override
    public boolean removeEdge(V source, V destination) {
        return adjacencyList.get(destination).remove(source) || adjacencyList.get(source).remove(destination);
    }

    @Override
    public Set<V> vertices() {
        return _vertices;
    }

    @Override
    public Set<Pair<V, V>> edges() {
        Set<Pair<V, V>> pairs = new HashSet<>();

        for (var entry : adjacencyList.entrySet()) {
            for (var value : entry.getValue()) {
                pairs.add(new Pair<>(entry.getKey(), value));
                pairs.add(new Pair<>(value, entry.getKey()));
            }
        }

        return pairs;
    }

    @Override
    public Collection<V> neighbors(V vertex) {
        return adjacencyList.get(vertex);
    }

    @Override
    public int countVertices() {
        return _vertices.size();
    }

    @Override
    public int countEdges() {
        // Each edge is counted twice due to
        // the graph working in both directions
        return edges().size() / 2;
    }

    @Override
    public boolean containsEdge(Pair<V, V> edge) {
        var source = edge.first();
        var destination = edge.second();

        return adjacencyList.get(source).contains(destination);
    }

    @Override
    public boolean containsVertex(V vertex) {
        return _vertices.contains(vertex);
    }

}
