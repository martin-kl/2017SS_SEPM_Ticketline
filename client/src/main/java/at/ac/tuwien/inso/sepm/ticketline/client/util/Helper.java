package at.ac.tuwien.inso.sepm.ticketline.client.util;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Helper {

    private static DateTimeFormatter dateAndTimeFormatter = DateTimeFormatter.ofPattern(
        (BundleManager.getBundle().getLocale().getLanguage().equals("de")
            ? "dd.MM.yyyy - HH:mm:ss" : "MM/dd/yyyy - HH:mm:ss"));

    public static BigDecimal getTotalPrice(List<? extends TicketDTO> tickets) {
        BigDecimal result = new BigDecimal(0);
        for (TicketDTO ticket : tickets) {
            result = result.add(ticket.getPrice());
        }
        return result;
    }

    public static String getFormattedTicketPlace(TicketDTO ticketDTO) {
        if (ticketDTO instanceof SeatTicketDTO) {
            SeatTicketDTO seatTicket = (SeatTicketDTO) ticketDTO;
            return BundleManager.getBundle().getString("row") + " " +
                    seatTicket.getSeat().getRow() + ", " +
                    BundleManager.getBundle().getString("seat") + " " + seatTicket
                    .getSeat().getColumn();
        } else {
            SectorTicketDTO sectorTicket = (SectorTicketDTO) ticketDTO;
            return BundleManager.getBundle().getString("sector") + " " +
                sectorTicket.getSector().getName();
        }
    }

    public static Stage setDefaultOnCloseRequest(Stage dialog) {
        dialog.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(dialog);
            alert.setTitle(BundleManager.getBundle().getString("dialog.customer.title"));
            alert.setHeaderText(BundleManager.getBundle().getString("dialog.customer.header"));
            alert.setContentText(BundleManager.getBundle().getString("dialog.customer.content"));
            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent() || !ButtonType.OK.equals(result.get())) {
                event.consume();
            }
        });
        return dialog;
    }

    public static Image convertToJavaFXImage(byte[] raw, final int width, final int height) {
        WritableImage image = new WritableImage(width, height);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(raw);
            BufferedImage read = ImageIO.read(bis);
            image = SwingFXUtils.toFXImage(read, null);
        } catch (IOException ex) {
            log.error("error while converting blob to javafx.scene.image, message=" + ex
                .getLocalizedMessage());
        }
        return image;
    }

    public static DateTimeFormatter getDateAndTimeFormatter() {
        return dateAndTimeFormatter;
    }

    public static void reloadLanguage() {
        dateAndTimeFormatter = DateTimeFormatter.ofPattern(
            (BundleManager.getBundle().getLocale().getLanguage().equals("de")
                ? "dd.MM.yyyy, HH:mm 'Uhr'" : "MM/dd/yyyy, hh:mm a"));
    }
}
