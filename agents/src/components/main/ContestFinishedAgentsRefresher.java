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

public class ContestFinishedAgentsRefresher extends TimerTask {

    private Consumer<String[]> contestFinishedConsumer;
    private String currentUser;
    private String battle;

    public ContestFinishedAgentsRefresher(Consumer<String[]>contestFinishedConsumer,String currentUser,String battle){
        this.contestFinishedConsumer=contestFinishedConsumer;
        this.currentUser=currentUser;
        this.battle=battle;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(ConstantsAgents.CONTEST_FINISHED_AGENT)
                .newBuilder().addQueryParameter("battle",battle).addQueryParameter("username",currentUser)
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
                    String[]dataOfContest=gson.fromJson(response.body().string(),String[].class);
                    if(dataOfContest!=null) {
                        if(dataOfContest[0]!=null) {
                            if (dataOfContest[0].equals("true")) {
                                contestFinishedConsumer.accept(dataOfContest);
                            }
                        }
                    }

                } else {
                    response.close();
                    //System.out.println("There is NO competitions yet!");

                }
            }
        });




    }



}
