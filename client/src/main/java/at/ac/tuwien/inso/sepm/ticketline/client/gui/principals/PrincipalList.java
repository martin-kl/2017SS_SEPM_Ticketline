package at.ac.tuwien.inso.sepm.ticketline.client.gui.principals;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PrincipalService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.*;
import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class PrincipalList {
    @FXML
    private VBox list;

    @FXML
    private ScrollPane scrollPane;


    private final SpringFxmlLoader springFxmlLoader;
    private final PrincipalService principalService;
    private final MainController mainController;

    private String search = "";
    private Boolean locked = null;
    private int loadedUntilPage = -1;
    private boolean currentlyLoading = false;



    public PrincipalList(MainController mainController, SpringFxmlLoader springFxmlLoader,
                        PrincipalService principalService) {
        this.springFxmlLoader = springFxmlLoader;
        this.principalService = principalService;
        this.mainController = mainController;
    }

    public void reload(String search, Boolean locked) {
        this.search = search;
        this.locked = locked;
        prepareForNewList();
        loadNext();
        scrollPane.vvalueProperty().addListener((ov, old_val, new_val) -> {
            if (list.getChildren().size() == 0) {
                return;
            }
            if (currentlyLoading) {
                return;
            }
            if (new_val.floatValue() > 0.9) {
                currentlyLoading = true;
                loadNext();
            }
        });
    }

    private boolean deleteEverythingBeforeNextRedraw = false;
    private void prepareForNewList() {
        loadedUntilPage = -1;
        deleteEverythingBeforeNextRedraw = true;
    }


    private void loadNext() {
        Task<List<PrincipalDTO>> task = new Task<List<PrincipalDTO>>() {
            @Override
            protected List<PrincipalDTO> call() throws DataAccessException {
                try {
                    return principalService.search(search, locked, ++loadedUntilPage);
                } catch (ExceptionWithDialog exceptionWithDialog) {
                    exceptionWithDialog.showDialog();
                    return new ArrayList<>();
                }
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if(deleteEverythingBeforeNextRedraw){
                    list.getChildren().clear();
                    deleteEverythingBeforeNextRedraw = false;
                }
                appendElements(getValue());
                currentlyLoading = false;
            }

            @Override
            protected void failed() {
                super.failed();
                currentlyLoading = false;
                JavaFXUtils.createExceptionDialog(getException(),
                    list.getScene().getWindow()).showAndWait();
            }

        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    private TwoArgCallable willDraw = (a1, a2) -> {};
    private TwoArgCallable clicked = (a1, a2) -> {};

    public void setWillDraw(TwoArgCallable willDraw) {
        this.willDraw = willDraw;
    }

    public void setClicked(TwoArgCallable clicked) {
        this.clicked = clicked;
    }

    private void appendElements(List<PrincipalDTO> elements) {
        Iterator<PrincipalDTO> iterator = elements.iterator();
        while (iterator.hasNext()) {
            PrincipalDTO principal = iterator.next();
            SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
                .loadAndWrap("/fxml/principal/principalsElement.fxml");

            ((PrincipalsElementController) wrapper.getController()).initializeData(principal);
            HBox principalBox = (HBox) wrapper.getLoadedObject();

            willDraw.call(principal, principalBox);


            principalBox.setOnMouseClicked((e) -> {
                clicked.call(principal, principalBox);

            });
            list.getChildren().add(principalBox);
            if (iterator.hasNext()) {
                Separator separator = new Separator();
                list.getChildren().add(separator);
            }
        }
    }

}
