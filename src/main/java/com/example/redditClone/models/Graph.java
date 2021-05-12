package com.example.redditClone.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Graph {

    // HashSet's set implementation
    // idea is a graph contains a set of nodes
    private Set<NodeDJ> nodes = new HashSet<>();

    public void addNode(NodeDJ nodeA) {
        nodes.add(nodeA);
    }

    public Set<NodeDJ> getNodes() {
        return nodes;
    }

    public void setNodes(Set<NodeDJ> nodes) {
        this.nodes = nodes;
    }

    public static void main(String [] args){

        NodeDJ nodeA = new NodeDJ("A");
        NodeDJ nodeB = new NodeDJ("B");
        NodeDJ nodeC = new NodeDJ("C");
        NodeDJ nodeD = new NodeDJ("D");
        NodeDJ nodeE = new NodeDJ("E");
        NodeDJ nodeF = new NodeDJ("F");

        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 15);

        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);

        nodeC.addDestination(nodeE, 10);

        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);

        nodeF.addDestination(nodeE, 5);

        Graph graph = new Graph();

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);

        graph = Dijkstra.calculateShortestPathFromSource(graph, nodeA);
        System.out.println("SLution " + graph.getNodes());
        System.out.println("Shortest path from source: " + Arrays.toString(nodeA.getShortestPath().toArray()));
    }
}
