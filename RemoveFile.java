import java.io.File;

public class RemoveFile {
    public static void rm(File file){
        if (file.exists() && !file.isDirectory()) {
            if (file.delete()) {
                System.out.println("Le fichier : "+file.getAbsolutePath()+" a ete definitivement supprimer");
            }
        }
        else{
            System.out.println(file.getAbsolutePath()+" n'est pas un fichier");
        }
    }
}
