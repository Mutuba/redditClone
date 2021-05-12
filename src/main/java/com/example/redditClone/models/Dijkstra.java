package com.example.redditClone.models;

import java.util.*;


public class Dijkstra {

    public static Graph calculateShortestPathFromSource( Graph graph, NodeDJ source ){
        source.setDistance(0);
        Set<NodeDJ> settledNodes =  new HashSet<>();
        Set<NodeDJ> unsettledNodes =  new HashSet<>();
        unsettledNodes.add(source);

        // only when it is zero then we have processed all nodes
        while (!unsettledNodes.isEmpty()){
            NodeDJ currentNode = getShortestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for(Map.Entry<NodeDJ, Integer> adjacencyPair: currentNode.getAdjacentNodes().entrySet()){

                NodeDJ adjacentNode = adjacencyPair.getKey();
                int edgeWeight  = adjacencyPair.getValue();

                if(!settledNodes.contains(adjacentNode)){
                    // get the shortest distance from currentNode to adjacentNode
                    // example A -> B, edgeWeight of 5
                    calculateShortestDistance(currentNode, adjacentNode, edgeWeight);
                    // by adding it to unsettled nodes, there is a chance it will be settled
                    unsettledNodes.add(adjacentNode);
                }
            }

            // after relaxing adjacent nodes, add current element to settled nodes list
            settledNodes.add(currentNode);

        }

        return graph;

    }


    public static NodeDJ getShortestDistanceNode(Set<NodeDJ> unsettledNodes){
        NodeDJ lowestDistanceNode = null;
        // given unsettled nodes, start with infinite as lowest
        // it safe coz we loop over all unsettled nodes
        int lowestDistance = Integer.MAX_VALUE;

        for (NodeDJ node : unsettledNodes){
            int currentNodeDistance = node.getDistance();
            if(currentNodeDistance < lowestDistance){
                lowestDistance= currentNodeDistance;
                lowestDistanceNode = node;
            }
        }

        return lowestDistanceNode;
    }
    public static void calculateShortestDistance(NodeDJ source, NodeDJ evaluationNode, int edgeWeight){
        // get source node distance
        int sourceDistance= source.getDistance();
        if(sourceDistance+edgeWeight< evaluationNode.getDistance()){
            // d(A) + cost(a->B) < d(B), then perform relaxation(update distance to B and set A as SP of B)
            evaluationNode.setDistance(sourceDistance+edgeWeight);
            // Could be empty, the shortest path to A, must have A, as it's true that at the
            // shortest path is self
            List<NodeDJ> shortestPath = source.getShortestPath();
            shortestPath.add(source);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
}