package com.officine.losto.security;

@FunctionalInterface
public interface ICryptographic {
    String encode(String key, String algorithm);
}
