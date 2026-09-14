package com.sparrow.file.boot;

import com.sparrow.file.config.FileOpenApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class AppConfig {

    /**
     * 注册 OpenAPI 元信息。
     *
     * <ul>
     *   <li>{@link ConditionalOnMissingBean}：若宿主项目已自定义 {@link OpenAPI} Bean，
     *       则本 Bean 不再注册，宿主配置优先。</li>
     *   <li>{@link ConditionalOnProperty}：通过 {@code sparrow.file.openapi.enabled=false}
     *       可关闭本 starter 提供的元信息。</li>
     * </ul>
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "sparrow.file.openapi", name = "enabled",
            havingValue = "true", matchIfMissing = true)
    public OpenAPI fileOpenAPI(FileOpenApiProperties properties) {
        Info info = new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion());

        if (StringUtils.hasText(properties.getTermsOfService())) {
            info.setTermsOfService(properties.getTermsOfService());
        }

        FileOpenApiProperties.Contact contact = properties.getContact();
        if (contact != null) {
            info.contact(new Contact()
                    .name(contact.getName())
                    .url(contact.getUrl())
                    .email(contact.getEmail()));
        }

        FileOpenApiProperties.License license = properties.getLicense();
        if (license != null && (StringUtils.hasText(license.getName()) || StringUtils.hasText(license.getUrl()))) {
            info.license(new License().name(license.getName()).url(license.getUrl()));
        }

        return new OpenAPI().info(info);
    }
}
