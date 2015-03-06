package com.yenlo.oauth;

import com.yenlo.oauth.token.ws.OAuth2TokenValidationService;
import com.yenlo.oauth.token.ws.OAuthAdminService;
import com.yenlo.oauth.token.ws.WSWso2TokenStore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;
import sun.misc.BASE64Encoder;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * OAuth2 Resource Server configuration
 * Created by Gavalda on 2/27/2015.
 */
@Configuration
@EnableResourceServer
public class OAuth2ServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String APPROVED_APPLICATION = "serviceProvider";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        //JDBC
        //TokenStore wso2TokenStore = new JdbcWso2TokenStore(wso2OauthDataSource());
        //WS
        TokenStore wso2TokenStore = new WSWso2TokenStore(oAuthAdminServiceClient(marshaller()), oAuth2TokenValidationService(marshaller()));
        //EXAMPLE: DO NOT DO THIS
        System.setProperty("javax.net.ssl.trustStore", "C:/dev/wso2is-5.0.0/repository/resources/security/wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        resources.tokenStore(wso2TokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //sets the authority as the application name defined in wso2
        http
                .authorizeRequests()
                .antMatchers("/greeting").hasAuthority(APPROVED_APPLICATION);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource_oauth")
    public DataSource wso2OauthDataSource() {
        return DataSourceBuilder.create().build();
    }


    private Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("wso2.services");
        return marshaller;
    }

    private HttpUrlConnectionMessageSender messageSender() {
        return new HttpUrlConnectionMessageSender() {
            @Override
            protected void prepareConnection(HttpURLConnection connection) throws IOException {
                BASE64Encoder enc = new sun.misc.BASE64Encoder();
                String userpassword = "admin:admin";
                String encodedAuthorization = enc.encode(userpassword.getBytes());
                connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);

                super.prepareConnection(connection);
            }
        };
    }

    private OAuthAdminService oAuthAdminServiceClient(Jaxb2Marshaller marshaller) {
        OAuthAdminService client = new OAuthAdminService();
        client.setDefaultUri("https://localhost:9443/services/OAuthAdminService.OAuthAdminServiceHttpsSoap11Endpoint/");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(messageSender());
        return client;
    }

    private OAuth2TokenValidationService oAuth2TokenValidationService(Jaxb2Marshaller marshaller) {
        OAuth2TokenValidationService client = new OAuth2TokenValidationService();
        client.setDefaultUri("https://localhost:9443/services/OAuth2TokenValidationService.OAuth2TokenValidationServiceHttpsSoap12Endpoint/");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(messageSender());
        return client;
    }

}
