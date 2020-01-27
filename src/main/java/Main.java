import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    private static String PROXY_HOST = "127.0.0.1" /* proxy host */;
    private static Integer PROXY_PORT = 9150 /* proxy port */;

    public static void main(String[] args) throws IOException, URISyntaxException {

        try {
            ApiContextInitializer.init();
            TelegramBotsApi botsApi = new TelegramBotsApi();
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

            botOptions.setProxyHost(PROXY_HOST);
            botOptions.setProxyPort(PROXY_PORT);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

            TelegramBot bot = new TelegramBot(botOptions);
            botsApi.registerBot(bot);


//            EPN epn = new EPN();
//            for (int i = 1; i < 2; i++) {
//                Vk vk = new Vk("s_stylist", i, 1);
//                String picLink = vk.getPicLink();
//                String longLinkForTG = epn.getLongLinkForTG(vk.getLinkForEpn());
//                String cutLinkForTG = epn.getCutLinkForTG(longLinkForTG);
//                bot.sendPostToTG(cutLinkForTG, picLink);
//            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
