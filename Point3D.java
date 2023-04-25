//Student Full Name: Brent Palmer
//Student ID: 300193610

/**
 * The class <b>Point3D</b> is used to 
 * store a point in 3D space. The storage mechanism
 * is the standard conventional cartesian coordinate
 * system for 3 dimensions (x, y, z coords).
 * 
 * The class has four instance variables. Three doubles x, y, and z
 * are used to store the cartesian coordinates. An int 'label'
 * is used to store the cluster status of a point. The status indicates whether the
 * point has been processed, and if it has been processed, it indicates which cluster, if
 * any, it is part of. 
 * 
 * The class includes getters for each of the four instance variables, and a getter for the value
 * associated with a given axis. It also includes a method to calculate the distance between the 
 * point that invokes the method, and any other point. 
 *
 * @author Brent Palmer
 */

public class Point3D{
	//instance variables used to store the coordinates of a 3D point
	private double x;
	private double y;
	private double z;

	//label used to store the cluster of the point after processing
	//null means unprocessed, 0 means noise, else are clusters
	private int label;

	/**
	 * The constructor for <B>Point3D</b> will initialize the three
	 * coordinates x, y, and z to their respective values based on given inputs.
	 * The instance variable label is initialized to -1 to represent that it
	 * has been unprocessed. 
	 * 
	 * @param x
	 * A double that represents the x coordinate of the point
	 * 
	 * @param y
	 * A double that represents the y coordinate of the point
	 * 
	 * @param z
	 * A double that represents the y coordinate of the point
	 */
	public Point3D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		label = -1; //default label (meaning unprocessed) will be set to -1
	}

	//the following three methods are getters for each of the three X, Y, Z coordinates

	/**
	 * The method <b>getX</b> is a getter method that
	 * is used to return the value of the points' X coordinate.
	 * 
	 * Inputs and Outputs:
	 * No inputs parameters
	 * @return
	 * Returns a double that represents the value of the X coordinate. 
	 */
	public double getX(){
		return x;
	}

	/**
	 * The method <b>getY</b> is a getter method that
	 * is used to return the value of the points' Y coordinate.
	 * 
	 * Inputs and Outputs:
	 * No inputs parameters
	 * @return
	 * Returns a double that represents the value of the Y coordinate. 
	 */
	public double getY(){
		return y;
	}

	/**
	 * The method <b>getZ</b> is a getter method that
	 * is used to return the value of the points' Z coordinate.
	 * 
	 * Inputs and Outputs:
	 * No inputs parameters
	 * @return
	 * Returns a double that represents the value of the Z coordinate. 
	 */
	public double getZ(){
		return z;
	}

	/**
	 * The method <b>getLabel</b> is a getter method that
	 * is used to return the label; that is, the status of point.
	 * That is to say:
	 * Unprocessed (-1)
	 * Processed
	 * 	-Noise
	 * 	-Cluster
	 * 
	 * Inputs and Outputs:
	 * No inputs parameters
	 * @return
	 * Returns an int that represents the value of the label. 
	 */
	public int getLabel(){
		return label;
	}

	/**
	 * The method <b>get</b> is a getter method that
	 * is used to return the value assocaited with a certain axis.
	 * For example, for the point (5,3,4), get(1) will return
	 * the value of the y axis, which is the y value 3. 
	 * 
	 * Inputs and Outputs:
	 * @param axis
	 * An int that represents the axis to check the value of.
	 * 
	 * @return
	 * Returns a double that represents the value of the given axis. 
	 */
	public double get(int axis){
		if(axis == 0) return x;
		if(axis == 1) return y;
		return z;
	}

	//the following method is a setter for label

    /**
     * The method <b>setLabel</b> is a setter method that
     * is used to change the label of a point.
     * 
     * Inputs and Outputs:
     * @param label
     * An int that is used to change
     * the current label of the point 
     * 
     * No return
     */
    public void setLabel(int label){
        this.label = label;
    }
    


	/**
	 * The method <b>distance</b> calculates the Euclidean distance 
	 * between two 3-dimensional points

	 * 
	 * Inputs and Outputs:
	 * @param pt
	 * A Point3D object; distance will be calculated between 'this' and pt
	 * @return
	 * Returns a double that represents the distance between 'this' and pt. 
	 */
	public double distance(Point3D pt){
		//Basic formula of the root of each side length squared
		return Math.sqrt( (x - pt.x) * (x - pt.x) + (y - pt.y) * (y - pt.y) + (z - pt.z) * (z - pt.z) );
	}

	/**
	 * The method <b>toString</b> coverts the point to string representation.
	 * 
	 * Inputs and Outputs:
	 * @return
	 * Returns the string representation of the point. 
	 */
  	public String toString() {
	  return "(" + x + "," + y + "," + z + ")";
  	}
}