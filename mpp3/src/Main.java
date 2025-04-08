import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    static String[] languages;
    static Perceptron[] perceptrons;

    public static void main(String[] args) {
        Map<String,List<String>> preparedContents = createPreparedContents("dane");

        Map<String,List<double[]>> relativeLetterFrequency = createRelativeLetterFrequency(preparedContents);
        //Wypisanie częstotliwości
        /*for(Map.Entry<String,List<double[]>> entry : relativeLetterFrequency.entrySet()) {
            System.out.println(entry.getKey());
            for(double[] arr : entry.getValue()) {
                System.out.println(Arrays.toString(arr));
            }
        }*/
        languages = preparedContents.keySet().toArray(new String[0]);
        perceptrons = new Perceptron[languages.length];
        for(int i = 0; i < languages.length; i++) {
            System.out.println("\nTrenowanie dla języka: " + languages[i]);
            perceptrons[i] = new Perceptron(languages[i]);
            perceptrons[i].train(relativeLetterFrequency);
            perceptrons[i].test(createRelativeLetterFrequency(createPreparedContents("test")));
        }
        SwingUtilities.invokeLater(() -> new Window());

    }
    public static List<String> predictLanguage(String line) {
        File file = new File("do_sprawdzenia\\sc\\file.txt");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(line);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<String,List<String>> fileContents = createPreparedContents("do_sprawdzenia");
        Map<String,List<double[]>> FileRelativeLetterFrequency = createRelativeLetterFrequency(fileContents);
        Map<String,Double> predictions = new HashMap<>();
        for(int i = 0; i < languages.length; i++) {
            predictions.put(perceptrons[i].getTargetLanguage(), perceptrons[i]
                    .predict(FileRelativeLetterFrequency.get("sc").get(0)));
        }
        List<String> results = new ArrayList<>();
        for(Map.Entry<String,Double> p : predictions.entrySet()) {
            if(p.getValue() == 1.0){
                results.add(p.getKey());
            }
        }
        return results;
    }
    private static Map<String,List<double[]>> createRelativeLetterFrequency(Map<String,List<String>> preparedContents) {
        Map<String,List<double[]>> relativeLetterFrequency = new HashMap<>();
        for(Map.Entry<String,List<String>> entry : preparedContents.entrySet()) {
            List<double[]> tmp = new ArrayList<>();
            for(String fileredString : entry.getValue()) {
                int[] letterCounts = new int[26];
                fileredString.chars().forEach(c -> letterCounts[c - 'a']++);

                double[] arr = IntStream.range(0, 26)
                        .mapToDouble(i -> (double) letterCounts[i] / fileredString.length())
                        .toArray();
                tmp.add(arr);
            }
            relativeLetterFrequency.put(entry.getKey(), tmp);
        }
        return relativeLetterFrequency;
    }
    private static Map<String,List<String>> createPreparedContents(String path) {
        Map<String,List<String>> preparedContents = new HashMap<>();
        try {
            Map<String,List<String>> allFilesContents = new Reader(path).readFilesAsStringsGroupedByFolder();
            Map<String,List<String>> filteredContents = new HashMap<>();
            for(Map.Entry<String,List<String>> entry : allFilesContents.entrySet()) {
                List<String> filteredString = entry.getValue().stream()
                        .map(s -> Normalizer.normalize(s, Normalizer.Form.NFD))
                        .map(s -> s.replaceAll("[^\\p{ASCII}]", "")) // Usuń znaki diakrytyczne
                        .map(String::toLowerCase)
                        .map(s -> s.replaceAll("[^a-z]", ""))
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                filteredContents.put(entry.getKey(), filteredString);
            }
            preparedContents.putAll(filteredContents);
            /*for(Map.Entry<String,List<String>> entry : filteredContents.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }*/
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return preparedContents;
    }
}
