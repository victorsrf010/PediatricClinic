package app.projeto.Controllers.Admin.PopUp;

import app.projeto.Entities.FuncionarioEntity;
import app.projeto.JPAUtil;
import app.projeto.Model;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class removerFuncController implements Initializable {

    public Button adicionarBtn;
    public Button cancelarBtn;
    public Text nome;
    public Text cargo;
    private FuncionarioEntity currentFunc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adicionarBtn.setOnAction(event -> removeFuncionario());
        cancelarBtn.setOnAction(e -> {
            Stage stage = (Stage) adicionarBtn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });
    }

    public void initializeData(FuncionarioEntity funcionario) {
        currentFunc = funcionario;
        nome.setText(String.valueOf(funcionario.getNome()));

        int tipoId = funcionario.getTipoId();
        if (tipoId == 1) {
            cargo.setText("Médico");
        } else if (tipoId == 2) {
            cargo.setText("Rececionista");
        } else if (tipoId == 3) {
            cargo.setText("Admin");
        }

    }

    public void removeFuncionario() {
        EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            FuncionarioEntity managedFunc = entityManager.find(FuncionarioEntity.class, currentFunc.getId());
            entityManager.remove(managedFunc);
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
        alert.setContentText("Funcionário removido com sucesso");

        alert.showAndWait();
    }
}
