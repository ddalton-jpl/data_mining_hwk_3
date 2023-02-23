import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class kMeansClustering {
    private static List<double[]> data = new ArrayList<>();
    public static void main(String[] args) {
        // Read in the data from the text file
        try {
            data = readInput("synthetic_control_data.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<double[]> readInput(String filename) throws IOException {
        List<double[]> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.trim().split("\\s+");
            double[] dataPoint = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                dataPoint[i] = Double.parseDouble(values[i]);
            }
            data.add(dataPoint);
        }
        br.close();
        return data;
    }
}