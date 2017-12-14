/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Wendell
 */

@Entity
@Table(name="producaoexecucao")
public class Producao implements Serializable {
    
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
    
  //  @NotNull
  //  @Size(min=10, max=100)
    @Column(nullable = false, length = 100)
    private String descricao;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Unidade unidade;
    
    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeRealizada;
    
   // @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadePrevistaHoras;

    
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, length = 2)
    private Mes mes;
    
    @NotNull
    @Min(2013) @Max(value = 2030, message = "Valor incorreto!")
    @Column(nullable = false, length = 4)
    private int ano;
    
   // @NotNull
    @Min(0) @Max(value = 999999999, message = "Valor incorreto!")
    @Column(nullable = false, length = 11)
    private BigDecimal participantes;
    
 //   @NotNull
    @Min(0) @Max(value = 999999999, message = "Valor incorreto!")
    @Column(nullable = false, length = 11)
    private BigDecimal quantidadeHoras;
    
    @NotNull
    @Min(0) @Max(value = 999999999, message = "Valor incorreto!")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadePrevista;

    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="datacriacao",nullable = false)
    private Date dataCriacao;
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="dataalteracao",nullable = false)
    private Date dataAlteracao;
    
    @NotNull
    @Column(nullable = false)
    private String log;
    
    @Column(columnDefinition = "text")
    private String observacao;
    
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

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public BigDecimal getQuantidadeRealizada() {
        return quantidadeRealizada;
    }

    public void setQuantidadeRealizada(BigDecimal quantidadeRealizada) {
        this.quantidadeRealizada = quantidadeRealizada;
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

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao.toUpperCase();
    }
    
    public boolean isNovo() {
       return novo = getId() == null;
    }

    public void setNovo(boolean novo) {
        this.novo = novo;
    }
    
    public BigDecimal getParticipantes() {
        return participantes;
    }

    public void setParticipantes(BigDecimal participantes) {
        this.participantes = participantes;
    }

    public BigDecimal getQuantidadeHoras() {
        return quantidadeHoras;
    }

    public void setQuantidadeHoras(BigDecimal quantidadeHoras) {
        this.quantidadeHoras = quantidadeHoras;
    }

    public BigDecimal getQuantidadePrevista() {
        return quantidadePrevista;
    }

    public void setQuantidadePrevista(BigDecimal quantidadePrevista) {
        this.quantidadePrevista = quantidadePrevista;
    }

    public BigDecimal getQuantidadePrevistaHoras() {
        return quantidadePrevistaHoras;
    }

    public void setQuantidadePrevistaHoras(BigDecimal quantidadePrevistaHoras) {
        this.quantidadePrevistaHoras = quantidadePrevistaHoras;
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
	Producao other = (Producao) obj;
	if (id == null) {
            if (other.id != null)
            return false;
	} else if (!id.equals(other.id))
            return false;
        return true;
    }    

    
}
