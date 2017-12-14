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
import java.util.Date;
import java.math.BigDecimal;
        
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
    
@Entity
@Table(name="aquisicao")
public class Aquisicao implements Serializable {
    
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
    
    @NotNull
    @Size(min=5, max=100)
    @Column(nullable = false, length = 100)
    private String descricao;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Unidade unidade;
    
    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;
    
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
        this.documentoref = documentoref;
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
        this.descricao = descricao.toUpperCase();
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
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
    
   public boolean isNovo() {
	return novo = getId() == null;
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
	Aquisicao other = (Aquisicao) obj;
	if (id == null) {
		if (other.id != null)
			return false;
	} else if (!id.equals(other.id))
		return false;
        return true;
    }

    @Override
    public String toString() {
        return "org.biosystemconsultoria.model.Aquisicao[ id=" + id + " ]";
    }    


}
