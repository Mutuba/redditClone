package com.example.redditClone.models;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NodeDJ {
    private String name;
    private int distance = Integer.MAX_VALUE;
    private List<NodeDJ> shortestPath = new LinkedList<>();
    private Map<NodeDJ, Integer> adjacentNodes = new HashMap<>();

    public NodeDJ(String name) {
        this.name = name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setShortestPath(List<NodeDJ> shortestPaths) {
        this.shortestPath = shortestPaths;
    }

    public void addDestination( NodeDJ destinationNode, int distance){
        adjacentNodes.put(destinationNode, distance);

    }
    public void setAdjacentNodes(Map<NodeDJ, Integer> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return distance;
    }

    public List<NodeDJ> getShortestPath() {
        return shortestPath;
    }

    public Map<NodeDJ, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }
}
