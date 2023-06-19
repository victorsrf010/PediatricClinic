package app.projeto.Controllers.Medico;

import app.projeto.AuthenticationService;
import app.projeto.Entities.ConsultaEntity;
import app.projeto.Entities.FuncionarioEntity;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

public class HorarioController {
    public CalendarView calendar;

    private Calendar consultasCalendar;

    public void initialize() {
        consultasCalendar = new Calendar("Consultas");
        CalendarSource calendarSource = new CalendarSource("Calendario");
        calendarSource.getCalendars().add(consultasCalendar);
        calendar.getCalendarSources().add(calendarSource);

        FuncionarioEntity currentFuncionario = AuthenticationService.getCurrentUser();
        if (currentFuncionario != null) {
            updateCalendarEntries(currentFuncionario);
        }
    }

    private void updateCalendarEntries(FuncionarioEntity funcionario) {
        consultasCalendar.clear();
        List<ConsultaEntity> consultas = loadConsultasForFuncionario(funcionario);
        for (ConsultaEntity consulta : consultas) {
            Entry<ConsultaEntity> entry = new Entry<>();
            entry.changeStartDate(consulta.getDataConsulta().toLocalDate());
            entry.changeStartTime(consulta.getHoraConsulta().toLocalTime());
            entry.changeEndTime(consulta.getHoraConsulta().toLocalTime().plusMinutes(30));

            entry.setTitle(consulta.getUtente().getNome());

            consulta.setFuncionario(funcionario);

            entry.setUserObject(consulta);
            consultasCalendar.addEntry(entry);
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
}
