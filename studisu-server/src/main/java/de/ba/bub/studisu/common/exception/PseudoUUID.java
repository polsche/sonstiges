package de.ba.bub.studisu.common.exception;

import java.security.SecureRandom;

/**
 * pseudo unique id using own allowed set of chars and securerandom
 * 
 * TODO check if used by spring, otherwise remove it from our code CKU
 */
public class PseudoUUID {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * private constructor - DONT INSTANTIATE ME
     */
    private PseudoUUID() {
    }

    static String create(int size) {
        final StringBuilder uuid = new StringBuilder();

        for (int i = 0; i < size; i++) {
            uuid.append(CHARS.charAt(SECURE_RANDOM.nextInt(CHARS.length())));
        }

        return uuid.toString();
    }

}