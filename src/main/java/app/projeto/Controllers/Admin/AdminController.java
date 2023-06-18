package app.projeto.Controllers.Admin;

import app.projeto.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    public BorderPane admin_parent;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().addListener(((observableValue, oldVal, newVal) -> {
            switch (newVal){
                case "Consultas" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getConsultasView());
                case "Pacientes" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getPacientesView());
                case "Perfil" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getPerfilView());
                default -> admin_parent.setCenter(Model.getInstance().getViewFactory().getFuncionariosView());
            }
        } ));
    }
}
