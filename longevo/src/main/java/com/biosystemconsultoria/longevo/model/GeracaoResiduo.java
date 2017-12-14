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
import javax.persistence.OneToOne;
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
@Table(name="geracaoresiduo")
public class GeracaoResiduo implements Serializable  {
 
  
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
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, length = 2)
    private Mes mes;
    
    @NotNull
    @Min(2013) @Max(value = 2030, message = "Valor incorreto!")
    @Column(nullable = false, length = 4)
    private int ano;
    
    //@NotNull
    //@Size(min=10, max=100)
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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AspectoTipo aspectoTipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private ResiduoTipo residuoTipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Etapa etapa;
    
   // @NotNull
    @Column(nullable = false, length = 30) 
    private String destinoEfluente;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DestinoResiduo destinoResiduo;
    
   // @NotNull
    @Column(nullable = false, length = 1)
    private String controleEmissao;
   
  //  @NotNull
    @Column(nullable = false, length = 11)
    private Long medicao;
    
  //  @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)     
    private Date dataPesagem;
    
   // @NotNull
    @Column(nullable = false, length = 20)        
    private String composicao;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal custoDestino;
    
  //  @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
  // @NotNull
    @Size(min=2, max=30)
    @Column(nullable = false, length = 30)
    private String departamento;
   
   // @NotNull
    @Size(min=1)
    @Column(nullable = false, length = 20)
    private String tipoPapel;
    
   // @NotNull
    @Size(min=1)
    @Column(nullable = false, length = 1)
    private String copiaImpressao;
    
   // @NotNull
    @Size(min=1)
    @Column(nullable = false, length = 1)
    private String cotaExtra;
    
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
        this.documentoref = documentoref;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
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
    
    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public AspectoTipo getAspectoTipo() {
        return aspectoTipo;
    }

    public void setAspectoTipo(AspectoTipo aspectoTipo) {
        this.aspectoTipo = aspectoTipo;
    }

    public ResiduoTipo getResiduoTipo() {
        return residuoTipo;
    }

    public void setResiduoTipo(ResiduoTipo residuoTipo) {
        this.residuoTipo = residuoTipo;
    }

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }

    public String getDestinoEfluente() {
        return destinoEfluente;
    }

    public void setDestinoEfluente(String destinoEfluente) {
        this.destinoEfluente = destinoEfluente;
    }

    public DestinoResiduo getDestinoResiduo() {
        return destinoResiduo;
    }

    public void setDestinoResiduo(DestinoResiduo destinoResiduo) {
        this.destinoResiduo = destinoResiduo;
    }

    public String getControleEmissao() {
        return controleEmissao;
    }

    public void setControleEmissao(String controleEmissao) {
        this.controleEmissao = controleEmissao;
    }

    public Long getMedicao() {
        return medicao;
    }

    public void setMedicao(Long medicao) {
        this.medicao = medicao;
    }

    public Date getDataPesagem() {
        return dataPesagem;
    }

    public void setDataPesagem(Date dataPesagem) {
        this.dataPesagem = dataPesagem;
    }

    public String getComposicao() {
        return composicao;
    }

    public void setComposicao(String composicao) {
        this.composicao = composicao;
    }

    public BigDecimal getCustoDestino() {
        return custoDestino;
    }

    public void setCustoDestino(BigDecimal custoDestino) {
        this.custoDestino = custoDestino;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getTipoPapel() {
        return tipoPapel;
    }

    public void setTipoPapel(String tipoPapel) {
        this.tipoPapel = tipoPapel;
    }

    public String getCopiaImpressao() {
        return copiaImpressao;
    }

    public void setCopiaImpressao(String copiaImpressao) {
        this.copiaImpressao = copiaImpressao;
    }

    public String getCotaExtra() {
        return cotaExtra;
    }

    public void setCotaExtra(String cotaExtra) {
        this.cotaExtra = cotaExtra;
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
	result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
	GeracaoResiduo other = (GeracaoResiduo) obj;
	if (getId() == null) {
            if (other.getId() != null)
            return false;
	} else if (!id.equals(other.id))
            return false;
        return true;
    }                

  
}
