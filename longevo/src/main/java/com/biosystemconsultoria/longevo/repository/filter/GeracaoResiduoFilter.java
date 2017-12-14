/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.repository.filter;

/**
 *
 * @author Wendell
 */


import com.biosystemconsultoria.longevo.util.ParametrosPaginacaoLazy;
import java.io.Serializable;
import java.util.Date;

import com.biosystemconsultoria.longevo.model.ResiduoTipo;
import com.biosystemconsultoria.longevo.model.Empresa;
import com.biosystemconsultoria.longevo.model.Mes;

public class GeracaoResiduoFilter implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Empresa empresa; 
    private Date    dataInicio;
    private Date    dataFinal;
    private Mes     mes;
    private int     ano;
    private String  documento;
    private ResiduoTipo residuoTipo;
    private String  descricao;
    private ParametrosPaginacaoLazy paginacaoLazy;
    
    public GeracaoResiduoFilter() {
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

    public ResiduoTipo getResiduoTipo() {
        return residuoTipo;
    }

    public void setResiduoTipo(ResiduoTipo residuoTipo) {
        this.residuoTipo = residuoTipo;
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
