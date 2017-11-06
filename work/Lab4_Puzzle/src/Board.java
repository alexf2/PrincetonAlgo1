import java.util.Arrays;
import java.util.Vector;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;


public class Board {
    
    private final char[] BoardData;
    private final int BoardRank;    
    private final int EmptyIndex;
    
    private Boolean IsGoal;
    private Integer Hamming;
    private Integer Manhattan;
    
    private Board (final char[] bdata, final int rank, final int emptyIdx)
    {
        BoardData = bdata;
        BoardRank = rank;        
        
        int idx = 0;
        if (emptyIdx == -1)
        {   
            for(int i = 0; i < bdata.length; ++i)
                if (bdata[i] == 0)
                {
                    idx = i;
                    break;
                }
        }
        else
            idx = emptyIdx;
        
        EmptyIndex = idx;
    }       
    
    public Board (final int[][] blocks)     
    {                    
        if (blocks == null)
            throw new NullPointerException();
        if (blocks.length < 2)
            throw new IllegalArgumentException(String.format("The board is too small: %d", blocks.length));
        
        BoardRank = blocks.length;
        int br = blocks.length;
        BoardData = new char[br * br];
        int countZeros = 0;
        int emptyIndex = -1;
        
        for (int i = 0; i < br; ++i)
        {
            if (blocks[i].length != blocks.length)
                throw new IllegalArgumentException(String.format("The row %d has illegat size %d", i, blocks[i].length));                       
            
            for (int j = 0; j < br; ++j) {
                int item = blocks[i][j];
                int targetIndex = br * i + j;
                
                if (item == 0)
                {
                    countZeros++;
                    emptyIndex = targetIndex;
                }
                
                if (countZeros > 1)
                    throw new IllegalArgumentException("The number of zeros is greater than 1");
                
                BoardData[targetIndex] = (char)item;
            }
        }
        
        if (countZeros == 0)
            throw new IllegalArgumentException("Empty square is not found");
        
        EmptyIndex = emptyIndex;
    }
    
    public int dimension(){
        return BoardRank;
    }
    
    public int hamming() {
        if (Hamming == null) {  
            int code = 0;
            for (int i = 0; i < BoardData.length; ++i) 
                if (BoardData[i] != 0 && BoardData[i] != i + 1)
                    ++code;
            
            Hamming = code;
        }
        return Hamming;
    }    
    
    private int getManhattenCode (final int index, final int value) {
               
        int row = index / BoardRank,
            col = index % BoardRank,                
            rowV = value / BoardRank,
            colV = value % BoardRank;
            
        return Math.abs(row - rowV) + Math.abs(col - colV);
    }
    
    public int manhattan() {
        if (Manhattan == null)
        {            
            int code = 0;            
            
            for (int i = 0; i < BoardData.length; ++i) {                
                if (BoardData[i] != 0 && BoardData[i] != i + 1)
                    code += getManhattenCode(i, BoardData[i] - 1);
            }                        
            
            Manhattan = code;        
        }
        return Manhattan;
    }
    public boolean isGoal() {
        if (IsGoal == null)
        {
        	if (Manhattan != null && Manhattan == 0 || Hamming != null && Hamming == 0)
        		IsGoal = true;
        	else {
	            IsGoal = true;
	            for (int i = 0; i < BoardData.length; ++i) 
	            {
	            	int v = BoardData[i];
	                if (v != 0 && v != i + 1) {
	                    IsGoal = false;
	                    break;
	                }
	            }
        	}            
        }
       return IsGoal;
    }
    
    public Board twin() {                
        int idx1 = 0, idx2 = 0;
        while (idx1 == idx2 || BoardData[idx1] == 0 || BoardData[idx2] == 0) {
            idx1 = StdRandom.uniform(0, BoardData.length);
            idx2 = StdRandom.uniform(0, BoardData.length);
        }

        return createBoardWithSwap(idx1, idx2, false, EmptyIndex);
    }
    
    private Board createBoardWithSwap(final int idx1, final int idx2, final boolean nextMove, int emptyIdx) {
        char[] tmp = BoardData.clone();
                
        char val = tmp[idx1];
        tmp[idx1] = tmp[idx2];
        tmp[idx2] = val;
        
        Board res = new Board(tmp, BoardRank, emptyIdx);
        
        //calculate codes
        if (nextMove) 
        {                   
            int i1, i2, c;            
            if (BoardData[idx1] == 0)
            {     
                i1 = idx2;
                i2 = idx1; //zero
                c = BoardData[idx2] - 1;
            }
            else {                
                i1 = idx1;
                i2 = idx2; //zero
                c = BoardData[idx1] - 1;
            }
            
          //adjusting Hamming's code
            if (Hamming != null) {                    
                int hammingDelta = 0;
                if (i1 == c)
                    hammingDelta = 1; //Legal --> Illegal
                else if (i2 == c) 
                    hammingDelta = -1; //Illegal --> Legal
                //Illegal --> Illegal
                                               
                res.Hamming = Hamming + hammingDelta;
            }
            
            //adjusting Manhettan's code
            if (Manhattan != null) {                
                int code1 = getManhattenCode(i1, c),
                    code2 = getManhattenCode(i2, c);                                              
                
                res.Manhattan = Manhattan + (code2 - code1);
            }
        }
        
        return res;
    }
    
