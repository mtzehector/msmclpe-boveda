package mx.gob.imss.dpes.boveda.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;


//import mx.gob.imss.cit.ws.externo.boveda.documental.schema.Atributo;
//import mx.gob.imss.cit.ws.externo.boveda.documental.schema.BovedaServicio;
//import mx.gob.imss.cit.ws.externo.boveda.documental.schema.EntradaAlta;
//import mx.gob.imss.cit.ws.externo.boveda.documental.schema.EntradaConsulta;
//import mx.gob.imss.cit.ws.externo.boveda.documental.schema.SalidaAlta;
//import mx.gob.imss.cit.ws.externo.boveda.documental.schema.SalidaConsulta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.gob.imss.cit.clienteswebservices.boveda.Atributo;
import mx.gob.imss.cit.clienteswebservices.boveda.TspiPortService;
import mx.gob.imss.cit.clienteswebservices.boveda.EntradaAlta;
import mx.gob.imss.cit.clienteswebservices.boveda.EntradaConsulta;
import mx.gob.imss.cit.clienteswebservices.boveda.SalidaAlta;
import mx.gob.imss.cit.clienteswebservices.boveda.SalidaConsulta;
import mx.gob.imss.dpes.boveda.model.CreateDocumentReq;
import mx.gob.imss.dpes.boveda.model.DocumentReq;
import mx.gob.imss.dpes.boveda.model.DocumentRes;
import mx.gob.imss.dpes.boveda.model.Documento;
import mx.gob.imss.dpes.boveda.model.RespuestaBoveda;
import mx.gob.imss.dpes.boveda.restclient.BitacoraClient;
import mx.gob.imss.dpes.common.enums.TipoServicioEnum;
import mx.gob.imss.dpes.common.service.BaseService;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraInterfaz;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Provider
public class BovedaV3Util extends BaseService {
	
	private static final String FOLIO_TRAMITE = "folioTramite";
	private static final String BOVEDA_NAME = "name";
	private static final String BOVEDA_ID = "id";
    private static final String BOVEDA_RUTA = "/mclpe";
	private static final String BOVEDA_TIPO_DOCUMENTAL = "mclpe:mclpe";
	private static final String BOVEDA_IDENTIFICADOR = "mclpe";
	@Inject
	@RestClient
	private BitacoraClient bitacoraClient;
	
	public DocumentRes getDocumentBovedaV3(DocumentReq documentReq) throws MalformedURLException {
    	log.log(Level.INFO, "########## Recuperando documento de la boveda V3 con el idDocumento [{0}] ##########", documentReq.getDocumento().getIdDocumento());
		EntradaConsulta entradaConsulta = new EntradaConsulta();
		
		Atributo atributo = new Atributo();
		atributo.setNombre(BOVEDA_ID);
		atributo.setValor(documentReq.getDocumento().getIdDocumento());
		entradaConsulta.setTipoDocumental(BOVEDA_TIPO_DOCUMENTAL);
		entradaConsulta.setIdentificador(BOVEDA_IDENTIFICADOR);
		entradaConsulta.getAtributo().add(atributo);
		
		SalidaConsulta salidaConsulta = //new BovedaServicio().getBovedaServicioSoap().consultaDocumento(entradaConsulta);
				new TspiPortService().getTspiPortSoap11().consultaDocumento(entradaConsulta);
		
		log.log(Level.INFO, "########## Documento recuperado con exito [{0}] ##########", salidaConsulta.isExito());
		log.log(Level.INFO, "########## Clave del error [{0}] ##########", salidaConsulta.getClave() != null ? salidaConsulta.getClave() : "0");
		log.log(Level.INFO, "########## Descripcion del error [{0}] ##########", salidaConsulta.getDescripcion());
		log.log(Level.INFO, "########## Nombre del documento [{0}] ##########", salidaConsulta.getDocumento().get(0).getNombre());
		
        return parseRespuestaBovedaV3(salidaConsulta);
    }
	
