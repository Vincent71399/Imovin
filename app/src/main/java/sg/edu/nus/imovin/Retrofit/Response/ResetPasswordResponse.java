package sg.edu.nus.imovin.Retrofit.Response;

import sg.edu.nus.imovin.Retrofit.Object.MetaData;
import sg.edu.nus.imovin.Retrofit.Object.ResponseData;

public class ResetPasswordResponse {
    private MetaData meta;
    private ResponseData response;

    public ResetPasswordResponse(MetaData meta, ResponseData response) {
        this.meta = meta;
        this.response = response;
    }

    public MetaData getMeta() {
        return meta;
    }

    public void setMeta(MetaData meta) {
        this.meta = meta;
    }

    public ResponseData getResponse() {
        return response;
    }

    public void setResponse(ResponseData response) {
        this.response = response;
    }
}
