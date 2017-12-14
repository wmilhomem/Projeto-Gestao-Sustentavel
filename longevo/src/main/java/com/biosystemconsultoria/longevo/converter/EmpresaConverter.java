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

import com.biosystemconsultoria.longevo.model.Empresa;
import com.biosystemconsultoria.longevo.repository.Empresas;
import com.biosystemconsultoria.longevo.util.cdi.CDIServiceLocator;


@FacesConverter(forClass = Empresa.class)
public class EmpresaConverter implements Converter {
    
    private Empresas empresas;
    
    public EmpresaConverter(){
        empresas = CDIServiceLocator.getBean(Empresas.class);
    }
    
        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Empresa retorno = null;
		
		if (value != null) {
			Long id = new Long(value);
			retorno = empresas.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Empresa empresa = (Empresa) value;
			return empresa.getId() == null ? null : empresa.getId().toString();
		}
		
		return "";
	}
}
