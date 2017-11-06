/******************************************************************************
 *  Compilation:  javac PuzzleChecker.java
 *  Execution:    java PuzzleChecker filename1.txt filename2.txt ...
 *  Dependencies: Board.java Solver.java
 *
 *  This program creates an initial board from each filename specified
 *  on the command line and finds the minimum number of moves to
 *  reach the goal state.
 *
 *  % java PuzzleChecker puzzle*.txt
 *  puzzle00.txt: 0
 *  puzzle01.txt: 1
 *  puzzle02.txt: 2
 *  puzzle03.txt: 3
 *  puzzle04.txt: 4
 *  puzzle05.txt: 5
 *  puzzle06.txt: 6
 *  ...
 *  puzzle3x3-impossible: -1
 *  ...
 *  puzzle42.txt: 42
 *  puzzle43.txt: 43
 *  puzzle44.txt: 44
 *  puzzle45.txt: 45
 *
 ******************************************************************************/

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class PuzzleChecker {

    public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            if (!in.exists())
            {
            	StdOut.println("File not found: " + filename);
            	return;
            }
            int N = in.readInt();
            int[][] tiles = new int[N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    tiles[i][j] = in.readInt();
                }
            }            
            
            // solve the slider puzzle
            Stopwatch w = new Stopwatch();
            Board initial = new Board(tiles);
            Solver solver = new Solver(initial);
            //StdOut.println("Inserts: " + solver.ins);
            if (solver.isSolvable()) {
                StdOut.println(filename + ": " + solver.moves());
                StdOut.println("Solved for: " + w.elapsedTime());
                
                
                StdOut.println("");
                int i = 0;
                for(Board b : solver.solution()) {
                	StdOut.println(++i);
                    StdOut.println( b.toString() );                    
                }
            }
            else {
                StdOut.println( filename + ": Not solvable" );
                StdOut.println("Solved for: " + w.elapsedTime());
                //StdOut.println( "Hamming code = " + initial.hamming());
                //StdOut.println( "Manhetten code = " + initial.manhattan() );
                
                /*StdOut.println( solver.moves() );
                StdOut.println( solver.solution() );
                StdOut.println( initial.isGoal() );*/
                
                /*Vector<Board> bb = (Vector<Board>)initial.neighbors();
                int cc = bb.size();
                for(Board bb2:bb)
                {
                    Vector<Board> bb3 = (Vector<Board>)bb2.neighbors();
                    cc += bb3.size();
                }
                StdOut.println( cc );*/
            }
        }
    }
}
