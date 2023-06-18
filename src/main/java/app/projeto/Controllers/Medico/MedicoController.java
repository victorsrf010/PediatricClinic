package app.projeto.Controllers.Medico;

import app.projeto.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MedicoController implements Initializable {

    public BorderPane medico_parent;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getMedicoSelectedMenuItem().addListener(((observableValue, oldVal, newVal) -> {
            switch (newVal){
                case "Horario" -> medico_parent.setCenter(Model.getInstance().getViewFactory().getHorarioView());
                case "Pacientes" -> medico_parent.setCenter(Model.getInstance().getViewFactory().getMeusPacientesView());
                case "Perfil" -> medico_parent.setCenter(Model.getInstance().getViewFactory().getPerfilView());
                default -> medico_parent.setCenter(Model.getInstance().getViewFactory().getConsultaView());
            }
        } ));
    }
}
