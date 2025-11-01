/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.boveda.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author eduardo.loyo
 */
@Data
public class ReporteRespuesta extends BaseModel {
    private byte[] archivo;
    private String solicitud;
    private String nombreArchivo;
    private String asegurado;
    private String idDocumento;
    private String claseArchivo;
    private Long sesion;
    @Override
    public String toString() {
      return ReflectionToStringBuilder.toStringExclude(this, "archivo");
    }
}
