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
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.biosystemconsultoria.longevo.model.Empresa;
import com.biosystemconsultoria.longevo.model.PerfilAcesso;
import com.biosystemconsultoria.longevo.model.Usuario;
import com.biosystemconsultoria.longevo.repository.PerfilAcessos;
import com.biosystemconsultoria.longevo.repository.Empresas;
import com.biosystemconsultoria.longevo.service.CadastroUsuarioService;
import com.biosystemconsultoria.longevo.util.CriptografaSenhaUsuario;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;




@Named
@ViewScoped
public class UsuarioAddEditMB implements Serializable {

    @Inject
    private CadastroUsuarioService usuarioService;
    
    @Inject
    private PerfilAcessos perfilAcessoDAO;
    
    @Inject
    private Empresas empresaDAO;
    
    private Usuario usuario;

    private PerfilAcesso perfilAcesso;
    
    private List<PerfilAcesso> perfilAcessos;
    
    private List<Empresa> empresas;
    
    private Empresa empresa;
    
    private String title;
    
    
    public UsuarioAddEditMB() {
	this.usuario = new Usuario();
    }    
    
    public void inicializar(){
        if (getPerfilAcessos() == null) {
            carregaPerfilAcessos();
        } 
        if (getEmpresas() == null ){
            carregaEmpresas();
        } 
    }
    
    public Usuario getUsuario() {
          return usuario;
    }

    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void saveOrUpdate() throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String senha = this.usuario.getSenha();
        
        this.usuario.setSenha(CriptografaSenhaUsuario.execute(senha));
        
        this.usuario = usuarioService.salvar(this.usuario);
        this.usuario = new Usuario();
        FacesUtil.addInfoMessage("Usuario salvo com sucesso!");
    }

    public List<PerfilAcesso> getPerfilAcessos() {
        return perfilAcessos;
    }

    public void setPerfilAcessos(List<PerfilAcesso> perfilAcessos) {
        this.perfilAcessos = perfilAcessos;
    }
    
    public PerfilAcesso getPerfilAcesso() {
	return perfilAcesso;
    }
    
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }    

    private void carregaPerfilAcessos() {
        setPerfilAcessos(perfilAcessoDAO.listar());
    }

    private void carregaEmpresas() {
        setEmpresas(empresaDAO.listar());
    }

    public List<Empresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }
   
    public void add(){
        this.usuario = new Usuario();
    }    
    
}
