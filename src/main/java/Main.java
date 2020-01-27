import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;

public class Main {

    private static String PROXY_HOST = "127.0.0.1" /* proxy host */;
    private static Integer PROXY_PORT = 9150 /* proxy port */;

    private static final String PORT = System.getenv("PORT");


    public static void main(String[] args) throws IOException, URISyntaxException {

//        try {
//            ApiContextInitializer.init();
//            TelegramBotsApi botsApi = new TelegramBotsApi();
//            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
//
//            botOptions.setProxyHost(PROXY_HOST);
//            botOptions.setProxyPort(PROXY_PORT);
//            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
//
//            TelegramBot bot = new TelegramBot(botOptions);
//            botsApi.registerBot(bot);
//
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new TelegramBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        try (ServerSocket serverSocket = new ServerSocket(Integer.valueOf(PORT))) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
