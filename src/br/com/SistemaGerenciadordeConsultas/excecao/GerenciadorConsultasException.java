/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SistemaGerenciadordeConsultas.excecao;

/**
 *
 * @author Rayssa
 */
public class GerenciadorConsultasException extends RuntimeException{
    public GerenciadorConsultasException(String mensagem){
        super(mensagem);
    }
    
    
}
