package com.yenlo.oauth.token.ws;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import wso2.services.FindOAuthConsumerIfTokenIsValidResponse;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;

/**
 * Implementation of the TokenStore using the wso2 IS webservices.
 * Created by agavalda on 02/03/2015.
 */
public class WSWso2TokenStore implements TokenStore {

    private OAuthAdminService oAuthAdminService;
    private OAuth2TokenValidationService oAuth2TokenValidationService;

    public WSWso2TokenStore(final OAuthAdminService oAuthAdminService, final OAuth2TokenValidationService oAuth2TokenValidationService) {
        this.oAuthAdminService = oAuthAdminService;
        this.oAuth2TokenValidationService = oAuth2TokenValidationService;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken oAuth2AccessToken) {
        return readAuthentication(oAuth2AccessToken.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        String consumerKeyFromToken = oAuth2TokenValidationService.getConsumerKeyFromToken(token);
        OAuth2Authentication oAuth2Authentication = null;
        if (consumerKeyFromToken != null) {
            String applicationName = oAuthAdminService.getApplicationName(consumerKeyFromToken);
            HashSet<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority(applicationName));
            OAuth2Request oAuth2Request = new OAuth2Request(null, applicationName, authorities, true, null, null, null, null, null);
            oAuth2Authentication = new OAuth2Authentication(oAuth2Request, null);
        }

        return oAuth2Authentication;


    }

    @Override
    public OAuth2AccessToken readAccessToken(final String bearerToken) {
        DefaultOAuth2AccessToken oAuth2AccessToken = null;
        FindOAuthConsumerIfTokenIsValidResponse findOAuthConsumerIfTokenIsValidResponse = oAuth2TokenValidationService.getFindOAuthConsumerIfTokenIsValidResponse(bearerToken);
        if (findOAuthConsumerIfTokenIsValidResponse != null)  {
            oAuth2AccessToken = new DefaultOAuth2AccessToken(bearerToken);
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.SECOND, findOAuthConsumerIfTokenIsValidResponse.getReturn().getAccessTokenValidationResponse().getExpiryTime().intValue());
            oAuth2AccessToken.setExpiration(calendar.getTime());
        }

        return oAuth2AccessToken;

    }

    @Override
    public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {
        //nothing to do here, the lifecycle is handled by IS
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
