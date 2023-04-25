//Student Full Name: Brent Palmer
//Student ID: 300193610

import java.io.*;
// import java.util.ArrayList;
// import java.util.Stack;
// import java.util.List;
// import java.util.Random;
import java.util.*;

/**
 * The class <b>DBScan</b> is used to enact the data clustering algorithm DBSCAN, which 
 * is Density-Based Spatial Clustering of Applications with Noise.
 * In essence, given a large set of high-dimensional points, the algorithm will clasify
 * neighboring points into clusters. Points that do not belong to any cluster (outlier 
 * points in low-density areas) will be classified as 'noise.'
 * 
 * The class has eight instance variables. points is used to store the list of all the points.
 * minPts stores the minimum points for a cluster, and eps stores the distance away for 
 * a point to be considered part of the same cluster. numClusters is a counter for the clusters,
 * and clusters is an ArrayList of cluster objects to store info unique to each cluster. algorithm
 * stores a String that represent which algorithm to use to find the nearestNeighbors. neighborFinder
 * and neighborFinderKD are used to store references to the NearestNeighbors and NearestNeighborsKD classes
 * which will be used for their rangeQuery methods. They are instantiated based on the chosen algorithm.
 * 
 * The class has setters for eps, minPts, and the algorithm. It has getters for eps, minPts, clusters, 
 * the list of points and the number of clusters. findClusters is a method used to
 * execute the DBScan algorithm and divide the points into clusters. The method read 
 * reads in a csv file of points and stores the points in a list. The method save 
 * writes the list of points with their corresponding clusters and RGB values to
 * a separate RGB file. The main is set up to run the DBScan algorithm given 
 * an algorithm type, a file, eps, and minPts, and output the results to a csv file using save. 
 * 
 * There is a nested class cluster used to store information about each cluster. 
 *
 * @author Brent Palmer
 */

public class DBScan {
    //list of points
    private List<Point3D> points;

    // minimum number of points required surrounding a point (inclusive of itself)  
    // within distance eps for a point to be considered part of a cluster 
    private double minPts;

    //the maximum distance of a point away from another point for the points 
    //to be part of the same neighborhood
    private double eps;

    //a counter that represents the number of distinct clusters found
    private int numClusters;

    //used to store a list of clusters
    private ArrayList<Cluster> clusters;

    //stores the type of nearestNeighbors algorithm
    private String algorithm;


    //stores a reference to NearestNeighbor classes to run rangeQuery
    private NearestNeighbors neighborFinder;
    private NearestNeighborsKD neighborFinderKD;
    
    /**
     * The class <b>Cluster</b> is used to organize the information that pertains 
     * to any specific cluster.
     * 
     * The class has five instance variables. One stores the label of the cluster.
     * A second is a counter that stores the number of points in the cluster.
     * The final three store the RGB values for the cluster.
     * 
     * The class has no methods 
     *
     * @author Brent Palmer
     */
    private static class Cluster implements Comparable<Cluster> {
        //used to store the label of the cluster
        private int label;

        //used to store how many points are in the cluster
        private int numPoints;

        //three variables used to store RGB values of the cluster
        private double red;
        private double green;
        private double blue;

        /**
         * The constructor for <B>Cluster</b> will give a cluster its 
         * label. It will also initialize a points counter to the number
         * of points in the cluster. It will also create random RGB values 
         * for the cluster. 
         * 
         * @param label
         * An int that represents the label of the cluster
         * 
         * @param numPoints
         * An int that represents the number of points in the cluster
         */
        public Cluster(int label, int numPoints){
            //store label of cluster
            this.label = label;

            //initialize counter of cluster points to 0
            this.numPoints = numPoints;

            //Create random RGB values for this cluster 
            Random random = new Random();
            red = random.nextDouble();
            green = random.nextDouble();
            blue = random.nextDouble();
        }

        //sort cluster based on numPoints 
        public int compareTo(Cluster other){
            if(this.numPoints < other.numPoints) return -1;
            if(this.numPoints > other.numPoints) return 1;
            return 0;
        }


    }

    /**
     * The class <b>SortByNumPoints</b> is used as a comparator to compare two clusters
     * based on their numPoints
     * 
     * The class uses a compare method to execute the comparison 
     *
     * @author Brent Palmer
     */
    public static class SortByNumPoints implements Comparator<Cluster> {

