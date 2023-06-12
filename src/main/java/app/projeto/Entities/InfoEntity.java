package app.projeto.Entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Info", schema = "dbo", catalog = "tinyhearts")
public class InfoEntity {
    @Basic
    @Column(name = "atendimentos")
    private Integer atendimentos;
    @Basic
    @Column(name = "pacientesPresentes")
    private Integer pacientesPresentes;
    @Basic
    @Column(name = "pacientesEmEspera")
    private Integer pacientesEmEspera;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    public Integer getAtendimentos() {
        return atendimentos;
    }

    public void setAtendimentos(Integer atendimentos) {
        this.atendimentos = atendimentos;
    }

    public Integer getPacientesPresentes() {
        return pacientesPresentes;
    }

    public void setPacientesPresentes(Integer pacientesPresentes) {
        this.pacientesPresentes = pacientesPresentes;
    }

    public Integer getPacientesEmEspera() {
        return pacientesEmEspera;
    }

    public void setPacientesEmEspera(Integer pacientesEmEspera) {
        this.pacientesEmEspera = pacientesEmEspera;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfoEntity that = (InfoEntity) o;
        return id == that.id && Objects.equals(atendimentos, that.atendimentos) && Objects.equals(pacientesPresentes, that.pacientesPresentes) && Objects.equals(pacientesEmEspera, that.pacientesEmEspera);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atendimentos, pacientesPresentes, pacientesEmEspera, id);
    }

}
