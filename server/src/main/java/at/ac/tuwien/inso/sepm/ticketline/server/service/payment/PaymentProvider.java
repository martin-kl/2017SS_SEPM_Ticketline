package at.ac.tuwien.inso.sepm.ticketline.server.service.payment;

import java.math.BigDecimal;

public interface PaymentProvider {

    /**
     * pay the provider
     *
     * @param amount amount to pay
     * @param source key containing all informations about the payment (created in the client)
     */
    String pay(BigDecimal amount, String source);

    /**
     * refunds the payment
     *
     * @param source key containing all informations about the refunding (created in the client)
     */
    void refund(String source);

}
