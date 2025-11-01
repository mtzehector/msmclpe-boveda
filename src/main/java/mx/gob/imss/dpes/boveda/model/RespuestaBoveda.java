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
public class RespuestaBoveda extends BaseModel {

    private boolean exito;
    private String clave;
    private String descripcion;
    private String idDocumento;
    private String codigoError;
    private String descripcionError;
    private byte[] archivo;
    @Override
    public String toString() {
      return ReflectionToStringBuilder.toStringExclude(this, "archivo");
    }
}
