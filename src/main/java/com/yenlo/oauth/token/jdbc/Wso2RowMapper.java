package com.yenlo.oauth.token.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

/**
 * DataSource row mapper helper
 * Created by agavalda on 28/02/2015.
 */
public class Wso2RowMapper {

    /**
     * Convert IDN_OAUTH2_ACCESS_TOKEN table
     * @return
     */
    public static RowMapper<OAuth2AccessToken> oAuth2AccessTokenMapper() {
        return new RowMapper<OAuth2AccessToken>() {
            @Override
            public OAuth2AccessToken mapRow(final ResultSet rs, int rowNum) throws SQLException {
                DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(rs.getString(1));
                defaultOAuth2AccessToken.setRefreshToken(new DefaultOAuth2RefreshToken(rs.getString(3)));
                Set<String> scope = new HashSet<String>();
                scope.add(rs.getString(2));
                defaultOAuth2AccessToken.setScope(scope);
                Timestamp date = rs.getTimestamp(4);
                Calendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(date.getTime());
                calendar.add(Calendar.MILLISECOND, rs.getInt(5));
                defaultOAuth2AccessToken.setExpiration(calendar.getTime());
                return defaultOAuth2AccessToken;
            }
        };
    }

    /**
     * Convert IDN_OAUTH_CONSUMER_APPS
     * @return
     */
    public static RowMapper<OAuth2Request> oAuth2RequestRowMapper() {
        return new RowMapper<OAuth2Request>() {
            @Override
            public OAuth2Request mapRow(ResultSet rs, int rowNum) throws SQLException {
                String appName = rs.getString(1);
                //set the authority as the name of the app on wso2IS
                HashSet<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
                authorities.add(new SimpleGrantedAuthority(appName));
                //TODO null values?
                return new OAuth2Request(null,appName, authorities,true,null,null,null,null,null);
            }
        };
    }
}
