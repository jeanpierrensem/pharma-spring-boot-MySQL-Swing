package com.officine.losto.validation;

/**
 * Bean Validation groups: {@link OnCreate} for POST bodies, {@link OnUpdate} for PUT bodies (partial updates allowed where annotations omit {@code groups}).
 */
public final class ValidationGroups {

    private ValidationGroups() {
    }

    public interface OnCreate {
    }

    public interface OnUpdate {
    }

    /**
     * Création d’un ticket de vente sans encaissement (périmètre « saisie vente »).
     */
    public interface OnCreateTicket {
    }
}
