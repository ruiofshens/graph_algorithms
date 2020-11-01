import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class BFS {
    /**
     * Algorithm to determine the shortest path to the closest hospital, for each node
     * It uses multi-source BFS, where we take all the hospitals as the source nodes
     * For each hospital, we increase the radius of our search by 1 sequentially, searching the same radius for
     * all the hospitals before moving on to radius+1
     *
     * Does not return any value, but rather outputs the results to a text file called "BFSresults.txt"
     *
     * In layman's terms, we are connecting all the hospitals to a single source node, and starting our BFS on that
     * @param hospitals int[] of hospital nodes in the graph, stored as their node id
     * @param adj Graph to be searched, in the form of an adjacency list implemented as a HashMap, with node id as
     *            first argument. This is to accommodate non-contiguous node ids
     * @param maxNodeId The maximum node id that the graph contains
     */
    public static void search(int[] hospitals, HashMap<Integer, ArrayList<Integer>> adj, int maxNodeId)
    {
        // For each node, the path to the nearest hospital
        ArrayList<Integer>[] paths = new ArrayList[maxNodeId+1];

        // Initialize shortest paths for all nodes
        for (int i=0; i<maxNodeId+1; i++) {
            paths[i] = new ArrayList<>();
            // If node id is not present in the graph, we indicate this with a -1 in the path
            if (!adj.containsKey(i)) paths[i].add(-1);
        }

        // Mark all the vertices as not visited
        boolean[] visited = new boolean[maxNodeId+1];

        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<>();

        // Mark all hospitals as source nodes
        for (int hospital : hospitals) {
            visited[hospital] = true;
            queue.add(hospital);
            paths[hospital].add(hospital);
        }

        // Initialize variables to be used in loop
        int currentNode;
        ArrayList<Integer> currentPath;
        while (queue.size() != 0) {
            // Dequeue a vertex from queue and store it's current path
            currentNode = queue.poll();
            currentPath = paths[currentNode];
            // Get all adjacent nodes of currentNode
            for (int n : adj.get(currentNode)) {
                if (!visited[n]) {
                    // If an adjacent node has not been visited, then mark it
                    visited[n] = true;
                    // Enqueue the adjacent node for examination later
                    queue.add(n);
                    // Add the adjacent node as the first node in its own path
                    paths[n].add(n);
                    // Copy the path of currentNode to hospital into its own path
                    for (Integer integer : currentPath) {
                        paths[n].add(integer);
                    }
                }
            }
        }
        outputResults(paths, maxNodeId);
    }

    /**
     * Outputs results of the search to a text file
     * @param paths Result from the BFS search, contains the paths of each node to their nearest hospital
     * @param maxNodeId Max nodeId, used to generate the output list
     */
    private static void outputResults(ArrayList<Integer>[] paths, int maxNodeId) {
        try {
            System.out.print("Enter name of file to save output to (e.g. BFSresults.txt): ");
            String fileName = new Scanner(System.in).nextLine();
            System.out.println("Writing to file " + fileName);
            File myObj = new File("output/"+fileName);
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter("output/"+fileName);
            myWriter.write("Results of parts (a) and (b).\n" +
                    "Format of the output is as follows:\n" +
                    "Node #: node1, node2, ... nodeHospital\n" +
                    "\tDistance: <number of edges to nodeHospital>\n" +
                    "If a node ID does not exist in the graph, or a node is not connected to any hospitals, " +
                    "their distances are recorded with a NIL.\n" +
                    "If a node is a hospital node, the distance will be 0.\n\n");
            for (int node = 0; node < maxNodeId+1; node++) {
                StringBuilder output = new StringBuilder();
                output.append("Node " + node + ": ");
                ArrayList<Integer> path = paths[node];
                if (path.size() == 0) {
                    output.append("No connection to any hospital found\n\tDistance: NIL\n");
                } else if (path.get(0) == -1) {
                    output.append("Node does not exist in the graph\n\tDistance: NIL\n");
                } else {
                    for (int i = 0; i < path.size() - 1; i++) {
                        output.append(path.get(i) + ", ");
                    }
                    output.append(path.get(path.size() - 1) + "\n" +
                            "\tDistance: " + (path.size() - 1) + "\n");
                }
                myWriter.write(output.toString());
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