        /**
         * The method <b>compare</b> compares two clusters numPoints
         * and returns the value of comparison according to comparators requirements
         * 
         * Inputs and Outputs:
         * @param a
         * The first Cluster to be compared
         * 
         * @param b
         * The second Cluster to be compared 
         * 
         * @return
         * Returns an int that represents the value of the comparison of numPoints
         */

        public int compare(Cluster a, Cluster b){
            return a.numPoints - b.numPoints;
        }
    }

    /**
     * The constructor for <B>DBScan</b> will initialize the list of 3D points 
     * to an input list of 3D points. 
     * The instance variable numClusters is initialized to 0, since the program 
     * begins having not found any clusters yet. 
     * The instance variables of minPts and eps will both be set to 4 as a default.
     * The algorithm is set to "lin" as default. 
     * 
     * @param points
     * A list of Point3D objects that contains all the 3D points that will
     * be divided into clusters
     */
    public DBScan(List<Point3D> points){
        this.points = points;
        minPts = 4;
        eps = 0.65;
        numClusters = 0;
        algorithm = "lin";
        neighborFinder = new NearestNeighbors(points);
    }

    /**
     * The constructor for <B>DBScan</b> will initialize the list of 3D points 
     * to an input list of 3D points. 
     * The instance variable numClusters is initialized to 0, since the program 
     * begins having not found any clusters yet. 
     * The instance variables of minPts and eps will both be set to 4 as a default.
     * The algorithm is set based on the input algorithm. 
     * 
     * @param points
     * A list of Point3D objects that contains all the 3D points that will
     * be divided into clusters
     * 
     * @param algo
     * String that represents the desired algorithm for nearestNeighbors
     */
    public DBScan(List<Point3D> points, String algo){
        this.points = points;
        minPts = 4;
        eps = 0.65;
        numClusters = 0;
        algorithm = algo;
        if(algo.equals("kd")) neighborFinderKD = new NearestNeighborsKD(points);
        else neighborFinder = new NearestNeighbors(points);
    }

    //the following two methods are setters

    /**
     * The method <b>setEps</b> is a setter method that
     * is used to change the value of eps.
     * 
     * Inputs and Outputs:
     * @param eps
     * An int that is used to be assigned to eps to change the 
     * maximum distance of a point away from another point for the points
     * to be part of the same neighborhood. 
     * 
     * No return
     */
    public void setEps(double eps){
        this.eps = eps;
    }

    /**
     * The method <b>setMinPts</b> is a setter method that
     * is used to change the value of minPts.
     * 
     * Inputs and Outputs:
     * @param minPts
     * An int that is used to be assigned to minPts to change the 
     * minimum number of points required surrounding a point (inclusive of itself)  
     * within distance eps for a point to be considered part of a cluster 
     * 
     * No return
     */
    public void setMinPts(double minPts){
        this.minPts = minPts;
    }

    /**
     * The method <b>setAlgorithm</b> is a setter method that
     * is used to change the algorithm for nearestNeighbors.
     * 
     * Inputs and Outputs:
     * @param algo
     * A String that represents the desired algorithm 
     * 
     * No return
     */
    public void setAlgorithm(String algo){
        this.algorithm = algo;
        if(algo.equals("kd")) neighborFinderKD = new NearestNeighborsKD(points);
    }
    
    //the following two methods are getters

    /**
     * The method <b>getNumberOfClusters</b> is a getter method that
     * is used to return the number of clusters found from the DBScan on
     * the list of points.
     * 
     * Inputs and Outputs:
     * No inputs parameters
     * @return
     * Returns a double that represents the number of clusters found. 
     */
    public double getNumberOfClusters(){
        return numClusters;
    }

    /**
     * The method <b>getPoints</b> is a getter method that
     * is used to return the list of Point3D objects.
     * 
     * Inputs and Outputs:
     * No inputs parameters
     * @return
     * Returns a List of Point3D objects. 
     */
    public List<Point3D> getPoints(){
        return points;
    }

    /**
     * The method <b>getClusters</b> is a getter method that
     * is used to return the ArrayList of Cluster objects.
     * 
     * Inputs and Outputs:
     * No inputs parameters
     * @return
     * Returns a List of Point3D objects. 
     */
    public ArrayList<Cluster> getClusters(){
        return clusters;
    }

