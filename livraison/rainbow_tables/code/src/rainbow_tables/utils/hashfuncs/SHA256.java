package rainbow_tables.utils.hashfuncs;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 implements IHashFunction {

    /**
     * @param message chaine de caractere à hasher
     * @return le hash en SHA1
     */
    @Override
    public String hash(String message) {

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");

            byte[] messageDigest = md.digest(message.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
