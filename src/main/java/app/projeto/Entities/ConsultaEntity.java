package app.projeto.Entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@NamedQuery(
        name = "ListaConsultas",
        query = "SELECT c FROM ConsultaEntity c WHERE c.dataConsulta BETWEEN :startOfDay AND :endOfDay"
)
@NamedQuery(
        name = "ListaConsultasUtente",
        query = "SELECT c FROM ConsultaEntity c WHERE c.utente = :utente ORDER BY c.data"
)
@jakarta.persistence.Table(name = "Consulta", schema = "dbo", catalog = "tinyhearts")
public class ConsultaEntity {
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

    @ManyToOne
    @JoinColumn(name = "IDFuncionario", nullable = false)
    private FuncionarioEntity funcionario;

    public FuncionarioEntity getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioEntity funcionario) {
        this.funcionario = funcionario;
    }

    @ManyToOne
    @JoinColumn(name = "IDUtente", nullable = false)
    private UtenteEntity utente;

    public UtenteEntity getUtente() {
        return utente;
    }

    public void setUtente(UtenteEntity utente) {
        this.utente = utente;
    }

    @Basic
    @Column(name = "DataConsulta")
    private Date dataConsulta;

    public Date getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(Date dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    @Basic
    @Column(name = "HoraConsulta")
    private Object horaConsulta;

    public Object getHoraConsulta() {
        return horaConsulta;
    }

    public void setHoraConsulta(Object horaConsulta) {
        this.horaConsulta = horaConsulta;
    }

    @Basic
    @Column(name = "IDTipo")
    private Integer idTipo;

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }

    @Basic
    @Column(name = "IDDiagnostico")
    private Integer idDiagnostico;

    public Integer getIdDiagnostico() {
        return idDiagnostico;
    }

    public void setIdDiagnostico(Integer idDiagnostico) {
        this.idDiagnostico = idDiagnostico;
    }

    @Basic
    @Column(name = "Estado")
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsultaEntity that = (ConsultaEntity) o;
        return id == that.id && funcionario.getId() == that.funcionario.getId() && utente.getId() == that.utente.getId() && Objects.equals(dataConsulta, that.dataConsulta) && Objects.equals(horaConsulta, that.horaConsulta) && Objects.equals(idTipo, that.idTipo) && Objects.equals(idDiagnostico, that.idDiagnostico) && Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, funcionario.getId(), utente.getId(), dataConsulta, horaConsulta, idTipo, idDiagnostico, estado);
    }
}
