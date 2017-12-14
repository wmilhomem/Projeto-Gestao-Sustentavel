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
public enum Unidade {
    
    BL("Bloco"),
    CX("Caixa"),
    FL("Folha"),
    HR("Hora"),
    JG("Jogo"),
    KG("Quilograma"),
    KW("Quilo Watt hora"),
    LT("Litro"),
    MM("Milimetro"),
    MT("Metro"),
    M3("Metro Cubico"),
    M2("Metro Quadrado"),
    MW("Mega Watt hora"),
    RL("Real"),
    TN("Tonelada"),
    UD("Unidade"),
    UN("Unitário");
    
    private String descricao;
	
    Unidade (String descricao) {
	this.descricao = descricao;
    }

    public String getDescricao() {
	return descricao;
    }
    
}
