package com.yenlo.oauth.token.jdbc;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * Token store connected to wso2 IS database
 * Created by agavalda on 27/02/2015.
 */
public class JdbcWso2TokenStore implements TokenStore {

    private static final String DEFAULT_ACCESS_TOKEN_SELECT_STATEMENT = "select ACCESS_TOKEN,TOKEN_SCOPE, REFRESH_TOKEN,TIME_CREATED,VALIDITY_PERIOD from IDN_OAUTH2_ACCESS_TOKEN where ACCESS_TOKEN = ? and TOKEN_STATE = 'ACTIVE'";
    private static final String UPDATE_EXPIRED_ACCESS_TOKEN_SELECT_STATEMENT = "update  IDN_OAUTH2_ACCESS_TOKEN set TOKEN_STATE = 'EXPIRED' where ACCESS_TOKEN = ?";
    private static final String DEFAULT_CONSUMER_SELECT_STATEMENT = "SELECT consu.APP_NAME FROM IDN_OAUTH_CONSUMER_APPS consu WHERE consu.CONSUMER_KEY = (select token.CONSUMER_KEY FROM IDN_OAUTH2_ACCESS_TOKEN token WHERE token.ACCESS_TOKEN = ?)";


    private final JdbcTemplate jdbcTemplate;

    public JdbcWso2TokenStore(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource required");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken oAuth2AccessToken) {
        return readAuthentication(oAuth2AccessToken.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        OAuth2Request oAuth2Request = jdbcTemplate.queryForObject(DEFAULT_CONSUMER_SELECT_STATEMENT,
                Wso2RowMapper.oAuth2RequestRowMapper(),
                token);
        return new OAuth2Authentication(oAuth2Request, null);
    }

    @Override
    public OAuth2AccessToken readAccessToken(final String bearerToken) {
        OAuth2AccessToken accessToken = null;
        try {
            accessToken =  jdbcTemplate.queryForObject(DEFAULT_ACCESS_TOKEN_SELECT_STATEMENT,
                                                        Wso2RowMapper.oAuth2AccessTokenMapper(),
                                                        bearerToken);
        } catch (EmptyResultDataAccessException e) {
        }
        return accessToken;

    }

    @Override
    public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {
        String token = oAuth2AccessToken.getValue();
        jdbcTemplate.update(UPDATE_EXPIRED_ACCESS_TOKEN_SELECT_STATEMENT, token);
    }


    @Override
    public void storeAccessToken(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken, OAuth2Authentication oAuth2Authentication) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication oAuth2Authentication) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String s) {
        throw new UnsupportedOperationException();
    }





}
