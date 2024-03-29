package main.java.controllers.auth;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import main.java.App;
import main.java.SceneManager;
import main.java.dao.UsersDAO;
import main.java.entity.Users;
import main.java.features.Alerts;
import main.java.features.Preference;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static main.java.dao.UserInfosDAO.getUserInfoByID;
import static main.java.dao.UsersDAO.getUsers;

public class PasswordReset implements Initializable {

    private static final Preference pref = new Preference();
    int userId;
    int userInfoId;
    @FXML
    private AnchorPane loginLeftPaneAnchorPane;
    @FXML
    private Label passwordResetSloganLabel;
    @FXML
    private Label passwordResetWelcomeLabel;
    @FXML
    private Label passwordResetInfoLabel;
    @FXML
    private Button passwordResetReturnButtonButton;
    @FXML
    private AnchorPane loginRightPaneAnchorPane;
    @FXML
    private Label passwordResetResetPasswordLabel;
    @FXML
    private TextField passwordResetEmailField;
    @FXML
    private Circle passwordResetEmailCircle;
    @FXML
    private TextField passwordResetVerificationCodeField;
    @FXML
    private Circle passwordResetVerificationCodeCircle;
    @FXML
    private TextField passwordResetPasswordField;
    @FXML
    private Circle passwordResetPasswordCircle;
    @FXML
    private TextField passwordResetConfirmPasswordField;
    @FXML
    private Circle passwordResetConfirmPasswordCircle;
    @FXML
    private Label passwordResetSendCodeLabel;
    @FXML
    private Button passwordResetSendCodeButton;
    @FXML
    private Label passwordResetVerifyCodeLabel;
    @FXML
    private Button passwordResetVerifyCodeButton;
    @FXML
    private Button passwordResetSetNewPasswordButton;
    @FXML
    private Label passwordResetSixCharsRequirement;
    @FXML
    private Label passwordResetSmallLetterRequirement;
    @FXML
    private Label passwordResetBigLetterRequirement;
    @FXML
    private Label passwordResetNumberRequirement;
    @FXML
    private Label passwordResetSpecialCharRequirement;
    @FXML
    private Label passwordResetSamePasswordsRequirement;
    @FXML
    private ImageView passwordResetLogoImageView;
    private String verificationCode = "";

    /**
     * <p>
     * Method used to prepare message that is going to be send to user.
     * </p>
     *
     * @param session            session that is held after login
     * @param outBoxEmailAccount sender's email
     * @param recipient          receiver's email
     * @param firstName          receiver's first name
     * @param verificationCode   code sent to user
     * @return returns message or null
     */
    private static Message prepareMessage(Session session,
                                          String outBoxEmailAccount,
                                          String recipient,
                                          String firstName,
                                          String verificationCode) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("OutBox_Support"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(App.getLanguageProperties("authResetPasswordMailSubject"));
            message.setText(App.getLanguageProperties("authResetPasswordMailMessage1") + firstName + ",\n" +
                    App.getLanguageProperties("authResetPasswordMailMessage2") + "\n" +
                    App.getLanguageProperties("authResetPasswordMailMessage3") + "\n" +
                    "\n" + verificationCode + "\n\n" +
                    App.getLanguageProperties("authResetPasswordMailMessage4") + "\n" +
                    App.getLanguageProperties("authResetPasswordMailMessage5") + "\n" +
                    "\n" +
                    App.getLanguageProperties("authResetPasswordMailMessage6") + "\n" +
                    App.getLanguageProperties("authResetPasswordMailMessage7"));
            return message;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Outbox logos, they change depending on used theme
        ImageView outboxBlack = new ImageView("main/resources/images/outbox_black.png");
        outboxBlack.setFitHeight(200);
        outboxBlack.setFitWidth(150);
        ImageView outboxWhite = new ImageView("main/resources/images/outbox_white.png");
        outboxWhite.setFitHeight(200);
        outboxWhite.setFitWidth(150);

        // Changes to UI depending on used theme
        if (Preference.readPreference("color").equals("red")) {
            passwordResetLogoImageView.setImage(outboxWhite.getImage());
        } else if (Preference.readPreference("color").equals("orange")) {
            passwordResetLogoImageView.setImage(outboxBlack.getImage());
        } else {
            passwordResetLogoImageView.setImage(outboxBlack.getImage());
        }
    }

