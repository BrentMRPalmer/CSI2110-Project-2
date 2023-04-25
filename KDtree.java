//Student Full Name: Brent Palmer
//Student ID: 300193610

import java.util.ArrayList;
import java.util.List;


/**
 * The class <b>KDtree</b> is used to represent a k-d tree.
 * 
 * The class has one instance variable, root, which stores
 * the root of the k-d tree.
 * 
 * The class has 4 methods. It has a constructor that initializes
 * an empty tree. There is a getter for the root. The method add calls
 * the method insert, which inserts a new KDnode into the tree. The root
 * is instantiated in add. 
 * 
 * There is a nested class KDnode that stores the KDnodes 
 * that the k-d tree is built upon.
 *
 * @author Brent Palmer
 */

public class KDtree{

    /**
    * The class <b>KDnode</b> is used to store the nodes, which represent
    * 3D points, in the k-d tree.
    * 
    * The class has 5 instance variables. The node stores the point associated
    * with it, the splitting axis of the point, the value of the splitting axis, and
    * references to the left and right children.
    * 
    * The class has no methods 
    *
    * @author Brent Palmer
    */
    public class KDnode{
        //stores the Point3D associated with the node
        public Point3D point;

        //stores the splitting axis of the node (x=0, y=1, z=2)
        public int axis;

        //stores the value of the splitting axis
        public double value;

        //stores reference to left child (null if no left child)
        public KDnode left;

        //stores reference to right child (null if no right child)
        public KDnode right;

        /**
         * The constructor for <B>KDnode</b> will initialize the point associated with
         * the node, the axis associated with the node, the value of the node based
         * on the associated axis, and set the children to null.
         * 
         * @param pt
         * A Point3D object that represents the point associated with the node
         * 
         * @param axis
         * An int that represents the axis associated with the node
         */
        public KDnode(Point3D pt, int axis){
            this.point = pt;
            this.axis = axis;

            //set the value of this node based on the axis
            this.value = pt.get(axis);

            //initialize children of a new node to null
            left = right = null;
        }
    }

    //stores the root of the KDtree
    private KDnode root;

    /**
    * The constructor for <B>KDtree</b> will initialize and 
    * empty k-d tree with a null root. 
    * 
    * No input parameters.
    */
    public KDtree(){
        root = null;
    }


    /**
    * The method <b>root</b> is a getter method that
    * is used to return the root of the k-d tree.
    * 
    * Inputs and Outputs:
    * No inputs parameters
    * @return
    * Returns a KDnode which is the root of the k-d tree. 
    */
    public KDnode root(){
        return root;
    }

    /**
    * The method <b>add</b> adds a point to the k-d tree
    * by calling the insert method. It also initalizes the
    * root if necessary. 
    * 
    * Inputs and Outputs:
    * @param point
    * A Point3D object that represents the point to be added to the k-d tree
    * 
    * No return 
    */
    public void add(Point3D point){
        //call the insertion starting from the root with axis 0 (x dimension first)
        root = insert(point, root, 0);
    }

    /**
    * The method <b>insert</b> inserts a point into the k-d tree by recursively
    * determining its logical placement.
    * 
    * Inputs and Outputs:
    * @param point
    * A Point3D object that represents the point to be added to the k-d tree
    * 
    * @param node
    * A KDnode that represents the current node in the traversal to find the
    * logical placement of the new node
    * 
    * @param axis
    * An int that represents which dimensions needs to be compared
    * 
    * @return
    * Returns the current KDnode 
    */
    public KDnode insert(Point3D point, KDnode node, int axis){
        //if dummy leaf, insert the node
        if(node == null) node = new KDnode(point, axis);

        //if the value of the point at the designed axis is less than the value of the current node
        //then try to insert in the left child
        //must recursively call insert with the same point, the left child as the next node, and the
        //axis + 1 to change the dimension of comparison (%3 to loop back to x axis after z axis)
        else if(point.get(axis) <= node.value) node.left = insert(point, node.left, (axis+1) % 3);

        //if the value of the point at the designed axis is more than the value of the current node
        //then try to insert in the right child
        //must recursively call insert with the same point, the right child as the next node, and the
        //axis + 1 to change the dimension of comparison (%3 to loop back to x axis after z axis)          
        else node.right = insert(point, node.right, (axis+1) % 3);

        return node;
    }
}