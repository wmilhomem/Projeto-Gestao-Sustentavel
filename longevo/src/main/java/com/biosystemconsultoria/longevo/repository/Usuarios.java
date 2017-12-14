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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biosystemconsultoria.longevo.model.Usuario;
import com.biosystemconsultoria.longevo.service.NegocioException;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;

public class Usuarios implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
        @SuppressWarnings("unchecked")
	public List<Usuario> ilike(String filtro) {
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Usuario.class);
                
            criteria.add(Restrictions.ilike("nome", filtro, MatchMode.ANYWHERE));
            return criteria.addOrder(Order.asc("nome")).list();
	}
        
  	@SuppressWarnings("unchecked")
	public List<Usuario> listar(){
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Usuario.class);
            return criteria.list();
	}
        
        @Transactional
	public void delete(Usuario usuario) {
            try {
		usuario = porId(usuario.getId());
		manager.remove(usuario);
		manager.flush();
            } catch (PersistenceException e) {
		throw new NegocioException("Usuario não pode ser excluído.");
            }
	}
        
        public Usuario porUsername(String user) {
            try {
                return manager.createQuery("from Usuario where username = :username", Usuario.class)
				.setParameter("username", user)
				.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
	}
        
        public Usuario porUsernameAndPassword(String username, String password) {
            try {
                return manager.createQuery("from Usuario u where u.username = :username and u.senha = :password", Usuario.class)
				.setParameter("username", username)
                                .setParameter("senha", password)
				.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
        
       	public Usuario guardar(Usuario usuario) {
		return manager.merge(usuario);
	}	
        
        public Usuario porId(Long id) {
		return this.manager.find(Usuario.class, id);
	}
	
	
}