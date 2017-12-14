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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;
    
    @NotNull
    @NotEmpty
    @Size(min=10, max=100)
    @Column(nullable = false, length = 100)
    private String nome;
    
    @NotNull
    @NotEmpty
    @Size(min=5, max=50)
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @NotNull
    @Size(min=6, max=255)
    @NotEmpty@Column(nullable = false, length = 255)
    private String senha;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name="empresaId", nullable = false)
    private Empresa empresa;
    
    @NotNull
    @Email
    @Column(nullable = false, unique = true, length = 100)
    private String email;
   
    
    @NotNull
    @ManyToOne
    @JoinColumn(name="perfilacessoId")
    private PerfilAcesso perfilAcesso;
       
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataCriacao;
    

    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataAlteracao;
    
    
    @NotNull
    @NotEmpty
    @Column(nullable = false, length = 25)
    private String log;
    
    @Transient
    private boolean novo;
    
    public Usuario() {
    }

	
    public Long getId() {
        return id;
    }
        
    public void setId(Long id) {
	this.id = id;
    }
	
    public String getNome() {
	return this.nome;
    }

    public void setNome(String nome) {
	this.nome = nome.toUpperCase();
    }

    public String getUsername() {
    	return this.username;
    }

    public void setUsername(String username) {
    	this.username = username;
    }

    public String getSenha() {
	return this.senha;
    }

    public void setSenha(String senha) {
	this.senha = senha;
    }

    public String getEmail() {
	return this.email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public Empresa getEmpresa() {
        if (empresa == null) {
            empresa = new Empresa();
        }
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
   
    public PerfilAcesso getPerfilAcesso() {
        if (perfilAcesso == null) {
            perfilAcesso = new PerfilAcesso();
        }
        return perfilAcesso;
    }
        
    public void setPerfilAcesso(PerfilAcesso perfilAcesso) {
	this.perfilAcesso = perfilAcesso;
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
	Usuario other = (Usuario) obj;
	if (id == null) {
		if (other.id != null)
			return false;
	} else if (!id.equals(other.id))
		return false;
	return true;
    }
    
    
    @Override
    public String toString() {
        return "org.biosystemconsultoria.model.Usuario[ id=" + id + " ]";
    }

 
}  