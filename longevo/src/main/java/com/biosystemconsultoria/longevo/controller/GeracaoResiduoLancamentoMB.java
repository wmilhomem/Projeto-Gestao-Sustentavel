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

import com.biosystemconsultoria.longevo.model.Aquisicao;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import com.biosystemconsultoria.longevo.model.GeracaoResiduo;
import com.biosystemconsultoria.longevo.model.Mes;
import com.biosystemconsultoria.longevo.model.DestinoResiduo;
import com.biosystemconsultoria.longevo.model.ResiduoTipo;
import com.biosystemconsultoria.longevo.model.Unidade;
import com.biosystemconsultoria.longevo.model.AspectoTipo;
import com.biosystemconsultoria.longevo.model.Etapa;
import com.biosystemconsultoria.longevo.repository.GeracaoResiduos;
import com.biosystemconsultoria.longevo.repository.filter.GeracaoResiduoFilter;
import com.biosystemconsultoria.longevo.service.CadastroGeracaoResiduoService;
import com.biosystemconsultoria.longevo.util.ConfigSettingsMB;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;
import java.util.Map;
import org.primefaces.model.SortOrder;


@Named
@ViewScoped
public class GeracaoResiduoLancamentoMB implements Serializable {
    
 private static final long serialVersionUID = 1L;
     
    @Inject
    private GeracaoResiduos geracaoResiduoDAO;
    
    @Inject
    private CadastroGeracaoResiduoService geracaoResiduoService;

    @Inject
    private GeracaoResiduo selectedGeracaoResiduo;

    @Inject
    private ConfigSettingsMB config;
    
    private GeracaoResiduoFilter filtro;
    
    private BigDecimal acumuladoResiduoQtde;
     
    private BigDecimal acumuladoResiduoCusto; 
     
    private String selectedTipotipoResiduo; 
    
    private LazyDataModel<GeracaoResiduo> geracoesLancadas;

    public GeracaoResiduoLancamentoMB(){
       filtro = new GeracaoResiduoFilter();
    }
    
    public void pesquisar() {
        buscaLancamentos();
    }
    
    public Unidade[] getUnidade() {
	return Unidade.values();
    }    
    
    public Mes[] getMes() {
        return Mes.values();
    }
    
    public Etapa[] getEtapa() {
	return Etapa.values();
    }      
    
   public DestinoResiduo[] getDestinoResiduo() {
	return DestinoResiduo.values();
    }      
        
    public Date getDataHoje() {
	return new Date();
    }
    
    public GeracaoResiduoFilter getFiltro() {
        return this.filtro;
    }

    public LazyDataModel<GeracaoResiduo> getGeracoesLancadas() {
        return geracoesLancadas;
    }

    public GeracaoResiduo getSelectedGeracaoResiduo() {
        return selectedGeracaoResiduo;
    }

    public void buscaLancamentos() {
        this.getFiltro().setEmpresa(config.getUserEmpresa());
       
        if (this.selectedGeracaoResiduo.getDescricao() != null) {
           calculaAcumuladoGeracaoResiduo();
        }   
        
        geracoesLancadas = new LazyDataModel<GeracaoResiduo>() {
                
                private static final long serialVersionUID = 1L;
               
                @Override
            	public List<GeracaoResiduo> load(int first, int pageSize,
			String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
				
				filtro.getPaginacaoLazy().setPrimeiroRegistro(first);
				filtro.getPaginacaoLazy().setQuantidadeRegistros(pageSize);
				filtro.getPaginacaoLazy().setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.getPaginacaoLazy().setPropriedadeOrdenacao(sortField);
				
				setRowCount(geracaoResiduoDAO.quantidadeFiltrados(filtro));
				
				return geracaoResiduoDAO.filtrados(filtro);
		}
        };        
            
    }
    
    public void setSelectedGeracaoResiduo(GeracaoResiduo selectedGeracaoResiduo) {
        this.selectedGeracaoResiduo = selectedGeracaoResiduo;
    }
    
    public void saveOrUpdate() {
        
        if (this.selectedGeracaoResiduo.getDescricao() != null && !this.selectedGeracaoResiduo.getDescricao().isEmpty()) {
                setaConfiguracoes();  
                this.selectedGeracaoResiduo = geracaoResiduoService.salvar(selectedGeracaoResiduo);
                this.selectedGeracaoResiduo = new GeracaoResiduo();
               // buscaLancamentos();
               // calculaAcumuladoGeracaoResiduo();
                FacesUtil.addInfoMessage("Lancamento salvo com sucesso!");
            
      } else {
            FacesUtil.addErrorMessage("Tipo de Residuo nÃ£o informado!!");  
      }  
    }    
    
