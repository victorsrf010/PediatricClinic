package app.projeto.Controllers.Funcionario;

import app.projeto.Entities.ConsultaEntity;
import app.projeto.Entities.FuncionarioEntity;
import app.projeto.Entities.InfoEntity;
import app.projeto.JPAUtil;
import app.projeto.Model;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardController implements Initializable {

    public TableView<ConsultaEntity> proximasConsultas;
    public TableColumn<ConsultaEntity, String> horaMarcacao;
    public TableColumn<ConsultaEntity, String> nomePaciente;
    public TableColumn<ConsultaEntity, String> nomeDoutor;
    public Text antendimentos;
    public Text pacientesPresentes;
    public Text pacientesEspera;
    public Text medicosDisponiveis;
    public Button consultaSemMarcacaoBtn;
    public Button efetuarPagamentoBtn;
    public ListView<FuncionarioEntity> medicosPresentes;
    public Button cancelarBtn;
    public Button reagendarBtn;
    public Button presenteBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        horaMarcacao.setCellValueFactory(data -> {
            Time sqlTime = (Time) data.getValue().getHoraConsulta();
            LocalTime localTime = LocalTime.parse(sqlTime.toString());
            return new SimpleStringProperty(localTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        });

        nomePaciente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUtente().getNome()));
        nomeDoutor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFuncionario().getNome()));

        cancelarBtn.setOnAction(event -> handleCancelarBtn());
        presenteBtn.setOnAction(event -> handlePresenteBtn());
        reagendarBtn.setOnAction(event -> handleReagendarBtn());


        updateMedicosDisponiveis();

        try {
            populateTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void populateTable() throws SQLException {
        EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            LocalDate currentDate = LocalDate.now();
            Date startOfDay = Date.valueOf(currentDate);
            Date endOfDay = Date.valueOf(currentDate.plusDays(1));

            TypedQuery<ConsultaEntity> listaConsultas = entityManager.createNamedQuery("ListaConsultas", ConsultaEntity.class);
            listaConsultas.setParameter("startOfDay", startOfDay);
            listaConsultas.setParameter("endOfDay", endOfDay);


            List<ConsultaEntity> consultas = listaConsultas.getResultList();

            // Filter the consultas based on idEstadoconsul
            List<ConsultaEntity> filteredConsultas = consultas.stream()
                    .filter(consulta -> Objects.equals(consulta.getEstado(), "Agendada"))
                    .collect(Collectors.toList());

            proximasConsultas.getItems().setAll(filteredConsultas);

            TypedQuery<FuncionarioEntity> medicosQuery = entityManager.createQuery(
                    "SELECT nome FROM FuncionarioEntity f WHERE f.tipoId = 1 AND f.estado = true",
                    FuncionarioEntity.class
            );
            medicosPresentes.getItems().setAll(medicosQuery.getResultList());

            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            entityManager.close();
        }
    }

    public void updateMedicosDisponiveis() {
        EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
        EntityManager entityManager = factory.createEntityManager();
        try {
            TypedQuery<Long> countQuery = entityManager.createQuery(
                    "SELECT COUNT(f) FROM FuncionarioEntity f WHERE f.tipoId = 3 AND f.estado = true",
                    Long.class
            );
            Long count = countQuery.getSingleResult();
            medicosDisponiveis.setText(String.valueOf(count));
        } finally {
            entityManager.close();
        }
    }

    public void handlePresenteBtn() {
        ConsultaEntity selectedConsulta = proximasConsultas.getSelectionModel().getSelectedItem();
        if (selectedConsulta != null) {
            EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
            EntityManager entityManager = factory.createEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();

                // Retrieve the ConsultaEntity from the database using its ID
                ConsultaEntity consultaEntity = entityManager.find(ConsultaEntity.class, selectedConsulta.getId());

                // Check if the ConsultaEntity exists
                if (consultaEntity != null) {
                    // Update the state of the selected consulta to "Em espera"
                    consultaEntity.setEstado("Em espera");
                    entityManager.merge(consultaEntity);
                    transaction.commit();
                    proximasConsultas.getItems().remove(selectedConsulta);

                } else {
                    // ConsultaEntity not found, handle the error accordingly
                    System.out.println("ConsultaEntity not found");
                }
            } finally {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                entityManager.close();
            }
        }

    }


    public void handleCancelarBtn() {
        ConsultaEntity selectedConsulta = proximasConsultas.getSelectionModel().getSelectedItem();
        if (selectedConsulta != null) {
            EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
            EntityManager entityManager = factory.createEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();

                // Retrieve the ConsultaEntity from the database using its ID
                ConsultaEntity consultaEntity = entityManager.find(ConsultaEntity.class, selectedConsulta.getId());

                // Check if the ConsultaEntity exists
                if (consultaEntity != null) {
                    // Update the state of the selected consulta to "Cancelada"
                    consultaEntity.setEstado("Cancelada");
                    entityManager.merge(consultaEntity);
                    transaction.commit();

                    // Remove the selected consulta from the TableView
                    proximasConsultas.getItems().remove(selectedConsulta);
                } else {
                    // ConsultaEntity not found, handle the error accordingly
                    System.out.println("ConsultaEntity not found");
                }
            } finally {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                entityManager.close();
            }
        }
    }

    public void handleReagendarBtn() {
        Model.getInstance().getViewFactory().showReagendarWindow();
    }

}
