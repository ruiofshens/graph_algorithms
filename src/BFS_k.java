import java.util.*;

/**
 * Code is mostly referenced from online
 * Modified to include multiple sources, and to return the paths
 */
public class BFS_k {
    // prints BFS traversal from a given source s
    public static int [][] search(int[] hospitals, HashMap<Integer, ArrayList<Integer>> adj, int k)
    {
        int V = adj.size();

        // Contains the lengths of paths to k nearest hospitals
        int [][] pathLengths = new int [V][k];

        // Record all the hospitals that have visited the node
        int [][] listHospitalsVisited = new int [V][k];

        // Record the current number of hospitals visited the node
        int [] numHospitalVisited = new int[V];


        // Mark all the vertices as not visited by any hospital
        int num_hospitals = hospitals.length; //the total number of hospitals
        boolean [] [] visited = new boolean[V] [V]; //array to confirm that it has not been visited by that hospital

        // Mark all the nodes that are already enqueued
        boolean [] isEnqueued = new boolean[V];

        // Integer to keep track of when all nodes have been filled with k hospitals
        int checkAllFilled = V;

        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // Mark all hospitals as source nodes
        for (int hospital : hospitals) {
            visited[hospital][hospital] = true;
            queue.add(hospital);
            isEnqueued[hospital] = true;
            pathLengths[hospital][0] = 1; // a path length is always positive. 0 indicates no paths
            numHospitalVisited[hospital] = 1;
        }

        int currentNode;
        int [] currentPathLengths;
        int [] currentHospitalsVisited;
        int currentNumHospitalVisited;
        while (queue.size() != 0 && checkAllFilled > 0)
        {
            // Dequeue a vertex from queue and print it
            currentNode = queue.poll();
            currentPathLengths = pathLengths[currentNode];
            currentHospitalsVisited = listHospitalsVisited[currentNode];
            currentNumHospitalVisited = numHospitalVisited[currentNode];
            // Get all adjacent vertices of the dequeued vertex s
            // If a adjacent has not been visited by the hospital, and number of currentHospitalsVisited is not k,
            // then mark it visited by that hospital, and append the hospital to listHospitalsVisited associated with the node
            // Only enqueue it if it is not already in queue
            for (int n : adj.get(currentNode)) {
                for (int i = 0; i < currentNumHospitalVisited; i++) {
                     int hospital =  currentHospitalsVisited[i];
                     int pathLengthToHospital = currentPathLengths[i];
                        if (!visited[n][hospital] && numHospitalVisited[n] < k) {
                            visited[n][hospital] = true;

                            // If node not in queue, enqueue
                            if (!isEnqueued[n]) queue.add(n);

                            // Put the hospital to the next index in the array hospitalVisited
                            listHospitalsVisited[n][numHospitalVisited[n]] = hospital;
                            pathLengths[n][numHospitalVisited[n]] = pathLengthToHospital + 1;
                            numHospitalVisited[n] += 1;
                            if (numHospitalVisited[n] == k) {
                                checkAllFilled -= 1;
                            }
                        }
                    }
                }
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
        int k = 1;

        int[][] pathLengths = BFS_k.search(hospitals, adj, 1);
        for (int i = 0; i < numNodes; i ++) {
            System.out.println(Arrays.toString(pathLengths[i]));
        }
        System.out.println("Compare that with normal BFS");

        LinkedList<Integer>[] result = BFS.search(hospitals, adj);
        for (LinkedList<Integer> path: result) {
            System.out.println(path);
        }


    }
}
