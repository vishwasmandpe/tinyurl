package com.example.tinyurl.util;


/**
 * Our own implementation of Base62 Encode/Decode since not out of the box provided by Java
 */
public class Base62Encoder {
    private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // Encodes a numeric ID to Base62 string
    public static String encode(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int rem = (int) (num % 62);
            sb.append(CHARSET.charAt(rem));
            num = num / 62;
        }
        return sb.reverse().toString();
    }

    // Decodes a Base62 string to numeric ID (for completeness, not strictly needed)
    public static long decode(String str) {
        long num = 0;
        for (int i = 0; i < str.length(); i++) {
            num = num * 62 + CHARSET.indexOf(str.charAt(i));
        }
        return num;
    }
}
