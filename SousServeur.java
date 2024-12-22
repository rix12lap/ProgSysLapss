// import java.io.*;
// import java.net.*;

// public class SousServeur {
//     private final int port;
//     private final String dossierStockage;

//     public SousServeur(int port, String dossierStockage) {
//         this.port = port;
//         this.dossierStockage = dossierStockage;
//     }

//     public void demarrer() {
//         File dossier = new File(dossierStockage);
//         if (!dossier.exists()) {
//             dossier.mkdir();
//         }

//         try (ServerSocket serverSocket = new ServerSocket(port)) {
//             System.out.println("Sous-serveur démarré sur le port : " + port);

//             while (true) {
//                 try (Socket client = serverSocket.accept();
//                      DataInputStream dis = new DataInputStream(client.getInputStream())) {

//                     String nomFichier = dis.readUTF();
//                     File fichier = new File(dossier, nomFichier);

//                     try (FileOutputStream fos = new FileOutputStream(fichier)) {
//                         byte[] buffer = new byte[1024];
//                         int bytesRead;
//                         while ((bytesRead = dis.read(buffer)) != -1) {
//                             fos.write(buffer, 0, bytesRead);
//                         }
//                         // System.out.println("Fichier " + nomFichier + " stocké dans " + dossierStockage);
//                     }
//                 }
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     public static void main(String[] args) {
//         int port = Integer.parseInt(args[0]);
//         String dossierStockage = args[1];
//         new SousServeur(port, dossierStockage).demarrer();
//     }
// }
