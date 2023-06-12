package app.projeto;

import app.projeto.Entities.UtenteEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) {

        Model.getInstance().getViewFactory().showLoginWindow();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();

        List<UtenteEntity> entities = em.createQuery("FROM UtenteEntity ", UtenteEntity.class).getResultList();
        for (UtenteEntity entity : entities) {
            System.out.println(entity);
        }

        em.close();
        emf.close();
    }

    public static void main(String[] args) {
        launch();
    }
}