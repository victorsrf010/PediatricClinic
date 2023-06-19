package app.projeto.Controllers.Funcionario;

import app.projeto.Entities.ConsultaEntity;
import app.projeto.Entities.FuncionarioEntity;
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
import javafx.scene.text.Text;

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
    public Text medicosDisponiveis;
    public Button consultaSemMarcacaoBtn;
    public ListView<FuncionarioEntity> medicosPresentes;
    public Button cancelarBtn;
    public Button presenteBtn;
    public Button pagarBtn;

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
        pagarBtn.setOnAction(event -> handlePagarBtn());

        consultaSemMarcacaoBtn.setOnAction(event ->{
            Model.getInstance().getViewFactory().showNovaConsultaWindow();
        });

        updateMedicosDisponiveis();

        try {
            populateTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshTable() throws SQLException {
        proximasConsultas.getItems().clear();

        List<ConsultaEntity> allConsultas = populateTable();
        if (allConsultas != null) {
            proximasConsultas.getItems().addAll(allConsultas);
        }
    }


    public List<ConsultaEntity> populateTable() throws SQLException {
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
        return null;
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

                ConsultaEntity consultaEntity = entityManager.find(ConsultaEntity.class, selectedConsulta.getId());

                if (consultaEntity != null) {
                    consultaEntity.setEstado("Em espera");
                    entityManager.merge(consultaEntity);
                    transaction.commit();
                    proximasConsultas.getItems().remove(selectedConsulta);

                } else {
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

                ConsultaEntity consultaEntity = entityManager.find(ConsultaEntity.class, selectedConsulta.getId());

                if (consultaEntity != null) {
                    consultaEntity.setEstado("Cancelada");
                    entityManager.merge(consultaEntity);
                    transaction.commit();

                    proximasConsultas.getItems().remove(selectedConsulta);
                } else {
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

    public void handlePagarBtn() {
        ConsultaEntity selectedConsulta = proximasConsultas.getSelectionModel().getSelectedItem();
        if (selectedConsulta != null) {
            Model.getInstance().setSelectedConsulta(selectedConsulta);
            Model.getInstance().getViewFactory().showPagarConsulta();
        }
    }

}
