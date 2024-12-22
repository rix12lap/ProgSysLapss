import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Fenetre{
    
    public static void main(String[] args) throws Exception {
        File[]  items2 = ClientFichiers.list();
        JFrame listing = new JFrame("LIST");
        JPanel panel2 = new JPanel();
         panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        listing.setSize(400, 500);
    JList<File> listf = new JList<>(items2);
    listf.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listf.setPreferredSize(new Dimension(800, 150));
    listf.setMaximumSize(new Dimension(800, 200));
    
    //Ajouter un JScrollPane pour rendre la liste défilable
    JScrollPane listfScrollPane = new JScrollPane(listf);
    listfScrollPane.setPreferredSize(new Dimension(800, 200));
    listfScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // Bouton de validation
    JButton DMbutton = new JButton("DOWNLOAD");
    DMbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
    DMbutton.addActionListener(a -> {
        //   Récupérer le fichier sélectionné
        File selectedf = listf.getSelectedValue();
        try {
            ClientFichiers.client("DOWNLOAD", selectedf);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //   Si un fichier est sélectionné, afficher son nom
        JOptionPane.showMessageDialog(listing, "Fichier sélectionné: " + selectedf.getName());
    });
    //  Bouton de validation
    JButton RMbutton = new JButton("REMOVE");
    RMbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
    RMbutton.addActionListener(b -> {
        //     Récupérer le fichier sélectionné
        File selectedf = listf.getSelectedValue();
        try {
            ClientFichiers.client("REMOVE",selectedf);
        } catch (Exception e1) {
            //       TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //   Si un fichier est sélectionné, afficher son nom
        JOptionPane.showMessageDialog(RMbutton, "Voulez-vous réellement supprimer le fichier " + selectedf.getName() + " ?", 
        "AVIS DU CLIENT", JOptionPane.WARNING_MESSAGE);
    });
    JButton refresh = new JButton("REFRESH");
    refresh.setAlignmentX(Component.TOP_ALIGNMENT);
    refresh.addActionListener(r ->{
      listing.getContentPane().removeAll();  
    });
    panel2.add(listfScrollPane);
    panel2.add(RMbutton);
    panel2.add(DMbutton);
    panel2.add(refresh);
    listing.add(panel2);
    listing.setVisible(true);
    }
}
