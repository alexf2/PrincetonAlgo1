import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    
    private final ArrayList<Pair<Point>> segments = new ArrayList<Pair<Point>>();
    private LineSegment[] resSeg;
    
    private class Pair<F> {
        public final F first;
        public final F second;
        
        public Pair(F x, F y){
            first = x;
            second = y;
        }
    }
    
    public BruteCollinearPoints(Point[] pp)   
    {    
        if (pp == null)
            throw new NullPointerException();
        
        Point[] points = pp.clone();
        pp = null;
        
        Arrays.sort(points); 
        for (int x = 0; x < points.length - 1; ++x)
            if (points[x].compareTo(points[x + 1]) == 0)
                throw new IllegalArgumentException("Duplicated points");
        
        if (points.length < 2)
            //throw new IllegalArgumentException("Array should be at least 4 points size");
            return;                       
        
        Point[] p4 = new Point[4];
        for (int i = 0; i < points.length - 3; ++i)
            for (int k = i + 1; k < points.length - 2; ++k)
                for (int j = k + 1; j < points.length - 1; ++j)
                    for (int m = j + 1; m < points.length; ++m)
                    {
                        p4[0] = points[i];
                        p4[1] = points[k];
                        p4[2] = points[j];
                        p4[3] = points[m];
                                                                       
                        if (p4[0] == null || p4[1] == null || p4[2] == null || p4[3] == null)
                            throw new NullPointerException();
                        
                        Comparator<Point> cmp = p4[0].slopeOrder();
                        if (cmp.compare(p4[1], p4[2]) == 0 && cmp.compare(p4[2], p4[3]) == 0) {                            
                            Arrays.sort(p4);
                                                                                    
                            addSeg(p4[0], p4[3], p4);
                        }
                    };
 
                                   
        segments.sort(new Comparator<Pair<Point>>() {
            @Override
            public int compare(Pair<Point> x, Pair<Point> y) {
                return x.first.compareTo(y.first);
            }
        });
    } 
    
    private void addSeg(final Point x, final Point y, final Point[] buff) {        
        for (int i = 0; i < segments.size(); ++i)
        {
            Pair<Point> pp = segments.get(i);
            
            buff[0] = pp.first;
            buff[1] = pp.second;
            buff[2] = x;
            buff[3] = y;
            Arrays.sort(buff);
                        
            Comparator<Point> cmp = buff[0].slopeOrder();
            
            double s1 = buff[0].slopeTo(buff[1]);            
            double s3 = buff[0].slopeTo(buff[3]);
            
            boolean c1 = cmp.compare(buff[1], buff[2]) == 0;
            boolean c2 = cmp.compare(buff[2], buff[3]) == 0;
            
            if (c1 && c2 || s1 == Double.NEGATIVE_INFINITY && c2 || c1 && s3 == Double.NEGATIVE_INFINITY) 
            {
                segments.remove(i);                               
                segments.add(new Pair<Point>(buff[0], buff[3]));                
                return;
            }
        }
        
        segments.add(new Pair<Point>(x, y));
    }
    
    public int numberOfSegments() {    
        return segments.size();
    }
    
    public LineSegment[] segments() {
        if (resSeg == null) {
            resSeg = new LineSegment[segments.size()];
            for (int i = 0; i < segments.size(); ++i)
                resSeg[i] = new LineSegment(segments.get(i).first, segments.get(i).second);
        }
        return resSeg.clone(); 
    }
           
    public static void main(String[] args) { 
                                            
        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt(); 
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        StdOut.printf("%n");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
