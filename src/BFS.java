import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Code is mostly referenced from online
 * Modified to include multiple sources, and to return the paths
 */
public class BFS {
    // prints BFS traversal from a given source s
    public static LinkedList<Integer>[] search(int[] hospitals, HashMap<Integer, ArrayList<Integer>> adj)
    {
        int V = adj.size();
        LinkedList<Integer>[] paths = new LinkedList[V]; //contains the closest hospital for each node
        for (int i=0; i<V; i++) {
            paths[i] = new LinkedList<>();
        }

        // Mark all the vertices as not visited
        boolean[] visited = new boolean[V];

        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // Mark all hospitals as source nodes
        for (int hospital : hospitals) {
            visited[hospital] = true;
            queue.add(hospital);
            paths[hospital].add(hospital);
        }

        int currentNode;
        LinkedList<Integer> currentPath;
        while (queue.size() != 0)
        {
            // Dequeue a vertex from queue and print it
            currentNode = queue.poll();
            currentPath = paths[currentNode];
            // Get all adjacent vertices of the dequeued vertex s
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            for (int n : adj.get(currentNode)) {
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                    paths[n].addFirst(n);
                    for (Integer integer : currentPath) {
                        paths[n].add(integer);
                    }
                }
            }
        }
        return paths;
    }
}
