package components.main;

import com.google.gson.Gson;
import data.DataCandidatesToAllies;
import data.DataCandidatesToUBoat;
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

public class TeamsCandidatesRefresher extends TimerTask {

    private Consumer<DataCandidatesToAllies[]> allCandidatesForAlliesConsumer;
    private String currentUser;

    public TeamsCandidatesRefresher(Consumer<DataCandidatesToAllies[]> allCandidatesForAlliesConsumer,String currentUser)
    {
        this.allCandidatesForAlliesConsumer=allCandidatesForAlliesConsumer;
        this.currentUser=currentUser;
    }
    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(ConstantsAllies.CANDIDATES_FOR_ALLIES_PAGE)
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
                    DataCandidatesToAllies[]dataCandidatesToAllies=gson.fromJson(response.body().string(),DataCandidatesToAllies[].class);
                    // JsonParser jsonParser=new JsonParser();
                    // JsonElement jsonElement=jsonParser.parse(response.body().string());
                    //JsonObject jsonObject =jsonElement.getAsJsonObject();
                    //allCandidatesForUBoat.setAllData(jsonElement.getAsJsonArray());
                    if(dataCandidatesToAllies!=null) {
                        allCandidatesForAlliesConsumer.accept(dataCandidatesToAllies);
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
