import java.io.File;
import java.util.Scanner;

public class Affichage {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== DHL File Manager ===");
        while (running) {
            System.out.println("\nCommandes disponibles :");
            System.out.println("1. UPLOAD - Télécharger un fichier vers le serveur");
            System.out.println("2. LIST - Lister les fichiers sur le serveur");
            System.out.println("3. EXIT - Quitter l'application");

            System.out.print("Entrez le numéro de la commande : ");
            String input = scanner.nextLine();

            switch (input) {
                case "1": // UPLOAD
                    System.out.print("Entrez le chemin absolu du fichier à uploader : ");
                    String uploadPath = scanner.nextLine();
                    File uploadFile = new File(uploadPath);
                    if (uploadFile.exists()) {
                        try {
                            ClientFichiers.client("UPLOAD", uploadFile);
                            System.out.println("Fichier uploadé avec succès : " + uploadFile.getName());
                        } catch (Exception e) {
                            System.err.println("Erreur lors de l'upload : " + e.getMessage());
                        }
                    } else {
                        System.out.println("Fichier introuvable. Vérifiez le chemin.");
                    }
                    break;

                case "2": // LIST
                    try {
                        File[] files = ClientFichiers.list();
                        if (files.length > 0) {
                            System.out.println("Fichiers disponibles sur le serveur :");
                            for (int i = 0; i < files.length; i++) {
                                System.out.println((i + 1) + ". " + files[i].getName());
                            }

                            System.out.println("\nOptions supplémentaires :");
                            System.out.println("1. Télécharger un fichier (DOWNLOAD)");
                            System.out.println("2. Supprimer un fichier (REMOVE)");
                            System.out.println("3. Retourner au menu principal");

                            System.out.print("Votre choix : ");
                            String listOption = scanner.nextLine();

                            if (listOption.equals("1")) { // DOWNLOAD
                                System.out.print("Entrez le numéro du fichier à télécharger : ");
                                int fileIndex = Integer.parseInt(scanner.nextLine()) - 1;
                                if (fileIndex >= 0 && fileIndex < files.length) {
                                    try {
                                        ClientFichiers.client("DOWNLOAD", files[fileIndex]);
                                        System.out.println("Fichier téléchargé avec succès : " + files[fileIndex].getName());
                                    } catch (Exception e) {
                                        System.err.println("Erreur lors du téléchargement : " + e.getMessage());
                                    }
                                } else {
                                    System.out.println("Numéro invalide.");
                                }
                            } else if (listOption.equals("2")) { // REMOVE
                                System.out.print("Entrez le numéro du fichier à supprimer : ");
                                int fileIndex = Integer.parseInt(scanner.nextLine()) - 1;
                                if (fileIndex >= 0 && fileIndex < files.length) {
                                    try {
                                        ClientFichiers.client("REMOVE", files[fileIndex]);
                                        System.out.println("Fichier supprimé avec succès : " + files[fileIndex].getName());
                                    } catch (Exception e) {
                                        System.err.println("Erreur lors de la suppression : " + e.getMessage());
                                    }
                                } else {
                                    System.out.println("Numéro invalide.");
                                }
                            }
                        } else {
                            System.out.println("Aucun fichier disponible sur le serveur.");
                        }
                    } catch (Exception e) {
                        System.err.println("Erreur lors de la récupération de la liste : " + e.getMessage());
                    }
                    break;

                case "3": // EXIT
                    running = false;
                    try {
                        ClientFichiers.client("EXIT", null);
                        System.out.println("Application fermée.");
                    } catch (Exception e) {
                        System.err.println("Erreur lors de la fermeture : " + e.getMessage());
                    }
                    break;

                default:
                    System.out.println("Commande invalide. Veuillez réessayer.");
            }
        }

        scanner.close();
    }
}
