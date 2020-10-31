import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BenchmarkApp {

    /**
     * ENSURE CALL TO outputResults IN THE FINAL LINE OF search IN BOTH ALGORITHMS
     * ARE COMMENTED OUT BEFORE DOING TESTING!
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HashMap<Integer, ArrayList<Integer>> graph;
        int maxNodeId; // largest node id, since largest node id >= number of nodes
        int[] hospitals;
        int choice;
        final int ITERATIONS = 25;
        final int WARMUPS = 5;
        ArrayList<Double> timings;
        double startTime;
        double endTime;
        do {
            System.out.println();
            graph = readGraph(sc);
            maxNodeId = Collections.max(graph.keySet());
            hospitals = readHospitals(sc);
            do {
                System.out.println();
                System.out.println("============Algorithms============\n" +
                        "|Enter '1' for Question (a) + (b)|\n" +
                        "|Enter '2' for Question (c)      |\n" +
                        "|Enter '3' for Question (d)      |\n" +
                        "==================================");
                try {
                    choice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    choice = -1;
                }
                switch (choice) {
                    case 1:
                        timings = new ArrayList<>();
                        for (int i=0; i < WARMUPS; i++) {
                            BFS.search(hospitals, graph, maxNodeId);
                        }
                        for (int j=0; j < ITERATIONS; j++) {
                            startTime = System.nanoTime();
                            BFS.search(hospitals, graph, maxNodeId);
                            endTime = System.nanoTime();
                            timings.add((endTime-startTime)/1000000);
                        }
                        System.out.println("Median timing: " + timings.get(timings.size()/2));
                        break;
                    case 2:
                        timings = new ArrayList<>();
                        for (int i=0; i < WARMUPS; i++) {
                            BFS_k.search(hospitals, graph, maxNodeId, 2);
                        }
                        for (int j=0; j < ITERATIONS; j++) {
                            startTime = System.nanoTime();
                            BFS_k.search(hospitals, graph, maxNodeId, 2);
                            endTime = System.nanoTime();
                            timings.add((endTime-startTime)/1000000);
                        }
                        System.out.println("Median timing: " + timings.get(timings.size()/2));
                        break;
                    case 3:
                        System.out.print("Enter k: ");
                        int k;
                        while (true) {
                            try {
                                k = Integer.parseInt(sc.nextLine());
                                break;
                            } catch (Exception e) {
                                System.out.println("Please enter a number.");
                            }
                        }
                        timings = new ArrayList<>();
                        for (int i=0; i < WARMUPS; i++) {
                            BFS_k.search(hospitals, graph, maxNodeId, k);
                        }
                        for (int j=0; j < ITERATIONS; j++) {
                            startTime = System.nanoTime();
                            BFS_k.search(hospitals, graph, maxNodeId, k);
                            endTime = System.nanoTime();
                            timings.add((endTime-startTime)/1000000);
                        }
                        System.out.println("Median timing: " + timings.get(timings.size()/2));
                        break;
                    default:
                        System.out.println("Please enter a valid option.");
                        continue;
                }
                //System.out.print("\nType 'y' to continue using the same graph and hospital files, any keys otherwise: ");
            } while (1 == 0); // forces restart of app to start a new test
            //System.out.print("\nType 'y' to use another graph, any keys otherwise: ");
        } while (1 == 0); // forces restart of app to start a new test
    }

    private static HashMap<Integer, ArrayList<Integer>> readGraph(Scanner sc) {
        HashMap<Integer, ArrayList<Integer>> graph = null;
        while (graph == null) {
            System.out.print("Enter name of graph file (e.g. file1.txt): ");
            String fileName = sc.nextLine();
            System.out.println("Reading graph...");
            graph = GraphGenerator.getGraphFromFile("data/graphs/"+fileName);
        }
        return graph;
    }

    private static int[] readHospitals(Scanner sc) {
        while (true) {
            try {
                System.out.print("Enter name of hospital file (e.g. file2.txt): ");
                String fileName = sc.nextLine();
                System.out.println("Reading hospitals...");
                File file = new File("data/hospitals/" + fileName);
                Scanner fileSc = new Scanner((file));

                int numOfHospitals = Integer.valueOf(fileSc.nextLine().replaceAll("[^0-9]", ""));
                int[] hospitals = new int[numOfHospitals];
                for (int i = 0; i < numOfHospitals; i++) {
                    hospitals[i] = Integer.valueOf(fileSc.nextLine());
                }
                return hospitals;
            } catch (FileNotFoundException e) {
                System.out.println("File not found! Please re-enter.");
            } catch (NumberFormatException e) {
                System.out.println("Unable to read data! Check if format of file2 is correct.");
            }
        }
    }
}
