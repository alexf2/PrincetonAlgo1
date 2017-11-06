import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class Subset {
    
    public static void main(String[] args)
    {
        if (args.length != 1)
            throw new IllegalArgumentException();
        
        RandomizedQueue<String> q = new RandomizedQueue<String>();
                                      
        for (String s:StdIn.readAllStrings())
            q.enqueue(s);
        
        for (int i = 0; i < Integer.parseInt(args[0]); i++)
            StdOut.println(q.dequeue());
    }
}
