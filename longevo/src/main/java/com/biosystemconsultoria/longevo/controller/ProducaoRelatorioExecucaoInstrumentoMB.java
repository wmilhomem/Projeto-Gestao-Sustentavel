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
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


import com.biosystemconsultoria.longevo.model.Mes;
import com.biosystemconsultoria.longevo.repository.Producoes;
import com.biosystemconsultoria.longevo.util.ConfigSettingsMB;
import com.biosystemconsultoria.longevo.model.Instrumento;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;


import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;
import com.biosystemconsultoria.longevo.util.report.ExecutorRelatorio;
import javax.swing.text.html.parser.*;
import java.io.File;
import java.io.IOException;

import java.math.BigDecimal;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



@Named
@RequestScoped
public class ProducaoRelatorioExecucaoInstrumentoMB  implements Serializable  {
    
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
    private Producoes producoesDAO;
    
    private List<ColumnModel> columns = new ArrayList<ColumnModel>();  
   
    private ArrayList instrumentosExecucaoAnoBase = new ArrayList();
    
    private ArrayList instrumentosExecucaoAnoAnterior = new ArrayList();
    
    private ArrayList instrumentosExecucaoAgrupado = new ArrayList();
    
    private ArrayList instrumentosExecutadosMensal = new ArrayList();

    private boolean anoBaseb;
    
    private int anoBase;
    
    private Mes mesBase;
    
    private String text;
    
    private ArrayList totalGeralAnualInstrumentoAnoBase = new ArrayList();
    
    private ArrayList totalGeralAnualInstrumentoAnoAnterior = new ArrayList();
    
    private int anoAnterior;
    
    private BigDecimal totalGeralAnoBaseAcumulado;
    
    private BigDecimal totalGeralAnoAnteriorAcumulado;
    
    private BigDecimal totalGeralDiferencaPorcentagemAcumulado;
    
    private BigDecimal agrupaSomaGeralTotalAnual;
    
    private ArrayList totalGeralDiferencaPorcentagem = new ArrayList();
    
    private static String url = "http://www.apache.org/";
    
    public void emitir() {
	Map<String, Object> parametros = new HashMap<>();
        parametros.put("empresa", config.getUserEmpresa().getId().intValue());
	parametros.put("anoBase", this.getAnoBase());
		
	ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/producao/relatorio_execucao_instrumento.jasper",
				this.response, parametros, "Execucao Instrumento Fisico.pdf");
		
	Session session = manager.unwrap(Session.class);
	session.doWork(executor);
		
