package at.ac.tuwien.inso.sepm.ticketline.server.service.payment;

import at.ac.tuwien.inso.sepm.ticketline.server.configuration.properties.PaymentConfigurationProperties;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.PaymentException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class StripeProvider implements PaymentProvider {

    // Testing
    // https://stripe.com/docs/testing

    private final PaymentConfigurationProperties paymentConfigurationProperties;

    public StripeProvider(PaymentConfigurationProperties paymentConfigurationProperties) {
        Stripe.apiKey = paymentConfigurationProperties.getStripeSecretKey();
        this.paymentConfigurationProperties = paymentConfigurationProperties;
    }

    @Override
    public String pay(BigDecimal amount, String source) {
        try {
            int centValue = amount.movePointRight(2).intValueExact();

            Map<String, Object> chargeMap = new HashMap<String, Object>();
            chargeMap.put("amount", centValue);
            chargeMap.put("currency", "eur");
            chargeMap.put("source", source);

            Charge charge = Charge.create(chargeMap);

            return charge.getId();
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException();
        }

    }

    @Override
    public void refund(String identifier) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("charge", identifier);
            Refund refund = Refund.create(params);
        } catch (StripeException e) {
            throw new PaymentException();
        }
    }
}