    /**
     * The method <b>getEps</b> is a getter method that
     * is used to return the value of eps.
     * 
     * Inputs and Outputs:
     * No inputs parameters
     * @return
     * Returns a double that represents eps. 
     */
    public double getEps(){
        return eps;
    }

    /**
     * The method <b>getMinPts</b> is a getter method that
     * is used to return the value of minPts.
     * 
     * Inputs and Outputs:
     * No inputs parameters
     * @return
     * Returns a double that represents minPts. 
     */
    public double getMinPts(){
        return minPts;
    }


    /**
     * The method <b>findClusters</b> uses the stored List of 
     * 3D points to find all the clusters using the stored min points per
     * cluster and the epsilon (the maximum distance for any two points
     * to be in the same neighborhood). All points will be processed and
     * assigned to noise (0) or a cluster (>= 1).
     * 
     * Inputs and Outputs:
     * No inputs parameters
     * No return 
     */
    public void findClusters() {
        //used to store list of neighbors of point currently being processed
        List<Point3D> neighbors;

        //stack of points to process
        Stack<Point3D> toProcess = new Stack<>();

        //current point to process
        Point3D current;

        //count points in current cluster
        int pointsInClusterCount = 0;

        //count noise points
        int noiseCount = 0;

        //initialize list of clusters
        clusters = new ArrayList<>();

        //create noise cluster
        clusters.add(new Cluster(0, 0));
        clusters.get(0).red = clusters.get(0).blue = clusters.get(0).green = 0;


        for (Point3D point : points){
            if(point.getLabel() != -1) continue; /* Already processed */

            //add all neighbors of current point being processed to List neighbors
            if(algorithm.equals("kd")) neighbors = neighborFinderKD.rangeQuery(point, eps);
            else neighbors = neighborFinder.rangeQuery(eps, point);

            if(neighbors.size() < minPts ){
                //if there are less neighbors than required for cluster, label point as noise and move on
                point.setLabel(0);
                noiseCount++;
                continue;
            }

            //if this line is reached, it is the beginning of a new cluster (increase numClusters and label point with new cluster label)
            numClusters++; 
            point.setLabel(numClusters);
            pointsInClusterCount++;

            //push each neighbor onto the stack of points to be processed for the current cluster
            for(Point3D neighbor : neighbors) toProcess.push(neighbor);

            while( !toProcess.empty() ){
                //while that processes every point to be processed

                //process top point
                current = toProcess.pop();

                //if noise, set to current cluster (outer point of cluster)
                if (current.getLabel() == 0){
                    current.setLabel(numClusters);
                    pointsInClusterCount++;
                    noiseCount--;
                }
                //if already part of a cluster (previously processed), continue
                if (current.getLabel() != -1) continue;

                //set label to current cluster
                current.setLabel(numClusters);
                pointsInClusterCount++;

                //find all neighbors of current processed point
                if(algorithm.equals("kd")) neighbors = neighborFinderKD.rangeQuery(current, eps);
                else neighbors = neighborFinder.rangeQuery(eps, current);

                //if dense enough to be part of cluster, add all neighboring points to stack to be processed
                if(neighbors.size() >= minPts) for(Point3D neighbor : neighbors) toProcess.push(neighbor);
            }
            
            clusters.add(new Cluster(numClusters, pointsInClusterCount));
            pointsInClusterCount = 0;
        }

        clusters.get(0).numPoints = noiseCount;

    }
        
    /**
     * The static method <b>read</b> is used to parse a file and extract a list of
     * Point3D objects. The objects are saved in a List.  
     * 
     * Inputs and Outputs:
     * @param filename
     * String that represents the name of the file to be parsed for points
     * 
     * @return
     * Returns the List of Point3D objects read from the given file
     */
    public static List<Point3D> read(String filename){
        //instantiate an ArrayList of Point3D objects to store the points from the file
        List<Point3D> points = new ArrayList<>();
        //create BR to read the file
        BufferedReader input; 
        //create a string to store each line of the csv file
        String line;
        //create array of strings to temporarily store the split line
        String[] splitLine; 

        try{
            //instantiate the buffered reader that takes the input stream of the provided file
            input = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));

