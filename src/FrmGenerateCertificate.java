import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

public class FrmGenerateCertificate extends JFrame {

    //    CertificateGenerator issuerCertificate = new CertificateGenerator();
    X509Certificate selfSignedX509Certificate;
    X509Certificate certificateSignedX509Certificate;
    static int serialNumber = 1;

    FrmGenerateCertificate(){
        setTitle("Generate Certificate");
        setSize(550, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        JLabel lblRootCertificate = new JLabel("Root Certificate");
        lblRootCertificate.setBounds(25, 10, 190, 30);
        add(lblRootCertificate);

        JLabel lblRootEmail = new JLabel("Root Email");
        lblRootEmail.setBounds(25, 50, 190, 30);
        add(lblRootEmail);

        JTextField txtRootEmail = new JTextField();
        txtRootEmail.setBounds(25, 80, 190, 30);
        add(txtRootEmail);

        JLabel lblRootSubject = new JLabel("Root Subject");
        lblRootSubject.setBounds(25, 110, 190, 30);
        add(lblRootSubject);

        JTextField txtRootSubject = new JTextField();
        txtRootSubject.setBounds(25, 140, 190, 30);
        add(txtRootSubject);

        JButton btnGenerateRootCertificate = new JButton("Generate Root Certificate");
        btnGenerateRootCertificate.setBounds(25, 180, 190, 30);
        add(btnGenerateRootCertificate);

        JButton btnExportRootCertificate = new JButton("Export Root Certificate");
        btnExportRootCertificate.setBounds(25, 220, 190, 30);
        add(btnExportRootCertificate);

        btnGenerateRootCertificate.addActionListener(e -> {

            if (txtRootEmail.getText().trim().isEmpty() || txtRootSubject.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all fields");
            }
            else {
                try {
                    selfSignedX509Certificate = CertificateGenerator.generateSelfSignedX509Certificate(txtRootEmail.getText(), txtRootSubject.getText());
                    serialNumber = 1;
                    JOptionPane.showMessageDialog(this, "Root Certificate Generated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
//                System.out.println(selfSignedX509Certificate);
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(this, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }


        });

        btnExportRootCertificate.addActionListener(e -> {
            if (selfSignedX509Certificate == null) {
                JOptionPane.showMessageDialog(this, "Please Generate Root Certificate First", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                exportCertificate(selfSignedX509Certificate);
            }
        });




        JLabel lblClientCertificate = new JLabel("Client Certificate");
        lblClientCertificate.setBounds(300, 10, 190, 30);
        add(lblClientCertificate);

        JLabel lblClientEmail = new JLabel("Client Email");
        lblClientEmail.setBounds(300, 50, 190, 30);
        add(lblClientEmail);

        JTextField txtClientEmail = new JTextField();
        txtClientEmail.setBounds(300, 80, 190, 30);
        add(txtClientEmail);

        JLabel lblClientSubject = new JLabel("Client Subject");
        lblClientSubject.setBounds(300, 110, 190, 30);
        add(lblClientSubject);

        JTextField txtClientSubject = new JTextField();
        txtClientSubject.setBounds(300, 140, 190, 30);
        add(txtClientSubject);

        JButton btnGenerateClientCertificate = new JButton("Generate Client Certificate");
        btnGenerateClientCertificate.setBounds(300, 180, 190, 30);
        add(btnGenerateClientCertificate);

        JButton btnExportClientCertificate = new JButton("Export Client Certificate");
        btnExportClientCertificate.setBounds(300, 220, 190, 30);
        add(btnExportClientCertificate);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(170, 280, 150, 30);
        add(btnBack);

        btnGenerateClientCertificate.addActionListener(e -> {

            if (txtClientEmail.getText().trim().isEmpty() || txtClientSubject.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all fields");
            } else {
                try {
                    certificateSignedX509Certificate = CertificateGenerator.generateCertificateSignedX509Certificate(txtClientEmail.getText(), txtClientSubject.getText(), serialNumber, CertificateGenerator.privateKey);
                    serialNumber++;
                    JOptionPane.showMessageDialog(this, "Client Certificate Generated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println(certificateSignedX509Certificate);
                }
                catch (CertificateEncodingException | InvalidKeyException | NoSuchProviderException |
                       NoSuchAlgorithmException | SignatureException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
        });

        btnExportClientCertificate.addActionListener(e -> {
            if (certificateSignedX509Certificate == null) {
                JOptionPane.showMessageDialog(this, "Please Generate Client Certificate First", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                exportCertificate(certificateSignedX509Certificate);
            }
        });

        btnBack.addActionListener(e -> {
            new FrmMain().setVisible(true);
            dispose();
        });

    }

    void exportCertificate(X509Certificate certificate){
        try {
            // parent component of the dialog
            JFrame parentFrame = new JFrame();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Certificate File", "cer");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(parentFrame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                System.out.println("Save as file: " + fileToSave.getAbsolutePath());

                CertificateGenerator.exportCertificate(certificate, fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Root Certificate Exported Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                System.out.println("Save command cancelled by user.");
            }
        }
        catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new FrmGenerateCertificate().setVisible(true);
    }
}
