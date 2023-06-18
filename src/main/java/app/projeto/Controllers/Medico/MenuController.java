package app.projeto.Controllers.Medico;

import app.projeto.AuthenticationService;
import app.projeto.Model;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    public Button pacientesButton;
    public Button perfilButton;
    public Button desconectarButton;
    public Text time;
    public Button consultaButton;
    public Button horariosButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addListeneres();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String currentTime = LocalTime.now().format(formatter);
            time.setText(currentTime);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();



        desconectarButton.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Desconectar");
            alert.setHeaderText("Pretende sair do sistema?");

            ButtonType buttonTypeOne = new ButtonType("Desconectar");
            ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne){
                onClose();
            }

        });
    }

    private void onClose() {
        Stage stage = (Stage) time.getScene().getWindow();
        AuthenticationService.logout();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }


    private void addListeneres() {
        consultaButton.setOnAction(actionEvent -> onConsulta());
        pacientesButton.setOnAction(actionEvent -> onPacientes());
        horariosButton.setOnAction(actionEvent -> onHorario());
        perfilButton.setOnAction(actionEvent -> onPerfil());
    }

    private void onConsulta() {
        Model.getInstance().getViewFactory().getMedicoSelectedMenuItem().set("Consulta");
    }

    private void onPacientes() {
        Model.getInstance().getViewFactory().getMedicoSelectedMenuItem().set("Pacientes");
        Model.getInstance().getViewFactory().getPacientesController().refreshTable();
    }

    private void onHorario() {
        Model.getInstance().getViewFactory().getMedicoSelectedMenuItem().set("Horario");
        Model.getInstance().getViewFactory().getConsultasController().refreshTable();

    }

    private void onPerfil() {
        Model.getInstance().getViewFactory().getMedicoSelectedMenuItem().set("Perfil");
    }

}
