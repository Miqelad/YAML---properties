/**

        <dependency>
            <groupId>com.vk.api</groupId>
            <artifactId>sdk</artifactId>
            <version>1.0.7</version>
        </dependency>
 * @author miq 13.07.2022
 */
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Sergei Fedorov (serezhka@xakep.ru)
 */
@Configuration
@PropertySource("classpath:vkdump.properties")
public class VkApiConfig {

    @Value("${proxy.host:#{null}}")
    private String proxyHost;

    @Value("${proxy.port:#{null}}")
    private Integer proxyPort;

    @Value("${vk.api.id}")
    private int id;

    @Value("${vk.api.token}")
    private String token;

    @Value("${vk.api.cooldown:1000}")
    private int cooldown;

    @Bean
    public UserActor tokenOwner() {
        return new UserActor(id, token);
    }

    @Bean
    public VkApiClient vkApiClient(HttpClient httpClient) throws NoSuchFieldException, IllegalAccessException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        TransportClient transportClient = HttpTransportClient.getInstance();
        Field httpClientField = HttpTransportClient.class.getDeclaredField("httpClient");
        httpClientField.setAccessible(true);
        httpClientField.set(null, httpClient);

        return new VkApiClient(transportClient);
    }

    @Bean
    public HttpClient httpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContextBuilder contextBuilder = new SSLContextBuilder();
        contextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(contextBuilder.build());

        HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setSSLSocketFactory(socketFactory);
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        clientBuilder.setDefaultRequestConfig(requestConfig);

        if (proxyHost != null && proxyPort != null) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            clientBuilder.setRoutePlanner(new DefaultProxyRoutePlanner(proxy));
        }

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(300);
        connectionManager.setDefaultMaxPerRoute(300);
        clientBuilder.setConnectionManager(connectionManager);

        BasicCookieStore cookieStore = new BasicCookieStore();
        clientBuilder.setDefaultCookieStore(cookieStore);
        clientBuilder.setUserAgent("Java VK SDK/0.4.3");

        clientBuilder.addInterceptorFirst(new HttpRequestInterceptor() {

            private long lastApiCallTime;

            @Override
            public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                long cooldown = System.currentTimeMillis() - lastApiCallTime;
                if (cooldown < VkApiConfig.this.cooldown) {
                    try {
                        Thread.sleep(VkApiConfig.this.cooldown - cooldown);
                    } catch (InterruptedException ignored) {
                    }
                }
                lastApiCallTime = System.currentTimeMillis();
            }
        });

        return clientBuilder.build();
    }
}
/*Метод для отправки сообщений*/



import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.asynchttpclient.util.ProxyUtils.PROXY_HOST;

/**
 * @author miq 13.07.2022
 */
@Component
@Slf4j
public class MyBot {
    @Autowired
    private VkApiClient vk;
    @Autowired
    private UserActor actor;

    public void run() throws ClientException, ApiException, InterruptedException {


        Integer ts = vk.messages().getLongPollServer(actor).execute().getTs();
        Random random = new Random();
        while (true){
            MessagesGetLongPollHistoryQuery historyQuery =  vk.messages().getLongPollHistory(actor).ts(ts);
            List<Message> messages = historyQuery.execute().getMessages().getItems();
            if (!messages.isEmpty()){
                messages.forEach(message -> {
                    System.out.println(message.toString());
                    try {
                        if (message.getText().equals("Привет")){
                            vk.messages().send(actor).message("Привет!").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }
                        else if (message.getText().equals("Кто я?")) {
                            vk.messages().send(actor).message("Ты хороший человек.").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }
                        else {
                            vk.messages().send(actor).message("Я тебя не понял.").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }
                    }
                    catch (ApiException | ClientException e) {e.printStackTrace();}
                });
            }
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
            Thread.sleep(500);
        }



    }
}
/*Для настройки proxy, vkdump.properties*/
proxy.host= 
proxy.port= 
vk.api.id= 
vk.api.token= 
