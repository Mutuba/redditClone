package com.example.redditClone.models;

import org.junit.*;
public class BinaryTreeTest {

    @Test
    public void testGetRootReturnsRootValue() {
        Node node = new Node(15);
        BinaryTree binaryTree = new BinaryTree(node);
        binaryTree.insert(20);
        Assert.assertEquals(15, binaryTree.getRoot().getData());


    }


    @Test
    public void givenABinaryTree_WhenAddingElements_ThenTreeContainsThoseElements() {
        Node node = new Node(15);
        BinaryTree binaryTree = new BinaryTree(node);
        binaryTree.insert(20);
        Assert.assertTrue(binaryTree.containsNode(20));

    }

    @Test
    public void givenABinaryTree_WhenDeletingElements_ThenTreeDoesNotContainThoseElements() {
        Node node = new Node(50);
        BinaryTree binaryTree = new BinaryTree(node);
        binaryTree.insert(80);

        binaryTree.insert(45);
        binaryTree.insert(75);
        binaryTree.insert(40);
        binaryTree.insert(60);
        binaryTree.insert(30);


        Assert.assertTrue(binaryTree.containsNode(45));
        binaryTree.delete(60);
        Assert.assertFalse(binaryTree.containsNode(60));
        binaryTree.printInorder();

    }
}
