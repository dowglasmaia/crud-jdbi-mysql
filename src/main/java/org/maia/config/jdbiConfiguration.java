package org.maia.config;

import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import org.maia.dao.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;


import javax.sql.DataSource;
import java.util.List;

@Configuration
@Slf4j
public class jdbiConfiguration {

    @Bean
    public Jdbi jdbi(DataSource ds,List<JdbiPlugin> jdbiPlugins, List<RowMapper<?>> rowMappers) {
        TransactionAwareDataSourceProxy proxy = new TransactionAwareDataSourceProxy(ds);
        Jdbi jdbi = Jdbi.create(proxy);

        // Register all available plugins
        log.info("[I27] Installing plugins... ({} found)", jdbiPlugins.size());
        jdbiPlugins.forEach(plugin -> jdbi.installPlugin(plugin));

        // Register all available rowMappers
        log.info("[I31] Installing rowMappers... ({} found)", rowMappers.size());
        rowMappers.forEach(mapper -> jdbi.registerRowMapper(mapper));

        return jdbi;
    }

    @Bean
    public UserDao userDao(Jdbi jdbi){
        return jdbi.onDemand(UserDao.class);
    }

    @Bean
    public JdbiPlugin sqlObjectPlugin() {
        return new SqlObjectPlugin();
    }
}
