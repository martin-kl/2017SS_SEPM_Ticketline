package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PaymentProviderOption;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketHistory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.PaymentException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketTransactionRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PaymentService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.payment.PaymentProvider;
import at.ac.tuwien.inso.sepm.ticketline.server.service.payment.StripeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private TicketTransactionRepository ticketTransactionRepository;

    Map<PaymentProviderOption, PaymentProvider> paymentProviderMap;

    public PaymentServiceImpl(StripeProvider stripeProvider) {
        paymentProviderMap = new HashMap<>();
        // Register your payment providers here
        paymentProviderMap.put(PaymentProviderOption.STRIPE, stripeProvider);
    }

    @Override
    public void pay(PaymentProviderOption paymentProvider, Long ticketTransactionId, String paymentIdentifier) {
        // verify payment provider and ticket transaction
        TicketTransaction ticketTransaction = ticketTransactionRepository.getOne(ticketTransactionId);
        if (ticketTransaction == null) {
            throw new BadRequestException();
        }
        PaymentProvider provider = getProvider(paymentProvider);

        if (ticketTransaction.getStatus() != TicketStatus.BOUGHT) {
            throw new BadRequestException();
        }
        if (ticketTransaction.getPaymentIdentifier() != null) {
            throw new PaymentException();
        }

        // get the total price for all tickets
        Set<TicketHistory> ticketHistories = ticketTransaction.getTicketHistories();
        BigDecimal sum = ticketTransaction
            .getTicketHistories()
            .stream()
            .map(th -> th.getTicket().getPrice())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // pay it
        String identifier = provider.pay(sum, paymentIdentifier);
        log.info("Paid €" + sum + " for transaction " + ticketTransactionId);

        // set that the transaction is already paid
        ticketTransaction.setPaymentIdentifier(identifier);
        ticketTransaction.setPaymentProviderOption(paymentProvider);

        ticketTransactionRepository.save(ticketTransaction);
    }

    @Override
    public void refund(Long ticketTransactionId) {
        TicketTransaction ticketTransaction = ticketTransactionRepository.getOne(ticketTransactionId);
        if (ticketTransaction == null) {
            throw new BadRequestException();
        }
        if (ticketTransaction.getStatus() != TicketStatus.BOUGHT) {
            throw new BadRequestException();
        }
        // transaction was not paid yet, so no need to refund it
        if (ticketTransaction.getPaymentIdentifier() == null || ticketTransaction.isRefunded()) {
            return;
        }
        PaymentProvider provider = getProvider(ticketTransaction.getPaymentProviderOption());

        provider.refund(ticketTransaction.getPaymentIdentifier());

        ticketTransaction.setRefunded(true);

        ticketTransactionRepository.save(ticketTransaction);
    }

    private PaymentProvider getProvider(PaymentProviderOption paymentProviderOption) {
        PaymentProvider provider = paymentProviderMap.get(paymentProviderOption);
        if (provider == null) {
            throw new PaymentException("Invalid Provider");
        }
        return provider;
    }

}