    public void handleSendVerificationCode(
            ActionEvent actionEvent) throws MessagingException {
        handleSendEmail();
    }

    public void handleSendVerificationCodesOnEnterPressed(
            KeyEvent keyEvent) throws MessagingException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleSendEmail();
        }
    }

    public void handleSendEmail() throws MessagingException {
        if (isEmail(passwordResetEmailField.getText())) {
            boolean foundEmail = false;


            List<Users> listOfUsers = getUsers();
            for (int i = 0; i < getUsers().size(); i++) {
                if (passwordResetEmailField.getText().equals(
                        listOfUsers.get(i).getEmail())) {
                    foundEmail = true;
                    userId = listOfUsers.get(i).getId();
                    userInfoId = listOfUsers.get(i).getUserInfoId();
                    break;
                }
            }

            if (foundEmail) {
                //Generating verification code
                Random rand = new Random();
                int vCode1 = rand.nextInt(9);
                int vCode2 = rand.nextInt(9);
                int vCode3 = rand.nextInt(9);
                int vCode4 = rand.nextInt(9);
                verificationCode = "" + vCode1 + vCode2 + vCode3 + vCode4 + "";
                sendEmail(passwordResetEmailField.getText(),
                        getUserInfoByID(userInfoId).get(0).getName(),
                        verificationCode);

                //Blocking email field and send code button
                passwordResetEmailField.setDisable(true);
                passwordResetEmailCircle.getStyleClass().clear();
                passwordResetEmailCircle.getStyleClass().add("fillCorrect");

                passwordResetEmailField.getStyleClass().clear();
                passwordResetEmailField.getStyleClass().add("textFieldsCorrect");

                passwordResetSendCodeLabel.setVisible(false);

                passwordResetSendCodeButton.setDisable(true);
                passwordResetSendCodeButton.setVisible(false);

                //Unblocking verification code field and verify button
                passwordResetVerificationCodeField.setDisable(false);
                passwordResetVerificationCodeCircle.getStyleClass().clear();
                passwordResetVerificationCodeCircle.getStyleClass().add("fill");

                passwordResetVerifyCodeLabel.setVisible(true);

                passwordResetVerifyCodeButton.setDisable(false);
            } else {
                errorOnEmail();
                Alerts.createCustomAlert(loginRightPaneAnchorPane,
                        passwordResetSendCodeButton, "WARNING",
                        App.getLanguageProperties("authNoEmailUserFoundAlert"),
                        390, 86, "alertFailure");
            }
        } else {
            errorOnEmail();
            Alerts.createCustomAlert(loginRightPaneAnchorPane,
                    passwordResetSendCodeButton, "WARNING",
                    App.getLanguageProperties("authWrongEmailFormatAlert"),
                    350, 86, "alertFailure");
        }
    }

    public boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+" +
                "@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);

        return mat.matches();
    }

    /**
     * <p>
     * Method used to start procedure of sending email to user, that
     * wants to reset password. It sends prepared message to user.
     * </p>
     *
     * @param recipient        string representing email address of user,
     *                         that is receiving email
     * @param firstName        string representing first name of user,
     *                         that is receiving email
     * @param verificationCode generated 4-digit string
     * @throws MessagingException in case of error
     */
    public void sendEmail(String recipient,
                          String firstName,
                          String verificationCode
    ) throws MessagingException {
        System.out.println("Starting process of sending email");
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String outBoxEmailAccount = "outbox2137@gmail.com";
        String outBoxEmailPassword = "zaq1@WSX";

        Session session = Session.getInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(outBoxEmailAccount,
                                outBoxEmailPassword);
                    }
                });

        Message message = prepareMessage(session,
                outBoxEmailAccount,
                recipient,
                firstName,
                verificationCode);

        Transport.send(message);
        Alerts.createCustomAlert(loginRightPaneAnchorPane,
                passwordResetReturnButtonButton, "CHECK",
                App.getLanguageProperties(
                        "authEmailSentSuccessfullyAlert"),
                300, 86, "alertSuccess");
    }

    public void handleVerifyCodeOnEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            verifyCode();
        }
    }

    public void handleVerify(ActionEvent actionEvent) {
        verifyCode();
    }

    /**
     * <p>
     * Method that verifies if user passed correct verification code.
     * </p>
     */
    public void verifyCode() {
        if (passwordResetVerificationCodeField.getText().equals(verificationCode)) {
            Alerts.createCustomAlert(loginRightPaneAnchorPane,
                    passwordResetReturnButtonButton, "CHECK",
                    App.getLanguageProperties(
                            "authVerificationSuccessfulAlert"),
                    300, 86, "alertSuccess");

            //Block verification code field and verify button
            passwordResetVerificationCodeField.setDisable(true);
            passwordResetVerificationCodeCircle.getStyleClass().clear();
            passwordResetVerificationCodeCircle.getStyleClass().add("fillCorrect");

            passwordResetVerificationCodeField.getStyleClass().clear();
            passwordResetVerificationCodeField.getStyleClass().add("textFieldsCorrect");

            passwordResetVerifyCodeLabel.setVisible(false);

            passwordResetVerifyCodeButton.setDisable(true);
            passwordResetVerifyCodeButton.setVisible(false);

            //Unblock password fields and set password button
            passwordResetPasswordField.setDisable(false);
            passwordResetPasswordCircle.getStyleClass().clear();
            passwordResetPasswordCircle.getStyleClass().add("fill");

            passwordResetConfirmPasswordField.setDisable(false);
            passwordResetConfirmPasswordCircle.getStyleClass().clear();
            passwordResetConfirmPasswordCircle.getStyleClass().add("fill");

            passwordResetSetNewPasswordButton.setDisable(false);

        } else {
            errorOnVerificationCode();
            Alerts.createCustomAlert(loginRightPaneAnchorPane,
                    passwordResetVerifyCodeButton, "WARNING",
                    App.getLanguageProperties(
                            "authVerificationCodeInvalidAlert"),
                    300, 86, "alertFailure");
        }
    }

    public void handleSetNewPassword(ActionEvent actionEvent) {
        passwordReset();
    }

    public void handlePasswordResetOnEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            passwordReset();
        }
    }

    public void passwordReset() {
        if (isValid(passwordResetPasswordField.getText(),
                passwordResetConfirmPasswordField.getText())) {

            UsersDAO.updatePassword(userId,
                    passwordResetPasswordField.getText());
            System.out.println("Password Reset Successful");

            SceneManager.renderScene("successfulPasswordReset");
        }
    }

    /**
     * <p>
     * Method verifies if passed strings are correct passwords.
     * Passwords should have at least: one small letter, one big letter,
     * one digit, six chars and special character. Typed in passwords should
     * be the same.
     * </p>
     *
     * @param password1 user's password
     * @param password2 repeated password
     * @return returns boolean value
     */
    public boolean isValid(String password1, String password2) {
        boolean passwordError = false, passwordNotTheSameError = false;
        int error = 0;

        Pattern patternPassword = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])" +
                "(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$");

        Matcher matchPassword = patternPassword.matcher(password1);

        if (!matchPassword.matches()) {
            errorOnPassword();
            passwordError = true;
            errorOnConfirmPassword();
            error++;
        } else {
            if (!password1.equals(password2)) {
                passwordNotTheSameError = true;
                errorOnConfirmPassword();
                error++;
            }
        }

        if (error <= 0) {
            return true;
        } else if (error == 1) {
            if (passwordError) {
                Alerts.createCustomAlert(loginRightPaneAnchorPane,
                        passwordResetSetNewPasswordButton, "WARNING",
                        App.getLanguageProperties(
                                "authWrongPasswordFormatAlert"),
                        350, 86, "alertFailure");
            }
            if (passwordNotTheSameError) {
                Alerts.createCustomAlert(loginRightPaneAnchorPane,
                        passwordResetSetNewPasswordButton, "WARNING",
                        App.getLanguageProperties(
                                "authPasswordsNotTheSameAlert"),
                        350, 86, "alertFailure");
            }
            return false;
        } else {

            Alerts.createCustomAlert(loginRightPaneAnchorPane,
                    passwordResetSetNewPasswordButton, "WARNING",
                    App.getLanguageProperties(
                            "authErrorsOnTextFieldsAlert"),
                    350, 86, "alertFailure");
            return false;
        }
    }

    /**
     * <p>
     * Method changes visual appearance of section that shows user
     * if typed in password meets its requirements.
     * </p>
     */
    private void passwordRequirements() {
        Pattern sixChars = Pattern.compile(".{6,}");
        Pattern smallLetter = Pattern.compile(".*[a-z]+.*");
        Pattern bigLetter = Pattern.compile(".*[A-Z]+.*");
        Pattern number = Pattern.compile(".*[0-9]+.*");
        Pattern specialChar = Pattern.compile(".*[!@#$%^&*()_\\-+=]+.*");

        Matcher matchSixChars = sixChars.matcher(
                passwordResetPasswordField.getText());
        Matcher matchSmallLetter = smallLetter.matcher(
                passwordResetPasswordField.getText());
        Matcher matchBigLetter = bigLetter.matcher(
                passwordResetPasswordField.getText());
        Matcher matchNumber = number.matcher(
                passwordResetPasswordField.getText());
        Matcher matchSpecialChar = specialChar.matcher(
                passwordResetPasswordField.getText());

        if (matchSixChars.matches()) {
            passwordResetSixCharsRequirement.getStyleClass().clear();
            passwordResetSixCharsRequirement.getStyleClass().add("successText");
        } else {
            passwordResetSixCharsRequirement.getStyleClass().clear();
            passwordResetSixCharsRequirement.getStyleClass().add("errorText");
        }

        if (matchSmallLetter.matches()) {
            passwordResetSmallLetterRequirement.getStyleClass().clear();
            passwordResetSmallLetterRequirement.getStyleClass().add("successText");
        } else {
            passwordResetSmallLetterRequirement.getStyleClass().clear();
            passwordResetSmallLetterRequirement.getStyleClass().add("errorText");
        }

        if (matchBigLetter.matches()) {
            passwordResetBigLetterRequirement.getStyleClass().clear();
            passwordResetBigLetterRequirement.getStyleClass().add("successText");
        } else {
            passwordResetBigLetterRequirement.getStyleClass().clear();
            passwordResetBigLetterRequirement.getStyleClass().add("errorText");
        }

        if (matchNumber.matches()) {
            passwordResetNumberRequirement.getStyleClass().clear();
            passwordResetNumberRequirement.getStyleClass().add("successText");
        } else {
            passwordResetNumberRequirement.getStyleClass().clear();
            passwordResetNumberRequirement.getStyleClass().add("errorText");
        }

        if (matchSpecialChar.matches()) {
            passwordResetSpecialCharRequirement.getStyleClass().clear();
            passwordResetSpecialCharRequirement.getStyleClass().add("successText");
        } else {
            passwordResetSpecialCharRequirement.getStyleClass().clear();
            passwordResetSpecialCharRequirement.getStyleClass().add("errorText");
        }

        if (passwordResetConfirmPasswordField.getText().equals(
                passwordResetPasswordField.getText())) {
            passwordResetSamePasswordsRequirement.getStyleClass().clear();
            passwordResetSamePasswordsRequirement.getStyleClass().add("successText");
        } else {
            passwordResetSamePasswordsRequirement.getStyleClass().clear();
            passwordResetSamePasswordsRequirement.getStyleClass().add("errorText");
        }
    }

    public void checkRequirements(KeyEvent keyEvent) {
        passwordRequirements();
    }


    public void errorOnEmail() {
        //EmailField
        passwordResetEmailField.getStyleClass().clear();
        passwordResetEmailField.getStyleClass().add("textFieldsError");
        //EmailCircle
        passwordResetEmailCircle.getStyleClass().clear();
        passwordResetEmailCircle.getStyleClass().add("fillError");
    }

    public void clearErrorsOnEmail(KeyEvent keyEvent) {
        //EmailField
        passwordResetEmailField.getStyleClass().clear();
        passwordResetEmailField.getStyleClass().add("textFields");
        //EmailCircle
        passwordResetEmailCircle.getStyleClass().clear();
        passwordResetEmailCircle.getStyleClass().add("fill");
    }

    public void errorOnVerificationCode() {
        //VerificationCodeField
        passwordResetVerificationCodeField.getStyleClass().clear();
        passwordResetVerificationCodeField.getStyleClass().add("textFieldsError");
        //VerificationCodeCircle
        passwordResetVerificationCodeCircle.getStyleClass().clear();
        passwordResetVerificationCodeCircle.getStyleClass().add("fillError");
    }

    public void clearErrorsOnVerificationCode(KeyEvent keyEvent) {
        //VerificationCodeField
        passwordResetVerificationCodeField.getStyleClass().clear();
        passwordResetVerificationCodeField.getStyleClass().add("textFields");
        //VerificationCodeCircle
        passwordResetVerificationCodeCircle.getStyleClass().clear();
        passwordResetVerificationCodeCircle.getStyleClass().add("fill");
    }

    public void errorOnPassword() {
        //PasswordField
        passwordResetPasswordField.getStyleClass().clear();
        passwordResetPasswordField.getStyleClass().add("textFieldsError");
        //PasswordCircle
        passwordResetPasswordCircle.getStyleClass().clear();
        passwordResetPasswordCircle.getStyleClass().add("fillError");
    }

    public void clearErrorsOnPassword(KeyEvent keyEvent) {
        //PasswordField
        passwordResetPasswordField.getStyleClass().clear();
        passwordResetPasswordField.getStyleClass().add("textFields");
        //PasswordCircle
        passwordResetPasswordCircle.getStyleClass().clear();
        passwordResetPasswordCircle.getStyleClass().add("fill");
    }

    public void errorOnConfirmPassword() {
        //ConfirmPasswordField
        passwordResetConfirmPasswordField.getStyleClass().clear();
        passwordResetConfirmPasswordField.getStyleClass().add("textFieldsError");
        //ConfirmPasswordCircle
        passwordResetConfirmPasswordCircle.getStyleClass().clear();
        passwordResetConfirmPasswordCircle.getStyleClass().add("fillError");
    }

    public void clearErrorsOnConfirmPassword(KeyEvent keyEvent) {
        //ConfirmPasswordField
        passwordResetConfirmPasswordField.getStyleClass().clear();
        passwordResetConfirmPasswordField.getStyleClass().add("textFields");
        //ConfirmPasswordCircle
        passwordResetConfirmPasswordCircle.getStyleClass().clear();
        passwordResetConfirmPasswordCircle.getStyleClass().add("fill");
    }

    public void handleReturn(ActionEvent actionEvent) {
        SceneManager.renderScene("login");
    }

    @FXML
    void exitApp(ActionEvent event) {
        Stage stage = (Stage) loginRightPaneAnchorPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void minApp(ActionEvent event) {
        Stage stage = (Stage) loginRightPaneAnchorPane.getScene().getWindow();
        stage.setIconified(true);
    }
}
