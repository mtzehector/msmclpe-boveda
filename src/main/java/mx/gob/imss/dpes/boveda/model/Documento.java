/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.boveda.model;

import java.util.List;
import lombok.Data;

import mx.gob.imss.cit.clienteswebservices.boveda.legada.Atributo;
import mx.gob.imss.dpes.common.model.BaseModel;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author eduardo.loyo
 */
@Data
public class Documento  extends BaseModel{
    private byte[] archivo;
    private String archivoString;
    private String extencion;
    private String nombreArchivo;
    private String idDocumento;
    private String mimeType;
    
    private transient List<Atributo> atributo;
    @Override
    public String toString() {
      return ReflectionToStringBuilder.toStringExclude(this, "archivo");
    }
}