	private DocumentRes parseRespuestaBovedaV3(SalidaConsulta salidaConsulta) {
        DocumentRes documentRes = new DocumentRes();
        RespuestaBoveda respuestaBoveda = new RespuestaBoveda();
        respuestaBoveda.setExito(salidaConsulta.isExito());
        respuestaBoveda.setCodigoError(salidaConsulta.getClave() != null ? salidaConsulta.getClave() : "0");
        respuestaBoveda.setDescripcionError(salidaConsulta.getDescripcion());        
        documentRes.setRespuestaBoveda(respuestaBoveda);
        if (documentRes.getRespuestaBoveda().isExito()) {
            Documento documento = new Documento();
            documento.setArchivo(salidaConsulta.getDocumento().get(0).getContenido());
            documento.setNombreArchivo(salidaConsulta.getDocumento().get(0).getNombre());
            documentRes.setDocumento(documento);
        }
        return documentRes;
    }
	
	public CreateDocumentReq createDocumentV3(CreateDocumentReq createDocumentReq) throws MalformedURLException {
		//log.log(Level.INFO, "########## Guardando documento en la bovedaV3 con el nombre [{0}] ##########", createDocumentReq.getDocumento().getNombreArchivo());
		
		EntradaAlta entradaAlta = new EntradaAlta();
		entradaAlta.setTipoDocumental(BOVEDA_TIPO_DOCUMENTAL);
		entradaAlta.setTipo(createDocumentReq.getDocumento().getExtencion());
		entradaAlta.setRuta(BOVEDA_RUTA);
		entradaAlta.setDocumento(createDocumentReq.getDocumento().getArchivo());

		//Lista de atributos
		List<Atributo> atributos = new ArrayList<Atributo>();
		
		//Atributo folio del tramite
		Atributo atributo = new Atributo();
		atributo.setNombre(FOLIO_TRAMITE);
		atributo.setValor(createDocumentReq.getTramite().getFolioTramite());
		atributos.add(atributo);
		
		//Atributo nombre fisico del documento
		atributo = new Atributo();
		atributo.setNombre(BOVEDA_NAME);
		atributo.setValor(createDocumentReq.getDocumento().getNombreArchivo());
		atributos.add(atributo);

		//Se setan todos atributos del documento
		entradaAlta.getAtributo().addAll(atributos);
		entradaAlta.setIdentificador(BOVEDA_IDENTIFICADOR);
		
		CreateDocumentReq createDocumentRes = new CreateDocumentReq();
		Date tiempoDuracionInicia = null;
		long tiempoEjecucion = 0;
		BitacoraInterfaz bitacora = null;
		try {
            //createDocumentRes = parseRespuestaCrearDocumentoBovedaV3(new BovedaServicio().getBovedaServicioSoap().altaDocumento(entradaAlta));
			tiempoDuracionInicia = new Date();
			createDocumentRes = parseRespuestaCrearDocumentoBovedaV3(new TspiPortService().getTspiPortSoap11().altaDocumento(entradaAlta));

			tiempoEjecucion = new Date().getTime() - tiempoDuracionInicia.getTime();
            if (!createDocumentRes.getRespuestaBoveda().isExito()) {
				bitacora = this.assemblerBitacoraRequest(
						createDocumentReq,
						createDocumentRes,
						tiempoEjecucion,
						0,
						entradaAlta
				);
            	log.log(Level.WARNING, "########## El servicio de guardar documento en boveda V3 respondio con error [" + createDocumentRes.getClave() + "] - descripcion [" + createDocumentRes.getDescripcion() + "] ##########");
    		} else {
				bitacora = this.assemblerBitacoraRequest(
						createDocumentReq,
						createDocumentRes,
						tiempoEjecucion,
						1,
						entradaAlta
				);
    			//log.log(Level.INFO, "########## El servicio de guardar documento en boveda V3 respondio con exito, idDocumento [" + createDocumentRes.getIdDocumento() + "] - descripcion [" + createDocumentRes.getDescripcion() + "] ##########");
    		}
        } catch (Exception e) {
			tiempoEjecucion = new Date().getTime() - tiempoDuracionInicia.getTime();
			bitacora = this.assemblerBitacoraRequest(
					createDocumentReq,
					createDocumentRes,
					tiempoEjecucion,
					0,
					entradaAlta
			);
            createDocumentRes.setRespuestaBoveda(new RespuestaBoveda());
            createDocumentRes.getRespuestaBoveda().setExito(false);
            createDocumentRes.getRespuestaBoveda().setDescripcionError(e.getMessage());
            log.log(Level.SEVERE, "Ocurrio un error al crear el documento en bovedaV3 - createDocumentReq [" + createDocumentReq + "]", e);
        }
		this.invocarServicioBitacora(bitacora);
        return createDocumentRes;
    }
	
