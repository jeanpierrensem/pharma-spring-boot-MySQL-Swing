package com.officine.losto.security.cryptographic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cryptographic {
	public static String encode(String key, String algorithm) {
		byte[] uniqueKey = key.getBytes();
		byte[] hash = null;

		try {

			hash = MessageDigest.getInstance(algorithm).digest(uniqueKey);
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

	public static boolean isPasswordCorrect(String input, String correctPassword) {

		if (input.length() != correctPassword.length()) return false;
			int i = 0;
			while ( i < input.length() 
					 && input.charAt(i) == correctPassword.charAt(i)) i++;		
			if (i < input.length())return false;
			return true;

		
	}

}
