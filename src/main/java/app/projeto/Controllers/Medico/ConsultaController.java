    package app.projeto.Controllers.Medico;

    import app.projeto.AuthenticationService;
    import app.projeto.Entities.ConsultaEntity;
    import app.projeto.Entities.DiagnosticoEntity;
    import app.projeto.Entities.FuncionarioEntity;
    import app.projeto.Entities.UtenteEntity;
    import app.projeto.Model;
    import jakarta.persistence.EntityManager;
    import jakarta.persistence.EntityManagerFactory;
    import jakarta.persistence.Persistence;
    import jakarta.persistence.TypedQuery;
    import javafx.fxml.Initializable;
    import javafx.scene.control.Alert;
    import javafx.scene.control.Button;
    import javafx.scene.control.ListView;
    import javafx.scene.control.TextArea;
    import javafx.scene.text.Text;

    import java.net.URL;
    import java.time.LocalDate;
    import java.time.LocalTime;
    import java.time.Period;
    import java.util.Collections;
    import java.util.List;
    import java.util.ResourceBundle;

    public class ConsultaController implements Initializable {
        public Text nomeInfo;
        public Text sexoInfo;
        public Text idadeInfo;
        public Text ultimaVisita;
        public ListView<DiagnosticoEntity> diagnósticos;
        public Button verDiagBtn;
        public TextArea sintomaasTxtArea;
        public TextArea tratamentoTxtArea;
        public Button guardarBtn;
        public Text nomeProx;
        public Text sexoProx;
        public Text idadeProx;
        public Text ultimaVisitaProx;
        public Text horaMarcação;
        public Button atenderBtn;

        private List<ConsultaEntity> consultas;
        private int currentIndex;

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            FuncionarioEntity currentFuncionario = AuthenticationService.getCurrentUser();
            this.consultas = loadConsultasForFuncionario(currentFuncionario);
            this.currentIndex = 0;

            if (!consultas.isEmpty()) {
                updateCurrentPatientInfo(consultas.get(currentIndex));

                if (consultas.size() > 1) {
                    updateNextPatientInfo(consultas.get(currentIndex + 1));
                }
            }

            atenderBtn.setOnAction(event -> handleAtenderBtn());
            guardarBtn.setOnAction(event -> handleGuardarBtn());
            verDiagBtn.setOnAction(event -> handleVerDiagBtn());
        }

        private List<ConsultaEntity> loadConsultasForFuncionario(FuncionarioEntity funcionario) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
            EntityManager em = emf.createEntityManager();

            try {
                em.getTransaction().begin();

                TypedQuery<ConsultaEntity> query = em.createQuery(
                        "SELECT c FROM ConsultaEntity c " +
                                "WHERE c.funcionario.id = :funcionarioId " +
                                "AND c.estado != :estado " +
                                "AND c.dataConsulta = :today " +
                                "ORDER BY c.horaConsulta",
                        ConsultaEntity.class
                );
                query.setParameter("funcionarioId", funcionario.getId());
                query.setParameter("estado", "Finalizada");
                query.setParameter("today", java.sql.Date.valueOf(LocalDate.now()));

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

        private void updateCurrentPatientInfo(ConsultaEntity currentConsulta) {
            UtenteEntity currentPatient = currentConsulta.getUtente();
            ConsultaEntity lastConsulta = getLastConsultaForUtente(currentPatient);
            nomeInfo.setText(currentPatient.getNome());
            sexoInfo.setText(currentPatient.getSexo());
            LocalDate currentDate = LocalDate.now();
            LocalDate birthday = currentPatient.getDataNascimento().toLocalDate();
            int age = Period.between(birthday, currentDate).getYears();
            idadeInfo.setText(String.valueOf(age));
            if (lastConsulta != null) {
                ultimaVisita.setText(lastConsulta.getDataConsulta().toString());
            }
            List<DiagnosticoEntity> diagnosticos = loadDiagnosticosForUtente(currentPatient);
            diagnósticos.getItems().clear();
            for (DiagnosticoEntity diagnostico : diagnosticos) {
                diagnósticos.getItems().add(diagnostico);
            }
        }

        private void updateNextPatientInfo(ConsultaEntity nextConsulta) {
            UtenteEntity nextPatient = nextConsulta.getUtente();
            ConsultaEntity lastConsulta = getLastConsultaForUtente(nextPatient);
            nomeProx.setText(nextPatient.getNome());
            sexoProx.setText(nextPatient.getSexo());
            LocalDate currentDate = LocalDate.now();
            LocalDate birthday = nextPatient.getDataNascimento().toLocalDate();
            int age = Period.between(birthday, currentDate).getYears();
            idadeProx.setText(String.valueOf(age));
            if (lastConsulta != null) {
                ultimaVisitaProx.setText(lastConsulta.getDataConsulta().toString());
            }
            LocalTime nextConsultaTime = nextConsulta.getHoraConsulta().toLocalTime();
            horaMarcação.setText(nextConsultaTime.toString());
        }

        private ConsultaEntity getLastConsultaForUtente(UtenteEntity utente) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
            EntityManager em = emf.createEntityManager();

            try {
                em.getTransaction().begin();

                TypedQuery<ConsultaEntity> query = em.createQuery(
                        "SELECT c FROM ConsultaEntity c " +
                                "WHERE c.utente.id = :utenteId " +
                                "ORDER BY c.dataConsulta DESC",
                        ConsultaEntity.class
                );
                query.setParameter("utenteId", utente.getId());
                query.setMaxResults(1);

                ConsultaEntity lastConsulta = query.getSingleResult();
                em.getTransaction().commit();

                return lastConsulta;
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
                return null;
            } finally {
                em.close();
                emf.close();
            }
        }

        private void handleAtenderBtn() {
            if (!consultas.isEmpty()) {
                ConsultaEntity currentConsulta = consultas.get(0);
                setConsultaEstado(currentConsulta, "Finalizada");

                consultas.remove(0);

                if (!consultas.isEmpty()) {
                    ConsultaEntity nextConsulta = consultas.get(0);
                    updateCurrentPatientInfo(nextConsulta);
                    if (consultas.size() > 1) {
                        ConsultaEntity upcomingConsulta = consultas.get(1);
                        updateNextPatientInfo(upcomingConsulta);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Informação");
                        alert.setHeaderText(null);
                        alert.setContentText("Já não tem mais consultas por hoje");

                        alert.showAndWait();
                    } else {
                        clearNextPatientInfo();
                    }
                } else {
                    clearCurrentPatientInfo();
                    clearNextPatientInfo();
                }
                sintomaasTxtArea.setText("");
                tratamentoTxtArea.setText("");
            }
            if (consultas.size() > 1) {
                ConsultaEntity currentConsulta = consultas.get(0);
                setConsultaEstado(currentConsulta, "Finalizada");

                consultas.remove(0);
                ConsultaEntity nextConsulta = consultas.get(0);
                updateCurrentPatientInfo(nextConsulta);
                if (consultas.size() > 1) {
                    ConsultaEntity upcomingConsulta = consultas.get(1);
                    updateNextPatientInfo(upcomingConsulta);
                    sintomaasTxtArea.setText("");
                    tratamentoTxtArea.setText("");
                } else {
                    clearNextPatientInfo();
                }
            }
        }

        private void clearCurrentPatientInfo() {
            nomeInfo.setText("");
            sexoInfo.setText("");
            idadeInfo.setText("");
            ultimaVisita.setText("");
            diagnósticos.getItems().clear();
            sintomaasTxtArea.setText("");
            tratamentoTxtArea.setText("");
        }

        private void clearNextPatientInfo() {
            nomeProx.setText("");
            sexoProx.setText("");
            idadeProx.setText("");
            ultimaVisitaProx.setText("");
            horaMarcação.setText("");
            sintomaasTxtArea.setText("");
            tratamentoTxtArea.setText("");
        }

        private void setConsultaEstado(ConsultaEntity consulta, String estado) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
            EntityManager em = emf.createEntityManager();

            try {
                em.getTransaction().begin();
                consulta.setEstado(estado);
                em.merge(consulta);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            } finally {
                em.close();
                emf.close();
            }
        }

        public void handleGuardarBtn() {
            ConsultaEntity currentConsulta = consultas.get(0);
            DiagnosticoEntity diagnostico = new DiagnosticoEntity();
            diagnostico.setIdConsulta(currentConsulta.getId());
            diagnostico.setSintomas(sintomaasTxtArea.getText());
            diagnostico.setTratamento(tratamentoTxtArea.getText());
            diagnostico.setIdUtente(currentConsulta.getUtente().getId());
            diagnostico.setIdFuncionario(currentConsulta.getFuncionario().getId());
            diagnostico.setDataDiagnostico(new java.sql.Date(System.currentTimeMillis()));
            diagnostico = saveDiagnostico(diagnostico);

            currentConsulta.setIdDiagnostico(diagnostico.getId());
            saveConsulta(currentConsulta);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText(null);
            alert.setContentText("Diagnóstico guardado com sucesso");

            alert.showAndWait();
        }

        private DiagnosticoEntity saveDiagnostico(DiagnosticoEntity diagnostico) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
            EntityManager em = emf.createEntityManager();

            try {
                em.getTransaction().begin();
                em.persist(diagnostico);
                em.flush();
                em.refresh(diagnostico);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            } finally {
                em.close();
                emf.close();
            }

            return diagnostico;
        }

        private void saveConsulta(ConsultaEntity consulta) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
            EntityManager em = emf.createEntityManager();

            try {
                em.getTransaction().begin();
                em.merge(consulta);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            } finally {
                em.close();
                emf.close();
            }
        }

        private List<DiagnosticoEntity> loadDiagnosticosForUtente(UtenteEntity utente) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
            EntityManager em = emf.createEntityManager();

            try {
                em.getTransaction().begin();

                TypedQuery<DiagnosticoEntity> query = em.createQuery(
                        "SELECT d FROM DiagnosticoEntity d " +
                                "WHERE d.idUtente = :utenteId " +
                                "ORDER BY d.dataDiagnostico DESC",
                        DiagnosticoEntity.class
                );
                query.setParameter("utenteId", utente.getId());

                List<DiagnosticoEntity> diagnosticos = query.getResultList();
                em.getTransaction().commit();

                return diagnosticos;
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
                return Collections.emptyList();
            } finally {
                em.close();
                emf.close();
            }
        }

        public void handleVerDiagBtn() {
            DiagnosticoEntity selectedDiag = diagnósticos.getSelectionModel().getSelectedItem();
            if (selectedDiag != null) {
                Model.getInstance().setSelectedDiag(selectedDiag);
                Model.getInstance().getViewFactory().showDiag();
            }
        }

    }
