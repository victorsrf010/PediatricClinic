package app.projeto.Entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "TipoPagamento", schema = "dbo", catalog = "tinyhearts")
public class TipoPagamentoEntity {
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
    @Column(name = "Metodo")
    private String metodo;

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoPagamentoEntity that = (TipoPagamentoEntity) o;
        return tipoId == that.tipoId && Objects.equals(metodo, that.metodo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipoId, metodo);
    }

    @Override
    public String toString() {
        return metodo;
    }
}
