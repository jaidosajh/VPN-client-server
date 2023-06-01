import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;

public class VPNClient {
    private static final String SERVER_ADDRESS = "127.0.0.1"; // Replace with your server address
    private static final int SERVER_PORT = 8888; // Replace with your server port

    public static void main(String[] args) {
        try {
            // Establish a connection to the VPN server
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

            // Generate and send the encryption key to the server
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(secretKey);

            // Receive the encrypted data from the server
            InputStream inputStream = socket.getInputStream();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
            BufferedReader reader = new BufferedReader(new InputStreamReader(cipherInputStream));
            String encryptedData = reader.readLine();

            // Decrypt and display the received data
            String decryptedData = decryptData(encryptedData, secretKey);
            System.out.println("Received data: " + decryptedData);

            // Close the connections
            reader.close();
            cipherInputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String decryptData(String encryptedData, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
