package org.maia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;


@Configuration
public class SnsConfig {

    @Value("${aws.region}")
    private String awsRegion;

   // @Value("${aws.topic.arn.user}")
    private static final String awsUserEventsTopicArn = "arn:aws:sns:us-east-1:426300336245:user-events-topic";

    @Bean
    public SnsClient snsClient(){
        return SnsClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean(name = "userEventsTopicArn")
    public String snsUserEventTopicArn() {
        return awsUserEventsTopicArn;
    }
}
