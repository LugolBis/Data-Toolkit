package com.lugolbis;

import com.lugolbis.dsa.GraphData;
import com.lugolbis.dsa.Graph;
import com.lugolbis.dsa.GraphType;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

public class GraphTest {
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

    static class TestEdgeValue implements GraphData {
        private final int weight;

        public TestEdgeValue(int weight) {
            this.weight = weight;
        }

        @Override
        public int hashCode() {
            return weight;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof TestEdgeValue)) return false;
            return weight == ((TestEdgeValue) obj).weight;
        }

        @Override
        public String toString() {
            return String.valueOf(weight);
        }
    }

    @Test
    @DisplayName("Oriented Graph")
    void testOrientedGraph() {
        Optional<Graph<TestNodeValue, TestEdgeValue>> result = Graph.<TestNodeValue, TestEdgeValue>newGraph(GraphType.OrientedGraph);

        assertTrue(result.isPresent());

        Graph<TestNodeValue, TestEdgeValue> graph = result.get();
        
        Graph<TestNodeValue, TestEdgeValue>.Node nodeA = graph.addNode(new TestNodeValue("A")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeB = graph.addNode(new TestNodeValue("B")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeC = graph.addNode(new TestNodeValue("C")).get();

        graph.addEdge(nodeA, nodeB);
        graph.addEdge(nodeB, nodeC);
        graph.addEdge(nodeA, nodeC);
        
        Assertions.assertTrue(graph.getEdge(nodeA, nodeB).isPresent());
        Assertions.assertTrue(graph.getEdge(nodeB, nodeC).isPresent());
        Assertions.assertTrue(graph.getEdge(nodeC, nodeA).isEmpty());

        Optional<HashSet<Graph<GraphTest.TestNodeValue,GraphTest.TestEdgeValue>.Node>> successors = graph.getSuccessors(nodeA);
        Optional<HashSet<Graph<GraphTest.TestNodeValue,GraphTest.TestEdgeValue>.Node>> predeccessors = graph.getPredeccessors(nodeC);
        
        if (successors.isEmpty() || predeccessors.isEmpty()) {
            Assertions.assertTrue(false);
        }
        else {
            HashSet<Graph<GraphTest.TestNodeValue,GraphTest.TestEdgeValue>.Node> hashset_s = new HashSet<>();
            HashSet<Graph<GraphTest.TestNodeValue,GraphTest.TestEdgeValue>.Node> hashset_p = new HashSet<>();

            hashset_s.add(nodeB); hashset_s.add(nodeC);
            hashset_p.add(nodeA); hashset_p.add(nodeB);

            Assertions.assertEquals(successors.get(), hashset_s);
            Assertions.assertEquals(predeccessors.get(), hashset_p);
        }
    }

    @Test
    @DisplayName("Unoriented Graph")
    void testUnorientedGraph() {
        Optional<Graph<TestNodeValue, TestEdgeValue>> result = Graph.<TestNodeValue, TestEdgeValue>newGraph(GraphType.UnorientedGraph);

        assertTrue(result.isPresent());

        Graph<TestNodeValue, TestEdgeValue> graph = result.get();
        
        Graph<TestNodeValue, TestEdgeValue>.Node nodeX = graph.addNode(new TestNodeValue("X")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeY = graph.addNode(new TestNodeValue("Y")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeZ = graph.addNode(new TestNodeValue("Z")).get();

        graph.addEdge(nodeX, nodeY);
        graph.addEdge(nodeY, nodeZ);

        Assertions.assertTrue(graph.getEdge(nodeY, nodeX).isPresent());
        Assertions.assertTrue(graph.getEdge(nodeZ, nodeY).isPresent());

        Optional<HashSet<Graph<GraphTest.TestNodeValue,GraphTest.TestEdgeValue>.Node>> neighbors = graph.getNeighbors(nodeY);

        if (neighbors.isEmpty()) {
            Assertions.assertTrue(false);
        }
        else {
            HashSet<Graph<GraphTest.TestNodeValue,GraphTest.TestEdgeValue>.Node> hashset = new HashSet<>();
            hashset.add(nodeZ); hashset.add(nodeX);

            Assertions.assertEquals(neighbors.get(), hashset);
        }
    }

    @Test
    @DisplayName("Edge removal")
    void testEdgeRemoval() {
        Optional<Graph<TestNodeValue, TestEdgeValue>> result = Graph.<TestNodeValue, TestEdgeValue>newGraph(GraphType.OrientedGraph);

        assertTrue(result.isPresent());

        Graph<TestNodeValue, TestEdgeValue> graph = result.get();
        
        Graph<TestNodeValue, TestEdgeValue>.Node nodeA = graph.addNode(new TestNodeValue("A")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeB = graph.addNode(new TestNodeValue("B")).get();

        Graph<TestNodeValue, TestEdgeValue>.Edge edge = graph.addEdge(nodeA, nodeB).get();
        
        graph.removeEdge(edge);
        Assertions.assertTrue(graph.getEdge(nodeA, nodeB).isEmpty());
    }

    @Test
    @DisplayName("Node removal")
    void testNodeRemoval() {
        Optional<Graph<TestNodeValue, TestEdgeValue>> result = Graph.<TestNodeValue, TestEdgeValue>newGraph(GraphType.OrientedGraph);

        assertTrue(result.isPresent());

        Graph<TestNodeValue, TestEdgeValue> graph = result.get();
        
        Graph<TestNodeValue, TestEdgeValue>.Node nodeA = graph.addNode(new TestNodeValue("A")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeB = graph.addNode(new TestNodeValue("B")).get();
        
        graph.addEdge(nodeA, nodeB);
        graph.removeNode(nodeA);
        
        Assertions.assertTrue(graph.getNode(new TestNodeValue("A")).isEmpty());
        Assertions.assertTrue(graph.getEdge(nodeA, nodeB).isEmpty());
    }

    @Test
    @DisplayName("Degree Calculations")
    void testDegreeCalculations() {
        Optional<Graph<TestNodeValue, TestEdgeValue>> result = Graph.<TestNodeValue, TestEdgeValue>newGraph(GraphType.OrientedGraph);

        assertTrue(result.isPresent());

        Graph<TestNodeValue, TestEdgeValue> graph = result.get();
        
        Graph<TestNodeValue, TestEdgeValue>.Node nodeA = graph.addNode(new TestNodeValue("A")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeB = graph.addNode(new TestNodeValue("B")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeC = graph.addNode(new TestNodeValue("C")).get();
        
        graph.addEdge(nodeA, nodeB);
        graph.addEdge(nodeB, nodeC);
        graph.addEdge(nodeC, nodeA);

        Optional<Integer> degreeIn = graph.degreeOutNode(nodeA);
        Optional<Integer> degreeOut = graph.degreeOutNode(nodeA);
        int degree = graph.degreeNode(nodeA);

        if (degreeIn.isEmpty() || degreeOut.isEmpty()) {
            Assertions.assertTrue(false);
        }
        else {
            Assertions.assertEquals(degreeIn.get(), 1);
            Assertions.assertEquals(degreeOut.get(), 1);
            Assertions.assertEquals(degree, 2);
        }
    }

    @Test
    @DisplayName("Edge with value")
    void testEdgeWithValue() {
        Optional<Graph<TestNodeValue, TestEdgeValue>> result = Graph.<TestNodeValue, TestEdgeValue>newGraph(GraphType.UnorientedGraph);

        assertTrue(result.isPresent());

        Graph<TestNodeValue, TestEdgeValue> graph = result.get();
        
        Graph<TestNodeValue, TestEdgeValue>.Node nodeA = graph.addNode(new TestNodeValue("A")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeB = graph.addNode(new TestNodeValue("B")).get();
        
        TestEdgeValue edgeValue = new TestEdgeValue(5);
        graph.addEdge(nodeA, nodeB, edgeValue);

        Optional<Graph<GraphTest.TestNodeValue,GraphTest.TestEdgeValue>.Edge> edge = graph.getEdge(nodeA, nodeB);
        
        if (edge.isPresent()) {
            Optional<GraphTest.TestEdgeValue> value = edge.get().getValue();

            if (value.isPresent()) {
                Assertions.assertEquals(value.get().weight, 5);
            }
            else {
                Assertions.assertTrue(false);
            }
        }
        else {
            Assertions.assertTrue(false);
        }
    }

    @Test
    @DisplayName("Don't exist a target Node")
    void testNonExistentElements() {
        Optional<Graph<TestNodeValue, TestEdgeValue>> result = Graph.<TestNodeValue, TestEdgeValue>newGraph(GraphType.OrientedGraph);

        assertTrue(result.isPresent());

        Graph<TestNodeValue, TestEdgeValue> graph = result.get();
        
        graph.addNode(new TestNodeValue("A"));
        
        Assertions.assertTrue(graph.getNode(new TestNodeValue("B")).isEmpty());
    }

    @Test
    @DisplayName("Test Matrice")
    void testMatrice() {
        Optional<Graph<TestNodeValue, TestEdgeValue>> result = Graph.<TestNodeValue, TestEdgeValue>newGraph(GraphType.OrientedGraph);

        assertTrue(result.isPresent());

        Graph<TestNodeValue, TestEdgeValue> graph = result.get();
        
        Graph<TestNodeValue, TestEdgeValue>.Node nodeA = graph.addNode(new TestNodeValue("A")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeB = graph.addNode(new TestNodeValue("B")).get();
        Graph<TestNodeValue, TestEdgeValue>.Node nodeC = graph.addNode(new TestNodeValue("C")).get();
        
        graph.addEdge(nodeA, nodeB, new TestEdgeValue(1));
        graph.addEdge(nodeB, nodeC, new TestEdgeValue(9));
        graph.addEdge(nodeC, nodeA, new TestEdgeValue(43));

        HashMap<TestNodeValue, ArrayList<Optional<TestEdgeValue>>> map = new HashMap<>();

        ArrayList<Optional<TestEdgeValue>> arrayA = new ArrayList<>();
        arrayA.add(Optional.empty()); arrayA.add(Optional.of(new TestEdgeValue(1))); arrayA.add(Optional.empty());
        map.put(new TestNodeValue("A"), arrayA);

        ArrayList<Optional<TestEdgeValue>> arrayB = new ArrayList<>();
        arrayB.add(Optional.empty()); arrayB.add(Optional.empty()); arrayB.add(Optional.of(new TestEdgeValue(9)));
        map.put(new TestNodeValue("B"), arrayB);

        ArrayList<Optional<TestEdgeValue>> arrayC = new ArrayList<>();
        arrayC.add(Optional.of(new TestEdgeValue(43))); arrayC.add(Optional.empty()); arrayC.add(Optional.empty());
        map.put(new TestNodeValue("C"), arrayC);

        Assertions.assertEquals(map, graph.getMatrice());
    }
}