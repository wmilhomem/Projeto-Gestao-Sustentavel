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
public enum Mes {
   
  SELECIONE("Selecione"),   
  JANEIRO("Janeiro"), 
  FEVEREIRO("Fevereiro"), 
  MARÇO("Março"),
  ABRIL("Abril"),
  MAIO("Maio"),
  JUNHO("Junho"),
  JULHO("Julho"),
  AGOSTO("Agosto"),
  SETEMBRO("Setembro"),
  OUTUBRO("Outubro"),
  NOVEMBRO("Novembro"),
  DEZEMBRO("Dezembro");
 
   private String descricao;
	
   Mes(String descricao) {
	this.descricao = descricao;
   }

    public String getDescricao() {
	return descricao;
    }

}
