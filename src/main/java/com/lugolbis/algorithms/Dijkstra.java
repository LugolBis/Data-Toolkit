package com.lugolbis.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import com.lugolbis.dsa.Graph;
import com.lugolbis.dsa.GraphData;
import com.lugolbis.dsa.GraphType;

public class Dijkstra<T extends GraphData, U extends GraphData & DijkstraData> {
    HashSet<Graph<T, U>.Node> nodes;
    HashMap<Graph<T, U>.Node, Double> distances;
    HashMap<Graph<T, U>.Node, Graph<T, U>.Node> predecessors;
    Graph<T, U> graph;

    private Dijkstra(Graph<T, U> graph) {
        nodes = new HashSet<>();
        distances = new HashMap<>();
        predecessors = new HashMap<>();
        this.graph = graph;
    }

    public HashMap<Graph<T, U>.Node, Double> getDistances() {
        return distances;
    }

    public HashMap<Graph<T, U>.Node, Graph<T, U>.Node> getPredecessors() {
        return predecessors;
    }

    public static <T extends GraphData, U extends GraphData & DijkstraData> Optional<Dijkstra<T, U>> run(Graph<T, U> graph, Graph<T, U>.Node node) {
        if (graph == null || node  == null) {
            return Optional.empty();
        }
        
        Dijkstra<T, U> dijkstra = new Dijkstra<>(graph);
        dijkstra.compute(node);
        return Optional.of(dijkstra);
    }

    private void compute(Graph<T, U>.Node nodeTarget) {
        for (Graph<T, U>.Node node : graph.getNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        distances.put(nodeTarget, 0.0);

        int nodesCount = graph.getNodes().size();
        while (nodes.size() < nodesCount) {
            ArrayList<Graph<T, U>.Node> nodesOuter = getOuterNodes();

            Graph<T, U>.Node nodeCurrent = getKeyDistanceMin(nodesOuter);
            
            nodes.add(nodeCurrent);
            update(nodesOuter, nodeCurrent);
        }
    }

    private ArrayList<Graph<T, U>.Node> getOuterNodes() {
        ArrayList<Graph<T, U>.Node> array = new ArrayList<>();

        for (Graph<T, U>.Node node : graph.getNodes()) {
            if (!nodes.contains(node)) {
                array.add(node);
            }
        }
        return array;
    }

    private Graph<T, U>.Node getKeyDistanceMin(ArrayList<Graph<T, U>.Node> nodesOuter) {
        Graph<T, U>.Node minKey = null;
        double minValue = Double.POSITIVE_INFINITY;

        for (Graph<T, U>.Node node : nodesOuter) {
            double value = distances.get(node);
            if (value < minValue) {
                minValue = value;
                minKey = node;
            }
        }
        return minKey;
    }

    private double getWeight(Graph<T, U>.Node node1, Graph<T, U>.Node node2) {
        if (graph.getType() == GraphType.OrientedGraph) {
            Optional<Graph<T, U>.Edge> edge = graph.getEdge(node1, node2);
            if (edge.isPresent()) {
                Optional<U> value = edge.get().getValue();
                return value.isPresent() ? value.get().getDouble() : Double.POSITIVE_INFINITY;
            }
            else {
                return Double.POSITIVE_INFINITY;
            }
        }
        else {
            Optional<Graph<T, U>.Edge> edge1 = graph.getEdge(node1, node2);

            if (edge1.isEmpty()) {
                Optional<Graph<T, U>.Edge> edge2 = graph.getEdge(node2, node1);

                if (edge2.isPresent()) {
                    Optional<U> value = edge2.get().getValue();
                    return value.isPresent() ? value.get().getDouble() : Double.POSITIVE_INFINITY;
                }
                else {
                    return Double.POSITIVE_INFINITY;
                }
            }
            else {
                Optional<U> value = edge1.get().getValue();
                return value.isPresent() ? value.get().getDouble() : Double.POSITIVE_INFINITY;
            }
        }
    }

    private void update(ArrayList<Graph<T, U>.Node> nodesOuter, Graph<T, U>.Node nodeCurrent) {
        for (Graph<T, U>.Node node : nodesOuter) {
            double weight = getWeight(nodeCurrent, node);
            if (weight != Double.POSITIVE_INFINITY) {
                double new_distance = distances.get(nodeCurrent) + weight;
                
                if (distances.get(node) > new_distance) {
                    distances.put(node, new_distance);
                    predecessors.put(node, nodeCurrent);
                }
            }
        }
    }
}
