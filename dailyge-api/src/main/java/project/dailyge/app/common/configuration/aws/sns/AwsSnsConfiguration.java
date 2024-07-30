package project.dailyge.app.common.configuration.aws.sns;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AwsSnsConfiguration {

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Bean
    public SnsClient snsClient() {
        final AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        final AwsCredentialsProvider provider = StaticCredentialsProvider.create(credentials);
        return SnsClient.builder()
            .credentialsProvider(provider)
            .region(Region.of(region))
            .build();
    }
}
