package com.yenlo.oauth.token.ws;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import wso2.services.FindOAuthConsumerIfTokenIsValid;
import wso2.services.FindOAuthConsumerIfTokenIsValidResponse;
import wso2.services.OAuth2TokenValidationRequestDTO;
import wso2.services.OAuth2TokenValidationRequestDTOOAuth2AccessToken;

/**
 * Implementation of OAuth2TokenValidationService
 * Created by agavalda on 02/03/2015.
 */
public class OAuth2TokenValidationService extends WebServiceGatewaySupport {


    /**
     * returns the consumer key from the token.
     * @param token
     * @return
     */
    public String getConsumerKeyFromToken(final String token) {
        FindOAuthConsumerIfTokenIsValidResponse response = getFindOAuthConsumerIfTokenIsValidResponse(token);
        return response.getReturn().getConsumerKey();
    }

    /**
     *
     * @param token
     * @return
     */
    public FindOAuthConsumerIfTokenIsValidResponse getFindOAuthConsumerIfTokenIsValidResponse(String token) {
        FindOAuthConsumerIfTokenIsValid findOAuthConsumerIfTokenIsValid = new FindOAuthConsumerIfTokenIsValid();
        OAuth2TokenValidationRequestDTO value = new OAuth2TokenValidationRequestDTO();
        OAuth2TokenValidationRequestDTOOAuth2AccessToken accessToken = new OAuth2TokenValidationRequestDTOOAuth2AccessToken();
        accessToken.setTokenType("bearer");
        accessToken.setIdentifier(token);
        value.setAccessToken(accessToken);
        findOAuthConsumerIfTokenIsValid.setValidationReqDTO(value);

        return (FindOAuthConsumerIfTokenIsValidResponse) getWebServiceTemplate().marshalSendAndReceive(findOAuthConsumerIfTokenIsValid);
    }
}
