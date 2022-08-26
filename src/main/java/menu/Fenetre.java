package menu;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static javax.imageio.ImageIO.read;

public class Fenetre {

    public static File dossier_destination;
    private JFrame frame;
    private File dossier_photo;
    private File dossier_signatures;
    private JProgressBar progressBar;

    public Fenetre() {
        initialize();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Fenetre window = new Fenetre();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
 
    private static void combineImage(File photofile, File signaturefile) throws IOException {

        BufferedImage photoImage = read(photofile);
        BufferedImage signatureImage = read(signaturefile);
        int combinedWidth = Math.max(photoImage.getWidth(), signatureImage.getWidth());
        int combinedHeight = photoImage.getHeight() + signatureImage.getHeight();
        BufferedImage combinedImage = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = combinedImage.getGraphics();
       
        g.drawImage(photoImage, 0, 0, null);
        g.drawImage(signatureImage, 0, photoImage.getHeight(), null);
        g.dispose();

        ImageIO.write(combinedImage, "png", new File(dossier_destination, "F" + photofile.getName().substring(1)));
    }

    private void initialize() {
        frame = new JFrame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ANOUSSI\\Pictures\\R.png"));
       frame.setBounds(100, 100, 720, 425);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.ORANGE);
       panel.setBounds(-12, 0, 716, 74);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel labelBienvenue = new JLabel("WELCOME");
        labelBienvenue.setHorizontalAlignment(SwingConstants.CENTER);
        labelBienvenue.setFont(new Font("Nirmala UI", Font.ITALIC, 33));
        labelBienvenue.setBounds(84, 5, 561, 58);
        panel.add(labelBienvenue);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(SystemColor.activeCaption);
        panel_1.setBorder(new LineBorder(Color.ORANGE, 3));
        panel_1.setBounds(-2, 75, 716, 311);
        panel_1.setLayout(null);
        frame.getContentPane().add(panel_1);
        JButton Button_photo = new JButton("Selectionnez le Dossier Photo");
        Button_photo.setBounds(26, 11, 238, 31);
        // ce label contient le chemin d'acces a notre dossier photo apres selection
        final JLabel labelphoto = new JLabel("");
        labelphoto.setBackground(SystemColor.inactiveCaptionBorder);
        labelphoto.setBounds(274, 11, 336, 31);
        labelphoto.setFont(new Font("Serif", Font.ITALIC, 13));

        Button_photo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    dossier_photo = chooser.getSelectedFile();
                    labelphoto.setText(dossier_photo.getAbsolutePath());

                }
            }

        });
        panel_1.add(Button_photo);
        panel_1.add(labelphoto);
        JButton Button_signature = new JButton("Selectionnez le Dossier Signature");
        Button_signature.setBounds(26, 72, 238, 31);
        final JLabel label_signature = new JLabel("");
        label_signature.setBounds(274, 72, 336, 31);
        label_signature.setFont(new Font("Serif", Font.ITALIC, 13));
        Button_signature.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    dossier_signatures = chooser.getSelectedFile();

                    label_signature.setText(dossier_signatures.getAbsolutePath());

                }

            }
        });
        panel_1.add(Button_signature);
        panel_1.add(label_signature);
        JButton Button_dest = new JButton("Selectionnez le Dossier Destination");
        Button_dest.setBounds(26, 125, 238, 31);
        final JLabel label_destination = new JLabel("");
        label_destination.setBackground(SystemColor.activeCaption);
        label_destination.setBounds(274, 125, 336, 31);
        label_destination.setFont(new Font("Serif", Font.ITALIC, 13));

        Button_dest.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                dossier_destination = chooser.getSelectedFile();
                label_destination.setText(dossier_destination.getAbsolutePath());
            }
        });
        panel_1.add(label_destination);
        panel_1.add(Button_dest);
        JButton btnValider = new JButton("VALIDER");
        btnValider.setBounds(294, 195, 99, 31);
        btnValider.addActionListener(e -> {
            // verification que les trois dossier contienet tous les fichiers
            if (dossier_photo != null && dossier_destination != null && dossier_signatures != null) {
                // cette methode retourne les fichier dont le prefixe est la lettre P
                File[] photos = dossier_photo.listFiles(file -> file.getName().startsWith("P"));
        
                // cette methode retourne les fichier dont le prefixe est la lettre S
                File[] signatures = dossier_signatures.listFiles(file -> {
                    return file.getName().startsWith("S");
                });

                comparaison( photos, signatures);

            }
        });
        panel_1.add(btnValider);
        progressBar = new JProgressBar(0, 100);
        progressBar.setBounds(94, 269, 516, 31);
        panel_1.add(progressBar);
    }
    private void comparaison( File[] photos, File[] signatures) {
        progressBar.setMaximum(photos.length);
        progressBar.setMinimum(0);
        progressBar.setValue(0);
        progressBar.setStringPainted(true); 
       
        Runnable runnable = () -> {
            int i = 0;

            for (File photo : photos) {

                Arrays.asList(signatures).forEach(signature -> {
                    if (photo.getName().substring(1).equalsIgnoreCase(signature.getName().substring(1))) {
                    
                        try {
                            combineImage(photo, signature);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                });

                progressBar.setValue(++i);
                progressBar.setString(i+" photos traitees "  );

            }
        };
        new Thread(runnable).start();
    }


}
