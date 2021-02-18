package br.com.redhat.configuration;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.learnwebservices.services.tempconverter.TempConverterEndpoint;

@Configuration
@Component
public class AppBeans {
	
	@Autowired
	private MQConfiguration mqConfiguration;

	@Bean
	public ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory amq = new ActiveMQConnectionFactory(mqConfiguration.getUser(), mqConfiguration.getPassword(), mqConfiguration.getUrl());
		PooledConnectionFactory factory = new PooledConnectionFactory(amq);
		
		return factory;
	}
	
//    @Bean
//    public DataSource getDataSource() {
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.driverClassName("org.h2.Driver");
//        dataSourceBuilder.url("jdbc:h2:mem:test");
//        dataSourceBuilder.username("sa");
//        dataSourceBuilder.password("");
//        return dataSourceBuilder.build();
//    }
	
	@Bean(name = "soapWebservice")
	public CxfEndpoint buildCxfEndpoint() {
		CxfEndpoint cxf = new CxfEndpoint();
		cxf.setAddress("http://www.learnwebservices.com/services/tempconverter?wsdl");
		cxf.setServiceClass(TempConverterEndpoint.class);
		return cxf;
	}
	
	@Bean(name = "sqsClient")
	public AmazonSQS awsSQSClient() {
		AWSCredentials awsCredentials = new BasicAWSCredentials("myAccessKey", "mySecretKey");
		AmazonSQS sqs = AmazonSQSClientBuilder.standard()
				  .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				  .withRegion(Regions.US_EAST_1)
				  .build();
		
		return sqs;
	}
}
