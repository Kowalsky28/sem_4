import java.util.*;

public class Perceptron {
    private double[] weights;
    private double learningRate = 0.1;
    private int epochs = 0;
    private final String targetLanguage;

    public Perceptron(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public void train(Map<String, List<double[]>> trainingData) {
        int featureCount = 26;
        weights = new double[featureCount + 1];
        weights[weights.length - 1] = 1;
        //System.out.println(Arrays.toString(weights));

        List<TrainingExample> examples = new ArrayList<>();
        for (Map.Entry<String, List<double[]>> entry : trainingData.entrySet()) {
            for (double[] frequencies : entry.getValue()) {
                double label = entry.getKey().equals(targetLanguage) ? 1.0 : 0.0;
                examples.add(new TrainingExample(frequencies, label));
            }
        }

        boolean perfectAccuracy = false;
        while (!perfectAccuracy) {
            int correct = 0;
            for (TrainingExample example : examples) {
                double prediction = predict(example.features);
                double error = example.label - prediction;

                // Aktualizacja wag
                for (int i = 0; i < featureCount; i++) {
                    weights[i] += learningRate * error * example.features[i];
                }
                weights[weights.length-1] += learningRate * error; // bias

                if (Math.abs(error) < 0.5) correct++;
            }

            double accuracy = (double) correct / examples.size();

            //System.out.printf("Epoka %d - Dokładność: %.2f%%\n", epochs, accuracy * 100);

            if(epochs == 2000 || accuracy == 1.0) {
                perfectAccuracy = true;
                System.out.printf("Epoka %d - Dokładność: %.2f%%\n", epochs, accuracy * 100);
            }
            epochs++;
        }
        //System.out.println(Arrays.toString(weights));
    }

    public double predict(double[] features) {
        double activation = weights[weights.length - 1]; // bias
        for (int i = 0; i < weights.length - 1; i++) {
            activation += weights[i] * features[i];
        }
        return activation >= 0 ? 1.0 : 0.0;
    }

    public void test(Map<String, List<double[]>> testData) {
        int correctTarget = 0;
        int totalTarget = 0;
        int correctOther = 0;
        int totalOther = 0;

        for (Map.Entry<String, List<double[]>> entry : testData.entrySet()) {
            boolean isTargetLanguage = entry.getKey().equals(targetLanguage);
            for (double[] frequencies : entry.getValue()) {
                double prediction = predict(frequencies);
                double expected = entry.getKey().equals(targetLanguage) ? 1.0 : 0.0;

                if (prediction == expected) {
                    if (isTargetLanguage) {
                        correctTarget++;
                    } else {
                        correctOther++;
                    }
                }
                if (isTargetLanguage) {
                    totalTarget++;
                } else {
                    totalOther++;
                }
            }
        }


        System.out.println("Test - Dokładność dla języka "+targetLanguage+": "+(double) correctTarget / totalTarget * 100 +"% "+correctTarget + "/" + totalTarget);
        System.out.println("Test - Dokładność dla pozostałych języków: "+(double) correctOther / totalOther * 100 +"% "+correctOther + "/" + totalOther);
    }

    private static class TrainingExample {

        double[] features;
        double label;
        TrainingExample(double[] features, double label) {
            this.features = features;
            this.label = label;
        }

    }

    public String getTargetLanguage() {
        return targetLanguage;
    }
}
