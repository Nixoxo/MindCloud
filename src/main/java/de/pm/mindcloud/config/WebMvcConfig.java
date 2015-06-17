package de.pm.mindcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.xslt.XsltView;
import org.springframework.web.servlet.view.xslt.XsltViewResolver;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver getXSLTViewResolver() {
        XsltViewResolver xsltResolover = new XsltViewResolver();
        xsltResolover.setOrder(1);
        xsltResolover.setSourceKey("xmlSource");

        xsltResolover.setViewClass(XsltView.class);
        xsltResolover.setViewNames(new String[]{"XSLTView"});
        xsltResolover.setPrefix("classpath:templates/");
        xsltResolover.setSuffix(".xsl");

        return xsltResolover;
    }
}