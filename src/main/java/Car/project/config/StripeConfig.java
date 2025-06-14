package Car.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfig {
    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @PostConstruct
    public void setup() {
        Stripe.apiKey = stripeSecretKey;
    }
}
