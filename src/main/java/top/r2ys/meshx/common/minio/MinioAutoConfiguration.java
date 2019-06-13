package top.r2ys.meshx.common.minio;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import top.r2ys.meshx.common.minio.service.MinioTemplate;

/**
 * minio 自动配置类
 *
 * @author r2ys
 */
@AllArgsConstructor
@Configuration
@Order(-1000)
@EnableConfigurationProperties({MinioProperties.class})
public class MinioAutoConfiguration {

	private final MinioProperties minioProperties;

    @Bean
    @ConditionalOnProperty(name = "minio.url")
    @ConditionalOnMissingBean(MinioTemplate.class)
    public MinioTemplate minioTemplate() {
		return new MinioTemplate(
                minioProperties.getUrl(),
                minioProperties.getAccessKey(),
                minioProperties.getSecretKey()
		);
	}



}
