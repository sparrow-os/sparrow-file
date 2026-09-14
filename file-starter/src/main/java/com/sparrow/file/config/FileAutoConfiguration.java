package com.sparrow.file.config;

import com.sparrow.file.servlet.FileDownLoad;
import com.sparrow.file.servlet.FileUpload;
import com.sparrow.file.support.utils.path.url.PathUrlConverter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@ComponentScan(
        basePackages = {"com.sparrow.file"}
)
@EnableConfigurationProperties(FileOpenApiProperties.class)
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

    /**
     * 注册一个分组，用于在 Swagger UI 右上角下拉中按应用隔离接口文档。
     *
     * <ul>
     *   <li>{@link ConditionalOnMissingBean}（按 bean 名）：仅当宿主没有同名 {@code fileGroup}
     *       时才注册，避免覆盖宿主自定义，同时允许宿主再注册其它分组共存。</li>
     *   <li>{@link ConditionalOnProperty}：与 {@code sparrow.file.openapi.enabled} 联动，
     *       关闭时一并移除分组。</li>
     * </ul>
     */
    @Bean
    @ConditionalOnMissingBean(name = "fileGroup")
    @ConditionalOnProperty(prefix = "sparrow.file.openapi", name = "enabled",
            havingValue = "true", matchIfMissing = true)
    public GroupedOpenApi fileGroup(FileOpenApiProperties properties) {
        return GroupedOpenApi.builder()
                .group(properties.getGroup())
                .packagesToScan(properties.getPackagesToScan())
                .build();
    }
}
