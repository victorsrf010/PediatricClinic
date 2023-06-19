package app.projeto.Controllers;

import app.projeto.AuthenticationService;
import app.projeto.Entities.FuncionarioEntity;
import app.projeto.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class PerfilController implements Initializable {
    public Text nome;
    public Text id;
    public Text cargo;
    public Text ntelemovel;
    public Text email;
    public Text nif;
    public Text morada;
    public Text localidade;
    public TextField passShown;
    public PasswordField passHidden;
    public CheckBox passToggle;
    public TextField newPassShown;
    public PasswordField newPassHidden;
    public CheckBox newPassToggle;
    public PasswordField confirmNewPassHidden;
    public Button changePasswordBtn;
    public Text matchError;
    public TextField newEmail;
    public Button changeEmailBtn;
    public Text matchErrorEmail;
    public TextField newEmailConfirmation;
    public TextField newRua;
    public Button changeMoradaBtn;
    public Text infoMessageMorada;
    public TextField newCodPostal;
    public TextField newLocalidade;
    public TextField newTel;
    public Button changeTelBtn;
    public Text infoMessageTel;


    FuncionarioEntity user = AuthenticationService.getCurrentUser();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nome.setText(user.getNome());
        id.setText(String.valueOf(user.getId()));
        checkCargo();
        ntelemovel.setText(user.getNumeroTelemovel());
        email.setText(user.getEmail());
        nif.setText(user.getNif());
        morada.setText(user.getRua());
        localidade.setText(user.getLocalidade());
        togglePassword(null);
        toggleNewPassword(null);
        passToggle.setOnAction(this::togglePassword);
        newPassToggle.setOnAction(this::toggleNewPassword);
        changePasswordBtn.setOnAction(event -> changePassword());
        changeEmailBtn.setOnAction(event -> changeEmail());
        changeMoradaBtn.setOnAction(event -> changeRua());
        changeTelBtn.setOnAction(event -> changeNtelemovel());
    }

    public void togglePassword(ActionEvent event) {
        boolean isPasswordVisible = passToggle.isSelected();

        if (isPasswordVisible) {
            passShown.setText(AuthenticationService.getCurrentUser().getPassword());
            passShown.setVisible(true);
            passHidden.setVisible(false);
        } else {
            passHidden.setText(AuthenticationService.getCurrentUser().getPassword());
            passShown.setVisible(false);
            passHidden.setVisible(true);
        }
    }

    public void toggleNewPassword(ActionEvent event) {
        boolean isNewPasswordVisible = newPassToggle.isSelected();

        if (isNewPasswordVisible) {
            newPassShown.setText(newPassHidden.getText());
            newPassShown.setVisible(true);
            newPassHidden.setVisible(false);
        } else {
            if (newPassShown.getText().isEmpty()) {
                newPassHidden.setText("");
            }else {
                newPassHidden.setText(newPassShown.getText());
                newPassShown.setVisible(false);
                newPassHidden.setVisible(true);
            }
        }
    }

    public void changePassword() {
        String newPassword = newPassHidden.getText();
        String confirmNewPassword = confirmNewPassHidden.getText();

        if (!newPassword.equals(confirmNewPassword)) {
            matchError.setText("Passwords não coincidem");
            return;
        }

        FuncionarioEntity currentUser = AuthenticationService.getCurrentUser();
        currentUser.setPassword(newPassword);

        EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(currentUser);
            entityManager.getTransaction().commit();
            passHidden.setText(newPassword);
            matchError.setText("Password alterada com sucesso");
        } catch (Exception e) {
            matchError.setText("Erro na alteração da password");
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }

    public void changeEmail() {
        String newMail = newEmail.getText();
        String confirmNewMail = newEmailConfirmation.getText();

        if (!newMail.equals(confirmNewMail)) {
            matchErrorEmail.setText("Emails não coincidem");
            return;
        }

        FuncionarioEntity currentUser = AuthenticationService.getCurrentUser();
        currentUser.setEmail(newMail);

        EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(currentUser);
            entityManager.getTransaction().commit();
            matchErrorEmail.setText("Email alterado com sucesso");
            email.setText(newMail);
        } catch (Exception e) {
            matchErrorEmail.setText("Erro na alteração do email");
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }

    public void changeRua() {
        String rua = newRua.getText();
        String localidadeK = newLocalidade.getText();
        String codPostalK = newCodPostal.getText();


        if (codPostalK == null || codPostalK.trim().isEmpty()) {
            infoMessageMorada.setText("O campo Código Postal não pode estar vazio");
            return;
        }

        FuncionarioEntity currentUser = AuthenticationService.getCurrentUser();
        currentUser.setRua(rua);
        currentUser.setLocalidade(localidadeK);
        currentUser.setCodigoPostal(codPostalK);

        EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();


            entityManager.merge(currentUser);
            entityManager.getTransaction().commit();

            morada.setText(rua);
            localidade.setText(localidadeK);
            infoMessageMorada.setText("Morada atualizada com sucesso");
        } catch (Exception e) {
            infoMessageMorada.setText("Erro na alteração da morada");
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }

    public void changeNtelemovel() {
        String newtel = newTel.getText();

        FuncionarioEntity currentUser = AuthenticationService.getCurrentUser();
        currentUser.setNumeroTelemovel(newtel);

        EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(currentUser);
            entityManager.getTransaction().commit();
            infoMessageTel.setText("Número alterado com sucesso");
            ntelemovel.setText(newtel);
        } catch (Exception e) {
            infoMessageTel.setText("Erro na alteração do número");
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }

    public void checkCargo() {
        if (user.getTipoId() == 3) {
            cargo.setText("Admin");
        } else if (user.getTipoId() == 2) {
            cargo.setText("Rececionista");
        } else if (user.getTipoId() == 1) {
            cargo.setText("Médico");
        }
    }
}
