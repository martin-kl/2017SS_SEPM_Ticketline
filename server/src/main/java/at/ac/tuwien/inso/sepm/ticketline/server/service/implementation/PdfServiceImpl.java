package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PdfService;
import com.lowagie.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private TemplateEngine templateEngine;

    public void download(OutputStream outputStream, TicketTransaction ticketTransaction, String language) throws IOException, DocumentException, URISyntaxException {
        if (language == null) {
            language = "de";
        }
        language = language.toLowerCase();
        if (ticketTransaction.getStatus() == TicketStatus.STORNO) {
            Context context = new Context();
            context.setVariable("customer", ticketTransaction.getCustomer());
            context.setVariable("transaction", ticketTransaction);
            context.setVariable("tickets", ticketTransaction
                .getTicketHistories()
                .stream()
                .map(th -> th.getTicket())
                .collect(Collectors.toList())
            );
            toPdf(outputStream, templateEngine.process("storno-pdf-" + language, context));
            outputStream.close();
        } else if (ticketTransaction.getStatus() == TicketStatus.BOUGHT) {
            Context context = new Context();
            context.setVariable("customer", ticketTransaction.getCustomer());
            context.setVariable("transaction", ticketTransaction);
            context.setVariable("tickets", ticketTransaction
                .getTicketHistories()
                .stream()
                .map(th -> th.getTicket())
                .collect(Collectors.toList())
            );
            toPdf(outputStream, templateEngine.process("bill-pdf-" + language, context));
            outputStream.close();
        } else {
            throw new BadRequestException();
        }
    }

    /**
     * Generate a PDF document
     * @param outputStream stream to save the pdf file
     * @param html HTML as a string
     */
    private void toPdf(OutputStream outputStream, String html) throws DocumentException, IOException {
        final ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
    }

}
