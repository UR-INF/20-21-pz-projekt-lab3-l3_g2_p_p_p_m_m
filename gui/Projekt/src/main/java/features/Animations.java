package main.java.features;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


public class Animations {

    /* Function that moves two AnchorPanes by value on x-axis (if value is negative then it moves to the left, otherwise to the right)
       Function takes four arguments: two AnchorPanes; first pane is actual pane that user can see, the second one is pane that user is going to see after animation is completed,
       the next argument is value of how much pixels will panes move before their visibility is set to false, next argument is duration of animation (in seconds). */
    public static void changePane(AnchorPane actualPane, AnchorPane nextPane, double value, double duration){

        actualPane.setDisable(true);    //Panes are disabled for time when animation is played (it stops user for clicking another button and starts animation over again
        nextPane.setDisable(true);      // which leads to unintended problems

        FadeTransition fadeActual = new FadeTransition(Duration.seconds(duration), actualPane);
        fadeActual.setFromValue(1);
        fadeActual.setToValue(0);
        fadeActual.play();

        moveByX(actualPane, value, duration);

        fadeActual.setOnFinished(event -> {
            actualPane.setVisible(false);
        });

        nextPane.setVisible(true);

        FadeTransition fadeNext = new FadeTransition(Duration.seconds(duration), nextPane);
        fadeNext.setFromValue(0);
        fadeNext.setToValue(1);
        fadeNext.play();

        moveByX(nextPane, value, duration);

        fadeNext.setOnFinished(event -> {
            actualPane.setDisable(false);
            nextPane.setDisable(false);
        });
    }

    // ###### Testing y-axis animation but it is not working as intended yet ######
    /* Function that moves two AnchorPanes by value on y-axis (if value is negative then it moves up , otherwise down)
       Function takes six arguments: two AnchorPanes; first pane is actual pane that user can see, the second one is pane that user is going to see after animation is completed,
       the next argument is value of how much pixels will panes move before their visibility is set to false, next argument is duration of animation (in seconds). The last two
       arguments are ToggleButtons that are being disabled to not let te user click them multiple times*/
    public static void changePane(AnchorPane actualPane, AnchorPane nextPane, double value, double duration, ToggleButton actualToggle, ToggleButton nextToggle){

        actualToggle.setDisable(true);
        nextToggle.setDisable(true);
        actualToggle.setOpacity(1);
        nextToggle.setOpacity(1);

        FadeTransition fadeActual = new FadeTransition(Duration.seconds(duration), actualPane);
        fadeActual.setFromValue(1);
        fadeActual.setToValue(0);
        fadeActual.play();

        moveByY(actualPane, value, duration);

        fadeActual.setOnFinished(event -> {
            actualPane.setVisible(false);
        });

        nextPane.setVisible(true);

        FadeTransition fadeNext = new FadeTransition(Duration.seconds(duration), nextPane);
        fadeNext.setFromValue(0);
        fadeNext.setToValue(1);
        fadeNext.play();

        moveByY(nextPane, value, duration);

        fadeNext.setOnFinished(event -> {
            nextToggle.setDisable(false);
        });
    }

    /* Function that moves an item by value on x-axis (if value is negative then it moves to the left, otherwise to the right)
       Function takes node (e.g AnchorPane, Circle), value (number of pixels) and duration (seconds - how long animation is going to last) arguments */
    public static void moveByX(Node item, double value, double duration){
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), item);
        translateTransition.setByX(value);
        translateTransition.play();
    }

    /* Function that moves an item by value on y-axis (if value is negative then it moves up , otherwise down)
       Function takes node (e.g AnchorPane, Circle), value (number of pixels) and duration (seconds - how long animation is going to last) arguments */
    public static void moveByY(Node item, double value, double duration){
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), item);
        translateTransition.setByY(value);
        translateTransition.play();
    }

    /* Method moves an Node by given value, durationOfMove is a value in seconds that animations plays. After animation is finished there is pause (durationOfPause)
       and after pause animation is played once again but with opposite Y-axis value. (........)*/
    public static void alertAnim(Pane item, double value, double durationOfMove, double durationOfPause, Node causeOfAnim, AnchorPane parent){
        causeOfAnim.setDisable(true);
        causeOfAnim.setOpacity(1);
        TranslateTransition translateStart = new TranslateTransition(Duration.seconds(durationOfMove), item);
        translateStart.setByY(value);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(durationOfPause));

        TranslateTransition translateBack = new TranslateTransition(Duration.seconds(durationOfMove),item);
        translateBack.setByY(-value);

        SequentialTransition sequentialTransition = new SequentialTransition(translateStart,pauseTransition,translateBack);
        sequentialTransition.play();

        sequentialTransition.setOnFinished(event -> {
            parent.getChildren().remove(item);
            causeOfAnim.setDisable(false);
        });
    }

    /* Method that fade away an item
       Method takes two arguments - node (e.g button), and duration of fading in seconds*/
    public static void fadeAway(Node item, double duration){
        item.setDisable(true);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration),item);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        fadeTransition.setOnFinished(event -> {
            item.setVisible(false);
            item.setDisable(false);
        });
    }


}