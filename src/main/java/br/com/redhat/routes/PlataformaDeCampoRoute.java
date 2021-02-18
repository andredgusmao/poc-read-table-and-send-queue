package br.com.redhat.routes;

import java.net.UnknownHostException;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import br.com.redhat.CustomException;
import br.com.redhat.beans.ConvertToOS;
import br.com.redhat.beans.TemperatureConverterRequestBuilder;
import br.com.redhat.bootstrap.Registro;

@Component
public class PlataformaDeCampoRoute extends RouteBuilder {

	private static final String CACHE_KEY = "pc-last-id";

	
	@SuppressWarnings("unchecked")
	@Override
	public void configure() throws Exception {
		onException(CustomException.class, UnknownHostException.class)
			.handled(true)
			.log("${exception.message}")
			.setBody(simple("${header.registro}"))
	        .marshal()
	        	.json(JsonLibrary.Jackson)		
//			.wireTap("log:br.com.redhat?showAll=true&multiline=true")
    		.log("[ENVIANDO PARA DLQ]")
		.to("jms:queue:amazon-sqs-DLQ")
		;
		
		from("timer:foo?period=15000")
			.routeId("initial-timer")
			.log("[INICIANDO ROTA]")
		.to("direct:retrieve-last-id")
		.to("direct:read-table-x")
//		.wireTap("log:br.com.redhat?showAll=true&multiline=true")
		;
		
		from("direct:read-table-x")
			.routeId("read-table-x")
			.log("[LENDO TABELA X]")
			.to("sql:classpath:sql/select-registro-from-last-id.sql?outputClass=br.com.redhat.bootstrap.Registro&outputType=StreamList")
			.split(body()).streaming()
				.to("direct:handle-record")
			.end()
		;
		
		from("direct:handle-record")
			.routeId("handle-record")
			.log("[TRATANDO REGISTRO] ${body}")
//			.wireTap("log:br.com.redhat?showAll=true&multiline=true")
			.process(retrieveLastIdHeader())
			.setHeader("registro", simple("${body}"))
			.to("direct:call-webservice")
		.to("direct:update-last-id")
		;
		
		from("direct:call-webservice")
			.routeId("call-webservice")
			.log("[CHAMANDO WEBSERVICE]")
		    .bean(TemperatureConverterRequestBuilder.class)
			    .setHeader(CxfConstants.OPERATION_NAME, constant("CelsiusToFahrenheit"))
		        .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("http://learnwebservices.com/services/tempconverter"))
	        .to("cxf:bean:soapWebservice")
	        .bean(ConvertToOS.class)
	        .marshal()
	        	.json(JsonLibrary.Jackson)
//	        .wireTap("log:br.com.redhat?showAll=true&multiline=true")
		.to("direct:send-to-amazon-sqs-mock")
		;
		
		from("direct:send-to-amazon-sqs-mock")
			.routeId("send-to-amazon-sqs-mock")
			.log("[ENVIANDO PARA AMAZON SQS (AMQ)]")
		.to("jms:queue:amazon-sqs")
		;
		
//		from("direct:send-to-amazon-sqs")
//			.routeId("send-to-amazon-sqs")
//			.log("[ENVIANDO PARA AMAZON SQS]")
//		.to("aws-sqs://teste?amazonSQSClient=#sqsClient&defaultVisibilityTimeout=10")
//		;
		
		from("direct:retrieve-last-id")
			.routeId("retrieve-last-id")	
			.log("[RECUPERANDO ULTIMO REGISTRO DO CACHE]")
			.setHeader("lastId", constant(0))
			.setHeader("cache-key", constant(CACHE_KEY))
		.to("direct:get-from-cache")
		.choice()
			.when(header("cache-hit").isEqualTo(true))
				.setHeader("lastId", simple("${body}"))
			.endChoice()
		.end()
		;
		
		from("direct:update-last-id")
			.routeId("update-last-id")
			.log("[ATUALIZANDO ULTIMO REGISTRO NO CACHE]")
			.setHeader("cache-key", constant(CACHE_KEY))
			.setBody(simple("${header.lastId}"))
		.to("direct:add-to-cache");
	}

	private Processor retrieveLastIdHeader() {
		return e -> {
			Registro registro = e.getIn().getBody(Registro.class);
			e.getIn().setHeader("lastId", registro.getId());
		};
	}
}
