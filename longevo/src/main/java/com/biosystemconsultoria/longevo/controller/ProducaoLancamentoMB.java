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

import java.util.Map;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.biosystemconsultoria.longevo.model.Producao;
import com.biosystemconsultoria.longevo.model.Mes;
import com.biosystemconsultoria.longevo.model.Unidade;
import com.biosystemconsultoria.longevo.repository.Producoes;
import com.biosystemconsultoria.longevo.repository.filter.ProducaoFilter;
import com.biosystemconsultoria.longevo.service.CadastroProducaoService;
import com.biosystemconsultoria.longevo.util.ConfigSettingsMB;
import com.biosystemconsultoria.longevo.util.ProducaoAcumulado;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named
@ViewScoped
public class ProducaoLancamentoMB implements Serializable {
    
    private static final long serialVersionUID = 1L;
     
    @Inject
    private Producoes producaoDAO;
    
    @Inject
    private CadastroProducaoService producaoService;

    @Inject
    private Producao selectedProducao;

    @Inject
    private ConfigSettingsMB config;
    
    private ProducaoFilter filtro;
    
    private BigDecimal acumuladoPrevisto;
    
    private BigDecimal acumuladoRealizado;
    
    private BigDecimal acumuladoHora;    
    
    private BigDecimal acumuladoPrevistoHora;
    
    private BigDecimal acumuladoParticipante;    
    
    private List<ProducaoAcumulado> tiposAcumulados;

    private boolean tipoProdutividadeHora;
    
    private boolean tipoProdutividadeParticipante;
    
    private String selectedTipoProdutividade; 
    
    private LazyDataModel<Producao> producoesLancadas;

