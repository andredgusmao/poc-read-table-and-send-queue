package br.com.redhat.routes;

import javax.jms.ConnectionFactory;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaixaRoute extends RouteBuilder {
	
	@Autowired
	private ConnectionFactory factory;
	
	private JacksonDataFormat dataFormat;
	
	@Override
	public void configure() throws Exception {
//		this.preConfigure();
//		
//		dataFormat = new JacksonDataFormat(Compra.class);
//
//		from("timer:nome-do-timer?period=3000")
//			.routeId("nova-compra")
//			.process(exchange -> {
//				Compra compra = new Compra();
//				exchange.getIn().setBody(compra);
//			})
//			.marshal()
//				.json(JsonLibrary.Jackson, false)
//			.log("[NOVA COMPRA] ${body}")
//		.to("jms:queue:nova-compra");
//		
//		from("jms:queue:pagamento-recusado")
//			.unmarshal(dataFormat)
//			.process(exchange -> {
//				Compra compra = exchange.getIn().getBody(Compra.class);
//				compra.setReprocessamento(true);
//			})
//			.marshal()
//				.json(JsonLibrary.Jackson, false)
//			.log("[REPROCESSANDO COMPRA] ${body}")
//		.to("jms:queue:pagamento-aprovado");
	}
	
	private void preConfigure() {
		this.getContext().addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(factory));
	}
	
}
