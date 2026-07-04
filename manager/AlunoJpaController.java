package manager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import model.Aluno;

/**
 * Controller JPA responsável pelas operações de persistência do aluno.
 */
public class AlunoJpaController {

    private final EntityManagerFactory entityManagerFactory;

    public AlunoJpaController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Persiste um novo aluno no banco.
     */
    public void create(Aluno aluno) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(aluno);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Lista todas as cartas do baralho.
     */
    public List<Aluno> findAlunoEntities() {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.createNamedQuery("Aluno.findAll", Aluno.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Lista apenas as cartas lendárias.
     */
    public List<Aluno> findLegendaryAlunoEntities() {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.createNamedQuery("Aluno.findLegendary", Aluno.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Busca uma carta pela matrícula.
     */
    public Aluno findAluno(String matricula) {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.find(Aluno.class, matricula);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Remove uma carta pela chave primária.
     */
    public void destroy(String matricula) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            Aluno aluno = entityManager.find(Aluno.class, matricula);
            if (aluno == null) {
                throw new IllegalArgumentException("Aluno nao encontrado com a matricula informada.");
            }
            entityManager.remove(aluno);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
