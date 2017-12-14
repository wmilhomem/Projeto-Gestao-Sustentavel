/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.controller;

import java.io.Serializable;

/**
 *
 * @author Wendell
 */

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.RoundingMode;

import com.biosystemconsultoria.longevo.model.Aquisicao;
import com.biosystemconsultoria.longevo.model.Etapa;
import com.biosystemconsultoria.longevo.model.Mes;
import com.biosystemconsultoria.longevo.model.Unidade;
import com.biosystemconsultoria.longevo.repository.Aquisicoes;
import com.biosystemconsultoria.longevo.repository.filter.AquisicaoFilter;
import com.biosystemconsultoria.longevo.service.CadastroAquisicaoService;
import com.biosystemconsultoria.longevo.util.ConfigSettingsMB;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named
@ViewScoped
public class AquisicaoLancamentoMB implements Serializable {
    
    private static final long serialVersionUID = 1L;
     
    @Inject
    private Aquisicoes aquisicaoDAO;
    
    @Inject
    private CadastroAquisicaoService aquisicaoService;

    @Inject
    private Aquisicao selectedAquisicao;

    @Inject
    private ConfigSettingsMB config;
    
    private AquisicaoFilter filtro;
    
    private LazyDataModel<Aquisicao> aquisicoesLancadas;

    public AquisicaoLancamentoMB(){
       filtro = new AquisicaoFilter();
    }
    
    public void pesquisar() {
        buscaLancamentos();
    }
    
    public Etapa[] getEtapa() {
	return Etapa.values();
    }    

    public Unidade[] getUnidade() {
	return Unidade.values();
    }    
    
    public Mes[] getMes() {
        return Mes.values();
    }
    
    public Date getDataHoje() {
	return new Date();
    }
    
    public AquisicaoFilter getFiltro() {
        return this.filtro;
    }

    public LazyDataModel<Aquisicao> getAquisicoesLancadas() {
        return aquisicoesLancadas;
    }

    public Aquisicao getSelectedAquisicao() {
        return selectedAquisicao;
    }

    public void buscaLancamentos() {
       this.filtro.setEmpresa(config.getUserEmpresa());
       
       if (this.selectedAquisicao.getMes() != null && !this.selectedAquisicao.getMes().equals(Mes.SELECIONE) ) {
           this.filtro.setMes(this.selectedAquisicao.getMes());
       } else {
           this.filtro.setMes(null);
       }
       
        aquisicoesLancadas = new LazyDataModel<Aquisicao>() {
                
                private static final long serialVersionUID = 1L;
               
                @Override
            	public List<Aquisicao> load(int first, int pageSize,
			String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
				
				filtro.getPaginacaoLazy().setPrimeiroRegistro(first);
				filtro.getPaginacaoLazy().setQuantidadeRegistros(pageSize);
				filtro.getPaginacaoLazy().setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.getPaginacaoLazy().setPropriedadeOrdenacao(sortField);
				
				setRowCount(aquisicaoDAO.quantidadeFiltrados(filtro));
				
				return aquisicaoDAO.filtrados(filtro);
		}
        };        
       
    }
    
    public void setSelectedAquisicao(Aquisicao selectedAquisicao) {
        this.selectedAquisicao = selectedAquisicao;
    }
    
    public void saveOrUpdate() {
        calculaValorUnitario();
        this.selectedAquisicao.setLog(config.getLogado().getUsername());
        this.selectedAquisicao.setEmpresaId(config.getUserEmpresa().getId());
        this.selectedAquisicao = aquisicaoService.salvar(selectedAquisicao);
        this.selectedAquisicao = new Aquisicao();
        buscaLancamentos();
        FacesUtil.addInfoMessage("Lancamento salvo com sucesso!");
    }    
    
    public void delete(Aquisicao aquisicao) {
            this.aquisicaoDAO.delete(aquisicao);
            
            FacesUtil.addInfoMessage("Lancamento " + aquisicao.getDocumentoref() + " excluído com sucesso.");
    } 
    
    public void add() {
        this.selectedAquisicao = new Aquisicao();
    }

    public void calculaValorUnitario() {
        if (this.selectedAquisicao != null) {
            this.selectedAquisicao.setValorUnitario(this.selectedAquisicao.getTotal().divide(this.selectedAquisicao.getQuantidade(),2,RoundingMode.UP));
        }
    }

    public ConfigSettingsMB getConfig() {
        return config;
    }

    public void setConfig(ConfigSettingsMB config) {
        this.config = config;
    }

    public void carregaMes() {
        this.filtro.setMes(this.selectedAquisicao.getMes());
    }
    
    public void carregaDocumento() {
        this.filtro.setDocumento(this.selectedAquisicao.getDocumentoref());
    }
    
    public void carregaDescricao() {
        this.filtro.setDescricao(this.selectedAquisicao.getDescricao());
    }
    
    
    public void carregaAno()  {
        SimpleDateFormat ano = new SimpleDateFormat("yyyy");  
        DateFormat       mes = new SimpleDateFormat("MM");
        Mes[] values = Mes.values();
     
        this.filtro.setAno(this.selectedAquisicao.getAno());
        this.selectedAquisicao.setMes(Mes.valueOf(values[Integer.parseInt(mes.format(new Date().getTime()).toUpperCase())].getDescricao().toUpperCase()));
    }
    
     
}
