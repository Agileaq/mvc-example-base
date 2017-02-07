package personal.arc.mvc.jwt;

import com.google.common.io.BaseEncoding;

/**
 * Created by Arc on 26/12/2016.
 */
public class Base64Util {

    /**
     * base64 使用url版本, 避免在web中传输被浏览器转码
     * @param str
     * @return
     */
    public static byte[] base64UrlDecode(String str) {
        return BaseEncoding.base64Url().decode(str);
    }

    /**
     * base64 使用url版本, 避免在web中传输被浏览器转码
     * @param bytes
     * @return
     */
    public static String base64UrlEncode(byte[] bytes) {
        return BaseEncoding.base64Url().encode(bytes);
    }

}
