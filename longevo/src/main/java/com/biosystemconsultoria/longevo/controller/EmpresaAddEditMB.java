
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

import java.util.List;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.biosystemconsultoria.longevo.model.Empresa;
import com.biosystemconsultoria.longevo.model.Endereco;
import com.biosystemconsultoria.longevo.model.Estado;
import com.biosystemconsultoria.longevo.model.PorteEmpresa;
import com.biosystemconsultoria.longevo.model.RamoAtividade;
import com.biosystemconsultoria.longevo.repository.Empresas;
import com.biosystemconsultoria.longevo.repository.Enderecos;
import com.biosystemconsultoria.longevo.service.CadastroEmpresaService;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;
import org.primefaces.event.SelectEvent;


@Named
@ViewScoped
public class EmpresaAddEditMB implements Serializable {
 
  private static final long serialVersionUID = 201311132355L;
    
    @Inject
    private CadastroEmpresaService empresaService;
    
    @Inject
    private Empresas empresaDAO;
    
    @Inject
    private Enderecos enderecoDAO;

    private Empresa empresa;
    
    private Endereco selectedEndereco;
    
    public EmpresaAddEditMB() {
	this.empresa = new Empresa();
    }    
    
    public Empresa getEmpresa() {
          return this.empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<Empresa> completarRazaoEmpresa(String razao) {
	return this.empresaDAO.porRazaoSocial(razao);
    }

    public void pesquisarCnpjEmpresa() {
        if (this.empresa.getCnpj() != null) {
            
            this.empresa = this.empresaDAO.porCnpj(this.empresa.getCnpj());
            
            if (this.empresa == null) {
                FacesUtil.addErrorMessage("Cnpj inexistente!");
            }    
        } else {
           FacesUtil.addErrorMessage("Cnpj precisa ser informado!");
        }   
    }

    public void saveOrUpdate() {
            this.empresa.removeEnderecoVazio();
            try {
            
                this.empresa = empresaService.salvar(this.empresa);
                this.selectedEndereco = new Endereco();
                this.empresa = new Empresa();
                FacesUtil.addInfoMessage("Empresa salvo com sucesso!");
                
            } finally {
                this.empresa.adicionarEnderecoVazio();
            }    
    }

    public void delete() {
	if (this.selectedEndereco != null) {
                
            this.enderecoDAO.delete(selectedEndereco);
            this.empresa.getEnderecos().remove(selectedEndereco);
            
            FacesUtil.addInfoMessage("Endereco " + this.selectedEndereco.getLogradouro()
				+ " excluído com sucesso.");
	}
    }    
    
    public Estado[] getEstado() {
	return Estado.values();
    }
    
    public PorteEmpresa[] getPorteEmpresa() {
        return PorteEmpresa.values();
    }
    
    public RamoAtividade[] getAtividade() {
        return RamoAtividade.values();
    }
    
    public Endereco getSelectedEndereco() {
        return this.selectedEndereco;
    }

    public void setSelectedEndereco(Endereco selectedEndereco) {
        this.selectedEndereco = selectedEndereco;
    }

    public void add(){
        this.empresa = new Empresa();
    }

    public void carregaEmpresa(SelectEvent event) {
        if (event.getObject() != null) {
            this.empresa = (Empresa) event.getObject();
        }    
    }
}