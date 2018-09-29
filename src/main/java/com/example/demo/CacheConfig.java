package com.example.demo;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    /**
     * Le cache manager est utile pour Spring Cache.
     * nécessité d'avoir importé le hazelcast-spring pour qu'il propose le hazelcastCacheManager
     */
    @Bean
    CacheManager cacheManager(HazelcastInstance instance) {

        return new HazelcastCacheManager(instance);
    }

    // peut etre pas utile car créé automatiquement par SPring Boot en ce basant sur la conf ?
//    @Bean
//    HazelcastInstance hazelcastInstance(Config hazelcastConfig) {
//        return Hazelcast.newHazelcastInstance(hazelcastConfig);
//    }

    @Bean
    public Config hazelcastConfig() {
        Config config = new Config();

        // s'inspirer de la conf par defaut : classpath:hazelcast-default.xml
        // J'ai 2 config ici :
        // la Map répliquée : qui sert pour le AllInCache..
        ReplicatedMapConfig replicatedMapConfig = config.getReplicatedMapConfig( "replicated" );

        replicatedMapConfig.setInMemoryFormat( InMemoryFormat.OBJECT )
                .setQuorumName( "quora" );

        // ... et le IMap qui sert pour le cache Spring Cache (qui ne supporte que le IMap)
        MapConfig mapConfig=config.getMapConfig("distributed");
        // j'ai une pseudo config à customiser selon les besoins...
        mapConfig.setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                        .setEvictionPolicy(EvictionPolicy.LRU)
                        .setTimeToLiveSeconds(20);

        // conf réseau : comment les instances se trouvent
        NetworkConfig network = config.getNetworkConfig();
        network.setPort(5701).setPortCount(20);
        network.setPortAutoIncrement(true);
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig()
                .addMember("machine1")
                .addMember("localhost").setEnabled(true);

        return config;
    }

}
