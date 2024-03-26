package at.bovt.exchange_rates_bot.exception;

public class ServiceException extends Exception{

    public ServiceException(String message, Throwable cause ){
        super(message, cause);
    }

    public ServiceException(String message) {
        super(message);
    }
}
