package components.main;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import components.main.model.AllEncryptedStringAndStartContestData;
import components.main.model.AllTeamsDataForContest;
import data.DataContestWebDto;
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

public class EncryptedStringAndContestStartRefresher extends TimerTask {

    private Consumer<AllEncryptedStringAndStartContestData>allEncryptedStringAndStartContestDataConsumer;
    private String currentBattle;

    public EncryptedStringAndContestStartRefresher(Consumer<AllEncryptedStringAndStartContestData>allEncryptedStringAndStartContestDataConsumer,String currentBattle){
        this.allEncryptedStringAndStartContestDataConsumer=allEncryptedStringAndStartContestDataConsumer;
        this.currentBattle=currentBattle;
    }


    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(ConstantsAllies.ENCRYPTED_STRING_AND_START_CONTEST_PAGE)
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
                    String[]data=gson.fromJson(response.body().string(),String[].class);
                    AllEncryptedStringAndStartContestData allEncryptedStringAndStartContestData=new AllEncryptedStringAndStartContestData();
                    allEncryptedStringAndStartContestData.setAllDataNew(data);
                    //JsonParser jsonParser=new JsonParser();
                    //JsonElement jsonElement=jsonParser.parse(response.body().string());
                    //allEncryptedStringAndStartContestData.setAllData(jsonElement.getAsJsonArray());
                    allEncryptedStringAndStartContestDataConsumer.accept(allEncryptedStringAndStartContestData);

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
