/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.controller;

import com.biosystemconsultoria.longevo.model.Aquisicao;
import com.biosystemconsultoria.longevo.model.AspectoTipo;
import java.io.Serializable;

/**
 *
 * @author Wendell
 */

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.math.RoundingMode;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.PieChartModel;
import org.primefaces.model.LazyDataModel;

import com.biosystemconsultoria.longevo.model.ConsumoAguaEnergia;
import com.biosystemconsultoria.longevo.model.ConsumoTipo;
import com.biosystemconsultoria.longevo.model.Endereco;
import com.biosystemconsultoria.longevo.model.Etapa;
import com.biosystemconsultoria.longevo.model.GrupoTarifario;
import com.biosystemconsultoria.longevo.model.Manutencao;
import com.biosystemconsultoria.longevo.model.Mes;
import com.biosystemconsultoria.longevo.model.Unidade;
import com.biosystemconsultoria.longevo.repository.ConsumosAguaEnergia;
import com.biosystemconsultoria.longevo.repository.Enderecos;
import com.biosystemconsultoria.longevo.repository.filter.ConsumoAguaEnergiaFilter;
import com.biosystemconsultoria.longevo.service.CadastroConsumoAguaEnergiaService;
import com.biosystemconsultoria.longevo.service.NegocioException;
import com.biosystemconsultoria.longevo.util.ConfigSettingsMB;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;
import com.biosystemconsultoria.longevo.util.ConsumoAguaEnergiaAcumulado;
import org.primefaces.model.SortOrder;



@Named
@ViewScoped
public class ConsumoAguaEnergiaLancamentoMB implements Serializable{
    
    private static final long serialVersionUID = 1L;
        
    @Inject
    private ConsumosAguaEnergia aspectoDAO;
    
    @Inject
    private CadastroConsumoAguaEnergiaService aspectoService;

    @Inject
    private ConsumoAguaEnergia selectedAspecto;

    @Inject
    private ConfigSettingsMB config;
    
    @Inject
    private Enderecos enderecoDAO;
    
    private boolean tipoManutencao;
    
    private ConsumoAguaEnergiaFilter filtro;  
    
    private LazyDataModel<ConsumoAguaEnergia> aspectosLancados;
    
    private List<ConsumoAguaEnergiaAcumulado> tiposAcumulados;
    
    private boolean tipoAspectoEnergia;
    
    private String tipoConsumoDesc;
    
    private BigDecimal acumuladoConsumo;
     
    private BigDecimal acumuladoTotal;
    
    private List<Endereco> enderecos;
    
    private Endereco endereco;
    
    private PieChartModel pieModel1;
   
    public ConsumoAguaEnergiaLancamentoMB(){
        filtro = new ConsumoAguaEnergiaFilter();
        endereco = new Endereco();
        
        pieModel1 = new PieChartModel();
        pieModel1.set("Janeiro",1);
        pieModel1.setTitle("Média Consumo Anual");
        pieModel1.setLegendPosition("w");
    }
        
    public PieChartModel getPieModel1() {
        return pieModel1;
    }
    
