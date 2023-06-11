package app.projeto.Entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "TipoFuncionario", schema = "dbo", catalog = "tinyhearts")
public class TipoFuncionarioEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @jakarta.persistence.Column(name = "TipoID")
    private int tipoId;

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    @Basic
    @Column(name = "Cargo")
    private String cargo;

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoFuncionarioEntity that = (TipoFuncionarioEntity) o;
        return tipoId == that.tipoId && Objects.equals(cargo, that.cargo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipoId, cargo);
    }
}
