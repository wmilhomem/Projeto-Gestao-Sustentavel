/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.converter;

/**
 *
 * @author Wendell
 */

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.biosystemconsultoria.longevo.model.Aquisicao;
import com.biosystemconsultoria.longevo.repository.Aquisicoes;
import com.biosystemconsultoria.longevo.util.cdi.CDIServiceLocator;


@FacesConverter(forClass = Aquisicao.class)
public class AquisicaoConverter implements Converter {
    
    private Aquisicoes aquisicoes;
    
     public AquisicaoConverter(){
       aquisicoes = CDIServiceLocator.getBean(Aquisicoes.class);
    }
     
   @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Aquisicao retorno = null;
		
		if (value != null) {
			Long id = new Long(value);
			retorno = aquisicoes.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Aquisicao aquisicao = (Aquisicao) value;
			return aquisicao.getId() == null ? null : aquisicao.getId().toString();
		}
		
		return "";
	}     
    
    
}
