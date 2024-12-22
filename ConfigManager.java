import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigManager {
        // Map pour stocker les variables du fichier de configuration
    private static Map<String, String> configMap = new HashMap<>();
    
        public static void readConfigFile() {
                try (BufferedReader br = new BufferedReader(new FileReader("config.txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        // Ignorer les lignes vides ou les commentaires (lignes commençant par #)
                        if (line.trim().isEmpty() || line.startsWith("#")) {
                            continue;
                        }
        
                        // Diviser chaque ligne en clé et valeur (séparés par un égal)
                        String[] parts = line.split("=", 2);  // On divise la ligne en 2 parties
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();
                            configMap.put(key, value);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public String getConfigValue(String key) {
        ConfigManager.readConfigFile();
        return configMap.get(key);
    }
}
