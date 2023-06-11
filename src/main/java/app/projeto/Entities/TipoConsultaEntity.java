package app.projeto.Entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "TipoConsulta", schema = "dbo", catalog = "tinyhearts")
public class TipoConsultaEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @jakarta.persistence.Column(name = "IDTipoConsulta")
    private int idTipoConsulta;

    public int getIdTipoConsulta() {
        return idTipoConsulta;
    }

    public void setIdTipoConsulta(int idTipoConsulta) {
        this.idTipoConsulta = idTipoConsulta;
    }

    @Basic
    @Column(name = "Tipo")
    private String tipo;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoConsultaEntity that = (TipoConsultaEntity) o;
        return idTipoConsulta == that.idTipoConsulta && Objects.equals(tipo, that.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTipoConsulta, tipo);
    }
}
