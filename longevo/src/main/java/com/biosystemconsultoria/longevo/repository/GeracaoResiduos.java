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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biosystemconsultoria.longevo.model.GeracaoResiduo;
import com.biosystemconsultoria.longevo.model.Producao;
import com.biosystemconsultoria.longevo.repository.filter.GeracaoResiduoFilter;
import com.biosystemconsultoria.longevo.repository.filter.ProducaoFilter;
import com.biosystemconsultoria.longevo.service.NegocioException;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;
import java.math.BigDecimal;
import org.hibernate.criterion.Projections;

public class GeracaoResiduos implements Serializable {
    
private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
        
        @SuppressWarnings("unchecked")
	public List<GeracaoResiduo> filtrados(GeracaoResiduoFilter filtro) {
                
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
        
  	@SuppressWarnings("unchecked")
	public List<GeracaoResiduo> listar(){
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(GeracaoResiduo.class);
            return criteria.list();
	}
        
         private Criteria criaCriteriaFiltrado(GeracaoResiduoFilter filtro) {
             
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(GeracaoResiduo.class);
             
            if (filtro.getEmpresa() != null) {
               criteria.add(Restrictions.eq("empresaId", filtro.getEmpresa().getId()));
            }
            
            if (filtro.getResiduoTipo() != null ) {
                criteria.add(Restrictions.eq("residuoTipo", filtro.getResiduoTipo()));
            }
            
            if (filtro.getDataInicio() != null) {
                criteria.add(Restrictions.ge("dataCriacao", filtro.getDataInicio()));
            }
            
            if (filtro.getDataFinal() != null) {
                criteria.add(Restrictions.le("dataCriacao", filtro.getDataFinal()));
            }
            
            if (filtro.getMes() != null ) {
		criteria.add(Restrictions.eq("mes", filtro.getMes()));
            }
            
            if (filtro.getAno() > 0 )  {
                criteria.add(Restrictions.eq("ano", filtro.getAno()));
            }
            
            if (StringUtils.isNotBlank(filtro.getDocumento())) {
                criteria.add(Restrictions.eq("documentoref", filtro.getDocumento()));
            }
            
            if (StringUtils.isNotBlank(filtro.getDescricao())) {
                criteria.add(Restrictions.eq("descricao", filtro.getDescricao()));
            }
             
             return criteria;
        }    
        
        public int quantidadeFiltrados(GeracaoResiduoFilter filtro) {
            
            Criteria criteria = criaCriteriaFiltrado(filtro);            
            criteria.setProjection(Projections.rowCount());
            return ((Number) criteria.uniqueResult()).intValue();
	}        
         
         
        @SuppressWarnings("unchecked")
        public BigDecimal residuoTipoAcumuladoQtde (GeracaoResiduoFilter filtro) {
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(GeracaoResiduo.class);
            
            return (BigDecimal) criteria.setProjection(Projections.sum("quantidade"))
                                    .add(Restrictions.eq("mes", filtro.getMes()))
                                    .add(Restrictions.eq("ano", filtro.getAno()))
                                    .add(Restrictions.eq("descricao", filtro.getDescricao()))
                                    .add(Restrictions.eq("empresaId", filtro.getEmpresa().getId())).uniqueResult();  
            
            
         }

       @SuppressWarnings("unchecked")
        public BigDecimal residuoTipoAcumuladoCusto (GeracaoResiduoFilter filtro) {
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(GeracaoResiduo.class);
            
            return (BigDecimal) criteria.setProjection(Projections.sum("custoDestino"))
                                    .add(Restrictions.eq("mes", filtro.getMes()))
                                    .add(Restrictions.eq("ano", filtro.getAno()))
                                    .add(Restrictions.eq("descricao", filtro.getDescricao()))
                                    .add(Restrictions.eq("empresaId", filtro.getEmpresa().getId())).uniqueResult();  
            
            
         }        
        
        @Transactional
	public void delete(GeracaoResiduo residuo) {
            try {
		residuo = porId(residuo.getId());
		manager.remove(residuo);
		manager.flush();
            } catch (PersistenceException e) {
		throw new NegocioException("Lancamento não pode ser excluído.");
            }
	}
        
        public GeracaoResiduo porDocumento(String doc, String desc) {
		try {
                    return manager.createQuery("from GeracaoResiduo where documentoref = :documentoref and descricao = :descricao", GeracaoResiduo.class)
				.setParameter("documentoref", doc)
                                .setParameter("descricao", desc)
				.getSingleResult();
		} catch (NoResultException e) {
                    return null;
		}
	}
        
        
       	public GeracaoResiduo guardar(GeracaoResiduo residuo) {
		return manager.merge(residuo);
	}
        
        public GeracaoResiduo porId(Long id) {
		return this.manager.find(GeracaoResiduo.class, id);
	}
    
    
}
