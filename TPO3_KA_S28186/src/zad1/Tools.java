/**
 *
 *  @author Kowalski Artur S28186
 *
 */

package zad1;


import org.yaml.snakeyaml.Yaml;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tools {
    static Options createOptionsFromYaml(String fileName) throws Exception{
        String yaml = Files.readAllLines(Paths.get(fileName)).stream().collect(Collectors.joining(System.lineSeparator()));
        Map options = new Yaml().loadAs(yaml, Map.class);
        return new Options(
                options.get("host").toString(),
                (int)options.get("port"),
                (boolean)options.get("concurMode"),
                (boolean)options.get("showSendRes"),
                (Map<String, List<String>>) options.get("clientsMap")
        );
    }

}
