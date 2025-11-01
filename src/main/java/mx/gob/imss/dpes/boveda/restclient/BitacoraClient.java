package mx.gob.imss.dpes.boveda.restclient;

import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraInterfaz;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient
@Path("/bitacora")
public interface BitacoraClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/interfaz")
    Response createBitacora(BitacoraInterfaz bitacoraInterfaz);

}
