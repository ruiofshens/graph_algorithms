import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BFS_k {
    // find the lengths of shortest paths from each node to k-nearest hospitals
    public static void search(int[] hospitals, HashMap<Integer, ArrayList<Integer>> adj, int maxNodeID, int k)
    {
        int V = maxNodeID + 1; //the upper limit of the node ID
        int numNodes = adj.size(); //the actual number of nodes in the graph

        // The first k elements of pathLengths[nodeID] contains the lengths of paths from nodeID to k nearest hospitals.
        // The last element (index k) is the number of hospitals that have visited the nodeID.
        int [][] pathLengths = new int [V][k+1];

        // Record all the hospitals that have visited the node
        int [][] listHospitalsVisited = new int [V][k];

        // Mark all the vertices as not visited by any hospital
        int num_hospitals = hospitals.length; //the total number of hospitals
        // visited[nodeID][hospitalIndex] indicates whether nodeID has been visited by hospitals[hospitalIndex]
        boolean [] [] visited = new boolean[V] [num_hospitals];

        // number of times the edges from each node have been traversed, maximum k
        int[] numEdgeTraversals = new int[V];

        // Mark all the nodes that are already enqueued
        boolean [] isEnqueued = new boolean[V];

        // Integer to keep track of when all nodes have been filled with k hospitals
        int checkAllFilled = 0;

        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<>();

        // Create a hashmap to map the h hospitals to indexes 0 to h-1
        HashMap<Integer, Integer> hospitalIndexes = new HashMap<>();

        // Mark all hospitals as source nodes
        for (int hospitalIndex = 0; hospitalIndex < num_hospitals; hospitalIndex ++) {
            int hospital = hospitals[hospitalIndex]; //get hospitalID
            hospitalIndexes.put(hospital, hospitalIndex); //map a hospital to its index in the hospitals array

            // Mark that the hospital has been visited by itself
            visited[hospital][hospitalIndex] = true;
            pathLengths[hospital][0] = 0; //hospital takes 0 steps to reach itself
            listHospitalsVisited[hospital][0] = hospital;
            pathLengths[hospital][k] = 1; //the kth element of pathLengths store the number of hospitals visited

            // Enqueue the hospital
            queue.add(hospital);
            isEnqueued[hospital] = true;
        }

        int currentNode;
        int [] currentPathLengths;
        int [] currentHospitalsVisited;
        int currentNumHospitalVisited;
        while (queue.size() != 0 && checkAllFilled < numNodes)
        {
            // Dequeue a vertex from queue
            currentNode = queue.poll();

            // Update that the currentNode is no longer in the queue
            isEnqueued[currentNode] = false;

            currentPathLengths = pathLengths[currentNode];
            currentHospitalsVisited = listHospitalsVisited[currentNode];
            currentNumHospitalVisited = pathLengths[currentNode][k];
            int j = numEdgeTraversals[currentNode];
            // Get all adjacent node of the current node
            for (int n : adj.get(currentNode)) {
                // Iterate through all hospitals that have visited the current node, starting from j
                for (int i = j; i < currentNumHospitalVisited; i++) {
                     int hospital =  currentHospitalsVisited[i];
                     int pathLengthToHospital = currentPathLengths[i];
                        // If node has not been visited by the hospital, and number of currentHospitalsVisited is not k,
                        // then mark it visited by that hospital, and append the hospital to listHospitalsVisited associated with the node
                        if (!visited[n][hospitalIndexes.get(hospital)] && pathLengths[n][k] < k) {
                            visited[n][hospitalIndexes.get(hospital)] = true;

                            // Put the hospital to the next index in the array hospitalVisited
                            listHospitalsVisited[n][pathLengths[n][k]] = hospital;
                            pathLengths[n][pathLengths[n][k]] = pathLengthToHospital + 1;
                            pathLengths[n][k] += 1; //Increment number of hospitals that have visited node n

                            // Only enqueue the node if it is not already in queue
                            if (!isEnqueued[n]) {
                                queue.add(n);
                                isEnqueued[n] = true;
                            }

                            // Increment the checkAllFilled to show number of nodes visited by k hospitals
                            if (pathLengths[n][k] == k) {
                                checkAllFilled += 1;
                            }
                        }
                    }
                }
                numEdgeTraversals[currentNode] = currentNumHospitalVisited;
            }
        outputResults(pathLengths, adj, maxNodeID, k);
    }

    private static void outputResults(int[][] pathLengths, HashMap<Integer, ArrayList<Integer>> adj, int maxNodeId, int k) {
        try {
            System.out.print("Enter name of file to save output to (e.g. BFS_kResults.txt): ");
            String fileName = new Scanner(System.in).nextLine();
            System.out.println("Writing to file " + fileName);
            File myObj = new File("output/"+fileName);
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter("output/"+fileName);
            myWriter.write("Results of parts (c) and (d), with k = " + k + ".\n" +
                    "Format of the output is as follows:\n" +
                    "Node #: <distance to nearest hospital>, <distance to 2nd nearest hospital>, ..., <distance to kth nearest hospital>\n" +
                    "\tNumber of hospitals within reach: <number of hospitals reached>\n\n" +
                    "If a node ID does not exist in the graph, their number of hospitals within reach are recorded as NIL.\n" +
                    "If the value of k exceeds the number of hospitals reachable, the remaining distances are indicated with a -1.\n" +
                    "If a node is a hospital node, the first distance will be 0.\n\n");
            for (int node = 0; node < maxNodeId+1; node++) {
                myWriter.write("Node " + node + ": ");
                if (!adj.containsKey(node)) {
                    myWriter.write("Node does not exist in the graph\n");
                    myWriter.write("\tNumber of hospitals within reach: NIL\n");
                } else {
                    if (pathLengths[node][k] == 0) {
                        myWriter.write("No connection to any hospital found\n");
                        myWriter.write("\tNumber of hospitals within reach: 0\n");
                    } else {
                        for (int i = 0; i < pathLengths[node].length - 2; i++) {
                            if (pathLengths[node][i] == 0 && i != 0)
                                myWriter.write("-1, ");
                            else
                                myWriter.write(pathLengths[node][i] + ", ");
                        }
                        if (pathLengths[node][pathLengths[node].length - 2] == 0)
                            myWriter.write("-1\n");
                        else
                            myWriter.write(pathLengths[node][pathLengths[node].length - 2] + "\n");
                        myWriter.write("\tNumber of hospitals within reach: " + pathLengths[node][pathLengths[node].length - 1] + "\n");
                    }
                }
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
