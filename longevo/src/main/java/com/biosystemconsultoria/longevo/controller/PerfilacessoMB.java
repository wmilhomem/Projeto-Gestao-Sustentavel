/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.controller;

import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.biosystemconsultoria.longevo.model.PerfilAcesso;
import com.biosystemconsultoria.longevo.repository.PerfilAcessos;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;

import org.primefaces.event.SelectEvent;

/**
 *
 * @author Wendell
 */

@Named
@ViewScoped
public class PerfilacessoMB implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private PerfilAcessos perfilacessoDAO;
    
    private String filtro;
    
    private List<PerfilAcesso> perfilacessos;
    
    private PerfilAcesso selectedPerfilAcesso;
      
    public PerfilacessoMB(){
    }

    public void onLoad() {
	this.perfilacessos = this.perfilacessoDAO.listar();
    }
      
    public void pesquisar(){
        this.perfilacessos = this.perfilacessoDAO.ilike(getFiltro());
    }
    
    public List<PerfilAcesso> getPerfilacessos() {
        return this.perfilacessos;
    }

     public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void delete(PerfilAcesso perfil) {
            this.perfilacessoDAO.delete(perfil);
            this.perfilacessos.remove(perfil);
            
            FacesUtil.addInfoMessage("Perfil Acesso " + perfil.getDescricao()
				+ " excluído com sucesso.");
    }
   
    public void selectPerfilAcesso(SelectEvent evt) {
	try {
		if (evt.getObject() != null) {
			this.selectedPerfilAcesso = (PerfilAcesso) evt.getObject();
		} else {
			this.selectedPerfilAcesso = null;
       		}
	} catch (Exception e) {
		this.selectedPerfilAcesso = null;
	}
    }
    
    
    public void unselectPerfilAcesso() {
	this.selectedPerfilAcesso = null;
    }
    
    public PerfilAcesso getSelectedPerfilAcesso() {
		return this.selectedPerfilAcesso;
    }

    public void setSelectedPerfilAcesso(PerfilAcesso selectedPerfilAcesso) {
        	this.selectedPerfilAcesso = selectedPerfilAcesso;
    }
    
}
