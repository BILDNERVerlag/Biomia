package de.biomiaAPI.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class Base64 {

    private static char[] map1 = new char[64];
    private static byte[] map2;

    static {
        int i = 0;

        char c;
        for(c = 'A'; c <= 'Z'; map1[i++] = c++) {
            ;
        }

        for(c = 'a'; c <= 'z'; map1[i++] = c++) {
            ;
        }

        for(c = '0'; c <= '9'; map1[i++] = c++) {
            ;
        }

        map1[i++] = '+';
        map1[i++] = '/';
        map2 = new byte[128];

        for(i = 0; i < map2.length; ++i) {
            map2[i] = -1;
        }

        for(i = 0; i < 64; ++i) {
            map2[map1[i]] = (byte)i;
        }

    }

    public static String toBase64(Object o) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(o);
            dataOutput.close();

            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    public static Object fromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            Object o = dataInput.readObject();
            dataInput.close();
            return o;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decodeLines(String s) {
        char[] buf = new char[s.length()];
        int p = 0;

        for(int ip = 0; ip < s.length(); ++ip) {
            char c = s.charAt(ip);
            if (c != ' ' && c != '\r' && c != '\n' && c != '\t') {
                buf[p++] = c;
            }
        }

        return decode(buf, 0, p);
    }

    public static byte[] decode(char[] in, int iOff, int iLen) {
        if (iLen % 4 != 0) {
            throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
        } else {
            while(iLen > 0 && in[iOff + iLen - 1] == '=') {
                --iLen;
            }

            int oLen = iLen * 3 / 4;
            byte[] out = new byte[oLen];
            int ip = iOff;
            int iEnd = iOff + iLen;
            int op = 0;

            while(ip < iEnd) {
                int i0 = in[ip++];
                int i1 = in[ip++];
                int i2 = ip < iEnd ? in[ip++] : 65;
                int i3 = ip < iEnd ? in[ip++] : 65;
                if (i0 <= 127 && i1 <= 127 && i2 <= 127 && i3 <= 127) {
                    int b0 = map2[i0];
                    int b1 = map2[i1];
                    int b2 = map2[i2];
                    int b3 = map2[i3];
                    if (b0 >= 0 && b1 >= 0 && b2 >= 0 && b3 >= 0) {
                        int o0 = b0 << 2 | b1 >>> 4;
                        int o1 = (b1 & 15) << 4 | b2 >>> 2;
                        int o2 = (b2 & 3) << 6 | b3;
                        out[op++] = (byte)o0;
                        if (op < oLen) {
                            out[op++] = (byte)o1;
                        }

                        if (op < oLen) {
                            out[op++] = (byte)o2;
                        }
                        continue;
                    }

                    throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
                }

                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            }

            return out;
        }
    }

}
