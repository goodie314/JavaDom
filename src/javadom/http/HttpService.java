package javadom.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by u6062536 on 2/1/2018.
 */
public class HttpService {

    public static HttpResponse get(String targetUrl) throws IOException {
        HttpURLConnection connection = null;
        Integer statusCode;
        String statusMessage;
        BufferedReader reader;
        String line;
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(targetUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.connect();
            statusCode = connection.getResponseCode();
            statusMessage = connection.getResponseMessage();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return new HttpResponse(statusCode, statusMessage, response.toString());
    }
}
