package br.com.allen.ifood.registration;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

public class RegistrationTestLifecycleManager implements QuarkusTestResourceLifecycleManager {
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:12.2");

    @Override
    public Map<String, String> start() {
        POSTGRES.start();
        Map<String, String> properties = new HashMap<>();

        properties.put("quarkus.datasource.url", POSTGRES.getJdbcUrl());
        properties.put("quarkus.datasource.username", POSTGRES.getUsername());
        properties.put("quarkus.datasource.password", POSTGRES.getPassword());
        return properties;
    }

    @Override
    public void stop() {
        if (POSTGRES != null && POSTGRES.isRunning()) {
            POSTGRES.stop();
        }
    }

    public static DockerImageName parse(String fullImageName) {
        return new DockerImageName(fullImageName);
    }
}
