import javax.crypto.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Simpher {
    private final static int ENCRYPT_MODE = 0;
    private final static int DECRYPT_MODE = 1;
    private final static int FILEOUTPUT_MODE = 0;
    private final static int CONOUTPUT_MODE = 1;

    public static void main(String[] args) {
        System.out.println("Encrypt or decrypt?(print e or d)");
        Scanner s = new Scanner(System.in);
        if (s.nextLine().toLowerCase().startsWith("e")) {
            System.out.println("Do you want to encrypt a file or a string?(print f or s)");
            if (s.nextLine().toLowerCase().startsWith("f")) {
                System.out.println("Starting file encrypt sequence...");
                System.out.println("You can provide your path or use your homepath " + System.getProperty("user.home"));
                System.out.println("If you want to use your homepath make sure your file is in it and its called just \"toenc\", " +
                        "without any extension");
                System.out.println("So do you want to use homepath or your path?(print h or p)");
                if (s.nextLine().toLowerCase().startsWith("h")) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(System.getProperty("user.home") + "toenc");
                    } catch (FileNotFoundException e) {
                        System.out.println("File " + System.getProperty("user.home") + "toenc not found!");
                        System.exit(0);
                    }
                    byte[] b = new byte[0];
                    try {
                        b = new byte[fis.available()];
                    } catch (IOException e) {
                        ioerror();
                    }
                    try {
                        fis.read(b);
                    } catch (IOException e) {
                        ioerror();
                    }
                } else {

                }

            } else {
                System.out.println("Starting string encrypt sequence...");
                System.out.println("Enter a string you want to encrypt");
                byte[] input = s.nextLine().getBytes(StandardCharsets.UTF_8);
                System.out.println("Enter algorithm you want to encrypt with (DES,AES,RSA)");
                String algo = s.nextLine();
                System.out.println("Do you want encoded stuff to be printed here or saved in file? (print p or s)");
                String outputmode = s.nextLine();
                boolean mode;
                if (outputmode.toLowerCase().startsWith("p")) {
                    mode = true;
                    System.out.println("The result will be here, in console");
                } else {
                    mode = false;
                    System.out.println("The result will be stored in you home user location, " + System.getProperty("user.home"));
                }
                encode(input, ENCRYPT_MODE, mode ? CONOUTPUT_MODE : FILEOUTPUT_MODE, algo);
            }
        } else {

        }
    }

    private static void encode(byte[] toEncode, int mode, int res, String algo) {
        if (mode == ENCRYPT_MODE) {
            algo = algo.toUpperCase();
            Cipher c = null;
            try {
                c = Cipher.getInstance(algo);
                Key k = KeyGenerator.getInstance(algo).generateKey();
                c.init(Cipher.ENCRYPT_MODE, k);
                System.out.println("String view of your key, just for fun: " + new String(k.getEncoded(), StandardCharsets.UTF_8));
                if (!(res == CONOUTPUT_MODE)) {
                    new FileOutputStream(System.getProperty("user.home") + "/key").write(k.getEncoded());
                    System.out.println("Key stored at " + System.getProperty("user.home") + "/key");
                } else {
                    System.out.println("Actual key that you can use in this program to decode your data: ");
                    StringBuilder sb = new StringBuilder();
                    for (byte b : k.getEncoded()) {
                        sb.append(String.valueOf(b));
                        sb.append(',');
                    }
                    System.out.println(sb.toString());
                }
                byte[] result;
                result = c.doFinal(toEncode);
                if (!(res == CONOUTPUT_MODE)) {
                    new FileOutputStream(System.getProperty("user.home") + "/encfile").write(result);
                    System.out.println("Encrypted file stored at " + System.getProperty("user.home") + "/encfile");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : result) {
                        sb.append(String.valueOf(b));
                        sb.append(',');
                    }
                    System.out.println("Your encrypted data: ");
                    System.out.println(sb.toString());
                    System.out.println("String view(for fun): " + new String(result, StandardCharsets.UTF_8));
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Wrong algorithm!");
                System.exit(0);
            } catch (NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                standarterror();
            } catch (IOException e) {
                ioerror();
            }
        }
    }

    private static void ioerror() {
        System.out.println("Error reading/writing your file!");
        System.exit(0);
    }
    private static void standarterror() {
        System.out.println("Error!");
        System.exit(0);
    }
}