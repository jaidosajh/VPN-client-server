import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;

public class VPNServer {
    private static final int SERVER_PORT = 8888; // Replace with your desired server port

    public static void main(String[] args) {
        try {
            // Start the VPN server and wait for client connections
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("VPN Server started on port " + SERVER_PORT);

            while (true) {
                // Accept client connection
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());

                // Receive the encryption key from the client
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                SecretKey secretKey = (SecretKey) inputStream.readObject();

                // Encrypt and send data to the client
                String data = "This is a sample message from the server";
                String encryptedData = encryptData(data, secretKey);
                OutputStream outputStream = socket.getOutputStream();
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
                PrintWriter writer = new PrintWriter(cipherOutputStream, true);
                writer.println(encryptedData);

                // Close the connections
                writer.close();
                cipherOutputStream
