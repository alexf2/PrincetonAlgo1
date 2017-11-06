import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private static final int CAPACITY = 10;
    
    private Item[] items;
    private int qSize;
    
    @SuppressWarnings("unchecked")
    public RandomizedQueue() {
        items = (Item[]) new Object[CAPACITY];
    }
    
    public boolean isEmpty() {
        return qSize == 0;
    }
    
    public int size() {
        return qSize;
    }
    
    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException();
        
        if (qSize == items.length)
            resize(2 * items.length);
        items[qSize++] = item;
    }
    
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        
        int idx = StdRandom.uniform(0, qSize);
        
        Item res = items[idx];        
        if (idx < qSize - 1)
        {
            items[idx] = items[qSize - 1];
            items[qSize - 1] = null;
        }
        else
            items[idx] = null;
        
        --qSize;
        if (qSize > 0 && qSize == items.length/4) 
            resize(items.length / 2);
        
        return res;
    }
    
    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        assert capacity >= qSize;
        Item[] temp = (Item[]) new Object[capacity];
        System.arraycopy(items, 0, temp, 0, qSize);
        items = temp;
    }
    
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        
        return items[StdRandom.uniform(0, qSize)];
    }
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator(items, qSize);
    }       
    
    private final class RandomizedQueueIterator implements Iterator<Item> {
        private int current;
        private Item[] itItems;
        
        @SuppressWarnings("unchecked")
        RandomizedQueueIterator(Item[] data, int dataCount)
        {
            if (data == null || data.length == 0)
                itItems = (Item[]) new Object[ 0 ];
            else {            
                itItems = (Item[]) new Object[ dataCount ];
                System.arraycopy(data, 0, itItems, 0, dataCount);
                StdRandom.shuffle(itItems);
            }
        }
        
        public boolean hasNext() {
            return current < itItems.length;
        }
        
        public void remove() { throw new UnsupportedOperationException(); }
        
        public Item next() {
            if (!hasNext()) 
                throw new NoSuchElementException();
            
            return itItems[current++];
        }
    }
}