    public void delete(GeracaoResiduo residuo) {
            this.geracaoResiduoDAO.delete(residuo);
            calculaAcumuladoGeracaoResiduo();
            
            FacesUtil.addInfoMessage("Lancamento " + residuo.getDocumentoref() + " excluÃ­do com sucesso.");
    } 
    
    public void add() {
        this.selectedGeracaoResiduo = new GeracaoResiduo();
    }

    public ConfigSettingsMB getConfig() {
        return config;
    }

    public void setConfig(ConfigSettingsMB config) {
        this.config = config;
    }

    public void setaTipoResiduo() {
        
        
        if (this.selectedGeracaoResiduo.getDescricao().equals("Papel") || 
                this.selectedGeracaoResiduo.getDescricao().equals("LÃ¢mpada") ||
                     this.selectedGeracaoResiduo.getDescricao().equals("Bateria(Pilhas)"))   {
                            this.selectedGeracaoResiduo.setUnidade(Unidade.UD);
        } else {
            this.selectedGeracaoResiduo.setUnidade(Unidade.KG);
        }
        
        if (this.selectedGeracaoResiduo.getAno() == 0 ) {
            
           SimpleDateFormat ano = new SimpleDateFormat("yyyy");  
           DateFormat       mes = new SimpleDateFormat("MM");
           Mes[] values = Mes.values(); 
           
           this.selectedGeracaoResiduo.setMes(Mes.valueOf(values[Integer.parseInt(mes.format(new Date().getTime()).toUpperCase())].getDescricao().toUpperCase()));
           this.selectedGeracaoResiduo.setAno(Integer.parseInt(ano.format(new Date().getTime())));
        }
        //buscaAcumuladoMes(this.selectedTipotipoResiduo);
    }

    public void setSelectedTipotipoResiduo(String selectedTipotipoResiduo) {
        this.selectedTipotipoResiduo = selectedTipotipoResiduo;
    }
 
    public String getSelectedTipotipoResiduo() {
        return selectedTipotipoResiduo;
    }

    public void calculaAcumuladoGeracaoResiduo() {
        
        this.getFiltro().setDescricao(this.selectedGeracaoResiduo.getDescricao());

        if (this.selectedGeracaoResiduo.getMes() != null && !this.selectedGeracaoResiduo.getMes().equals(Mes.SELECIONE) ) {        
            this.getFiltro().setMes(this.selectedGeracaoResiduo.getMes());
        } else {
            this.filtro.setMes(null);
        }    
        
        this.getFiltro().setAno(this.selectedGeracaoResiduo.getAno());
        this.acumuladoResiduoQtde  = geracaoResiduoDAO.residuoTipoAcumuladoQtde(filtro);
        this.acumuladoResiduoCusto = geracaoResiduoDAO.residuoTipoAcumuladoCusto(filtro);
        
    }  
    
    public void setaConfiguracoes() {
        this.selectedGeracaoResiduo.setLog(config.getLogado().getUsername());
        this.selectedGeracaoResiduo.setEmpresaId(config.getUserEmpresa().getId());
        this.selectedGeracaoResiduo.setResiduoTipo(ResiduoTipo.SOLIDO);
        this.selectedGeracaoResiduo.setAspectoTipo(AspectoTipo.ECONOMICO);
    }
    
    public void carregaMes() {
        this.filtro.setMes(this.selectedGeracaoResiduo.getMes());
    }
    
    public void carregaAno()  {
        this.filtro.setAno(this.selectedGeracaoResiduo.getAno());
    }

    public void carregaDocumento() {
        this.filtro.setDocumento(this.selectedGeracaoResiduo.getDocumentoref());
    }
    
    public void setFiltro(GeracaoResiduoFilter filtro) {
        this.filtro = filtro;
    }

    public BigDecimal getAcumuladoResiduoQtde() {
        return acumuladoResiduoQtde;
    }

    public void setAcumuladoResiduoQtde(BigDecimal acumuladoResiduoQtde) {
        this.acumuladoResiduoQtde = acumuladoResiduoQtde;
    }

    public BigDecimal getAcumuladoResiduoCusto() {
        return acumuladoResiduoCusto;
    }

    public void setAcumuladoResiduoCusto(BigDecimal acumuladoResiduoCusto) {
        this.acumuladoResiduoCusto = acumuladoResiduoCusto;
    }
    
}
