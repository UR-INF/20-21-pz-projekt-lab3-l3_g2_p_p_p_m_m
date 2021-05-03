package main.java.controllers.courier;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.java.SceneManager;
import main.java.controllers.auth.Login;
import main.java.dao.UserInfosDAO;
import main.java.entity.UserInfos;
import main.java.features.Animations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Courier implements Initializable {

    boolean hamburgerClicked = false;
    @FXML
    private AnchorPane mainWindow;
    @FXML
    private VBox paneRight;
    @FXML
    private FontAwesomeIconView hamburger;
    @FXML
    private Pane welcomeMessage;
    @FXML
    private Pane alertPane;
    @FXML
    private AnchorPane window;

    @FXML
    private Text loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /* name and surname after login
        UserInfos ui = UserInfosDAO.getUserInfoByID(Login.getUserInfoID()).get(0);
        loggedUser.setText(ui.getName() + " " + ui.getSurname());
         */
        SceneManager.getStage().getScene().getRoot().setStyle("-fx-main-color: red;");

        paneRight.setTranslateX(-200);
        alertPane.setTranslateY(-500);

        hamburger.setOnMouseClicked(event -> {      // If hamburger button is clicked then menu slides in and transition last for 0.5s
            if (hamburgerClicked == false) {

                hamburger.setDisable(true);
                hamburgerClicked = true;
                paneRight.setVisible(true);

                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), paneRight);
                fadeTransition.setFromValue(0);
                fadeTransition.setToValue(1);
                fadeTransition.play();

                Animations.moveByX(paneRight, +200, 0.5);
                Animations.moveByX(welcomeMessage, +170, 0.5);
                Animations.moveByX(mainWindow, +70, 0.5);

                fadeTransition.setOnFinished(event1 -> {
                    hamburger.setDisable(false);
                });
            } else {
                hamburger.setDisable(true);
                hamburgerClicked = false;

                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), paneRight);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                fadeTransition.play();

                Animations.moveByX(paneRight, -200, 0.5);
                Animations.moveByX(welcomeMessage, -170, 0.5);
                Animations.moveByX(mainWindow, -70, 0.5);

                fadeTransition.setOnFinished(event1 -> {
                    paneRight.setVisible(false);
                    hamburger.setDisable(false);
                });
            }
        });
        try {
            SceneManager.loadScene("../../../resources/view/courier/courierHome.fxml", mainWindow);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openHome(ActionEvent actionEvent) throws IOException {
        SceneManager.loadScene("../../../resources/view/courier/courierHome.fxml", mainWindow);
    }

    public void openSecond(ActionEvent actionEvent) throws IOException {
        SceneManager.loadScene("../../../resources/view/courier/courierSecond.fxml", mainWindow);
    }


    public void openSettings(ActionEvent actionEvent) throws IOException {
        SceneManager.loadScene("../../../resources/view/courier/courierSettings.fxml", mainWindow);
    }

    @FXML
    void logout(ActionEvent event) {
        Animations.moveByY(alertPane, +500, 0.3);
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(8);
        window.setDisable(true);
        window.setEffect(gaussianBlur);
    }

    @FXML
    void logoutNo(ActionEvent event) {
        Animations.moveByY(alertPane, -500, 0.3);
        window.setEffect(null);
        window.setDisable(false);
    }

    @FXML
    void logoutYes(ActionEvent event) {
        SceneManager.renderScene("login");
    }
}
