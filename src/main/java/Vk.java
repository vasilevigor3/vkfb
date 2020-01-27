import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

//https://oauth.vk.com/authorize?client_id=7153000&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=friends&response_type=token&v=5.101&state=123456

public class Vk {

    private String rulePic = "/response/items/0/attachments/0/photo/sizes/6/url";
    private String ruleItem = "/response/items/0/text";

    public void setToken(String token) {
        this.token = token;
    }
    private String token = "";
//    private String token = "09b3f2d274b6e31d825deb518a6422f957b69195817b157000af04096795f1af88ccf0a36fb003fb45d36";
    String groupName;
    int offset;
    int count;

    public Vk(String groupName, int offset, int count) {
        this.groupName = groupName;
        this.offset = offset;
        this.count = count;
    }
    public Vk(){
    }

    public String constructor() {
        return "https://api.vk.com/method/wall.get?domain=" +
                groupName +
                "&offset=" +
                offset +
                "&count=" +
                count +
                "&access_token=" + token + "&v=5.101";
    }

    JsonUtility jsonUtility = new JsonUtility();

    public String getStringResponse() {
        String s = "null";
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(constructor()))) {
            HttpEntity entity = response.getEntity();
            s = IOUtils.toString(entity.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public String getPicLink() {
        return jsonUtility.readJson(getStringResponse()).at(rulePic).asText();
    }

    public String getItemLink() {
        String postText = jsonUtility.readJson(getStringResponse()).at(ruleItem).asText();
        int index = postText.indexOf("http");
        String s1 = postText.substring(index,index+21);
        System.out.println(s1);
        return s1;
    }

    public String getLinkForEpn() throws IOException, URISyntaxException {
        String s = "null";

        RequestConfig requestConfig = RequestConfig.custom()
                .setCircularRedirectsAllowed(true)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) " +
                        "AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")
                .build();

        HttpClientContext context = HttpClientContext.create();
        HttpGet httpGet = new HttpGet(getItemLink());
        System.out.println("Executing request " + httpGet.getRequestLine());
        System.out.println("----------------------------------------");

        httpClient.execute(httpGet, context);
        HttpHost target = context.getTargetHost();
        List<URI> redirectLocations = context.getRedirectLocations();
        URI location = URIUtils.resolve(httpGet.getURI(), target, redirectLocations);
        System.out.println("Final HTTP location: " + location.toASCIIString());

        s=location.toASCIIString();
        String linkForEpn = s.substring(0,s.indexOf("?")).replace("ru","com");
        System.out.println(linkForEpn);

        return linkForEpn;
    }
    public String getLinkForEpn(String link) throws IOException, URISyntaxException {
        String s = "null";
        RequestConfig requestConfig = RequestConfig.custom()
                .setCircularRedirectsAllowed(true)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) " +
                        "AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")
                .build();

        HttpClientContext context = HttpClientContext.create();
        HttpGet httpGet = new HttpGet(link);
        System.out.println("Executing request " + httpGet.getRequestLine());
        System.out.println("----------------------------------------");

        httpClient.execute(httpGet, context);
        HttpHost target = context.getTargetHost();
        List<URI> redirectLocations = context.getRedirectLocations();
        URI location = URIUtils.resolve(httpGet.getURI(), target, redirectLocations);
        System.out.println("Final HTTP location: " + location.toASCIIString());

        s=location.toASCIIString();
        String linkForEpn = s.substring(0,s.indexOf("?")).replace("ru","com");
        System.out.println(linkForEpn);

        return linkForEpn;
    }
}
