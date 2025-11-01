/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.boveda.service;

import java.net.MalformedURLException;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import mx.gob.imss.cit.clienteswebservices.boveda.legada.SalidaActualizacion;
import mx.gob.imss.dpes.boveda.model.CreateDocumentReq;
import mx.gob.imss.dpes.boveda.model.DocumentReq;
import mx.gob.imss.dpes.boveda.model.DocumentRes;
import mx.gob.imss.dpes.boveda.model.Documento;
import mx.gob.imss.dpes.boveda.model.ReporteRespuesta;
import mx.gob.imss.dpes.boveda.model.Tramite;
import mx.gob.imss.dpes.boveda.model.Usuario;
import mx.gob.imss.dpes.common.service.BaseService;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class BovedaServiceUtil extends BaseService {

    @Inject
    private BovedaUtil bovedaUtility;
    
    @Inject
    private BovedaV3Util bovedaV3Utility;

    private static final String PDF_EXTENSION = "pdf";
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String TIPO_ID_USR = "IDPERSONA";
    private static final String XML_EXTENSION = "xml";
    private static final String XML_MIME_TYPE = MediaType.APPLICATION_XML;
    private static final String CODE_BP_5005 = "BP-5005";
    private static final String XLSX_EXTENSION = "xlsx";
    private static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String TXT_EXTENSION = "txt";
    private static final String TXT_MIME_TYPE = "text/plain";

    public ReporteRespuesta subirDocumento(ReporteRespuesta reporteRespuesta) {
        log.info("Entrando a BovedaService:subirDocumento");
        log.info("Subir Documento");
        if (reporteRespuesta.getArchivo() != null && reporteRespuesta.getArchivo().length > 0) {
            
            Tramite tramite = new Tramite();

            tramite.setFolioTramite(reporteRespuesta.getSolicitud());
            log.log(Level.INFO,"Extensión: {0}", reporteRespuesta);
            Documento documento = new Documento();
            switch (reporteRespuesta.getClaseArchivo()) {
                case ".pdf":
                    documento.setNombreArchivo(reporteRespuesta.getNombreArchivo() + "." + PDF_EXTENSION);
                    documento.setExtencion(PDF_EXTENSION);
                    documento.setMimeType(PDF_MIME_TYPE);
                    break;
                case ".xml":
                    documento.setNombreArchivo(reporteRespuesta.getNombreArchivo() + "." + XML_EXTENSION);
                    documento.setExtencion(XML_EXTENSION);
                    documento.setMimeType(XML_MIME_TYPE);
                    break;
                case ".xlsx":
                    documento.setNombreArchivo(reporteRespuesta.getNombreArchivo() + "." + XLSX_EXTENSION);
                    documento.setExtencion(XLSX_EXTENSION);
                    documento.setMimeType(XLSX_MIME_TYPE);
                    break;
                case ".txt":
                    documento.setNombreArchivo(reporteRespuesta.getNombreArchivo() + "." + TXT_EXTENSION);
                    documento.setExtencion(TXT_EXTENSION);
                    documento.setMimeType(TXT_MIME_TYPE);
                    break;           
                default:
                    log.log(Level.INFO,"Tipo de archivo no valido");
                    break;
            }

            documento.setArchivo(reporteRespuesta.getArchivo());

            Usuario usuario = new Usuario();
            log.log(Level.INFO, "Tomando cveIdPersonaAseg: {0}", reporteRespuesta.getAsegurado());
            usuario.setIdUsr(String.valueOf(reporteRespuesta.getAsegurado()));
            usuario.setOwner(false);
            usuario.setTipoIdUsr(TIPO_ID_USR);

            CreateDocumentReq createDocumentReq = new CreateDocumentReq();
            createDocumentReq.setDocumento(documento);
            createDocumentReq.setTramite(tramite);
            createDocumentReq.setUsuario(usuario);
            createDocumentReq.setSesion(reporteRespuesta.getSesion());

            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append("DOCUMENTO:[Nombre Archivo: ");
            sBuilder.append(documento.getNombreArchivo());
            sBuilder.append(" extension: ");
            sBuilder.append(documento.getExtencion());
            sBuilder.append(" MimeType: ");
            sBuilder.append(documento.getMimeType());
            sBuilder.append(" bytes: ");
            sBuilder.append(documento.getArchivo() != null);
            sBuilder.append("] ");
            sBuilder.append("TRAMITE:[Folio Tramite: ");
            sBuilder.append(tramite.getFolioTramite());
            sBuilder.append("] ");
            sBuilder.append("USUARIO:[IdUsr: ");
            sBuilder.append(usuario.getIdUsr());
            sBuilder.append(" Owner: ");
            sBuilder.append(usuario.getOwner());
            sBuilder.append(" TipoIdUsr: ");
            sBuilder.append(usuario.getTipoIdUsr());
            sBuilder.append("]");
            log.log(Level.INFO, "Parametros upload documento: {0}", sBuilder.toString());
            reporteRespuesta.setIdDocumento(enviarDocumentoBoveda(createDocumentReq));
            return reporteRespuesta;
        } else {
            log.info("No hay archivo que guardar");
            return reporteRespuesta;
        }

    }

    private String enviarDocumentoBoveda(CreateDocumentReq createDocumentReq) {
        CreateDocumentReq createDocumentRes;
        try {
            createDocumentRes = bovedaV3Utility.createDocumentV3(createDocumentReq);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error al subir el documento ", e);
            return null;
        }

        if (createDocumentRes.getRespuestaBoveda().isExito()) {
            log.info("documento insertado en boveda");
            log.log(Level.INFO, "EL DOCUMENTO SUBIDO ES : {0}",
                    createDocumentReq.getDocumento().getNombreArchivo());
            return createDocumentRes.getRespuestaBoveda().getIdDocumento();
        } else if (createDocumentRes.getRespuestaBoveda().getCodigoError() != null
                && createDocumentRes.getRespuestaBoveda().getCodigoError().equals(CODE_BP_5005)) {
            return "0";
        }
        log.info("Error en createDocument :::");
        StringBuilder sb = new StringBuilder();
        sb.append(createDocumentRes.getRespuestaBoveda()
                .getCodigoError());
        sb.append("-");
        sb.append(createDocumentRes.getRespuestaBoveda()
                .getDescripcionError());
        log.log(Level.INFO, "Boveda Error: {0}", sb.toString());
        return null;
    }
    

    public DocumentRes recuperarDocumento(ReporteRespuesta reporteRespuesta)
            throws MalformedURLException {
        log.info("Entrando a BovedaService:recuperarDocumento");

        Documento doc = new Documento();
        doc.setNombreArchivo(reporteRespuesta.getNombreArchivo() + "." + PDF_EXTENSION);
        doc.setExtencion(PDF_EXTENSION);
        doc.setIdDocumento(reporteRespuesta.getIdDocumento());
        log.log(Level.INFO, "Nombre {0}", doc.getNombreArchivo());
        log.log(Level.INFO, "Extencion {0}", doc.getExtencion());
        log.log(Level.INFO, "IdDocumento {0}", doc.getIdDocumento());

        DocumentReq documentReq = new DocumentReq();

        Tramite tramite = new Tramite();
        log.log(Level.INFO, "tramite FolioTramite {0}", reporteRespuesta.getSolicitud());
        tramite.setFolioTramite(String.valueOf(reporteRespuesta.getSolicitud()));

        Usuario usuario = new Usuario();

        usuario.setIdUsr(reporteRespuesta.getAsegurado());
        usuario.setOwner(false);
        usuario.setTipoIdUsr(TIPO_ID_USR);

        documentReq.setDocumento(doc);
        documentReq.setTramite(tramite);
        documentReq.setUsuario(usuario);

        DocumentRes documentRes = bovedaUtility.getDocumentV2(documentReq);

        return documentRes;
    }
    
    public DocumentRes recuperarDocumentoBovedaV3(ReporteRespuesta reporteRespuesta)
            throws MalformedURLException {
        log.info("Entrando a BovedaService:recuperarDocumento");

        Documento doc = new Documento();
        doc.setNombreArchivo(reporteRespuesta.getNombreArchivo() + "." + PDF_EXTENSION);
        doc.setExtencion(PDF_EXTENSION);
        doc.setIdDocumento(reporteRespuesta.getIdDocumento());
        log.log(Level.INFO, "Nombre {0}", doc.getNombreArchivo());
        log.log(Level.INFO, "Extencion {0}", doc.getExtencion());
        log.log(Level.INFO, "IdDocumento {0}", doc.getIdDocumento());

        DocumentReq documentReq = new DocumentReq();

        Tramite tramite = new Tramite();
        log.log(Level.INFO, "tramite FolioTramite {0}", reporteRespuesta.getSolicitud());
        tramite.setFolioTramite(String.valueOf(reporteRespuesta.getSolicitud()));

        Usuario usuario = new Usuario();

        usuario.setIdUsr(reporteRespuesta.getAsegurado());
        usuario.setOwner(false);
        usuario.setTipoIdUsr(TIPO_ID_USR);

        documentReq.setDocumento(doc);
        documentReq.setTramite(tramite);
        documentReq.setUsuario(usuario);

        DocumentRes documentRes = bovedaV3Utility.getDocumentBovedaV3(documentReq);

        return documentRes;
    }

    public String actualizarDocumento(ReporteRespuesta reporteRespuesta) {
        SalidaActualizacion salida;
        salida = bovedaUtility.actualizarDocumentV2(reporteRespuesta);
        if (salida != null) {
            return salida.getClave();
        }
        return "N";
    }

}
