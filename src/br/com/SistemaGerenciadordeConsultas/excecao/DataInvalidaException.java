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
public class DataInvalidaException extends GerenciadorConsultasException{

    public DataInvalidaException() {
        super("Data inválida para Agendar Consulta! A data está no Passado");
    }
    
}
