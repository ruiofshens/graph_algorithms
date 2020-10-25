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
import java.util.Scanner;

/**
 * Only need to care about 2 methods: getRandomGraph and getGraphFromFile
 */
public class GraphGenerator {

    /**
     * Basically generates and stores a random graph into randomGraph.txt,
     * then calls getGraphFromFile to return the graph in the form of a HashMap
     * @param numNodes number of nodes in the random graph
     * @param avgDegree average degree of each node in the random graph
     * @return HashMap<Integer, ArrayList<Integer>> where node ID is key and neighbours are values
     */
    public static HashMap<Integer, ArrayList<Integer>> getRandomGraph(int numNodes, int avgDegree) {
        generateRandomGraphFile(numNodes, avgDegree, "randomGraph.txt");
        return getGraphFromFile("data/randomGraph.txt");
    }

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
                if (adjList.get(node1) == null) {
                    adjList.put(node1, new ArrayList<>());
                }
                if (adjList.get(node2) == null) {
                    adjList.put(node2, new ArrayList<>());
                }
                if (!adjList.get(node1).contains(node2)) {
                    adjList.get(node1).add(node2);
                }
            }
            return adjList;
        } catch (FileNotFoundException e) {
            System.out.println("File not found! Please re-enter.");
            return null;
        }
    }

    private static void generateRandomGraphFile(int numNodes, int avgDegree, String fileName) {
        Graph graph = randomGraph(numNodes, avgDegree);
        try {
            FileWriter writer = new FileWriter("data/"+fileName);
            int numOfNodes = graph.getNodeCount();
            int numOfEdges = graph.getEdgeCount();
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
        } catch (IOException e) {
            e.printStackTrace();
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
        HashMap<Integer, ArrayList<Integer>> test = getGraphFromFile("data/graphs/roadNet-PA.txt");
//        int n = test.size();
//        for (int i = 0; i < n; i++) {
//            if (test.get(i) == null) {
//                System.out.println(i + " doesn't exist");
//            }
//        }
//        System.out.println("size: " + n);
    }
}
