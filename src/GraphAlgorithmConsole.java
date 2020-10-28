import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;

//TODO: Decide on output format
//TODO: GUI/Graph visualization?
public class GraphAlgorithmConsole {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to our application!");
        System.out.println("Ensure graph text files (file1) are placed under data/graphs,");
        System.out.println("and hospital text files (file2) are placed under data/hospitals");

        int menuOption = -1;
        do {
            System.out.println();
            System.out.println("Main menu:");
            System.out.println("1. Test the algorithms");
            System.out.println("2. Generate hospital files");
            System.out.println("3. Generate random graphs");
            System.out.println("4. Quit");
            System.out.print("Choose an option: ");
            try {
                menuOption = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("ERROR: Please choose a valid option.");
            }
            switch (menuOption) {
                case 1:
                    testAlgorithms(sc);
                    break;
                case 2:
                    generateHospitalFiles(sc);
                    break;
                case 3:
                    generateRandomGraphs(sc);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("ERROR: Please choose a valid option.");
            }
        } while (menuOption != 4);
    }

    private static void testAlgorithms(Scanner sc) {
        HashMap<Integer, ArrayList<Integer>> graph;
        int maxNodeId; // largest node id, since largest node id >= number of nodes
        int[] hospitals;
        int choice;
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
                        BFS.search(hospitals, graph, maxNodeId);
                        break;
                    case 2:
                        BFS_k.search(hospitals, graph, maxNodeId,2);
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
                        BFS_k.search(hospitals, graph, maxNodeId, k);
                        break;
                    default:
                        System.out.println("Please enter a valid option.");
                        continue;
                }
                System.out.print("\nType 'y' to continue using the same graph and hospital files, any keys otherwise: ");
            } while (sc.nextLine().equals("y"));
            System.out.print("\nType 'y' to use another graph, any keys otherwise: ");
        } while (sc.nextLine().equals("y"));
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

    private static void generateHospitalFiles(Scanner sc) {
        Random random = new Random();
        try{
            HashMap<Integer, ArrayList<Integer>> graph = readGraph(sc);
            ArrayList<Integer> nodes = new ArrayList<>(graph.keySet());
            System.out.print("Enter name of new hospital file (e.g. hospital.txt): ");
            FileWriter writer = new FileWriter("data/hospitals/"+sc.nextLine());
            System.out.print("Enter number of hospital nodes: ");
            int numOfHospitals = Integer.parseInt(sc.nextLine());
            writer.write("# " + numOfHospitals + "\n");
            ArrayList<Integer> selectedNodes = new ArrayList<>();
            int randomHospital;
            for (int i = 0; i < numOfHospitals; i++) {
                randomHospital = nodes.get(random.nextInt(nodes.size()));
                if (!selectedNodes.contains(randomHospital)) {
                    selectedNodes.add(randomHospital);
                    writer.write(randomHospital + "\n");
                } else {
                    i--;
                }
            }
            writer.close();
            System.out.println("Hospital file successfully created.");
        } catch (IOException e) {
            System.out.println("Error occurred when writing file.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number of hospital nodes.");
        }
    }

    private static void generateRandomGraphs(Scanner sc) {
        try {
            System.out.print("Enter name of new graph file (e.g. randomGraph.txt): ");
            String fileName = sc.nextLine();
            System.out.print("Enter number of nodes: ");
            int numOfNodes = Integer.parseInt(sc.nextLine());
            System.out.print("Enter average degree: ");
            int avgDegree = Integer.parseInt(sc.nextLine());
            GraphGenerator.generateRandomGraphFile(numOfNodes, avgDegree, fileName);
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a number.");
        }
    }
}
