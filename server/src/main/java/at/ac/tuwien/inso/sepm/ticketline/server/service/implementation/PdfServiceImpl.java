package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PdfService;
import com.lowagie.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
public class PdfServiceImpl implements PdfService {

    public void download(OutputStream outputStream, TicketTransaction ticketTransaction) throws IOException, DocumentException, URISyntaxException {
        if (ticketTransaction.getStatus() == TicketStatus.STORNO) {
            String content = new String(
                Files.readAllBytes(
                    Paths.get(getClass().getClassLoader().getResource("./templates/storno-pdf.html").toURI())
                )
            );
            toPdf(outputStream, content);
            outputStream.close();
        } else if (ticketTransaction.getStatus() == TicketStatus.BOUGHT) {
            String content = new String(
                Files.readAllBytes(
                    Paths.get(getClass().getClassLoader().getResource("./templates/storno-pdf.html").toURI())
                )
            );
            toPdf(outputStream, content);
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
