import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;

public class GraphAlgorithmConsole {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to our application!");
        System.out.println("Place files to be read in data folder");

        // to test the BFS algo
        HashMap<Integer, ArrayList<Integer>> graph = GraphGenerator.getRandomGraph(100, 3);
        int[] hospitals = {10, 50, 80};
        System.out.println("Hospitals are located at 10, 50 and 80");
        LinkedList<Integer>[] result = BFS.search(hospitals, graph);
        for (LinkedList<Integer> path: result) {
            System.out.println(path);
        }
    }
}
