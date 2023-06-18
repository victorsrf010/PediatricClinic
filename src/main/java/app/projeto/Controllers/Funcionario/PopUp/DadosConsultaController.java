package app.projeto.Controllers.Funcionario.PopUp;

import app.projeto.Entities.ConsultaEntity;
import app.projeto.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class DadosConsultaController implements Initializable {
    public Button verDiagBtn;
    public Text dtConsulta;
    public Text doutor;
    public Text paciente;
    public Text hora;
    public Text estado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ConsultaEntity selectedConsulta = Model.getInstance().getSelectedConsulta();

        doutor.setText(selectedConsulta.getFuncionario().getNome());
        paciente.setText(selectedConsulta.getUtente().getNome());
        hora.setText(selectedConsulta.getHoraConsulta().toString());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(selectedConsulta.getDataConsulta());
        dtConsulta.setText(formattedDate);
        estado.setText(selectedConsulta.getEstado());
    }


}
