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

import java.io.Serializable;

import java.util.Date;
import javax.inject.Inject;
import java.util.Objects;

import com.biosystemconsultoria.longevo.model.Producao;
import com.biosystemconsultoria.longevo.repository.Producoes;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;

public class CadastroProducaoService implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@Inject
	private Producoes producoes;
        
	@Transactional
	public Producao salvar(Producao producao) {
		
            Producao producaoExistente = producoes.porDocumento(producao.getDocumentoref(), producao.getDescricao());
            // Verifica se existe ja existe uma chave gravada com o mesmo documento e tipo
            if (producaoExistente != null) {
                if (Objects.equals(producaoExistente.getDocumentoref(),producao.getDocumentoref()) 
                        && Objects.equals(producaoExistente.getDescricao(),producao.getDescricao())) {
                    
                        if (Objects.equals(producao.getId(),null )) {
                            throw new NegocioException("Existe um documento com esta referencia ja lançado!");
                        }
                }
            }
        
            // Seta Data hora para novo registro ou alterado
            if (producao.isNovo()) {
                producao.setDataCriacao(new java.sql.Date(new Date().getTime()));
                producao.setDataAlteracao(producao.getDataCriacao());
            } else {
                producao.setDataAlteracao(new java.sql.Date(new Date().getTime()));
            }
            
            return producoes.guardar(producao);
                
                
	}
	    
    
}
