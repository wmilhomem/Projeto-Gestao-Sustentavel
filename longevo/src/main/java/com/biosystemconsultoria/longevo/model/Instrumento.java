/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.model;

/**
 *
 * @author Wendell
 */
public enum Instrumento {
    
    CONSULTORIA("Consultoria"),
    CURSO("Curso"),
    EMPRESAATENDIDA("Empresa Atendida"),
    ORIENTACAO("Orientação"),
    PALESTRAOFICINASEMINARIO("Palestra/Oficina/Seminário");
        
       
    private String descricao;
	
    Instrumento(String descricao) {
	this.descricao = descricao;
    }

    public String getDescricao() {
	return descricao;
    }
}