package app.projeto.Controllers.Admin.PopUp;

import app.projeto.Entities.FuncionarioEntity;
import app.projeto.Entities.UtenteEntity;
import app.projeto.JPAUtil;
import app.projeto.Model;
import jakarta.persistence.EntityManager;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class adicionarFuncController implements Initializable{

    public TextField nomeTxtfd;
    public TextField nifTxtfd;
    public TextField telTxtfd;
    public TextField emailTxtfd;
    public ChoiceBox<String> cargoChoicebox;
    public Button adicionarBtn;
    public Button cancelarBtn;
    public TextField ruaTxtfd;
    public TextField codPostalTxtfd;
    public TextField localidade;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargoChoicebox.getItems().addAll("Médico", "Rececionista", "Admin");
        cargoChoicebox.setValue("Médico");

        adicionarBtn.setOnAction(e -> {
            EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

            FuncionarioEntity newFunc = new FuncionarioEntity();
            newFunc.setNome(nomeTxtfd.getText());
            newFunc.setNif(nifTxtfd.getText());
            newFunc.setNumeroTelemovel(telTxtfd.getText());
            newFunc.setEmail(emailTxtfd.getText());
            newFunc.setRua(ruaTxtfd.getText());
            newFunc.setLocalidade(codPostalTxtfd.getText());
            newFunc.setLocalidade(localidade.getText());
            newFunc.setEstado(false);


            String newCargo = cargoChoicebox.getValue();
            int newTipoId;
            if ("Médico".equals(newCargo)) {
                newTipoId = 1;
            } else if ("Rececionista".equals(newCargo)) {
                newTipoId = 2;
            } else if ("Admin".equals(newCargo)) {
                newTipoId = 3;
            } else {
                newTipoId = 0; // default value, adjust as needed
            }
            newFunc.setTipoId(newTipoId);

            em.getTransaction().begin();
            em.persist(newFunc);
            em.getTransaction().commit();

            em.close();

            Stage stage = (Stage) adicionarBtn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText(null);
            alert.setContentText("Funcionário adicionado com sucesso");

            alert.showAndWait();
        });

        cancelarBtn.setOnAction(e -> {
            Stage stage = (Stage) adicionarBtn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });
    }
}
