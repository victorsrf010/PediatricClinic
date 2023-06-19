package app.projeto.Controllers.Admin;

import app.projeto.Entities.FuncionarioEntity;
import app.projeto.Model;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class FuncionariosController implements Initializable {
    public TableView<FuncionarioEntity> funcionario;
    public TableColumn<FuncionarioEntity, String> nomeClm;
    public TableColumn<FuncionarioEntity, String> nifClm;
    public TableColumn<FuncionarioEntity, Integer> cargoClm;
    public TableColumn<FuncionarioEntity, String> contatoClm;
    public TableColumn<FuncionarioEntity, String> emailClm;
    public Button adicionarPacBtn;
    public Button editarBtn;
    public TextField pesquisarTxtfd;
    public Button removerBtn;
    public Text nome;
    public Text cargo;
    public Text ntelemovel;
    public Text email;
    public Text nif;
    public Text morada;
    public Text localidade;
    public Text id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearFuncionarioDetails();
        refreshTable();

        nomeClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        nifClm.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNif()));
        cargoClm.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTipoId()));
        contatoClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumeroTelemovel()));
        emailClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        editarBtn.setOnAction(event -> Model.getInstance().getViewFactory().showEditarFuncionario(funcionario.getSelectionModel().getSelectedItem()));

        pesquisarTxtfd.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchTerm = newValue.trim();

            if (searchTerm.isEmpty()) {
                funcionario.getSelectionModel().clearSelection();
                clearFuncionarioDetails();
                return;
            }

            funcionario.getItems().clear();
            funcionario.getItems().addAll(filterFuncionarios(searchTerm));
        });

        funcionario.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFuncDetails(newValue);
            }
        });

        adicionarPacBtn.setOnAction(event -> Model.getInstance().getViewFactory().showAdicionarFuncionario());
        editarBtn.setOnAction(event -> Model.getInstance().getViewFactory().showEditarFuncionario(funcionario.getSelectionModel().getSelectedItem()));
        removerBtn.setOnAction(event -> Model.getInstance().getViewFactory().showRemoverFunc(funcionario.getSelectionModel().getSelectedItem()));

    }

    public void refreshTable() {
        funcionario.getItems().clear();

        List<FuncionarioEntity> allFuncs = loadFuncionariosData();
        funcionario.getItems().addAll(allFuncs);
    }

    private List<FuncionarioEntity> filterFuncionarios(String searchTerm) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            List<FuncionarioEntity> utentes = em.createQuery("SELECT u FROM FuncionarioEntity u WHERE u.nome LIKE :searchTerm ORDER BY u.nome", FuncionarioEntity.class)
                    .setParameter("searchTerm", "%" + searchTerm + "%")
                    .getResultList();
            em.getTransaction().commit();

            return utentes;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return Collections.emptyList();
        } finally {
            em.close();
            emf.close();
        }
    }

    private List<FuncionarioEntity> loadFuncionariosData() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            List<FuncionarioEntity> utentes = em.createQuery("SELECT u FROM FuncionarioEntity u ORDER BY nome", FuncionarioEntity.class)
                    .getResultList();
            em.getTransaction().commit();

            return utentes;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return Collections.emptyList();
        } finally {
            em.close();
            emf.close();
        }
    }

    private void updateFuncDetails(FuncionarioEntity funcionario) {
        nome.setText(funcionario.getNome());
        int tipoId = funcionario.getTipoId();
        if (tipoId == 1) {
            cargo.setText("Médico");
        } else if (tipoId == 2) {
            cargo.setText("Rececionista");
        } else if (tipoId == 3) {
            cargo.setText("Admin");
        } else {
            cargo.setText("Unknown");
        }
        nif.setText(funcionario.getNif());
        ntelemovel.setText(funcionario.getNumeroTelemovel());
        email.setText(funcionario.getEmail());
        morada.setText(funcionario.getRua());
        localidade.setText(funcionario.getLocalidade());
        id.setText(String.valueOf(funcionario.getId()));

    }

    private void clearFuncionarioDetails() {
        nome.setText("");
        cargo.setText("");
        nif.setText("");
        ntelemovel.setText("");
        email.setText("");
        morada.setText("");
        localidade.setText("");
        id.setText("");
    }
}
