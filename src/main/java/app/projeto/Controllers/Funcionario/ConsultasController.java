package app.projeto.Controllers.Funcionario;

import app.projeto.Entities.ConsultaEntity;
import app.projeto.Model;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


public class ConsultasController implements Initializable {
    public Button agendarConsultaBtn;
    public Button reagendarConsultaBtn;
    public TableView<ConsultaEntity> consultas;
    public TableColumn<ConsultaEntity, Integer> idClm;
    public TableColumn<ConsultaEntity, String> estadoClm;
    public TableColumn<ConsultaEntity, String> doutorClm;
    public TableColumn<ConsultaEntity, String> pacienteClm;
    public TableColumn<ConsultaEntity, String> pagamentoClm;
    public TableColumn<ConsultaEntity, Date> DataClm;
    public TableColumn<ConsultaEntity, Time> horaClm;
    public TextField pesquisarTxtfd;
    public Button detalhesConsultaBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idClm.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        estadoClm.setCellValueFactory(cellData -> {
            String estadoconsul = cellData.getValue().getEstado();
            return new SimpleStringProperty(estadoconsul);
        });

        doutorClm.setCellValueFactory(cellData -> {
            String nomeDoutor = cellData.getValue().getFuncionario().getNome();
            return new SimpleStringProperty(nomeDoutor);
        });

        pacienteClm.setCellValueFactory(cellData -> {
            String nomeUtente = cellData.getValue().getUtente().getNome();
            return new SimpleStringProperty(nomeUtente);
        });

        DataClm.setCellValueFactory(cellData -> {
            Date data = cellData.getValue().getDataConsulta();
            return new SimpleObjectProperty<>(data);
        });

        horaClm.setCellValueFactory(cellData -> {
            Time hora = cellData.getValue().getHoraConsulta();
            return new SimpleObjectProperty<>(hora);
        });



        // Load all ConsultaEntity records and populate the table view
        List<ConsultaEntity> allConsultas = loadAllConsultas();
        consultas.getItems().addAll(allConsultas);

        // Add listener to the pesquisarTxtfd for search functionality
        pesquisarTxtfd.textProperty().addListener((observable, oldValue, newValue) -> searchConsultas(newValue));

        agendarConsultaBtn.setOnAction(event ->{
            Model.getInstance().getViewFactory().showNovaConsultaWindow();
        });


    }

    private List<ConsultaEntity> loadAllConsultas() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            List<ConsultaEntity> consultas = em.createQuery("SELECT c FROM ConsultaEntity c", ConsultaEntity.class)
                    .getResultList();
            em.getTransaction().commit();

            return consultas;
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            em.getTransaction().rollback();
            return Collections.emptyList();
        } finally {
            em.close();
            emf.close();
        }
    }
    private void searchConsultas(String keyword) {
        // Clear the current table data
        consultas.getItems().clear();

        // Load filtered ConsultaEntity records based on the keyword
        List<ConsultaEntity> filteredConsultas = loadFilteredConsultas(keyword);
        consultas.getItems().addAll(filteredConsultas);
    }

    private List<ConsultaEntity> loadFilteredConsultas(String keyword) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Create a named query to retrieve ConsultaEntity records filtered by Utente or Funcionario name
            TypedQuery<ConsultaEntity> query = em.createQuery(
                    "SELECT c FROM ConsultaEntity c " +
                            "WHERE LOWER(c.utente.nome) LIKE LOWER(:keyword) " +
                            "   OR LOWER(c.funcionario.nome) LIKE LOWER(:keyword)",
                    ConsultaEntity.class
            );
            query.setParameter("keyword", "%" + keyword + "%");

            List<ConsultaEntity> consultas = query.getResultList();
            em.getTransaction().commit();

            return consultas;
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            em.getTransaction().rollback();
            return Collections.emptyList();
        } finally {
            em.close();
            emf.close();
        }
    }

}
