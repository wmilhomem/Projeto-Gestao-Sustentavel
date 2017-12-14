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
public enum DestinoResiduo {
    
    COMPOSTAGEM("Compostagem"),
    COOPERATIVA("Cooperativa"),
    RECICLAGEM("Empresa Reciclagem"),
    ESPECILIZADA("Empresa especializada"),
    COLETA("Coleta Pública"),
    VENDA("Venda");
    
     private String descricao;
	
    DestinoResiduo(String descricao) {
	this.descricao = descricao;
    }

    public String getDescricao() {
	return descricao;
    }
    
}
