/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.util;

/**
 *
 * @author Wendell
 */

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.LinkedHashMap;

import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.enterprise.context.SessionScoped;
import org.primefaces.component.selectonemenu.SelectOneMenu;

import com.biosystemconsultoria.longevo.model.Empresa;
import com.biosystemconsultoria.longevo.model.RamoAtividade;
import com.biosystemconsultoria.longevo.model.Usuario;
import com.biosystemconsultoria.longevo.repository.Usuarios;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;
import java.io.IOException;
import javax.faces.FacesException;



@Named
@SessionScoped
public class ConfigSettingsMB implements Serializable {

	private static final long serialVersionUID = 201405150723L;

        @Inject
        private Usuarios usuarios;

	private String localeCode;
        
        private Usuario logado;
        
        private Empresa userEmpresa;
        
	private static Map<String, Locale> countries;

	static {
            countries = new LinkedHashMap<String, Locale>();
            countries.put("English", new Locale("en"));
            countries.put("Português", new Locale("pt"));
	}

	public ConfigSettingsMB() {
	}

	public Map<String, Locale> getCountries() {
		return countries;
	}

	public String getLocaleCode() {
            
            if ((this.localeCode == null) && (FacesContext.getCurrentInstance().getViewRoot() != null) &&
                    (FacesContext.getCurrentInstance().getViewRoot().getLocale() != null)) {
                this.localeCode = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
            }
            if (this.localeCode == null) {
                this.localeCode = "pt";
            }
            if (this.logado == null){
                setUsuarioEmpresa();
            }
            return this.localeCode;  
          
            
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public void localeCodeChanged(AjaxBehaviorEvent e) {
		String newLocaleValue = ((SelectOneMenu)e.getSource()).getValue() + "";

		for (Entry<String, Locale> entry : countries.entrySet()) {
			if (entry.getValue().toString().equals(newLocaleValue)) {
				FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
			}
		}
	}

        public Usuario getLogado() {
            return logado;
        }

        public void setLogado(Usuario logado) {
            this.logado = logado;
        }

        public void setUsuarioEmpresa() {
            this.logado      = usuarios.porUsername(FacesUtil.getUsuarioSessao());
            this.userEmpresa = logado.getEmpresa();
        }
    
        public Empresa getUserEmpresa() {
            return userEmpresa;
        }
    
        public void setUserEmpresa(Empresa userEmpresa) {
            this.userEmpresa = userEmpresa;
        }

        

}