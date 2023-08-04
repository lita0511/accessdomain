package notification_bot;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class notifyBot extends TelegramLongPollingBot {

    private static final String CHAT_ID = "-1001712253184";

	
	
	@Override
    public void onUpdateReceived(Update update) {

    }

    /**
     * Method for creating a message and sending it.
     * @param chatId chat id
     * @param s The String that you want to send as a message.
     */
    public void sendMsg(String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(CHAT_ID);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

	
	@Override
	public String getBotUsername() {
        return "access_domain_bot";
     }

	@Override
     public String getBotToken() {
        return "6211550327:AAExJaqVsl_3kA-gpvpG3q7dVgVLzfxjB90";
     }




	

	

}
