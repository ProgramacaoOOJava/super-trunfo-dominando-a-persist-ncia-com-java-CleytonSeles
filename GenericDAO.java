import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Estrutura base para DAOs com tipo de entidade e chave parametrizados.
 */
public abstract class GenericDAO<E, K> {

    private final EntityManagerFactory entityManagerFactory;

    protected GenericDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    protected EntityManager criarEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public abstract boolean incluir(E entidade);

    public abstract boolean excluir(K chave);

    public abstract E obter(K chave);

    public abstract List<E> obterTodos();

    public abstract boolean alterar(E entidade);
}
