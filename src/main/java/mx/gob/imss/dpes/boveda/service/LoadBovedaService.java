/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.boveda.service;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import lombok.extern.java.Log;
import mx.gob.imss.dpes.boveda.exception.BovedaException;
import mx.gob.imss.dpes.boveda.model.CreateDocumentReq;
import mx.gob.imss.dpes.boveda.model.DocumentRes;
import mx.gob.imss.dpes.boveda.model.ReporteRespuesta;
import mx.gob.imss.dpes.boveda.model.RespuestaBoveda;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

/**
 *
 * @author eduardo.loyo
 */
@Log
@Provider
public class LoadBovedaService extends ServiceDefinition<CreateDocumentReq, CreateDocumentReq> {

    @Inject
    private BovedaServiceUtil service;

    @Override
    public Message<CreateDocumentReq> execute(Message<CreateDocumentReq> request) throws BusinessException {
        log.info("Inicia LoadBoveda");
        //wrappers
        CreateDocumentReq response = new CreateDocumentReq();
        ReporteRespuesta reporteIn = new ReporteRespuesta();
        RespuestaBoveda rB = new RespuestaBoveda();
        
        //TODO
        reporteIn.setIdDocumento(request.getPayload().getDocumento().getIdDocumento());
        reporteIn.setNombreArchivo(request.getPayload().getDocumento().getNombreArchivo());
        reporteIn.setSolicitud(request.getPayload().getTramite().getFolioTramite());
        reporteIn.setAsegurado(request.getPayload().getUsuario().getTipoIdUsr());
        try {
            DocumentRes res  = service.recuperarDocumento(reporteIn);
            if(res.getRespuestaBoveda().isExito()){
                //log.log(Level.INFO,"Servicio Load Back exitoso: {0}", res);
                rB.setArchivo(res.getDocumento().getArchivo());
                rB.setDescripcion(res.getDocumento().getNombreArchivo());
                response.setRespuestaBoveda(rB);
            }else{
                throw new BovedaException(BovedaException.LOAD);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(LoadBovedaService.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        //log.log(Level.INFO,"Response de Load Back: {0}",response);
        return new Message<>(response);
    }

}
