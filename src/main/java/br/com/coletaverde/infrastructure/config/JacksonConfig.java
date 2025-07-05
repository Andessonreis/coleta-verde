package br.com.coletaverde.infrastructure.config;


import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    /**
     * Cria e registra o módulo do Hibernate para o Jackson.
     * Este módulo ensina o Jackson a lidar corretamente com os tipos de dados do Hibernate,
     * especialmente os proxies de lazy-loading, evitando erros de serialização.
     *
     * @return um Módulo do Jackson configurado para o Hibernate.
     */
    @Bean
    public Module hibernate5JakartaModule() {
        return new Hibernate5JakartaModule();
    }
}