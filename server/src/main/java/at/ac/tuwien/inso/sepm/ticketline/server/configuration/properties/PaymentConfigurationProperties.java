package at.ac.tuwien.inso.sepm.ticketline.server.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
public class PaymentConfigurationProperties {

    @Value("${payment.stripe-secret-key}")
    private String StripeSecretKey;

    @Value("${payment.stripe-public-key}")
    private String StripePublicKey;

}
