package Util;

/**
 * WebPage queue is a interface of queue for handling the order of crawling
 */
public interface Queue {
    /**
     * Enqueue a element
     */
    void enqueue(String link);

    /**
     * dequeue a element
     * @return the element dequeued
     */
    String dequeue();

    /**
     * get size of the queue
     * @return size of remaining elements
     */
    int size();
}
