package com.lugolbis;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

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

    static class TestEdgeValue implements DijkstraData, GraphData {
        private final Double weight;
        public TestEdgeValue(Double weight) { this.weight = weight; }
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
        Optional<Graph<TestNodeValue, TestEdgeValue>> result = Graph.<TestNodeValue, TestEdgeValue>newGraph(GraphType.OrientedGraph);

        assertTrue(result.isPresent());

        Graph<TestNodeValue, TestEdgeValue> graph = result.get();
        
        Graph<TestNodeValue, TestEdgeValue>.Node nodeA = graph.addNode(new TestNodeValue("A")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeB = graph.addNode(new TestNodeValue("B")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeC = graph.addNode(new TestNodeValue("C")).get();
        
        graph.addEdge(nodeA, nodeB, new TestEdgeValue(2.0));
        graph.addEdge(nodeB, nodeC, new TestEdgeValue(3.0));
        graph.addEdge(nodeA, nodeC, new TestEdgeValue(10.0));

        Optional<Dijkstra<TestNodeValue, TestEdgeValue>> resultB = Dijkstra.run(graph, nodeA);

        assertTrue(resultB.isPresent());
        Dijkstra<TestNodeValue, TestEdgeValue> dijkstra = resultB.get();

        assertEquals(0.0, dijkstra.getDistances().get(nodeA));
        assertEquals(2.0, dijkstra.getDistances().get(nodeB));
        assertEquals(5.0, dijkstra.getDistances().get(nodeC));
        assertEquals(nodeB, dijkstra.getPredecessors().get(nodeC));
    }

    @Test
    void testUnorientedGraph() {
        Optional<Graph<TestNodeValue, TestEdgeValue>> result = Graph.<TestNodeValue, TestEdgeValue>newGraph(GraphType.UnorientedGraph);

        assertTrue(result.isPresent());
        Graph<TestNodeValue, TestEdgeValue> graph = result.get();
        
        Graph<TestNodeValue, TestEdgeValue>.Node nodeA = graph.addNode(new TestNodeValue("A")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeB = graph.addNode(new TestNodeValue("B")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeC = graph.addNode(new TestNodeValue("C")).get();
        
        graph.addEdge(nodeA, nodeB, new TestEdgeValue(1.0));
        graph.addEdge(nodeB, nodeC, new TestEdgeValue(2.0));
        graph.addEdge(nodeA, nodeC, new TestEdgeValue(5.0));

        Optional<Dijkstra<TestNodeValue, TestEdgeValue>> resultB = Dijkstra.run(graph, nodeA);

        assertTrue(resultB.isPresent());
        Dijkstra<TestNodeValue, TestEdgeValue> dijkstra = resultB.get();
        
        assertEquals(0.0, dijkstra.getDistances().get(nodeA));
        assertEquals(1.0, dijkstra.getDistances().get(nodeB));
        assertEquals(3.0, dijkstra.getDistances().get(nodeC));
        assertEquals(nodeB, dijkstra.getPredecessors().get(nodeC));
    }

    @Test
    void testNoPath() {
        Optional<Graph<TestNodeValue, TestEdgeValue>> result = Graph.<TestNodeValue, TestEdgeValue>newGraph(GraphType.OrientedGraph);

        assertTrue(result.isPresent());

        Graph<TestNodeValue, TestEdgeValue> graph = result.get();
        
        Graph<TestNodeValue, TestEdgeValue>.Node nodeA = graph.addNode(new TestNodeValue("A")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeB = graph.addNode(new TestNodeValue("B")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeC = graph.addNode(new TestNodeValue("C")).get();
        
        graph.addEdge(nodeA, nodeB, new TestEdgeValue(2.0));

        Optional<Dijkstra<TestNodeValue, TestEdgeValue>> resultB = Dijkstra.run(graph, nodeA);

        assertTrue(resultB.isPresent());
        Dijkstra<TestNodeValue, TestEdgeValue> dijkstra = resultB.get();
        
        assertEquals(Double.POSITIVE_INFINITY, dijkstra.getDistances().get(nodeC));
        assertNull(dijkstra.getPredecessors().get(nodeC));
        assertEquals(0.0, dijkstra.getDistances().get(nodeA));
        assertEquals(2.0, dijkstra.getDistances().get(nodeB));
    }
}
