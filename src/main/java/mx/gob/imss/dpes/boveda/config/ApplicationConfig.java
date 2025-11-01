package mx.gob.imss.dpes.boveda.config;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        //addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(mx.gob.imss.dpes.boveda.assembler.BovedaAssembler.class);
        resources.add(mx.gob.imss.dpes.boveda.endpoint.BovedaEndPoint.class);
        resources.add(mx.gob.imss.dpes.boveda.service.BovedaServiceUtil.class);
        resources.add(mx.gob.imss.dpes.boveda.service.BovedaUtil.class);
        resources.add(mx.gob.imss.dpes.boveda.service.CreateBovedaService.class);
        resources.add(mx.gob.imss.dpes.boveda.service.LoadBovedaService.class);
        resources.add(mx.gob.imss.dpes.boveda.service.LoadBovedaV3Service.class);
        resources.add(mx.gob.imss.dpes.boveda.service.UpdateBovedaService.class);
        resources.add(mx.gob.imss.dpes.common.exception.AlternateFlowMapper.class);
        resources.add(mx.gob.imss.dpes.common.exception.BusinessMapper.class);
        resources.add(mx.gob.imss.dpes.common.rule.MontoTotalRule.class);
        resources.add(mx.gob.imss.dpes.common.rule.PagoMensualRule.class);
    }
    
}