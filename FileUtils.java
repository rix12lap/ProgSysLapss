import java.io.File;

public class FileUtils {

    // Fonction pour supprimer un fichier en vérifiant son nom et ses extensions
    public static boolean deleteFileByNameAndExtension(String directoryPath, String fileName) {
        // Vérifier que le répertoire existe
        File folder = new File(directoryPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Dossier non valide ou introuvable.");
            return false;
        }

        // Créer un objet File pour le fichier avec le nom donné
        File fileToDelete = new File(folder,fileName);
        
        // Vérifier si le fichier existe
        if (!fileToDelete.exists()) {
            System.out.println("Le fichier n'existe pas.");
            return false;
        }

        // Vérifier si l'extension du fichier correspond aux extensions valides
        // Si toutes les conditions sont remplies, supprimer le fichier
        boolean deleted = fileToDelete.delete();
        if (deleted) {
            System.out.println("Le fichier a été supprimé avec succès : " + fileName);
            return true;
        } else {
            System.out.println("Échec de la suppression du fichier.");
            return false;
        }
    }

    // Fonction pour obtenir l'extension d'un fichier

    // public static void main(String[] args) {
    //     // Exemple d'utilisation
    //     String directoryPath = "C:\\Reseau\\transfert\\stockage";
    //     String fileName = "list.txt";

    //     boolean success = deleteFileByNameAndExtension(directoryPath, fileName);
    //     if (success) {
    //         System.out.println("Fichier supprimé avec succès.");
    //     } else {
    //         System.out.println("Échec de la suppression.");
    //     }
    // }
}
