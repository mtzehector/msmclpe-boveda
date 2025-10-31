/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.boveda.service;

import java.util.Base64;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import lombok.extern.java.Log;
import mx.gob.imss.dpes.boveda.exception.BovedaException;
import mx.gob.imss.dpes.boveda.model.CreateDocumentReq;

import mx.gob.imss.dpes.boveda.model.ReporteRespuesta;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

/**
 *
 * @author eduardo.loyo
 */
@Log
@Provider
public class UpdateBovedaService extends ServiceDefinition<CreateDocumentReq, CreateDocumentReq> {

    @Inject
    private BovedaServiceUtil service;
    
    private static final String CODE_N = "N";

    @Override
    public Message<CreateDocumentReq> execute(Message<CreateDocumentReq> request) throws BusinessException {
        CreateDocumentReq response = new CreateDocumentReq();
        ReporteRespuesta rR = new ReporteRespuesta();
        rR.setIdDocumento(request.getPayload().getDocumento().getIdDocumento());
        byte[] decoded = Base64.getDecoder().decode(request.getPayload().getDocumento().getArchivoString());
        rR.setArchivo(decoded);
        if(service.actualizarDocumento(rR).equals(CODE_N)){
            throw new BovedaException(BovedaException.UPDATE);
        }
        return new Message<>(response);
    }

}
