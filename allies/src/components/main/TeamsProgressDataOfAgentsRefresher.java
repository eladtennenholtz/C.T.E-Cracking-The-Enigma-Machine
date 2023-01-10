package components.main;

import com.google.gson.Gson;
import data.DataAgentsProgressToAllies;
import data.DataCandidatesToAllies;
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

public class TeamsProgressDataOfAgentsRefresher extends TimerTask {

    private Consumer<DataAgentsProgressToAllies[]> allAgentsProgress;
    private String currentUser;

    public TeamsProgressDataOfAgentsRefresher(Consumer<DataAgentsProgressToAllies[]> allAgentsProgress,String currentUser)
    {
        this.allAgentsProgress=allAgentsProgress;
        this.currentUser=currentUser;
    }
    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(ConstantsAllies.PROGRESS_OF_AGENTS_ALLIES_PAGE)
                .newBuilder().addQueryParameter("username",currentUser)
                .build()
                .toString();
        HttpClientUtilAllies.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code()==200) {
                    Gson gson=new Gson();
                    DataAgentsProgressToAllies[]dataAgentsProgressToAllies=gson.fromJson(response.body().string(),DataAgentsProgressToAllies[].class);
                    // JsonParser jsonParser=new JsonParser();
                    // JsonElement jsonElement=jsonParser.parse(response.body().string());
                    //JsonObject jsonObject =jsonElement.getAsJsonObject();
                    //allCandidatesForUBoat.setAllData(jsonElement.getAsJsonArray());
                    if(dataAgentsProgressToAllies!=null) {
                        allAgentsProgress.accept(dataAgentsProgressToAllies);
                    }

                } else{

                    response.close();
                    //System.out.println("fuck not good");
                    //contestStartedConsumer.accept("false");
                    //System.out.println("There is NO competitions yet!");

                }
            }
        });

    }
}
