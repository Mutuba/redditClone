package com.example.redditClone.models;

import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;

public class PriorityQueue2Test {


    @Before
    public void setup() {
    }

    @Test
    public void testEmpty() {
        PriorityQueue2<Integer> q = new PriorityQueue2<>();
        assertThat(q.size()).isEqualTo(0);
        assertThat(q.isEmpty()).isTrue();
        assertThat(q.poll()).isNull();
        assertThat(q.peek()).isNull();
    }


    @Test
    public void testAddOneElement() {
        PriorityQueue2<Integer> q = new PriorityQueue2<>();
        q.insert(12);
        q.insert(15);
        q.insert(20);
        assertThat(q.size()).isEqualTo(3);
        assertThat(q.isEmpty()).isFalse();
        assertThat(q.poll()).isEqualTo(20);
        assertThat(q.peek()).isNotNull();


    }

    @Test
    public void testHeapProperty() {

        PriorityQueue2<Integer> q = new PriorityQueue2<>();
        Integer[] nums = {3, 2, 5, 6, 7, 9, 4, 8, 1};

        // populate and test max heap
        for (int n : nums) q.insert(n);
        for (int i = 9; i <= 1; i++) assertThat(q.poll()).isEqualTo(i);
        q.printPriorityQueue();

        q.clear();

        // heapify constructor i.e one that takes an array of elements
        // and calls sink ((q.size()/2)-1)
        q = new PriorityQueue2<>(nums);
        for (int i = 9; i <= 1; i++) assertThat(q.poll()).isEqualTo(i);
    }

}
