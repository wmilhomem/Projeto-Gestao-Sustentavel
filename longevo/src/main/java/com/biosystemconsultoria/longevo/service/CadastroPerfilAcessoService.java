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

import javax.inject.Inject;
import java.util.Objects;
import java.util.Date;

import com.biosystemconsultoria.longevo.model.PerfilAcesso;
import com.biosystemconsultoria.longevo.repository.PerfilAcessos;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;


public class CadastroPerfilAcessoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PerfilAcessos perfilAcessos;
	
	@Transactional
	public PerfilAcesso salvar(PerfilAcesso perfilAcesso) {
		
            PerfilAcesso perfilExistente = perfilAcessos.porNome(perfilAcesso.getDescricao());
            // Verifica se existe ja existe uma chave gravada com o mesmo nome
            if (perfilExistente != null) {
                if (Objects.equals(perfilExistente.getDescricao(), perfilAcesso.getDescricao())) {
                    if (Objects.equals(perfilAcesso.getId(),null )) {
                        throw new NegocioException("Já existe um perfil com este nome!");
                    }
                }
            }
        
            // Seta Data hora e log para novo registro
            if (perfilAcesso.isNovo()) {
                
                perfilAcesso.setDataCriacao(new java.sql.Date(new Date().getTime()));
                perfilAcesso.setDataAlteracao(new java.sql.Date(new Date().getTime()));
                perfilAcesso.setLog(FacesUtil.getUsuarioSessao());
            } else {
                 
                perfilAcesso.setDataAlteracao(new java.sql.Date(new Date().getTime()));
            }
            
            return perfilAcessos.guardar(perfilAcesso);
                
                
	}
	
}
