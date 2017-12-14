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
import java.util.ArrayList;
import java.math.RoundingMode;
import java.text.NumberFormat;

import com.biosystemconsultoria.longevo.model.Temperatura;
import com.biosystemconsultoria.longevo.model.Mes;
import com.biosystemconsultoria.longevo.repository.ConsumosAguaEnergia;
import com.biosystemconsultoria.longevo.util.ConfigSettingsMB;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;

import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;
import com.biosystemconsultoria.longevo.util.report.ExecutorRelatorio;
import com.google.common.base.Strings;
import java.math.BigDecimal;


@Named
@RequestScoped
public class ConsumoAguaEnergiaRelatorioMB implements Serializable  {
    
    private static final long serialVersionUID = 1L; 
   
    @Inject
    private ConfigSettingsMB config;
  
    @Inject
    private FacesContext facesContext;
    
    @Inject
    private HttpServletResponse response;

    @Inject
    private EntityManager manager;
      
    @Inject
    private ConsumosAguaEnergia consumoAguaEnergiaDAO;
    
    private ArrayList dadosLocalidadeGrupo = new ArrayList();
    
    private ArrayList dadosConsumoMensal = new ArrayList();
    
    private ArrayList dadosConsumoMensalTotalizado = new ArrayList();
    
    private ArrayList dadosConsumoAnualTotalizado = new ArrayList();
    
    private ArrayList dadosConsumosGeraisAnoBase = new ArrayList();
    
    private ArrayList dadosConsumosGeraisAnoAnterior = new ArrayList();
    
    private ArrayList dadosValoresGeraisAnoBase = new ArrayList();
    
    private ArrayList dadosValoresGeraisAnoAnterior = new ArrayList();
    
    private List<Temperatura> dadosTemperaturaLocalAnoBase;
       
    private List<Temperatura> dadosTemperaturaLocalAnoAnterior;
    
    private List<ColumnModel> columns = new ArrayList<ColumnModel>();    
    
    private boolean anoBaseb;
    
    private boolean consumoKwh;
   
    private int anoBase;
     
    private Mes mesBase;
     
    private int anoAnterior;
    
    private int enderecoMatriz;
    
    private String cidadeMatriz;     
    
    private String totalGeralValorFormatado;
    
    private String totalMediaValorMensal;
    
    private BigDecimal temperaturaMediaMaximaAnoBase = new BigDecimal("0.0");
    
    private BigDecimal temperaturaMediaMinimaAnoBase = new BigDecimal("0.0");
    
    private BigDecimal temperaturaMediaMaximaAnoAnterior = new BigDecimal("0.0");
    
    private BigDecimal temperaturaMediaMinimaAnoAnterior = new BigDecimal("0.0");    

    private BigDecimal temperaturaDiferencaMaxima = new BigDecimal("0.0");
    
    private BigDecimal temperaturaDiferencaMinima = new BigDecimal("0.0");    
    
    private BigDecimal totalGeralConsumoAnoBase;
    
    private BigDecimal totalGeralConsumoAnoAnterior;
    
    private BigDecimal totalGeralConsumoDiferencaPorcentagem;
    
    private BigDecimal totalGeralValorAnoBase;
    
    private BigDecimal totalGeralValorAnoAnterior;
    
    private BigDecimal totalGeralValorDiferencaPorcentagem;
    
    private ArrayList dadosConsumoDiferencaPorcentagem = new ArrayList();
    
    private ArrayList dadosValorDiferencaPorcentagem = new ArrayList();
   
     
   public void emitir() {
	Map<String, Object> parametros = new HashMap<>();
        parametros.put("empresa", config.getUserEmpresa().getId().intValue());
	parametros.put("anoBase", this.anoBase);
		
	ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/aspecto/relatorio_consumo_energia.jasper",
				this.response, parametros, "Dados Gestao de Energia.doc");
		
	Session session = manager.unwrap(Session.class);
	session.doWork(executor);
		
