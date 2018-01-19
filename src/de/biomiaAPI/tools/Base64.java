package de.biomiaAPI.tools;

import java.io.UnsupportedEncodingException;

public class Base64 {

    public static String toBase64(Object o) {
        String ed = o.toString();
        byte[] encodedBytes = com.sun.xml.internal.messaging.saaj.util.Base64.encode(ed.getBytes());
        String str = null;

        try {
            str = new String(encodedBytes, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
        }
        return str;
    }

    public static Object fromBase64(String data) {
        return com.sun.xml.internal.messaging.saaj.util.Base64.base64Decode(data);
    }
}
