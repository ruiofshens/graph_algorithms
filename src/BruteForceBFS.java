import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class BruteForceBFS {

    /**
     * For parts (a) and (b)
     * @param graph
     * @param maxNodeId
     */
    public static void nearestPath(HashMap<Integer, ArrayList<Integer>> graph, int maxNodeId, int[] hospitals)
    {
        for (int node=0; node<=maxNodeId; node++) {
            if (graph.get(node) == null) {
                continue;
            }
            System.out.print("Node " + node + ": ");
            // Mark all the vertices as not visited(By default
            // set as false)
            boolean visited[] = new boolean[maxNodeId+1];

            // Create a queue for BFS
            LinkedList<Integer> queue = new LinkedList<Integer>();

            // Mark the current node as visited and enqueue it
            visited[node] = true;
            queue.add(node);

            int s;
            boolean hospitalFound = false;
            while (queue.size() != 0) {
                // Dequeue a vertex from queue and print it
                s = queue.poll();
                System.out.print(s + " ");
                for (int hospital : hospitals) {
                    if (hospital == s) {
                        hospitalFound = true;
                        break;
                    }
                }
                // Get all adjacent vertices of the dequeued vertex s
                // If a adjacent has not been visited, then mark it
                // visited and enqueue it
                Iterator<Integer> i = graph.get(s).listIterator();
                while (i.hasNext()) {
                    int n = i.next();
                    if (!visited[n]) {
                        for (int hospital : hospitals) {
                            if (hospital == n) {
                                System.out.print(n);
                                hospitalFound = true;
                                break;
                            }
                        }
                        if (hospitalFound) {
                            break;
                        }
                        visited[n] = true;
                        queue.add(n);
                    }
                }
                if (hospitalFound) {
                    break;
                }
            }
            System.out.println();
        }
    }
}
