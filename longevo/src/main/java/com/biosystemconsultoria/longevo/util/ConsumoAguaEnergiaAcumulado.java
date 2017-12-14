/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.util;

/**
 *
 * @author Wendell
 */

;
import java.io.Serializable;
import java.math.BigDecimal;
import com.biosystemconsultoria.longevo.model.Mes;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
        
public class ConsumoAguaEnergiaAcumulado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String tipoConsumo;
    private Mes mes;
    private BigDecimal quantidadeConsumida;
    private Long unidadeConsumidora;
    private String grupoTarifario;
    private String grupoCliente;
    private String logradouro;
    private Long numero;

    public String getTipoConsumo() {
        return tipoConsumo;
    }

    public void setTipoConsumo(String tipoConsumo) {
        this.tipoConsumo = tipoConsumo;
    }

    public BigDecimal getQuantidadeConsumida() {
        return quantidadeConsumida;
    }

    public void setQuantidadeConsumida(BigDecimal quantidadeConsumida) {
        this.quantidadeConsumida = quantidadeConsumida;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public Long getUnidadeConsumidora() {
        return unidadeConsumidora;
    }

    public void setUnidadeConsumidora(Long unidadeConsumidora) {
        this.unidadeConsumidora = unidadeConsumidora;
    }

    public String getGrupoTarifario() {
        return grupoTarifario;
    }

    public void setGrupoTarifario(String grupoTarifario) {
        this.grupoTarifario = grupoTarifario;
    }

    public String getGrupoCliente() {
        return grupoCliente;
    }

    public void setGrupoCliente(String grupoCliente) {
        this.grupoCliente = grupoCliente;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }
        
    
}
