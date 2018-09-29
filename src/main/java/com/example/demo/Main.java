package com.example.demo;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

/**
 * Petit exemple hyper minimaliste d'utilisation de Replicated Map
 */
public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    void run() {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        Map<String, String> map = hz.getReplicatedMap("map");

        // si aucune  conf n'est spécifiée, il utilise celle par defaut.
        // ELle est dans le classpath : hazelcast-default.xml

        map.put("1", "Tokyo");
        map.put("2", "Paris");
        map.put("3", "New York");

        System.out.println("Finished loading map");
        hz.shutdown();
    }
}
