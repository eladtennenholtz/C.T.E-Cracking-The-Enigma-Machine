package components.main;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ContestStartedRefresherUBoat extends TimerTask {
    private Consumer<String> contestStartedConsumer;
    private String currentUser;

    public ContestStartedRefresherUBoat(Consumer<String> contestStartedConsumer,String currentUser){
        this.contestStartedConsumer=contestStartedConsumer;
        this.currentUser=currentUser;

    }
    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.CONTEST_STARTED_UBOAT_PAGE)
                .newBuilder().addQueryParameter("username",currentUser)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code()==200) {
                    contestStartedConsumer.accept("true");

                    response.close();
                    // allContestsData.setAllData(jsonElement.getAsJsonArray());
                    //  allContestsDataConsumer.accept(allContestsData);

                } else if(response.code()==204) {

                    contestStartedConsumer.accept("false");
                    //System.out.println("There is NO competitions yet!");

                    response.close();
                }
            }
        });


    }
}
