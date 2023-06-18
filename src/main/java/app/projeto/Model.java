package app.projeto;

import app.projeto.Entities.ConsultaEntity;
import app.projeto.Entities.DiagnosticoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Model {

    private static Model model;
    private final ViewFactory viewFactory;
    private ConsultaEntity selectedConsulta;
    private DiagnosticoEntity selectedDiag;

    private Model() {
        this.viewFactory = new ViewFactory();
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public ConsultaEntity getSelectedConsulta() {
        return selectedConsulta;
    }

    public void setSelectedConsulta(ConsultaEntity selectedConsulta) {
        this.selectedConsulta = selectedConsulta;
    }

    public DiagnosticoEntity getSelectedDiag() {
        return selectedDiag;
    }

    public void setSelectedDiag(DiagnosticoEntity selectedDiag) {
        this.selectedDiag = selectedDiag;
    }

    public DiagnosticoEntity getDiagnosticoById(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();

        DiagnosticoEntity diagnostico = null;
        try {
            diagnostico = em.find(DiagnosticoEntity.class, id);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }

        return diagnostico;
    }

}
