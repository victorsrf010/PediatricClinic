package app.projeto.Entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "Pagamento", schema = "dbo", catalog = "tinyhearts")
public class PagamentoEntity {
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
    @Column(name = "Valor")
    private BigDecimal valor;

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Basic
    @Column(name = "TipoID")
    private Integer tipoId;

    public Integer getTipoId() {
        return tipoId;
    }

    public void setTipoId(Integer tipoId) {
        this.tipoId = tipoId;
    }

    @Basic
    @Column(name = "DataPagamento")
    private Date dataPagamento;

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
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

    @ManyToOne
    @JoinColumn(name="IDConsulta")
    private ConsultaEntity consulta;

    public ConsultaEntity getConsulta() {
        return consulta;
    }

    public void setConsulta(ConsultaEntity consulta) {
        this.consulta = consulta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagamentoEntity that = (PagamentoEntity) o;
        return id == that.id && Objects.equals(valor, that.valor) && Objects.equals(tipoId, that.tipoId) && Objects.equals(dataPagamento, that.dataPagamento) && Objects.equals(consulta.getId(), that.consulta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valor, tipoId, dataPagamento, consulta.getId());
    }
}
