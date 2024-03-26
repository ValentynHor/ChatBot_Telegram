package at.bovt.exchange_rates_bot.service;

import at.bovt.exchange_rates_bot.exception.ServiceException;

public interface ExchangeRatesService {

    String getUSDExchangeRate() throws ServiceException;

    String getEURExchangeRate() throws ServiceException;
    void clearUSDCache();

    void clearEURCache();
}
