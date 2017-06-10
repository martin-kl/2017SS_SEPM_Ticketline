package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PaymentProviderOption;

public interface PaymentService {

    /**
     * Method to pay the transaction
     *
     * @param paymentProviderOption
     * @param ticketTransactionId
     * @param paymentIdentifier
     */
    void pay(PaymentProviderOption paymentProviderOption, Long ticketTransactionId, String paymentIdentifier);

    /**
     * refund the
     *
     * @param ticketTransactionId ticketId of the old transaction
     */
    void refund(Long ticketTransactionId);

}
