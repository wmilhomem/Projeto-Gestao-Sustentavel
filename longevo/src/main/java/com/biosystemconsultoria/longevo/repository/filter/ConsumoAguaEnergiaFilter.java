
package com.biosystemconsultoria.longevo.repository.filter;


/**
 *
 * @author Wendell
 */

import com.biosystemconsultoria.longevo.util.ParametrosPaginacaoLazy;
import com.biosystemconsultoria.longevo.model.ConsumoTipo;
import java.util.Date;
import java.io.Serializable;

import com.biosystemconsultoria.longevo.model.Empresa;
import com.biosystemconsultoria.longevo.model.Mes;

public class ConsumoAguaEnergiaFilter implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Empresa empresa; 
    private Date    dataInicio;
    private Date    dataFinal;
    private Mes     mes;
    private int     ano;
    private Long     unidadeConsumidora;
    private String  documento;
    private ConsumoTipo consumoTipo;
    private String  descricao;
    private ParametrosPaginacaoLazy paginacaoLazy;

    public ConsumoAguaEnergiaFilter() {
        paginacaoLazy = new ParametrosPaginacaoLazy();
    }
    
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public ConsumoTipo getConsumoTipo() {
        return consumoTipo;
    }

    public void setConsumoTipo(ConsumoTipo consumoTipo) {
        this.consumoTipo = consumoTipo;
    }

    public Long getUnidadeConsumidora() {
        return unidadeConsumidora;
    }

    public void setUnidadeConsumidora(Long unidadeConsumidora) {
        this.unidadeConsumidora = unidadeConsumidora;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ParametrosPaginacaoLazy getPaginacaoLazy() {
        return paginacaoLazy;
    }

    public void setPaginacaoLazy(ParametrosPaginacaoLazy paginacaoLazy) {
        this.paginacaoLazy = paginacaoLazy;
    }

  
   
    
    
}
