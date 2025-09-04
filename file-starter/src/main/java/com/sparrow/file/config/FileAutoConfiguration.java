package com.sparrow.file.config;

import com.sparrow.file.servlet.FileDownLoad;
import com.sparrow.file.servlet.FileUpload;
import com.sparrow.file.support.utils.path.url.PathUrlConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
@Configuration
@ComponentScan(
        basePackages = {"com.sparrow.file"}
)
@Slf4j
@EnableSwagger2WebMvc
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
    @ConditionalOnMissingBean(ApiInfo.class)
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Sparrow Community").description("Sparrow Community").termsOfServiceUrl("www.sparrowzoo.com").contact(new Contact("harry", "http://www.sparrowzoo.com", "zh_harry@163.com")).version("1.0").build();
    }
    @Bean
    public Docket fileDocket(ApiInfo apiInfo) {
        return new
                Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo).groupName("文件中台").select().apis(
                RequestHandlerSelectors.basePackage("com.sparrow.file.controller")
        ).paths(PathSelectors.any()).build();
    }
}
