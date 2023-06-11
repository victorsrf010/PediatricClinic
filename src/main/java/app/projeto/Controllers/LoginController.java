package app.projeto.Controllers;

import app.projeto.AuthenticationService;
import app.projeto.Entities.FuncionarioEntity;
import app.projeto.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public TextField numFuncionario;
    public ImageView logo;
    public Button entrarButton;
    public PasswordField password;
    public Text errorText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        entrarButton.setOnAction(actionEvent -> {
            if (checkFields()) {
                onLogin();
            }

        });
    }

    private void onLogin() {
        int id = Integer.parseInt(numFuncionario.getText());
        String pass = password.getText();

        // Call the service to check the user credentials
        FuncionarioEntity funcionario = AuthenticationService.findFuncionarioByIdAndPassword(id, pass);

        if (funcionario != null) {
            Stage stage = (Stage) errorText.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
            // Check the ID_TIPOFUNCIONARIO of the user and open the corresponding window
            switch (funcionario.getTipoId()) {
                case 3 -> Model.getInstance().getViewFactory().showAdminWindow();
                case 2 -> Model.getInstance().getViewFactory().showFuncionarioWindow();
                case 1 -> Model.getInstance().getViewFactory().showMedicoWindow();
            }
        } else {
            errorText.setText("Credenciais incorretas, por favor tente novamente.");
        }
    }

    private boolean checkFields() {
        String idText = numFuncionario.getText();
        String pass = password.getText();

        if (idText.isEmpty() || pass.isEmpty()) {
            errorText.setText("Por favor preencha todos os campos.");
            return false;
        } else {
            return true;
        }
    }
}