package sg.edu.nus.imovin2.System;

import sg.edu.nus.imovin2.Common.CommonFunc;

public class FitbitConstants {

    public static final String ClientID = "22DCXJ";
    public static final String Secret = "e38ae53f136d14d6190a5b361b3d029a";
    public static final String RedirectURI = "imovin://oauthcallback";
    public static final String AuthorizationURI = "https://www.fitbit.com/oauth2/authorize";
    public static final String TokenRequestURI = "https://api.fitbit.com/oauth2/token";
    public static final String Scopes = "activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight";
    public static final Long ExpiresIn = 2592000l;
    public static final String ResponseType = "code";
    public static final String GrantType = "authorization_code";
    public static final String AuthorizationRequestState = "ars";
    public static final String TokenRequestState = "trs";

    public static final String FitbitAuthorizationFormat = "?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&expires_in=%d&state=%s";

    public static String getCombinedID_Secret(){
        String combinedString = CommonFunc.Base64Encode(ClientID + ":" + Secret);
        combinedString = combinedString.replace("\n", "");
        return "Basic " + combinedString;
    }
}
