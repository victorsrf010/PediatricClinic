package app.projeto;

import app.projeto.Entities.ConsultaEntity;

public class Model {

    private static Model model;
    private final ViewFactory viewFactory;
    private ConsultaEntity selectedConsulta;

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
}
