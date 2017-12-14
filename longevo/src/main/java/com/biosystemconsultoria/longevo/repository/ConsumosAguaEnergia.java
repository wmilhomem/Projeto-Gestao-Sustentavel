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

import com.biosystemconsultoria.longevo.model.ConsumoAguaEnergia;
import com.biosystemconsultoria.longevo.model.Empresa;
import com.biosystemconsultoria.longevo.model.Endereco;
import com.biosystemconsultoria.longevo.model.Mes;
import com.biosystemconsultoria.longevo.model.Temperatura;
import com.biosystemconsultoria.longevo.repository.filter.ConsumoAguaEnergiaFilter;
import com.biosystemconsultoria.longevo.service.NegocioException;
import com.biosystemconsultoria.longevo.util.ConsumoAguaEnergiaAcumulado;
import com.biosystemconsultoria.longevo.util.jpa.Transactional;
import com.google.common.base.Strings;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;

public class ConsumosAguaEnergia implements Serializable {
    
private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
        
        @SuppressWarnings("unchecked")
	public List<ConsumoAguaEnergia> filtrados(ConsumoAguaEnergiaFilter filtro) {
            
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
	public List<ConsumoAguaEnergia> listar(){
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(ConsumoAguaEnergia.class);
            return criteria.list();
            
	}
        
         private Criteria criaCriteriaFiltrado(ConsumoAguaEnergiaFilter filtro) {
             
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(ConsumoAguaEnergia.class);
            
            if (filtro.getEmpresa() != null) {
               criteria.add(Restrictions.eq("empresaId", filtro.getEmpresa().getId()));
            }
            
            if (filtro.getConsumoTipo() != null ) {
                criteria.add(Restrictions.eq("consumoTipo", filtro.getConsumoTipo()));
            }
            
            if (filtro.getUnidadeConsumidora() != null) {
                criteria.add(Restrictions.eq("unidadeConsumidora", filtro.getUnidadeConsumidora()));
            }
            
            if (filtro.getDataInicio() != null) {
                criteria.add(Restrictions.ge("dataCriacao", filtro.getDataInicio()));
            }
            
            if (filtro.getDataFinal() != null) {
                criteria.add(Restrictions.le("dataCriacao", filtro.getDataFinal()));
            }
            
            if (filtro.getMes() != null && !filtro.getMes().equals(Mes.SELECIONE) ) {
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
        
        public int quantidadeFiltrados(ConsumoAguaEnergiaFilter filtro) {
            
            Criteria criteria = criaCriteriaFiltrado(filtro);            
            criteria.setProjection(Projections.rowCount());
            return ((Number) criteria.uniqueResult()).intValue();
	}        
         
        @SuppressWarnings("unchecked")
        public List<ConsumoAguaEnergiaAcumulado> consumoTiposAcumulado(ConsumoAguaEnergiaFilter filtro) {
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(ConsumoAguaEnergia.class);
                     
            ProjectionList pl = Projections.projectionList()   
                        .add(Projections.groupProperty("descricao").as("tipoConsumo"))
                        .add(Projections.sum("quantidadeConsumida").as("quantidadeConsumida"));
            criteria.setProjection(pl)
                    .add(Restrictions.eq("consumoTipo",filtro.getConsumoTipo()))
                    .add(Restrictions.eq("empresaId", filtro.getEmpresa().getId()))
                    .addOrder(Order.desc("tipoConsumo"))
                    .setResultTransformer(Transformers.aliasToBean(ConsumoAguaEnergiaAcumulado.class));
            return criteria.list();
            
        }        
        
        @SuppressWarnings("unchecked")
        public List<ConsumoAguaEnergiaAcumulado> consumoTiposAcumuladoAnual(ConsumoAguaEnergiaFilter filtro) {
            
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(ConsumoAguaEnergia.class);
                     
            ProjectionList pl = Projections.projectionList()   
                        .add(Projections.groupProperty("mes").as("Mes"))
                        .add(Projections.sum("quantidadeConsumida").as("quantidadeConsumida"));
            criteria.setProjection(pl)
                    .add(Restrictions.eq("consumoTipo",filtro.getConsumoTipo()))
                    .add(Restrictions.eq("ano", filtro.getAno()))
                    .add(Restrictions.eq("descricao", filtro.getDescricao()))
                    .add(Restrictions.eq("empresaId", filtro.getEmpresa().getId()))
                    .addOrder(Order.asc("mes"))
                    .setResultTransformer(Transformers.aliasToBean(ConsumoAguaEnergiaAcumulado.class));
            return criteria.list();
            
        }        
        
        
        @SuppressWarnings("unchecked")
        public BigDecimal acumuladoConsumo (ConsumoAguaEnergiaFilter filtro) {
           
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(ConsumoAguaEnergia.class);
            
            criteria.setProjection(Projections.sum("quantidadeConsumida"));
            
            if (filtro.getMes() != null && !filtro.getMes().equals(Mes.SELECIONE) ) {
		criteria.add(Restrictions.eq("mes", filtro.getMes()));
            }
            
            if (filtro.getUnidadeConsumidora() != null) {
                criteria.add(Restrictions.eq("unidadeConsumidora", filtro.getUnidadeConsumidora()));
            }
            
            criteria.add(Restrictions.eq("ano", filtro.getAno()));
            criteria.add(Restrictions.eq("descricao", filtro.getDescricao()));
            criteria.addOrder(Order.asc("mes"));
            return (BigDecimal) criteria.add(Restrictions.eq("empresaId", filtro.getEmpresa().getId())).uniqueResult();  
            
            
        }

        @SuppressWarnings("unchecked")
        public BigDecimal acumuladoTotal (ConsumoAguaEnergiaFilter filtro) {
            Session session = manager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(ConsumoAguaEnergia.class);
            
            criteria.setProjection(Projections.sum("total"));
            if (filtro.getMes() != null && !filtro.getMes().equals(Mes.SELECIONE) ) {
		criteria.add(Restrictions.eq("mes", filtro.getMes()));
            }
            
            if (filtro.getUnidadeConsumidora() != null) {
                criteria.add(Restrictions.eq("unidadeConsumidora", filtro.getUnidadeConsumidora()));
            }
            
            criteria.add(Restrictions.eq("ano", filtro.getAno()));
            criteria.add(Restrictions.eq("descricao", filtro.getDescricao()));
            criteria.addOrder(Order.asc("mes"));
            return (BigDecimal) criteria.add(Restrictions.eq("empresaId", filtro.getEmpresa().getId())).uniqueResult();              
            
         }        
        
     

        public ArrayList<Strings> localidadeUnidadeGrupo(Empresa empresa) {
            
            return (ArrayList<Strings>) this.manager.createQuery("select c.unidadeConsumidora," +
                                            "       c.grupoTarifario , " +
                                            "       c.grupoCliente , " +
                                            "	    e.logradouro, " +
                                            "       e.numero, " +
                                            "       e.id, "+
                                            "       e.matrizfilial,"+
                                            "       e.cidade"+
                                            "       from Endereco e, ConsumoAguaEnergia c "
                                          + "where e.id = c.numeroEnderecoId"
                                          + "  and e.empresa = :empresa"
                                          + " group by e.logradouro, e.numero"
                                          + " order by e.logradouro, e.numero")
				.setParameter("empresa", empresa ).getResultList();
        }
        
        public ArrayList<Strings> dadosConsumoMensalTotalizado(Long empresa, int ano) {
             return (ArrayList<Strings>) this.manager.createQuery("select mes, sum(c.quantidadeConsumida), sum(total)" +
                                          "       from ConsumoAguaEnergia c "
                                          + "where c.empresaId = :empresa"
                                          + "  and c.ano       = :anoBase"
                                          + " group by c.mes"
                                          + " order by c.mes")
				.setParameter("empresa", empresa )
                                .setParameter("anoBase", ano).getResultList();  
           
        }        
        
        public ArrayList<Strings> dadosConsumoMensal(Long empresa, int ano) {
             return (ArrayList<Strings>) this.manager.createQuery("select mes, unidadeConsumidora, quantidadeConsumida, total" +
                                          "       from ConsumoAguaEnergia c "
                                          + "where c.empresaId = :empresa"
                                          + "  and c.ano       = :anoBase"
                                          + " order by c.mes, c.unidadeConsumidora")
				.setParameter("empresa", empresa )
                                .setParameter("anoBase", ano).getResultList();  
           
        }  
        
        public ArrayList<Strings> dadosConsumoAnualTotalizado(Long empresa, int ano, Mes mes) {
             return (ArrayList<Strings>) this.manager.createQuery("select c.unidadeConsumidora, sum(c.quantidadeConsumida), sum(c.total)" +
                                          "       from ConsumoAguaEnergia c "
                                          + "where c.empresaId = :empresa"
                                          + "  and c.ano       = :anoBase"
                                          + "  and c.mes      <= :mes"
                                          + " group by c.unidadeConsumidora"
                                          + " order by c.unidadeConsumidora")
				.setParameter("empresa", empresa )
                                .setParameter("mes", mes)
                                .setParameter("anoBase", ano).getResultList();  
           
        }                
        
        public List<Temperatura> dadosTemperatura(Long endereco, int ano, Mes mes) {
                try {   
                    return this.manager.createQuery("from Temperatura where enderecoId = :endereco and ano = :anoBase and mes <= :mesBase order by mes",Temperatura.class)
				.setParameter("endereco", endereco )
                                .setParameter("anoBase", ano)
                                .setParameter("mesBase", mes).getResultList();  
                } catch (NoResultException e) {
                    return null;
		}
           
        }                  
          
        public ArrayList<BigDecimal> dadosTemperaturaMediaMaximaMinima(Long endereco, int ano, Mes mes) {
            try {
                return (ArrayList<BigDecimal>) this.manager.createQuery("select sum(maxima),sum(minima) from Temperatura where = :endereco and ano = :anoBase and mes <= :mesBase order by mes")
                                .setParameter("endereco", endereco )
                                .setParameter("anoBase", ano)
                                .setParameter("mesBase", mes).getResultList();  
                
            } catch (NoResultException e) {
                    return null;
	    }
        }
          
   
        @Transactional
	public void delete(ConsumoAguaEnergia consumo) {
            try {
		consumo = porId(consumo.getId());
		manager.remove(consumo);
		manager.flush();
            } catch (PersistenceException e) {
		throw new NegocioException("Lancamento não pode ser excluído.");
            }
	}
        
        public ConsumoAguaEnergia porDocumento(String doc, String desc) {
		try {
                    return manager.createQuery("from ConsumoAguaEnergia where documentoref = :documentoref and descricao = :descricao", ConsumoAguaEnergia.class)
				.setParameter("documentoref", doc)
                                .setParameter("descricao", desc)
				.getSingleResult();
		} catch (NoResultException e) {
                    return null;
		}
	}
        
        
       	public ConsumoAguaEnergia guardar(ConsumoAguaEnergia consumo) {
		return manager.merge(consumo);
	}
        
        public ConsumoAguaEnergia porId(Long id) {
		return this.manager.find(ConsumoAguaEnergia.class, id);
	}
    
    
}