     public void pesquisar(String tipoConsumo) {
        buscaLancamentos(tipoConsumo);
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }
 
    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }
    
    public Etapa[] getEtapa() {
	return Etapa.values();
    }    
    
    public Manutencao[] getManutencao() {
	return Manutencao.values();
    }    
     
    public GrupoTarifario[] getGrupoTarifario() {
	return GrupoTarifario.values();
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
    
    public ConsumosAguaEnergia getAspectoDAO() {
        return aspectoDAO;
    }

    public void setAspectoDAO(ConsumosAguaEnergia aspectoDAO) {
        this.aspectoDAO = aspectoDAO;
    }

    public CadastroConsumoAguaEnergiaService getAspectoService() {
        return aspectoService;
    }

    public void setAspectoService(CadastroConsumoAguaEnergiaService aspectoService) {
        this.aspectoService = aspectoService;
    }

    public ConsumoAguaEnergia getSelectedAspecto() {
        return selectedAspecto;
    }

    public void setSelectedAspecto(ConsumoAguaEnergia selectedAspecto) {
        this.selectedAspecto = selectedAspecto;
    }

    public void buscaLancamentos(String tipoConsumo) {
        
       this.filtro.setEmpresa(config.getUserEmpresa());
       this.filtro.setConsumoTipo(ConsumoTipo.valueOf(tipoConsumo));
       
        if (selectedAspecto.getDescricao() != null) {
           calculaAcumuladoConsumoValor();
        }  
       
        aspectosLancados  = new LazyDataModel<ConsumoAguaEnergia>() {
                
            private static final long serialVersionUID = 1L;
               
            @Override
            public List<ConsumoAguaEnergia> load(int first, int pageSize,
		String sortField, SortOrder sortOrder,
		Map<String, Object> filters) {
				
		filtro.getPaginacaoLazy().setPrimeiroRegistro(first);
		filtro.getPaginacaoLazy().setQuantidadeRegistros(pageSize);
		filtro.getPaginacaoLazy().setAscendente(SortOrder.ASCENDING.equals(sortOrder));
		filtro.getPaginacaoLazy().setPropriedadeOrdenacao(sortField);
		
		setRowCount(aspectoDAO.quantidadeFiltrados(filtro));
			
		return aspectoDAO.filtrados(filtro);
            }
        };        
        
    }  
    
    public void saveOrUpdate(String tipoConsumo) {
        
        if (this.selectedAspecto.getMes().equals(Mes.SELECIONE)) {
           throw new NegocioException("Mês não informado!");
        }

        if (this.tipoConsumoDesc != null && !this.tipoConsumoDesc.isEmpty()) {
                calculaValorUnitario();
                setaConfiguracoes(tipoConsumo);  
                this.selectedAspecto = aspectoService.salvar(selectedAspecto);
                this.selectedAspecto = new ConsumoAguaEnergia();
               // buscaLancamentos();
               // calculaAcumuladoProducao();
                FacesUtil.addInfoMessage("Lancamento salvo com sucesso!");
            
      } else {
            FacesUtil.addErrorMessage("Tipo de Consumo não informado!!");  
      }  
    }    
    
    public void delete(ConsumoAguaEnergia consumo) {
            this.aspectoDAO.delete(consumo);
            // calculaAcumuladoAspecto();
            
            FacesUtil.addInfoMessage("Lancamento " + consumo.getDocumentoref() + " excluído com sucesso.");
    } 
    
    public void add() {
        this.selectedAspecto = new ConsumoAguaEnergia();
    }
    
    public ConfigSettingsMB getConfig() {
        return config;
    }

    public void setConfig(ConfigSettingsMB config) {
        this.config = config;
    }

    public ConsumoAguaEnergiaFilter getFiltro() {
        return filtro;
    }

    public void setFiltro(ConsumoAguaEnergiaFilter filtro) {
        this.filtro = filtro;
    }

    public LazyDataModel<ConsumoAguaEnergia> getAspectosLancados() {
        return aspectosLancados;
    }

    public void setaTipoAspecto() {

        if (this.selectedAspecto.getDescricao().equals("Elétrica")) {
            this.selectedAspecto.setUnidade(Unidade.KW);
        }

        if (this.selectedAspecto.getDescricao().equals("Gás GLP")) {
            this.selectedAspecto.setUnidade(Unidade.M3);
        }
        
        if (this.selectedAspecto.getDescricao().equals("Queima de Biomassa")) {
            this.selectedAspecto.setUnidade(Unidade.KG);
        }   
        
        if (this.selectedAspecto.getDescricao().equals("Concessionária")) {
            this.selectedAspecto.setUnidade(Unidade.M3);
        }
        
        if (this.selectedAspecto.getDescricao().equals("Poço Artesiano")) {
            this.selectedAspecto.setUnidade(Unidade.M3);
        }

        if (this.selectedAspecto.getDescricao().equals("Poço Semiartesiano")) {
            this.selectedAspecto.setUnidade(Unidade.M3);
        }
        
        if (this.selectedAspecto.getAno() == 0 ) {
           SimpleDateFormat ano = new SimpleDateFormat("yyyy");  
           this.selectedAspecto.setAno(Integer.parseInt(ano.format(new Date().getTime())));
        }
        
        if (this.selectedAspecto.getMes() == null || this.selectedAspecto.getMes().equals(Mes.SELECIONE)) {
           Mes[] values = Mes.values(); 
           DateFormat       mes = new SimpleDateFormat("MM");
           this.selectedAspecto.setMes(Mes.valueOf(values[Integer.parseInt(mes.format(new Date().getTime()).toUpperCase())].getDescricao().toUpperCase()));
        }    
        this.tipoConsumoDesc =  this.selectedAspecto.getDescricao();
        //buscaAcumuladoMes(this.selectedTipoProdutividade);
        if (getEnderecos() == null) {
           carregarEnderecos();
        }
    }

    public boolean isTipoAspectoEnergia() {
        return tipoAspectoEnergia;
    }

    public void setTipoAspectoEnergia(boolean tipoAspectoEnergia) {
        this.tipoAspectoEnergia = tipoAspectoEnergia;
    }
    
    public void calculaValorUnitario() {
        if (this.selectedAspecto != null) {
            this.selectedAspecto.setValorUnitario(this.selectedAspecto.getTotal().divide(this.selectedAspecto.getQuantidadeConsumida(),2,RoundingMode.UP));
        }
    }    
    
    public void setaConfiguracoes(String tipoConsumo) {
        
        this.selectedAspecto.setLog(config.getLogado().getUsername());
        this.selectedAspecto.setEmpresaId(config.getUserEmpresa().getId());
        this.selectedAspecto.setAspectoTipo(AspectoTipo.ECONOMICO);
        this.selectedAspecto.setDescricao(this.tipoConsumoDesc);
        this.selectedAspecto.setConsumoTipo(ConsumoTipo.valueOf(tipoConsumo));

    }
    
    public void carregaMes() {
         this.filtro.setMes(this.selectedAspecto.getMes());
    }
    
    public void carregaAno()  {
        this.filtro.setAno(this.selectedAspecto.getAno());
    }

    public void carregarEnderecos() {
       setEnderecos(enderecoDAO.listar(config.getUserEmpresa().getId()));
    }
    
    public void carregaDocumento() {
        this.filtro.setDocumento(this.selectedAspecto.getDocumentoref());
    }
    
    public void carregaUnidadeConsumidora() {
        this.filtro.setUnidadeConsumidora(this.selectedAspecto.getUnidadeConsumidora());
    }
  
    public boolean isTipoManutencao() {
        return tipoManutencao;
    }

    public void setTipoManutencao(boolean tipoManutencao) {
        this.tipoManutencao = tipoManutencao;
    }
    
    public void carregaDataManutencao() {
        if (this.selectedAspecto.getManutencao() != null) {
            this.tipoManutencao = this.selectedAspecto.getManutencao().equals(Manutencao.CORRETIVA);
        }
    }
    
    public void calculaAcumuladoConsumoValor() {
        this.filtro.setDescricao(this.selectedAspecto.getDescricao());
        
     //   this.filtro.setDocumento(this.selectedAspecto.getDocumentoref());
        
   //     this.filtro.setUnidadeConsumidora(this.selectedAspecto.getUnidadeConsumidora());
        
        if (this.selectedAspecto.getMes() != null && !this.selectedAspecto.getMes().equals(Mes.SELECIONE) ) {
           this.filtro.setMes(this.selectedAspecto.getMes());
        } else {
           this.filtro.setMes(null);
        }       
        
        this.getFiltro().setAno(this.selectedAspecto.getAno());
        this.acumuladoConsumo  = aspectoDAO.acumuladoConsumo(filtro);
        this.acumuladoTotal    = aspectoDAO.acumuladoTotal(filtro);
        this.tiposAcumulados   = aspectoDAO.consumoTiposAcumuladoAnual(filtro);
        
        pieModel1 = new PieChartModel();
        for( ConsumoAguaEnergiaAcumulado tp: tiposAcumulados ) {
             pieModel1.set(tp.getMes().getDescricao(), tp.getQuantidadeConsumida());
        }
        pieModel1.setTitle("Média Consumo Anual");
        pieModel1.setLegendPosition("w");
        
      
    }
   
       
    public String getTipoConsumoDesc() {
        return tipoConsumoDesc;
    }

    public void setTipoConsumoDesc(String tipoConsumoDesc) {
        this.tipoConsumoDesc = tipoConsumoDesc;
    }
    
    public void editaConsumoDesc() {
         System.out.println(this.selectedAspecto.getDescricao());
        this.tipoConsumoDesc = this.selectedAspecto.getDescricao();
    }

    public BigDecimal getAcumuladoConsumo() {
        return acumuladoConsumo;
    }

    public void setAcumuladoConsumo(BigDecimal acumuladoConsumo) {
        this.acumuladoConsumo = acumuladoConsumo;
    }

    public BigDecimal getAcumuladoTotal() {
        return acumuladoTotal;
    }

    public void setAcumuladoTotal(BigDecimal acumuladoTotal) {
        this.acumuladoTotal = acumuladoTotal;
    }

    public List<ConsumoAguaEnergiaAcumulado> getTiposAcumulados() {
        return tiposAcumulados;
    }

    public void setTiposAcumulados(List<ConsumoAguaEnergiaAcumulado> tiposAcumulados) {
        this.tiposAcumulados = tiposAcumulados;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }


    
}


