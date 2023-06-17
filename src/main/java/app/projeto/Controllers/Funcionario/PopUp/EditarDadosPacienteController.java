package app.projeto.Controllers.Funcionario.PopUp;

import app.projeto.Entities.UtenteEntity;
import app.projeto.JPAUtil;
import app.projeto.Model;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class EditarDadosPacienteController implements Initializable {
    public TextField nomeTxtfd;
    public TextField nifTxtfd;
    public TextField nomeRepLegalTxtfd;
    public Button adicionarBtn;
    public Button cancelarBtn;
    public TextField telTxtfd;
    public TextField emailTxtfd;
    public ChoiceBox<String> sexoChoicebox;
    public DatePicker dtNascimentoPicker;

    private UtenteEntity currentUtente;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        adicionarBtn.setOnAction(event -> updateUtenteInfo());

        cancelarBtn.setOnAction(e -> {
            Stage stage = (Stage) adicionarBtn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });

    }

    public void initializeData(UtenteEntity utente) {
        currentUtente = utente;
        nomeTxtfd.setText(String.valueOf(utente.getNome()));
        nifTxtfd.setText(String.valueOf(utente.getNif()));
        dtNascimentoPicker.setValue(utente.getDataNascimento().toLocalDate());
        nomeRepLegalTxtfd.setText(String.valueOf(utente.getNomeRepresentanteLegal()));
        telTxtfd.setText(String.valueOf(utente.getContactoRepresentanteLegal()));
        emailTxtfd.setText(String.valueOf(utente.getEmail()));
        sexoChoicebox.setValue(String.valueOf(utente.getSexo()));

    }

    public void updateUtenteInfo() {
        // Gather the new data from the fields
        String newNome = nomeTxtfd.getText();
        String newNif = nifTxtfd.getText();
        String newNomeRepLegal = nomeRepLegalTxtfd.getText();
        String newTel = telTxtfd.getText();
        String newEmail = emailTxtfd.getText();
        String newSexo = sexoChoicebox.getValue();
        Date newDtNascimento = Date.valueOf(dtNascimentoPicker.getValue());

        // Now update the UtenteEntity object
        currentUtente.setNome(newNome);
        currentUtente.setNif(newNif);
        currentUtente.setNomeRepresentanteLegal(newNomeRepLegal);
        currentUtente.setContactoRepresentanteLegal(newTel);
        currentUtente.setEmail(newEmail);
        currentUtente.setSexo(newSexo);
        currentUtente.setDataNascimento(newDtNascimento);

        // Update the entity in the database
        EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(currentUtente);
            entityManager.getTransaction().commit();

            // Here you can show a success message if you want
        } catch (Exception e) {
            // Handle the exception appropriately
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }

        Stage stage = (Stage) adicionarBtn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Iformação do utente editada com sucesso");

        alert.showAndWait();
    }
}
