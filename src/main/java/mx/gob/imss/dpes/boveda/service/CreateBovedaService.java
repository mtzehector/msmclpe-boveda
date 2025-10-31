/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.boveda.service;

import java.util.Base64;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import lombok.extern.java.Log;
import mx.gob.imss.dpes.boveda.assembler.BovedaAssembler;
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
@Provider
@Log
public class CreateBovedaService extends ServiceDefinition<CreateDocumentReq, CreateDocumentReq> {

    @Inject
    private BovedaServiceUtil service;
    @Inject
    private BovedaAssembler assembler;

    @Override
    public Message<CreateDocumentReq> execute(Message<CreateDocumentReq> request) throws BusinessException {
        log.info("########## Creacion documento ##########");
        ReporteRespuesta reporte;
        if (request.getPayload().getDocumento().getArchivo() != null) {
            reporte = service.subirDocumento(assembler.toEntity(request.getPayload()));
            log.log(Level.INFO,"Subir documento response: {0}" ,reporte);

        } else if (request.getPayload().getDocumento().getArchivoString() != null
                || ! request.getPayload().getDocumento().getArchivoString().isEmpty() ) {

            byte[] decoded = Base64.getDecoder().decode(request.getPayload().getDocumento().getArchivoString());
            request.getPayload().getDocumento().setArchivo(decoded);
            reporte = service.subirDocumento(assembler.toEntity(request.getPayload()));
            log.log(Level.INFO,"Subir documento response: {0}" ,reporte);

        } else {
            throw new BovedaException(BovedaException.FILENULL);
        }

        if ( reporte.getIdDocumento() == null || "".equals(reporte.getIdDocumento())  ) {
            log.log(Level.INFO,"Error idDocumento vacio");
            throw new BovedaException(BovedaException.CREATE);
        }

        CreateDocumentReq response = assembler.assemble(reporte);

        return new Message<>(response);
    }

}
