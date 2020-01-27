import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientUtility {

    private CloseableHttpClient httpClient;

    public HttpClientUtility () {
        httpClient = HttpClients.custom()
                .disableRedirectHandling()
                .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) " +
                        "AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")
                .build();
    }

    @SneakyThrows
    public String getStringResponse(String url){
        CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
        HttpEntity entity = response.getEntity();
        httpClient.close();
        return IOUtils.toString(entity.getContent());
    }

    @SneakyThrows
    public String getLinkForEpn (String url) {
        CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
        Header[] headers = response.getHeaders(HttpHeaders.LOCATION);
        String newUrl = headers[0].getValue();
//        String str [] = newUrl.split("2F");
//        String resultLink = "https://it.aliexpress.com/item/" + str[str.length-1];
        httpClient.close();
        return newUrl;
    }



}
