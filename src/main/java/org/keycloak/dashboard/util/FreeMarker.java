package org.keycloak.dashboard.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.TimeZone;

public class FreeMarker {

    private Map<String, Object> attributes;
    private Configuration configuration;

    public FreeMarker(Map<String, Object> attributes) {
        this.attributes = attributes;

        configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setClassLoaderForTemplateLoading(FreeMarker.class.getClassLoader(), "/");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setTimeZone(TimeZone.getTimeZone("UTC"));

        DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_31);
        owb.setDefaultDateType(TemplateDateModel.DATETIME);

        configuration.setObjectWrapper(owb.build());
    }

    public void template(String template, File output) throws IOException, TemplateException {
        Template t = configuration.getTemplate(template);

        File parent = output.getParentFile();
        if (!parent.isDirectory()) {
            parent.mkdir();
        }

        Writer w = new FileWriter(output);
        t.process(attributes, w);
    }

}