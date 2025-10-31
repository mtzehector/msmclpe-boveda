/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.boveda.assembler;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.boveda.model.CreateDocumentReq;
import mx.gob.imss.dpes.boveda.model.ReporteRespuesta;
import mx.gob.imss.dpes.boveda.model.RespuestaBoveda;
import java.util.logging.Level;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class BovedaAssembler extends BaseAssembler <ReporteRespuesta,CreateDocumentReq> {

    
    public ReporteRespuesta toEntity(CreateDocumentReq source) {
        ReporteRespuesta reporteRespuesta = new ReporteRespuesta();
        reporteRespuesta.setArchivo(source.getDocumento().getArchivo());
        reporteRespuesta.setNombreArchivo(source.getDocumento().getNombreArchivo());
        reporteRespuesta.setSolicitud(source.getTramite().getFolioTramite());
        reporteRespuesta.setAsegurado(source.getUsuario().getTipoIdUsr());
        reporteRespuesta.setClaseArchivo(source.getDocumento().getExtencion());
        reporteRespuesta.setSesion(source.getSesion());
        return reporteRespuesta;
    }
    
    @Override
    public CreateDocumentReq assemble(ReporteRespuesta source) {
        //log.log(Level.INFO,"assembler data: {0}", source);
        CreateDocumentReq response = new CreateDocumentReq();
        response.setRespuestaBoveda(new RespuestaBoveda());
        response.getRespuestaBoveda().setIdDocumento(source.getIdDocumento());
        response.getRespuestaBoveda().setDescripcion(source.getNombreArchivo());
        response.getRespuestaBoveda().setExito(true);
        response.setSesion(source.getSesion());
        return response;
    }
    
}
