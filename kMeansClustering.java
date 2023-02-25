// References: https://www.baeldung.com/java-k-means-clustering-algorithm
/*
 * Clustering: implement k-means clustering algorithm from scratch using Java to find six 
 * clusters from control chart data. Once the clusters are formed, extract the examples that 
 * belong to the same cluster into a .txt file. Altogether, your program should output six .txt 
 * files
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class kMeansClustering {
    private static List<double[]> data = new ArrayList<>();
    private static final int NUM_CLUSTERS = 6;
    private static final int NUM_ITERATIONS = 100;
    private static final double THRESHOLD = 0.0001;
    private static List<List<double[]>> clusters = new ArrayList<>();

    public static void main(String[] args) {
        // Read in the data from the text file
        try {
            data = readInput("synthetic_control_data.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Run the k-means algorithm
        kMeans(data, NUM_CLUSTERS, NUM_ITERATIONS);

        // Extract the examples
        extractExamples(clusters);

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

    // Calculate the Euclidean distance between two points
    // Euclidean distance was chosen because the data is continuous
    public static double calculateDistance(double[] point1, double[] point2) {
        double sum = 0;
        for (int i = 0; i < point1.length; i++) {
            sum += Math.pow(point1[i] - point2[i], 2);
        }
        return Math.sqrt(sum);
    }

    // Initialize the centroids to random points
    public static List<double[]> initializeCentroids(List<double[]> data, int numClusters) {
        List<double[]> centroids = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numClusters; i++) {
            int index = random.nextInt(data.size());
            centroids.add(data.get(index));
        }
        return centroids;
    }

    // Assign each data point to the closest centroid
    public static List<List<double[]>> assignClusters(List<double[]> data, List<double[]> centroids) {
        List<List<double[]>> clusters = new ArrayList<>();
        for (int i = 0; i < centroids.size(); i++) {
            clusters.add(new ArrayList<>());
        }

        for (double[] point : data) {
            double minDistance = Double.MAX_VALUE;
            int clusterIndex = -1;
            for (int i = 0; i < centroids.size(); i++) {
                double distance = calculateDistance(point, centroids.get(i));
                if (distance < minDistance) {
                    minDistance = distance;
                    clusterIndex = i;
                }
            }
            clusters.get(clusterIndex).add(point);
        }
        return clusters;
    }

    // Calculate the new centroids based on the clusters
    public static List<double[]> calculateCentroids(List<List<double[]>> clusters) {
        List<double[]> centroids = new ArrayList<>();
        for (List<double[]> cluster : clusters) {
            double[] centroid = new double[cluster.get(0).length];
            for (double[] point : cluster) {
                for (int i = 0; i < point.length; i++) {
                    centroid[i] += point[i];
                }
            }
            for (int i = 0; i < centroid.length; i++) {
                centroid[i] /= cluster.size();
            }
            centroids.add(centroid);
        }
        return centroids;
    }

    // Calculate the sum of squared errors
    public static double calculateSSE(List<List<double[]>> clusters, List<double[]> centroids) {
        double sse = 0;
        for (int i = 0; i < clusters.size(); i++) {
            List<double[]> cluster = clusters.get(i);
            double[] centroid = centroids.get(i);
            for (double[] point : cluster) {
                sse += Math.pow(calculateDistance(point, centroid), 2);
            }
        }
        return sse;
    }

    // Check if the centroids have converged
    public static boolean hasConverged(List<double[]> oldCentroids, List<double[]> newCentroids) {
        for (int i = 0; i < oldCentroids.size(); i++) {
            double[] oldCentroid = oldCentroids.get(i);
            double[] newCentroid = newCentroids.get(i);
            double distance = calculateDistance(oldCentroid, newCentroid);
            if (distance > THRESHOLD) {
                return false;
            }
        }
        return true;
    }

    // Repeat the process until the clusters have converged
    public static void kMeans(List<double[]> data, int numClusters, int numIterations) {
        List<double[]> centroids = initializeCentroids(data, numClusters);
        clusters = assignClusters(data, centroids);
        List<double[]> newCentroids = calculateCentroids(clusters);
        double sse = calculateSSE(clusters, newCentroids);
        boolean hasConverged = hasConverged(centroids, newCentroids);
        int iteration = 0;

        while (!hasConverged && iteration < numIterations) {
            centroids = newCentroids;
            clusters = assignClusters(data, centroids);
            newCentroids = calculateCentroids(clusters);
            sse = calculateSSE(clusters, newCentroids);
            hasConverged = hasConverged(centroids, newCentroids);
            iteration++;
        }

        System.out.println("SSE: " + sse);
        System.out.println("Iterations: " + iteration);
        System.out.println("Centroids: ");
        for (double[] centroid : newCentroids) {
            for (double value : centroid) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    // extract the examples that belong to the same cluster into a .txt file.
    // Altogether, your program should output six .txt files
    public static void extractExamples(List<List<double[]>> clusters) {
        for (int i = 0; i < clusters.size(); i++) {
            List<double[]> cluster = clusters.get(i);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("cluster" + i + ".txt"));
                for (double[] point : cluster) {
                    for (double value : point) {
                        writer.write(value + " ");
                    }
                    writer.write("\n");
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}