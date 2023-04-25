//Student Full Name: Brent Palmer
//Student ID: 300193610

import java.util.List;
import java.util.ArrayList;

import java.io.*;  
import java.util.Scanner;  

/**
* The class <b>Exp2</b> is used check the average running time of
* each rangeQuery for each point from the list of points corresponding 
* to a given step value. For example, if the step is ten, it will average
* the rangeQuery times for point 0, 10, 20, etc. 
* 
* To run the code, an example input is as follows:
* java Exp2 kd 0.5 Point_Cloud_1.csv 10
* The first parameter is the algorithm. "kd" for k-d tree search, "lin" for linear search. 
* The second parameter is the chosen eps value. 
* The third parameter is the name of the file that stores the points.
* The fourth parameter is the step value.
*  
* The class has no instance variables. 
* 
* The class has 2 methods. read is used to read in a list of points from
* a given file. main is used to run the actual tests. 
*
* @author Brent Palmer
*/

public class Exp2 {

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
	* The main method <b>main</b> is used to receive the algorithm to be used,
	* the desired eps value, the file name of the file that stores the points, 
	* and the step value that determines which points to be processed.
	* The method runs the rangeQuery corresponding to the chosen algorithm for
	* all of the desired points, measures the time it takes for each rangeQuery,
	* and outputs the average time per rangeQuery to the console. 
	* 
	* Inputs and Outputs:
	* @param args
	* An array of strings from the system input
	* 
	* No return
	*/
  public static void main(String[] args) throws Exception {  
  	KDtree compilation;

  	//an accumulator for the time it takes to perform the nearestneighbors operations
  	long runningTotal = 0;

  	//stores the average running time after processing all of the points
  	double averageRunningTimeMS;

  	//a counter to keep track of how many points were processed
  	int numPointsProcessed = 0;

  	//checks time before and after a rangeQuery
  	long startTime;
  	long endTime;
  
	// not reading args[0]
	double eps = Double.parseDouble(args[1]);

	// reads the csv file
	List<Point3D> points = Exp1.read(args[2]);

	//used to determine the step of which points to process
	int step = Integer.parseInt(args[3]);

	//used to store the neighbors of given point
	List<Point3D> neighbors = new ArrayList<>();

	if(args[0].equals("lin")){
		// if a linear search is requested, run a linear search
		// creates the NearestNeighbor instance
		NearestNeighbors nn = new NearestNeighbors(points);
	
		//loop over all the points in the list, incrementing by the given step
		for(int i = 0; i < points.size(); i+=step){
			startTime = System.nanoTime();
			nn.rangeQuery(eps, points.get(i));
			endTime = System.nanoTime();
			runningTotal += (endTime - startTime);
			numPointsProcessed++;
		}
	}else if(args[0].equals("kd")){
		// if a k-d tree search is requested, run a k-d tree search
		// creates the NearestNeighborKD instance
		NearestNeighborsKD nnKD = new NearestNeighborsKD(points);

		//loop over all the points in the list, incrementing by the given step
		for(int i = 0; i < points.size(); i+=step){
			startTime = System.nanoTime();
			nnKD.rangeQuery(points.get(i), eps);
			endTime = System.nanoTime();
			runningTotal += (endTime - startTime);
			numPointsProcessed++;
		}
	}

	//calculate the average running time in milliseconds
	averageRunningTimeMS = runningTotal/numPointsProcessed/1000000.0;
		
	System.out.println("The running total is " + runningTotal +"ns");
	System.out.println("The number of points processed is " + numPointsProcessed);
	System.out.println("Average time to find the neighbors of every " + step + " points in file " 
		+ args[2] +" using " + args[0] + " search: " + averageRunningTimeMS + "ms");
  }   
}