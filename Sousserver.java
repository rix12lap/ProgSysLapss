import java.io.*;
import java.net.*;

public class Sousserver {
    private final int port;
    private final String dossierStockage;

    public Sousserver(int port, String dossierStockage) {
        this.port = port;
        this.dossierStockage = dossierStockage;
    }

    public void demarrer() {
        File dossier = new File(dossierStockage);
        if (!dossier.exists()) {
            dossier.mkdir();
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Sous-serveur démarré sur le port : " + port + "attribue au stockage "+dossierStockage);
            while (true) {
                try (Socket client = serverSocket.accept();
                     DataInputStream dis = new DataInputStream(client.getInputStream());
                     DataOutputStream dos = new DataOutputStream(client.getOutputStream())) {
                        //recuperation de la commande venant du server principale
                        String commande = dis.readUTF();
                        //verifie la commande
                        System.out.println(commande);
                    if (commande.startsWith("UPLOAD")) {
                        System.out.println("upload...");
                        String nomFichier = commande.split(" ")[1];
                        //ouvrir le flux pour lire le fichier du server
                    File fichier = new File(dossier, nomFichier);
                    try (FileOutputStream fos = new FileOutputStream(fichier)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = dis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                        System.out.println("Fichier " + nomFichier + " stocké dans " + dossierStockage);
                        }                
                    }
                    // Vérifie si la commande est un téléchargement de fichier
                    else if (commande.startsWith("DOWNLOAD")) {
                        String nomFichier = commande.split(" ")[1];
                        File fichier = new File(dossierStockage, nomFichier);
                        // Vérifie si le fichier demandé existe
                        if (fichier.exists()) {
                            dos.writeUTF("OK"); // Signale que le fichier existe
                            // Envoie le fichier demandé
                            //ouvrir le flux pour envoyer le fichier du server
                            try (FileInputStream fis = new FileInputStream(fichier)) {
                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = fis.read(buffer)) != -1) {
                                    dos.write(buffer, 0, bytesRead);
                                }
                            }
                            System.out.println("Fichier " + nomFichier + " envoyé au serveur principal.");
                        } else {
                            dos.writeUTF("Fichier non trouvé");
                        }
                    } 
                    else if (commande.startsWith("REMOVE")) {
                        String nomFichier = commande.split(" ")[1];
                        String[] p = ServeurPrincipal.mise_en_extension(nomFichier);
                        System.out.println(p[0]+port+p[1]);
                        FileUtils.deleteFileByNameAndExtension(dossierStockage,p[0]+port+p[1]);                        
                    }
                    else {
                        dos.writeUTF("Commande inconnue");
                        serverSocket.close();
                    }
                } catch (IOException e) {
                    System.out.println("probleme de commande ou bien le fichier est introuvable");
                    System.out.println(e.getCause());
                    break;
                }
            }
        } catch (IOException e) {
          System.out.println("probleme du server");
          System.out.println(e.getCause());
        }
    }

    public static void main(String[] args) {
        ConfigManager reader = new ConfigManager();
        String port = reader.getConfigValue(args[0]); 
        int sport = Integer.parseInt(port);
        String dossierStockage = reader.getConfigValue(args[0] + "s");
        new Sousserver(sport, dossierStockage).demarrer();
    }
}
