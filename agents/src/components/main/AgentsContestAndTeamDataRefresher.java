package components.main;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import components.login.modal.AllAgentsContestAndTeamData;
import components.login.modal.AllTeamsData;
import data.DataContestAndTeamForAgentDto;
import data.DataContestWebDto;
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

public class AgentsContestAndTeamDataRefresher extends TimerTask {
    private Consumer<AllAgentsContestAndTeamData>allAgentsContestAndTeamDataConsumer;
    private String currentUser;


    public AgentsContestAndTeamDataRefresher(Consumer<AllAgentsContestAndTeamData>allAgentsContestAndTeamDataConsumer,String currentUser){
        this.allAgentsContestAndTeamDataConsumer=allAgentsContestAndTeamDataConsumer;
        this.currentUser=currentUser;
    }
    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(ConstantsAgents.AGENTS_CONTEST_AND_TEAM_DATA)
                .newBuilder().addQueryParameter("username",currentUser)
                .build()
                .toString();
        HttpClientUtilAgents.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson=new Gson();
                    DataContestAndTeamForAgentDto[]dataContestAndTeamForAgentDtos=gson.fromJson(response.body().string(),DataContestAndTeamForAgentDto[].class);

                    AllAgentsContestAndTeamData allAgentsContestAndTeamData=new AllAgentsContestAndTeamData();
                    //JsonParser jsonParser=new JsonParser();
                    //JsonElement jsonElement=jsonParser.parse(response.body().string());
                    //allAgentsContestAndTeamData.setAllData(jsonElement.getAsJsonArray());
                    if(dataContestAndTeamForAgentDtos!=null) {
                        allAgentsContestAndTeamData.setAllDataNew(dataContestAndTeamForAgentDtos);
                        allAgentsContestAndTeamDataConsumer.accept(allAgentsContestAndTeamData);
                    }

                } else {
                    response.close();
                    //System.out.println("There is NO competitions yet!");

                }
            }
        });

    }
}
