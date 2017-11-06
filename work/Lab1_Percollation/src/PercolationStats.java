import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


/**
 * Represents a threshold detector.
 * 
 * @author Aleksey Fedorov
 *
 */
public class PercolationStats {
    
    private final int n, t;
    private boolean calculated;
    private double mean, stddev, confLo, confHi;
    
    /**
     * Initializes the instance.
     * 
     * @param N lattice size
     * @param T number of runs
     */
    public PercolationStats(int N, int T)     
    {    
        if (N < 1)
            throw new IllegalArgumentException(String.format("N has invalid value '%d', should be >= 1.", N));
        
        if (T < 1)
            throw new IllegalArgumentException(String.format("T has invalid value '%d', should be >= 1.", T));
        
        n = N; 
        t = T;              
    }
    
    /**
     * @return sample mean of percolation threshold
     */
    public double mean()                      
    {    
        calculate();
        return mean;
    }
    
    /**
     * @return sample standard deviation of percolation threshold
     */
    public double stddev()                    
    {
        if (n == 1)
            return Double.NaN;
        calculate();
        return stddev;
    }
    
    /**
     * @return low  endpoint of 95% confidence interval
     */
    public double confidenceLo()
    {
        calculate();
        return confLo;
    }
    
    /**
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi()
    {
        calculate();
        return confHi;
    }
    
    private void calculate()
    {
        //StdRandom.setSeed(System.nanoTime());
        
        if (!calculated)
        {
            calculated = true;
            
            double[] results = new double[t];
            for (int i = 0; i < t; ++i)
                results[ i ] = testPercollation();
            
            mean = StdStats.mean(results);
            stddev = StdStats.stddev(results);
            double derivation = 1.96 * stddev / Math.sqrt((double) t);
            confLo = mean - derivation;   
            confHi = mean + derivation;
        }
    }
    
    private double testPercollation()
    {
        if (n == 1)
            return StdRandom.uniform(0, 2);
        
        long totalCells = (long) n * (long) n;
        
        Percolation perc = new Percolation(n);
        long openCount = 0;
        do {
            int i = StdRandom.uniform(1, n + 1), j = StdRandom.uniform(1, n + 1);
            
            if (perc.isOpen(i, j))
                continue;
            
            openCount++;
            perc.open(i, j);
        } while (!perc.percolates() && openCount < totalCells);
        
        return (double) openCount / (double) totalCells;
    }

    /**
     * Test client (described below).
     * 
     * @param args
     */
    public static void main(String[] args)
    {        
        if (args.length == 2){
            Integer 
                n = tryParseInt(args[ 0 ]),
                t = tryParseInt(args[ 1 ]);                      
            
            if (n == null || t == null)
            {
                System.out.printf("Arguments are not integers: %s %s", args[0], args[1]);
                return;
            }
            
            //Stopwatch w = new Stopwatch(); 
            PercolationStats ps = new PercolationStats(n, t);
            
            System.out.printf("%% java PercolationStats %d %d%n", n, t);
            System.out.printf("mean\t\t\t = %f%n", ps.mean());
            System.out.printf("stddev\t\t\t = %f%n", ps.stddev());
            System.out.printf("95%% confidence interval\t = %f, %f%n", ps.confidenceLo(), ps.confidenceHi());
            
            //StdOut.printf("%nElapsed time is: %f%n", w.elapsedTime());                      
        } 
        else
            System.out.println("Bad number of arguments. Specify N and T.");
    }
    
    //@SuppressWarnings("unchecked")
    private static Integer tryParseInt(String val)
    {
        Integer result = null;
        try {
            result = Integer.parseInt(val);
        }
        catch (NumberFormatException ex) {
            result = null;
        }
        return result;
    }   
}
