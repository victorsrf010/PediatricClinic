package app.projeto.Entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "Diagnostico", schema = "dbo", catalog = "tinyhearts")
public class DiagnosticoEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @jakarta.persistence.Column(name = "ID")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "IDUtente")
    private Integer idUtente;

    public Integer getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
    }

    @Basic
    @Column(name = "IDFuncionario")
    private Integer idFuncionario;

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    @Basic
    @Column(name = "Sintomas")
    private String sintomas;

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    @Basic
    @Column(name = "Tratamento")
    private String tratamento;

    public String getTratamento() {
        return tratamento;
    }

    public void setTratamento(String tratamento) {
        this.tratamento = tratamento;
    }

    @Basic
    @Column(name = "DataDiagnostico")
    private Date dataDiagnostico;

    public Date getDataDiagnostico() {
        return dataDiagnostico;
    }

    public void setDataDiagnostico(Date dataDiagnostico) {
        this.dataDiagnostico = dataDiagnostico;
    }

    @Basic
    @Column(name = "Observacoes")
    private String observacoes;

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Basic
    @Column(name = "IDConsulta")
    private Integer idConsulta;

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiagnosticoEntity that = (DiagnosticoEntity) o;
        return id == that.id && Objects.equals(idUtente, that.idUtente) && Objects.equals(idFuncionario, that.idFuncionario) && Objects.equals(sintomas, that.sintomas) && Objects.equals(tratamento, that.tratamento) && Objects.equals(dataDiagnostico, that.dataDiagnostico) && Objects.equals(observacoes, that.observacoes) && Objects.equals(idConsulta, that.idConsulta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUtente, idFuncionario, sintomas, tratamento, dataDiagnostico, observacoes, idConsulta);
    }

    @Override
    public String toString() {
        return dataDiagnostico.toString();
    }

}
