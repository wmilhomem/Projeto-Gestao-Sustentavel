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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="temperatura")
public class Temperatura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
        
    @NotNull
    @Column(nullable = false)
    private Long estacao;
    
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, length = 2)
    private Mes mes;
    
    @NotNull
    @Min(2013) @Max(value = 2030, message = "Valor incorreto!")
    @Column(nullable = false, length = 4)
    private int ano;
 
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date data;
        
    @NotNull
    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal maxima;
 
    @NotNull
    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal minima;
 
    @NotNull
    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal media; 
      
    @NotNull
    @Column(nullable = false)
    private String log;
            
    @NotNull
    @Column(nullable = false)
    private Long enderecoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEstacao() {
        return estacao;
    }

    public void setEstacao(Long estacao) {
        this.estacao = estacao;
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
    
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getMaxima() {
        return maxima;
    }

    public void setMaxima(BigDecimal maxima) {
        this.maxima = maxima;
    }

    public BigDecimal getMinima() {
        return minima;
    }

    public void setMinima(BigDecimal minima) {
        this.minima = minima;
    }

    public BigDecimal getMedia() {
        return media;
    }

    public void setMedia(BigDecimal media) {
        this.media = media;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Long getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(Long enderecoId) {
        this.enderecoId = enderecoId;
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
	Temperatura other = (Temperatura) obj;
	if (id == null) {
            if (other.id != null)
            return false;
	} else if (!id.equals(other.id))
            return false;
        return true;
    }                   

        
    
}
