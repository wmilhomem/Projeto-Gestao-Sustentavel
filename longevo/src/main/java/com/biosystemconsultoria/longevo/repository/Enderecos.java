/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.repository;

/**
 *
 * @author Wendell
 */

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.biosystemconsultoria.longevo.model.Endereco;
import com.biosystemconsultoria.longevo.service.NegocioException;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;
import javax.persistence.NoResultException;

public class Enderecos implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager manager;
    
    @Transactional
    public void delete(Endereco endereco) {
        try {
            endereco = porId(endereco.getId());
            manager.remove(endereco);
            manager.flush();
        } catch (PersistenceException e) {
            throw new NegocioException("Endereco não pode ser excluído.");
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Endereco> listar(Long empresa_id){
            
        try {
                return manager.createQuery("from Endereco where empresaId = :empresa_id", Endereco.class)
				.setParameter("empresa_id", empresa_id)
				.getResultList();
            } catch (NoResultException e) {
                return null;
            }
    }
    
    public Endereco porId(Long id) {
	return this.manager.find(Endereco.class, id);
    }
}
