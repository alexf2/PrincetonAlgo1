import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
    
    private Node first, last;
    private int qSize;

    public Deque()
    {
    }
    
    public boolean isEmpty() {
        return last == null;
    }
    
    public int size() {
        return qSize;
    }
    public void addFirst(Item item) {
        if (item == null)
            throw new NullPointerException();
        
        
        Node nd = new Node(item);        
        
        if (first == null)
            first = last = nd;
        else
        {
            nd.next = first;        
            first.prev = nd;
            first = nd;
        }        
        ++qSize;
    }
    public void addLast(Item item) {
        if (item == null)
            throw new NullPointerException();
        
        Node nd = new Node(item);        
        if (last == null)
            first = last = nd;
        else
        {
            last.next = nd;
            nd.prev = last;
            last = nd;
        }
        ++qSize;
    }
    
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
                
        Node res = first;
        if (first == last)
        {
            first = last = null;
            qSize = 0;            
        }
        else {
            first = res.next;
            first.prev = null;
            res.next = null;
            --qSize;
        }
        return res.value;
    }
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        
        Node res = last;
        if (first == last)
        {
            first = last = null;
            qSize = 0;
        }
        else {
            last = res.prev;
            last.next = null;
            res.prev = null;
            --qSize;
        }
        return res.value;
    }
    public Iterator<Item> iterator() {
        return new DeqIterator(first);
    }
    
    public static void main(String[] args) {        
    }
    
    private final class Node {
        private Node prev, next;
        private Item value;
        
        Node(Item val)
        {
            value = val;
        }
    }
    
    private final class DeqIterator implements Iterator<Item> {
        private Node current;
        
        DeqIterator(Node start)
        {
            current = start;
        }
        
        public boolean hasNext() {
            return current != null;
        }
        
        public void remove() { throw new UnsupportedOperationException(); }
        
        public Item next() {
            if (!hasNext()) 
                throw new NoSuchElementException();
            
            Item item = current.value;
            current = current.next; 
            
            return item;
        }
    }
}
