package app.projeto.Controllers.Funcionario;

import app.projeto.Entities.ConsultaEntity;
import app.projeto.Entities.DoencasConhecidasEntity;
import app.projeto.Entities.PagamentoEntity;
import app.projeto.Entities.UtenteEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PacientesController implements Initializable {

    public TableView<UtenteEntity> utente;
    public TableColumn<UtenteEntity, String> nomeClm;
    public TableColumn<UtenteEntity, String> nifClm;
    public TableColumn<UtenteEntity, Date> dtNascimentoClm;
    public TableColumn<UtenteEntity, String> repLegalClm;
    public TableColumn<UtenteEntity, String> contatoRepLegalClm;
    public TableColumn<UtenteEntity, String> emailRepLegalClm;
    public Button adicionarPacBtn;
    public Button editarBtn;
    public TextField pesquisarTxtfd;
    public TableView<ConsultaEntity> ultimasConsultas;
    public TableColumn<ConsultaEntity, Date> dtPagamentoClm;
    public TableColumn<ConsultaEntity, String> drClm;
    public TableColumn<ConsultaEntity, BigDecimal> valorClm;
    public TableColumn<ConsultaEntity, String> estadoPagClm;
    public Text nomeInfo;
    public Text sexoInfo;
    public Text idadeInfo;
    public Button agendarConsultaBtn;
    public Button historicoBtn;
    public Text knownDiseasesText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nomeClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        nifClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNif()));
        dtNascimentoClm.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataNascimento()));
        repLegalClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomeRepresentanteLegal()));
        contatoRepLegalClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContactoRepresentanteLegal()));
        emailRepLegalClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        dtPagamentoClm.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataConsulta()));
        drClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFuncionario().getNome()));
        valorClm.setCellValueFactory(cellData -> {
            BigDecimal valor = BigDecimal.ZERO;
            List<PagamentoEntity> pagamentos = cellData.getValue().getPagamentos();
            if (!pagamentos.isEmpty()) {
                valor = pagamentos.get(0).getValor();
            }
            return new SimpleObjectProperty<>(valor);
        });
        estadoPagClm.setCellValueFactory(cellData -> {
            String estado = "";
            List<PagamentoEntity> pagamentos = cellData.getValue().getPagamentos();
            if (!pagamentos.isEmpty()) {
                estado = pagamentos.get(0).getEstado();
            }
            return new SimpleStringProperty(estado);
        });

        // Add a listener to the pesquisarTxtfd TextField
        pesquisarTxtfd.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchTerm = newValue.trim(); // Get the trimmed search term

            // Clear the selection and details when search term is empty
            if (searchTerm.isEmpty()) {
                utente.getSelectionModel().clearSelection();
                clearUtenteDetails();
                return;
            }

            // Filter the utente table based on the search term
            utente.getItems().clear();
            utente.getItems().addAll(filterUtentes(searchTerm));
        });

        utente.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateUtenteDetails(newValue);
            }
        });

        loadUtenteData();
    }

    private List<UtenteEntity> filterUtentes(String searchTerm) {
        // Retrieve the UtenteEntity data from the database based on the search term
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            List<UtenteEntity> utentes = em.createQuery("SELECT u FROM UtenteEntity u WHERE u.nome LIKE :searchTerm ORDER BY u.nome", UtenteEntity.class)
                    .setParameter("searchTerm", "%" + searchTerm + "%")
                    .getResultList();
            em.getTransaction().commit();

            return utentes;
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

    private void loadUtenteData() {
        // Retrieve the UtenteEntity data from the database
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            List<UtenteEntity> utentes = em.createQuery("SELECT u FROM UtenteEntity u ORDER BY nome", UtenteEntity.class)
                    .getResultList();
            em.getTransaction().commit();

            // Populate the table view with the retrieved data
            utente.getItems().addAll(utentes);
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
            emf.close();
        }
    }

    private void updateUtenteDetails(UtenteEntity utente) {
        nomeInfo.setText(utente.getNome());
        sexoInfo.setText(utente.getSexo());

        LocalDate currentDate = LocalDate.now();
        LocalDate birthday = utente.getDataNascimento().toLocalDate();
        int age = Period.between(birthday, currentDate).getYears();

        idadeInfo.setText(String.valueOf(age));

        loadConsultas(utente);

        // Retrieve the known diseases of the selected UtenteEntity
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            TypedQuery<DoencasConhecidasEntity> doencasQuery = em.createQuery(
                    "SELECT d FROM DoencasConhecidasEntity d JOIN UtenteDoencasEntity ud ON d.doencaId = ud.doencaId WHERE ud.utenteId = :utenteId",
                    DoencasConhecidasEntity.class
            );
            doencasQuery.setParameter("utenteId", utente.getId());
            List<DoencasConhecidasEntity> doencas = doencasQuery.getResultList();

            StringBuilder diseasesBuilder = new StringBuilder();
            for (DoencasConhecidasEntity doenca : doencas) {
                diseasesBuilder.append(doenca.getNomeDoenca()).append(", ");
            }
            String knownDiseases = diseasesBuilder.toString();
            if (knownDiseases.length() > 2) {
                knownDiseases = knownDiseases.substring(0, knownDiseases.length() - 2);
            }

            knownDiseasesText.setText(knownDiseases);

            em.getTransaction().commit();
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
            emf.close();
        }

        loadConsultas(utente);
    }


    private void clearUtenteDetails() {
        nomeInfo.setText("");
        sexoInfo.setText("");
        idadeInfo.setText("");
        // Clear other Text fields if needed
    }

    private void loadConsultas(UtenteEntity utente) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            TypedQuery<ConsultaEntity> consultaQuery = em.createQuery(
                    "SELECT c FROM ConsultaEntity c JOIN FETCH c.pagamentos WHERE c.utente = :utente ORDER BY c.dataConsulta DESC",
                    ConsultaEntity.class
            );
            consultaQuery.setParameter("utente", utente);
            List<ConsultaEntity> consultas = consultaQuery.getResultList();

            ObservableList<ConsultaEntity> consultasList = FXCollections.observableArrayList(consultas);

            // Set the items of the TableView to the consultasList
            ultimasConsultas.setItems(consultasList);

            em.getTransaction().commit();
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
            emf.close();
        }
    }


}
