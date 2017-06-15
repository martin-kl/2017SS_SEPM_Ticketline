package at.ac.tuwien.inso.sepm.ticketline.client.gui.principals;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.Debouncer;
import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PrincipalsController {
    private static final int HEADER_ICON_SIZE = 25;
    @FXML
    private Label lblHeaderIcon;
    @FXML
    private Label lblHeaderTitle;
    private FontAwesome fontAwesome;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;

    private PrincipalList principalList;

    @FXML
    private Button btnAdd;

    @FXML
    private VBox main;

    @FXML
    private VBox customerSelectionParent;
    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> status;

    public PrincipalsController(MainController mainController, SpringFxmlLoader springFxmlLoader) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    @FXML
    public void initialize() {
        setDropDownText();
        status.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> onLockedChange());
    }

    private void setDropDownText() {
        status.setItems(FXCollections.observableArrayList(
            BundleManager.getBundle().getString("any"),
            BundleManager.getBundle().getString("principal.locked"),
            BundleManager.getBundle().getString("principal.unlocked")
        ));
        status.setValue(BundleManager.getBundle().getString("any"));
    }

    private Boolean getLocked() {
        if (status.getSelectionModel().getSelectedIndex() == 0) { return null; }
        if (status.getSelectionModel().getSelectedIndex() == 1) { return true; }
        return false;
    }

    public void setFont(FontAwesome fontAwesome){
        this.fontAwesome = fontAwesome;
        setIcon(FontAwesome.Glyph.USERS);
        setTitle(BundleManager.getBundle().getString("accounts.title"));
    }
    private void setIcon(FontAwesome.Glyph glyph) {
        lblHeaderIcon.setGraphic(
            fontAwesome
                .create(glyph)
                .size(HEADER_ICON_SIZE));
    }
    private void setTitle(String title) {
        lblHeaderTitle.setText(title);
    }

    public void reloadLanguage(boolean alreadyLoggedIn) {
        setTitle(BundleManager.getBundle().getString("accounts.title"));
        searchField.setPromptText(BundleManager.getBundle().getString("search"));
        btnAdd.setText(BundleManager.getBundle().getString("customer.add"));
        setDropDownText();
        debouncer.call(1);

    }


    public void init() {
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/principal/principalList.fxml");

        principalList = ((PrincipalList) wrapper.getController());
        ScrollPane list = (ScrollPane) wrapper.getLoadedObject();
        customerSelectionParent.getChildren().clear();
        customerSelectionParent.getChildren().add(list);
        principalList.setClicked((principal, box) ->{
            mainController.addEditPrincipalWindow((PrincipalDTO) principal);
        });
        principalList.reload(searchField.getText().trim(), getLocked());
    }


    Debouncer<Integer> debouncer = new Debouncer<>(o -> principalList.reload(searchField.getText(), getLocked()), 250);
    public void onSearchChange(KeyEvent keyEvent) {
        debouncer.call(1);
    }

    public void onLockedChange() {
        debouncer.call(1);
    }



    public void handleAdd(ActionEvent actionEvent) {
        mainController.addEditPrincipalWindow(null);
    }
}