	if (executor.isRelatorioGerado()) {
            facesContext.responseComplete();
	} else {
            FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
	}
    }
   
 
    public void processar() {
       this.setDadosLocalidadeGrupo(consumoAguaEnergiaDAO.localidadeUnidadeGrupo(config.getUserEmpresa())); 
       loadDadosAnoBase();
       loadDadosAnoAnterior();
       loadDadosTemperatura();
    }

    public void loadDadosAnoBase() {
        
        this.setDadosConsumoMensal(consumoAguaEnergiaDAO.dadosConsumoMensal(config.getUserEmpresa().getId(), getAnoBase()));
        loadMesBase();
        this.setDadosConsumoMensalTotalizado(consumoAguaEnergiaDAO.dadosConsumoMensalTotalizado(config.getUserEmpresa().getId(), getAnoBase()));
        this.setDadosConsumoAnualTotalizado(consumoAguaEnergiaDAO.dadosConsumoAnualTotalizado(config.getUserEmpresa().getId(), getAnoBase(),Mes.DEZEMBRO));
        anoBaseb = true;
        if (this.dadosConsumoMensal != null && this.dadosConsumoMensal.size() > 0  ) {
          gerarTabelaDinamicaTotais();   
        }
    }
    
    public void loadDadosAnoAnterior() {
       // Mes[] values = Mes.values();
       // Mes mes = Mes.valueOf(values[totalMesesGeradoAnoBase].getDescricao().toUpperCase());
        
        this.setDadosConsumoMensal(consumoAguaEnergiaDAO.dadosConsumoMensal(config.getUserEmpresa().getId(), getAnoAnterior()));
        this.setDadosConsumoMensalTotalizado(consumoAguaEnergiaDAO.dadosConsumoMensalTotalizado(config.getUserEmpresa().getId(), getAnoAnterior()));
        this.setDadosConsumoAnualTotalizado(consumoAguaEnergiaDAO.dadosConsumoAnualTotalizado(config.getUserEmpresa().getId(), getAnoAnterior(),getMesBase()));
        anoBaseb = false;
        
        if (this.dadosConsumoMensal != null && this.dadosConsumoMensal.size() > 0  ) {
          columns.clear();  
          gerarTabelaDinamicaTotais();   
        }
    }
    
    public void loadDadosTemperatura() {
        this.setDadosTemperaturaLocalAnoBase((List<Temperatura>) consumoAguaEnergiaDAO.dadosTemperatura(loadEnderecoMatriz(), getAnoBase(),Mes.DEZEMBRO));
        calculaTemperaturaMediaMaximaMinimaAnoBase();
        Mes[] values = Mes.values();
        Mes mes = Mes.valueOf(values[dadosTemperaturaLocalAnoBase.size()].getDescricao().toUpperCase());
        this.setDadosTemperaturaLocalAnoAnterior((List<Temperatura>) consumoAguaEnergiaDAO.dadosTemperatura(loadEnderecoMatriz(), getAnoAnterior(),mes));
        calculaTemperaturaMediaMaximaMinimaAnoAnterior();
    }
    
    
    public void gerarTabelaDinamicaTotais(){
        montarColunasTabela();
        preencherColunasTabela();
        gerarTotalizacaoTabela();
        if (!anoBaseb) {
          gerarDiferencaPorcentagemTabela();
        }
    }      
   
    public void loadMesBase() {
        Object[] objBase = null;
        Mes m = null;
        for (int i=0; i < dadosConsumoMensal.size(); i++) {
            objBase = (Object[]) dadosConsumoMensal.get(i);
            m = (Mes) objBase[0]; 
        }
        this.setMesBase(m);
    }

    @NotNull
    public int getAnoBase() {
	return anoBase;
    }
    
    public void setAnoBase(int anoBase) {
        this.anoBase = anoBase;
        setAnoAnterior(anoBase-1);
    }
    
    public Mes[] getMes() {
        return Mes.values();
    }

    public ConsumosAguaEnergia getConsumoAguaEnergiaDAO() {
        return consumoAguaEnergiaDAO;
    }

    public void setConsumoAguaEnergiaDAO(ConsumosAguaEnergia consumoAguaEnergiaDAO) {
        this.consumoAguaEnergiaDAO = consumoAguaEnergiaDAO;
    }

    public ArrayList<Strings> getDadosLocalidadeGrupo() {
        return dadosLocalidadeGrupo;
    }

    public void setDadosLocalidadeGrupo(ArrayList<Strings> dadosLocalidadeGrupo) {
        this.dadosLocalidadeGrupo = dadosLocalidadeGrupo;
    }

    public ArrayList<Strings> getDadosConsumoMensal() {
        return dadosConsumoMensal;
    }

    public void setDadosConsumoMensal(ArrayList<Strings> dadosConsumoMensal) {
        this.dadosConsumoMensal = dadosConsumoMensal;
    }

    public ArrayList<Strings> getDadosConsumoMensalTotalizado() {
        return dadosConsumoMensalTotalizado;
    }

    public void setDadosConsumoMensalTotalizado(ArrayList<Strings> dadosConsumoMensalTotalizado) {
        this.dadosConsumoMensalTotalizado = dadosConsumoMensalTotalizado;
    }

  
    public ArrayList getDadosConsumoAnualTotalizado() {
        return dadosConsumoAnualTotalizado;
    }

    public void setDadosConsumoAnualTotalizado(ArrayList dadosConsumoAnualTotalizado) {
        this.dadosConsumoAnualTotalizado = dadosConsumoAnualTotalizado;
    }

    public ArrayList getDadosConsumosGeraisAnoBase() {
        return dadosConsumosGeraisAnoBase;
    }

    public void setDadosConsumosGeraisAnoBase(ArrayList dadosConsumosGeraisAnoBase) {
        this.dadosConsumosGeraisAnoBase = dadosConsumosGeraisAnoBase;
    }

    public ArrayList getDadosConsumosGeraisAnoAnterior() {
        return dadosConsumosGeraisAnoAnterior;
    }

    public void setDadosConsumosGeraisAnoAnterior(ArrayList dadosConsumosGeraisAnoAnterior) {
        this.dadosConsumosGeraisAnoAnterior = dadosConsumosGeraisAnoAnterior;
    }

    public int getAnoAnterior() {
        return anoAnterior;
    }

    public void setAnoAnterior(int anoAnterior) {
        this.anoAnterior = anoAnterior;
    }

    public BigDecimal getTotalGeralConsumoAnoBase() {
        return totalGeralConsumoAnoBase;
    }

    public void setTotalGeralConsumoAnoBase(BigDecimal totalGeralConsumoAnoBase) {
        this.totalGeralConsumoAnoBase = totalGeralConsumoAnoBase;
    }

    public BigDecimal getTotalGeralConsumoAnoAnterior() {
        return totalGeralConsumoAnoAnterior;
    }

    public void setTotalGeralConsumoAnoAnterior(BigDecimal totalGeralConsumoAnoAnterior) {
        this.totalGeralConsumoAnoAnterior = totalGeralConsumoAnoAnterior;
    }

    public ArrayList getDadosConsumoDiferencaPorcentagem() {
        return dadosConsumoDiferencaPorcentagem;
    }

    public void setTotalConsumoDiferencaPorcentagem(ArrayList dadosConsumoDiferencaPorcentagem) {
        this.dadosConsumoDiferencaPorcentagem = dadosConsumoDiferencaPorcentagem;
    }

    public BigDecimal getTotalGeralConsumoDiferencaPorcentagem() {
        return totalGeralConsumoDiferencaPorcentagem;
    }

    public void setTotalGeralConsumoDiferencaPorcentagem(BigDecimal totalGeralConsumoDiferencaPorcentagem) {
        this.totalGeralConsumoDiferencaPorcentagem = totalGeralConsumoDiferencaPorcentagem;
    }

    public List<Temperatura> getDadosTemperaturaLocalAnoBase() {
        return dadosTemperaturaLocalAnoBase;
    }

    public void setDadosTemperaturaLocalAnoBase(List<Temperatura> dadosTemperaturaLocalAnoBase) {
        this.dadosTemperaturaLocalAnoBase = dadosTemperaturaLocalAnoBase;
    }

    public int getEnderecoMatriz() {
        return enderecoMatriz;
    }

    public void setEnderecoMatriz(int enderecoMatriz) {
        this.enderecoMatriz = enderecoMatriz;
    }

    public List<Temperatura> getDadosTemperaturaLocalAnoAnterior() {
        return dadosTemperaturaLocalAnoAnterior;
    }

    public void setDadosTemperaturaLocalAnoAnterior(List<Temperatura> dadosTemperaturaLocalAnoAnterior) {
        this.dadosTemperaturaLocalAnoAnterior = dadosTemperaturaLocalAnoAnterior;
    }

    public String getCidadeMatriz() {
        return cidadeMatriz;
    }

    public void setCidadeMatriz(String cidadeMatriz) {
        this.cidadeMatriz = cidadeMatriz;
    }

    public BigDecimal getTemperaturaMediaMaximaAnoBase() {
        return temperaturaMediaMaximaAnoBase;
    }

    public void setTemperaturaMediaMaximaAnoBase(BigDecimal temperaturaMediaMaximaAnoBase) {
        this.temperaturaMediaMaximaAnoBase = temperaturaMediaMaximaAnoBase;
    }

    public BigDecimal getTemperaturaMediaMinimaAnoBase() {
        return temperaturaMediaMinimaAnoBase;
    }

    public void setTemperaturaMediaMinimaAnoBase(BigDecimal temperaturaMediaMinimaAnoBase) {
        this.temperaturaMediaMinimaAnoBase = temperaturaMediaMinimaAnoBase;
    }

    public BigDecimal getTemperaturaMediaMaximaAnoAnterior() {
        return temperaturaMediaMaximaAnoAnterior;
    }

    public void setTemperaturaMediaMaximaAnoAnterior(BigDecimal temperaturaMediaMaximaAnoAnterior) {
        this.temperaturaMediaMaximaAnoAnterior = temperaturaMediaMaximaAnoAnterior;
    }

    public BigDecimal getTemperaturaMediaMinimaAnoAnterior() {
        return temperaturaMediaMinimaAnoAnterior;
    }

    public void setTemperaturaMediaMinimaAnoAnterior(BigDecimal temperaturaMediaMinimaAnoAnterior) {
        this.temperaturaMediaMinimaAnoAnterior = temperaturaMediaMinimaAnoAnterior;
    }

    public BigDecimal getTemperaturaDiferencaMaxima() {
        return temperaturaDiferencaMaxima;
    }

    public void setTemperaturaDiferencaMaxima(BigDecimal temperaturaDiferencaMaxima) {
        this.temperaturaDiferencaMaxima = temperaturaDiferencaMaxima;
    }

    public BigDecimal getTemperaturaDiferencaMinima() {
        return temperaturaDiferencaMinima;
    }

    public void setTemperaturaDiferencaMinima(BigDecimal temperaturaDiferencaMinima) {
        this.temperaturaDiferencaMinima = temperaturaDiferencaMinima;
    }

    public ArrayList getDadosValoresGeraisAnoBase() {
        return dadosValoresGeraisAnoBase;
    }

    public void setDadosValoresGeraisAnoBase(ArrayList dadosValoresGeraisAnoBase) {
        this.dadosValoresGeraisAnoBase = dadosValoresGeraisAnoBase;
    }

    public ArrayList getDadosValoresGeraisAnoAnterior() {
        return dadosValoresGeraisAnoAnterior;
    }

    public void setDadosValoresGeraisAnoAnterior(ArrayList dadosValoresGeraisAnoAnterior) {
        this.dadosValoresGeraisAnoAnterior = dadosValoresGeraisAnoAnterior;
    }

    public boolean isConsumoKwh() {
        return consumoKwh;
    }

    public void setConsumoKwh(boolean consumoKwh) {
        this.consumoKwh = consumoKwh;
    }

    public BigDecimal getTotalGeralValorAnoBase() {
        return totalGeralValorAnoBase;
    }

    public void setTotalGeralValorAnoBase(BigDecimal totalGeralValorAnoBase) {
        this.totalGeralValorAnoBase = totalGeralValorAnoBase;
    }

    public BigDecimal getTotalGeralValorAnoAnterior() {
        return totalGeralValorAnoAnterior;
    }

    public void setTotalGeralValorAnoAnterior(BigDecimal totalGeralValorAnoAnterior) {
        this.totalGeralValorAnoAnterior = totalGeralValorAnoAnterior;
    }

    public BigDecimal getTotalGeralValorDiferencaPorcentagem() {
        return totalGeralValorDiferencaPorcentagem;
    }

    public void setTotalGeralValorDiferencaPorcentagem(BigDecimal totalGeralValorDiferencaPorcentagem) {
        this.totalGeralValorDiferencaPorcentagem = totalGeralValorDiferencaPorcentagem;
    }

    public void setDadosConsumoDiferencaPorcentagem(ArrayList dadosConsumoDiferencaPorcentagem) {
        this.dadosConsumoDiferencaPorcentagem = dadosConsumoDiferencaPorcentagem;
    }

    public ArrayList getDadosValorDiferencaPorcentagem() {
        return dadosValorDiferencaPorcentagem;
    }

    public void setDadosValorDiferencaPorcentagem(ArrayList dadosValorDiferencaPorcentagem) {
        this.dadosValorDiferencaPorcentagem = dadosValorDiferencaPorcentagem;
    }

    public String getTotalGeralValorFormatado() {
        return totalGeralValorFormatado;
    }

    public void setTotalGeralValorFormatado(String totalGeralValorFormatado) {
        this.totalGeralValorFormatado = totalGeralValorFormatado;
    }

    public String getTotalMediaValorMensal() {
        return totalMediaValorMensal;
    }

    public void setTotalMediaValorMensal(String totalMediaValorMensal) {
        this.totalMediaValorMensal = totalMediaValorMensal;
    }

    public Mes getMesBase() {
        return mesBase;
    }

    public void setMesBase(Mes mesBase) {
        this.mesBase = mesBase;
    }
    

    static public class ColumnModel implements Serializable {
 
        private String header;
        private String property;
 
        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }
 
        public String getHeader() {
            return header;
        }
 
        public String getProperty() {
            return property;
        }
    }   
    
     public List<ColumnModel> getColumns() {
        return columns;
    }
    
    public void montarColunasTabela() {
        
       columns.add(new ColumnModel("Mês","0"));
       int c = 0;
       
       for (int i = 0; i < dadosLocalidadeGrupo.size(); i++) {
           
            Object[] object = (Object[]) dadosLocalidadeGrupo.get(i);
            Long b    = (Long) object[0];
            String s  = (String) object[4]; 
            columns.add(new ColumnModel("UC "+Long.toString(b)+" n."+s,Integer.toString(i+1)));
            c = (c+i);
       }
       columns.add(new ColumnModel("Total Mensal ",Integer.toString(c+1)));
    }   
      
    public void preencherColunasTabela() {
        
       String[] line = new String[20];
       String[] lineReal = new String[20];
       Object[] obj = null, objTot;
       String s = "";
       int cnt = 1, cntKwh = 0, cntReal = 0, count = 0;
       
       for (int i = 0; i < dadosConsumoMensal.size(); i++) {
           
            obj = (Object[]) dadosConsumoMensal.get(i);
            Mes m = (Mes) obj[0];
            
            if (!s.equals(m.getDescricao())) {
               String ano; 
               if (anoBaseb) {ano = (String.valueOf(getAnoBase()));}
               else {ano = (String.valueOf(getAnoAnterior()));}
               line[cntKwh] = m.getDescricao()+"("+ano+")";
               lineReal[cntReal] = m.getDescricao()+"("+ano+")";
               cntKwh++;
               cntReal++;
            }
            
            s = m.getDescricao();
            
            BigDecimal b  = (BigDecimal) obj[2];
            BigDecimal r  = (BigDecimal) obj[3];
            line[cntKwh] = NumberFormat.getInstance().format(b);
            lineReal[cntReal] = NumberFormat.getCurrencyInstance().format(r);
            
            cntKwh++;
            cntReal++;
            
            if (cnt == dadosLocalidadeGrupo.size()) { 
               objTot = (Object[]) dadosConsumoMensalTotalizado.get(count);
               BigDecimal t  = (BigDecimal) objTot[1];
               BigDecimal d  = (BigDecimal) objTot[2];
               
               line[cntKwh] = NumberFormat.getInstance().format(t);
               lineReal[cntReal] = NumberFormat.getCurrencyInstance().format(d);
               cntKwh++;
               cntReal++;
               if (anoBaseb) {
                  dadosConsumosGeraisAnoBase.add(line);
                  dadosValoresGeraisAnoBase.add(lineReal);
               } else {
                   if (count < this.mesBase.ordinal()) {
                     dadosConsumosGeraisAnoAnterior.add(line);
                     dadosValoresGeraisAnoAnterior.add(lineReal);
                   }  
               }
               line = new String[20];
               lineReal = new String[20];
               cnt = 0; cntKwh = 0; cntReal = 0; count++;
            }  
            ++cnt;
           
            obj= null;
            
        }
    }   
     
    public void gerarTotalizacaoTabela() {
        
        String[] line = new String[20];
        String[] lineReal = new String[20];
        String[] lineMedia = new String[20];
        BigDecimal totalConsumoGeral = new BigDecimal("0");
        BigDecimal totalValorGeral = new BigDecimal("0");
        BigDecimal totalMediaValorGeral = new BigDecimal("0");
        BigDecimal quantMeses = new BigDecimal(String.valueOf(this.mesBase.ordinal()));
        Object[] obj = null, objTot;
        int cnt = 0;
        
        line[cnt] = "Total Kwh";
        lineReal[cnt] = "Total R$";
        lineMedia[cnt] = "Mêdia";
        cnt++;
       
        for (int i = 0; i < dadosLocalidadeGrupo.size(); i++) {
          objTot = (Object[]) dadosConsumoAnualTotalizado.get(i);
          BigDecimal b = (BigDecimal) objTot[1];
          BigDecimal r = (BigDecimal) objTot[2];
          totalConsumoGeral = totalConsumoGeral.add(b);
          totalValorGeral = totalValorGeral.add(r);
          totalMediaValorGeral = r.divide(quantMeses,2,RoundingMode.UP);
          line[cnt]  = NumberFormat.getInstance().format(b);
          lineReal[cnt] = NumberFormat.getCurrencyInstance().format(r);
          lineMedia[cnt] = NumberFormat.getCurrencyInstance().format(totalMediaValorGeral);
          cnt++;
          
        }
        line[cnt] = NumberFormat.getInstance().format(totalConsumoGeral);
        lineReal[cnt] = NumberFormat.getCurrencyInstance().format(totalValorGeral);
        lineMedia[cnt] = NumberFormat.getCurrencyInstance().format(totalValorGeral.divide(quantMeses, RoundingMode.UP));
        
        if (anoBaseb) {
            dadosConsumosGeraisAnoBase.add(line);
            dadosValoresGeraisAnoBase.add(lineReal);
            dadosValoresGeraisAnoBase.add(lineMedia);
            setTotalGeralConsumoAnoBase(totalConsumoGeral);
            setTotalGeralValorAnoBase(totalValorGeral);
            setTotalGeralValorFormatado(NumberFormat.getCurrencyInstance().format(totalValorGeral));
            setTotalConsumoDiferencaPorcentagem(dadosConsumoAnualTotalizado);
            setTotalMediaValorMensal(NumberFormat.getCurrencyInstance().format(totalValorGeral.divide(quantMeses, RoundingMode.UP)));
        } else {
            dadosConsumosGeraisAnoAnterior.add(line);
            dadosValoresGeraisAnoAnterior.add(lineReal);
            dadosValoresGeraisAnoAnterior.add(lineMedia);
            setTotalGeralConsumoAnoAnterior(totalConsumoGeral);
            setTotalGeralValorAnoAnterior(totalValorGeral);
        }
     
    }
    
    public void gerarDiferencaPorcentagemTabela() {
        BigDecimal f = new BigDecimal("100");
        String[] line = new String[20];
        String[] lineReal = new String[20];
        int cntKwh = 0, cntReal = 0;
        
        line[cntKwh] = "Diferença %";
        lineReal[cntReal] = "Diferença %";
        cntKwh++;
        cntReal++;
        for (int i = 0; i < dadosConsumoAnualTotalizado.size(); i++ ) {
            Object[] objB = (Object[]) getDadosConsumoDiferencaPorcentagem().get(i);
            Object[] objA = (Object[]) dadosConsumoAnualTotalizado.get(i);
            
            BigDecimal b = (BigDecimal) objB[1];
            BigDecimal a = (BigDecimal) objA[1];
            
            BigDecimal r = (BigDecimal) objB[2];
            BigDecimal d = (BigDecimal) objA[2];
            
            line[cntKwh]  = b.subtract(a).divide(a,4,RoundingMode.UP).multiply(f).toString()+"%"; 
            lineReal[cntReal] = r.subtract(d).divide(d,4,RoundingMode.UP).multiply(f).toString()+"%"; 
            cntKwh++;
            cntReal++;
        }
        line[cntKwh] = totalGeralConsumoAnoBase.subtract(totalGeralConsumoAnoAnterior).divide(totalGeralConsumoAnoAnterior,4,RoundingMode.UP).multiply(f).toString()+"%";
        lineReal[cntReal] = totalGeralValorAnoBase.subtract(totalGeralValorAnoAnterior).divide(totalGeralValorAnoAnterior,4,RoundingMode.UP).multiply(f).toString()+"%";
        dadosConsumosGeraisAnoBase.add(line);
        dadosValoresGeraisAnoBase.add(lineReal);
        
        setTotalGeralConsumoDiferencaPorcentagem(totalGeralConsumoAnoBase.subtract(totalGeralConsumoAnoAnterior).divide(totalGeralConsumoAnoAnterior,4,RoundingMode.UP).multiply(f));
        setTotalGeralValorDiferencaPorcentagem(totalGeralValorAnoBase.subtract(totalGeralValorAnoAnterior).divide(totalGeralValorAnoAnterior,4,RoundingMode.UP).multiply(f));
    }
    
    public Long loadEnderecoMatriz() {
        
        Long end = null;
        String matriz;
        
        for (int i = 0; i < dadosLocalidadeGrupo.size(); i++) { 
          Object[] obj = (Object[]) dadosLocalidadeGrupo.get(i);
          matriz = (String) obj[6];
          
          if (matriz.equals("M")) {
             end = (Long) obj[5];
             setCidadeMatriz((String) obj[7]);
          }
        }  
        return end;
    }
    
    public void calculaTemperaturaMediaMaximaMinimaAnoBase() {
        
        BigDecimal mediaMaxima = new BigDecimal("0.0");
        BigDecimal mediaMinima = new BigDecimal("0.0");
        BigDecimal div = new BigDecimal(dadosTemperaturaLocalAnoBase.size());

        for (Temperatura temp: this.dadosTemperaturaLocalAnoBase) {
            
            mediaMaxima = new BigDecimal(temp.getMaxima().toString());
            mediaMinima = new BigDecimal(temp.getMinima().toString());
            
            this.temperaturaMediaMaximaAnoBase = this.temperaturaMediaMaximaAnoBase.add(mediaMaxima);
            this.temperaturaMediaMinimaAnoBase = this.temperaturaMediaMinimaAnoBase.add(mediaMinima);
        }
        
        this.temperaturaMediaMaximaAnoBase = this.temperaturaMediaMaximaAnoBase.divide(div,2,RoundingMode.UP);
        this.temperaturaMediaMinimaAnoBase = this.temperaturaMediaMinimaAnoBase.divide(div,2,RoundingMode.UP);
    }

    public void calculaTemperaturaMediaMaximaMinimaAnoAnterior() {
        
        BigDecimal mediaMaxima = new BigDecimal("0.0");
        BigDecimal mediaMinima = new BigDecimal("0.0");
        BigDecimal div = new BigDecimal(dadosTemperaturaLocalAnoAnterior.size());
        BigDecimal f = new BigDecimal("100"); 
         
         
        for (Temperatura temp: this.dadosTemperaturaLocalAnoAnterior) {
            
            mediaMaxima = new BigDecimal(temp.getMaxima().toString());
            mediaMinima = new BigDecimal(temp.getMinima().toString());
            
            this.temperaturaMediaMaximaAnoAnterior = this.temperaturaMediaMaximaAnoAnterior.add(mediaMaxima);
            this.temperaturaMediaMinimaAnoAnterior = this.temperaturaMediaMinimaAnoAnterior.add(mediaMinima);
        }
        
        this.temperaturaMediaMaximaAnoAnterior = this.temperaturaMediaMaximaAnoAnterior.divide(div,2,RoundingMode.UP);
        this.temperaturaMediaMinimaAnoAnterior = this.temperaturaMediaMinimaAnoAnterior.divide(div,2,RoundingMode.UP);
        
        this.temperaturaDiferencaMaxima = this.temperaturaMediaMaximaAnoBase.subtract(this.temperaturaMediaMaximaAnoAnterior).divide(this.temperaturaMediaMaximaAnoAnterior,4,RoundingMode.UP).multiply(f);
        this.temperaturaDiferencaMinima = this.temperaturaMediaMinimaAnoBase.subtract(this.temperaturaMediaMinimaAnoAnterior).divide(this.temperaturaMediaMinimaAnoAnterior,4,RoundingMode.UP).multiply(f);
    }


}
