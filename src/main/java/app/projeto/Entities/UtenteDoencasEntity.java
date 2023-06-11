package app.projeto.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "UtenteDoencas", schema = "dbo", catalog = "tinyhearts")
public class UtenteDoencasEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @jakarta.persistence.Column(name = "UtenteID")
    private int utenteId;

    public int getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UtenteDoencasEntity that = (UtenteDoencasEntity) o;
        return utenteId == that.utenteId && doencaId == that.doencaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(utenteId, doencaId);
    }
}
