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

import com.biosystemconsultoria.longevo.model.PerfilAcesso;
import com.biosystemconsultoria.longevo.repository.PerfilAcessos;
import com.biosystemconsultoria.longevo.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = PerfilAcesso.class)
public class PerfilAcessoConverter implements Converter {
    
    private PerfilAcessos perfilAcessos;
    
    public PerfilAcessoConverter(){
        perfilAcessos = CDIServiceLocator.getBean(PerfilAcessos.class);
    }
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
		PerfilAcesso retorno = null;
		
		if (value != null) {
			Long id = new Long(value);
			retorno = perfilAcessos.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			PerfilAcesso perfilAcesso = (PerfilAcesso) value;
			return perfilAcesso.getId() == null ? null : perfilAcesso.getId().toString();
		}
		
		return "";
	}
}
