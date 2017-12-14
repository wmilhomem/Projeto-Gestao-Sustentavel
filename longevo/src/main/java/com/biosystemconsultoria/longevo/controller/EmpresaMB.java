/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.biosystemconsultoria.longevo.model.Empresa;
import com.biosystemconsultoria.longevo.model.Estado;
import com.biosystemconsultoria.longevo.model.PorteEmpresa;
import com.biosystemconsultoria.longevo.repository.Empresas;
import com.biosystemconsultoria.longevo.repository.filter.EmpresaFilter;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Wendell
 */

@Named
@ViewScoped
public class EmpresaMB implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private Empresas empresaDAO;
    
    private EmpresaFilter filtro;
    
    private LazyDataModel<Empresa> empresas;
    
    private Empresa selectedEmpresa;
    
    public EmpresaMB(){
        filtro = new EmpresaFilter();
    }

      
    public void pesquisar(){
        
        empresas = new LazyDataModel<Empresa>() {
                
                private static final long serialVersionUID = 1L;
               
                @Override
            	public List<Empresa> load(int first, int pageSize,
			String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
				
				filtro.getPaginacaoLazy().setPrimeiroRegistro(first);
				filtro.getPaginacaoLazy().setQuantidadeRegistros(pageSize);
				filtro.getPaginacaoLazy().setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.getPaginacaoLazy().setPropriedadeOrdenacao(sortField);
				
				setRowCount(empresaDAO.quantidadeFiltrados(filtro));
				
				return empresaDAO.filtrados(filtro);
		}
        };        
    }
    
    public LazyDataModel<Empresa> getEmpresas() {
        return this.empresas;
    }

     public EmpresaFilter getFiltro() {
        return filtro;
    }

    public void setFiltro(EmpresaFilter filtro) {
        this.filtro = filtro;
    }
    
    public PorteEmpresa[] getPorte() {
        return PorteEmpresa.values();
    }
    
    public Estado[] getUf() {
	return Estado.values();
    }

    public void delete(Empresa empresa) {
            this.empresaDAO.delete(empresa);
           // this.empresas.remove(empresa);
            
            FacesUtil.addInfoMessage("Empresa : " + empresa.getRazaoSocial()
				+ " excluída com sucesso.");
    }
   
    public void selectEmpresa(SelectEvent evt) {
	try {
		if (evt.getObject() != null) {
			this.selectedEmpresa = (Empresa) evt.getObject();
		} else {
			this.selectedEmpresa = null;
       		}
	} catch (Exception e) {
		this.selectedEmpresa = null;
	}
    }
    
    
    public void unselectEmpresa() {
	this.selectedEmpresa = null;
    }
    
    public Empresa getSelectedEmpresa() {
	return this.selectedEmpresa;
    }

    public void setSelectedEmpresa(Empresa selectedEmpresa) {
        this.selectedEmpresa = selectedEmpresa;
    }
        
   
}
