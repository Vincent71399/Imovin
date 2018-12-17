package sg.edu.nus.imovin.HttpConnection;

import android.util.Log;

import org.json.JSONObject;

import sg.edu.nus.imovin.Objects.Oauth2TokenRequest;
import sg.edu.nus.imovin.System.FitbitConstants;
import sg.edu.nus.imovin.System.LogConstants;

public class UploadRequests {
    public static JSONObject Oauth2Login(String code){
        Oauth2TokenRequest oauth2TokenRequest = new Oauth2TokenRequest(code);

        JSONObject responseObject = CommonRequests.PostRequestWithSecret(FitbitConstants.TokenRequestURI, FitbitConstants.getCombinedID_Secret(), oauth2TokenRequest.getPostForm());

        Log.d(LogConstants.LogTag, responseObject.toString());

        return responseObject;
    }
}
