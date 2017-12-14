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
public enum Autorizacao {

        ROLE_ADMIN("Administrador"), 
	ROLE_USER("Usuario padrão"), 
	ROLE_MANAGER("Usuario avançado");
	
	private String descricao;
	
	Autorizacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}