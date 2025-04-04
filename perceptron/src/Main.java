import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final String TRAINING_DATA_FILE = "iris_training.txt";
    private static final String TEST_DATA_FILE = "iris_test.txt";

    public static void main(String[] args) {
        Perceptron perceptron = new Perceptron();
        ArrayList<double[]> trainingData = perceptron.loadData(TRAINING_DATA_FILE);
        ArrayList<double[]> testData = perceptron.loadData(TEST_DATA_FILE);

        System.out.println("Training data:\n");
        perceptron.train(trainingData);
        System.out.println("Test data:\n");
        perceptron.test(testData);

        Scanner sc = new Scanner(System.in);
        boolean run = true;
        while (run) {
            System.out.println("Enter data, split values with spaces or type \"exit\" to quit");
            String line = sc.nextLine();
            if (line.toLowerCase().equals("exit")) {
                run = false;
                continue;
            }
            String[] inputs = line.trim().split("\\s+");
            double[] inputs_2 = new double[inputs.length];
            for(int i = 0; i < inputs.length; i++) {
                inputs_2[i] = Double.parseDouble(inputs[i]);
            }
            if(perceptron.predict(inputs_2) <= 0){
                System.out.println("Prediction: Iris-setosa");
            }else System.out.println("Prediction: NOT Iris-setosa");
            System.out.println("-----------------------------");
        }
    }
}
