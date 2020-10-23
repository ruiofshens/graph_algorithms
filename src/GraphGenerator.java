import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * READ FIRST!
 * 2 methods to generate a random graph:
 * getRandomGraphAdjMatrix - returns a matrix int[][]
 * getRandomGraphAdjList - returns an array of LinkedLists
 *
 * Both methods require 2 arguments:
 * numNodes - number of nodes in the random graph
 * avgDegree - average degree each node has in the random graph
 */
public class GraphGenerator {

    public static int[][] getRandomGraphAdjMatrix(int numNodes, int avgDegree) {
        return convertToAdjMatrix(randomGraph(numNodes, avgDegree));
    }

    public static LinkedList[] getRandomGraphAdjList(int numNodes, int avgDegree) {
        return convertToAdjList(getRandomGraphAdjMatrix(numNodes, avgDegree));
    }

    public static int[][] graphFromFileAdjMatrix(String fileName) {
        return convertToAdjMatrix(graphFromFile((fileName)));
    }

    public static LinkedList[] graphFromFileAdjList(String fileName) {
        return convertToAdjList(graphFromFileAdjMatrix(fileName));
    }

    private static Graph graphFromFile(String fileName) {
        try {
            File graphFile = new File(fileName);
            Scanner sc = new Scanner(graphFile);
            String nextLine;
            String[] nodesInLine;
            String node1, node2;
            Graph graph = new SingleGraph("Graph");
            while (sc.hasNextLine()) {
                nextLine = sc.nextLine();
                if (nextLine.startsWith("#")) { // remove headers
                    continue;
                }
                node1 = nextLine.split("\\s+")[0];
                node2 = nextLine.split("\\s+")[1];
                if (graph.getNode(node1) == null) {
                    graph.addNode(node1);
                }
                if (graph.getNode(node2) == null) {
                    graph.addNode(node2);
                }
                try {
                    graph.addEdge(node1 + "&" + node2, node1, node2);
                } catch (EdgeRejectedException e) {
                }
            }
            return graph;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

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

    private static int[][] convertToAdjMatrix(Graph graph) {
        int n = graph.getNodeCount();
        int[][] adjMatrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjMatrix[i][j] = graph.getNode(i).hasEdgeBetween(j) ? 1 : 0;
            }
        }
        return adjMatrix;
    }

    private static LinkedList[] convertToAdjList(int[][] adjMatrix) {
        int n = adjMatrix.length;
        LinkedList[] adjList = new LinkedList[n];
        for (int i = 0; i < n; i++) {
            if (adjList[i] == null) {
                adjList[i] = new LinkedList();
            }
            for (int j = 0; j < n; j++) {
                if (adjMatrix[i][j] == 1) {
                    adjList[i].add(j);
                }
            }
        }
        return adjList;
    }

}
