//Student Full Name: Brent Palmer
//Student ID: 300193610

import java.util.List;
import java.util.ArrayList;

import java.io.*;  
import java.util.Scanner;  

/**
 * The class <b>Exp1</b> is used to validate the functionality
 * of KDtree and NearestNeighborsKD. It prints out the nearest neighbors
 * of a given input point. 
 * 
 * To run the code, an example input is as follows:
 * java Exp1 kd 0.05 Point_Cloud_1.csv -5.429850155 0.807567049 -0.398216823
 * The first parameter is the algorithm. "kd" for k-d tree search, "lin" for linear search.
 * The second parameter is the chosen eps value. 
 * The third parameter is the name of the file that stores the points.
 * The final three parameters are the x y z values of the point to process. 
 * 
 * The class has no instance variables. 
 * 
 * The class has 3 methods. read is used to read in a list of points from
 * a given file. save is used to output a list of points to a file, which is 
 * useful for automatically generating outputs. main is used to run the actual 
 * tests. 
 *
 * @author Brent Palmer
 */

public class Exp1 {
  

	/**
	* The method <b>read</b> is used to read a file of 3D points
	* and save them into a list. 
	* 
	* Inputs and Outputs:
	* @param filename
	* String that represents the name of the file to be read from.
	* 
	* @return
	* Returns the List of 3DPoints read from the given file
	*/
	public static List<Point3D> read(String filename) throws Exception {
	  
		List<Point3D> points= new ArrayList<Point3D>(); 
		double x,y,z;

		Scanner sc = new Scanner(new File(filename));  
		// sets the delimiter pattern to be , or \n \r
		sc.useDelimiter(",|\n|\r");  

		// skipping the first line x y z
		sc.next(); sc.next(); sc.next();

		// read points
		while (sc.hasNext()){  
			x = Double.parseDouble(sc.next());
			y = Double.parseDouble(sc.next());
			z = Double.parseDouble(sc.next());
			points.add(new Point3D(x,y,z));  
	  	}   

		sc.close();  //closes the scanner  

		return points;
	}

	/**
	* The method <b>save</b> is used to write the list of Point3D objects
	* to a new file. 
	* 
	* Inputs and Outputs:
	* @param filename
	* String that represents the name of the file to be written to
	* 
	* @param points
	* The list of points to be written to a new file
	* 
	* No return
	*/
	public void save(String filename, List<Point3D> points){
		//use a string to write 
		String currentLine = ""; 

		//create the output stream writer that will be used to write the points to a new file
		OutputStreamWriter output;

		try{
		    //instantiate output to write to the desired new file
		    output = new OutputStreamWriter( new FileOutputStream(filename) );
		    
		    //create header
		    output.write( "x,y,z\n");

		    for(Point3D point : points){
		        //go through each point in the List of points

		        //write each point to the file
		        output.write( point.getX() + "," + point.getY() + "," + point.getZ() + "\n" );
		    }

		    //close the stream after writing every point
		    output.close();

		} catch (Exception e){
		    System.out.println(e);
		}
	} 

    /**
	* The main method <b>main</b> is used to receive the algorithm to be used,
	* the desired eps value, the file name of the file that stores the points, 
	* and the three values that represent the 3D point to be processed.
	* The method runs the rangeQuery corresponding to the chosen algorithm,
	* and outputs the found neighbors to the console. 
	* 
	* Inputs and Outputs:
	* @param args
	* An array of strings from the system input
	* 
	* No return
	*/
	public static void main(String[] args) throws Exception {  
		KDtree compilation;
		//Exp1 saver = new Exp1();

		// not reading args[0]
		double eps = Double.parseDouble(args[1]);

		// reads the csv file
		List<Point3D> points = Exp1.read(args[2]);

		Point3D query = new Point3D(Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));

		//used to store the neighbors of given point
		List<Point3D> neighbors = new ArrayList<>();

		if(args[0].equals("lin")){
			// if a linear search is requested, run a linear search
			// creates the NearestNeighbor instance
			NearestNeighbors nn = new NearestNeighbors(points);

			// use the rangeQuery method that uses linear search to determine the neighbors
			neighbors = nn.rangeQuery(eps, query);

			// write the neighbors to a file
			//saver.save("ptx_lin_" + query.getX() + ".csv", neighbors);

		}else if(args[0].equals("kd")){
			// if a k-d tree search is requested, run a k-d tree search
			// creates the NearestNeighborKD instance
			NearestNeighborsKD nnKD = new NearestNeighborsKD(points);

			// use the rangeQuery method that uses k-d tree search to determine the neighbors
			neighbors = nnKD.rangeQuery(query, eps);

			// write the neighbors to a file
			//saver.save("ptx_kd_" + query.getX() + ".csv", neighbors);
		}

		System.out.println("Results of searching for the neighbors of " + query + " using " + args[0] +" search:");
		System.out.println("Number of neighbors = " + neighbors.size());
		for(Point3D point : neighbors){
		    //go through each point in the List of neighbors and write each point to the terminal
		    System.out.println( point );
		}
  }   
}
