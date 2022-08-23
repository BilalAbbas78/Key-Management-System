import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import javax.security.auth.x500.X500Principal;
import javax.swing.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

@SuppressWarnings("ALL")
public class CertificateGenerator {

    static PublicKey publicKey;

    public static X509Certificate generateSelfSignedX509Certificate(String issuerName, String issuerSubject) throws CertificateEncodingException, InvalidKeyException, IllegalStateException,
            NoSuchProviderException, NoSuchAlgorithmException, SignatureException {
        Security.addProvider(new BouncyCastleProvider());

        // generate a key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // build a certificate generator
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

        // add some options
        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setIssuerDN(new X500Principal("CN=" + issuerName)); // use the same
        certGen.setSubjectDN(new X509Name("DN=" + issuerSubject));
        // yesterday
        certGen.setNotBefore(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        // in 2 years
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 2L * 365 * 24 * 60 * 60 * 1000));
        certGen.setPublicKey(keyPair.getPublic());
        publicKey = keyPair.getPublic();
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
        certGen.addExtension(X509Extensions.ExtendedKeyUsage, true,
                new ExtendedKeyUsage(KeyPurposeId.id_kp_timeStamping));

        // finally, sign the certificate with the private key of the same KeyPair
        return certGen.generate(keyPair.getPrivate(), "BC");
    }

    public static void importCertificate(String certificateFilePath, String certificateFileName) throws FileNotFoundException, CertificateException, IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
//        X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(certificateFilePath + certificateFileName));
//        System.out.println(certificate);
        FileInputStream fileInputStream = new FileInputStream(certificateFilePath + certificateFileName);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
//        System.out.println(certificate);

    }

    public static void verifyCertificate (X509Certificate certificate, PublicKey publicKey){
        try {
            certificate.verify(publicKey);
            JOptionPane.showMessageDialog(null, "Certificate is valid");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Certificate is not valid", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }

    public static void exportCertificate(X509Certificate selfSignedX509Certificate, String filePath) throws CertificateEncodingException, IOException {
        final FileOutputStream os = new FileOutputStream(filePath +  ".cer");
        os.write("-----BEGIN CERTIFICATE-----\n".getBytes("US-ASCII"));
        os.write(Base64.encode(selfSignedX509Certificate.getEncoded()));
        os.write("-----END CERTIFICATE-----\n".getBytes("US-ASCII"));
        os.close();
    }

    public X509Certificate generateCertificateSignedX509Certificate(String clientName, String clientSubject, PrivateKey issuerPrivateKey) throws CertificateEncodingException, InvalidKeyException, IllegalStateException,
            NoSuchProviderException, NoSuchAlgorithmException, SignatureException {
        Security.addProvider(new BouncyCastleProvider());

        // generate a key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // build a certificate generator
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

        // add some options
        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setIssuerDN(new X500Principal(clientName)); // use the same
        certGen.setSubjectDN(new X509Name(clientSubject));
        // yesterday
        certGen.setNotBefore(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        // in 2 years
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 2L * 365 * 24 * 60 * 60 * 1000));
        certGen.setPublicKey(keyPair.getPublic());
        publicKey = keyPair.getPublic();
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
        certGen.addExtension(X509Extensions.ExtendedKeyUsage, true,
                new ExtendedKeyUsage(KeyPurposeId.id_kp_timeStamping));

        // finally, sign the certificate with the private key of the same KeyPair
        return certGen.generate(issuerPrivateKey, "BC");
    }

//    public void addBouncyCastleAsSecurityProvider() {
//        Security.addProvider(new BouncyCastleProvider());
//    }
}