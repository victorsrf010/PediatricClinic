package app.projeto;

import app.projeto.Controllers.Admin.adminController;
import app.projeto.Controllers.Funcionario.FuncionarioController;
import app.projeto.Controllers.Medico.medicoController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {

    //Vistas funcionario

    private final StringProperty adminSelectedMenuItem;
    private final StringProperty funcionarioSelectedMenuItem;
    private final StringProperty medicoSelectedMenuItem;

    private AnchorPane funcionariosView;
    private AnchorPane pacientesAdminView;
    private AnchorPane consultasAdminView;

    private AnchorPane dashboardView;
    private AnchorPane pacientesView;
    private AnchorPane consultasView;

    private AnchorPane consultaView;
    private AnchorPane meusPacientesView;
    private AnchorPane calendarioView;


    private AnchorPane perfilView;


    public ViewFactory() {
        this.adminSelectedMenuItem = new SimpleStringProperty("");
        this.funcionarioSelectedMenuItem = new SimpleStringProperty("");
        this.medicoSelectedMenuItem = new SimpleStringProperty("");
    }



    // ** ** Admin ** **

    public StringProperty getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }

    public AnchorPane getFuncionariosView() {
        if (funcionariosView == null) {
            try {
                funcionariosView = new FXMLLoader(getClass().getResource("FXML/Admin/funcionarios.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return funcionariosView;
    }

    public AnchorPane getPacientesAdminView() {
        if (pacientesAdminView == null) {
            try {
                pacientesAdminView = new FXMLLoader(getClass().getResource("FXML/Admin/pacientes.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pacientesAdminView;
    }

    public AnchorPane getConsultasAdminView() {
        if (consultasAdminView == null) {
            try {
                consultasAdminView = new FXMLLoader(getClass().getResource("FXML/Admin/consultas.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return consultasAdminView;
    }



    //  ** ** Funcionartio ** **

    public StringProperty getFuncionarioSelectedMenuItem() {
        return funcionarioSelectedMenuItem;
    }

    public AnchorPane getDashboardView() {
        if (dashboardView == null) {
            try {
                dashboardView = new FXMLLoader(getClass().getResource("FXML/Funcionario/dashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dashboardView;
    }

    public AnchorPane getConsultasView() {
        if (consultasView == null) {
            try {
                consultasView = new FXMLLoader(getClass().getResource("FXML/Funcionario/consultas.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return consultasView;
    }

    public AnchorPane getPacientesView() {
        if (pacientesView == null) {
            try {
                pacientesView = new FXMLLoader(getClass().getResource("FXML/Funcionario/pacientes.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pacientesView;
    }



    // ** ** Medico ** **

    public StringProperty getMedicoSelectedMenuItem() {
        return medicoSelectedMenuItem;
    }

    public AnchorPane getPerfilView() {
        if (perfilView == null) {
            try {
                perfilView = new FXMLLoader(getClass().getResource("FXML/perfil.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return perfilView;
    }

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/login.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("TinyHearts");
        stage.show();
    }

    public void showAdminWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Admin/admin.fxml"));
        adminController adminController = new adminController();
        loader.setController(adminController);
        createStage(loader);
    }

    public void showFuncionarioWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Funcionario/funcionario.fxml"));
        FuncionarioController funcionarioController = new FuncionarioController();
        loader.setController(funcionarioController);
        createStage(loader);
    }

    public void showMedicoWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Medico/medico.fxml"));
        medicoController medicoController = new medicoController();
        loader.setController(medicoController);
        createStage(loader);
    }



    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("TinyHearts");
        stage.setMaximized(true);
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

}
