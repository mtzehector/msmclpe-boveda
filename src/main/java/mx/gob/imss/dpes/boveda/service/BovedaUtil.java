/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.boveda.service;

import java.net.MalformedURLException;
import java.util.logging.Level;
import javax.ws.rs.ext.Provider;

import mx.gob.imss.cit.clienteswebservices.boveda.legada.*;
import mx.gob.imss.dpes.boveda.model.CreateDocumentReq;
import mx.gob.imss.dpes.boveda.model.DocumentReq;
import mx.gob.imss.dpes.boveda.model.DocumentRes;
import mx.gob.imss.dpes.boveda.model.Documento;

import mx.gob.imss.dpes.boveda.model.ReporteRespuesta;
import mx.gob.imss.dpes.boveda.model.RespuestaBoveda;
import mx.gob.imss.dpes.common.service.BaseService;

/**
 *
 * @author eduardo.loyo
 */

@Provider
public class BovedaUtil extends BaseService{

    private static final String TIPO_ID_USR = "IDPERSONA";
    private static final String FOLIO_TRAMITE = "folioTramite";

    public DocumentRes getDocumentV2(DocumentReq documentReq) throws MalformedURLException {
        EntradaConsulta entradaConsulta = new EntradaConsulta();
        String tipoDocumentalTspi = "D:tspi:tspi_padre";
        String tipoDocumentalHistoricoTspi = "/tspi";

        Atributo atributo = new Atributo();
        if (documentReq.getDocumento().getIdDocumento() != null) {
        	log.log(Level.INFO, "########## El Id del documento NO es null [{0}] ##########", documentReq.getDocumento().getIdDocumento());
            atributo.setNombre("objectId");
            atributo.setValor(documentReq.getDocumento().getIdDocumento());
            entradaConsulta.setTipoDocumental(tipoDocumentalTspi);
        } else {
        	log.log(Level.INFO, "########## El Id del documento es null y el folio tramite es [{0}] ##########", documentReq.getTramite().getFolioTramite());
            atributo.setNombre(FOLIO_TRAMITE);
            atributo.setValor(documentReq.getTramite().getFolioTramite());
            entradaConsulta.setTipoDocumental(tipoDocumentalHistoricoTspi);
        }
        log.log(Level.INFO, "Atributo ID Val: {0}", atributo.getValor());
        entradaConsulta.getAtributo().add(atributo);

        return parseDocumentRes(getLegadoPortSoap().consultaDocumento(entradaConsulta));
    }

    public CreateDocumentReq createDocumentV2(CreateDocumentReq createDocumentReq) throws MalformedURLException {
        EntradaAlta entradaAlta = new EntradaAlta();
        String tipoDocumentalTspi = "D:tspi:tspi_padre";
        String rutaTspi = "/tspi";

        entradaAlta.setDocumento(createDocumentReq.getDocumento().getArchivo());
        entradaAlta.setTipoDocumental(tipoDocumentalTspi);
        entradaAlta.setRuta(rutaTspi);
        entradaAlta.setTipo(createDocumentReq.getDocumento().getExtencion());

        Atributo atributo = new Atributo();
        atributo.setNombre("name");
        atributo.setValor(createDocumentReq.getDocumento().getNombreArchivo());
        Atributo atributo1 = new Atributo();
        atributo1.setNombre(FOLIO_TRAMITE);
        atributo1.setValor(createDocumentReq.getTramite().getFolioTramite());
        Atributo atributo2 = new Atributo();
        atributo2.setNombre("idSolicitud");
        atributo2.setValor("1");
        Atributo atributo3 = new Atributo();
        atributo3.setNombre("tipoDocumento");
        atributo3.setValor(" ");
        Atributo atributo4 = new Atributo();
        atributo4.setNombre("folio");
        atributo4.setValor(createDocumentReq.getTramite().getFolioTramite());

        entradaAlta.getAtributo().add(atributo);
        entradaAlta.getAtributo().add(atributo1);
        entradaAlta.getAtributo().add(atributo2);
        entradaAlta.getAtributo().add(atributo3);
        entradaAlta.getAtributo().add(atributo4);

        CreateDocumentReq createDocumentRes = new CreateDocumentReq();
        try {
            createDocumentRes = parseCreateDocumentReq(getLegadoPortSoap().altaDocumento(entradaAlta));

        } catch (Exception e) {

            createDocumentRes.setRespuestaBoveda(new RespuestaBoveda());
            createDocumentRes.getRespuestaBoveda().setExito(false);
            createDocumentRes.getRespuestaBoveda().setDescripcionError(e.getMessage());

            log.log(Level.INFO, "Ocurrio un error al adjuntar el documento: {0}", e);
        }

        return createDocumentRes;
    }

