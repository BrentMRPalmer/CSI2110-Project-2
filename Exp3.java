//Student Full Name: Brent Palmer
//Student ID: 300193610

import java.util.List;
import java.util.ArrayList;

import java.io.*;  
import java.util.Scanner;  

/**
* The class <b>Exp3</b> is used check the running time of DBScan using either
* a linear search or a k-d tree search. 
* 
* To run the code, an example input is as follows:
* java Exp3 kd 1.2 10 Point_Cloud_2.csv
* The first parameter is the algorithm. "kd" for k-d tree search, "lin" for linear search.
* The second parameter is the chosen eps value.  
* The third parameter is the chosen minPts value.
* The fourth parameter is the name of the file that stores the points.
*  
* The class has no instance variables. 
* 
* The class has 2 methods. read is used to read in a list of points from
* a given file. main is used to run the actual tests. 
*
* @author Brent Palmer
*/

public class Exp3 {
  
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
	* the desired eps value, the desured minPts, and the file name of the file 
	* that stores the points.
	* The method runs DBScan corresponding to the chosen algorithm, 
	* and measures the time it takes to run the entire DBScan. This includes
	* reading the points, finding the clusters, writing to a file, and 
	* outputing the clusters to the console.
	* 
	* Inputs and Outputs:
	* @param args
	* An array of strings from the system input
	* 
	* No return
	*/
  public static void main(String[] args) throws Exception {  
  	KDtree compilation;

  	//checks time before and after a DBScan
  	long startTime;
  	long endTime;

  	startTime = System.nanoTime();

  	DBScan.main(args);

    endTime = System.nanoTime();

    //calculates the total time of DBScan in ms
    double totalTime = ((double)endTime - startTime)/1000000;
			
		System.out.println("The total time to run DBScan on file " + args[3] + " using " + args[0] +" search is " + totalTime + "ms");
	  }   
}