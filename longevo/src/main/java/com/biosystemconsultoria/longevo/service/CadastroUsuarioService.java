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

import com.biosystemconsultoria.longevo.model.Usuario;
import com.biosystemconsultoria.longevo.repository.Usuarios;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;
import com.biosystemconsultoria.longevo.util.jsf.FacesUtil;
import java.util.Date;


public class CadastroUsuarioService implements Serializable  {
    
    private static final long serialVersionUID = 1L;

    @Inject
    private Usuarios usuarios;
    
    
    @Transactional
    public Usuario salvar(Usuario usuario) {            
        
        Usuario usuarioExistente = usuarios.porUsername(usuario.getUsername());
            // Verifica se existe ja existe uma chave gravada com o mesmo username
           if (usuarioExistente != null) {
                if (Objects.equals(usuarioExistente.getUsername(), usuario.getUsername())) {
                    if (Objects.equals(usuario.getId(),null )) {
                        throw new NegocioException("Já existe um usuario com este username!");
                    }
                }
            }
        
  // Seta Data hora e log para novo registro
        if (usuario.isNovo()) {
                
            usuario.setDataCriacao(new java.sql.Date(new Date().getTime()));
            usuario.setDataAlteracao(new java.sql.Date(new Date().getTime()));
            usuario.setLog(FacesUtil.getUsuarioSessao());
        } else {
            
            usuario.setDataAlteracao(new java.sql.Date(new Date().getTime()));
        }
                       
        return usuarios.guardar(usuario);
                
                
    }
	
}
