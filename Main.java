public class Main {
    public static void main(String[] args) {
            ConfigManager reader = new ConfigManager();
          
    
            // Accéder aux valeurs lues depuis le fichier
            String port_server = reader.getConfigValue("port_server");
            String ip_server = reader.getConfigValue("ip_server");
            String sb1 = reader.getConfigValue("sb1");
            String sb2 = reader.getConfigValue("sb2");
            String sb3 = reader.getConfigValue("passage");
            String nb = reader.getConfigValue("nbr");
            System.out.println(Integer.parseInt(nb));
            // Afficher les valeurs lues
            System.out.println("Username: " + port_server);
            System.out.println("Password: " + ip_server);
            System.out.println("Server Address: " +sb1 );
            System.out.println("Server Address: " +sb2 );
            System.out.println("Server Address: " +sb3 );
        
    }
}