    @Override
    public boolean equals (final Object y) {
        if (y == this)
            return true;
        
        if (!(y instanceof Board))
            return false;
        
        Board obj = (Board)y;
        if (dimension() != obj.dimension())
            return false;        
        
        return EmptyIndex == obj.EmptyIndex  && myHashCode() == obj.myHashCode() && 
               Arrays.equals(BoardData, obj.BoardData); 
    }
    
    private int Hs = 0;
    //@Override
    private int myHashCode() {
        if (Hs == 0) {
            int res = 17;
            Hs = 31*res + Arrays.hashCode(BoardData);
        }
        return Hs;
    }
    
    public Iterable<Board> neighbors() {
        int row = EmptyIndex / BoardRank;
        int col = EmptyIndex % BoardRank;
        int maxRow = BoardRank - 1, maxCol = BoardRank - 1; 
        
        boolean t = row != 0,
                b = row != maxRow,
                l = col != 0,
                r = col != maxCol;
        
        int size = (t ? 1:0) + (b ? 1:0) + (l ? 1:0) + (r ? 1:0);
        
        Vector<Board> res = new Vector<Board>(size); 
                
        if (t)
            res.add(createBoardWithSwap(EmptyIndex, EmptyIndex - BoardRank, true, EmptyIndex - BoardRank));
        if (b)
            res.add(createBoardWithSwap(EmptyIndex, EmptyIndex + BoardRank, true, EmptyIndex + BoardRank));
        if (l)
            res.add(createBoardWithSwap(EmptyIndex, EmptyIndex - 1, true, EmptyIndex - 1));
        if (r)
            res.add(createBoardWithSwap(EmptyIndex, EmptyIndex + 1, true, EmptyIndex + 1));
        
        return res;
    }
    
    private String StrVal;
    public String toString()   {
        if (StrVal == null) {
            StringBuilder s = new StringBuilder();
            String fmt = BoardRank > 10 ? "%6d ":"%2d ";
            
            s.append(BoardRank + "\n");
            for (int i = 0; i < BoardData.length; ++i)
            {
                if (i != 0 && i % BoardRank == 0)
                    s.append("\n");
                
                s.append( String.format(fmt, (int)BoardData[i]) );            
            }
            s.append("\n");
            
            StrVal = s.toString();
        }
        return StrVal;
    }
    
    private boolean isSolvable()
    {
        if (isGoal())
            return true;
        
        int inversions = 0;
                        
        for (int i = 0; i < BoardData.length - 1; i++)
            for(int j = i + 1; j < BoardData.length; j++)
                if (BoardData[j] != 0 && BoardData[i] != 0 && BoardData[j] > BoardData[i])
                    inversions++;                                             
                        
        if (BoardRank % 2 == 0)                                              
            return  (BoardRank - (EmptyIndex+1)/BoardRank - 1) % 2 != 0 && inversions % 2 == 0;        
        else
            return inversions % 2 == 0;
    }
    
    public static void main(final String[] args){
        int[][] a = new int[][]{
            {8, 1, 3},
            {4, 0, 2},
            {7, 6, 5}
        };
        
        int[][] a2 = new int[][]{
                {5, 1, 8},
                {2, 0, 3},
                {4, 7, 6}
            };
        
        int[][] a3 = new int[][]{
                {1, 2},
                {0, 3}
            };
            
            
            
        Board bb = new Board(a2);
        StdOut.println(bb.isSolvable());
        StdOut.println("----------");
        
        for (Board ff : bb.neighbors()) {
        	StdOut.println(ff);
        	StdOut.println("hamming: " + ff.hamming());
        	StdOut.println("manh: " + ff.manhattan());
        }
        
        bb.hamming();
        //Board ff = bb.createBoardWithSwap(7, 4, true, 4);
        
        
        //StdOut.println(a.length);
        //StdOut.println(a[0].length);
        
        Board b = new Board(a);
        //StdOut.println(b.isGoal());               
        
        StdOut.println(b.hamming());
        StdOut.println(b.manhattan());
                
        
        Board b2 = b.createBoardWithSwap(1, 4, true, 4);
        
        StdOut.println(b2.hamming());
        StdOut.println(b2.manhattan());
        
        b2.Hamming = null; b2.Manhattan = null; 
        StdOut.println(b2.hamming());
        StdOut.println(b2.manhattan());
        StdOut.println(b2.toString());
    }
}
