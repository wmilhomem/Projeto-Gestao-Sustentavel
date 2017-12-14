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

import com.biosystemconsultoria.longevo.model.Usuario;
import com.biosystemconsultoria.longevo.repository.Usuarios;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;

import org.primefaces.event.SelectEvent;


/**
 *
 * @author Wendell
 */

@Named
@ViewScoped
public class UsuarioMB implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private Usuarios usuarioDAO;
  
    private String filtro;
    
    private List<Usuario> usuarios;
    
    private Usuario selectedUsuario;
      
    public UsuarioMB(){
    }

    public void onLoad() {
	this.usuarios = this.usuarioDAO.listar();
    }
      
    public void pesquisar(){
        this.usuarios = this.usuarioDAO.ilike(getFiltro());
    }
    
    public List<Usuario> getUsuarios() {
        return this.usuarios;
    }

     public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void delete(Usuario user) {
            this.usuarioDAO.delete(user);
            this.usuarios.remove(user);
            
            FacesUtil.addInfoMessage("Usuario : " + user.getNome()
				+ " excluído com sucesso.");
    }
   
    public void selectUsuario(SelectEvent evt) {
	try {
		if (evt.getObject() != null) {
			this.selectedUsuario = (Usuario) evt.getObject();
		} else {
			this.selectedUsuario = null;
       		}
	} catch (Exception e) {
		this.selectedUsuario = null;
	}
    }
    
    
    public void unselectUsuario() {
	this.selectedUsuario = null;
    }
    
    public Usuario getSelectedUsuario() {
		return this.selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        	this.selectedUsuario = selectedUsuario;
    }
        
    
}
