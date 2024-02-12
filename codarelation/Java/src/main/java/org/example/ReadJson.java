package org.example;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
public class ReadJson {
    public static String getSourceCode(String fileName) throws FileNotFoundException, IOException {
        Path path = Paths.get(fileName);

        try (Reader reader = Files.newBufferedReader(path,
                StandardCharsets.UTF_8)) {

            JsonParser parser = new JsonParser();
            JsonElement tree = parser.parse(reader);

            JsonObject file = tree.getAsJsonObject();

            JsonObject historyShort = file.get("changeHistoryShort").getAsJsonObject();

            /*exclude those revisions when the container file of a method was renamed*/
            historyShort.entrySet().removeIf(entry -> entry.getValue().getAsString().equals("yrename"));

            String bornId = null;
            /* find the source code of the methods when they were born (from the commit type YIntroduce*/
            for (Map.Entry<String, JsonElement> typeChangeID : historyShort.entrySet()) {
                if (typeChangeID.getValue().getAsString().equals("Yintroduced")) {
                    bornId = typeChangeID.getKey();
                }
            }

            JsonObject details = file.get("changeHistoryDetails").getAsJsonObject();
            JsonObject codeactual = new JsonObject();
            String code;
            for (Map.Entry<String, JsonElement> historyCode : details.entrySet()) {

                if (historyCode.getKey().equals(bornId)) {
                    codeactual = historyCode.getValue().getAsJsonObject();
                }
            }
            if (codeactual.get("actualSource") != null) {
                code = codeactual.get("actualSource").getAsString();
                code = code.trim();
                return code;
            }
            return null;
        }
    }
    public static int getRevisionNum(String fileName) throws IOException {

        Path path = Paths.get(fileName);

        try (Reader reader = Files.newBufferedReader(path,
                StandardCharsets.UTF_8)) {

            JsonParser parser = new JsonParser();
            JsonElement tree = parser.parse(reader);
            JsonObject file = tree.getAsJsonObject();
            JsonObject historyShort = file.get("changeHistoryShort").getAsJsonObject();

            /*exclude those revisions when the container file of a method was renamed*/
            historyShort.entrySet().removeIf(entry -> entry.getValue().getAsString().equals("yrename"));

        int revisionNum=historyShort.size();
            return revisionNum;
        }
    }
}


