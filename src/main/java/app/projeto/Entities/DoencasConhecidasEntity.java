package app.projeto.Entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "DoencasConhecidas", schema = "dbo", catalog = "tinyhearts")
public class DoencasConhecidasEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @jakarta.persistence.Column(name = "DoencaID")
    private int doencaId;

    public int getDoencaId() {
        return doencaId;
    }

    public void setDoencaId(int doencaId) {
        this.doencaId = doencaId;
    }

    @Basic
    @Column(name = "NomeDoenca")
    private String nomeDoenca;

    public String getNomeDoenca() {
        return nomeDoenca;
    }

    public void setNomeDoenca(String nomeDoenca) {
        this.nomeDoenca = nomeDoenca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoencasConhecidasEntity that = (DoencasConhecidasEntity) o;
        return doencaId == that.doencaId && Objects.equals(nomeDoenca, that.nomeDoenca);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doencaId, nomeDoenca);
    }
}
