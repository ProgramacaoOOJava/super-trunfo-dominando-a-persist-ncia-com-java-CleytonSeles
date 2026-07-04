package manager;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Centraliza a criacao do EntityManagerFactory da aplicacao Mestre.
 */
public final class JPAUtil {

    private static final String UNIDADE_PERSISTENCIA = "super-trunfo-mestre-pu";
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
        Persistence.createEntityManagerFactory(UNIDADE_PERSISTENCIA);

    private JPAUtil() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return ENTITY_MANAGER_FACTORY;
    }

    public static void close() {
        if (ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }
}

