package app.projeto;

import app.projeto.Controllers.Admin.AdminController;
import app.projeto.Controllers.Admin.PopUp.editarFuncController;
import app.projeto.Controllers.Admin.PopUp.removerFuncController;
import app.projeto.Controllers.Funcionario.ConsultasController;
import app.projeto.Controllers.Funcionario.DashboardController;
import app.projeto.Controllers.Funcionario.FuncionarioController;
import app.projeto.Controllers.Funcionario.PacientesController;
import app.projeto.Controllers.Funcionario.PopUp.EditarDadosPacienteController;
import app.projeto.Controllers.Medico.MedicoController;
import app.projeto.Entities.FuncionarioEntity;
import app.projeto.Entities.UtenteEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Objects;

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


    private ConsultasController consultasController;
    private PacientesController pacientesController;
    private DashboardController dashboardController;

    public ConsultasController getConsultasController() {
        if (consultasController == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Funcionario/consultas.fxml"));
                consultasView = loader.load();
                consultasController = loader.getController();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return consultasController;
    }

    public PacientesController getPacientesController() {
        if (pacientesController == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Funcionario/pacientes.fxml"));
                pacientesView = loader.load();
                pacientesController = loader.getController();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pacientesController;
    }

    public DashboardController getDashboardController() {
        if (dashboardController == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Funcionario/dashboard.fxml"));
                dashboardView = loader.load();
                dashboardController = loader.getController();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dashboardController;
    }

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

    public void showAdicionarFuncionario() {
        showPopUpWindow("FXML/Admin/PopUp/adicionarFunc.fxml", "Adicionar funcionario");
    }

    public void showRemoverFunc(FuncionarioEntity funcionario) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Admin/PopUp/removerFunc.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        removerFuncController controller = loader.getController();
        controller.initializeData(funcionario);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Images/green_logo.png"))));
        stage.setTitle("Editar funcionário");
        stage.show();
    }

    public void showEditarFuncionario(FuncionarioEntity funcionario) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Admin/PopUp/editarFunc.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        editarFuncController controller = loader.getController();
        controller.initializeData(funcionario);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Images/green_logo.png"))));
        stage.setTitle("Editar funcionário");
        stage.show();
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

    public void showNovaConsultaWindow() {
        showPopUpWindow("FXML/Funcionario/PopUp/novaConsulta.fxml", "Nova consulta");
    }

    public void showAdicionarPaciente() {
        showPopUpWindow("FXML/Funcionario/PopUp/adicionarPaciente.fxml", "Adicionar paciente");
    }

    public void showEditarPaciente(UtenteEntity utente) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Funcionario/PopUp/editarDadosPaciente.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        EditarDadosPacienteController controller = loader.getController();
        controller.initializeData(utente);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Images/green_logo.png"))));
        stage.setTitle("Editar paciente");
        stage.show();
    }

    public void showPagarConsulta(){
        showPopUpWindow("FXML/Funcionario/PopUp/pagarConsulta.fxml", "Pagar");
    }

    public void showDadosConsulta(){
        showPopUpWindow("FXML/Funcionario/PopUp/dadosConsulta.fxml", "Dados da consulta");
    }

    public void showDiag(){
        showPopUpWindow("FXML/Funcionario/PopUp/diag.fxml", "Diagnóstico");
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

    public AnchorPane getConsultaView() {
        if (consultaView == null) {
            try {
                consultaView = new FXMLLoader(getClass().getResource("FXML/Medico/consulta.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return consultaView;
    }

    public AnchorPane getHorarioView() {
        if (calendarioView == null) {
            try {
                calendarioView = new FXMLLoader(getClass().getResource("FXML/Medico/horario.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return calendarioView;
    }

    public AnchorPane getMeusPacientesView() {
        if (meusPacientesView == null) {
            try {
                meusPacientesView = new FXMLLoader(getClass().getResource("FXML/Medico/meusPacientes.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return meusPacientesView;
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
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Images/green_logo.png"))));
        stage.setScene(scene);
        stage.setTitle("TinyHearts");
        stage.show();
    }

    public void showAdminWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Admin/admin.fxml"));
        AdminController adminController = new AdminController();
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
        MedicoController medicoController = new MedicoController();
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
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Images/green_logo.png"))));
        stage.setTitle("TinyHearts");
        stage.setMaximized(true);
        stage.show();
    }

    public void showPopUpWindow(String fxmlPath, String title) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Images/green_logo.png"))));
        stage.setTitle(title);
        stage.show();
    }


    public void closeStage(Stage stage) {
        stage.close();
    }

}
