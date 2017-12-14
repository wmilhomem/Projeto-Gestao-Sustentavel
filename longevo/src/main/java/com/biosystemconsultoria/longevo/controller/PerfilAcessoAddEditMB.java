/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.controller;


/**
 *
 * @author Wendell
 */

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.biosystemconsultoria.longevo.model.Autorizacao;
import com.biosystemconsultoria.longevo.model.PerfilAcesso;
import com.biosystemconsultoria.longevo.service.CadastroPerfilAcessoService;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PerfilAcessoAddEditMB implements Serializable {
    
    private static final long serialVersionUID = 201311132355L;
    
    @Inject
    private CadastroPerfilAcessoService perfilAcessoService;
    
    @Inject
    private PerfilAcesso perfilAcesso;
    
    private String title;
    
    public PerfilAcessoAddEditMB() {
	this.perfilAcesso = new PerfilAcesso();
    }    
    
    public PerfilAcesso getPerfilAcesso() {
        return perfilAcesso;
    }

    
    public void setPerfilAcesso(PerfilAcesso perfilAcesso) {
        this.perfilAcesso = perfilAcesso;
    }

     
    public void saveOrUpdate() {
        this.perfilAcesso = perfilAcessoService.salvar(this.perfilAcesso);
        this.perfilAcesso = new  PerfilAcesso();
        FacesUtil.addInfoMessage("Perfil Acesso salvo com sucesso!");
    }

    public Autorizacao[] getAutorizacao() {
	return Autorizacao.values();
    }
    
    public void add() {
        this.perfilAcesso = new PerfilAcesso();
    }
    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }    

        
}
