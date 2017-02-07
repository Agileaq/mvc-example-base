package personal.arc.mvc.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by Arc on 28/12/2016.
 */
public class AuthPublicKeyGetter extends RSABase {

    private static final Logger logger = LoggerFactory.getLogger(AuthPublicKeyGetter.class);
    private RSAPublicKey rsaPublicKey;

    public AuthPublicKeyGetter(String base64UrlEncodedPublicKey) {
        try {
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64Util.base64UrlDecode(base64UrlEncodedPublicKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            rsaPublicKey = (RSAPublicKey)keyFactory.generatePublic(pubKeySpec);
        } catch (Exception e) {
            logger.error("AuthPublicKeyGetter initial failed!!! jwt related function will be unavailable.", e);
        }
    }

    public RSAPublicKey getRsaPublicKey() {
        return rsaPublicKey;
    }

}
