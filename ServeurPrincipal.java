import java.io.*;
import java.net.*;


public class ServeurPrincipal {
        public static ConfigManager conf = new ConfigManager();
        public static String nbD = conf.getConfigValue("nbr");
        private static final int PORT = Integer.parseInt(conf.getConfigValue("port_server"));
        private static final String DOSSIER_TEMP = conf.getConfigValue("passage");

    public static void main(String[] args) throws Exception {
        File dossierTemp = new File(DOSSIER_TEMP);
        if (!dossierTemp.exists()) {
            dossierTemp.mkdir();
        }
  
        File[] list =listFILES();
        for (File file : list) {
            System.out.println(file.getName());
        }
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur principal démarré sur le port : " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connexion en cours ...");
                new Thread(() -> {
                    try {
                        traiterClient(clientSocket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
               
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String[] mise_en_extension(String fichier){
        String nom =  new String();
        String extension = new String();
       
        nom = fichier.substring(0, fichier.lastIndexOf('.'));
        extension =   fichier.substring(fichier.lastIndexOf('.'));
            String[] result = new String[2];
            result[0] = nom;
            result[1] = extension;
            return result;
        }
    
    private static void traiterClient(Socket clientSocket) throws Exception {
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        String commande = dis.readUTF();
        System.out.println(commande);
            if (commande.startsWith("UPLOAD")) {
                String nomFichier = commande.split(" ")[1];
                File fichierTemp = new File(DOSSIER_TEMP, nomFichier);
                try (FileOutputStream fos = new FileOutputStream(fichierTemp)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = dis.read(buffer)) != -1) {                        
                        fos.write(buffer, 0, bytesRead);

                    }
                    decouperEtDistribuer(fichierTemp);
                }
                System.out.println("Fichier reçu et sauvegardé dans le dossier temporaire : " + fichierTemp.getAbsolutePath());
            }else if (commande.startsWith("DOWNLOAD")) {
                String nomFichier = commande.split(" ")[1];
                traiterDownload(clientSocket , nomFichier);
            }
            else if (commande.equalsIgnoreCase("EXIT")) {
                System.out.println("Le client s'est deconnectee.");
                
            }
            else if (commande.equalsIgnoreCase("LIST")) {
                System.out.println("envoie de la list");
                envoilist(clientSocket);
            } 
            else if (commande.startsWith("REMOVE")) {
                String nomFichier = commande.split(" ")[1];
                System.out.println(nomFichier);
                File temp = new File(nomFichier);
                for (int i = 0; i < 3; i++) {
                    envoyerAuxSousServeurs(temp,conf.getConfigValue("port"+(i+1)),Integer.parseInt(conf.getConfigValue("sb"+(i+1))), 1);
                }
            }
            else {
                System.out.println("Commande inconnue : " + commande);
            }
        }
        private static void decouperEtDistribuer(File fichier) throws IOException {
            long taillePartie = fichier.length() / Integer.parseInt(nbD);
            File[] fichiersParties = new File[(Integer.parseInt(nbD))];
            System.out.println("decoupe");
            try (FileInputStream fis = new FileInputStream(fichier)) {
               String[] file = mise_en_extension(fichier.getName());
            for (int i = 0; i < Integer.parseInt(nbD); i++) {
                fichiersParties[i] = new File(DOSSIER_TEMP,file[0]+Integer.parseInt(conf.getConfigValue("sb"+(i+1)))+file[1]);
                try (FileOutputStream fos = new FileOutputStream(fichiersParties[i])) {
                    byte[] buffer = new byte[1024];
                    long bytesRestants = (i == 2) ? Long.MAX_VALUE : taillePartie;
                    int bytesRead;
                    while (bytesRestants > 0 && (bytesRead = fis.read(buffer, 0, (int) Math.min(buffer.length, bytesRestants))) != -1) {
                        fos.write(buffer, 0, bytesRead);
                        bytesRestants -= bytesRead;
                    }
                }
            }
        }
        for (int i = 0; i < Integer.parseInt(nbD); i++) {
           envoyerAuxSousServeurs(fichiersParties[i],conf.getConfigValue("port"+(i+1)),Integer.parseInt(conf.getConfigValue("sb"+(i+1))),0);
        }
         purgerTemp();
    }

    private static void envoyerAuxSousServeurs(File fichier, String adresse, int port,int signale) throws IOException {
        try (Socket sousServeurSocket = new Socket(adresse, port);
             DataOutputStream dos = new DataOutputStream(sousServeurSocket.getOutputStream())) {
                if (signale == 1) {
                    dos.writeUTF("REMOVE "+fichier.getName());
                    System.out.println("envoie sous server "+fichier.getName());
                }
                if (signale == 0) {
                FileInputStream fis = new FileInputStream(fichier);
                dos.writeUTF("UPLOAD "+fichier.getName());
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }
                dos.flush();
                System.out.println("Fichier " + fichier.getName() + " envoyé au sous-serveur sur " + adresse + ":" + port);
            }
        }
        catch (Exception e) {
            System.out.println(e.getLocalizedMessage()+" "+e.getCause());
        }
    }
        private static void purgerTemp() {
        File dossierTemp = new File(DOSSIER_TEMP);
        File[] fichiers = dossierTemp.listFiles();
        
        if (fichiers != null) {
            for (File fichier : fichiers) {
                if (fichier.isFile()) {
                    if (fichier.delete()) {                       
                        System.out.println("Fichier temporaire supprimé : " + fichier.getName());
                    } else {
                        System.out.println("Impossible de supprimer le fichier : " + fichier.getName());
                    }
                }
            }
        } else {
            System.out.println("Le dossier temporaire est vide ou inaccessible.");
        }
    }
  
     private static void traiterDownload(Socket clientSocket, String nomFichier) throws IOException {
        // Récupère les parties du fichier depuis les sous-serveurs
        recupererSousServeur(nomFichier);
        // Assemble les parties en un fichier complet
        File fichierAssemble = assemblerFichiers(nomFichier);
        // Envoie le fichier assemblé au client
        try (FileInputStream fis = new FileInputStream(fichierAssemble);
             DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }
              System.out.println("Fichier " + nomFichier + " envoyé au client.");
        }
        purgerTemp();
      
    }
     private static File assemblerFichiers(String nomFichier) throws IOException {
        String[] temp = mise_en_extension(nomFichier);
        File fichierAssemble = new File(DOSSIER_TEMP,nomFichier);
        try (FileOutputStream fos = new FileOutputStream(fichierAssemble)) {
            for (int i = 0; i < Integer.parseInt(nbD); i++) {
                File fichierPartie = new File(DOSSIER_TEMP,temp[0]+Integer.parseInt(conf.getConfigValue("sb"+(i+1)))+temp[1]);
                if (fichierPartie.exists()) {
                    try (FileInputStream fis = new FileInputStream(fichierPartie)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }    
                    System.out.println("Partie " + i  + " ajoutée au fichier assemblé.");
                }
                else System.out.println("fichier non existant dans nos stockage");
            }
        }
        return fichierAssemble;
    }
    private static void recupererSousServeur(String nomFichier) throws IOException {
        for (int i = 0; i < 3; i++) {
            try (Socket sousServeurSocket = new Socket(conf.getConfigValue("port"+(i+1)), Integer.parseInt(conf.getConfigValue("sb"+(i+1))));
                 DataOutputStream dos = new DataOutputStream(sousServeurSocket.getOutputStream());
                 DataInputStream dis = new DataInputStream(sousServeurSocket.getInputStream())) {
                 String[] temp = mise_en_extension(nomFichier);
                // Demande la partie du fichier au sous-serveur
                dos.writeUTF("DOWNLOAD " +temp[0]+Integer.parseInt(conf.getConfigValue("sb"+(i+1)))+temp[1]);
                // Vérifie si le sous-serveur renvoie un message de succès
                String response = dis.readUTF();
                if ("OK".equals(response)) {
                // Si le fichier est trouvé, commence à recevoir le fichier
                File fichierPartie = new File(DOSSIER_TEMP,temp[0]+Integer.parseInt(conf.getConfigValue("sb"+(i+1)))+temp[1]);
                    try (FileOutputStream fos = new FileOutputStream(fichierPartie)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = dis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                    System.out.println("Partie " + (i + 1) + " du fichier " + nomFichier + " récupérée du sous-serveur " + (i+1));
                } else {
                    System.out.println("Erreur lors du téléchargement du fichier " + temp[0]+(i+1)+temp[1]+".txt" + " depuis le sous-serveur " + i + ": " + response);
                }
            }
        }
    }      
    public static File[] listFILES() throws Exception {
        File stockage1 = new File("stockage_sous_serveur_1");
        File[] fichiers = stockage1.listFiles();
        File[] listing = new File[fichiers.length];
        int i = 0 ;
        for (File fichier : fichiers) {
            String[] f = fichier.getName().split("12346.");
            File file = new File(f[0]+"."+f[1]);
            if (fichier.isFile()) {
                listing[i] = file;
                i++;
            }
        }
        return listing;
    }
    public static void envoilist(Socket client) throws Exception{
        File[] f = listFILES();
        ObjectOutputStream obs = null;
        try {
            obs = new ObjectOutputStream(client.getOutputStream());
            obs.writeObject(f);
            obs.flush();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
