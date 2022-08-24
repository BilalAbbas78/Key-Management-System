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
import java.text.ParseException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class FrmGenerateCertificate extends JFrame {

    //    CertificateGenerator issuerCertificate = new CertificateGenerator();
    X509Certificate selfSignedX509Certificate;
    X509Certificate certificateSignedX509Certificate;
    static int serialNumber = 1;

    FrmGenerateCertificate(){
        setTitle("Generate Certificate");
        setSize(550, 500);
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
        AtomicReference<JDatePickerImpl> datePicker = new AtomicReference<>(new JDatePickerImpl(datePanel, new DateLabelFormatter()));
        datePicker.get().setBounds(25, 140, 120, 30);
        add(datePicker.get());

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

        JButton btnLoadRootCertificate = new JButton("Load Root Certificate");
        btnLoadRootCertificate.setBounds(25, 320, 190, 30);
        add(btnLoadRootCertificate);

        btnGenerateRootCertificate.addActionListener(e -> {

            if (txtIssuer.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields");
            }
            else {
                try {
                    selfSignedX509Certificate = CertificateGenerator.generateSelfSignedX509Certificate(txtIssuer.getText().trim(), datePicker.get().getModel().getValue().toString(), datePicker1.getModel().getValue().toString());
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

        btnLoadRootCertificate.addActionListener(e -> {
            selfSignedX509Certificate = CertificateGenerator.loadCertificateFromFile();
            JOptionPane.showMessageDialog(null, "Certificate loaded successfully");
            assert selfSignedX509Certificate != null;
            txtIssuer.setText(selfSignedX509Certificate.getIssuerX500Principal().getName().replaceFirst("CN=", ""));





//            try {
//                datePicker.set(new JDatePickerImpl(datePanel, (JFormattedTextField.AbstractFormatter) new DateLabelFormatter().stringToValue(selfSignedX509Certificate.getNotBefore().toString())));
//            } catch (ParseException ex) {
//                throw new RuntimeException(ex);
//            }

//            try {
//                SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
//                datePicker.getModel().setValue(selfSignedX509Certificate.getNotBefore());
//            } catch (ParseException ex) {
//                throw new RuntimeException(ex);
//            }
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

        JLabel lblClientFromDate = new JLabel("From Date");
        lblClientFromDate.setBounds(300, 110, 190, 30);
        add(lblClientFromDate);

        UtilDateModel model2 = new UtilDateModel();
        JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, new Properties());
        JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
        datePicker2.setBounds(300, 140, 120, 30);
        add(datePicker2);

        JLabel lblClientToDate = new JLabel("To Date");
        lblClientToDate.setBounds(300, 170, 190, 30);
        add(lblClientToDate);

        UtilDateModel model3 = new UtilDateModel();
        JDatePanelImpl datePanel3 = new JDatePanelImpl(model3, new Properties());
        JDatePickerImpl datePicker3 = new JDatePickerImpl(datePanel3, new DateLabelFormatter());
        datePicker3.setBounds(300, 200, 120, 30);
        add(datePicker3);

        JButton btnGenerateClientCertificate = new JButton("Generate Client Certificate");
        btnGenerateClientCertificate.setBounds(300, 240, 190, 30);
        add(btnGenerateClientCertificate);

        JButton btnExportClientCertificate = new JButton("Export Client Certificate");
        btnExportClientCertificate.setBounds(300, 280, 190, 30);
        add(btnExportClientCertificate);

        JButton btnLoadClientCertificate = new JButton("Load Client Certificate");
        btnLoadClientCertificate.setBounds(300, 320, 190, 30);
        add(btnLoadClientCertificate);

        JButton btnVerify = new JButton("Verify");
        btnVerify.setBounds(170, 360, 150, 30);
        add(btnVerify);

        btnGenerateClientCertificate.addActionListener(e -> {

            if (txtClientSubject.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields");
            } else {
                if (selfSignedX509Certificate == null) {
                    JOptionPane.showMessageDialog(this, "Please Generate Root Certificate First", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        certificateSignedX509Certificate = CertificateGenerator.generateCertificateSignedX509Certificate(selfSignedX509Certificate.getIssuerX500Principal().getName().replace("CN=", ""), txtClientSubject.getText(), serialNumber, CertificateGenerator.privateKey, datePicker2.getModel().getValue().toString(), datePicker3.getModel().getValue().toString());
                        serialNumber++;
                        JOptionPane.showMessageDialog(this, "Client Certificate Generated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
//                    System.out.println(certificateSignedX509Certificate);
                    } catch (CertificateEncodingException | InvalidKeyException | NoSuchProviderException |
                             NoSuchAlgorithmException | SignatureException | ParseException ex) {
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

        btnLoadClientCertificate.addActionListener(e -> {

            certificateSignedX509Certificate = CertificateGenerator.loadCertificateFromFile();
            JOptionPane.showMessageDialog(null, "Certificate loaded successfully");
            assert certificateSignedX509Certificate != null;
            txtClientSubject.setText(certificateSignedX509Certificate.getSubjectDN().getName().replaceFirst("DNQ=", ""));

//            certificateSignedX509Certificate = CertificateGenerator.loadCertificateFromFile();
        } );

        btnVerify.addActionListener(e -> {
            if (certificateSignedX509Certificate == null || selfSignedX509Certificate == null) {
                JOptionPane.showMessageDialog(null, "Please load certificates first");
            }
            else {
                CertificateGenerator.verifyCertificate(certificateSignedX509Certificate, selfSignedX509Certificate.getPublicKey());
            }

//            new FrmMain().setVisible(true);
//            dispose();
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
