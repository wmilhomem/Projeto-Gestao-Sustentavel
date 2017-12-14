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

import com.biosystemconsultoria.longevo.model.Empresa;
import com.biosystemconsultoria.longevo.model.Mes;
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

import com.biosystemconsultoria.longevo.model.Producao;
import com.biosystemconsultoria.longevo.repository.filter.ProducaoFilter;
import com.biosystemconsultoria.longevo.service.NegocioException;
import com.biosystemconsultoria.longevo.util.ProducaoAcumulado;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;
import com.google.common.base.Strings;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;

public class Producoes implements Serializable {
    
private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
        
        @SuppressWarnings("unchecked")
	public List<Producao> filtrados(ProducaoFilter filtro) {
            
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
        
        public int quantidadeFiltrados(ProducaoFilter filtro) {
            
            Criteria criteria = criaCriteriaFiltrado(filtro);            
            criteria.setProjection(Projections.rowCount());
            return ((Number) criteria.uniqueResult()).intValue();
	}        
        
        
  	@SuppressWarnings("unchecked")
	public List<Producao> listar(){
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Producao.class);
            return criteria.list();
	}
        
        private Criteria criaCriteriaFiltrado(ProducaoFilter filtro) {
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Producao.class);

            if (filtro.getEmpresa() != null) {
               criteria.add(Restrictions.eq("empresaId", filtro.getEmpresa().getId()));
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
            
            if (StringUtils.isNotBlank(filtro.getDescricao())) {
                criteria.add(Restrictions.eq("descricao", filtro.getDescricao()));
            }
            
            if (StringUtils.isNotBlank(filtro.getDocumento())) {
                criteria.add(Restrictions.eq("documentoref", filtro.getDocumento()));
            }
            
            return criteria;
         }
        
        
         @SuppressWarnings("unchecked")
        public List<ProducaoAcumulado> producaoTiposAcumuladoAnual(ProducaoFilter filtro) {
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Producao.class);
                     
