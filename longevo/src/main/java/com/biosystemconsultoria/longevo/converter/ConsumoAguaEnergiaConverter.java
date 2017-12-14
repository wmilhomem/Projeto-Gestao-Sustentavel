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

import com.biosystemconsultoria.longevo.model.ConsumoAguaEnergia;
import com.biosystemconsultoria.longevo.repository.ConsumosAguaEnergia;
import com.biosystemconsultoria.longevo.util.cdi.CDIServiceLocator;


@FacesConverter(forClass = ConsumoAguaEnergia.class)
public class ConsumoAguaEnergiaConverter implements Converter {
    
    private ConsumosAguaEnergia consumosAguaEnergia;
    
     public ConsumoAguaEnergiaConverter(){
       consumosAguaEnergia = CDIServiceLocator.getBean(ConsumosAguaEnergia.class);
    }
     
   @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
		ConsumoAguaEnergia retorno = null;
		
		if (value != null) {
			Long id = new Long(value);
			retorno = consumosAguaEnergia.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			ConsumoAguaEnergia consumo = (ConsumoAguaEnergia) value;
			return consumo.getId() == null ? null : consumo.getId().toString();
		}
		
		return "";
	}     
    
    
}
