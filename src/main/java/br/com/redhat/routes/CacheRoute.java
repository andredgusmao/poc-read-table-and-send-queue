package br.com.redhat.routes;

import java.util.Objects;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.infinispan.InfinispanConstants;
import org.apache.camel.component.infinispan.InfinispanOperation;
import org.springframework.stereotype.Component;

@Component
public class CacheRoute extends RouteBuilder {

	private static final String CACHE_NAME = "teste";

	@Override
	public void configure() throws Exception {
		from("direct:get-from-cache")
			.routeId("get-from-cache")
			.setHeader(InfinispanConstants.OPERATION, constant(InfinispanOperation.GET))
	        .setHeader(InfinispanConstants.KEY, simple("${header.cache-key}"))
	    .to("infinispan://" + CACHE_NAME + "?cacheContainer=#cacheContainer")
	    .process(CacheRoute::addCacheHitHeader);

        from("direct:add-to-cache")
	        .routeId("add-to-cache")
	        .setHeader(InfinispanConstants.OPERATION, constant(InfinispanOperation.PUT))
	        .setHeader(InfinispanConstants.KEY, simple("${header.cache-key}"))
//	        .setHeader(InfinispanConstants.LIFESPAN_TIME).constant(configuration.getCacheLifespanTime())
//	        .setHeader(InfinispanConstants.LIFESPAN_TIME_UNIT).constant(TimeUnit.MILLISECONDS.toString())
//	        .setHeader(InfinispanConstants.MAX_IDLE_TIME).constant(-1)
//	        .setHeader(InfinispanConstants.MAX_IDLE_TIME_UNIT).constant(TimeUnit.MILLISECONDS.toString())
	        .setHeader(InfinispanConstants.VALUE, simple("${body}"))
	    .to("infinispan:" + CACHE_NAME + "?cacheContainer=#cacheContainer");
	}

	public static void addCacheHitHeader(Exchange exchange) {
		final Message message = exchange.getIn();
		boolean hasBody = Objects.nonNull(message.getBody());
		message.setHeader("cache-hit", hasBody);
	}
}
