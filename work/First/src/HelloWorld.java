import java.io.*;

import static java.lang.Math.*;
import java.util.*;
//import edu.princeton.cs.algs4.*;

public class HelloWorld {

	//final int pp = 1;
	/**
	 * @param args
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		System.out.println("Hello!\r\n");
		if (args.length > 0)
			System.out.printf("%s\r\n", args[0]);
		
		System.out.println( System.getProperty("java.version") );
		
		Cls2 cc = new Cls2(); cc.A = "2";
		System.out.println(cc.getA());
		System.out.println(cc.bB);		
		
		char symb1=1067;
		char symb2 ='Û';

		System.out.println("symb1 contains "+ symb1);
		System.out.println("symb2 contains "+ (int)symb2);
		
		String str3 = "20150";
		String str4 = "20150";
	    byte[] b3 = str3.getBytes();
	    System.out.println(b3);
	    
	    System.out.println((Object)str3 == (Object)str4);
	    
	    int[] numbers = {10, 20, 30};
	    for (int x:numbers)
	    {
	    	System.out.print(x);
	    }	    
	    
	    Integer gg = 1;
	    int g2 = gg;
	    Object g3 = (byte)g2;	    
	    System.out.print("\r\n" + gg.toString());
	    System.out.print("\r\n" + g3.hashCode());
	    
	    byte bb = -128;
	    System.out.print("\r\n" + (bb & 0xff));
	    
	    HashMap<Integer, String> dic = new HashMap<Integer, String>();
	    dic.put(1, "ccc");
	    dic.put(2, "aaa");
	    for(Object item:dic.values())
	    {
	    	System.out.print("\r\n" + item);
	    }
		
		
		
		int[][] t1 = new int[][]{
				{0,6},
				{8,6},
				{3,6},
				{6,7},
				{5,9},
				{2,7}
		};
		
		//testUf(new QuickUnionUF(10), t1, 10);		
		
		int[][] t2 = new int[][]{
				{0, 5},		
				{3, 4},
				{9, 2},
				{3, 6},
				{0, 2},
				{7, 6},
				{1, 4},
				{2, 7},
				{0, 8}
		};
		
		//testUf(new WeightedQuickUnionUF(10), t2, 10);
		//testUf(new QuickUnionUF(10), t2, 10);
			
		
		edu.princeton.cs.algs4.WeightedQuickUnionUF tt = new edu.princeton.cs.algs4.WeightedQuickUnionUF(10);
		for(int[] ent:t2)		
			tt.union(ent[0], ent[1]);
		for(int i = 0; i < 10; ++i)
			System.out.printf("%d ", tt.find(i));
		
		
		
		MysteryBox.testSer();	    
	}
	
	private static void testUf(IUf uf, int[][] ents, int size)
	{
		for(int[] ent:ents)		
			uf.union(ent[0], ent[1]);
		
		for(int i = 0; i < size; ++i)
			System.out.printf("%d ", uf.find(i));
		
		System.out.printf("%n");
		
	}
}

