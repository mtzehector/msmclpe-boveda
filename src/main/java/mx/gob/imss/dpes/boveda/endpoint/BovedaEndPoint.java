/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.boveda.endpoint;

import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.boveda.model.CreateDocumentReq;
import mx.gob.imss.dpes.boveda.service.CreateBovedaService;
import mx.gob.imss.dpes.boveda.service.LoadBovedaService;
import mx.gob.imss.dpes.boveda.service.LoadBovedaV3Service;
import mx.gob.imss.dpes.boveda.service.UpdateBovedaService;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author eduardo.loyo
 */
@Path("/boveda")
@RequestScoped
public class BovedaEndPoint extends BaseGUIEndPoint<CreateDocumentReq, CreateDocumentReq, CreateDocumentReq> {

    @Inject
    private CreateBovedaService create;
    @Inject
    private LoadBovedaService load;
    @Inject
    private LoadBovedaV3Service loadBovedaV3;
    @Inject
    private UpdateBovedaService update;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Consumo de Boved",
            description = "uso de la boveda")
    @Override
    public Response create(CreateDocumentReq request) throws BusinessException {
    	log.log(Level.INFO, "########## URL invocada [/boveda/create] ##########");
        log.log(Level.INFO, "Crear : {0}", request);
        //log.log(Level.INFO,"*********************Data file size: {0}", request.getDocumento().getArchivo().length);
        ServiceDefinition[] steps = {create};
        Message<CreateDocumentReq> response
                = create.executeSteps(steps, new Message<>(request));
        //log.log(Level.INFO, "Response crear: {0}", response);

        return toResponse(response);
    }

    @POST
    @Path("/load")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Consumo de Boved",
            description = "uso de la boveda")
    @Override
    public Response load(CreateDocumentReq request) throws BusinessException {
    	log.log(Level.INFO, "########## URL invocada [/boveda/load] ##########");
        log.log(Level.INFO, "Buscar: {0}", request);
        ServiceDefinition[] steps = {load};
        Message<CreateDocumentReq> response
                = load.executeSteps(steps, new Message<>(request));
        //log.log(Level.INFO, "Response buscar: {0}", response);

        return toResponse(response);
    }
    
    @POST
    @Path("/loadBovedaV3")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Consumo de Boved",
            description = "uso de la boveda")
    @Override
    public Response loadBovedaV3(CreateDocumentReq request) throws BusinessException {
    	log.log(Level.INFO, "########## URL invocada [/boveda/loadBovedaV3] ##########");
        log.log(Level.INFO, "Buscar: {0}", request);
        ServiceDefinition[] steps = {loadBovedaV3};
        Message<CreateDocumentReq> response
                = load.executeSteps(steps, new Message<>(request));
        //log.log(Level.INFO, "Response buscar: {0}", response);

        return toResponse(response);
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Consumo de Boved",
            description = "uso de la boveda")
    @Override
    public Response update(CreateDocumentReq request) throws BusinessException {
    	log.log(Level.INFO, "########## URL invocada [/boveda/update] ##########");
        log.log(Level.INFO, "Actulizar: {0}", request);
        ServiceDefinition[] steps = {update};
        Message<CreateDocumentReq> response
                = update.executeSteps(steps, new Message<>(request));
        //log.log(Level.INFO, "Response actualizar: {0}", response);

        return toResponse(response);
    }

}
