package components.main;

import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.ConstantsAgents;
import utils.HttpClientUtilAgents;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class PressedAllyFinishedRefresher extends TimerTask {

    private Consumer<Boolean>consumerAllyPressedFinished;
    private String team;

    public PressedAllyFinishedRefresher(Consumer<Boolean>consumerAllyPressedFinished,String team){
        this.consumerAllyPressedFinished=consumerAllyPressedFinished;
        this.team=team;
    }
    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(ConstantsAgents.CHECK_ALLY_PRESSED_FINISHED)
                .newBuilder().addQueryParameter("team",team)
                .build()
                .toString();
        HttpClientUtilAgents.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    consumerAllyPressedFinished.accept(true);

                } else {
                    consumerAllyPressedFinished.accept(false);
                    //System.out.println("There is NO competitions yet!");

                }
                response.close();
            }
        });




    }
}
