/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.model;

/**
 *
 * @author Wendell
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name="consumoaguaenergia")
public class ConsumoAguaEnergia implements Serializable {
    
 private static final long serialVersionUID = 201404140102L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    @NotNull
    @Size(min=1, max=100)
    @Column(nullable = false, length = 100)
    private String documentoref;
    
    @NotNull
    @Column(nullable = false)
    private Long empresaId;
    
    //@NotNull
    //@Size(min=10, max=100)
    @Column(nullable = false, length = 100)
    private String descricao;
    
   // @NotNull
    @Column(nullable = false)
    private Long unidadeConsumidora;
    
   // @NotNull
    @Column(nullable = false)
    private Long numeroEnderecoId;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Etapa etapa;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AspectoTipo aspectoTipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private ConsumoTipo consumoTipo;

    
    @NotNull
    @Size(min=1)
    @Column(nullable = false, length = 1)
    private String grupoCliente;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GrupoTarifario grupoTarifario;
    
    //@NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxaAdicional;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal demandaContratada;

  //  @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Manutencao manutencao;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Unidade unidade;
    
    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeConsumida;
    
    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;
    
    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, length = 2)
    private Mes mes;
    
    @NotNull
    @Min(2013) @Max(value = 2030, message = "Valor incorreto!")
    @Column(nullable = false, length = 4)
    private int ano;
    
   // @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dataManutencao;
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dataCriacao;
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dataAlteracao;
    
    @Column(columnDefinition = "text")
    private String observacao;
    
    @NotNull
    @Column(nullable = false)
    private String log;
    
    @Transient
    private boolean novo;    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentoref() {
        return documentoref;
    }

    public void setDocumentoref(String documentoref) {
        this.documentoref = documentoref.toUpperCase();
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }

    public AspectoTipo getAspectoTipo() {
        return aspectoTipo;
    }

    public void setAspectoTipo(AspectoTipo aspectoTipo) {
        this.aspectoTipo = aspectoTipo;
    }

    public ConsumoTipo getConsumoTipo() {
        return consumoTipo;
    }

    public void setConsumoTipo(ConsumoTipo consumoTipo) {
        this.consumoTipo = consumoTipo;
    }

    public Manutencao getManutencao() {
        return manutencao;
    }

    public void setManutencao(Manutencao manutencao) {
        this.manutencao = manutencao;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }
    
    public Long getUnidadeConsumidora() {
        return unidadeConsumidora;
    }

    public void setUnidadeConsumidora(Long unidadeConsumidora) {
        this.unidadeConsumidora = unidadeConsumidora;
    }

    public Long getNumeroEnderecoId() {
        return numeroEnderecoId;
    }

    public void setNumeroEnderecoId(Long numeroEnderecoId) {
        this.numeroEnderecoId = numeroEnderecoId;
    }

    public BigDecimal getQuantidadeConsumida() {
        return quantidadeConsumida;
    }

    public void setQuantidadeConsumida(BigDecimal quantidadeConsumida) {
        this.quantidadeConsumida = quantidadeConsumida;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Date getDataManutencao() {
        return dataManutencao;
    }

    public void setDataManutencao(Date dataManutencao) {
        this.dataManutencao = dataManutencao;
    }
    
    public String getGrupoCliente() {
        return grupoCliente;
    }

    public void setGrupoCliente(String grupoCliente) {
        this.grupoCliente = grupoCliente;
    }

    public GrupoTarifario getGrupoTarifario() {
        return grupoTarifario;
    }

    public void setGrupoTarifario(GrupoTarifario grupoTarifario) {
        this.grupoTarifario = grupoTarifario;
    }

    public BigDecimal getTaxaAdicional() {
        return taxaAdicional;
    }

    public void setTaxaAdicional(BigDecimal taxaAdicional) {
        this.taxaAdicional = taxaAdicional;
    }

    public BigDecimal getDemandaContratada() {
        return demandaContratada;
    }

    public void setDemandaContratada(BigDecimal demandaContratada) {
        this.demandaContratada = demandaContratada;
    }
    
    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao.toUpperCase();
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public boolean isNovo() {
       return novo = getId() == null;
    }

    public void setNovo(boolean novo) {
        this.novo = novo;
    }
   
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }
       
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
            return true;
	if (obj == null)
            return false;
	if (getClass() != obj.getClass())
            return false;
	ConsumoAguaEnergia other = (ConsumoAguaEnergia) obj;
	if (id == null) {
            if (other.id != null)
            return false;
	} else if (!id.equals(other.id))
            return false;
        return true;
    }               

  
    
    
}
