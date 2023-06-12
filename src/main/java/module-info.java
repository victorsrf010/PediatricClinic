module app.projeto {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    requires transitive javafx.graphics;
    requires com.calendarfx.view;


    opens app.projeto to javafx.fxml;
    opens app.projeto.Entities to javafx.base, org.hibernate.orm.core;
    opens app.projeto.Controllers to javafx.fxml;
    opens app.projeto.Controllers.Funcionario to javafx.fxml;
    opens app.projeto.Controllers.Funcionario.PopUp   to javafx.fxml;
    opens app.projeto.Controllers.Admin to javafx.fxml;
    opens app.projeto.Controllers.Medico to javafx.fxml;
    exports app.projeto;
    exports app.projeto.Controllers;
    exports app.projeto.Entities;

}