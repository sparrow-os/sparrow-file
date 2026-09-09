package com.sparrow.file.config;

import com.sparrow.file.servlet.FileDownLoad;
import com.sparrow.file.servlet.FileUpload;
import com.sparrow.file.support.utils.path.url.PathUrlConverter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@Configuration
@ComponentScan(
        basePackages = {"com.sparrow.file"}
)
@Slf4j
public class FileAutoConfiguration {
    @Bean
    public PathUrlConverter pathUrlConverter() {
        return new PathUrlConverter();
    }

    @Bean
    public ServletRegistrationBean fileUpload() {
        return new ServletRegistrationBean(new FileUpload(), "/file-upload");
    }

    @Bean
    public ServletRegistrationBean fileDownload() {
        return new ServletRegistrationBean(new FileDownLoad(), "/file-download");
    }

    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI fileOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sparrow Community")
                        .description("Sparrow Community")
                        .contact(new Contact()
                                .name("harry")
                                .url("http://www.sparrowzoo.com")
                                .email("zh_harry@163.com"))
                        .version("1.0"));
    }

}
