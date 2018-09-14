package br.edu.ifsp.sdm.manhani.listacontatossdm.model;

import java.io.Serializable;

public class Contato implements Serializable {
    private String nome;
    private String endereco;
    private String telefone;
    private String email;

    public Contato(String nome, String endereco, String telefone, String email) {
        this.setNome(nome);
        this.setEndereco(endereco);
        this.setTelefone(telefone);
        this.setEmail(email);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}