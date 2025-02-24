import java.security.InvalidAlgorithmParameterException;
import java.io.*;
import java.util.*;

/**
 * Models a weighted graph of latitude-longitude points
 * and supports various distance and routing operations.
 * To do: Add your name(s) as additional authors
 * 
 * @author Brandon Fain
 * @author Owen Astrachan modified in Fall 2023
 *
 */
public class GraphProcessor {
    /**
     * Creates and initializes a graph from a source data
     * file in the .graph format. Should be called
     * before any other methods work.
     * 
     * @param file a FileInputStream of the .graph file
     * @throws Exception if file not found or error reading
     */

    // include instance variables here
    private Map<Point, List<Point>> myMap;
    private int numnode;

    public GraphProcessor() {
        // TODO initialize instance variables
        myMap = new HashMap<Point, List<Point>>();
        numnode = 0;
    }

    /**
     * Creates and initializes a graph from a source data
     * file in the .graph format. Should be called
     * before any other methods work.
     * 
     * @param file a FileInputStream of the .graph file
     * @throws IOException if file not found or error reading
     */

    public void initialize(FileInputStream file) throws IOException {
        Scanner s = new Scanner(file);
        String line = s.nextLine();
        List<Point> points = new ArrayList<>();
        int nodes = 0;
        int edge = 0;
        int index = 0;
        for (String i : line.split(" ")) {
            if (index == 0) {
                nodes = Integer.parseInt(i);
                index++;
            }
            edge = Integer.parseInt(i);
        }
        while (s.hasNextLine()) {
            if (index <= nodes) {
                line = s.nextLine();
                String[] pp = line.split(" ");
                points.add(new Point(Double.parseDouble(pp[1]), Double.parseDouble(pp[2])));
                index++;
            } else {
                line = s.nextLine();
                String[] pp = line.split(" ");
                myMap.putIfAbsent(points.get(Integer.parseInt(pp[0])), new ArrayList<>());
                myMap.get(points.get(Integer.parseInt(pp[0]))).add(points.get(Integer.parseInt(pp[1])));
                myMap.putIfAbsent(points.get(Integer.parseInt(pp[1])), new ArrayList<>());
                myMap.get(points.get(Integer.parseInt(pp[1]))).add(points.get(Integer.parseInt(pp[0])));
            }
        }
        numnode = nodes;
    }

    /**
     * NOT USED IN FALL 2023, no need to implement
     * 
     * @return list of all vertices in graph
     */

    public List<Point> getVertices() {
        return null;
    }

    /**
     * NOT USED IN FALL 2023, no need to implement
     * 
     * @return all edges in graph
     */
    public List<Point[]> getEdges() {
        return null;
    }

    /**
     * Searches for the point in the graph that is closest in
     * straight-line distance to the parameter point p
     * 
     * @param p is a point, not necessarily in the graph
     * @return The closest point in the graph to p
     */
    public Point nearestPoint(Point p) {
        // TODO implement nearestPoint
        Set<Point> temp = myMap.keySet();
        Point ret = null;
        double min = 10000000;
        for (Point hold : temp) {
            if (p.distance(hold) < min) {
                min = p.distance(hold);
                ret = hold;
            }
        }
        return ret;
    }

    /**
     * Calculates the total distance along the route, summing
     * the distance between the first and the second Points,
     * the second and the third, ..., the second to last and
     * the last. Distance returned in miles.
     * 
     * @param start Beginning point. May or may not be in the graph.
     * @param end   Destination point May or may not be in the graph.
     * @return The distance to get from start to end
     */
    public double routeDistance(List<Point> route) {
        if (route == null)
            throw new IllegalArgumentException();
        double d = 0.0;
        // TODO implement routeDistance
        for (int i = 1; i < route.size(); i++) {
            d += route.get(i - 1).distance(route.get(i));
        }
        return d;
    }

    /**
     * Checks if input points are part of a connected component
     * in the graph, that is, can one get from one to the other
     * only traversing edges in the graph
     * 
     * @param p1 one point
     * @param p2 another point
     * @return true if and onlyu if p2 is reachable from p1 (and vice versa)
     */
    private Set<Point> visited = new HashSet<>();

    public boolean connected(Point p1, Point p2) {
        Stack<Point> stack = new Stack<>();
        if(p1.equals(p2)) return true;
        if (myMap.get(p1) == null || visited.size() == numnode)
            return false;
        stack.push(p1);
        while (stack.size() > 0) {
            Point p = stack.pop();
            if(p.equals(p2)) return true;
            if (!visited.contains(p)) {
                visited.add(p);
                stack.addAll(myMap.get(p));
                
            }
        }
        return false;
    }

    /**
     * Returns the shortest path, traversing the graph, that begins at start
     * and terminates at end, including start and end as the first and last
     * points in the returned list. If there is no such route, either because
     * start is not connected to end or because start equals end, throws an
     * exception.
     * 
     * @param start Beginning point.
     * @param end   Destination point.
     * @return The shortest path [start, ..., end].
     * @throws IllegalArgumentException if there is no such route,
     *                                  either because start is not connected to end
     *                                  or because start equals end.
     */
    public List<Point> route(Point start, Point end) throws IllegalArgumentException {
        // TODO implement route
        if (start.equals(end))
            throw new IllegalArgumentException();
        List<Point> shortestPath = new ArrayList<>();
        Map<Point, Double> distanceMap = new HashMap<>();
        Map<Point, Point> predMap = new HashMap<>();
        for (Point p : myMap.keySet()) {
            distanceMap.put(p, Double.POSITIVE_INFINITY);
        }
        predMap.put(start, null);
        Comparator<Point> comp = Comparator.comparingDouble(distanceMap::get);
        PriorityQueue<Point> pq = new PriorityQueue<Point>(comp);
        Point current = start;
        distanceMap.put(start, 0.0);
        pq.add(current);
        while (pq.size() > 0) {
            current = pq.remove();
            if (current.equals(end)) {
                while (current != null) {
                    shortestPath.add(current);
                    current = predMap.get(current);
                }
                Collections.reverse(shortestPath);
                return shortestPath;
            }
            for (Point p : myMap.get(current)) {
                double weight = current.distance(p);
                double newDist = distanceMap.get(current) + weight;
                if (newDist < distanceMap.get(p)) {
                    distanceMap.put(p, newDist);
                    predMap.put(p, current);
                    pq.add(p);
                }
            }
        }
        throw new IllegalArgumentException("No path between start and end");
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String name = "data/usa.graph";
        GraphProcessor gp = new GraphProcessor();
        gp.initialize(new FileInputStream(name));
        System.out.println("running GraphProcessor");
    }
}
