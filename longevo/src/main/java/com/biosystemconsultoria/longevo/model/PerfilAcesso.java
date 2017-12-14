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
        
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "perfilacesso")
public class PerfilAcesso implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Size(min=5, max=100)
    @NotNull
    @NotEmpty
    @Column(nullable=false, unique = true, length=100)
    private String descricao;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Autorizacao autorizacao;

    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dataCriacao;

    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dataAlteracao;
    
    @NotNull
    @NotEmpty
    @Column(nullable = false, length = 25)
    private String log;
    
    @Transient
    private boolean novo;
    
    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getDescricao() {
    	return descricao;
    }

    public void setDescricao(String descricao) {
    	this.descricao = descricao.toUpperCase();
    }

    public Autorizacao getAutorizacao() {
    	return autorizacao;
    }

    public void setAutorizacao(Autorizacao autorizacao) {
    	this.autorizacao = autorizacao;
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
	PerfilAcesso other = (PerfilAcesso) obj;
	if (id == null) {
		if (other.id != null)
			return false;
	} else if (!id.equals(other.id))
		return false;
        return true;
    }

    @Override
    public String toString() {
        return "org.biosystemconsultoria.model.Perfilacesso[ id=" + id + " ]";
    }

    
}
