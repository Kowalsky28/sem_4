import java.util.*;

public class Perceptron {
    private double[] weights;
    private double learningRate = 0.1;
    private int epochs = 0;

    public String getTargetLanguage() {
        return targetLanguage;
    }

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



            if(epochs == 2000){
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

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
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
}
/*
import java.util.*;

public class Perceptron {
    private double[] weights;
    private double learningRate = 0.05;  // Zmniejszony learning rate
    private int epochs = 0;

    public String getTargetLanguage() {
        return targetLanguage;
    }

    private final String targetLanguage;
    private final double errorThreshold = 0.01;
    private double bestError = Double.MAX_VALUE;
    private double[] bestWeights;
    private final int patience = 40;  // Early stopping po 20 epokach bez poprawy
    private int epochsWithoutImprovement = 0;

    public Perceptron(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public void train(Map<String, List<double[]>> trainingData) {
        int featureCount = 26;
        weights = new double[featureCount + 1];
        bestWeights = new double[featureCount + 1];
        Random rand = new Random();

        // Lepsza inicjalizacja wag
        for (int i = 0; i < weights.length; i++) {
            weights[i] = rand.nextGaussian() * 0.1;  // Rozkład normalny
        }
        normalizeWeights();

        List<TrainingExample> examples = new ArrayList<>();
        for (Map.Entry<String, List<double[]>> entry : trainingData.entrySet()) {
            for (double[] frequencies : entry.getValue()) {
                double label = entry.getKey().equals(targetLanguage) ? 1.0 : -1.0;
                double[] normalizedFeatures = normalizeFeatures(frequencies);
                examples.add(new TrainingExample(normalizedFeatures, label));
            }
        }

        // Mieszanie przykładów treningowych
        Collections.shuffle(examples);

        boolean shouldStop = false;
        int maxEpochs = 2000;

        while (!shouldStop && epochs < maxEpochs) {
            double totalError = 0;

            for (TrainingExample example : examples) {
                double prediction = predictRaw(example.features);
                double error = example.label - prediction;

                // Aktualizacja wag z momentum
                for (int i = 0; i < featureCount; i++) {
                    weights[i] += learningRate * error * example.features[i];
                }
                weights[featureCount] += learningRate * error;

                totalError += Math.abs(error);
            }

            double averageError = totalError / examples.size();

            // Adaptive learning rate
            if (epochs % 100 == 0 && epochs > 0) {
                learningRate *= 0.95;
            }

            // Śledzenie najlepszych wag
            if (averageError < bestError) {
                bestError = averageError;
                System.arraycopy(weights, 0, bestWeights, 0, weights.length);
                epochsWithoutImprovement = 0;
            } else {
                epochsWithoutImprovement++;
            }

            */
/*System.out.printf("Epoka %d - Średni błąd: %.4f, LR: %.5f%n",
                    epochs, averageError, learningRate);*/
/*


            // Warunki stopu
            shouldStop = (averageError < errorThreshold) ||
                    (epochsWithoutImprovement >= patience);

            epochs++;
        }

        // Przywróć najlepsze wagi
        weights = bestWeights;

        System.out.println("Trening zakończony. Najlepszy błąd: " + bestError);
    }

    private void normalizeWeights() {
        double norm = 0.0;
        for (double w : weights) {
            norm += w * w;
        }
        norm = Math.sqrt(norm);

        if (norm > 0) {
            for (int i = 0; i < weights.length; i++) {
                weights[i] /= norm;
            }
        }
    }

    private double[] normalizeFeatures(double[] features) {
        double[] normalized = new double[features.length];
        double norm = 0.0;

        for (double f : features) {
            norm += f * f;
        }
        norm = Math.sqrt(norm);

        if (norm > 0) {
            for (int i = 0; i < features.length; i++) {
                normalized[i] = features[i] / norm;
            }
        }

        return normalized;
    }

    public double predictRaw(double[] features) {
        double[] normalizedFeatures = normalizeFeatures(features);
        double activation = weights[weights.length - 1];
        for (int i = 0; i < weights.length - 1; i++) {
            activation += weights[i] * normalizedFeatures[i];
        }
        return activation;
    }

    public double predict(double[] features) {
        return predictRaw(features) >= 0 ? 1.0 : -1.0;
    }

    public void test(Map<String, List<double[]>> testData) {
        int correct = 0;
        int total = 0;

        for (Map.Entry<String, List<double[]>> entry : testData.entrySet()) {
            for (double[] frequencies : entry.getValue()) {
                double prediction = predict(frequencies);
                double expected = entry.getKey().equals(targetLanguage) ? 1.0 : -1.0;

                if (prediction == expected) {
                    correct++;
                }
                total++;
            }
        }

        System.out.printf("\nTest - Dokładność dla języka %s: %.2f%%\n",
                targetLanguage, (double) correct / total * 100);
    }

    private static class TrainingExample {
        double[] features;
        double label;

        TrainingExample(double[] features, double label) {
            this.features = features;
            this.label = label;
        }
    }
}*/
