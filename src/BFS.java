import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class BFS {
    /**
     * Algorithm to determine the shortest path to the closest hospital, for each node
     * It uses multi-source BFS, where we take all the hospitals as the source nodes
     * For each hospital, we increase the radius of our search by 1 sequentially, searching the same radius for
     * all the hospitals before moving on to radius+1
     *
     * In layman's terms, we are connecting all the hospitals to a single source node, and starting our BFS on that
     * @param hospitals int[] of hospital nodes in the graph, stored as their node id
     * @param adj Graph to be searched, in the form of an adjacency list implemented as a HashMap, with node id as
     *            first argument. This is to accommodate non-contiguous node ids
     * @param maxNodeId The maximum node id that the graph contains
     * @return The paths from each node to the nearest hospital, in the form of an array of LinkedLists. The LinkedList
     * contains a value of -1 if the node id is not present in the graph, and is an empty LinkedList if the node is
     * present in the graph, but is not connected to a hospital
     */
    public static String search(int[] hospitals, HashMap<Integer, ArrayList<Integer>> adj, int maxNodeId)
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
        while (queue.size() != 0)
        {
            // Dequeue a vertex from queue and print it
            currentNode = queue.poll();
            currentPath = paths[currentNode];
            // Get all adjacent nodes of currentNode
            for (int n : adj.get(currentNode)) {
                // If an adjacent node has not been visited, then mark it
                if (!visited[n]) {
                    visited[n] = true;
                    // Enqueue the
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
        return outputResults(paths, maxNodeId);
    }

    private static String outputResults(ArrayList<Integer>[] paths, int maxNodeId) {
        String output = "Results of (a) and (b)\n";
        output += "Every 2 lines show the node ID, the path to the nearest hospital, and the distance of the path.\n" +
                "If a node ID does not exist in the graph, then it is indicated with a -1.\n\n";

        for (int node = 0; node < maxNodeId+1; node++) {
            output += "Node " + node + ": ";
            ArrayList<Integer> path = paths[node];
            if (path.size() == 0) {
                output += "No connection to hospital found.\n";
                output += "\tDistance: NIL";
            } else if (path.get(0) == -1) {
                output += "Node does not exist in the graph\n";
                output += "\tDistance: NIL";
            } else {
                for (int i = 0; i < path.size() - 1; i++) {
                    output += path.get(i) + ", ";
                }
                output += path.get(path.size() - 1) + "\n";
                output += "\tDistance: " + (path.size() - 1) + "\n";
            }
        }
        return output;
    }
}
