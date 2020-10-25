import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

//TODO: Decide on output format, create abstract algo class again?
public class GraphAlgorithmConsole {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HashMap<Integer, ArrayList<Integer>> graph;
        int maxNodeId; // largest node id, since largest node id >= number of nodes
        int[] hospitals;
        int choice;

        System.out.println("Welcome to our application!");
        System.out.println("Ensure graph text files (file1) are placed under data/graphs,");
        System.out.println("and hospital text files (file2) are placed under data/hospitals");
        System.out.println();

        while (true) {
            graph = readGraph(sc);
            maxNodeId = Collections.max(graph.keySet());
            hospitals = readHospitals(sc);

            System.out.println("");
            System.out.println( "============Algorithms============\n" +
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
//                    int[] hospitals = {10, 50, 80};
//                    System.out.println("Hospitals are located at 10, 50 and 80");
                    LinkedList<Integer>[] result = BFS.search(hospitals, graph); // broken for now
                    for (LinkedList<Integer> path: result) {
                        System.out.println(path);
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
            break;
        }
    }

    private static HashMap<Integer, ArrayList<Integer>> readGraph(Scanner sc) {
        HashMap<Integer, ArrayList<Integer>> graph = null;
        while (graph == null) {
            System.out.print("Enter name of file1 (e.g. file1.txt): ");
            String fileName = sc.nextLine();
            System.out.println("Reading file1...");
            graph = GraphGenerator.getGraphFromFile("data/graphs/"+fileName);
        }
        return graph;
    }

    private static int[] readHospitals(Scanner sc) {
        while (true) {
            try {
                System.out.print("Enter name of file2 (e.g. file2.txt): ");
                String fileName = sc.nextLine();
                System.out.println("Reading file2...");
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
