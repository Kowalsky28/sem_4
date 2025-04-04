import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Perceptron {

    private double[] weights;
    private double learningRate = 0.1;
    private static int counter = 0;

    public ArrayList<double[]> loadData(String filePath) {
        ArrayList<double[]> data = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().trim().split("\\s+");
                double[] features = new double[line.length];
                for (int i = 0; i < line.length - 1; i++) {
                    features[i] = Double.parseDouble(line[i].replace(',', '.'));
                }
                features[line.length - 1] = line[line.length - 1].equals("Iris-setosa") ? 0.0 : 1.0;
                data.add(features);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void train(ArrayList<double[]> trainingData) {
        int featureCount = trainingData.get(0).length - 1;

        weights = new double[featureCount + 1];
        while (!test(trainingData)) {
            for (double[] row : trainingData) {

                double output = predict(row);
                double error = row[row.length - 1] - output; //output = 1 albo 0
                for (int i = 0; i < featureCount; i++) {
                    weights[i] += learningRate * error * row[i];
                }
                weights[featureCount] += learningRate * error;
            }
            counter++;
        }
        System.out.println("Epochs needed: " + counter+"\n");
        /*for(int i = 0; i < weights.length; i++)
            System.out.println(weights[i]);*/
    }

    public double predict(double[] inputs) {
        double activation = weights[weights.length - 1];
        for (int i = 0; i < weights.length - 1; i++) {
            activation += weights[i] * inputs[i];
        }
        return activation >= 0 ? 1.0 : 0.0;
    }

    public boolean test(ArrayList<double[]> testData) {
        int correct = 0;
        for (double[] row : testData) {
            double output = predict(row);
            if (output == row[row.length - 1]) {
                correct++;
            }
        }
        System.out.println("Correctly Classified: " + correct);
        System.out.println("Accuracy: " + (double) correct / testData.size() * 100 + "%");
        System.out.println("-----------------------------");
        return ((double) correct / testData.size() * 100) == 100.0;
    }
}