    public ProducaoLancamentoMB(){
       filtro = new ProducaoFilter();
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
    
    public Date getDataHoje() {
	return new Date();
    }
    
    public ProducaoFilter getFiltro() {
        return this.filtro;
    }

    public LazyDataModel<Producao> getProducoesLancadas() {
        return producoesLancadas;
    }

    public Producao getSelectedProducao() {
        return selectedProducao;
    }

    public void buscaLancamentos() {
       this.filtro.setEmpresa(config.getUserEmpresa());

     //  if (this.selectedProducao.getDescricao() != null) {
           calculaAcumuladoProducao();
    //   }

        producoesLancadas = new LazyDataModel<Producao>() {
                
            private static final long serialVersionUID = 1L;
               
            @Override
            public List<Producao> load(int first, int pageSize,
		String sortField, SortOrder sortOrder,
		Map<String, Object> filters) {
				
		filtro.getPaginacaoLazy().setPrimeiroRegistro(first);
		filtro.getPaginacaoLazy().setQuantidadeRegistros(pageSize);
		filtro.getPaginacaoLazy().setAscendente(SortOrder.ASCENDING.equals(sortOrder));
		filtro.getPaginacaoLazy().setPropriedadeOrdenacao(sortField);
		
		setRowCount(producaoDAO.quantidadeFiltrados(filtro));
			
		return producaoDAO.filtrados(filtro);
            }
        };        
       
    }
    
    public void setSelectedProducao(Producao selectedProducao) {
        this.selectedProducao = selectedProducao;
    }
    
    public void saveOrUpdate() {
        if (this.selectedProducao.getDescricao() != null && !this.selectedProducao.getDescricao().isEmpty()) {
                setaConfiguracoes();  
                this.selectedProducao = producaoService.salvar(selectedProducao);
                this.selectedProducao = new Producao();
               // buscaLancamentos();
               // calculaAcumuladoProducao();
                FacesUtil.addInfoMessage("Lancamento salvo com sucesso!");
            
      } else {
            FacesUtil.addErrorMessage("Tipo de produção não informado!!");  
      }  
    }    
    
    public void delete(Producao producao) {
            this.producaoDAO.delete(producao);
            calculaAcumuladoProducao();
            
            FacesUtil.addInfoMessage("Lancamento " + producao.getDocumentoref() + " excluído com sucesso.");
    } 
    
    public void add() {
        this.selectedProducao = new Producao();
    }

    public ConfigSettingsMB getConfig() {
        return config;
    }

    public void setConfig(ConfigSettingsMB config) {
        this.config = config;
    }

    public boolean isTipoProdutividadeHora() {
        return this.tipoProdutividadeHora;
    }

    public boolean isTipoProdutividadeParticipante() {
        return this.tipoProdutividadeParticipante;
    }
    
    public void setaTipoProdutividade() {
        this.tipoProdutividadeHora = this.selectedProducao.getDescricao().equals("Consultoria") || 
                                     this.selectedProducao.getDescricao().equals("Orientações") || 
                                     this.selectedProducao.getDescricao().equals("Eventos");
        
        this.tipoProdutividadeParticipante = this.selectedProducao.getDescricao().equals("Eventos");
        
        if (this.tipoProdutividadeHora) {
            this.selectedProducao.setUnidade(Unidade.UD);
        }
        
        if (this.selectedProducao.getDescricao().equals("Faturamento")) {
            this.selectedProducao.setUnidade(Unidade.RL);
        }
        
        if (this.selectedProducao.getAno() == 0 ) {
            
           SimpleDateFormat ano = new SimpleDateFormat("yyyy");  
           DateFormat       mes = new SimpleDateFormat("MM");
           Mes[] values = Mes.values();
            
           this.selectedProducao.setMes(Mes.valueOf(values[Integer.parseInt(mes.format(new Date().getTime()).toUpperCase())].getDescricao().toUpperCase()));
           this.selectedProducao.setAno(Integer.parseInt(ano.format(new Date().getTime())));
        }
        //buscaAcumuladoMes(this.selectedTipoProdutividade);
    }

    public void setSelectedTipoProdutividade(String selectedTipoProdutividade) {
        this.selectedTipoProdutividade = selectedTipoProdutividade;
    }
 
    public String getSelectedTipoProdutividade() {
        return selectedTipoProdutividade;
    }

    public BigDecimal getAcumuladoPrevisto() {
        return acumuladoPrevisto;
    }

    public void setAcumuladoPrevisto(BigDecimal acumuladoPrevisto) {
        this.acumuladoPrevisto = acumuladoPrevisto;
    }
     
    public BigDecimal getAcumuladoRealizado() {
        return acumuladoRealizado;
    }

    public void setAcumuladoRealizado(BigDecimal acumuladoRealizado) {
        this.acumuladoRealizado = acumuladoRealizado;
    }

    public void calculaAcumuladoProducao() {
        
        this.filtro.setDescricao(this.selectedProducao.getDescricao());
        
        if (this.selectedProducao.getMes() != null && !this.selectedProducao.getMes().equals(Mes.SELECIONE) ) {
           this.filtro.setMes(this.selectedProducao.getMes());
        } else {
           this.filtro.setMes(null);
        }       

        if (this.filtro.getDescricao() == null) {
            this.tipoProdutividadeHora = true;
        }
        this.filtro.setAno(this.selectedProducao.getAno());
        
        this.acumuladoPrevisto     = producaoDAO.producaoTipoAcumuladoPrevisto(filtro);
        this.acumuladoRealizado    = producaoDAO.producaoTipoAcumuladoRealizado(filtro);   
        this.acumuladoHora         = producaoDAO.producaoTipoAcumuladoHora(filtro);   
        this.acumuladoPrevistoHora = producaoDAO.producaoTipoAcumuladoPrevistoHora(filtro);  
      //  this.acumuladoParticipante = producaoDAO.producaoTipoAcumuladoParticipante(filtro);   
    }  
    
    public void setaConfiguracoes() {
        this.selectedProducao.setLog(config.getLogado().getUsername());
        this.selectedProducao.setEmpresaId(config.getUserEmpresa().getId());
    }
    
    public void carregaMes() {
         this.filtro.setMes(this.selectedProducao.getMes());
    }
    
    public void carregaAno()  {
        this.filtro.setAno(this.selectedProducao.getAno());
    }

    public void carregaDocumento() {
        this.filtro.setDocumento(this.selectedProducao.getDocumentoref());
    }
  
    public BigDecimal getAcumuladoHora() {
        return acumuladoHora;
    }

    public void setAcumuladoHora(BigDecimal acumuladoHora) {
        this.acumuladoHora = acumuladoHora;
    }

    public BigDecimal getAcumuladoPrevistoHora() {
        return acumuladoPrevistoHora;
    }

    public void setAcumuladoPrevistoHora(BigDecimal acumuladoPrevistoHora) {
        this.acumuladoPrevistoHora = acumuladoPrevistoHora;
    }    
    
    public BigDecimal getAcumuladoParticipante() {
        return acumuladoParticipante;
    }

    public void setAcumuladoParticipante(BigDecimal acumuladoParticipante) {
        this.acumuladoParticipante = acumuladoParticipante;
    }
    
  public List<ProducaoAcumulado> getTiposAcumulados() {
        return tiposAcumulados;
    }

    public void setTiposAcumulados(List<ProducaoAcumulado> tiposAcumulados) {
        this.tiposAcumulados = tiposAcumulados;
    }    
}
