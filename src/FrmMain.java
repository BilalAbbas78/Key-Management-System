import javax.swing.*;

public class FrmMain extends JFrame {
    FrmMain(){
        setTitle("Key Management System (KMS)");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        JButton btnGenerateCertificate = new JButton("Generate Certificate");
        btnGenerateCertificate.setBounds(100, 30, 180, 30);
        add(btnGenerateCertificate);

        JButton btnVerifyCertificate = new JButton("Verify Certificate");
        btnVerifyCertificate.setBounds(100, 70, 180, 30);
        add(btnVerifyCertificate);

        JButton btnExit = new JButton("Exit");
        btnExit.setBounds(100, 110, 180, 30);
        add(btnExit);

        btnGenerateCertificate.addActionListener(e -> {
            new FrmGenerateCertificate().setVisible(true);
            setVisible(false);
        });

        btnVerifyCertificate.addActionListener(e -> {
            new FrmVerifyCertificate().setVisible(true);
            setVisible(false);
        });

        btnExit.addActionListener(e -> System.exit(0));




    }

    public static void main(String[] args) {
        new FrmMain().setVisible(true);
    }
}
