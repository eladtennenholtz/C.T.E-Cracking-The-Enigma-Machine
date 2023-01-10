package components.login;

import components.main.AlliesMainController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.ConstantsAllies;
import utils.HttpClientUtilAllies;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginControllerAllies implements Initializable {
    AlliesMainController alliesMainController;
    @FXML
    private Button loginButton;
    @FXML
    private TextField loginTextField;
    @FXML
    private Label loginErrorLabel;
    
    
    
    public void loginButtonListener(ActionEvent actionEvent) { String userName = loginTextField.getText();
        if (userName.isEmpty()) {
            loginErrorLabel.setText("User Allies name is empty. You can't login with empty user name");
            return;
        }
        for(int i=0;i<userName.length();i++){
            if(userName.charAt(i)=='\n'){
                loginErrorLabel.setText("You cant type the 'enter' key for user name.");
                return;
            }
        }


        String finalUrl = HttpUrl
                .parse(ConstantsAllies.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName).addQueryParameter("type","allies")
                .build()
                .toString();

        //updateHttpStatusLine("New request is launched for: " + finalUrl);
        HttpClientUtilAllies.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        loginErrorLabel.setText("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            loginErrorLabel.setText("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        alliesMainController.setCurrentLoginAccount(userName);
                        alliesMainController.setPageAfterLoginSuccess();
                    });
                }
                response.close();
            }
        });
    }

    public void setAlliesMainController(AlliesMainController alliesMainController) {
        this.alliesMainController=alliesMainController;
        loginTextField.setText("");
        loginErrorLabel.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
