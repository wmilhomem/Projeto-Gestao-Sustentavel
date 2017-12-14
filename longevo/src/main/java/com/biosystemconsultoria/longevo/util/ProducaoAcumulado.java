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
        
public class ProducaoAcumulado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String tipoProducao;
    private Mes mes;
    private BigDecimal quantidadeProduzida;

    public String getTipoProducao() {
        return tipoProducao;
    }

    public void setTipoProducao(String tipoProducao) {
        this.tipoProducao = tipoProducao;
    }

    public BigDecimal getQuantidadeProduzida() {
        return quantidadeProduzida;
    }

    public void setQuantidadeProduzida(BigDecimal quantidadeProduzida) {
        this.quantidadeProduzida = quantidadeProduzida;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }
        
    
}
