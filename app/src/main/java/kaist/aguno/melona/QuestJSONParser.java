package kaist.aguno.melona;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class QuestJSONParser {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<HashMap<String, String>> parse(JSONObject jObject){
        JSONArray jQuests = null;
        try {
            /* Retrieves all the elements in the 'countries' array */
            jQuests = jObject.getJSONArray("quests");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /* Invoking getCountries with the array of json object
         * where each json object represent a country
         */
        return getQuests(jQuests);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<HashMap<String, String>> getQuests(JSONArray jQuests){
        int questCount = jQuests.length();
        List<HashMap<String, String>> questList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> quest = null;

        /** Taking each country, parses and adds to list object */
        for(int i=0; i<questCount;i++){
            try {
                /** Call getCountry with country JSON object to parse the country */
                quest = getQuest((JSONObject)jQuests.get(i));
                questList.add(quest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return questList;
    }

    /** Parsing the Country JSON object */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private HashMap<String, String> getQuest(JSONObject jQuest){

        HashMap<String, String> quest = new HashMap<String, String>();
        String title = "";
        String startPoint="";
        String destination = "";
        String reward = "";
        String text = "";
        String where = "";
        String hashtags = "";
        String questId = "";
        String state = "";

        try {
            title = jQuest.getString("title");
            startPoint = jQuest.getString("startPoint");
            destination = jQuest.getString("destination");
            text = jQuest.getString("text");
            reward = "$" + jQuest.getString("coinReward");
            where = startPoint +" -> "+ destination;
            questId= jQuest.getString("_id");
            state = jQuest.getString("state");

            /*해시태그 작업*/
            JSONArray jsonArray = jQuest.getJSONArray("tag");
            /*
            String[] arr = new String[jsonArray.length()];
            for(int i=0; i < jsonArray.length(); i++){
                arr[i] = jsonArray.getString(i);
            }
            */
            if (jsonArray.length()!=0){
                hashtags = jsonArray.get(0).toString();
                if (hashtags.equals("")) {
                    hashtags = "No Hashtag";
                } else {
                    String[] arr = hashtags.split(",");

                    hashtags = "#" + String.join("  #", arr);
                }
            }


            if(state.equals("2"))
                state = "매칭됨";
            else
                state = "대기중";

            quest.put("title", title);
            quest.put("where", where);
            quest.put("reward", reward);
            quest.put("text", text);
            quest.put("tag", hashtags);
            quest.put("_id", questId);
            quest.put("state", state);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return quest;
    }
}