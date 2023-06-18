package app.projeto.Controllers.Admin.PopUp;

import app.projeto.Entities.FuncionarioEntity;
import app.projeto.Entities.UtenteEntity;
import app.projeto.JPAUtil;
import app.projeto.Model;
import com.sun.javafx.css.StyleManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class editarFuncController implements Initializable {

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
    private FuncionarioEntity currentFunc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adicionarBtn.setOnAction(event -> updateFuncionarioInfo());
        cancelarBtn.setOnAction(e -> {
            Stage stage = (Stage) adicionarBtn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });
    }

    public void initializeData(FuncionarioEntity funcionario) {

        currentFunc = funcionario;
        nomeTxtfd.setText(String.valueOf(funcionario.getNome()));
        nifTxtfd.setText(String.valueOf(funcionario.getNif()));
        telTxtfd.setText(String.valueOf(funcionario.getNumeroTelemovel()));
        emailTxtfd.setText(String.valueOf(funcionario.getEmail()));
        ruaTxtfd.setText(funcionario.getRua());
        codPostalTxtfd.setText(funcionario.getCodigoPostal());
        localidade.setText(funcionario.getLocalidade());

        ObservableList<String> cargoOptions = FXCollections.observableArrayList("Médico", "Rececionista", "Admin");

        cargoChoicebox.setItems(cargoOptions);

        int tipoId = funcionario.getTipoId();
        if (tipoId == 1) {
            cargoChoicebox.setValue("Médico");
        } else if (tipoId == 2) {
            cargoChoicebox.setValue("Rececionista");
        } else if (tipoId == 3) {
            cargoChoicebox.setValue("Admin");
        } else {
            cargoChoicebox.setValue("Unknown");
        }

    }

    public void updateFuncionarioInfo() {
        // Gather the new data from the fields
        String newNome = nomeTxtfd.getText();
        String newNif = nifTxtfd.getText();
        String newTel = telTxtfd.getText();
        String newEmail = emailTxtfd.getText();
        String newRua = ruaTxtfd.getText();
        String newCodPostal = codPostalTxtfd.getText();
        String newCargo = cargoChoicebox.getValue();
        String newLocalidade = localidade.getText();

        // Map the cargo to its respective IDTipo
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

        // Now update the FuncionarioEntity object
        currentFunc.setNome(newNome);
        currentFunc.setNif(newNif);
        currentFunc.setNumeroTelemovel(newTel);
        currentFunc.setEmail(newEmail);
        currentFunc.setRua(newRua);
        currentFunc.setLocalidade(newLocalidade);
        currentFunc.setTipoId(newTipoId);
        currentFunc.setCodigoPostal(newCodPostal);

        // Update the entity in the database
        EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(currentFunc);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }

        Stage stage = (Stage) adicionarBtn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Informação do funcionário editada com sucesso");

        alert.showAndWait();
    }

}
