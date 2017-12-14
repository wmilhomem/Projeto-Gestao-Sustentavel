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

import com.biosystemconsultoria.longevo.model.Aquisicao;
import com.biosystemconsultoria.longevo.repository.Aquisicoes;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;

public class CadastroAquisicaoService implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@Inject
	private Aquisicoes aquisicoes;
        
	@Transactional
	public Aquisicao salvar(Aquisicao aquisicao) {
		
            Aquisicao aquisicaoExistente = aquisicoes.porDocumento(aquisicao.getDocumentoref());
            // Verifica se existe ja existe uma chave gravada com o mesmo nome
            if (aquisicaoExistente != null) {
                if (Objects.equals(aquisicaoExistente.getDocumentoref(),aquisicao.getDocumentoref())) {
                    if (Objects.equals(aquisicao.getId(),null )) {
                        throw new NegocioException("Existe um documento com esta referencia ja lançado!");
                    }
                }
            }
        
            // Seta Data hora para novo registro ou alterado
            if (aquisicao.isNovo()) {
                aquisicao.setDataCriacao(new java.sql.Date(new Date().getTime()));
                aquisicao.setDataAlteracao(aquisicao.getDataCriacao());
            } else {
                aquisicao.setDataAlteracao(new java.sql.Date(new Date().getTime()));
            }
            
            return aquisicoes.guardar(aquisicao);
                
                
	}
	    
    
}
