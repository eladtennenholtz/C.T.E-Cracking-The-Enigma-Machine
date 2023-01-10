package components.main;

import com.google.gson.Gson;
import data.DataAgentsProgressToAllies;
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

public class ProgressOfTasksDmRefresher extends TimerTask {

    private Consumer<Integer[]> progressTasksDm;
    private String currentUser;
    private String battle;

    public ProgressOfTasksDmRefresher(Consumer<Integer[]> progressTasksDm,String currentUser,String battle){
        this.progressTasksDm=progressTasksDm;
        this.currentUser=currentUser;
        this.battle=battle;
    }


    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(ConstantsAllies.TOTAL_PROGRESS_DATA_OF_DM_PAGE)
                .newBuilder().addQueryParameter("username",currentUser).addQueryParameter("battle",battle)
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
                    Integer[]dataOfTasksProgress=gson.fromJson(response.body().string(),Integer[].class);
                    // JsonParser jsonParser=new JsonParser();
                    // JsonElement jsonElement=jsonParser.parse(response.body().string());
                    //JsonObject jsonObject =jsonElement.getAsJsonObject();
                    //allCandidatesForUBoat.setAllData(jsonElement.getAsJsonArray());
                    if(dataOfTasksProgress!=null) {
                        progressTasksDm.accept(dataOfTasksProgress);
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
