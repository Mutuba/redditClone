package com.example.redditClone.models;

public class BinaryTree {

    private Node root;

    public BinaryTree(Node root) {
        this.root = root;
    }


    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }


    public void insert(int data) {
        Node rootNode = getRoot();
        addRecursive(rootNode, data);
    }

    private Node addRecursive(Node node, int value) {
        if (node == null) {
            return new Node(value);
        }

        if (value < node.getData()) {
            // returns a left node i.e create a left node WRT current node
            node.setLeft(addRecursive(node.getLeft(), value));
        }
        else{
            node.setRight(addRecursive(node.getRight(), value));
        }

        return node;
    }


    public void printInorder() {
        Node rootNode = getRoot();
        inOrder(rootNode);

    }

    // Traverses given tree in Inorder fashion and
    // prints all nodes that have both children as
    // non-empty.
    private void inOrder(Node node) {

        if (node == null) return;
        inOrder(node.getLeft());
        System.out.printf("%s ", node.getData());

        inOrder(node.getRight());
    }


    // Traverses given tree in preOrder fashion and
    // prints all nodes that have both children as
    // non-empty.
    public void preOrder(Node node) {

        if (node == null) return;
        System.out.printf("%s ", node.getData());
        preOrder(node.getLeft());
        preOrder(node.getRight());
    }


    // Traverses given tree in postOrder fashion and
    // prints all nodes that have both children as
    // non-empty.
    public void postOrder(Node node) {

        if (node == null) return;
        postOrder(node.getLeft());
        postOrder(node.getRight());
        System.out.printf("%s ", node.getData());
    }


    public boolean containsNode(int value) {
        return containsNodeRecursive(getRoot(), value);
    }


    private boolean containsNodeRecursive(Node current, int value) {
        if (current == null) {
            return false;
        }
        if (value == current.getData()) {
            return true;
        }
        return value < current.getData()
                ? containsNodeRecursive(current.getLeft(), value)
                : containsNodeRecursive(current.getRight(), value);
    }


    public void delete(int value) {
        root = deleteRecursive(root, value);
    }

    private Node deleteRecursive(Node node, int value) {
        if (node == null) {
            return null;
        }

        // we go left to look for the value to delete
        if(value < node.getData()){
            // call the delete function function recursively to continue looking
            // for the value as we do not have it yet
            node.setLeft(deleteRecursive(node.getLeft(), value));
        }
        // we go right to look for value to delete
        if(value > node.getData()){
            // call the delete function function recursively to continue looking
            // for the value as we do not have it yet
            node.setRight(deleteRecursive(node.getRight(), value));
        }

        // node is root possibly
        else{

            if (value == node.getData()) {
                // Node to delete found and has no left or right child
                if (node.getLeft() == null && node.getRight() == null) {
                    return null;
                }

                // tree has one child i.e either the left or right child
                if (node.getRight() == null) {
                    return node.getLeft();
                }

                if (node.getLeft() == null) {
                    return node.getRight();
                }

                // has both left and right child, replace the node with smallest node from right
                // subtree and delete the node in the right subtree
                int smallestValue = findSmallestValue(node.getRight());
                node.setData(smallestValue);
                // remove the smallest value from the right subtree
                return deleteRecursive(node.getRight(), smallestValue);

            }

        }

        return node;
    }


    // the smallest is always possibly on the left for a BST
    // and if we are deleting the parent node the smallest should replace the parent
    private int findSmallestValue(Node node) {
        // if no left child then the node is the smallest, else recursively look for smallest in the left subtree
        return node.getLeft() == null ? node.getData() : findSmallestValue(node.getLeft());
    }

    public static void main(String[] args) {
        Node node = new Node(20);
        BinaryTree binaryTree = new BinaryTree(node);

        binaryTree.insert(30);
        binaryTree.insert(35);

        binaryTree.insert(39);
        binaryTree.insert(40);
        binaryTree.insert(50);
        binaryTree.insert(60);


        binaryTree.inOrder(node);
    }
}

























