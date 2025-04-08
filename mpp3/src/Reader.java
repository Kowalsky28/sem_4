import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Reader {
    private String path;
    public Reader(String path) {
        this.path = path;
    }
    public Map<String, List<String>> readFilesAsStringsGroupedByFolder() throws IOException {
        Map<String, List<String>> folderFilesMap = new HashMap<>();
        Path dataPath = Paths.get(path);

        if (Files.exists(dataPath)) {
            Files.walk(dataPath)
                    .filter(Files::isDirectory)
                    .filter(path -> !path.equals(dataPath))
                    .forEach(folderPath -> {
                        try {
                            List<String> filesContent = Files.list(folderPath)
                                    .filter(Files::isRegularFile)
                                    .map(filePath -> {
                                        try {
                                            return new String(Files.readAllBytes(filePath));
                                        } catch (IOException e) {
                                            System.err.println("Błąd czytania pliku: " + filePath);
                                            return "";
                                        }
                                    })
                                    .collect(Collectors.toList());

                            folderFilesMap.put(folderPath.getFileName().toString(), filesContent);
                        } catch (IOException e) {
                            System.err.println("Błąd przetwarzania folderu: " + folderPath);
                        }
                    });
        }

        return folderFilesMap;
    }

}
