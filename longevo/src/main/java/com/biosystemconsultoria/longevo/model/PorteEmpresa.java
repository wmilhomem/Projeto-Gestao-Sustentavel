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
public enum PorteEmpresa {
    
    MEI("Micro Empreendedor Individual"),
    EPP("Empresa Pequeno Porte"),
    ME("Micro Empresa"),
    PME("Média Empresa"),
    GE("Grande Empresa");
    
    private String descricao;
	
    PorteEmpresa(String descricao) {
	this.descricao = descricao;
    }

    public String getDescricao() {
	return descricao;
    }
    
}
