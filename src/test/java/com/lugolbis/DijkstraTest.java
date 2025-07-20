package com.lugolbis;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.lugolbis.dsa.Graph;
import com.lugolbis.dsa.GraphData;
import com.lugolbis.dsa.GraphType;
import com.lugolbis.algorithms.Dijkstra;
import com.lugolbis.algorithms.DijkstraData;


class DijkstraTest {
    static class TestNodeValue implements GraphData {
        private final String id;

        public TestNodeValue(String id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof TestNodeValue)) return false;
            return id.equals(((TestNodeValue) obj).id);
        }

        @Override
        public String toString() {
            return id;
        }
    }

    static class TestEdge implements DijkstraData, GraphData {
        private final Double weight;
        public TestEdge(Double weight) { this.weight = weight; }
        @Override public double getDouble() { return weight; }
        @Override
        public int hashCode() { return weight.hashCode(); }
        @Override
        public boolean equals(Object o) { return weight.equals(o); }
        @Override
        public String toString() { return weight.toString(); }
    }

    @Test
    void testOrientedGraph() {
        Graph<TestNodeValue, TestEdge> graph = new Graph<>(GraphType.OrientedGraph);
        
        Graph<TestNodeValue, TestEdge>.Node nodeA = graph.addNode(new TestNodeValue("A"));
        Graph<TestNodeValue, TestEdge>.Node nodeB = graph.addNode(new TestNodeValue("B"));
        Graph<TestNodeValue, TestEdge>.Node nodeC = graph.addNode(new TestNodeValue("C"));
        
        graph.addEdge(nodeA, nodeB, new TestEdge(2.0));
        graph.addEdge(nodeB, nodeC, new TestEdge(3.0));
        graph.addEdge(nodeA, nodeC, new TestEdge(10.0));

        Dijkstra<TestNodeValue, TestEdge> dijkstra = Dijkstra.main(graph, nodeA);

        assertEquals(0.0, dijkstra.getDistances().get(nodeA));
        assertEquals(2.0, dijkstra.getDistances().get(nodeB));
        assertEquals(5.0, dijkstra.getDistances().get(nodeC));
        assertEquals(nodeB, dijkstra.getPredecessors().get(nodeC));
    }

    @Test
    void testUnorientedGraph() {
        Graph<TestNodeValue, TestEdge> graph = new Graph<>(GraphType.UnorientedGraph);
        
        Graph<TestNodeValue, TestEdge>.Node nodeA = graph.addNode(new TestNodeValue("A"));
        Graph<TestNodeValue, TestEdge>.Node nodeB = graph.addNode(new TestNodeValue("B"));
        Graph<TestNodeValue, TestEdge>.Node nodeC = graph.addNode(new TestNodeValue("C"));
        
        graph.addEdge(nodeA, nodeB, new TestEdge(1.0));
        graph.addEdge(nodeB, nodeC, new TestEdge(2.0));
        graph.addEdge(nodeA, nodeC, new TestEdge(5.0));

        Dijkstra<TestNodeValue, TestEdge> dijkstra = Dijkstra.main(graph, nodeA);
        
        assertEquals(0.0, dijkstra.getDistances().get(nodeA));
        assertEquals(1.0, dijkstra.getDistances().get(nodeB));
        assertEquals(3.0, dijkstra.getDistances().get(nodeC));
        assertEquals(nodeB, dijkstra.getPredecessors().get(nodeC));
    }

    @Test
    void testNoPath() {
        Graph<TestNodeValue, TestEdge> graph = new Graph<>(GraphType.OrientedGraph);
        
        Graph<TestNodeValue, TestEdge>.Node nodeA = graph.addNode(new TestNodeValue("A"));
        Graph<TestNodeValue, TestEdge>.Node nodeB = graph.addNode(new TestNodeValue("B"));
        Graph<TestNodeValue, TestEdge>.Node nodeC = graph.addNode(new TestNodeValue("C"));
        
        graph.addEdge(nodeA, nodeB, new TestEdge(2.0));

        Dijkstra<TestNodeValue, TestEdge> dijkstra = Dijkstra.main(graph, nodeA);
        
        assertEquals(Double.POSITIVE_INFINITY, dijkstra.getDistances().get(nodeC));
        assertNull(dijkstra.getPredecessors().get(nodeC));
        assertEquals(0.0, dijkstra.getDistances().get(nodeA));
        assertEquals(2.0, dijkstra.getDistances().get(nodeB));
    }
}
