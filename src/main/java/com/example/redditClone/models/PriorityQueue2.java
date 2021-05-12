package com.example.redditClone.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PriorityQueue2<T extends Comparable<T>> {

    // the heap is generic that assumes the type passed during initialization
    private List<T> heap = null;

    // initialize the pq with an initial size
    public PriorityQueue2() {
        heap = new ArrayList<>();
    }


    public PriorityQueue2(T[] elements) {
        int heapSize = elements.length;
        heap = new ArrayList<>(heapSize);
        for (int i = 0; i < heapSize; i++) {
            heap.add(elements[i]);
        }

        // heapify the queue
        for (int i = (heapSize / 2) - 1; i >= 0; i--) {
            sink(i);
        }
    }


    public PriorityQueue2(Collection<T> elements) {
        int heapSize = elements.size();
        heap = new ArrayList<>(heapSize);
        heap.addAll(elements);

        // heapify the queue
        for (int i = (heapSize / 2) - 1; i >= 0; i--) {
            sink(i);
        }
    }


    public void insert(T value) {
        if (contains(value)) {
            throw new IllegalArgumentException("Element already in queue");
        }
        if (size() == 0) {
            heap.add(value);
            return;
        }

        heap.add(value);
        // position of last element (equally position of newly inserted element)
        int lastIndex = size() - 1;

        swim(lastIndex);

    }


    // Test if an element is in heap, O(n)
    public boolean contains(T elem) {
        // Linear scan to check containment
        for (int i = 0; i < size(); i++)
            if (heap.get(i).equals(elem)) return true;
        return false;
    }


    // Perform bottom up node swim, O(log(n))
    private void swim(int k) {

        // Grab the index of the parent WRT last element
        // i.e the just inserted element's parent
        int parent = (k - 1) / 2;

        // Keep swimming while we have not reached the
        // root and while we're greater than our parent.
        // coz it is a max heap parent is at 0
        while (k > 0 && greater(k, parent)) {
            // Exchange k with the parent
            swap(parent, k);

            // new position of last element
            // i.e try to push the element up if possible by
            // calculating next parent from it's current position
            k = parent;
            // Grab the index of the next parent node WRT to the element being swapped
            parent = (k - 1) / 2;
        }
    }


    // Swap two nodes. Assumes i & j are valid, O(1)
    private void swap(int i, int j) {
        T elem_i = heap.get(i);
        T elem_j = heap.get(j);

        heap.set(i, elem_j);
        heap.set(j, elem_i);
    }

    // Returns true/false depending on if the priority queue is empty
    public boolean isEmpty() {
        return size() == 0;
    }

    // Clears everything inside the heap, O(n)
    public void clear() {
        heap.clear();
    }

    // Return the size of the heap
    public int size() {
        return heap.size();
    }


    // Returns the value of the element with the highest
    // priority in this priority queue. If the priority
    // queue is empty null is returned.
    public T peek() {
        if (isEmpty()) return null;
        return heap.get(0);
    }


    // Removes the root of the heap, O(log(n))
    public T poll() {
        return removeAt(0);
    }


    // Tests if the value of node i < node j
    // This method assumes i & j are valid indices, O(1)
    private boolean greater(int i, int j) {
        T node1 = heap.get(i);
        T node2 = heap.get(j);
        // returns true/false
        return node1.compareTo(node2) > 0;
    }

    // helper function to remove an element at most probably the root
    // Removes a node at particular index, O(log(n))
    private T removeAt(int i) {
        if (isEmpty()) return null;

        // the last element takes the position of removed node (root)
        // the element has to bubble down until it's in current position
        int indexOfLastElem = size() - 1;

        // get the element to be removed
        // using the index passed (it is actually the root)

        T removed_data = heap.get(i);

        // only one element in the heap
        // no need to swap and carry out sink operations
        if (indexOfLastElem == 0) {
            heap.remove(indexOfLastElem);
            return removed_data;

        }

        // swap the last element into place of root element
        // the last element becomes the root
        swap(i, indexOfLastElem);

        // Obliterate the value
        // the root was swapped with initial last element so this is actually

        // removing the root element coz it was moved to the last position in the heap
        heap.remove(indexOfLastElem);

        // Try sinking element
        sink(i);

        return removed_data;
    }

    // Top down node sink, O(log(n))
    private void sink(int k) {
        // get the heap size
        int heapSize = size();
        int left = 2 * k + 1; // Left  node
        int right = 2 * k + 2; // Right node
        int largest = k; // Assume left is the smallest node of the two children

        // Find which is smaller left or right
        // If right is smaller set smallest to be right
        if (right < heapSize && greater(right, largest)) largest = right;

        // left is in bounds of tree and left greater than right, then right is largest
        if (left < heapSize && greater(left, largest)) largest = left;

        // Move down the tree following the largest node
        if (largest != k) {
            swap(largest, k);
            k = largest;
            sink(k);
        }
    }


    public void printPriorityQueue() {
        for (int i = 0; i < size(); i++) {
            System.out.print(heap.get(i) + " ");
        }
    }

    public static void main(String[] args) {
        PriorityQueue2 a = new PriorityQueue2();
//        a.insert(12);
//        a.insert(7);
//        a.insert(10);
//        a.insert(1);
//        a.insert(8);

//        a.printPriorityQueue();

        System.out.println();
//        System.out.println(a.poll());
//        a.printPriorityQueue();
//
//        System.out.println();
//        System.out.println(a.remove());
//        a.printPriorityQueue();

//        System.out.println();
//        System.out.println(a.getPeek());
//        a.printPriorityQueue();
    }


}