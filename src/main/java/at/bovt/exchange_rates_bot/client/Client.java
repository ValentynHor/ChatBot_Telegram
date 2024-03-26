package at.bovt.exchange_rates_bot.client;

import at.bovt.exchange_rates_bot.exception.ServiceException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class Client {

    private final OkHttpClient client;

    public Client(OkHttpClient client) {
        this.client = client;
    }

    @Value("${xml.url}")
    private String url;

    public Optional<String> getXML() throws ServiceException{
        var request = new Request.Builder()
                .url(url)
                .build();

        try {
            var response = client.newCall(request).execute();
            var body = response.body();
            return body == null ? Optional.empty() : Optional.of(body.string());
        } catch (IOException e) {
            throw new ServiceException("Error getting currency from Bank",e);
        }
    }
}
