package app.projeto.Entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Utente", schema = "dbo", catalog = "tinyhearts")
public class UtenteEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NIF")
    private String nif;

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    @Basic
    @Column(name = "Nome")
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Basic
    @Column(name = "Email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "DataNascimento")
    private Date dataNascimento;

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Basic
    @Column(name = "NomeRepresentanteLegal")
    private String nomeRepresentanteLegal;

    public String getNomeRepresentanteLegal() {
        return nomeRepresentanteLegal;
    }

    public void setNomeRepresentanteLegal(String nomeRepresentanteLegal) {
        this.nomeRepresentanteLegal = nomeRepresentanteLegal;
    }

    @Basic
    @Column(name = "Sexo")
    private String sexo;

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Basic
    @Column(name = "ContactoRepresentanteLegal")
    private String contactoRepresentanteLegal;

    public String getContactoRepresentanteLegal() {
        return contactoRepresentanteLegal;
    }

    public void setContactoRepresentanteLegal(String contactoRepresentanteLegal) {
        this.contactoRepresentanteLegal = contactoRepresentanteLegal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UtenteEntity that = (UtenteEntity) o;
        return id == that.id && Objects.equals(nif, that.nif) && Objects.equals(nome, that.nome) && Objects.equals(email, that.email) && Objects.equals(dataNascimento, that.dataNascimento) && Objects.equals(nomeRepresentanteLegal, that.nomeRepresentanteLegal) && Objects.equals(sexo, that.sexo) && Objects.equals(contactoRepresentanteLegal, that.contactoRepresentanteLegal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nif, nome, email, dataNascimento, nomeRepresentanteLegal, sexo, contactoRepresentanteLegal);
    }

    @Override
    public String toString() {
        return nome;
    }

}

