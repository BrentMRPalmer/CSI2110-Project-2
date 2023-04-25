//Student Full Name: Brent Palmer
//Student ID: 300193610

import java.util.ArrayList;
import java.util.List;


/**
 * The class <b>NearestNeighbors</b> is used to determine the 
 * 3D points that are in the neighborhood of a given 3D point given
 * an epsilon value. 
 * 
 * The class has one instance variable, a List of Point3D objects
 * used to store the initial list of 3D points.
 * 
 * The class has one main method, rangeQuery. It is used to create and return
 * a list of all 3D points that are in the same neighborhood as a given
 * 3D point. 
 *
 * @author Brent Palmer
 */

public class NearestNeighbors{
	List<Point3D> points;

	/**
     * The constructor for <B>NearestNeighbors</b> will initialize the list of 3D points 
     * to an input list of 3D points. 
     * 
     * @param points
     * A list of Point3D objects that contains all the 3D points
     */
	public NearestNeighbors(List<Point3D> points){
		this.points = points;
	}

	/**
     * The method <B>rangeQuery</b> is used to determine which 3D points
     * from the entire list of 3D points are in the neighborhood
     * of a specific input Point3D object. The points that are in the
     * neighborhood are stored in a List of Point3D objects.
     * 
     * @param eps
     * A double that represents the maximum distance for two points to 
     * be considered part of the same cluster. 
     * 
     * @param P
     * A Point3D object that will be compared with all other Point3D objects
     * to determine which points are in its neighborhood.
     * 
     * @return
     * Returns a List of Point3D objects that are in 
     * the neighborhood of P
     */
	public List<Point3D> rangeQuery(double eps, Point3D P){
		//create an instance of ArrayList to store all neighbors of point P
		List<Point3D> neighbors = new ArrayList<>();

		for(Point3D point : points){
			//go through each point, and store any points that are within radius of epsilon
			if(point.distance(P) <= eps) neighbors.add(point);
		}

        return neighbors;
	}
}