package components.main;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import components.main.model.AllContestsData;
import data.DataContestWebDto;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.ConstantsAllies;
import utils.HttpClientUtilAllies;

import java.io.IOException;
import java.io.InputStream;
import java.util.TimerTask;
import java.util.function.Consumer;

public class DataContestsRefresher extends TimerTask {

    Consumer<AllContestsData>allContestsDataConsumer;


    public DataContestsRefresher(Consumer<AllContestsData>allContestsDataConsumer){

        this.allContestsDataConsumer=allContestsDataConsumer;

    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(ConstantsAllies.CONTESTS_DATA_PAGE)
                .newBuilder()
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
                    DataContestWebDto[]dataContestWebDtos=gson.fromJson(response.body().string(),DataContestWebDto[].class);

                    AllContestsData allContestsData=new AllContestsData();
                    allContestsData.setEmpty(false);
                    //JsonParser jsonParser=new JsonParser();
                    //JsonElement jsonElement=jsonParser.parse(response.body().string());
                    //allContestsData.setAllData(jsonElement.getAsJsonArray());
                    allContestsData.setAllDataNew(dataContestWebDtos);
                    allContestsDataConsumer.accept(allContestsData);

                } else {
                    AllContestsData allContestsData=new AllContestsData();
                    allContestsData.setEmpty(true);
                    allContestsDataConsumer.accept(allContestsData);
                    response.close();
                    //System.out.println("There is NO competitions yet!");

                }
            }
        });

    }
}
