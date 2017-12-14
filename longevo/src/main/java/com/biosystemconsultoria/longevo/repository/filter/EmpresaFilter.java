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

import com.biosystemconsultoria.longevo.model.PorteEmpresa;
import com.biosystemconsultoria.longevo.model.Estado;


public class EmpresaFilter implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String razaoSocial;
    private PorteEmpresa porte;
    private String cidade;
    private Estado uf;
    private ParametrosPaginacaoLazy paginacaoLazy;
 
    
    public EmpresaFilter() {
        paginacaoLazy = new ParametrosPaginacaoLazy();
    }
    
    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
    
    public PorteEmpresa getPorte() {
        return porte;
    }

    public void setPorte(PorteEmpresa porte) {
        this.porte = porte;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Estado getUf() {
        return uf;
    }

    public void setUf(Estado uf) {
        this.uf = uf;
    }

    public ParametrosPaginacaoLazy getPaginacaoLazy() {
        return paginacaoLazy;
    }

    public void setPaginacaoLazy(ParametrosPaginacaoLazy paginacaoLazy) {
        this.paginacaoLazy = paginacaoLazy;
    }


    
    
    
}
