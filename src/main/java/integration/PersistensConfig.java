package integration;

import javax.sql.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistensConfig extends PersistenceConfig {

  @Value( "${url}" )
  private String url;

  @Value( "${password}" )
  private String password;

  @Bean
  public DataSource dataSource() {

    PoolProperties poolProperties = new PoolProperties();
    String host = url;
    String jdbcUrl = "jdbc:";
    poolProperties.setUrl(jdbcUrl);
    poolProperties.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    poolProperties.setUsername("admin");

    try {
      String defaultPassword = password;
      String blowfishedPasswd = PersistenceConfig.Blowfish.blowfishEncryptBase64(defaultPassword);
      poolProperties.setPassword(PersistenceConfig.Blowfish.blowfishDecryptBase64(blowfishedPasswd));
    } catch (Exception var6) {
      //log.error("Failed to read the db password.", var6);
    }

    poolProperties.setTestWhileIdle(true);
    poolProperties.setRemoveAbandoned(true);
    poolProperties.setRemoveAbandonedTimeout(90);
    poolProperties.setTimeBetweenEvictionRunsMillis(10000);
    poolProperties.setValidationQuery("SELECT 1");
    poolProperties.setValidationQueryTimeout(2);
    poolProperties.setMinEvictableIdleTimeMillis(600000);
    poolProperties.setMinIdle(2);
    poolProperties.setMaxIdle(5);
    poolProperties.setMaxAge(900000L);
    return new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
  }

}
