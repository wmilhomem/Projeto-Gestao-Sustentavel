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
public enum AspectoTipo {
    
    AMBIENTAL("Ambiental"),
    SOCIAL("Social"),
    ECONOMICO("Econômico");
    
    private String descricao;
	
    AspectoTipo(String descricao) {
	this.descricao = descricao;
    }

    public String getDescricao() {
	return descricao;
    }
    
}
