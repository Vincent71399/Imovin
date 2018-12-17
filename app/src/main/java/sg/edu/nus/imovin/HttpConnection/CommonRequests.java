package sg.edu.nus.imovin.HttpConnection;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommonRequests {
    private static final Integer connect_timeout = 10000;
    private static final Integer read_timeout = 15000;

    public static JSONObject GetRequest(String urlString){
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setConnectTimeout(connect_timeout);
            connection.setReadTimeout(read_timeout);
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }

            reader.close();

            connection.disconnect();

            String responseString = sb.toString();

            JSONObject jsonResponse = new JSONObject(responseString);

            return jsonResponse;

        }catch(Exception e){
            Log.e("GetRequestException", e.toString());
            return null;
        }
    }

    public static JSONObject PostRequest(String urlString, String postObject){
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setConnectTimeout(connect_timeout);
            connection.setReadTimeout(read_timeout);
            connection.connect();

            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            osw.write(postObject.toString());
            osw.flush();
            osw.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }

            reader.close();

            connection.disconnect();

            String responseString = sb.toString();

            JSONObject jsonResponse = new JSONObject(responseString);

            return jsonResponse;

        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject PostRequest(String urlString, JSONObject postObject){
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setConnectTimeout(connect_timeout);
            connection.setReadTimeout(read_timeout);
            connection.connect();

            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            osw.write(postObject.toString());
            osw.flush();
            osw.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }

            reader.close();

            connection.disconnect();

            String responseString = sb.toString();

            JSONObject jsonResponse = new JSONObject(responseString);

            return jsonResponse;

        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject PostRequestWithSecret(String urlString, String authorization, String postForm){
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("authorization",authorization);
            connection.setConnectTimeout(connect_timeout);
            connection.setReadTimeout(read_timeout);
            connection.connect();

            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            osw.write(postForm);
            osw.flush();
            osw.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }

            reader.close();

            connection.disconnect();

            String responseString = sb.toString();

            JSONObject jsonResponse = new JSONObject(responseString);

            return jsonResponse;

        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
