package at.bovt.exchange_rates_bot.service.impl;

import at.bovt.exchange_rates_bot.client.Client;
import at.bovt.exchange_rates_bot.exception.ServiceException;
import at.bovt.exchange_rates_bot.service.ExchangeRatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesServiceImpl.class);
    private static final String USD_XPATH = "/ValCurs/Valute[CharCode='USD']/Value";
    private static final String EUR_XPATH = "/ValCurs/Valute[CharCode='EUR']/Value";
    private final Client client;

    public ExchangeRatesServiceImpl(Client client) {
        this.client = client;
    }

    @Cacheable(value = "usd", unless = "#result == null or #result.isEmpty()")
    @Override
    public String getUSDExchangeRate() throws ServiceException {
        var xmlOptional = client.getXML();
        String xml = xmlOptional.orElseThrow(
                () -> new ServiceException("Error bei getting XML")
        );
        return extractCurrencyValueFromXML(xml, USD_XPATH);
    }

    @Cacheable(value = "eur", unless = "#result == null or #result.isEmpty()")
    @Override
    public String getEURExchangeRate() throws ServiceException {
        var xmlOptional = client.getXML();
        String xml = xmlOptional.orElseThrow(
                () -> new ServiceException("Error bei getting XML")
        );
        return extractCurrencyValueFromXML(xml, EUR_XPATH);
    }

    @CacheEvict("usd")
    @Override
    public void clearUSDCache() {
        LOG.info("Cache \"usd\" cleared!");
    }

    @CacheEvict("eur")
    @Override
    public void clearEURCache() {
        LOG.info("Cache \"eur\" cleared!");
    }

    private static String extractCurrencyValueFromXML(String xml, String xpathExpression)
            throws ServiceException {
        var source = new InputSource(new StringReader(xml));
        LOG.info("XML Abfrage");
        try {
            var xpath = XPathFactory.newInstance().newXPath();
            var document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

            return xpath.evaluate(xpathExpression, document);
        } catch (XPathExpressionException e) {
            throw new ServiceException("Error. XML ist schlecht", e);
        }
    }
}
