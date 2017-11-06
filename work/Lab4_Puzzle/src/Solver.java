import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;


public class Solver {
    private final Board Brd;
    
    private SearchNode Solution;
    private Iterable<Board> Result;
    
    private int MovesCount = -1;
    private Boolean IsSolavble;           
    
    private static final class SearchNode {
        public final SearchNode Prev;
        public final Board Brd;
        public final int Weight;
        public final int Weight2;
        public final int MovesCount;
        
        public SearchNode (final SearchNode prev, final Board brd, final int w, final int w2, int movesCnt) {
            Prev = prev;
            Brd = brd;
            Weight = w;
            Weight2 = w2;
            MovesCount = movesCnt;
        }
                        
        public boolean has(Board b) {  
        	int w = getWeight(b);
        	int w2 = getWeight2(b);
        	
            for (SearchNode n = this; n != null; n = n.Prev)
                if (w == getWeight(n.Brd) && w2 == getWeight2(n.Brd) && n.Brd.equals(b))                 	                	              
                    return true;
                
            return false;
        }
        
        @Override
        public boolean equals (final Object y) {
            if (y == this)
                return true;
            
            if (!(y instanceof SearchNode))
                return false;
            
            SearchNode obj = (SearchNode)y;
            
            return getWeight(Brd) == getWeight(obj.Brd) && 
                    getWeight2(Brd) == getWeight2(obj.Brd) /*&& obj.Brd.equals(Brd)*/;
        }
        
        private int Hs = 0;
        @Override
        public int hashCode() {
            if (Hs == 0) {
                int res = 17;
                res = 31*res + getWeight(Brd);
                res = 31*res + getWeight2(Brd);
                Hs = res;
            }
            return Hs;
        }
    }
    
    private final static class BoardNodesComparator implements Comparator<SearchNode> {        
        @Override
        public int compare(final SearchNode n1, final SearchNode n2) {
            int res = n1.Weight - n2.Weight;
            if (res == 0)
                res = n1.Weight2 - n2.Weight2;
            return res;
        }
    } 
    
    public Solver(final Board initial) {
        
        if (initial == null)
            throw new NullPointerException();
        
        Brd = initial;        
        Solve();
    }
    
    public boolean isSolvable() {
            
        if (IsSolavble == null) { //is set in Solve, so this code is not executed
            
            if (Brd.isGoal())
                IsSolavble = true;
            else {
                In in = new In(new Scanner(Brd.toString()));
                int N = in.readInt();
                int n1 = N;
                int emptyIdx = 0;
                
                N *= N;
                int[] tiles = new int[N];
                for (int i = 0; i < N; i++)
                {
                    tiles[i] = in.readInt();
                    if (tiles[i] == 0)
                        emptyIdx = i;
                }
                
                int inversions = 0;
                
                for (int i = 0; i < N - 1; i++)
                    for(int j = i + 1; j < N; j++)
                        if (tiles[j] != 0 && tiles[i] != 0 && tiles[j] > tiles[i])
                            inversions++;                
                    
                if (n1 % 2 == 0) {                
                    IsSolavble = ((n1 - (emptyIdx+1)/n1) % 2 != 0) && (inversions % 2 == 0);
                }
                else
                    IsSolavble = inversions % 2 == 0;
            }
        }
        
        return IsSolavble;
    }
    
    public int moves() {
        
        Solve();
                       
        if (MovesCount == -1)
        {                                   
            if (Solution != null && isSolvable())
                for (SearchNode nd = Solution; nd != null; nd = nd.Prev)
                    MovesCount++;
        }
        return MovesCount;
    }    
    
    public Iterable<Board> solution() {
        
        Solve();
        
        if (!isSolvable())
            return null;
        
        if (Result == null) {                       
            ArrayList<Board> lst = new ArrayList<Board>(moves());
            
            for (SearchNode nd = Solution; nd != null; nd = nd.Prev)
                lst.add(nd.Brd);
            
            Collections.reverse(lst);            
            Result = lst;            
        }
        
        return Result;
    }
    
