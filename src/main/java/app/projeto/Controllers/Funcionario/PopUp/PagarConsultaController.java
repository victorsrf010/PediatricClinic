package app.projeto.Controllers.Funcionario.PopUp;

import app.projeto.Entities.ConsultaEntity;
import app.projeto.Entities.PagamentoEntity;
import app.projeto.Entities.TipoPagamentoEntity;
import app.projeto.JPAUtil;
import app.projeto.Model;
import jakarta.persistence.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class PagarConsultaController implements Initializable {
    public Button pagarBtn;
    public Text valor;
    public Text dtConsulta;
    public ChoiceBox<TipoPagamentoEntity> metodoPag;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ConsultaEntity selectedConsulta = Model.getInstance().getSelectedConsulta();

        if (!selectedConsulta.getPagamentos().isEmpty()) {
            PagamentoEntity pagamento = selectedConsulta.getPagamentos().get(0);
            valor.setText(String.valueOf(pagamento.getValor()));
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(selectedConsulta.getDataConsulta());
        dtConsulta.setText(formattedDate);

        populateUtenteChoicebox();

        pagarBtn.setOnAction(event -> handlePagarBtn());
    }


    private void populateUtenteChoicebox() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tinyhearts");
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TipoPagamentoEntity> query = em.createQuery("SELECT u FROM TipoPagamentoEntity u", TipoPagamentoEntity.class);
            List<TipoPagamentoEntity> metodos = query.getResultList();
            metodoPag.getItems().addAll(metodos);
        } finally {
            em.close();
            emf.close();
        }
    }

    public void handlePagarBtn() {
        ConsultaEntity selectedConsulta = Model.getInstance().getSelectedConsulta();
        if (selectedConsulta != null) {
            EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
            EntityManager entityManager = factory.createEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();

                ConsultaEntity consultaEntity = entityManager.find(ConsultaEntity.class, selectedConsulta.getId());

                if (consultaEntity != null) {
                    if (!consultaEntity.getPagamentos().isEmpty()) {
                        PagamentoEntity pagamento = consultaEntity.getPagamentos().get(0);

                        if ("Pago".equals(pagamento.getEstado())) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Erro");
                            alert.setHeaderText(null);
                            alert.setContentText("O pagamento já foi efetuado");

                            alert.showAndWait();

                            return;
                        }

                        TipoPagamentoEntity selectedTipoPagamento = metodoPag.getSelectionModel().getSelectedItem();
                        if (selectedTipoPagamento != null) {
                            pagamento.setTipoId(selectedTipoPagamento.getTipoId());
                        }

                        pagamento.setDataPagamento(new java.sql.Date(System.currentTimeMillis()));
                    }

                    consultaEntity.setEstado("Em espera");
                    entityManager.merge(consultaEntity);
                    transaction.commit();

                } else {
                    System.out.println("ConsultaEntity not found");
                }
            } finally {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                entityManager.close();
            }

            Stage stage = (Stage) metodoPag.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);

            Model.getInstance().getViewFactory().getConsultasController().refreshTable();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText(null);
            alert.setContentText("Pagamento efetuado com sucesso");

            alert.showAndWait();
        }
    }

}
