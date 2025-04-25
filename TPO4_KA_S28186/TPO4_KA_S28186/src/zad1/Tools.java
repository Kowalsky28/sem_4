/**
 *
 *  @author Kowalski Artur S28186
 *
 */

package zad1;


import org.yaml.snakeyaml.Yaml;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tools {
    //https://pejot.sharepoint.com/sites/2425L_wis_TPO_26_sem4/_layouts/15/stream.aspx?id=%2Fsites%2F2425L%5Fwis%5FTPO%5F26%5Fsem4%2FShared%20Documents%2FGeneral%2FRecordings%2FTPO%2D20250416%5F183307%2DNagrywanie%20spotkania%2Emp4&referrer=StreamWebApp%2EWeb&referrerScenario=AddressBarCopied%2Eview%2Ef24aa5eb%2D1ccc%2D437f%2D84f5%2D56f33e115d2e

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
