import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Class that returns a graph in the form of adjacency list (Hashmap)<br>
 * Graphs can come from 2 sources:
 * <ul>
 *     <li>From a text file (same format as real road network file)</li>
 *     <li>Random generation, with number of nodes and average degree specified</li>
 * </ul>
 * GraphAlgorithmConsole uses getGraphFromFile to read in and return the graph.
 * There is no call to getRandomGraph from GraphAlgorithmConsole.
 */
public class GraphGenerator {

    /**
     * Reads from a text file (roughly same format as real road network file) and returns a HashMap of the graph
     * @param fileName
     * @return HashMap<Integer, ArrayList<Integer>> where node ID is key and neighbours are values
     */
    public static HashMap<Integer, ArrayList<Integer>> getGraphFromFile(String fileName) {
        try {
            File graphFile = new File(fileName);
            Scanner sc = new Scanner(graphFile);
            String nextLine;
            int node1, node2;
            HashMap<Integer, ArrayList<Integer>> adjList = new HashMap<>();
            while (sc.hasNextLine()) {
                nextLine = sc.nextLine();
                if (nextLine.startsWith("#")) { // remove headers
                    continue;
                }
                node1 = Integer.valueOf(nextLine.split("\\s+")[0]);
                node2 = Integer.valueOf(nextLine.split("\\s+")[1]);
                try{
                    adjList.get(node1).add(node2);
                } catch (Exception e) {
                    adjList.put(node1, new ArrayList<>());
                }
//                if (adjList.get(node1) == null) {
//                    adjList.put(node1, new ArrayList<>());
//                }
//                if (adjList.get(node2) == null) {
//                    adjList.put(node2, new ArrayList<>());
//                }
//                if (!adjList.get(node1).contains(node2)) {
//                    adjList.get(node1).add(node2);
//                }
            }
            return adjList;
        } catch (FileNotFoundException e) {
            System.out.println("File not found! Please re-enter.");
            return null;
        }
    }

    /**
     * Generates a random graph file in the same format as the real road network file<br>
     * Issue: Number of nodes generated is numNodes + avgDegree + 1 (not sure why)
     * @param numNodes number of nodes in the random graph
     * @param avgDegree average degree of each node in the random graph
     * @param fileName name of file to save graph into
     */
    public static void generateRandomGraphFile(int numNodes, int avgDegree, String fileName) {
        Graph graph = randomGraph(numNodes, avgDegree);
        try {
            FileWriter writer = new FileWriter("data/graphs/"+fileName);
            int numOfNodes = graph.getNodeCount();
            int numOfEdges = graph.getEdgeCount();
            writer.write("# Directed graph (each unordered pair of nodes is saved once): " + fileName + "\n");
            writer.write("# Random graph\n");
            writer.write("# Nodes: " + numOfNodes + " Edges: " + numOfEdges + "\n");
            writer.write("# FromNodeId\tToNodeId\n");
            for (int i = 0; i < numOfNodes; i++) {
                for (int j = 0; j < numOfNodes; j++) {
                    Node thisNode = graph.getNode(i);
                    if (thisNode.hasEdgeBetween(j)) {
                        writer.write(thisNode + "\t" + j + "\n");
                    }
                }
            }
            writer.close();
            System.out.println("Random graph successfully generated.");
        } catch (IOException e) {
            System.out.println("Error occurred when writing file.");
        }
    }

    // for some reason 3 additional nodes are generated,even though I copied the code
    private static Graph randomGraph(int numNodes, int avgDegree) {
        Graph graph = new SingleGraph("Random");
        BaseGenerator gen = new RandomGenerator(avgDegree);
        gen.addSink(graph);
        gen.begin();
        for (int i = 0; i < numNodes; i++) {
            gen.nextEvents();
        }
        gen.end();
        return graph;
    }

    // for testing
    public static void main(String[] args) {
        HashMap<Integer, ArrayList<Integer>> test = getGraphFromFile("data/graphs/test.txt");
        int n = test.size();
        for (int i = 0; i < n; i++) {
            if (test.get(i) == null) {
                //System.out.println(i + " doesn't exist");
                continue;
            } else {
                System.out.println(i + ": " + test.get(i));
            }
        }
        System.out.println("size: " + n);
    }
}
