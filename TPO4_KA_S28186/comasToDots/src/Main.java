import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "C:\\Users\\PC\\Documents\\studia\\mpp4\\iris_test.txt";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        convertCommasToDots(path);
    }
    public static void convertCommasToDots(String filePath) {
        try {
            // Odczytaj zawartość pliku
            Path path = Paths.get(filePath);
            String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

            // Zamień przecinki na kropki
            String modifiedContent = content.replace(',', '.');

            // Zapisz zmodyfikowaną zawartość z powrotem do pliku
            Files.write(path, modifiedContent.getBytes(StandardCharsets.UTF_8));

            System.out.println("Pomyślnie zamieniono przecinki na kropki w pliku: " + filePath);
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas przetwarzania pliku: " + e.getMessage());
        }
    }
}
