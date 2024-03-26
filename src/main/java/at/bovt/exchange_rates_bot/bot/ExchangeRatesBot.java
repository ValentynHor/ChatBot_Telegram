package at.bovt.exchange_rates_bot.bot;

import at.bovt.exchange_rates_bot.exception.ServiceException;
import at.bovt.exchange_rates_bot.service.ExchangeRatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;


@Component
public class ExchangeRatesBot extends TelegramLongPollingBot {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesBot.class);

    private static final String START = "/start";
    private static final String USD = "/usd";
    private static final String EUR = "/eur";
    private static final String HELP = "/help";

    private final ExchangeRatesService exchangeRatesService;

    public ExchangeRatesBot(@Value("${bot.token}") String botToken, ExchangeRatesService exchangeRatesService) {
        super(botToken);
        this.exchangeRatesService = exchangeRatesService;
    }

    @Override
    public String getBotUsername() {
        return "Exchange rates bot";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(!update.hasMessage() || !update.getMessage().hasText()){
            return;
        }

        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();

        switch (message){
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId,userName);
            }
            case USD -> usdCommand(chatId);
            case EUR -> eurCommand(chatId);
            case HELP -> helpCommand(chatId);
            default -> unknownCommand(chatId);
        }
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Error schicken Nachricht", e);
        }
    }

    private void startCommand(Long chatId, String userName) {
        var text =
            """
            Willkommen beim Bot, %s!
            
            Hier können Sie die offiziellen Wechselkurse für heute abrufen, 
            die von der Zentralbank der Ukraine festgelegt wurden.
            
            Um dies zu tun, verwenden Sie die Befehle:
            /usd - Dollar Kurs
            /eur - Euro Kurs
            
            Zusätzliche Befehle:
            /help - Erhalten von Hilfe
            """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    private void unknownCommand(Long chatId) {
        var text = "Unerkannte Command";
        sendMessage(chatId, text);
    }

    private void helpCommand(Long chatId) {
        var text = """
            Help:
            
            Es gibt folgende Befehle:
            
            /usd - Dollar Kurs
            /eur - Euro Kurs
            """;
        sendMessage(chatId, text);
    }

    private void usdCommand(Long chatId) {
        String formattedText;
        try {
            var usd = exchangeRatesService.getUSDExchangeRate();
            var text = "USD von %s beträgt %s UAH";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (ServiceException e) {
            LOG.error("Error getting USD", e);
            formattedText = "Error getting USD. Versuchen Sie später.";
        }
        sendMessage(chatId, formattedText);
    }

    private void eurCommand(Long chatId) {
        String formattedText;
        try {
            var eur = exchangeRatesService.getEURExchangeRate();
            var text = "EUR von %s beträgt %s UAH";
            formattedText = String.format(text, LocalDate.now(), eur);
        } catch (ServiceException e) {
            LOG.error("Error getting EUR", e);
            formattedText = "Error getting EUR. Versuchen Sie später.";
        }
        sendMessage(chatId, formattedText);
    }


}