    private static int getWeight(final Board b) {
        return b.manhattan();
    }
    private static int getWeight2(final Board b) {
        return b.hamming();
    }
    
    //Full solving with parallel iteration of twin board to detect unsolvable board in a timely fashion 
    private void Solve() 
    {               
        if (Solution == null) {
                        
            MinPQ<SearchNode> queue = new MinPQ<SearchNode>(new BoardNodesComparator());            
            queue.insert(new SearchNode(null, Brd, getWeight(Brd), getWeight2(Brd), 0));            
            
            MinPQ<SearchNode> queue2 = new MinPQ<SearchNode>(new BoardNodesComparator());
            Board btwin = Brd.twin();
            queue2.insert(new SearchNode(null, btwin, getWeight(btwin), getWeight2(btwin), 0));
                
           SearchNode node = null, node2 = null;           
           boolean twin = false;
           while (!queue.isEmpty() || !queue2.isEmpty()) {
               
               if (!queue.isEmpty())
               {
                   node = queue.delMin();               
                   if (node.Brd.isGoal())
                       break;
               }
               else
            	   node = null;
               
               if (!queue2.isEmpty()) {
                   node2 = queue2.delMin();               
                   if (node2.Brd.isGoal())
                   {
                       twin = true;
                       break;
                   }
               }
               else 
            	   node2 = null;                            
               
               if (node != null)
	               for (Board b : node.Brd.neighbors()) 
	                   if (!node.has(b)) {
	                      SearchNode adjusentNode = new SearchNode(node, b, getWeight(b) + node.MovesCount, 
	                              getWeight2(b) + node.MovesCount, node.MovesCount + 1);
	                      queue.insert(adjusentNode);                      
	                   }               
               
               if (node2 != null)
	               for (Board b : node2.Brd.neighbors()) 
	                   if (!node2.has(b)) {
	                      SearchNode adjusentNode = new SearchNode(node2, b, getWeight(b) + node2.MovesCount, 
	                              getWeight2(b) + node2.MovesCount, node2.MovesCount + 1);
	                      queue2.insert(adjusentNode);                      
	                   }
           }
                            
           IsSolavble = !twin;           
           Solution = twin ? node2:node;
        }
    }
            
    //reduced solving: may solve unsolvable boards for a long time
    private void Solve2() 
    {               
        if (Solution == null) {
                        
            MinPQ<SearchNode> queue = new MinPQ<SearchNode>(new BoardNodesComparator());            
            queue.insert(new SearchNode(null, Brd, getWeight(Brd), getWeight2(Brd), 0));
        
            //HashSet<Board> hs = new HashSet<Board>();
                                       
           SearchNode node = null;     
           int moves = 0;
           while (!queue.isEmpty()) {
                              
               node = queue.delMin();             
               if (node.Brd.isGoal())
                   break;

               //hs.add(node.Brd);
               ++moves;//not for priority: used for solvability detection
               
               for (Board b : node.Brd.neighbors()) {                                                                          
                   if (!node.has(b)) {                   
                   //if (!hs.contains(b)) {
                      SearchNode adjusentNode = new SearchNode(node, b, getWeight(b) + node.MovesCount, 
                              getWeight2(b) + node.MovesCount, node.MovesCount + 1);
                       
                       if (b.isGoal())
                       {
                           Solution = adjusentNode;
                           IsSolavble = true;
                           return;
                       }
                       
                      queue.insert(adjusentNode);
                   }                                 
               }
           }
                            
           IsSolavble = !queue.isEmpty() || moves == 0;           
           Solution = node;
        }
    }
    
    
    public static void main(final String[] args) {
        
        /*int[][] a = new int[][]{
                {8, 1, 3},
                {4, 0, 2},
                {7, 6, 5}
            };
        
        Board b = new Board(a);
        
        Solver s = new Solver(b);
        
        StdOut.println(s.isSolvable());*/
        
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        
        Board initial = new Board(blocks);
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }               
    }
}
