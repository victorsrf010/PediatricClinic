package app.projeto;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {

        Model.getInstance().getViewFactory().showLoginWindow();

    }

    public static void main(String[] args) {
        launch();
    }
}