            //skip the header x,y,z
            input.readLine();

            //loop through every line of the csv file (end of file will make line null) and create a point extracted from each line
            while( (line = input.readLine()) != null ){
                //save an array of strings storing each coordinate
                //csv file uses comma as delimeter so use split(",") to isolate coordinates
                splitLine = line.split(",");

                //create the new Point3D object and add it to the List of Point3D objects
                points.add(new Point3D(Double.parseDouble(splitLine[0]), Double.parseDouble(splitLine[1]), Double.parseDouble(splitLine[2])));

            }

            //close the stream after collecting every point
            input.close();

        }catch(FileNotFoundException e){
            System.out.println("Could not find file");
        }catch(Exception e){
            System.out.println(e);
        }

        return points;
    } 

    /**
     * The method <b>save</b> is used to write the list of Point3D objects
     * and their respective clusters to a new file. The new file will have a unique
     * RGB color associated with each distinct cluster. Each Point3D will be given
     * the corresponding RGB values.
     * 
     * 
     * Inputs and Outputs:
     * @param filename
     * String that represents the name of the file to be written to
     * 
     * No return
     */
    public void save(String filename){
        //use a string to write 
        String currentLine = ""; 

        //create the output stream writer that will be used to write the points to a new file
        OutputStreamWriter output;

        try{
            //instantiate output to write to the desired new file
            output = new OutputStreamWriter( new FileOutputStream(filename) );
            
            //create header
            output.write( "x,y,z,C,R,G,B\n");

            for(Point3D point : points){
                //go through each point in the List of points

                //write each point to the file
                output.write( point.getX() + "," + point.getY() + "," + point.getZ() + "," + point.getLabel() + "," 
                    + clusters.get(point.getLabel()).red + "," + clusters.get(point.getLabel()).green + "," + 
                    clusters.get(point.getLabel()).blue + "\n" );
            }

            //close the stream after writing every point
            output.close();

        } catch (Exception e){
            System.out.println(e);
        }
    } 


    /**
     * The main method <b>main</b> is used to receive the file name
     * that stores the points, the desired algorithm, eps, and minPts, and then 
     * find the clusters given the specifications. It will also inform the
     * user of the number of clusters found, and write the results to a csv
     * file. Finally, it will print the clusters in order of points per cluster. 
     * 
     * Inputs and Outputs:
     * @param args
     * An array of strings from the system input
     * 
     * No return
     */
    public static void main(String[] args){
        //list to store points read in from csv file
        List<Point3D> points;

        try{
            //throws an exception if wrong type for filename
            //store a list of points from specified csv file
            points = read(args[3]);

            //create an instance of DBScan using the List of points
            DBScan scan = new DBScan(points, args[0]);
            
            try{
                //overwrite eps if given an input for eps
                scan.setEps(Double.parseDouble(args[1]));
            } catch(IndexOutOfBoundsException e){
                //will use default value of 0.65 if nothing passed in
            } catch(Exception e){
                //will use default if not a number
            }

            try{
                //overwrite minPts if given an input for minPts
                scan.setMinPts(Double.parseDouble(args[2]));
            } catch(IndexOutOfBoundsException e){
                //will use default value of 4 if nothing passed in
            } catch(Exception e){
                //will use default if not a number
            }

            //find the clusters for the given List of points
            scan.findClusters();

            //produce output csv file with the points, and the corresponding clusters and RGB values
            //set output file name based on inputs and results of finding clusters
            scan.save(args[3].substring(0, args[3].length()-4) + "_clusters_" + scan.getEps() + "_" + scan.getMinPts() + "_" + scan.getNumberOfClusters() + ".csv");

            //Sort the clusters by number of points per cluster and print out the points in each cluster
            ArrayList<Cluster> clusters = scan.clusters;

            //store the noise cluster to print last
            Cluster noise = clusters.remove(0);

            Collections.sort(clusters, new SortByNumPoints());
            Collections.reverse(clusters);

            for(Cluster cluster : clusters) System.out.println("Cluster " + cluster.label + " contains " + cluster.numPoints + " points");
            System.out.println("Noise cluster contains " + noise.numPoints + " points"); 

        }catch(Exception e){
            System.out.println(e);
        }

    }
}