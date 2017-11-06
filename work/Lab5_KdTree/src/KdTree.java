import java.util.ArrayList;
import java.util.Collection;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;


public class KdTree {       
    
    private static final class KdNode {
        private final Point2D pt;        
        private KdNode left; 
        private KdNode right;
        
        KdNode(Point2D point) {
            pt = point;
        }
        
        int kdCompare(Point2D point, int level) {            
            return (level & 1) == 0 ? Double.compare(pt.x(), point.x()) : Double.compare(pt.y(), point.y());
        }
        
        void draw(RectHV r, int level) {                       
            StdDraw.setPenRadius();
            if ((level & 1) == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(pt.x(), r.ymin(), pt.x(), r.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(r.xmin(), pt.y(), r.xmax(), pt.y());
            }
        }
        
        RectHV leftRect(RectHV r, int level) {
            if ((level & 1) == 0)
                return new RectHV(r.xmin(), r.ymin(), pt.x(), r.ymax());
            else
                return new RectHV(r.xmin(), r.ymin(), r.xmax(), pt.y());
        }
        
        RectHV rightRect(RectHV r, int level) {
            if ((level & 1) == 0)
                return new RectHV(pt.x(), r.ymin(), r.xmax(), r.ymax());
            else
                return new RectHV(r.xmin(), pt.y(), r.xmax(), r.ymax());
        }
    }
    
    private KdNode root;
    private int treeSize;
    private static final RectHV ROOT_SQUARE = new RectHV(0, 0, 1, 1);
    
    public KdTree() {        
    }
    
    public boolean isEmpty() {
        return treeSize == 0;
    }
    
    public int size() {
        return treeSize;
    }
    
    public void insert(final Point2D p)  {        
        if (p == null)
            throw new NullPointerException("Point is null");
        
      if (root == null) {
          root = new KdNode(p);
          treeSize++;
          return;
      }
      
      KdNode currentNode = root;
      int level = 0; 
      while (true) {                   
          if (currentNode.pt.equals(p)) //already in tree
              return;
          
          int cmp = currentNode.kdCompare(p, level);
          
          if (cmp > 0) {
              if (currentNode.left == null) {
                  currentNode.left = new KdNode(p);
                  treeSize++;
                  return;
              }
              currentNode = currentNode.left;
          }
          else {
              if (currentNode.right == null) {
                  currentNode.right = new KdNode(p);
                  treeSize++;
                  return;
              }
              currentNode = currentNode.right;
          }
          level++;
      }
    }
    
    public boolean contains(final Point2D p) {
        if (p == null)
            throw new NullPointerException("Point is null");
        
        KdNode currentNode = root;
        int level = 0; 
        while (currentNode != null) {
            if (currentNode.pt.equals(p)) //already in tree
                return true;
            
            int cmp = currentNode.kdCompare(p, level);
            if (cmp > 0) 
                currentNode = currentNode.left;
            else
                currentNode = currentNode.right;
            
            level++;
        }
        
        return false;
    }
    
    public void draw() {
        StdDraw.setScale(0, 1);
        StdDraw.setPenColor(StdDraw.BLACK);  
        StdDraw.setPenRadius(); 
        ROOT_SQUARE.draw();
        
        if (treeSize == 3) {
            int g=1;
            g = 2*g;
        }
        
        draw(root, ROOT_SQUARE, 0);
    }
    
    private void draw(final KdNode node, final RectHV r, final int level) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.pt.draw();
        
        node.draw(r, level);        
                        
        if (node.left != null)
            draw(node.left, node.leftRect(r, level), level + 1);
        if (node.right != null)
            draw(node.right, node.rightRect(r, level), level + 1);
    }
    
    public Iterable<Point2D> range(final RectHV rect){
        if (rect == null)
            throw new NullPointerException("Rectangle is null");
 
        final ArrayList<Point2D> res = new ArrayList<Point2D>();
        
        if (root != null)
            new RangeSearch(res, rect).search(root, ROOT_SQUARE, 0);
        
        return res;
    }      
    
    private static final class RangeSearch {
        final Collection<Point2D> result;
        final RectHV rectFind;
        
        RangeSearch(final Collection<Point2D> result, final RectHV rectFind) {
            this.result = result;
            this.rectFind = rectFind;
        }
        
        void search(final KdNode node, final RectHV nodeRect, final int level) {
            if (nodeRect.intersects(rectFind)) {
                if (rectFind.contains(node.pt))
                    result.add(node.pt);
                
                if (node.left != null)
                    search(node.left, node.leftRect(nodeRect, level), level + 1);
                if (node.right != null)
                    search(node.right, node.rightRect(nodeRect, level), level + 1);
            }
        }
    }
          
    public Point2D nearest(final Point2D p) {
        if (p == null)
            throw new NullPointerException("Point is null");
        if (isEmpty())
            return null;

        if (root != null)
            return new NearestSearch(p).search(root, ROOT_SQUARE, 0);
        
        return null;
    }
    
    private static final class NearestSearch {
        final Point2D pointSearch;
        Point2D nearestPt;
        double bestSqDist = Double.MAX_VALUE;
        
        NearestSearch(final Point2D pointSearch) {
            this.pointSearch = pointSearch;
            
        }                       
        
        public Point2D search(final KdNode node, final RectHV nodeRect, final int level) {
            
            double dist = pointSearch.distanceSquaredTo(node.pt);
            if (dist < bestSqDist) {
                nearestPt = node.pt;
                bestSqDist = dist;
            }                       
            
            KdNode n1 = node.left;
            RectHV r1 = null;
            double dist1 = Double.MAX_VALUE;
            if (n1 != null) {
                r1 = node.leftRect(nodeRect, level);
                dist1 = r1.distanceSquaredTo(pointSearch);
                if (dist1 > bestSqDist) //if the left rectangle is farther away then do not proceed
                    n1 = null;
            }
            
            KdNode n2 = node.right;
            RectHV r2 = null;
            double dist2 = Double.MAX_VALUE;
            if (n2 != null) {
                r2 = node.rightRect(nodeRect, level);
                dist2 = r2.distanceSquaredTo(pointSearch);
                if (dist2 > bestSqDist) //if the right rectangle is farther away then do not proceed
                    n2 = null;
            }
            
            if (n1 != null && n2 != null) {
                if (dist1 > dist2) {
                    KdNode tmp = n1;
                    n1 = n2; n2 = tmp;
                    RectHV tmpR = r1;
                    r1 = r2; r2 = tmpR;
                    double distT = dist1;
                    dist1 = dist2; dist2 = distT;
                }
                
                search(n1, r1, level + 1);
                if (dist2 < bestSqDist) //reevaluate distance to subtree rectangle, proceed only if it is better 
                    search(n2, r2, level + 1);
            }
            else if (n1 != null) {
                search(n1, r1, level + 1);
            }
            else if (n2 != null) {
                search(n2, r2, level + 1);
            }
            
            return nearestPt;
        }
    }

    public static void main(String[] args) {        
    }
}
