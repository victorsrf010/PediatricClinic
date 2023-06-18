package app.projeto.Controllers.Funcionario.PopUp;

import app.projeto.Entities.ConsultaEntity;
import app.projeto.Entities.FuncionarioEntity;
import app.projeto.Entities.UtenteEntity;
import app.projeto.Model;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class NovaConsultaController {

    public CalendarView calendarView;
    public ChoiceBox<FuncionarioEntity> drChoicebox;
    public ChoiceBox<UtenteEntity> utenteChoicebox;
    public Button guardarBtn;

    private Calendar calendar;
    private Entry<ConsultaEntity> currentEntry;

    public void initialize() {

        calendar = new Calendar("Consultas");
        CalendarSource calendarSource = new CalendarSource("Calendario");
        calendarSource.getCalendars().add(calendar);
        calendarView.getCalendarSources().add(calendarSource);

        drChoicebox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                updateCalendarEntries(newV);
            }
        });

        utenteChoicebox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null && currentEntry != null) {
                currentEntry.setTitle(newV.getNome());
                ConsultaEntity consulta = currentEntry.getUserObject();
                if (consulta == null) {
                    consulta = new ConsultaEntity();
                    currentEntry.setUserObject(consulta);
                }
                consulta.setUtente(newV);
            }
        });

        calendarView.setEntryFactory(param -> {
            ZonedDateTime zonedDateTime = param.getZonedDateTime();

            currentEntry = new Entry<>();
            currentEntry.changeStartDate(zonedDateTime.toLocalDate());
            currentEntry.changeStartTime(zonedDateTime.toLocalTime());
            currentEntry.changeEndTime(zonedDateTime.toLocalTime().plusMinutes(30)); // Set default duration to 1 hour.

            // Set the title to the name of the currently selected UtenteEntity
            UtenteEntity selectedUtente = utenteChoicebox.getValue();
            if (selectedUtente != null) {
                currentEntry.setTitle(selectedUtente.getNome());
            } else {
                // Set a default title if no UtenteEntity is selected
                currentEntry.setTitle("Nova consulta");
            }

            return currentEntry;
        });

        populateDrChoicebox();
        populateUtenteChoicebox();

        guardarBtn.setOnAction(event -> handleGuardarBtnClick());
    }

    private void updateCalendarEntries(FuncionarioEntity funcionario) {
        calendar.clear();
        List<ConsultaEntity> consultas = loadConsultasForFuncionario(funcionario);
        for (ConsultaEntity consulta : consultas) {
            Entry<ConsultaEntity> entry = new Entry<>();
            entry.changeStartDate(consulta.getDataConsulta().toLocalDate());
            entry.changeStartTime(consulta.getHoraConsulta().toLocalTime());
            entry.changeEndTime(consulta.getHoraConsulta().toLocalTime().plusMinutes(30));

            // Set the title to the name of the Utente
            entry.setTitle(consulta.getUtente().getNome());

            consulta.setFuncionario(funcionario);

            entry.setUserObject(consulta);
            calendar.addEntry(entry);
        }
    }


    private List<ConsultaEntity> loadConsultasForFuncionario(FuncionarioEntity funcionario) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            TypedQuery<ConsultaEntity> query = em.createQuery(
                    "SELECT c FROM ConsultaEntity c " +
                            "WHERE c.funcionario.id = :funcionarioId",
                    ConsultaEntity.class
            );
            query.setParameter("funcionarioId", funcionario.getId());

            List<ConsultaEntity> consultas = query.getResultList();
            em.getTransaction().commit();

            return consultas;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return Collections.emptyList();
        } finally {
            em.close();
            emf.close();
        }
    }

    private void populateUtenteChoicebox() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<UtenteEntity> query = em.createQuery("SELECT u FROM UtenteEntity u", UtenteEntity.class);
            List<UtenteEntity> utentes = query.getResultList();
            utenteChoicebox.getItems().addAll(utentes);
        } finally {
            em.close();
            emf.close();
        }
    }

    private void populateDrChoicebox() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<FuncionarioEntity> query = em.createQuery("SELECT f FROM FuncionarioEntity f WHERE f.tipoId=1", FuncionarioEntity.class);
            List<FuncionarioEntity> funcionarios = query.getResultList();
            drChoicebox.getItems().addAll(funcionarios);
        } finally {
            em.close();
            emf.close();
        }
    }

    private void handleGuardarBtnClick() {
        if (currentEntry != null) {
            LocalDateTime startDateTime = currentEntry.getStartAsLocalDateTime();

            ConsultaEntity consulta = new ConsultaEntity();
            consulta.setDataConsulta(java.sql.Date.valueOf(startDateTime.toLocalDate()));
            consulta.setHoraConsulta(java.sql.Time.valueOf(startDateTime.toLocalTime()));
            consulta.setIdTipo(1);
            consulta.setEstado("Agendada");
            consulta.setIdDiagnostico(null);

            UtenteEntity selectedUtente = utenteChoicebox.getValue();
            if (selectedUtente != null) {
                consulta.setUtente(selectedUtente);
            } else {
                System.out.println("Sem utente");
            }

            FuncionarioEntity selectedFuncionario = drChoicebox.getValue();
            if (selectedFuncionario != null) {
                consulta.setFuncionario(selectedFuncionario);
            } else {
                System.out.println("Sem funcionario");
            }

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                em.persist(consulta);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            } finally {
                em.close();
                emf.close();
            }

            Stage stage = (Stage) drChoicebox.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);

            Model.getInstance().getViewFactory().getConsultasController().refreshTable();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText(null);
            alert.setContentText("Consulta adicionado com sucesso");

            alert.showAndWait();
        }
    }
}

