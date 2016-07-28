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
public class CpfMedicoExisteException extends GerenciadorConsultasException{

    public CpfMedicoExisteException() {
         super("Médico já cadastrado com esse CPF!");
    }

    
}
