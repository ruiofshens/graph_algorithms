import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.*;

/**
 * Code is mostly referenced from online
 * Modified to include multiple sources, and to return the paths
 */
public class BFS_k {
    // prints BFS traversal from a given source s
    public static int [][] search(int[] hospitals, HashMap<Integer, ArrayList<Integer>> adj, int maxNodeID, int k)
    {
        int V = maxNodeID + 1; //the upper limit of the node ID
        int numNodes = adj.size(); //the actual number of nodes in the graph
        // Contains the lengths of paths to k nearest hospitals. The last element is the number of hospitals visited
        int [][] pathLengths = new int [V][k+1];

        // Record all the hospitals that have visited the node
        int [][] listHospitalsVisited = new int [V][k];

        // Mark all the vertices as not visited by any hospital
        int num_hospitals = hospitals.length; //the total number of hospitals
        boolean [] [] visited = new boolean[V] [num_hospitals]; //array to confirm that it has not been visited by that hospital

        // Mark all the nodes that are already enqueued
        boolean [] isEnqueued = new boolean[V];

        // Integer to keep track of when all nodes have been filled with k hospitals
        int checkAllFilled = numNodes;

        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<>();

        // Create a hashmap to map the h hospitals to indexes 0 to h-1
        HashMap<Integer, Integer> hospitalIndexes = new HashMap<>();

        // Mark all hospitals as source nodes
        for (int hospitalIndex = 0; hospitalIndex < num_hospitals; hospitalIndex ++) {
            int hospital = hospitals[hospitalIndex];
            hospitalIndexes.put(hospital, hospitalIndex); // map a hospital to its index in the hospitals array

            visited[hospital][hospitalIndex] = true;
            queue.add(hospital);
            isEnqueued[hospital] = true;
            pathLengths[hospital][0] = 0; // a path length is always positive. 0 indicates no paths
            listHospitalsVisited[hospital][0] = hospital;
            pathLengths[hospital][k] = 1; // the kth element of pathLengths store the number of hospitals visited
        }

        int currentNode;
        int [] currentPathLengths;
        int [] currentHospitalsVisited;
        int currentNumHospitalVisited;
        while (queue.size() != 0 && checkAllFilled > 0)
        {

            // Dequeue a vertex from queue and print it
            currentNode = queue.poll();
            // Update that the currentNode is no longer in the queue
            isEnqueued[currentNode] = false;
//            System.out.println("current node: " + currentNode);
            currentPathLengths = pathLengths[currentNode];
            currentHospitalsVisited = listHospitalsVisited[currentNode];
//            System.out.println("hospital visited at current node: " + Arrays.toString(currentHospitalsVisited));

            currentNumHospitalVisited = pathLengths[currentNode][k];
            // Get all adjacent vertices of the dequeued vertex s
            // If a adjacent has not been visited by the hospital, and number of currentHospitalsVisited is not k,
            // then mark it visited by that hospital, and append the hospital to listHospitalsVisited associated with the node
            // Only enqueue it if it is not already in queue
            for (int n : adj.get(currentNode)) {
//                System.out.println("child node: " + n);
                for (int i = 0; i < currentNumHospitalVisited; i++) {
                     int hospital =  currentHospitalsVisited[i];
//                     System.out.println("current hospital: " + hospital);
                     int pathLengthToHospital = currentPathLengths[i];
//                        System.out.println("number of hospital visited at child node: " + numHospitalVisited[n]);
//                        System.out.println("visited by this hospital before: " + visited[n][hospital]);
                        if (!visited[n][hospitalIndexes.get(hospital)] && pathLengths[n][k] < k) {
                            visited[n][hospitalIndexes.get(hospital)] = true;

                            // If node not in queue, enqueue
                            if (!isEnqueued[n]) {
                                queue.add(n);
                                isEnqueued[n] = true;
                            }

                            // Put the hospital to the next index in the array hospitalVisited
                            listHospitalsVisited[n][pathLengths[n][k]] = hospital;
                            pathLengths[n][pathLengths[n][k]] = pathLengthToHospital + 1;
                            pathLengths[n][k] += 1;
                            if (pathLengths[n][k] == k) {
                                checkAllFilled -= 1;
//                                System.out.println(checkAllFilled);
                            }
//                            System.out.println("pathLengths of child node: " + Arrays.toString(pathLengths[n]));
                        }
                    }
                }
//                System.out.println("queue size = " + queue.size());
            }
        return pathLengths;
    }

    // Main method used for testing the code
    public static void main(String[] args) {

        HashMap<Integer, ArrayList<Integer>> adj = new HashMap<>();
        adj.put(0, new ArrayList<>(List.of(3,5,6)));
        adj.put(1, new ArrayList<>(List.of(4)));
        adj.put(2, new ArrayList<>(List.of(3,8)));
        adj.put(3, new ArrayList<>(List.of(0,2,8)));
        adj.put(4, new ArrayList<>(List.of(1,7)));
        adj.put(5, new ArrayList<>(List.of(0,6)));
        adj.put(6, new ArrayList<>(List.of(0,5,7)));
        adj.put(7, new ArrayList<>(List.of(4,6,8,11)));
        adj.put(8, new ArrayList<>(List.of(2,3,7,9)));
        adj.put(9, new ArrayList<>(List.of(8,10)));
        adj.put(10, new ArrayList<>(List.of(9)));
        adj.put(11, new ArrayList<>(List.of(7)));

        int numNodes = 12;
        int [] hospitals = {0,8,7};
        int k = 4;
        int maxNodeId = numNodes - 1;

        int[][] pathLengths = BFS_k.search(hospitals, adj, maxNodeId, k);
        for (int i = 0; i < numNodes; i ++) {
            System.out.print("Node " + i + ": ");
            System.out.println(Arrays.toString(pathLengths[i]));
        }
//        System.out.println("Compare that with normal BFS");
//
//        LinkedList<Integer>[] result = BFS.search(hospitals, adj);
//        for (LinkedList<Integer> path: result) {
//            System.out.println(path);
//        }


    }
}
