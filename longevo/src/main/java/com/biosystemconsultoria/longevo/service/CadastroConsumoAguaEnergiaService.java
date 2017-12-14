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

import com.biosystemconsultoria.longevo.model.ConsumoAguaEnergia;
import com.biosystemconsultoria.longevo.repository.ConsumosAguaEnergia;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;

public class CadastroConsumoAguaEnergiaService implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@Inject
	private ConsumosAguaEnergia consumoAguaEnergias;
        
	@Transactional
	public ConsumoAguaEnergia salvar(ConsumoAguaEnergia consumoAguaEnergia) {
		
            ConsumoAguaEnergia consumoAguaEnergiaExistente = consumoAguaEnergias.porDocumento(consumoAguaEnergia.getDocumentoref(),consumoAguaEnergia.getDescricao());
            // Verifica se existe uma chave gravada com o mesmo documento e tipo
            
            if (consumoAguaEnergiaExistente != null) {
                if (Objects.equals(consumoAguaEnergiaExistente.getDocumentoref(),consumoAguaEnergia.getDocumentoref())
                        && Objects.equals(consumoAguaEnergiaExistente.getDescricao(),consumoAguaEnergia.getDescricao())) {
                    
                        if (Objects.equals(consumoAguaEnergia.getId(),null )) {
                            throw new NegocioException("Existe um documento com esta referencia já lançado!");
                        }
                }
            }
        
            // Seta Data hora para novo registro ou alterado
            if (consumoAguaEnergia.isNovo()) {
                consumoAguaEnergia.setDataCriacao(new java.sql.Date(new Date().getTime()));
                consumoAguaEnergia.setDataAlteracao(consumoAguaEnergia.getDataCriacao());
            } else {
                consumoAguaEnergia.setDataAlteracao(new java.sql.Date(new Date().getTime()));
            }
            
            return consumoAguaEnergias.guardar(consumoAguaEnergia);
                
                
	}
	    
    
}
