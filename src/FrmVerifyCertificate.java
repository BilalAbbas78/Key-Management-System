import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class FrmVerifyCertificate extends JFrame {

    PublicKey publicKey;
    X509Certificate certificate;

    FrmVerifyCertificate(){
        setTitle("Verify Certificate");
        setSize(550, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        JButton btnLoadPublicKey = new JButton("Load Public Key");
        btnLoadPublicKey.setBounds(25, 10, 190, 30);
        add(btnLoadPublicKey);

        JButton btnLoadCertificate = new JButton("Load Certificate");
        btnLoadCertificate.setBounds(25, 50, 190, 30);
        add(btnLoadCertificate);

        JButton btnVerifyCertificate = new JButton("Verify Certificate");
        btnVerifyCertificate.setBounds(25, 90, 190, 30);
        add(btnVerifyCertificate);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(25, 130, 190, 30);
        add(btnBack);

        btnLoadPublicKey.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + "\\Desktop");
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Public Key", "txt");
            fileChooser.addChoosableFileFilter(filter);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
//                System.out.println(selectedFile.getName());
                try {

                    String content = Files.readString(selectedFile.toPath(), StandardCharsets.US_ASCII);
                    JOptionPane.showMessageDialog(null, "Public Key loaded");
//                    System.out.println(content);

                    publicKey = CertificateGenerator.getPublicKeyFromString(content);

//                    publicKey = CertificateGenerator.loadPublicKey(selectedFile);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        btnLoadCertificate.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + "\\Desktop");
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Certificate", "cer");
            fileChooser.addChoosableFileFilter(filter);
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    certificate = CertificateGenerator.loadCertificate(selectedFile);
                    JOptionPane.showMessageDialog(null, "Certificate loaded");
//                    System.out.println(certificate);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        btnVerifyCertificate.addActionListener(e -> {
            if (publicKey == null || certificate == null) {
                JOptionPane.showMessageDialog(null, "Please load public key and certificate");
            }
            else {
                CertificateGenerator.verifyCertificate(certificate, publicKey);
            }
        });

        btnBack.addActionListener(e -> {
            new FrmMain().setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        new FrmVerifyCertificate().setVisible(true);
    }
}
