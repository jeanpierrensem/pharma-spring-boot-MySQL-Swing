package com.officine.losto.security;

import lombok.*;
import org.springframework.stereotype.*;

import java.security.*;

@Setter
@Component
public class CryptographicMD5 implements ICryptographic {
    private byte[] uniqueKey;
    private byte[] hash;

    public String encode(String key, String algorithm) {
        try {
            setUniqueKey(key.getBytes());
            setHash(MessageDigest.getInstance(algorithm).digest(uniqueKey));
        } catch (NoSuchAlgorithmException e) {
            throw new Error("no support for cryptographic algorithm" + algorithm);
        }

        StringBuffer hashString = new StringBuffer();
        for (int i = 0; i < hash.length; ++i) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                hashString.append("0");
                hashString.append(hex.charAt(hex.length() - 1));
            } else {
                hashString.append(hex.substring(hex.length() - 2));
            }
        }
        return hashString.toString();
    }
}