package kaist.aguno.melona;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class QuestList extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_list);


        Bundle extras = getIntent().getExtras();
        String strJson = "{\"quests\": "+extras.getString("strJSON")+"}";
        //String strJson = "{\"quests\": [{\"tag\":[\"정말\", \"배고파\"],\"_id\":\"5a62ed8a2b2cee0df5c1cdd4\",\"startPoint\":\"창의관\",\"destination\":\"undefined\",\"coinReward\":0,\"expReward\":0,\"title\":\"quest1\",\"text\":\"도움이 필요해요!!\",\"state\":1,\"from\":\"kakao1\",\"to\":\"\",\"__v\":0},{\"tag\":[],\"_id\":\"5a62eda02b2cee0df5c1cdd5\",\"startPoint\":\"인사동\",\"destination\":\"undefined\",\"coinReward\":0,\"expReward\":0,\"title\":\"quest2\",\"text\":\"\",\"state\":1,\"from\":\"kakao1\",\"to\":\"\",\"__v\":0}]}";

        /** The parsing of the xml data is done in a non-ui thread */
        ListViewLoaderTask listViewLoaderTask = new ListViewLoaderTask();

        /** Start parsing xml data */
        listViewLoaderTask.execute(strJson);


    }
    private class ListViewLoaderTask extends AsyncTask<String, Void, SimpleAdapter> {

        JSONObject jObject;
        /** Doing the parsing of xml data in a non-ui thread */
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected SimpleAdapter doInBackground(String... strJson) {
            try{
                jObject = new JSONObject(strJson[0]);
                QuestJSONParser questJsonParser = new QuestJSONParser();
                questJsonParser.parse(jObject);
            }catch(Exception e){
                Log.d("JSON Exception1",e.toString());
            }

            QuestJSONParser questJsonParser = new QuestJSONParser();

            List<HashMap<String, String>> quests = null;

            try{
                /** Getting the parsed data as a List construct */
                quests = questJsonParser.parse(jObject);
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }

            /** Keys used in Hashmap */
            String[] from = { "title","where", "reward",  };

            /** Ids of views in listview_layout */
            int[] to = { R.id.title,R.id.where, R.id.reward};

            /** Instantiating an adapter to store each items
             *  R.layout.listview_layout defines the layout of each item
             */
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), quests, R.layout.quest_item, from, to);

            return adapter;
        }

        /** Invoked by the Android system on "doInBackground" is executed completely */
        /** This will be executed in ui thread */
        @Override

        protected void onPostExecute(SimpleAdapter adapter) {

            /** Getting a reference to listview of main.xml layout file */
            final ListView listView = ( ListView ) findViewById(R.id.listView);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(QuestList.this, QuestDetail.class);
                    intent.putExtra("toDetail", listView.getItemAtPosition(i).toString());
                    startActivity(intent);
                    finish();
                }
            });

            /** Setting the adapter containing the country list to listview */
            listView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quest_list, menu);
        return true;
    }

}