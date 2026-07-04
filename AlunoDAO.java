import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import java.util.List;

/**
 * DAO concreto para operacoes de persistencia de Aluno.
 */
public class AlunoDAO extends GenericDAO<Aluno, String> {

    public AlunoDAO(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    @Override
    public boolean incluir(Aluno entidade) {
        EntityManager entityManager = criarEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entidade);
            entityManager.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            rollback(entityManager);
            System.err.println("Erro ao incluir aluno: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean excluir(String chave) {
        EntityManager entityManager = criarEntityManager();
        try {
            Aluno aluno = entityManager.find(Aluno.class, chave);
            if (aluno == null) {
                return false;
            }

            entityManager.getTransaction().begin();
            entityManager.remove(aluno);
            entityManager.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            rollback(entityManager);
            System.err.println("Erro ao excluir aluno: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Aluno obter(String chave) {
        EntityManager entityManager = criarEntityManager();
        try {
            return entityManager.find(Aluno.class, chave);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Aluno> obterTodos() {
        EntityManager entityManager = criarEntityManager();
        try {
            return entityManager.createQuery(
                "SELECT a FROM Aluno a ORDER BY a.nome",
                Aluno.class
            ).getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean alterar(Aluno entidade) {
        EntityManager entityManager = criarEntityManager();
        try {
            Aluno existente = entityManager.find(Aluno.class, entidade.getMatricula());
            if (existente == null) {
                return false;
            }

            entityManager.getTransaction().begin();
            existente.setNome(entidade.getNome());
            existente.setAno(entidade.getAno());
            entityManager.merge(existente);
            entityManager.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            rollback(entityManager);
            System.err.println("Erro ao alterar aluno: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }

    private void rollback(EntityManager entityManager) {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }
}

