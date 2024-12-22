// import java.awt.*;
// import java.io.File;
// import javax.swing.*;

// public class ListeFichiersSwing {
//     public File[] file ;
//     public ListeFichiersSwing(File[] f) throws Exception {
//                 // Création de la fenêtre principale
//                 this.file = f;
//                 JFrame frame = new JFrame("Affichage de texte");
//                 frame.setSize(800, 400);
        
//                 // Création du panneau principal avec un layout adapté
//                 JPanel panel = new JPanel();
//                 panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
//                 // Titre stylisé
//                 JLabel title = new JLabel("DHL File");
//                 title.setFont(new Font("Comic Sans MS", Font.BOLD, 36)); // Police stylée
//                 title.setAlignmentX(Component.CENTER_ALIGNMENT);
//                 title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Espacement autour du titre
        
//                 // Label de commande
//                 JLabel label_com = new JLabel("Commande à exécuter");
//                 label_com.setFont(new Font("Arial", Font.PLAIN, 20));
//                 label_com.setAlignmentX(Component.CENTER_ALIGNMENT);
        
//                 // Liste des fichiers (à adapter selon la méthode ServeurPrincipal.listFILES())
//                 File[] items = f; 
//                 for (File file : items) {
//                     System.out.println(file);
//                 }
//                 // Assurez-vous que cette méthode existe et fonctionne
//                 if (items == null || items.length == 0) {
//                     // Si aucun fichier n'est trouvé, afficher un message d'erreur
//                     JOptionPane.showMessageDialog(frame, "Aucun fichier trouvé ou erreur de lecture.");
//                     return;  // Arrêter l'exécution si la liste est vide ou invalide
//                 }
        
//                 // Créer la JList pour afficher les fichiers
             
//                 JList<File> list = new JList<>(items);
//                 list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//                 list.setPreferredSize(new Dimension(300, 150));
//                 list.setMaximumSize(new Dimension(300, 200));
        
//                 // Ajouter un JScrollPane pour rendre la liste défilable
//                 JScrollPane listScrollPane = new JScrollPane(list);
//                 listScrollPane.setPreferredSize(new Dimension(300, 200));
//                 listScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        
//                 // Bouton de validation
//                 JButton DMbutton = new JButton("DOWNLOAD");
//                 DMbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
//                 DMbutton.addActionListener(e -> {
//                     // Récupérer le fichier sélectionné
//                     File selected = list.getSelectedValue();
//                     if (selected != null) {
//                         try {
//                             ClientFichiers.client("DOWNLOAD", selected);
//                         } catch (Exception e1) {
//                             // TODO Auto-generated catch block
//                             e1.printStackTrace();
//                         }
//                         // Si un fichier est sélectionné, afficher son nom
//                         JOptionPane.showMessageDialog(frame, "Fichier sélectionné: " + selected.getName());
//                     } else {
//                         // Si aucun fichier n'est sélectionné, afficher un message d'erreur
//                         JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un fichier.");
//                     }
//                 });
//          // Bouton de validation
//          JButton RMbutton = new JButton("REMOVE");
//          RMbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
//          RMbutton.addActionListener(e -> {
//              // Récupérer le fichier sélectionné
//              File selected = list.getSelectedValue();
//              System.out.println(selected);
//              if (selected != null) {
//                 try {
//                     ClientFichiers.client("REMOVE",selected);
//                 } catch (Exception e1) {
//                     // TODO Auto-generated catch block
//                     e1.printStackTrace();
//                 }
//                  // Si un fichier est sélectionné, afficher son nom
//                  JOptionPane.showMessageDialog(RMbutton, "Voulez-vous réellement supprimer le fichier " + selected.getName() + " ?", 
//                  "AVIS DU CLIENT", JOptionPane.WARNING_MESSAGE);     } else {
//                  // Si aucun fichier n'est sélectionné, afficher un message d'erreur
//                  JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un fichier.");
//              }
//          });
//                 // Ajouter tous les composants au panneau
//                 panel.add(title);
//                 panel.add(label_com);
//                 panel.add(listScrollPane); // Ajouter la liste défilable
//                 panel.add(RMbutton);  // Ajouter le bouton de validation
//                 panel.add(DMbutton);
//                 // Ajouter le panneau à la fenêtre
//                 frame.add(panel);
        
//                 // Afficher la fenêtre
//                 frame.setVisible(true);
//             }
//            public static void main(String[] args) {
//                           //Lancer l'application Swing dans le thread d'UI
//             ListeFichiersSwing list = new ListeFichiersSwing(File[] f);                        
//        };
//     }
