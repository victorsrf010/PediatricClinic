package app.projeto;

import app.projeto.Entities.FuncionarioEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;



public class AuthenticationService {

    static EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
    static EntityManager entityManager = factory.createEntityManager();

    public static FuncionarioEntity currentUser;

    public static FuncionarioEntity findFuncionarioByIdAndPassword(int id, String password) {



        TypedQuery<FuncionarioEntity> query = entityManager.createQuery(
                "SELECT f FROM FuncionarioEntity f WHERE f.id = :id AND f.password = :password",
                FuncionarioEntity.class
        );
        query.setParameter("id", id);
        query.setParameter("password", password);

        try {
            FuncionarioEntity user = query.getSingleResult();
            currentUser = user;
            user.setEstado(true);
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();

            return user;
        } catch (NoResultException e) {
            return null; // User not found
        }
    }

    public static void logout() {
        FuncionarioEntity user = getCurrentUser();
        try {
            user.setEstado(false);
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        }finally {
            entityManager.close();
        }
    }

    public static FuncionarioEntity getCurrentUser() {
        return currentUser;
    }

    // Other methods related to user management, session handling, etc.
}