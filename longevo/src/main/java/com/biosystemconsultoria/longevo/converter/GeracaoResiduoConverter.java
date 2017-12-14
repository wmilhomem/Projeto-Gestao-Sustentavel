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

import com.biosystemconsultoria.longevo.model.GeracaoResiduo;
import com.biosystemconsultoria.longevo.repository.GeracaoResiduos;
import com.biosystemconsultoria.longevo.util.cdi.CDIServiceLocator;


@FacesConverter(forClass = GeracaoResiduo.class)
public class GeracaoResiduoConverter implements Converter {
    
    private GeracaoResiduos geracaoResiduos;
    
    public GeracaoResiduoConverter(){
        geracaoResiduos = CDIServiceLocator.getBean(GeracaoResiduos.class);
    }
    
        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
		GeracaoResiduo retorno = null;
		
		if (value != null) {
			Long id = new Long(value);
			retorno = geracaoResiduos.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			GeracaoResiduo geracaoResiduo = (GeracaoResiduo) value;
			return geracaoResiduo.getId() == null ? null : geracaoResiduo.getId().toString();
		}
		
		return "";
	}
}
