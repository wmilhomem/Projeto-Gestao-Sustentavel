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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biosystemconsultoria.longevo.model.PerfilAcesso;
import com.biosystemconsultoria.longevo.service.NegocioException;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;

public class PerfilAcessos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
        
        @SuppressWarnings("unchecked")
	public List<PerfilAcesso> ilike(String filtro) {
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(PerfilAcesso.class);
                
            criteria.add(Restrictions.ilike("descricao", filtro, MatchMode.ANYWHERE));
            return criteria.addOrder(Order.asc("descricao")).list();
	}
        
  	@SuppressWarnings("unchecked")
	public List<PerfilAcesso> listar(){
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(PerfilAcesso.class);
            return criteria.list();
	}
        
        @Transactional
	public void delete(PerfilAcesso perfilacesso) {
            try {
		perfilacesso = porId(perfilacesso.getId());
		manager.remove(perfilacesso);
		manager.flush();
            } catch (PersistenceException e) {
		throw new NegocioException("Perfil acesso não pode ser excluído.");
            }
	}
        
        public PerfilAcesso porNome(String perfil) {
		try {
                    return manager.createQuery("from PerfilAcesso where descricao = :descricao", PerfilAcesso.class)
				.setParameter("descricao", perfil)
				.getSingleResult();
		} catch (NoResultException e) {
                    return null;
		}
	}
        
        
       	public PerfilAcesso guardar(PerfilAcesso perfilAcesso) {
		return manager.merge(perfilAcesso);
	}
        
        public PerfilAcesso porId(Long id) {
		return this.manager.find(PerfilAcesso.class, id);
	}

      
}
