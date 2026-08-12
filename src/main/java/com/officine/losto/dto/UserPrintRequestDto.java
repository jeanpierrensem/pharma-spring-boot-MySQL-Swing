package com.officine.losto.dto;

import lombok.*;

/**
 * Payload for generating a printable user sheet (PDF). Photo is optional (Base64, with or without data-URL prefix).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPrintRequestDto {
    private Long userId;
    private String name;
    private String login;
    private String phoneNumber;
    private String email;
    /**
     * Single line (e.g. group name or comma-separated).
     */
    private String groupLabel;
    /**
     * Masked for display; never send raw password from the client.
     */
    private String passwordMasked;
    /**
     * Raw Base64 or full {@code data:image/...;base64,...} string.
     */
    private String photoBase64;
}
