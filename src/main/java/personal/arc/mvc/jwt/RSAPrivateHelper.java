package personal.arc.mvc.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;

import static personal.arc.mvc.jwt.Base64Util.*;

/**
 * Created by Arc on 28/12/2016.
 */
public class RSAPrivateHelper extends RSABase {

    private static final Logger logger = LoggerFactory.getLogger(RSAPrivateHelper.class);

    private PrivateKey priKey;
    private Signature priSignature;

    public RSAPrivateHelper(String privateKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            priSignature = Signature.getInstance(SIGNATURE_ALGORITHM);
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(base64UrlDecode(privateKey));
            priKey = keyFactory.generatePrivate(priPKCS8);
            priSignature.initSign(priKey);
        } catch (Exception e) {
            logger.error("RSAPrivateHelper initial failed!!! jwt related function will be unavailable.", e);
        }
    }

    public String sign(String content) throws UnsupportedEncodingException, SignatureException {
        priSignature.update(content.getBytes(CHARSET_NAME));
        byte[] signed = priSignature.sign();
        return base64UrlEncode(signed);
    }
}
