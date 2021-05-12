package com.example.redditClone.models;

class Node {
    private int data;
    private Node left;
    private   Node right;


    public Node(int data) {
        this.data = data;
        right = null;
        left = null;
    }

    public void setData(int value) {
        this.data = data;
    }

    public void setLeft(Node left) {
        this.left = left;
    }
    public void setRight(Node right) {
        this.right = right;
    }


    public int getData() {
        return data;
    }
    public Node getLeft() {
        return left;
    }
    public Node getRight() {
        return right;
    }
}
