package app.projeto.Controllers.Funcionario;

import app.projeto.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class FuncionarioController implements Initializable{

    public BorderPane funcionario_parent;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getFuncionarioSelectedMenuItem().addListener(((observableValue, oldVal, newVal) -> {
            switch (newVal){
                case "Consultas" -> funcionario_parent.setCenter(Model.getInstance().getViewFactory().getConsultasView());
                case "Pacientes" -> funcionario_parent.setCenter(Model.getInstance().getViewFactory().getPacientesView());
                case "Perfil" -> funcionario_parent.setCenter(Model.getInstance().getViewFactory().getPerfilView());
                default -> funcionario_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
            }
        } ));
    }
}
