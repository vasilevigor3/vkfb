import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class TelegramBot extends TelegramLongPollingBot {
    private final String redirectVK = "https://oauth.vk.com/authorize?client_id=7153000&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=friends&response_type=token&v=5.101&state=123456";
    private final String redirectEPN = "https://app.epn.bz/auth/social/google?client_id=Uym179IeibNjJVHshF0gPMAd3kZYfLWv&role=user&redirect_after_auth_url=https%3A%2F%2Fepn.bz%2Fapp-auth";

    private final String itemLinkText = "itemLink: ";
    private final String picLinkText = "picLink: ";
    private final String autoSendText = "Enter the group name: ";


    private final String botUsername = "tets88_bot";
    private final String botToken = "1036923097:AAEK_tX1pC6vvbMOm9wsC-6QWo3v9oHFA7c";
    private final String chatId = "-1001304932946";

    private String itemLink = "";
    private String picLink = "";

    private String group = "";
    private int from;
    private int to;

    private String vkToken = "";
    private String epnToken = "";

    private int status;

    List<String> picList = new ArrayList<>();
    List<String> linkList = new ArrayList<>();

    Vk vk = new Vk();
    EPN epn = new EPN();

    public TelegramBot(DefaultBotOptions options) {
        super(options);
    }

    public void sendPostToTG(String cutLink, String photoLink) {
        try {
            execute(new SendPhoto().setChatId(chatId).setPhoto(photoLink).setCaption(cutLink));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void autoPosting(String groupName, int from, int to) throws IOException, URISyntaxException {
        for (int i = from; i < to; i++) {
            Vk vk = new Vk(groupName, i, 1);
            vk.setToken(vkToken);
            epn.setAccess_token(epnToken);
            String picLink = vk.getPicLink();
            String longLinkForTG = epn.getLongLinkForTG(vk.getLinkForEpn());
            String cutLinkForTG = epn.getCutLinkForTG(longLinkForTG);
            System.out.println("test");
            sendPostToTG(cutLinkForTG, picLink);
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void sendText(Update update, String text) {
        try {
            execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText(text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public SendMessage sendKeyboardItalia(Update update) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboard1Row = new KeyboardRow();
        KeyboardRow keyboard2Row = new KeyboardRow();
        KeyboardRow keyboard3Row = new KeyboardRow();
        KeyboardRow keyboard4Row = new KeyboardRow();
        KeyboardRow keyboard5Row = new KeyboardRow();

        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        keyboard.clear();

        keyboard1Row.add("alimpp");
        keyboard1Row.add("beardaliexpress");

        keyboard2Row.add("bandos_ali");
        keyboard2Row.add("batya_ali");

        keyboard3Row.add("w2b_it");
        keyboard3Row.add("top_do_5");

        keyboard4Row.add("s_stylist");
        keyboard4Row.add("godnoten");

        keyboard5Row.add("moykitay");
        keyboard5Row.add("doehalo");

        keyboard.add(keyboard1Row);
        keyboard.add(keyboard2Row);
        keyboard.add(keyboard3Row);
        keyboard.add(keyboard4Row);
        keyboard.add(keyboard5Row);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return new SendMessage().setChatId(update.getMessage().getChatId())
                .setText("Choose the group: ")
                .setReplyMarkup(replyKeyboardMarkup);
    }

    @Override
    public void onUpdateReceived(Update update) {


        if (update.getMessage().getText().equals("/clear")) {
            setStatus(0);
        } else if (update.getMessage().getText().equals("/itemlink")) {
            sendText(update, itemLinkText);
            setStatus(1);
        } else if (update.getMessage().getText().equals("/piclink")) {
            sendText(update, picLinkText);
            setStatus(2);
        } else if (update.getMessage().getText().equals("/autosend")) {
            sendText(update, autoSendText);
            setStatus(3);
        } else if (update.getMessage().getText().equals("/send")) {
            for (int i = 0; i < linkList.size(); i++) {
                sendPostToTG(linkList.get(i), picList.get(i));
                picList.clear();
                linkList.clear();
            }
        } else if (update.getMessage().getText().equals("/grouplist")) {
            try {
                execute(sendKeyboardItalia(update));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.getMessage().getText().equals("/vk")) {
            sendText(update,redirectVK);
            setStatus(6);
        } else if (update.getMessage().getText().equals("/epn")) {
            sendText(update,redirectEPN);
            setStatus(7);
        } else {
            if (status == 1) {
                itemLink = update.getMessage().getText();
                try {
                    vk.setToken(vkToken);
                    epn.setAccess_token(epnToken);
                    String longLinkForTG = epn.getLongLinkForTG(vk.getLinkForEpn(itemLink));
                    String cutLinkForTG = epn.getCutLinkForTG(longLinkForTG);
                    linkList.add(cutLinkForTG);
                    sendText(update, itemLinkText + vk.getLinkForEpn(itemLink));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else if (status == 2) {
                picLink = update.getMessage().getText();
                picList.add(picLink);
                sendText(update, picLinkText + picLink);
            } else if (status == 3) {
//                String group = "";
                group = update.getMessage().getText();
                System.out.println(group);
                setStatus(4);
                sendText(update, "Enter the \"from\": ");
            } else if (status == 4) {
//                int from;
                from = Integer.parseInt(update.getMessage().getText());
                System.out.println(from);
                setStatus(5);
                sendText(update, "Enter the \"to\": ");

            } else if (status == 5) {
//                int to;
                to = Integer.parseInt(update.getMessage().getText());
                try {
                    autoPosting(group, from, to);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                setStatus(0);
            }else if (status == 6) {
                vkToken = update.getMessage().getText();
                sendText(update,"VKtoken: " + vkToken);
                setStatus(0);
            }else if (status == 7) {
                epnToken = update.getMessage().getText();
                sendText(update,"EPNtoken: " + epnToken);
                setStatus(0);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}