    public SalidaActualizacion actualizarDocumentV2(ReporteRespuesta reporteRespuesta) {
        log.info("Actualizando documento");
        EntradaActualizacion entradaActualizacion = new EntradaActualizacion();
        entradaActualizacion.setDocumento(reporteRespuesta.getArchivo());
        entradaActualizacion.setIdDocumento(reporteRespuesta.getIdDocumento());
        entradaActualizacion.setVersionado(Boolean.FALSE);
        Atributo atributo = new Atributo();
        atributo.setNombre("name");
        atributo.setValor(reporteRespuesta.getNombreArchivo());
        Atributo atributo1 = new Atributo();
        atributo1.setNombre(FOLIO_TRAMITE);
        atributo1.setValor(reporteRespuesta.getSolicitud());
        Atributo atributo2 = new Atributo();
        atributo2.setNombre("idSolicitud");
        atributo2.setValor(reporteRespuesta.getSolicitud());
        Atributo atributo3 = new Atributo();
        atributo3.setNombre("tipoDocumento");
        atributo3.setValor(TIPO_ID_USR);
        Atributo atributo4 = new Atributo();
        atributo4.setNombre("folio");
        atributo4.setValor(reporteRespuesta.getSolicitud());

        entradaActualizacion.getAtributo().add(atributo);
        entradaActualizacion.getAtributo().add(atributo1);
        entradaActualizacion.getAtributo().add(atributo2);
        entradaActualizacion.getAtributo().add(atributo3);
        entradaActualizacion.getAtributo().add(atributo4);
        SalidaActualizacion salidaActualizacion = null;
        try {
            salidaActualizacion = getLegadoPortSoap().actualizacionDocumento(entradaActualizacion);

            log.log(Level.INFO, "CVE:{0}", salidaActualizacion.getClave() );
            log.log(Level.INFO, "Desc:{0}", salidaActualizacion.getDescripcion() );
            return salidaActualizacion;
        } catch (Exception e) {
            log.log(Level.INFO, "Error: ", e);
        }
        return salidaActualizacion;
    }

    public LegadoPort getLegadoPortSoap() {

        final LegadoPortService service = new LegadoPortService();
        final LegadoPort port = service.getLegadoPortSoap11();

        return port;

    }

    private CreateDocumentReq parseCreateDocumentReq(SalidaAlta salidaAlta) {
        CreateDocumentReq createDocumentRes = new CreateDocumentReq();

        RespuestaBoveda respuestaBoveda = new RespuestaBoveda();
        respuestaBoveda.setExito(salidaAlta.isExito());
        respuestaBoveda.setCodigoError(salidaAlta.getClave() != null ? salidaAlta.getClave() : "0");
        respuestaBoveda.setDescripcionError(salidaAlta.getDescripcion());

        respuestaBoveda.setIdDocumento(salidaAlta.getIdDocumento());

        createDocumentRes
                .setRespuestaBoveda(respuestaBoveda);
        return createDocumentRes;
    }

    private DocumentRes parseDocumentRes(SalidaConsulta salidaConsultaGuarderia) {
        DocumentRes documentRes = new DocumentRes();
        RespuestaBoveda respuestaBoveda = new RespuestaBoveda();
        respuestaBoveda.setExito(salidaConsultaGuarderia.isExito());
        respuestaBoveda.setCodigoError(salidaConsultaGuarderia.getClave() != null ? salidaConsultaGuarderia.getClave() : "0");
        respuestaBoveda.setDescripcionError(salidaConsultaGuarderia.getDescripcion());        
        documentRes.setRespuestaBoveda(respuestaBoveda);
        if (documentRes.getRespuestaBoveda().isExito()) {
            Documento documento = new Documento();
            documento.setArchivo(salidaConsultaGuarderia.getDocumento().get(0).getContenido());
            documento.setNombreArchivo(salidaConsultaGuarderia.getDocumento().get(0).getNombre());
            documentRes.setDocumento(documento);
        }
        return documentRes;
    }
}
