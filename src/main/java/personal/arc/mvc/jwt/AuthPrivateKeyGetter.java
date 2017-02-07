package personal.arc.mvc.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created by Arc on 28/12/2016.
 */
public class AuthPrivateKeyGetter extends RSABase {

    private static final Logger logger = LoggerFactory.getLogger(AuthPrivateKeyGetter.class);
    private RSAPrivateKey rsaPrivateKey;

    public AuthPrivateKeyGetter(String base64UrlEncodedPrivateKey) {
        try {
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64Util.base64UrlDecode(base64UrlEncodedPrivateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            rsaPrivateKey = (RSAPrivateKey)keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            logger.error("AuthPrivateKeyGetter initial failed!!! jwt related function will be unavailable.", e);
        }
    }

    public RSAPrivateKey getRsaPrivateKey() {
        return rsaPrivateKey;
    }

}