	private CreateDocumentReq parseRespuestaCrearDocumentoBovedaV3(SalidaAlta salidaAlta) {
        CreateDocumentReq createDocumentRes = new CreateDocumentReq();

        RespuestaBoveda respuestaBoveda = new RespuestaBoveda();
        respuestaBoveda.setExito(salidaAlta.isExito());
        respuestaBoveda.setCodigoError(salidaAlta.getClave() != null ? salidaAlta.getClave() : "0");
        respuestaBoveda.setDescripcionError(salidaAlta.getDescripcion());
        respuestaBoveda.setIdDocumento(salidaAlta.getIdDocumento());

        createDocumentRes.setRespuestaBoveda(respuestaBoveda);
        return createDocumentRes;
    }

	private BitacoraInterfaz assemblerBitacoraRequest(CreateDocumentReq request, CreateDocumentReq response, long tiempoMilisegundos, int exito, EntradaAlta entradaAlta)  {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			EntradaAlta requestBoveda = new EntradaAlta();
			requestBoveda.setRuta(entradaAlta.getRuta());
			requestBoveda.setIdentificador(entradaAlta.getIdentificador());
			requestBoveda.setTipo(entradaAlta.getTipo());
			requestBoveda.setTipoDocumental(entradaAlta.getTipoDocumental());
			requestBoveda.getAtributo().addAll(entradaAlta.getAtributo());

			BitacoraInterfaz bitacora = new BitacoraInterfaz();
			bitacora.setSesion(request.getSesion());
			bitacora.setIdTipoServicio(TipoServicioEnum.BOVEDA_IPICYT.getId());
			bitacora.setDescRequest(objectMapper.writeValueAsString(requestBoveda));
			bitacora.setNumTiempoResp(tiempoMilisegundos);
			bitacora.setExito(exito);
			bitacora.setReponseEndpoint((!(response != null && response.getRespuestaBoveda() != null)) ? "" :
					objectMapper.writeValueAsString(response.getRespuestaBoveda()));
			String ip = this.obtenerIPEndPoint() == null? "": this.obtenerIPEndPoint();
			bitacora.setEndpoint("http://" + ip + "/alfresco-ws-1.0/servicios");
			bitacora.setCveReporte(0L);
			return bitacora;
		} catch (Exception e) {
			log.log(Level.SEVERE, "BovedaV3Util.assemblerBitacoraRequest request = [" + request +
				", response = [" + response + "], tiempoMilisegundos = [" + tiempoMilisegundos +"], exito = [" +
				exito + "], entradaAlta = [" + entradaAlta + "]", e);
			return null;
		}
	}

	private String obtenerIPEndPoint(){
		try {
			Properties propiedades = new Properties();
			propiedades.load(BovedaV3Util.class.getResourceAsStream("/META-INF/microprofile-config.properties"));
			return propiedades.getProperty("ip.boveda.ipicyt");
		}catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "Ocurrio un error el archivo properties no fue encontrado ", e);
			return null;
		} catch (IOException e) {
			log.log(Level.SEVERE, "Ocurrio un error al intentar obtener la propiedad ip.boveda.ipicyt del archivo properties ", e);
			return null;
		}
	}

	private void invocarServicioBitacora(BitacoraInterfaz bitacoraRequest){
		if (bitacoraRequest == null)
			log.log(Level.WARNING, "No fueron proporcionados los parametros al objeto BitacoraInterfaz para crear el registro de bitacora");
		else{
			try {
				bitacoraClient.createBitacora(bitacoraRequest);
			}catch (Exception e){
				log.log(Level.SEVERE, "Ocurrio un error en el servicio de bitacora ", e);
			}
		}
	}

}
