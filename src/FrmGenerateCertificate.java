import org.jdatepicker.DateLabelFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Properties;

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

        JLabel lblIssuer = new JLabel("Issuer Name");
        lblIssuer.setBounds(25, 50, 190, 30);
        add(lblIssuer);

        JTextField txtIssuer = new JTextField();
        txtIssuer.setBounds(25, 80, 190, 30);
        add(txtIssuer);

        JLabel lblIssuerFromDate = new JLabel("From Date");
        lblIssuerFromDate.setBounds(25, 110, 190, 30);
        add(lblIssuerFromDate);

        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setBounds(25, 140, 120, 30);
        add(datePicker);

        JLabel lblIssuerToDate = new JLabel("To Date");
        lblIssuerToDate.setBounds(25, 170, 190, 30);
        add(lblIssuerToDate);

        UtilDateModel model1 = new UtilDateModel();
        JDatePanelImpl datePanel1 = new JDatePanelImpl(model1, new Properties());
        JDatePickerImpl datePicker1 = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
        datePicker1.setBounds(25, 200, 120, 30);
        add(datePicker1);

        JButton btnGenerateRootCertificate = new JButton("Generate Root Certificate");
        btnGenerateRootCertificate.setBounds(25, 240, 190, 30);
        add(btnGenerateRootCertificate);

        JButton btnExportRootCertificate = new JButton("Export Root Certificate");
        btnExportRootCertificate.setBounds(25, 280, 190, 30);
        add(btnExportRootCertificate);

        btnGenerateRootCertificate.addActionListener(e -> {

            if (txtIssuer.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields");
            }
            else {
                try {
                    selfSignedX509Certificate = CertificateGenerator.generateSelfSignedX509Certificate(txtIssuer.getText().trim(), datePicker.getModel().getValue().toString(), datePicker1.getModel().getValue().toString());
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

        JLabel lblClientSubject = new JLabel("Client Name");
        lblClientSubject.setBounds(300, 50, 190, 30);
        add(lblClientSubject);

        JTextField txtClientSubject = new JTextField();
        txtClientSubject.setBounds(300, 80, 190, 30);
        add(txtClientSubject);

        JButton btnGenerateClientCertificate = new JButton("Generate Client Certificate");
        btnGenerateClientCertificate.setBounds(300, 180, 190, 30);
        add(btnGenerateClientCertificate);

        JButton btnExportClientCertificate = new JButton("Export Client Certificate");
        btnExportClientCertificate.setBounds(300, 220, 190, 30);
        add(btnExportClientCertificate);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(170, 320, 150, 30);
        add(btnBack);

        btnGenerateClientCertificate.addActionListener(e -> {

            if (txtClientSubject.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields");
            } else {
                if (selfSignedX509Certificate == null) {
                    JOptionPane.showMessageDialog(this, "Please Generate Root Certificate First", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        certificateSignedX509Certificate = CertificateGenerator.generateCertificateSignedX509Certificate(selfSignedX509Certificate.getIssuerX500Principal().getName().replace("CN=", ""), txtClientSubject.getText(), serialNumber, CertificateGenerator.privateKey);
                        serialNumber++;
                        JOptionPane.showMessageDialog(this, "Client Certificate Generated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
//                    System.out.println(certificateSignedX509Certificate);
                    } catch (CertificateEncodingException | InvalidKeyException | NoSuchProviderException |
                             NoSuchAlgorithmException | SignatureException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }
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

            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + "\\Desktop");
            fileChooser.setDialogTitle("Specify a file to save");
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Certificate File", "cer");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(parentFrame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
//                System.out.println("Save as file: " + fileToSave.getAbsolutePath());

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
