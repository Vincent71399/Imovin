package sg.edu.nus.imovin2.Objects;

import java.util.Locale;

import sg.edu.nus.imovin2.System.FitbitConstants;

public class Oauth2TokenRequest {
    private String grant_type;
    private String code;
    private String redirect_uri;
    private String client_id;
    private Long expires_in;
    private String state;

    private static final String PostFormBasic = "grant_type=%s&code=%s&redirect_uri=%s&client_id=%s&expires_in=%d&state=%s";

    public Oauth2TokenRequest(String code) {
        this.grant_type = FitbitConstants.GrantType;
        this.code = code;
        this.redirect_uri = FitbitConstants.RedirectURI;
        this.client_id = FitbitConstants.ClientID;
        this.expires_in = FitbitConstants.ExpiresIn;
        this.state = FitbitConstants.TokenRequestState;
    }

    public String getPostForm(){
        String postForm = String.format(
                Locale.ENGLISH,
                PostFormBasic,
                this.grant_type,
                this.code,
                this.redirect_uri,
                this.client_id,
                this.expires_in,
                this.state);
        return postForm;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
