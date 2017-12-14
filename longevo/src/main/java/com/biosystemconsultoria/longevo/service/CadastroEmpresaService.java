/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.service;

/**
 *
 * @author Wendell
 */

import java.util.Date;
import java.io.Serializable;

import javax.inject.Inject;
import java.util.Objects;

import com.biosystemconsultoria.longevo.model.Empresa;
import com.biosystemconsultoria.longevo.repository.Empresas;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;


public class CadastroEmpresaService implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Inject
    private Empresas empresas;
    
    @Transactional
    public Empresa salvar(Empresa empresa) {
        
            Empresa empresaExistente = empresas.porCnpj(empresa.getCnpj());
            // Verifica se existe ja existe uma chave gravada com o mesmo cnpj
            if (empresaExistente != null) {
                if (Objects.equals(empresaExistente.getCnpj(), empresa.getCnpj())) {
                    if (Objects.equals(empresa.getId(),null )) {
                        throw new NegocioException("Já existe este Cnpj cadastrado no banco de dados!");
                    }
                }
            }
            
            if (empresa.getEnderecos().isEmpty()) {
		throw new NegocioException("A Empresa deve possuir pelo menos um endereço!");
            }
		
        
            // Seta Data hora e log para novo registro
            if (empresa.isNovo()) {
                
                empresa.setDataCriacao(new java.sql.Date(new Date().getTime()));
                empresa.setDataAlteracao(new java.sql.Date(new Date().getTime()));
                empresa.setLog(FacesUtil.getUsuarioSessao());
                
            } else {
                
                empresa.setDataAlteracao(new java.sql.Date(new Date().getTime()));
            }
            
            
            return empresas.guardar(empresa);
                
                
    }
   
	
}
