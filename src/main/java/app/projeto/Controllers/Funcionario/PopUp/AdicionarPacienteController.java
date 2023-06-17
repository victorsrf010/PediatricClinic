package app.projeto.Controllers.Funcionario.PopUp;

import app.projeto.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import jakarta.persistence.EntityManager;
import app.projeto.JPAUtil;
import app.projeto.Entities.UtenteEntity;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


import java.sql.Date;


import java.net.URL;
import java.util.ResourceBundle;

public class AdicionarPacienteController implements Initializable {
    public TextField nomeTxtfd;
    public TextField nifTxtfd;
    public TextField nomeRepLegalTxtfd;
    public Button adicionarBtn;
    public Button cancelarBtn;
    public TextField emailTxtfd;
    public ChoiceBox<String> sexoChoicebox;
    public DatePicker dtNascimentoPicker;
    public TextField telReplegalTxtfd;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sexoChoicebox.getItems().addAll("M", "F");
        sexoChoicebox.setValue("M");

        adicionarBtn.setOnAction(e -> {
            EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

            UtenteEntity newUtente = new UtenteEntity();
            newUtente.setNome(nomeTxtfd.getText());
            newUtente.setNif(nifTxtfd.getText());
            newUtente.setNomeRepresentanteLegal(nomeRepLegalTxtfd.getText());
            newUtente.setEmail(emailTxtfd.getText());
            newUtente.setSexo(sexoChoicebox.getValue());
            newUtente.setContactoRepresentanteLegal(telReplegalTxtfd.getText());

            Date sqlDate = Date.valueOf(dtNascimentoPicker.getValue());
            newUtente.setDataNascimento(sqlDate);

            em.getTransaction().begin();
            em.persist(newUtente);
            em.getTransaction().commit();

            em.close();

            Model.getInstance().getViewFactory().getPacientesController().refreshTable();

            Stage stage = (Stage) adicionarBtn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText(null);
            alert.setContentText("Utente adicionado com sucesso");

            alert.showAndWait();
        });

        cancelarBtn.setOnAction(e -> {
            Stage stage = (Stage) adicionarBtn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });

    }


}