            ProjectionList pl = Projections.projectionList()   
                        .add(Projections.groupProperty("mes").as("Mes"))
                        .add(Projections.sum("quantidadeRealizada").as("quantidadeRealizada"));
            criteria.setProjection(pl)
                    .add(Restrictions.eq("ano", filtro.getAno()))
                    .add(Restrictions.eq("descricao", filtro.getDescricao()))
                    .add(Restrictions.eq("empresaId", filtro.getEmpresa().getId()))
                    .addOrder(Order.asc("mes"))
                    .setResultTransformer(Transformers.aliasToBean(ProducaoAcumulado.class));
            return criteria.list();
            
        }        
        
         
        @SuppressWarnings("unchecked")
        public List<Producao> producaoTiposAcumulada(ProducaoFilter filtro) {
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Producao.class);
            
            return criteria.setProjection(Projections.projectionList()
                           .add((Projection) Restrictions.eq("empresaId",filtro.getEmpresa().getId()))
                           .add(Projections.sum("quantidadePrevista"))
                           .add(Projections.sum("quantidadeRealizada"))
                           .add(Projections.sum("quantidadeHoras"))
                           .add(Projections.groupProperty("descricao"))).list();
            
        }
        
        @SuppressWarnings("unchecked")
        public BigDecimal producaoTipoAcumulada (ProducaoFilter filtro) {
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Producao.class);
            
            return (BigDecimal) criteria.setProjection(Projections.sum("quantidadePrevista"))
                                    .add(Restrictions.eq("mes", filtro.getMes()))
                                    .add(Restrictions.eq("ano", filtro.getAno()))
                                    .add(Restrictions.eq("descricao", filtro.getDescricao()))
                                    .add(Restrictions.eq("empresaId", filtro.getEmpresa().getId())).uniqueResult();  
            
            
         }

        @SuppressWarnings("unchecked")
        public BigDecimal producaoTipoAcumuladoRealizado (ProducaoFilter filtro) {
           
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Producao.class);
            
            criteria.setProjection(Projections.sum("quantidadeRealizada"));
            
            if (filtro.getMes() != null && !filtro.getMes().equals(Mes.SELECIONE) ) {
		criteria.add(Restrictions.eq("mes", filtro.getMes()));
            }

            if (filtro.getDescricao() != null) {
                criteria.add(Restrictions.eq("descricao", filtro.getDescricao()));
            }
         
            criteria.add(Restrictions.eq("ano", filtro.getAno()));
            criteria.addOrder(Order.asc("mes"));
            return (BigDecimal) criteria.add(Restrictions.eq("empresaId", filtro.getEmpresa().getId())).uniqueResult();              
            
         }
         
         
         @SuppressWarnings("unchecked")
         public BigDecimal producaoTipoAcumuladoPrevisto (ProducaoFilter filtro) {
             
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Producao.class);
            
            if (filtro.getDescricao() != null) {
                criteria.setProjection(Projections.max("quantidadePrevista"));
            } else {
                criteria.setProjection(Projections.sum("quantidadePrevista"));
            }
            
            if (filtro.getMes() != null && !filtro.getMes().equals(Mes.SELECIONE) ) {
		criteria.add(Restrictions.eq("mes", filtro.getMes()));
            }

            if (filtro.getDescricao() != null) {
                criteria.add(Restrictions.eq("descricao", filtro.getDescricao()));
            }
            
            criteria.add(Restrictions.eq("ano", filtro.getAno()));
            criteria.addOrder(Order.asc("mes"));
            return (BigDecimal) criteria.add(Restrictions.eq("empresaId", filtro.getEmpresa().getId())).uniqueResult();               
         
         }

         @SuppressWarnings("unchecked")
         public BigDecimal producaoTipoAcumuladoHora (ProducaoFilter filtro) {
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Producao.class);
            
            criteria.setProjection(Projections.sum("quantidadeHoras"));
            if (filtro.getMes() != null && !filtro.getMes().equals(Mes.SELECIONE) ) {
		criteria.add(Restrictions.eq("mes", filtro.getMes()));
            }
            
            if (filtro.getDescricao() != null) {
                criteria.add(Restrictions.eq("descricao", filtro.getDescricao()));
            }            
            
            criteria.add(Restrictions.eq("ano", filtro.getAno()));
            criteria.addOrder(Order.asc("mes"));
            return (BigDecimal) criteria.add(Restrictions.eq("empresaId", filtro.getEmpresa().getId())).uniqueResult();               
         }
         
         @SuppressWarnings("unchecked")
         public BigDecimal producaoTipoAcumuladoPrevistoHora (ProducaoFilter filtro) {
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Producao.class);
            
       
            if (filtro.getDescricao() != null) {
                criteria.setProjection(Projections.max("quantidadePrevistaHoras"));
            } else {
                criteria.setProjection(Projections.sum("quantidadePrevistaHoras"));
            }   
            
            if (filtro.getMes() != null && !filtro.getMes().equals(Mes.SELECIONE) ) {
		criteria.add(Restrictions.eq("mes", filtro.getMes()));
            }
           
            if (filtro.getDescricao() != null) {
                criteria.add(Restrictions.eq("descricao", filtro.getDescricao()));
            }            
            criteria.add(Restrictions.eq("ano", filtro.getAno()));
            criteria.addOrder(Order.asc("mes"));
            return (BigDecimal) criteria.add(Restrictions.eq("empresaId", filtro.getEmpresa().getId())).uniqueResult();               
         }         

         @SuppressWarnings("unchecked")
         public BigDecimal producaoTipoAcumuladoParticipante (ProducaoFilter filtro) {
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Producao.class);
            
            criteria.setProjection(Projections.sum("participantes"));
            if (filtro.getMes() != null && !filtro.getMes().equals(Mes.SELECIONE) ) {
		criteria.add(Restrictions.eq("mes", filtro.getMes()));
            }
            
            criteria.add(Restrictions.eq("ano", filtro.getAno()));
            criteria.add(Restrictions.eq("descricao", filtro.getDescricao()));
            criteria.addOrder(Order.asc("mes"));
            return (BigDecimal) criteria.add(Restrictions.eq("empresaId", filtro.getEmpresa().getId())).uniqueResult();                  
            
         }
         
         
         public ArrayList<Strings> agruparInstrumenstosFisico(Long empresa, int ano, Mes mes) {
             return (ArrayList<Strings>) this.manager.createQuery("select descricao, sum(quantidadeRealizada),"+
                                                                  "                  sum(quantidadeHoras),"+
                                                                  "                  max(quantidadePrevista),"+
                                                                  "                  max(quantidadePrevistaHoras), max(mes) from Producao "+
                                                                  "                  where empresaId = :empresa "+
                                                                  "                    and     ano = :anoBase "+
                                                                  "                    and     mes <= :mesBase "+
                                                                  "                  group by descricao")
                                                                .setParameter("empresa", empresa )
                                                                .setParameter("anoBase", ano)
                                                                .setParameter("mesBase", mes).getResultList();
         }
         
        public ArrayList<Strings> listarInstrumenstosFisico(Long empresa, int ano, Mes mes) {
             return (ArrayList<Strings>) this.manager.createQuery("select mes, descricao, quantidadeRealizada,"+
                                                                  "                  quantidadeHoras,"+
                                                                  "                  quantidadePrevista,"+
                                                                  "                  quantidadePrevistaHoras from Producao "+
                                                                  "                  where empresaId = :empresa "+
                                                                  "                    and     ano = :anoBase "+
                                                                  "                    and     mes <= :mesBase "+
                                                                  "                  order by mes, descricao")
                                                                .setParameter("empresa", empresa )
                                                                .setParameter("anoBase", ano)
                                                                .setParameter("mesBase", mes).getResultList();
         }
         
         
        @Transactional
	public void delete(Producao producao) {
            try {
		producao = porId(producao.getId());
		manager.remove(producao);
		manager.flush();
            } catch (PersistenceException e) {
		throw new NegocioException("Lancamento não pode ser excluído.");
            }
	}
        
        public Producao porDocumento(String doc, String desc) {
		try {
                    return manager.createQuery("from Producao where documentoref = :documentoref and descricao = :descricao", Producao.class)
				.setParameter("documentoref", doc)
                                .setParameter("descricao", desc)
				.getSingleResult();
		} catch (NoResultException e) {
                    return null;
		}
	}
        
        
       	public Producao guardar(Producao producao) {
		return manager.merge(producao);
	}
        
        public Producao porId(Long id) {
		return this.manager.find(Producao.class, id);
	}
    
    
}
