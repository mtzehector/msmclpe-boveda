/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.boveda.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author eduardo.loyo
 */

public class CreateDocumentReq extends BaseModel{
  @Getter @Setter Documento documento = new Documento();
  @Getter @Setter Tramite tramite = new Tramite();
  @Getter @Setter Usuario usuario = new Usuario();
  
  @Getter @Setter private RespuestaBoveda respuestaBoveda = new RespuestaBoveda();
  @Getter @Setter   private boolean exito;
  @Getter @Setter   private String clave;
  @Getter @Setter   private String descripcion;
  @Getter @Setter   private String idDocumento;
  @Getter @Setter   private String descripcionError;
  @Getter @Setter  private Long sesion;
   
}
