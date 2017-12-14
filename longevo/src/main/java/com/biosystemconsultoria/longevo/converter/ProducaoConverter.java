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

import com.biosystemconsultoria.longevo.model.Producao;
import com.biosystemconsultoria.longevo.repository.Producoes;
import com.biosystemconsultoria.longevo.util.cdi.CDIServiceLocator;


@FacesConverter(forClass = Producao.class)
public class ProducaoConverter implements Converter {
    
    private Producoes producoes;
    
     public ProducaoConverter(){
       producoes = CDIServiceLocator.getBean(Producoes.class);
    }
     
   @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Producao retorno = null;
		
		if (value != null) {
			Long id = new Long(value);
			retorno = producoes.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Producao producao = (Producao) value;
			return producao.getId() == null ? null : producao.getId().toString();
		}
		
		return "";
	}     
    
    
}
