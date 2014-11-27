package Utils;

import android.util.Base64;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by gzc on 14-11-1.
 */
public class CryptoUtility {
    private static final String MAC_NAME = "HmacSHA1";

    public static byte[] hmacSHA1Encrypt(String encryptKey, String encryptText) throws InvalidKeyException, NoSuchAlgorithmException {
        Mac mac = Mac.getInstance(MAC_NAME);
        mac.init(new SecretKeySpec(encryptKey.getBytes(), MAC_NAME));
        return mac.doFinal(encryptText.getBytes());
    }

    public static String generate(String id, String ts, String nonce, String method, String host, String uri,
                                  String key) throws InvalidKeyException, NoSuchAlgorithmException {
        StringBuilder buffer = new StringBuilder();
        buffer.append(id).append(ts).append(nonce).append(method).append(host).append(uri);
        byte[] ciphertext = CryptoUtility.hmacSHA1Encrypt(key, buffer.toString());
        return Base64.encodeToString(ciphertext, Base64.NO_WRAP);
    }

    public static Header genAuthHeader(String url, String id, String key, String method) {
        long timeStamp= System.currentTimeMillis();
        String nonce = UUID.randomUUID().toString();
        String auth = "";
        try {
            URL urlParsed = new URL(url);
            Log.v("id", String.valueOf(id));
            Log.v("nonce", nonce);
            Log.v("ts", String.valueOf(timeStamp));
            Log.v("host", urlParsed.getHost());
            Log.v("filename", urlParsed.getFile());
            String mac = generate(id, String.valueOf(timeStamp), nonce, method, urlParsed.getHost(), urlParsed.getFile(), key);
            Log.v("mac", mac);
            //"Authorization: MAC id=\"18810513858\", nonce=\"77253d58-2c89-4fb3-9531-267c977d69c4\", ts=\"1414845397693\", mac=\"FqJoUL4dSzj7xLQxbjtkgBaljF8=\""

            StringBuilder sb = new StringBuilder();
            sb.append("MAC ")
                    .append("id=\"").append(id).append("\", ")
                    .append("nonce=\"").append(nonce).append("\", ")
                    .append("ts=\"").append(String.valueOf(timeStamp)).append("\", ")
                    .append("mac=\"").append(mac).append("\"");
            auth = sb.toString();
            Log.v("auth", auth);
        } catch (Exception e) {
            Log.e("Auth", e.getMessage());
        }
        if (auth.isEmpty()) {
            Log.e("Auth", "gen auth header failed");
        }
        return new BasicHeader("Authorization", auth);
    }
}
