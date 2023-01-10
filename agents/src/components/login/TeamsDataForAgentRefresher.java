package components.login;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import components.login.modal.AllTeamsData;
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

public class TeamsDataForAgentRefresher extends TimerTask {

    Consumer<AllTeamsData> allTeamsDataConsumer;

    public TeamsDataForAgentRefresher(Consumer<AllTeamsData>allTeamsDataConsumer){

        this.allTeamsDataConsumer=allTeamsDataConsumer;

    }
    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(ConstantsAgents.AGENTS_TEAMS)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtilAgents.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    AllTeamsData allTeamsData=new AllTeamsData();
                    JsonParser jsonParser=new JsonParser();
                    JsonElement jsonElement=jsonParser.parse(response.body().string());
                    //JsonObject jsonObject =jsonElement.getAsJsonObject();
                    allTeamsData.setAllData(jsonElement.getAsJsonArray());
                    allTeamsDataConsumer.accept(allTeamsData);

                } else {
                    response.close();
                    //System.out.println("There is NO competitions yet!");

                }
            }
        });

    }
}
