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
public enum Etapa {
    
    COMERCIALIZACAO("Comercialização"),
    EXECUCAOSSERVICOS("Execução/Serviços"),
    PRODUCAO("Produção"),
    RECEBIMENTO("Recebimento/Armazenamento"),
    EXPEDICAO("Expedição/Entrega"),
    USOGERAL("Uso Geral");
    
       
    private String descricao;
	
    Etapa(String descricao) {
	this.descricao = descricao;
    }

    public String getDescricao() {
	return descricao;
    }
}
