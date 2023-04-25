//Student Full Name: Brent Palmer
//Student ID: 300193610

import java.util.ArrayList;
import java.util.List;


/**
 * The class <b>NearestNeighborsKD</b> is used to determine the 
 * 3D points that are in the neighborhood of a given 3D point given
 * an epsilon value. This class is a variation of NearestNeighbors,
 * using k-d trees to determine the nearest neighbors. 
 * 
 * The class has one instance variable, kdTree, which is a k-d tree that 
 * stores all of the points.
 * 
 * The class has a constructor and two other methods. The constructor
 * instantiates the k-d tree from an input list of points. 
 * There are two methods called rangeQuery. The first is called by the
 * user and calls the second, recursive, rangeQuery method and ultimately
 * returns the list of neighbors. The second rangeQuery method recursively adds the
 * neighbors of the given point to the list of neighbors. 
 *
 * @author Brent Palmer
 */

public class NearestNeighborsKD{
    KDtree kdTree;

	/**
     * The constructor for <B>NearestNeighborsKD</b> will build the k-d tree which requires inserting
     * all of the 3D points into a binary tree. 
     * 
     * @param list
     * A list of Point3D objects that contains all the 3D points
     */
	public NearestNeighborsKD(List<Point3D> list){
        //instantiate the k-d tree
		kdTree = new KDtree();
        //loop through each point in the list and add them to the k-d tree
        for(Point3D p : list) kdTree.add(p);
	}

	/**
     * The method <B>rangeQuery</b> is used to determine which 3D points
     * from the entire list of 3D points are in the neighborhood
     * of a specific input Point3D object. The points that are in the
     * neighborhood are stored in a List of Point3D objects. This method
     * will call a recursive method that searches a k-d tree and updates
     * the List of neighboring Point3D objects. 
     * 
     * @param p
     * A Point3D object that will be compared with all other Point3D objects
     * to determine which points are in its neighborhood.
     * 
     * @param eps
     * A double that represents the maximum distance for two points to 
     * be considered part of the same cluster. 
     * 
     * 
     * @return
     * Returns a List of Point3D objects that are in 
     * the neighborhood of p
     */
	public List<Point3D> rangeQuery(Point3D p, double eps){
        //instatiate a list of neighbors 
		List<Point3D> neighbors = new ArrayList<>();
        //call recursive version of rangeQuery to determine the neighbors using kd tree
        rangeQuery(p, eps, neighbors, kdTree.root());
        return neighbors;
	}

    /**
     * The method <B>rangeQuery</b> is called by the original rangeQuery method that takes
     * a point and eps as inputs. To find the points in the neighborhood of the given point, 
     * the k-d tree having a depth of O(log(n)) is recursively searched in this method. The 
     * method updates the List of neighboring 3DPoints whenever a new neighbor is found. 
     * 
     * @param p
     * A Point3D object that will be compared with all other Point3D objects
     * to determine which points are in its neighborhood.
     * 
     * @param eps
     * A double that represents the maximum distance for two points to 
     * be considered part of the same cluster. 
     * 
     * @param neighbors
     * A list of Point3D objects that is the current list of neighbors of point p
     * 
     * @param node
     * A KDnode object that represents the current node in traversal of k-d tree
     *
     * No return
     */
    private void rangeQuery(Point3D p, double eps, List<Point3D> neighbors, KDtree.KDnode node){
        //if node is null, there is no point to add to list of neighbors
        if(node == null) return;

        //if the distance between the point of the node and the primary point we are checking
        //is less than eps, then add the point of the node to the list of neighbors
        if(p.distance(node.point) < eps) neighbors.add(node.point);
        
        //if the range of the search from the primary point includes values lower than the 
        //cut value of the current point, then check the point that has lower value than 
        //the current point
        if(p.get(node.axis) - eps <= node.value) rangeQuery(p, eps, neighbors, node.left);

        //if the range of the serach from the primary point includes values higher than the 
        //cut value of the current point, then check the point that has greater value than 
        //the current point
        if(p.get(node.axis) + eps > node.value) rangeQuery(p, eps, neighbors, node.right);

        return;
    }
}