package com.proyecto.Oksumoda.Utils;

import freemarker.template.Template;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import jakarta.servlet.http.HttpServletResponse;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;
import java.util.Map;

@Component
public class PdfGenerator {

    private final FreeMarkerConfigurer configurer;

    public PdfGenerator(FreeMarkerConfigurer configurer) {
        this.configurer = configurer;
    }

    /**
     * Genera un PDF a partir de una plantilla FreeMarker
     * 
     * @param templateName Nombre del archivo de plantilla (sin extensión .ftl)
     * @param model        Mapa con los datos a pasar a la plantilla
     * @param response     Respuesta HTTP donde se escribirá el PDF
     * @param fileName     Nombre del archivo PDF a descargar
     * @throws Exception si hay error en la generación
     */
    public void generarPdf(String templateName, Map<String, Object> model, 
                          HttpServletResponse response, String fileName) throws Exception {
        
        // Cargar la plantilla FreeMarker
        Template template = configurer.getConfiguration().getTemplate(templateName + ".ftl");
        
        // Procesar la plantilla con los datos
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        
        // Configurar la respuesta HTTP
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".pdf");
        
        // Generar el PDF
        OutputStream out = response.getOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(out);
        out.close();
    }
}