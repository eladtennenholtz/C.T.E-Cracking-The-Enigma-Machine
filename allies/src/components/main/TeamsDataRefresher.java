package components.main;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import components.main.model.AllTeamsAgentsData;
import components.main.model.AllTeamsDataForContest;
import data.DataContestWebDto;
import data.DataTeamWebDto;
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

public class TeamsDataRefresher extends TimerTask {

   private Consumer<AllTeamsDataForContest> allTeamsDataForContestConsumer;
    String currentBattle;

   public TeamsDataRefresher(Consumer<AllTeamsDataForContest> allTeamsDataForContestConsumer,String currentBattle){
       this.allTeamsDataForContestConsumer=allTeamsDataForContestConsumer;
       this.currentBattle=currentBattle;


   }
    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(ConstantsAllies.ALL_TEAMS_DATA_FOR_CONTEST_PAGE)
                .newBuilder().addQueryParameter("battle",currentBattle)
                .build()
                .toString();
        HttpClientUtilAllies.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson=new Gson();
                    DataTeamWebDto[]dataTeamWebDtos=gson.fromJson(response.body().string(),DataTeamWebDto[].class);
                    AllTeamsDataForContest allTeamsDataForContest=new AllTeamsDataForContest();
                    //JsonParser jsonParser=new JsonParser();
                    //JsonElement jsonElement=jsonParser.parse(response.body().string());
                    //allTeamsDataForContest.setAllData(jsonElement.getAsJsonArray());
                    allTeamsDataForContest.setAllDataNew(dataTeamWebDtos);
                    allTeamsDataForContestConsumer.accept(allTeamsDataForContest);

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
