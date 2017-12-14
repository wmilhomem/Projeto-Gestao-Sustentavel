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
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biosystemconsultoria.longevo.model.Empresa;
import com.biosystemconsultoria.longevo.repository.filter.EmpresaFilter;
import com.biosystemconsultoria.longevo.service.NegocioException;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Projections;

public class Empresas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
    
        @SuppressWarnings("unchecked")
	public List<Empresa> filtrados(EmpresaFilter filtro) {
           
            Criteria criteria = criaCriteriaFiltrado(filtro);
            
            criteria.setFirstResult(filtro.getPaginacaoLazy().getPrimeiroRegistro());
            criteria.setMaxResults(filtro.getPaginacaoLazy().getQuantidadeRegistros());
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);       
            
            if (filtro.getPaginacaoLazy().isAscendente() && filtro.getPaginacaoLazy().getPropriedadeOrdenacao() != null) {
                criteria.addOrder(Order.asc(filtro.getPaginacaoLazy().getPropriedadeOrdenacao()));
            } else if (filtro.getPaginacaoLazy().getPropriedadeOrdenacao() != null) {
            	criteria.addOrder(Order.desc(filtro.getPaginacaoLazy().getPropriedadeOrdenacao()));
            }  
            
            return criteria.list();
            
        }
        
        
        private Criteria criaCriteriaFiltrado(EmpresaFilter filtro) {
            
            Session session = this.manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Empresa.class);

            if (StringUtils.isNotBlank(filtro.getCidade()) || filtro.getUf() != null ) {
                Conjunction and = Restrictions.conjunction();
                criteria.createAlias("enderecos", "e");
                criteria.add(and);
            }
            
            if (StringUtils.isNotBlank(filtro.getRazaoSocial())) {
		 criteria.add(Restrictions.ilike("razaoSocial", filtro.getRazaoSocial(), MatchMode.ANYWHERE));
            }
            
            if (StringUtils.isNotBlank(filtro.getCidade())) {
                 criteria.add(Restrictions.ilike("e.cidade", filtro.getCidade()));
            }
            
            if (filtro.getUf() != null) {
                 criteria.add(Restrictions.eq("e.uf", filtro.getUf()));
            }
            
            if (filtro.getPorte() != null) {
                 criteria.add(Restrictions.eq("porteEmpresa", filtro.getPorte()));
            }
            

             return criteria;
        }
        
        
        public int quantidadeFiltrados(EmpresaFilter filtro) {
            
            Criteria criteria = criaCriteriaFiltrado(filtro);            
            criteria.setProjection(Projections.rowCount());
            return ((Number) criteria.uniqueResult()).intValue();
	}        
        
        @SuppressWarnings("unchecked")
        public List<Empresa> filtradosProj(EmpresaFilter filtro) {
            
            Session session = this.manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Empresa.class);
            
            criteria.setProjection(Projections.distinct(Projections.property("razaoSocial"))); 
            
            
            
            return criteria.addOrder(Order.asc("razaoSocial")).list();
        }
        
        
  	@SuppressWarnings("unchecked")
	public List<Empresa> listar(){
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Empresa.class);
            return criteria.list();
	}
        
        @Transactional
	public void delete(Empresa empresa) {
            try {
		empresa = porId(empresa.getId());
		manager.remove(empresa);
		manager.flush();
            } catch (PersistenceException e) {
		throw new NegocioException("Empresa não pode ser excluída!");
            }
	}
        
        public Empresa porCnpj(String cnpj) {
		try {
                    return manager.createQuery("from Empresa where cnpj = :cnpj", Empresa.class)
				.setParameter("cnpj", cnpj)
				.getSingleResult();
		} catch (NoResultException e) {
                    return null;
		}
	}
        
        public List<Empresa> porRazaoSocial(String razao) {
		return this.manager.createQuery("from Empresa where upper(razaoSocial) like :razao", Empresa.class)
				.setParameter("razao", razao.toUpperCase() + "%").getResultList();
	}
	
        
       	public Empresa guardar(Empresa empresa) {
		return manager.merge(empresa);
	}        
        
        
        public Empresa porId(Long id) {
		return this.manager.find(Empresa.class, id);
	}
}
