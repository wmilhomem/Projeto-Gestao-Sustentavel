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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
        
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="empresa")
public class Empresa implements Serializable {
    
    private static final long serialVersionUID = 1L;
  
    @Id
    @GeneratedValue
    private Long id;

    @Size(min=5, max=100)
    @NotNull
    @NotEmpty
    @Column(nullable = false, length = 100)
    private String razaoSocial;
    
    @NotEmpty
    @NotNull
    @Size(min=14)    
 //   @CNPJ
    @Column(nullable = false, length = 14)
    private String cnpj;
    
    @Column(length = 20)
    private String inscricaoMunicipal;
    
    @Column(length = 20)
    private String inscricaoEstadual;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private PorteEmpresa porteEmpresa;

    @Size(min=8)    
    @NotEmpty
    @NotNull
    @Column(nullable = false, length = 11)
    private String telefone;
    
    @Column(length = 11)
    private String fax;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private RamoAtividade ramoAtividade;

    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(nullable = false)
    private Date   dataAbertura;

    @NotNull
    @Column(nullable = false)
    private Long   qtdFuncionarios;

    @Size(min=3, max=50)    
    @NotEmpty
    @NotNull
    @Column(nullable = false, length = 50)
    private String responsavel;

    @NotEmpty
    @NotNull
    @Column(nullable = false, length = 30)
    private String cargo;

    @NotEmpty
    @NotNull
    @Email
    @Column(nullable = false, length = 100)
    private String email;
    
    @Size(min=8)    
    @NotEmpty
    @NotNull
    @Column(nullable = false, length = 11)
    private String telefoneResponsavel;

    @Column(columnDefinition = "text")
    private String observacao;
    
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

    @NotNull
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Endereco> enderecos = new ArrayList<>();
    
    @Transient
    private boolean novo;
    
      
    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial.toUpperCase();
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
       this.cnpj = cnpj.replaceAll("\\.","").replaceAll("\\/","").replace("-","");
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual.replace(".", "").replace("-","");
    }

    public PorteEmpresa getPorteEmpresa() {
        return porteEmpresa;
    }

    public void setPorteEmpresa(PorteEmpresa porteEmpresa) {
        this.porteEmpresa = porteEmpresa;
    }


    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone.replace("(", "").replace(")","").replace("-", "");
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
      this.fax = fax.replace("(", "").replace(")","");
    }

    public RamoAtividade getRamoAtividade() {
        return ramoAtividade;
    }

    public void setRamoAtividade(RamoAtividade ramoAtividade) {
        this.ramoAtividade = ramoAtividade;
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Long getQtdFuncionarios() {
        return qtdFuncionarios;
    }

    public void setQtdFuncionarios(Long qtdFuncionarios) {
        this.qtdFuncionarios = qtdFuncionarios;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel.toUpperCase();
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo.toUpperCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

     public String getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
         this.telefoneResponsavel = telefoneResponsavel.replace("(", "").replace(")","").replace("-", "");
    }
    
    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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
    
     public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    
    public boolean isNovo() {
	return novo = getId() == null;
    }
   
    public void adicionarEnderecoVazio(){
        if (this.getEnderecos().isEmpty()) {
        
            Endereco endereco = new Endereco();
            endereco.setEmpresa(this);
        
            this.getEnderecos().add(0,endereco);
        } else {
           
            Endereco primeiroItem = this.getEnderecos().get(0);
            
            if (primeiroItem != null && primeiroItem.getLogradouro() != null) {
                
                Endereco endereco = new Endereco();
                endereco.setEmpresa(this);
        
                this.getEnderecos().add(0,endereco);
            }
            
        }
    }
    
    public void removeEnderecoVazio() {
        Endereco primeiroItem = this.getEnderecos().get(0);
        
        if (primeiroItem != null && primeiroItem.getLogradouro() == null) {
            this.getEnderecos().remove(0);
        }
            
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
	Empresa other = (Empresa) obj;
	if (id == null) {
            if (other.id != null)
            return false;
	} else if (!id.equals(other.id))
            return false;
        return true;
    }


      
}
