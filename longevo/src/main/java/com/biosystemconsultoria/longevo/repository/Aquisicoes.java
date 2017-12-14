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

import com.biosystemconsultoria.longevo.model.Aquisicao;
import com.biosystemconsultoria.longevo.repository.filter.AquisicaoFilter;
import com.biosystemconsultoria.longevo.service.NegocioException;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;
import org.hibernate.criterion.Projections;

public class Aquisicoes implements Serializable {
    
private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
        
        @SuppressWarnings("unchecked")
	public List<Aquisicao> filtrados(AquisicaoFilter filtro) {
            
            Criteria criteria = criaCriteriaFiltrado(filtro);
            criteria.setFirstResult(filtro.getPaginacaoLazy().getPrimeiroRegistro());
            criteria.setMaxResults(filtro.getPaginacaoLazy().getQuantidadeRegistros());
            
            if (filtro.getPaginacaoLazy().isAscendente() && filtro.getPaginacaoLazy().getPropriedadeOrdenacao() != null) {
                criteria.addOrder(Order.asc(filtro.getPaginacaoLazy().getPropriedadeOrdenacao()));
            } else if (filtro.getPaginacaoLazy().getPropriedadeOrdenacao() != null) {
            	criteria.addOrder(Order.desc(filtro.getPaginacaoLazy().getPropriedadeOrdenacao()));
            }  
              
            
            return criteria.list();
	}
        
        public int quantidadeFiltrados(AquisicaoFilter filtro) {
            
            Criteria criteria = criaCriteriaFiltrado(filtro);            
            criteria.setProjection(Projections.rowCount());
            return ((Number) criteria.uniqueResult()).intValue();
	}        
        
        
  	@SuppressWarnings("unchecked")
	public List<Aquisicao> listar(){
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Aquisicao.class);
            return criteria.list();
	}
        
        private Criteria criaCriteriaFiltrado(AquisicaoFilter filtro) {
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Aquisicao.class);
            
            if (filtro.getEmpresa() != null) {
               criteria.add(Restrictions.eq("empresaId", filtro.getEmpresa().getId()));
            }
            
            if (filtro.getMes() != null ) {
		criteria.add(Restrictions.eq("mes", filtro.getMes()));
            }
            
            if (filtro.getAno() > 0 )  {
                criteria.add(Restrictions.eq("ano", filtro.getAno()));
            }

            if (StringUtils.isNotBlank(filtro.getDescricao())) {
                criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
            }
            
            if (StringUtils.isNotBlank(filtro.getDocumento())) {
                criteria.add(Restrictions.eq("documentoref", filtro.getDocumento()));
            }
            
            
            if (filtro.getDataInicio() != null) {
                criteria.add(Restrictions.ge("dataCriacao", filtro.getDataInicio()));
            }
            
            if (filtro.getDataFinal() != null) {
                criteria.add(Restrictions.le("dataCriacao", filtro.getDataFinal()));
            }
            
            return criteria;
        }
        
        
        @Transactional
	public void delete(Aquisicao aquisicao) {
            try {
		aquisicao = porId(aquisicao.getId());
		manager.remove(aquisicao);
		manager.flush();
            } catch (PersistenceException e) {
		throw new NegocioException("Lancamento não pode ser excluído.");
            }
	}
        
        public Aquisicao porDocumento(String doc) {
		try {
                    return manager.createQuery("from Aquisicao where documentoref = :documentoref", Aquisicao.class)
				.setParameter("documentoref", doc)
				.getSingleResult();
		} catch (NoResultException e) {
                    return null;
		}
	}
        
        
       	public Aquisicao guardar(Aquisicao aquisicao) {
		return manager.merge(aquisicao);
	}
        
        public Aquisicao porId(Long id) {
		return this.manager.find(Aquisicao.class, id);
	}
    
    
}
