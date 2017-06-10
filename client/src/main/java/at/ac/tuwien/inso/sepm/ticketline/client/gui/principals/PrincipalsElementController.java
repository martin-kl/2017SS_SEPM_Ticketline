package at.ac.tuwien.inso.sepm.ticketline.client.gui.principals;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PrincipalRole;
import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PrincipalsElementController {
    @FXML
    private Label username;
    @FXML
    private Label email;
    @FXML
    private Label type;

    @FXML
    private Label locked;

    public void initializeData(PrincipalDTO principalDTO) {
        username.setText(principalDTO.getUsername());
        email.setText(principalDTO.getEmail());
        type.setText(BundleManager.getBundle().getString(PrincipalRole.ADMIN == principalDTO.getPrincipalRole() ? "principal.admin" : "principal.seller"));
        String s = BundleManager.getBundle().getString(principalDTO.getLocked() ? "principal.locked" : "principal.unlocked");
        locked.setText(s);
    }
}

