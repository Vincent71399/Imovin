package sg.edu.nus.imovin2.HttpConnection;

import android.util.Log;

import org.json.JSONObject;

import sg.edu.nus.imovin2.Objects.Oauth2TokenRequest;
import sg.edu.nus.imovin2.System.FitbitConstants;
import sg.edu.nus.imovin2.System.LogConstants;

public class UploadRequests {
    public static JSONObject Oauth2Login(String code){
        Oauth2TokenRequest oauth2TokenRequest = new Oauth2TokenRequest(code);

        JSONObject responseObject = CommonRequests.PostRequestWithSecret(FitbitConstants.TokenRequestURI, FitbitConstants.getCombinedID_Secret(), oauth2TokenRequest.getPostForm());

        Log.d(LogConstants.LogTag, responseObject.toString());

        return responseObject;
    }
}
