package app.projeto.Controllers.Funcionario.PopUp;

import app.projeto.Entities.ConsultaEntity;
import app.projeto.Entities.DiagnosticoEntity;
import app.projeto.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class DiagController implements Initializable {
    public Text dtConsulta;
    public TextArea sintomaasTxtArea;
    public TextArea tratamentoTxtArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DiagnosticoEntity selectedDiag = Model.getInstance().getSelectedDiag();

        sintomaasTxtArea.setText(selectedDiag.getSintomas());
        tratamentoTxtArea.setText(selectedDiag.getTratamento());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf.format(selectedDiag.getDataDiagnostico());
        dtConsulta.setText(formattedDate);
    }
}
