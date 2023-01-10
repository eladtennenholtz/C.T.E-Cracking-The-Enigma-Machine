package components.main;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import components.main.model.AllContestsData;
import components.main.model.AllTeamsAgentsData;
import data.DataContestWebDto;
import data.DtoAgent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.ConstantsAllies;
import utils.HttpClientUtilAllies;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class TeamsAgentsRefresher extends TimerTask {

    Consumer<AllTeamsAgentsData> allTeamsAgentsDataConsumer;
    String currentUser;

    public TeamsAgentsRefresher(Consumer<AllTeamsAgentsData> allTeamsAgentsDataConsumer,String currentUser){
        this.allTeamsAgentsDataConsumer=allTeamsAgentsDataConsumer;
        this.currentUser=currentUser;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(ConstantsAllies.TEAMS_AGENTS_DATA_PAGE)
                .newBuilder().addQueryParameter("username",currentUser)
                .build()
                .toString();
        HttpClientUtilAllies.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    AllTeamsAgentsData allTeamsAgentsData=new AllTeamsAgentsData();
                    Gson gson=new Gson();
                    DtoAgent[]dtoAgents=gson.fromJson(response.body().string(),DtoAgent[].class);
                    allTeamsAgentsData.setAllDataNew(dtoAgents);
                    //JsonParser jsonParser=new JsonParser();
                    //JsonElement jsonElement=jsonParser.parse(response.body().string());
                    //allTeamsAgentsData.setAllData(jsonElement.getAsJsonArray());
                    allTeamsAgentsDataConsumer.accept(allTeamsAgentsData);

                   // allContestsData.setAllData(jsonElement.getAsJsonArray());
                  //  allContestsDataConsumer.accept(allContestsData);

                } else {
                    response.close();
                    //System.out.println("There is NO competitions yet!");

                }
            }
        });

    }
}
