import java.io.*;
import java.net.*;

public class ClientFichiers extends Thread {
    public static ConfigManager conf = new ConfigManager();
    private static final String HOST =conf.getConfigValue("ip_server") ;
    private static final int PORT = Integer.parseInt(conf.getConfigValue("port_server"));
    public static void client(String commande,File fichier) throws Exception {
        // Création du dossier "client_files" pour les fichiers téléchargés
        File dossierClient = new File("client_files");
        if (!dossierClient.exists()) {
            dossierClient.mkdir();
            System.out.println("Dossier 'client_files' créé pour stocker les fichiers téléchargés.");
        }
        try (
             Socket socket = new Socket(HOST, PORT);             
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {
            System.out.println("Connecté au serveur principal.");
            while (true) {

                if (commande.equalsIgnoreCase("EXIT")) {
                    System.out.println("Déconnexion...");
                    output.writeUTF("EXIT");
                    break;
                }
                if (commande.equalsIgnoreCase("REMOVE")) {
                    System.out.println("Suppression..."+fichier.getName());
                    output.writeUTF("REMOVE "+fichier.getName());
                    break;
                }
                if (commande.equals("UPLOAD")) {
                    if (!fichier.exists() || !fichier.isFile()) {
                        System.out.println("Erreur : Fichier introuvable.");
                        continue;
                    }
                    output.writeUTF("UPLOAD " + fichier.getName());
                    try (FileInputStream fis = new FileInputStream(fichier)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            output.write(buffer, 0, bytesRead);
                        }
                        output.flush();
                        System.out.println("fichier envoyer avec succes");
                        break;
                    }
                } else if (commande.equals("DOWNLOAD")) {
                    output.writeUTF("DOWNLOAD " + fichier.getName());
                    // Réception du fichier depuis le serveur
                    File fichierRecu = new File(dossierClient,fichier.getName()); // Sauvegarde dans client_files
                    try (FileOutputStream fos = new FileOutputStream(fichierRecu)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = dis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                        System.out.println("Fichier " + fichier.getName() + " téléchargé avec succès et sauvegardé dans 'client_files'.");
                        break;
                    }                   
                } else {
                    System.out.println("Commande inconnue. Veuillez réessayer.");
                }
            }

        } catch (IOException e) {
            System.out.println("Server is disconnected");
        }
    }
    public static File[] list() throws Exception {
        try (
             Socket socket = new Socket(HOST, PORT);             
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {
            System.out.println("Connecté au serveur principal.");
            output.writeUTF("LIST");
            File[] F = receivelist(socket); 
            return F;  

        } catch (IOException e) {
            System.out.println("Server is disconnected");
        }
                return null;
    }
    
    public static File[] receivelist(Socket client) throws Exception{
        ObjectInputStream obs = null;
        try {
            obs = new ObjectInputStream(client.getInputStream());
          @SuppressWarnings("unchecked")
        File[] list = (File[])obs.readObject();
        return list;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}
