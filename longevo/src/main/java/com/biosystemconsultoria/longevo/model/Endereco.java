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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="endereco")
public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;

        @Id
	@GeneratedValue
	private Long id;
        
        @NotEmpty
        @Size(min=3, max=100)
        @Column(nullable = false, length = 150)
	private String logradouro;
        
        @NotNull
        @Column(nullable = false, length = 20)
	private String numero;
        
	@Column(length = 150)
        private String complemento;

        @Size(min=5, max=50)
        @NotNull
        @NotEmpty
        @Column(nullable = false, length = 50)
        private String bairro;

        @Size(min=5, max=50)
        @NotNull
        @NotEmpty
        @Column(nullable = false, length = 50)
        private String cidade;

        @NotNull
        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 2)
	private Estado uf;
 
        @Size(min=5, max=50)
        @NotNull
        @NotEmpty
        @Column(nullable = false, length = 50)
        private String pais;
         
        @NotNull
        @Column(nullable = false, length = 8)
	private String cep;
        
        @NotNull
        @NotEmpty
        @Column(nullable = false, length = 1)
        private String matrizfilial;
        
        @ManyToOne
	@JoinColumn(name = "empresaId", nullable = false)
	private Empresa empresa;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro.toUpperCase();
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento.toUpperCase();
	}

        public String getBairro() {
            return bairro;
        }

        public void setBairro(String bairro) {
            this.bairro = bairro.toUpperCase();
        }

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade.toUpperCase();
	}

        public String getPais() {
            return pais;
        }

        public void setPais(String pais) {
            this.pais = pais.toUpperCase();
        }        
    
	public Estado getUf() {
		return uf;
	}

	public void setUf(Estado uf) {
		this.uf = uf;
	}
	
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep.replaceAll("-","");
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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
		Endereco other = (Endereco) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

    public String getMatrizfilial() {
        return matrizfilial;
    }

    public void setMatrizfilial(String matrizfilial) {
        this.matrizfilial = matrizfilial;
    }
  

}