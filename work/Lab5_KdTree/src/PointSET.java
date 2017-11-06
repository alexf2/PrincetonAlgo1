import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {

    private final TreeSet<Point2D> points = new TreeSet<Point2D>();
    
    public PointSET() {        
    }
    
    public boolean isEmpty() {
        return points.size() == 0;
    }
    
    public int size() {
        return points.size();
    }
    
    public void insert(final Point2D p)  {        
        if (p == null)
            throw new NullPointerException("Point is null");
        
        points.add(p);                
    }
    
    public boolean contains(final Point2D p) {
        if (p == null)
            throw new NullPointerException("Point is null");
        return points.contains(p);
    }
    
    public void draw() {
        for (Point2D pt:points)
            StdDraw.point(pt.x(), pt.y());
    }
    
    public Iterable<Point2D> range(final RectHV rect){
        if (rect == null)
            throw new NullPointerException("Rectangle is null");
        
        return new Iterable<Point2D>(){
            private RectHV rect;    
            
            @Override
            public Iterator<Point2D> iterator() {
                return new XIterator( 
                        points.subSet(new Point2D(rect.xmin(), rect.ymin()), true, new Point2D(rect.xmax(), rect.ymax()), true),
                            rect.xmin(), rect.xmax()
                );
               };
               
            private Iterable<Point2D> init(final RectHV r) {
                rect = r;
                return this;
            }
        }.init(rect);
    }      
    
    private static final class XIterator implements Iterator<Point2D> {        
        private final double x1, x2;
        private final Iterator<Point2D> it;
        private Point2D nextPt;
        
        XIterator(final NavigableSet<Point2D> set, final double xMin, final double xMax) {            
            x1 = xMin;
            x2 = xMax;
            it = set.iterator();
            nextPt = getNext();            
        }
        
        public boolean hasNext() {
            return nextPt != null;
        }
        
        public void remove() { throw new UnsupportedOperationException(); }
        
        public Point2D next() {
            if (nextPt == null) 
                throw new NoSuchElementException();
            Point2D res = nextPt;
            nextPt = getNext();
            return res;
        }
        
        private Point2D getNext() {
            Point2D res = null;
            while (it.hasNext()){
                Point2D tmp = it.next();
                if (tmp.x() >= x1 && tmp.x() <= x2) {
                    res = tmp;
                    break;
                }
            }
            return res;
        }
    }
    
    public Point2D nearest(final Point2D p) {
        if (p == null)
            throw new NullPointerException("Point is null");
        if (isEmpty())
            return null;

        double distMin = Double.MAX_VALUE;
        Point2D res = null;
        
        for (Point2D pt:points) {
            double dst = p.distanceSquaredTo(pt);
            if (dst < distMin)
            {
                distMin = dst;
                res = pt;
            }
        }
                       
        return res;
    }

    public static void main(String[] args) {        
    }
}
