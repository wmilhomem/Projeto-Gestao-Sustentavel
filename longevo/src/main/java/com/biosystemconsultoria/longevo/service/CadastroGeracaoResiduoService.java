/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.service;

import com.biosystemconsultoria.longevo.model.GeracaoResiduo;
import com.biosystemconsultoria.longevo.repository.GeracaoResiduos;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.inject.Inject;

/**
 *
 * @author Wendell
 */
public class CadastroGeracaoResiduoService implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private GeracaoResiduos geracaoResiduos;
        
	@Transactional
	public GeracaoResiduo salvar(GeracaoResiduo geracaoResiduo) {
		
            GeracaoResiduo geracaoResiduoExistente = geracaoResiduos.porDocumento(geracaoResiduo.getDocumentoref(),geracaoResiduo.getDescricao());
            // Verifica se existe ja existe uma chave gravada com o mesmo documento e tipo
            
            if (geracaoResiduoExistente != null) {
                if (Objects.equals(geracaoResiduoExistente.getDocumentoref(),geracaoResiduo.getDocumentoref())
                        && Objects.equals(geracaoResiduoExistente.getDescricao(),geracaoResiduo.getDescricao())) {
                    
                        if (Objects.equals(geracaoResiduo.getId(),null )) {
                            throw new NegocioException("Existe um documento com esta referencia ja lançado!");
                        }
                }
            }
        
            // Seta Data hora para novo registro ou alterado
            if (geracaoResiduo.isNovo()) {
                geracaoResiduo.setDataCriacao(new java.sql.Date(new Date().getTime()));
                geracaoResiduo.setDataAlteracao(geracaoResiduo.getDataCriacao());
            } else {
                geracaoResiduo.setDataAlteracao(new java.sql.Date(new Date().getTime()));
            }
            
            return geracaoResiduos.guardar(geracaoResiduo);
                
                
	}    
}
