package com.kss.dataprocess.application.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Freemarker 模板工具
 */
public class FreemarkerUtil {

    private static Logger logger = LoggerFactory.getLogger(FreemarkerUtil.class);

    /**
     * 获取 html
     * @param templateName 模板名称
     * @param data 模板所需数据
     * @return
     */
    public static String htmlGenerator(String templateName, Map data) {
        Configuration config = new Configuration(Configuration.VERSION_2_3_23);
        // 设置 Freemarker 模板加载路径
        config.setClassForTemplateLoading(FreemarkerUtil.class, "/templates/");
        config.setDefaultEncoding("UTF-8");
        Template tp = null;
        StringWriter writer = null;
        String htmlStr = null;
        try {
            tp = config.getTemplate(templateName);
            writer = new StringWriter();
            tp.setOutputEncoding("UTF-8");
            // 将模板和数据写入 writer 中
            tp.process(data, writer);
            htmlStr = writer.toString();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (TemplateException e) {
            logger.error(e.getMessage(), e);
        } finally {

            try {
                if (writer != null)
                {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return htmlStr;
    }

}
