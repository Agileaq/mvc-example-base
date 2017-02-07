package personal.arc.mvc.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;

import static personal.arc.mvc.jwt.Base64Util.*;

/**
 * Created by Arc on 28/12/2016.
 */
public class RSAPublicHelper extends RSABase {

    private static final Logger logger = LoggerFactory.getLogger(RSAPublicHelper.class);

    private PublicKey pubKey;
    private Signature pubSignature;

    public RSAPublicHelper(String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            pubSignature = Signature.getInstance(SIGNATURE_ALGORITHM);
            pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(base64UrlDecode(publicKey)));
            pubSignature.initVerify(pubKey);
        } catch (Exception e) {
            logger.error("RSAPublicHelper initial failed!!! jwt related function will be unavailable.", e);
        }
    }

    public boolean check(String content, String sign) throws UnsupportedEncodingException, SignatureException {
        pubSignature.update(content.getBytes(CHARSET_NAME));
        boolean bverify = pubSignature.verify(base64UrlDecode(sign));
        return bverify;
    }
}
