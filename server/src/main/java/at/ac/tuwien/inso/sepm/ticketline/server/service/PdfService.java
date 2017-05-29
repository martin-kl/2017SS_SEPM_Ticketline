package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import com.lowagie.text.DocumentException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

public interface PdfService {

    /**
     * downloads a pdf file of a transaction
     *
     * @param outputStream write object, where pdf will be written
     * @param ticketTransaction ticket transaction object
     */
    void download(OutputStream outputStream, TicketTransaction ticketTransaction, String language) throws IOException, DocumentException, URISyntaxException;

}
