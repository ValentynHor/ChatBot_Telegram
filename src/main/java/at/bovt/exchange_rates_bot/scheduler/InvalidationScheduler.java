package at.bovt.exchange_rates_bot.scheduler;

import at.bovt.exchange_rates_bot.service.ExchangeRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InvalidationScheduler {

    private final ExchangeRatesService service;

    public InvalidationScheduler(ExchangeRatesService service) {
        this.service = service;
    }

    @Scheduled(cron = "* 0 0 * * ?")
    public void invalidateCache() {
        service.clearUSDCache();
        service.clearEURCache();
    }
}
