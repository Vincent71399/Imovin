package sg.edu.nus.imovin.Common;

import android.content.Context;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

public class AuthDownloader extends BaseImageDownloader {

    private String header_key;
    private String header_value;

    public AuthDownloader(Context context, String key, String value){
        super(context);
        this.header_key = key;
        this.header_value = value;
    }

    @Override
    protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
        HttpURLConnection conn = super.createConnection(url, extra);
        conn.setRequestProperty(header_key, header_value);
        return conn;
    }
}
