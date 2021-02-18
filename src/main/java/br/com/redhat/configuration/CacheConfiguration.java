package br.com.redhat.configuration;

import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CacheConfiguration {

    @Bean(name = "cacheContainer")
    public DefaultCacheManager remoteCacheManagerExample() {
    	ConfigurationBuilder builder = new ConfigurationBuilder();
        Configuration configuration = builder
        		.simpleCache(true)
        		.build();
		DefaultCacheManager cacheManager = new DefaultCacheManager(configuration);
		
		cacheManager.createCache("teste", configuration);
        
		return cacheManager;
    }
}

