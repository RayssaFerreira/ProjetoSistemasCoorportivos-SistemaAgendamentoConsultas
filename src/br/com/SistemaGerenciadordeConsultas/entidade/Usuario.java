/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SistemaGerenciadordeConsultas.entidade;

/**
 *
 * @author Rayssa
 */
public class Usuario {

    private int id;
    private String nome;
    private String login;
    private String senha;
    private String grupo_Usuario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGrupo_Usuario() {
        return grupo_Usuario;
    }

    public void setGrupo_Usuario(String grupo_Usuario) {
        this.grupo_Usuario = grupo_Usuario;
    }
    

}
