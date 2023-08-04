package notification_bot;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import javax.ws.rs.core.MediaType;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class VipTalkBotService {

    private String botUrl;
    private String recipients;
    private final CloseableHttpClient httpClient;
    private final ResponseHandler<String> responseHandler;

    public VipTalkBotService(String propertiesFile) {
        try (FileInputStream fis = new FileInputStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(fis);
            botUrl = properties.getProperty("bot.viptalk.url");
            recipients = properties.getProperty("recipients");
        } catch (Exception e) {
            System.out.println("Loading config failed: " + propertiesFile);
        }

        List<BasicHeader> headers = Arrays.asList(
                new BasicHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED),
                new BasicHeader("Accept", MediaType.APPLICATION_JSON),
                new BasicHeader("User-Agent", "Access domain app"),
                new BasicHeader("Connection",  "keep-alive")
        );

        httpClient = HttpClients.custom().setDefaultHeaders(headers).build();

        responseHandler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (200 <= statusCode && statusCode < 300) {
                    return "Call api vip talk successful";
                } else {
                    return "Call api vip talk failed, status code = {}" + statusCode;
                }
            }
        };
    }

    public void send(String message) throws IOException {
        List<BasicNameValuePair> params = Arrays.asList(
                new BasicNameValuePair("users", recipients),
                new BasicNameValuePair("text", message)
        );
        HttpEntity entity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
        HttpPost postRequest = new HttpPost(botUrl);
        postRequest.setEntity(entity);
        String result = httpClient.execute(postRequest, responseHandler);
        System.out.println(result);
    }
}
