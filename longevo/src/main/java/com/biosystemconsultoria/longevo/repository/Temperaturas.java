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

import com.biosystemconsultoria.longevo.model.Temperatura;
import com.biosystemconsultoria.longevo.service.NegocioException;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;
import javax.persistence.NoResultException;

public class Temperaturas implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager manager;
    
    @Transactional
    public void delete(Temperatura temperatura) {
        try {
            temperatura = porId(temperatura.getId());
            manager.remove(temperatura);
            manager.flush();
        } catch (PersistenceException e) {
            throw new NegocioException("Temperatura não pode ser excluído.");
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Temperatura> listar(Long endereco_id){
            
        try {
                return manager.createQuery("from Temperatura where enderecoId = :endereco_id", Temperatura.class)
				.setParameter("endereco_id", endereco_id)
				.getResultList();
            } catch (NoResultException e) {
                return null;
            }
    }
    
    public Temperatura porId(Long id) {
	return this.manager.find(Temperatura.class, id);
    } 
}
