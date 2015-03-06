package com.yenlo.oauth.token.ws;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import wso2.services.GetOAuthApplicationData;
import wso2.services.GetOAuthApplicationDataResponse;

/**
 * Implementation of the OAuthAdminService
 * Created by agavalda on 02/03/2015.
 */
public class OAuthAdminService extends WebServiceGatewaySupport {

    /**
     * returns the application name from a consumer key (client id)
     * @param consumerKey
     * @return
     */
    public String getApplicationName(final String consumerKey) {
        GetOAuthApplicationData getOAuthApplicationData = new GetOAuthApplicationData();
        getOAuthApplicationData.setConsumerKey(consumerKey);
        GetOAuthApplicationDataResponse response = (GetOAuthApplicationDataResponse) getWebServiceTemplate().marshalSendAndReceive(getOAuthApplicationData);
        return response.getReturn().getApplicationName();
    }
}
