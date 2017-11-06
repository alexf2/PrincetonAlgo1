import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints {
            
    private final ArrayList<Pair<Point>> segments = new ArrayList<Pair<Point>>();
    //private Point[] buff = new Point[4];
    private LineSegment[] resSeg;
    
    private class Pair<F> {
        public final F first;
        public final F second;
        
        public Pair(F x, F y){
            first = x;
            second = y;
        }
    }
    
    public FastCollinearPoints(Point[] pp) {        
        
        if (pp == null)
            throw new NullPointerException();
        
        Point[] points = pp.clone();
        pp = null;
        
        Arrays.sort(points);
        for (int x = 0; x < points.length - 1; ++x)                        
            if (points[x].compareTo(points[x + 1]) == 0)
                throw new IllegalArgumentException("Duplicated points");        
        
        
        if (points.length < 2)            
            return;                               
                                  
        Point[] pp0  = points.clone();
        
        for (int j = 0; j < points.length; ++j)
        {
            Point p1 = pp0[ j ];            
            Arrays.sort(points, 0, points.length, p1.slopeOrder());
                        
            
            double firstSlope = -999;
            int firstIdx = 0, i = 0;
            for (; i < points.length; ++i)
            {       
                Point p2 = points[i];
                if (p2 == p1)
                    continue;
                
                double slp = p1.slopeTo(p2);
                if (cmp(firstSlope, slp) != 0)
                {            
                    if (i - firstIdx > 2)
                    {
                        addSegment(p1, points, firstIdx, i - 1);
                        
                        firstIdx = i; 
                        firstSlope = slp;                                               
                    }
                    
                    firstIdx = i; 
                    firstSlope = slp;                    
                }
            }        
            
            if (i - firstIdx > 2)
                addSegment(p1, points, firstIdx, i - 1);
            /*else {            
                i = points.length - 1;
                for (; i > 0 && p1.slopeTo(points[i]) == Double.POSITIVE_INFINITY; --i);
                if (i > 0 && i < points.length - 3)
                    addSegment(p1, points, i + 1, points.length - 1);
            }*/
        }               
        
        segments.sort(new Comparator<Pair<Point>>() {
            @Override
            public int compare(Pair<Point> x, Pair<Point> y) {
                return x.first.compareTo(y.first);
            }
        });
    }     
        
    private void addSegment(final Point p1, Point[] points, final int i, final int j)
    {               
        Arrays.sort(points, i, j + 1);
        Point p0 = points[i];
        Point p2 = points[j];
        addSeg(p1.compareTo(p0) < 0 ? p1 : p0, p1.compareTo(p2) > 0 ? p1 : p2);
    }
    
    
    private void addSeg(Point x, Point y) {        
        /*for (int i = 0; i < segments.size(); ++i)
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
        
        segments.add(new Pair<Point>(x, y)); */
        
                        
        if (x.compareTo(y) > 0)
        {
            Point t = x;
            x = y;
            y = t;
        }
        
        Pair<Point> seg = new Pair<Point>(x, y);        
        int idx = binarySearchSeg(seg);
        
        if (idx < 0)
            segments.add(~idx, seg);                      
    }
    
    private int binarySearchSeg(Pair<Point> seg)
    {    
        int low = 0,
            high = segments.size() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
        
            Pair<Point> pt = segments.get(mid);
            int cmp = cmpSegs(pt, seg);
                    
            if (cmp == 0)
                return mid;
            if (cmp < 0)
                low = mid + 1;
            else
                high = mid - 1;
        }

        return ~low;
    }
    
    private static int cmpSegs(Pair<Point> seg1, Pair<Point> seg2)
    {       
        if (seg1.first.compareTo(seg2.first) == 0 && seg1.second.compareTo(seg2.second) == 0)
            return 0;
        
        int res = seg1.first.compareTo(seg2.first);
        if (res != 0)
            return res;
        return seg1.second.compareTo(seg2.second);
    }
    
    private static int cmp(double v1, double v2)
    {               
        int c = Double.compare(v1, v2);                       
        
        if (c == 0)
            return 0;
        
        if (Math.abs(v1 - v2) < 0.0000001)
            return 0;        
        
        return c;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();            
        }
    }

}
