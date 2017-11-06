import edu.princeton.cs.algs4.WeightedQuickUnionUF;
//import edu.princeton.cs.algs4.StdIn;

import java.util.HashSet;

/**
 * 
 * Represents a NxN lattice with an ability to model percollation.  
 * 
 * @author Aleksey Fedorov
 *
 */
public class Percolation {

    private static final int MAX_SIZE = (int) Math.sqrt(Integer.MAX_VALUE - 8 /*for array header*/) - 2;
    /*2 - is a reserv for virtual top and bottom*/
    private static final int VTOP = 0;
    
    private final WeightedQuickUnionUF collator;
    private final int n;
    
    private final int vBottom;
    private final HashSet<Integer> opened = new HashSet<Integer>();
    
    /**
     * Initializes N-by-N lattice, with all sites blocked.
     * 
     * @param N the lattice size 
     */
    public Percolation(int N)
    {        
        if (N < 1)
            throw new IllegalArgumentException(
                    String.format("Lattice size is illegal: %d. It shouild be greater, than 0.", N));
        
        if (N > MAX_SIZE)
            throw new IllegalArgumentException(
                    String.format("Lattice size is too big: %d. It shouild not exceed %d.", N, MAX_SIZE));
        
        n = N;
        collator = new WeightedQuickUnionUF(N*N + 2);
        vBottom = toLattice(N, N) + 1;
    }
    

    private int toLattice(int row, int col)
    {
        return row*n + col - n;
    }
    
    private void validateCell(int row, int col)
    {
        if (row < 1 || row > n)
            throw new IndexOutOfBoundsException(String.format("Row '%d' is out of range. Should be 1 - %d.", row, n));
        if (col < 1 || col > n)
            throw new IndexOutOfBoundsException(String.format("Column '%d' is out of range. Should be 1 - %d.", col, n));
    }
    
    /**
     * Opens a cell row, column.
     * 
     * @param i row number i >= 1
     * @param j column number j >= 1
     */
    public void open(int i, int j)
    {        
        validateCell(i, j);
        
        int id = toLattice(i, j);
        if (opened.contains(id))
            return;
        
        opened.add(id);
        
        if (i == 1)
            collator.union(id, VTOP);
        if (i == n)
            collator.union(vBottom, id);
        
        //left
        if (j > 1)
            connectTo(id, i, j - 1);
        //right
        if (j < n)
            connectTo(id, i, j + 1);
        //top
        if (i > 1)
            connectTo(id, i - 1, j);
        //bottom        
        if (i < n)
            connectTo(id, i + 1, j);
    }
    
    private void connectTo(int latticeId, int row, int col)
    {
        int id = toLattice(row, col);
        if (opened.contains(id))
            collator.union(latticeId, id);
    }
    
    /**
     * @param i row number i >= 1
     * @param j column number j >= 1
     * @return true if the cell i, j is open
     */
    public boolean isOpen(int i, int j)
    {
        validateCell(i, j);
        return opened.contains(toLattice(i, j));
    }
    
    /**
     * @param i row number i >= 1
     * @param j column number j >= 1
     * @return true if the cell i, j is open
     */
    public boolean isFull(int i, int j) 
    {
        validateCell(i, j);
        return collator.connected(toLattice(i, j), VTOP);
    }
    
    /**
     * @return true if the lattice percolates
     */
    public boolean percolates()
    {
        return collator.connected(vBottom, VTOP);
    }

    /**
     * @param args command line arguments
     */
    public static void main(String[] args) 
    {
        /*int N = StdIn.readInt();
        Percolation uf = new Percolation(N);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.isOpen(p, q)) continue;
            uf.open(p, q);            
        }
        StdOut.println(uf.percolates());*/
    }
}
