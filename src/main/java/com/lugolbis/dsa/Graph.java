package com.lugolbis.dsa;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;

public class Graph<T extends GraphData, U extends GraphData> {
    private HashSet<Node> nodes;
    private HashSet<Edge> edges;
    private GraphType type;

    public class Node implements GraphData {
        private T value;

        private Node(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Graph.Node) {
                return this.value.hashCode() == other.hashCode();
            }
            else {
                return false;
            }
        }

        @Override
        public String toString() {
            return String.format("(%s)", value);
        }
    }

    public class Edge implements GraphData {
        private Node start;
        private Node end;
        private Optional<U> value;

        private Edge(Node start, Node end) {
            this.start = start;
            this.end = end;
            value = Optional.empty();
        }

        private Edge(Node start, Node end, U value) {
            this(start, end);
            this.value = Optional.of(value);
        }

        public Optional<U> getValue() {
            return this.value;
        }

        public void setValue(U value) {
            this.value = Optional.of(value);
        }

        @Override
        public int hashCode() {
            return (start.hashCode() * 31) ^ end.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Graph.Edge) {
                return this.hashCode() == other.hashCode();
            }
            else {
                return false;
            }
        }

        @Override
        public String toString() {
            if (value.isPresent()) {
                return String.format("%s--< %s >-->%s", start, value.get(), end);
            }
            else {
                return String.format("%s------>%s", start, end);
            }
        }
    }
    
    public Graph(GraphType type) {
        nodes = new HashSet<>();
        edges = new HashSet<>();
        this.type = type;
    }

    /**
     * Create and adding a new Node to the graph from the value of type T.
     */
    public Node addNode(T value) {
        Node node = new Node(value);
        nodes.add(node);
        return node;
    }

    /**
     * Remove the node and all the edges with this node.
     */
    public void removeNode(Node node) {
        nodes.remove(node);

        Iterator<Edge> iterator = edges.iterator();

        while (iterator.hasNext()) {
            Edge edge = iterator.next();

            if (edge.start.equals(node) || edge.end.equals(node)) {
                edges.remove(edge);
            }
        }
    }

    /** 
     * The order is not important if it's  not an Orientend Graph.
     * @param start is the node from where the edge start
     * @param end is the node from where the edge end
     * */ 
    public Edge addEdge(Node start, Node end) {
        Edge edge = new Edge(start, end);
        edges.add(edge);
        return edge;
    }

    /** 
     * @param start is the node from where the edge start
     * @param end is the node from where the edge end
     * @param value is the value of the edge
     * */ 
    public Edge addEdge(Node start, Node end, U value) {
        Edge edge = new Edge(start, end, value);
        edges.add(edge);
        return edge;
    }

    /**
     * Remove the specified edge
     */
    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    /**
     * @param value is the value of the Node
     * @return an Optional that contains the target Node or who's empty
     */
    public Optional<Node> getNode(T value) {
        Iterator<Node> iterator = nodes.iterator();

        while (iterator.hasNext()) {
            Node node = iterator.next();

            if (node.value.equals(value)) {
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }

    /**
     * @param start is the node from where the edge start
     * @param end is the node from where the edge end
     * @return an Optional that contains the target Edge or who's empty
     */
    public Optional<Edge> getEdge(Node start, Node end) {
        Iterator<Edge> iterator = edges.iterator();

        switch (type) {
            case OrientedGraph : return this.getEdgeOriented(start, end, iterator);
            case UnorientedGraph : return this.getEdgeUnoriented(start, end, iterator);
            default : return Optional.empty();
        }
    }

    private Optional<Edge> getEdgeOriented(Node start, Node end, Iterator<Edge> iterator) {
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            
            if (edge.start.equals(start) && edge.end.equals(end)) {
                return Optional.of(edge);
            }
        }
        return Optional.empty();
    }

    private Optional<Edge> getEdgeUnoriented(Node node1, Node node2, Iterator<Edge> iterator) {
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            
            if (
                (edge.start.equals(node1) && edge.end.equals(node2))
                || (edge.start.equals(node2) && edge.end.equals(node1))
            ) {
                return Optional.of(edge);
            }
        }
        return Optional.empty();
    }

    /**
     * @param node is the target Node.
     * @return an Array of the nodes who are the successors of the node in input or an empty Optional if it's an Unoriented Graph
     */
    public Optional<HashSet<Node>> getSuccessors(Node node) {
        if (type == GraphType.UnorientedGraph) {
            return Optional.empty();
        }

        HashSet<Node> array = new HashSet<>();
        Iterator<Edge> iterator = edges.iterator();

        while (iterator.hasNext()) {
            Edge edge = iterator.next();

            if (edge.start.equals(node)) {
                array.add(edge.end);
            }
        }

        return Optional.of(array);
    }

    /**
     * @param node is the target Node.
     * @return an Array of the nodes who are the predeccessors of the node in input or an empty Optional if it's an Unoriented Graph
     */
    public Optional<HashSet<Node>> getPredeccessors(Node node) {
        if (type == GraphType.UnorientedGraph) {
            return Optional.empty();
        }

        HashSet<Node> array = new HashSet<>();
        Iterator<Edge> iterator = edges.iterator();

        while (iterator.hasNext()) {
            Edge edge = iterator.next();

            if (edge.end.equals(node)) {
                array.add(edge.start);
            }
        }

        return Optional.of(array);
    }

    /**
     * @param node is the target Node
     * @return an Array of the nodes who are the neighbors of the target node or an empty Optional if it's an Oriented Graph
     */
    public Optional<HashSet<Node>> getNeighbors(Node node) {
        if (type == GraphType.OrientedGraph) {
            return Optional.empty();
        }

        HashSet<Node> array = new HashSet<>();
        Iterator<Edge> iterator = edges.iterator();

        while (iterator.hasNext()) {
            Edge edge = iterator.next();

            if (edge.start.equals(node)) {
                array.add(edge.end);
            }
            else if (edge.end.equals(node)) {
                array.add(edge.start);
            }
        }

        return Optional.of(array);
    }

    /**
     * This method is for Oriented Graph, it return the in-degree of a node or an empty Optional
     */
    public Optional<Integer> degreeInNode(Node node) {
        if (type == GraphType.UnorientedGraph) {
            return Optional.empty();
        }
        else {
            Iterator<Edge> iterator = edges.iterator();
            int degreeIn = 0;

            while (iterator.hasNext()) {
                Edge edge = iterator.next();

                if (edge.end.equals(node)) {
                    degreeIn++;
                }
            }
            return Optional.of(degreeIn);
        }
    }

    /**
     * This method is for Unoriented Graph, it return the out-degree of a node or an empty Optional
     */
    public Optional<Integer> degreeOutNode(Node node) {
        if (type == GraphType.UnorientedGraph) {
            return Optional.empty();
        }
        else {
            Iterator<Edge> iterator = edges.iterator();
            int degreeOut = 0;

            while (iterator.hasNext()) {
                Edge edge = iterator.next();

                if (edge.start.equals(node)) {
                    degreeOut++;
                }
            }
            return Optional.of(degreeOut);
        }
    }

    /**
     * This method return the degree of a node.
     */
    public int degreeNode(Node node) {
        if (type == GraphType.OrientedGraph) {
            return degreeInNode(node).get() + degreeOutNode(node).get();
        }
        else {
            return getNeighbors(node).get().size();
        }
    }

    /**
     * @return the degree of the Graph
     */
    public int degree() {
        int degree = 0;

        for (Node node: nodes) {
            int degreeNode = degreeNode(node);

            if(degree < degreeNode) {
                degree = degreeNode;
            }
        }

        return degree;
    }
}