	if (executor.isRelatorioGerado()) {
            facesContext.responseComplete();
	} else {
            FacesUtil.addErrorMessage("A execução do relatório nÃ£o retornou dados.");
	}
    }
    
    
    public void processar() {
        loadInstrumentosExecutados();
    }

    public void loadInstrumentosExecutados() {
        this.setInstrumentosExecucaoAnoBase(producoesDAO.agruparInstrumenstosFisico(config.getUserEmpresa().getId(), getAnoBase(), Mes.DEZEMBRO));
        loadMesBase();
        this.setInstrumentosExecucaoAnoAnterior(producoesDAO.agruparInstrumenstosFisico(config.getUserEmpresa().getId(), getAnoAnterior(), getMesBase()));
        agruparDadosComparativos();
        gerarColunasExecucaoInstrumentos();
        this.setInstrumentosExecucaoAnoBase(producoesDAO.listarInstrumenstosFisico(config.getUserEmpresa().getId(), getAnoBase(), Mes.DEZEMBRO));
        this.setInstrumentosExecucaoAnoAnterior(producoesDAO.listarInstrumenstosFisico(config.getUserEmpresa().getId(), getAnoAnterior(), getMesBase()));
        gerarTabelaExecucaoMensalAnoAnterior();
        gerarTabelaExecucaoMensalAnoBase();
    }
    
    public Producoes getProducoesDAO() {
        return producoesDAO;
    }

    public void setProducoesDAO(Producoes producoesDAO) {
        this.producoesDAO = producoesDAO;
    }

    public boolean isAnoBaseb() {
        return anoBaseb;
    }

    public void setAnoBaseb(boolean anoBaseb) {
        this.anoBaseb = anoBaseb;
    }

    public int getAnoBase() {
        return anoBase;
    }

    public void setAnoBase(int anoBase) {
        this.anoBase = anoBase;
        setAnoAnterior(anoBase-1);
    }

    public int getAnoAnterior() {
        return anoAnterior;
    }

    public void setAnoAnterior(int anoAnterior) {
        this.anoAnterior = anoAnterior;
    }

    public ArrayList getInstrumentosExecucaoAnoBase() {
        return instrumentosExecucaoAnoBase;
    }

    public void setInstrumentosExecucaoAnoBase(ArrayList instrumentosExecucaoAnoBase) {
        this.instrumentosExecucaoAnoBase = instrumentosExecucaoAnoBase;
    }

    public ArrayList getInstrumentosExecucaoAnoAnterior() {
        return instrumentosExecucaoAnoAnterior;
    }

    public void setInstrumentosExecucaoAnoAnterior(ArrayList instrumentosExecucaoAnoAnterior) {
        this.instrumentosExecucaoAnoAnterior = instrumentosExecucaoAnoAnterior;
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }

    public ArrayList getInstrumentosExecucaoAgrupado() {
        return instrumentosExecucaoAgrupado;
    }

    public void setInstrumentosExecucaoAgrupado(ArrayList instrumentosExecucaoAgrupado) {
        this.instrumentosExecucaoAgrupado = instrumentosExecucaoAgrupado;
    }

    public Mes getMesBase() {
        return mesBase;
    }

    public void setMesBase(Mes mesBase) {
        this.mesBase = mesBase;
    }

    public BigDecimal getTotalGeralAnoBaseAcumulado() {
        return totalGeralAnoBaseAcumulado;
    }

    public void setTotalGeralAnoBaseAcumulado(BigDecimal totalGeralAnoBaseAcumulado) {
        this.totalGeralAnoBaseAcumulado = totalGeralAnoBaseAcumulado;
    }

    public BigDecimal getTotalGeralAnoAnteriorAcumulado() {
        return totalGeralAnoAnteriorAcumulado;
    }

    public void setTotalGeralAnoAnteriorAcumulado(BigDecimal totalGeralAnoAnteriorAcumulado) {
        this.totalGeralAnoAnteriorAcumulado = totalGeralAnoAnteriorAcumulado;
    }

    public BigDecimal getTotalGeralDiferencaPorcentagemAcumulado() {
        return totalGeralDiferencaPorcentagemAcumulado;
    }

    public void setTotalGeralDiferencaPorcentagemAcumulado(BigDecimal totalGeralDiferencaPorcentagemAcumulado) {
        this.totalGeralDiferencaPorcentagemAcumulado = totalGeralDiferencaPorcentagemAcumulado;
    }

    public ArrayList getInstrumentosExecutadosMensal() {
        return instrumentosExecutadosMensal;
    }

    public void setInstrumentosExecutadosMensal(ArrayList instrumentosExecutadosMensal) {
        this.instrumentosExecutadosMensal = instrumentosExecutadosMensal;
    }

    public ArrayList getTotalGeralAnualInstrumentoAnoBase() {
        return totalGeralAnualInstrumentoAnoBase;
    }

    public void setTotalGeralAnualInstrumentoAnoBase(ArrayList totalGeralAnualInstrumentoAnoBase) {
        this.totalGeralAnualInstrumentoAnoBase = totalGeralAnualInstrumentoAnoBase;
    }

    public ArrayList getTotalGeralAnualInstrumentoAnoAnterior() {
        return totalGeralAnualInstrumentoAnoAnterior;
    }

    public void setTotalGeralAnualInstrumentoAnoAnterior(ArrayList totalGeralAnualInstrumentoAnoAnterior) {
        this.totalGeralAnualInstrumentoAnoAnterior = totalGeralAnualInstrumentoAnoAnterior;
    }

    public ArrayList getTotalGeralDiferencaPorcentagem() {
        return totalGeralDiferencaPorcentagem;
    }

    public void setTotalGeralDiferencaPorcentagem(ArrayList totalGeralDiferencaPorcentagem) {
        this.totalGeralDiferencaPorcentagem = totalGeralDiferencaPorcentagem;
    }

    public BigDecimal getAgrupaSomaGeralTotalAnual() {
        return agrupaSomaGeralTotalAnual;
    }

    public void setAgrupaSomaGeralTotalAnual(BigDecimal agrupaSomaGeralTotalAnual) {
        this.agrupaSomaGeralTotalAnual = agrupaSomaGeralTotalAnual;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
    
    public void loadMesBase() {
        Object[] objBase = null;
        Mes m = null;
        for (int i=0; i < instrumentosExecucaoAnoBase.size(); i++) {
            objBase = (Object[]) instrumentosExecucaoAnoBase.get(i);
            m = (Mes) objBase[5]; 
        }
        this.setMesBase(m);
    }
    
    public void agruparDadosComparativos() {
        
        String[] line = new String[20];
        Object[] objBase = null, objAnte;
        BigDecimal fator = new BigDecimal("100");
        BigDecimal totalAnoAnterior = new BigDecimal("0.00");
        BigDecimal totalAnoBase = new BigDecimal("0.00");
        BigDecimal totalGeralAnoBase = new BigDecimal("0.00");
        BigDecimal totalGeralAnoAnterior = new BigDecimal("0.00");
        
        int cnt = 0;
        
        for (int i=0; i < instrumentosExecucaoAnoBase.size(); i++) {
            objBase = (Object[]) instrumentosExecucaoAnoBase.get(i);
            objAnte = (Object[]) instrumentosExecucaoAnoAnterior.get(i);
            String instrumento = (String) objBase[0]; 
            if (instrumento.equals(Instrumento.CONSULTORIA.getDescricao())) {
               line[cnt] = "Horas " + instrumento;
            } else {
               line[cnt] = "Nº " + instrumento; 
            }   
            cnt++;
            if (instrumento.equals(Instrumento.CONSULTORIA.getDescricao())) {
               totalAnoAnterior = (BigDecimal) objAnte[4];
               totalAnoBase = (BigDecimal) objBase[4];
           } else {
               totalAnoAnterior = (BigDecimal) objAnte[3];
               totalAnoBase = (BigDecimal) objBase[3];
           }
            
           totalGeralAnoBase = totalGeralAnoBase.add(totalAnoBase);
           totalGeralAnoAnterior = totalGeralAnoAnterior.add(totalAnoAnterior);
           
           line[cnt] = NumberFormat.getInstance().format(totalAnoAnterior);
           cnt++;
           line[cnt] = NumberFormat.getInstance().format(totalAnoBase);
           cnt++;
           line[cnt] = NumberFormat.getInstance().format(totalAnoBase.subtract(totalAnoAnterior).divide(totalAnoAnterior, 4, RoundingMode.UP).multiply(fator))+"%";
           instrumentosExecucaoAgrupado.add(line);
           line = new String[20];
           cnt = 0;
        }
        this.setTotalGeralAnoBaseAcumulado(totalGeralAnoBase);
        this.setTotalGeralAnoAnteriorAcumulado(totalGeralAnoAnterior);
        this.setTotalGeralDiferencaPorcentagemAcumulado(totalGeralAnoBase.subtract(totalGeralAnoAnterior).divide(totalGeralAnoAnterior,4,RoundingMode.UP).multiply(fator));
    }
    
    
    public void gerarColunasExecucaoInstrumentos() {
        montarColunasTabela();
    }
    
     public void montarColunasTabela() {
       this.totalGeralAnualInstrumentoAnoBase.add("Total"); 
       this.totalGeralAnualInstrumentoAnoAnterior.add("Total"); 
       this.totalGeralDiferencaPorcentagem.add("Diferenca %");
       BigDecimal valorBase = new BigDecimal("0.00");
       BigDecimal valorAnte = new BigDecimal("0.00");
       getColumns().add(new ProducaoRelatorioExecucaoInstrumentoMB.ColumnModel("Mês","0"));
       int c = 1;
       
       for (int i = 0; i < instrumentosExecucaoAnoBase.size(); i++) {
           
            Object[] objectBase = (Object[]) instrumentosExecucaoAnoBase.get(i);
            Object[] objectAnte = (Object[]) instrumentosExecucaoAnoAnterior.get(i);
            String instrumento  = (String) objectBase[0];
            BigDecimal fator = new BigDecimal("100");
           
         
            getColumns().add(new ProducaoRelatorioExecucaoInstrumentoMB.ColumnModel(instrumento,Integer.toString(i+1)));
            
           
            if (instrumento.equals(Instrumento.CONSULTORIA.getDescricao())) {
              valorBase = (BigDecimal) objectBase[2];
              valorAnte = (BigDecimal) objectAnte[2];
            } else {
              valorBase = (BigDecimal) objectBase[1];
              valorAnte = (BigDecimal) objectAnte[1];
            }  
            getTotalGeralAnualInstrumentoAnoBase().add(NumberFormat.getInstance().format(valorBase));
            getTotalGeralAnualInstrumentoAnoAnterior().add(NumberFormat.getInstance().format(valorAnte));
            getTotalGeralDiferencaPorcentagem().add(NumberFormat.getInstance().format(valorBase.subtract(valorAnte).divide(valorAnte,4,RoundingMode.UP).multiply(fator))+"%");
            c++;
       }
     
       getColumns().add(new ProducaoRelatorioExecucaoInstrumentoMB.ColumnModel("Somatório Instrumentos Executados ",Integer.toString(c)));
       getColumns().add(new ProducaoRelatorioExecucaoInstrumentoMB.ColumnModel("Execução Física Correspondente à  previsão anual ajustada "+getAnoAnterior()+" e "+getAnoBase(),Integer.toString(c+1)));
    }  
     
     
    public void gerarTabelaExecucaoMensalAnoAnterior() {
       String[] line = new String[20];
        Object[] obj = null;
        
        BigDecimal totalMensal = new BigDecimal("0.00");
        BigDecimal somaTotalMensal = new BigDecimal("0.00");
        BigDecimal somaGeralTotalMensal = new BigDecimal("0.00");
        BigDecimal percentualComparativo = new BigDecimal("0.01");
        String mesmoMes = "";
        int cnt = 0, cntInstumentos = 1;
        
        for (int i = 0; i < instrumentosExecucaoAnoAnterior.size(); i++) {
               
            obj = (Object[])instrumentosExecucaoAnoAnterior.get(i);
            
            Mes m = (Mes) obj[0];
            if (!mesmoMes.equals(m.getDescricao())) {
              line[cnt] = m.getDescricao()+"("+getAnoAnterior()+")";
              cnt++;
            }
            mesmoMes = m.getDescricao();              
            
            String instrumento = (String) obj[1]; 
            if (instrumento.equals(Instrumento.CONSULTORIA.getDescricao())) {
              totalMensal = (BigDecimal) obj[3];  
            } else {
              totalMensal = (BigDecimal) obj[2];  
            }
            
            line[cnt] = NumberFormat.getIntegerInstance().format(totalMensal);
            cnt++;
            
            somaTotalMensal = somaTotalMensal.add(totalMensal);
            
            if (cntInstumentos == (columns.size()-3)) {
                
               line[cnt] = NumberFormat.getIntegerInstance().format(somaTotalMensal);
               cnt++;
               line[cnt] = percentualComparativo.toString()+"%";
               
               instrumentosExecutadosMensal.add(line); 
               line = new String[20];
               cntInstumentos = 0; cnt = 0;
               somaGeralTotalMensal = somaGeralTotalMensal.add(somaTotalMensal);
               somaTotalMensal = new BigDecimal("0.00");
            }
            cntInstumentos++;
         }
         getTotalGeralAnualInstrumentoAnoAnterior().add(NumberFormat.getInstance().format(somaGeralTotalMensal));
         this.agrupaSomaGeralTotalAnual = new BigDecimal(somaGeralTotalMensal.toString());
         
         instrumentosExecutadosMensal.add(totalGeralAnualInstrumentoAnoAnterior);
    } 
     
    public void gerarTabelaExecucaoMensalAnoBase() {
        
        String[] line = new String[20];
        Object[] obj = null;
        BigDecimal totalMensal = new BigDecimal("0.00");
        BigDecimal somaTotalMensal = new BigDecimal("0.00");
        BigDecimal somaGeralTotalMensal = new BigDecimal("0.00");
        BigDecimal percentualComparativo = new BigDecimal("0.01");
        BigDecimal fator = new BigDecimal("100");
        String mesmoMes = "";
        int cnt = 0, cntInstumentos = 1;
        
        for (int i = 0; i < instrumentosExecucaoAnoBase.size(); i++) {
               
            obj = (Object[])instrumentosExecucaoAnoBase.get(i);
            
            Mes m = (Mes) obj[0];
            if (!mesmoMes.equals(m.getDescricao())) {
              line[cnt] = m.getDescricao()+"("+getAnoBase()+")";
              cnt++;
            }
            mesmoMes = m.getDescricao();              
            
            String instrumento = (String) obj[1]; 
            if (instrumento.equals(Instrumento.CONSULTORIA.getDescricao())) {
              totalMensal = (BigDecimal) obj[3];  
            } else {
              totalMensal = (BigDecimal) obj[2];  
            }
            
            line[cnt] = NumberFormat.getIntegerInstance().format(totalMensal);
            cnt++;
            
            somaTotalMensal = somaTotalMensal.add(totalMensal);
            
            if (cntInstumentos == (columns.size()-3)) {
                
               line[cnt] = NumberFormat.getIntegerInstance().format(somaTotalMensal);
               cnt++;
               line[cnt] = percentualComparativo.toString()+"%";
               
               instrumentosExecutadosMensal.add(line); 
               line = new String[20];
               cntInstumentos = 0; cnt = 0;
               somaGeralTotalMensal = somaGeralTotalMensal.add(somaTotalMensal);
               somaTotalMensal = new BigDecimal("0.00");
            }
            cntInstumentos++;
         }
         getTotalGeralAnualInstrumentoAnoBase().add(NumberFormat.getInstance().format(somaGeralTotalMensal));
         agrupaSomaGeralTotalAnual = new BigDecimal(somaGeralTotalMensal.subtract(agrupaSomaGeralTotalAnual).divide(agrupaSomaGeralTotalAnual,4,RoundingMode.UP).multiply(fator).toString());
         
         getTotalGeralAnualInstrumentoAnoBase().add("0.01%");
         instrumentosExecutadosMensal.add(totalGeralAnualInstrumentoAnoBase);
         getTotalGeralDiferencaPorcentagem().add(NumberFormat.getInstance().format(agrupaSomaGeralTotalAnual)+"%");
       
         instrumentosExecutadosMensal.add(this.totalGeralDiferencaPorcentagem);
         
       
    } 
    
    public void montarRelatorioTexto() throws IOException{
        //   FacesContext facesContext = FacesContext.getCurrentInstance();
        //  System.out.println( facesContext.getExternalContext().getRequestContentType());
        //  System.out.println( facesContext.getPartialViewContext().getPartialResponseWriter().getWrapped().getContentType());
        // System.out.println(this.response.getOutputStream().toString() );
        //  System.out.println( facesContext.getExternalContext().getFlash().toString());
        //System.out.println( facesContext.getExternalContext().getRequestContentType());
        //Object obj = facesContext.getViewRoot();
        //setText(facesContext.getViewRoot().toString());
// Escreve o html da página no console, porém, você pode fazer o que quiser o resultado.
//setText(codigoHtml);
        //UIViewRoot view =facesContext.getViewRoot();
        //FacesContext fc = FacesContext.getCurrentInstance();
        //UIViewRoot uiViewRoot = facesContext.getViewRoot();
        //HtmlInputText inputText = (HtmlInputText) uiViewRoot.findComponent("producaoForm:relatorioExecucaoPanel");
   //  System.out.println(facesContext.getExternalContext().getRealPath("")+facesContext.getViewRoot().getViewId());
     File input = new File(facesContext.getExternalContext().getRealPath("")+facesContext.getViewRoot().getViewId());
     Document doc = Jsoup.connect("http://localhost:8080/Longevo/pages/producao/relatorioExecucaoInstrumento.faces").get();
        Elements html = doc.select("table");
    
    setText(html.html());
    

        
      
  //  }
    }
   
 
}
