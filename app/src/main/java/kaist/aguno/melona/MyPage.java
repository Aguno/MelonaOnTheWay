package kaist.aguno.melona;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2018-01-21.
 */

public class MyPage extends Activity{
    String kakaoID,kakaNickname,kakaoThumbnail,kakaoProfilePicture;
    int coin_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);
        SharedPreferences prefs = getSharedPreferences("kakaoID",MODE_PRIVATE);
        kakaoID = prefs.getString("kakaoID",null);
        kakaNickname = prefs.getString("kakaoNickname",null);
        kakaoThumbnail = prefs.getString("kakaoProfileThumbnail",null);
        kakaoProfilePicture = prefs.getString("kakaoProfilePicture", null);
        coin_left = getIntent().getIntExtra("coin_left", 9999);

        Bundle extras = getIntent().getExtras();
        String strJson = "{\"quests\": "+extras.getString("acceptedQuest")+"}";
        String strJson2 = "{\"quests\": "+extras.getString("uploadedQuestPending");
        String strJson3 = extras.getString("uploadedQuestMatched")+"}";
        strJson2 = strJson2.substring(0,strJson2.length()-1);
        strJson3 = strJson3.substring(1);
        Log.d("msg", strJson);
        Log.d("msg", strJson2+strJson3);
        //String strJson = "{\"quests\": [{\"tag\":[\"정말\", \"배고파\"],\"_id\":\"5a62ed8a2b2cee0df5c1cdd4\",\"startPoint\":\"창의관\",\"destination\":\"undefined\",\"coinReward\":0,\"expReward\":0,\"title\":\"quest1\",\"text\":\"도움이 필요해요!!\",\"state\":2,\"from\":\"kakao1\",\"to\":\"703014046\",\"__v\":0},{\"tag\":[],\"_id\":\"5a62eda02b2cee0df5c1cdd5\",\"startPoint\":\"인사동\",\"destination\":\"undefined\",\"coinReward\":0,\"expReward\":0,\"title\":\"quest2\",\"text\":\"\",\"state\":2,\"from\":\"kakao1\",\"to\":\"703014046\",\"__v\":0}]}";
        //String strJson2 = "{\"quests\": [{\"tag\":[],\"_id\":\"5a62eda02b2cee0df5c1cdd5\",\"startPoint\":\"인사동\",\"destination\":\"undefined\",\"coinReward\":0,\"expReward\":0,\"title\":\"waiting\",\"text\":\"\",\"state\":1,\"from\":\"703014046\",\"to\":\"\",\"__v\":0}";
        //String strJson3 = ",{\"tag\":[],\"_id\":\"5a62eda02b2cee0df5c1cdd5\",\"startPoint\":\"인사동\",\"destination\":\"undefined\",\"coinReward\":0,\"expReward\":0,\"title\":\"matched\",\"text\":\"\",\"state\":2,\"from\":\"703014046\",\"to\":\"123\",\"__v\":0}]}";





        strJson2 = strJson2+","+ strJson3;
        /** Start parsing xml data */
        if (!strJson.equals("{\"quests\": []}")){
            MyPage.ListViewLoaderTask2 listViewLoaderTask2 = new MyPage.ListViewLoaderTask2();
            listViewLoaderTask2.execute(strJson);
        }

        if (!strJson2.equals("{\"quests\": []}")){
            MyPage.ListViewLoaderTask listViewLoaderTask = new MyPage.ListViewLoaderTask();
            listViewLoaderTask.execute(strJson2);
        }


        //locate Views
        ImageView iv = (ImageView) findViewById(R.id.image);
        TextView nickname = (TextView) findViewById(R.id.nickname);
        TextView howmuch = (TextView) findViewById(R.id.how_much);

        String imgURL  = kakaoProfilePicture;
        new DownloadImageTask(iv)
                .execute(imgURL);

        //set all Listview adapter
        nickname.setText(kakaNickname);
        howmuch.setText(coin_left+"");


        //set dynmic height for all listviews
        /*setDynamicHeight(ListView1);
        setDynamicHeight(ListView2);
        new DownLoadImageTask(iv).execute(imgURL);*/

    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            try {
                URL url = new URL(urldisplay);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            Bitmap resized = Bitmap.createScaledBitmap(result, 300, 300, true);
            bmImage.setImageBitmap(resized);
        }
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
            String[] from = { "title","where", "reward", "state"};

            /** Ids of views in listview_layout */
            int[] to = { R.id.title,R.id.where, R.id.reward, R.id.state};

            /** Instantiating an adapter to store each items
             *  R.layout.listview_layout defines the layout of each item
             */
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), quests, R.layout.my_page_item1, from, to);

            return adapter;
        }

        /** Invoked by the Android system on "doInBackground" is executed completely */
        /** This will be executed in ui thread */
        @Override
        protected void onPostExecute(SimpleAdapter adapter) {

            /** Getting a reference to listview of main.xml layout file */
            final ListView listView1 = ( ListView ) findViewById(R.id.listView1);
            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(MyPage.this, MyPageDetail.class);
                    intent.putExtra("toDetail", listView1.getItemAtPosition(i).toString());
                    startActivity(intent);
                    finish();
                }
            });
            listView1.setAdapter(adapter);
        }
    }

    private class ListViewLoaderTask2 extends AsyncTask<String, Void, SimpleAdapter> {

        JSONObject jObject2;
        /** Doing the parsing of xml data in a non-ui thread */
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected SimpleAdapter doInBackground(String... strJson) {
            try{
                jObject2 = new JSONObject(strJson[0]);
                QuestJSONParser questJsonParser = new QuestJSONParser();
                questJsonParser.parse(jObject2);
            }catch(Exception e){
                Log.d("JSON Exception1",e.toString());
            }

            QuestJSONParser questJsonParser = new QuestJSONParser();

            List<HashMap<String, String>> quests = null;

            try{
                /** Getting the parsed data as a List construct */
                quests = questJsonParser.parse(jObject2);
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }

            /** Keys used in Hashmap */
            String[] from = { "title","where", "reward"};

            /** Ids of views in listview_layout */
            int[] to = { R.id.title,R.id.where, R.id.reward};

            /** Instantiating an adapter to store each items
             *  R.layout.listview_layout defines the layout of each item
             */
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), quests, R.layout.my_page_item2, from, to);

            return adapter;
        }

        /** Invoked by the Android system on "doInBackground" is executed completely */
        /** This will be executed in ui thread */
        @Override
        protected void onPostExecute(SimpleAdapter adapter) {

            final ListView listView2 = ( ListView ) findViewById(R.id.listView2);
            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(MyPage.this, MyPageDetail2.class);
                    intent.putExtra("toDetail", listView2.getItemAtPosition(i).toString());
                    startActivity(intent);
                    finish();
                }
            });
            listView2.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_page, menu);
        return true;
    }









/*

    public static void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>{
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }


        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();

                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            Bitmap resized = Bitmap.createScaledBitmap(logo, 300, 300, true);
            return resized;
        }


        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
    */
}