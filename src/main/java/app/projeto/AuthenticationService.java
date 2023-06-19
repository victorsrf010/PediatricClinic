package app.projeto;

import app.projeto.Entities.FuncionarioEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class AuthenticationService {
    static EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
    public static FuncionarioEntity currentUser;

    public static FuncionarioEntity findFuncionarioByIdAndPassword(int id, String password) {
        EntityManager entityManager = factory.createEntityManager();

        TypedQuery<FuncionarioEntity> query = entityManager.createQuery(
                "SELECT f FROM FuncionarioEntity f WHERE f.id = :id AND f.password = :password",
                FuncionarioEntity.class
        );
        query.setParameter("id", id);
        query.setParameter("password", password);

        try {
            entityManager.getTransaction().begin();
            FuncionarioEntity user = query.getSingleResult();
            currentUser = user;
            user.setEstado(true);
            entityManager.merge(user);
            entityManager.getTransaction().commit();
            return user;
        } catch (NoResultException e) {
            return null;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    public static void logout() {
        EntityManager entityManager = factory.createEntityManager();

        FuncionarioEntity user = getCurrentUser();
        try {
            entityManager.getTransaction().begin();
            user.setEstado(false);
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    public static FuncionarioEntity getCurrentUser() {
        return currentUser;
    }
}
