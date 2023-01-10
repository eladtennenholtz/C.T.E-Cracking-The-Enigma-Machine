package components.main;

import com.google.gson.Gson;
import components.login.modal.AllAgentsContestAndTeamData;
import data.DataContestAndTeamForAgentDto;
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

public class AgentIsContestStartedRefresher extends TimerTask {

    private Consumer<Boolean>contestStartedConsumer;
    private String agentName;
    private String allyName;


    public AgentIsContestStartedRefresher(Consumer<Boolean>contestStartedConsumer,String agentName,String allyName){
        this.contestStartedConsumer=contestStartedConsumer;
        this.agentName=agentName;
        this.allyName=allyName;
    }

    @Override
    public void run() {


        String finalUrl = HttpUrl
                .parse(ConstantsAgents.IS_CONTEST_STARTED_AGENT_PAGE)
                .newBuilder().addQueryParameter("username",agentName).addQueryParameter("team",allyName)
                .build()
                .toString();
        HttpClientUtilAgents.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    contestStartedConsumer.accept(true);

                } else {
                    response.close();
                    //System.out.println("There is NO competitions yet!");

                }
            }
        });

    }

    }

