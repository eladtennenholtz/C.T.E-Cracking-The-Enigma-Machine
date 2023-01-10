package components.main;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import components.main.model.AllCandidatesForUBoat;
import data.DataCandidatesToUBoat;
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

public class CandidatesRefresherUBoat extends TimerTask {
    private Consumer<DataCandidatesToUBoat[]> allCandidatesForUBoatConsumer;
    private String currentUser;

    public CandidatesRefresherUBoat(Consumer<DataCandidatesToUBoat[]> allCandidatesForUBoatConsumer,String currentUser){
        this.allCandidatesForUBoatConsumer=allCandidatesForUBoatConsumer;
        this.currentUser=currentUser;
    }


    @Override
    public void run() {



        String finalUrl = HttpUrl
                .parse(Constants.RECEIVE_CANDIDATES_UBOAT_SERVLET)
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
                    Gson gson=new Gson();
                    DataCandidatesToUBoat[]dataCandidatesToUBoats=gson.fromJson(response.body().string(),DataCandidatesToUBoat[].class);

                   // JsonParser jsonParser=new JsonParser();
                   // JsonElement jsonElement=jsonParser.parse(response.body().string());
                    //JsonObject jsonObject =jsonElement.getAsJsonObject();
                    //allCandidatesForUBoat.setAllData(jsonElement.getAsJsonArray());
                    if(dataCandidatesToUBoats!=null) {
                        allCandidatesForUBoatConsumer.accept(dataCandidatesToUBoats);
                    }

                } else{

                    //System.out.println("fuck not good");
                    //contestStartedConsumer.accept("false");
                    //System.out.println("There is NO competitions yet!");

                }
            }
        });

    }
}
