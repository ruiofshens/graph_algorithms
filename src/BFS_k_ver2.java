import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Code is mostly referenced from online
 * Modified to include multiple sources, and to return the paths
 */
public class BFS_k_ver2 {
    // prints BFS traversal from a given source s
    public static LinkedList<Integer>[] search(int[] hospitals, HashMap<Integer, ArrayList<Integer>> adj, int k)
    {
        int V = adj.size();

        // Contains the lengths of paths to k nearest hospitals
        LinkedList<Integer>[] pathLengths = new LinkedList[V];

        // Record all the hospitals that have visited the node
        LinkedList<Integer> [] hospitalsVisited = new LinkedList[V];

        for (int i=0; i<V; i++) {
            pathLengths[i] = new LinkedList<>();
            hospitalsVisited[i] = new LinkedList<>();
        }

        // Mark all the vertices as not visited by any hospital
        int num_hospitals = hospitals.length; //the number of hospitals
        boolean [] [] visited = new boolean[V] [num_hospitals]; //array to confirm that it has not been visited by that hospital

        // Mark all the nodes that are already enqueued
        boolean [] isEnqueued = new boolean[V];



        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // Mark all hospitals as source nodes
        for (int hospital : hospitals) {
            visited[hospital][hospital] = true;
            queue.add(hospital);
            isEnqueued[hospital] = true;
            pathLengths[hospital].add(0);
        }

        int currentNode;
        LinkedList<Integer> currentPathLengths;
        LinkedList<Integer> currentHospitalsVisited;
        while (queue.size() != 0)
        {
            // Dequeue a vertex from queue and print it
            currentNode = queue.poll();
            currentPathLengths = pathLengths[currentNode];
            currentHospitalsVisited = hospitalsVisited[currentNode];

            // Get all adjacent vertices of the dequeued vertex s
            // If a adjacent has not been visited by the hospital, and number of currentHospitalsVisited is not k,
            // then mark it visited by that hospital, and append the hospital to hospitalsVisited associated with the node
            // Only enqueue it if it is not already in queue
            for (int n : adj.get(currentNode)) {
                for (int hospital: currentHospitalsVisited) {
                    if (!visited[n][hospital] && hospitalsVisited[n].size() < k) {
                        visited[n][hospital] = true;
                        queue.add(n);
                        pathLengths[n].addLast();
                        for (Integer integer : currentPathLengths) {
                            pathLengths[n].add(integer);
                        }
                    }
                }
            }
        }
        return pathLengths;
    }
}
