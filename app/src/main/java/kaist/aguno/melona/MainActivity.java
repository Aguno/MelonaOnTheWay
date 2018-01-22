package kaist.aguno.melona;
import android.bluetooth.le.AdvertiseData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
//MARK: Global Variables
    //Buttons
    Button west_dorm,west_gate,north_dorm,creative_building,humanities;
    Button sort_starting_point, sort_destinationi;
    ImageButton toMinas;
    ImageButton add_request;
    //Kakao ID
    String kakaoID,kakaNickname,kakaoThumbnail,kakaoProfilePicture;

    String starting_humanities,starting_north_dorm, starting_west_dorm,starting_west_gate,starting_creative_building;
    String destination_humanities,destination_north_dorm,destination_west_dorm,destination_west_gate,destination_creative_building;
    String uploaded_quest_pending,uploaded_quest_matched,accepted_quest;

    //Boolean and int for sorting choice
    boolean start=true;
    boolean matched = true;
    //get_type
    //0: normal, 1: uploaded quest list, 2: accepted quest list
    int get_type=0;



    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        //get Kaokao ID from Shared Preferences
        SharedPreferences prefs = getSharedPreferences("kakaoID",MODE_PRIVATE);
        kakaoID = prefs.getString("kakaoID",null);
        kakaNickname = prefs.getString("kakaoNickname",null);
        kakaoThumbnail = prefs.getString("kakaoProfileThumbnail",null);
        kakaoProfilePicture = prefs.getString("kakaoProfilePicture",null);
        Log.d("check for kakao ID in main activity",kakaoID);
        //Button for each Location
        west_dorm = (Button) findViewById(R.id.west_dorm);
        west_gate = (Button) findViewById(R.id.west_gate);
        north_dorm = (Button) findViewById(R.id.north_dorm);
        creative_building = (Button) findViewById(R.id.creative_building);
        humanities = (Button) findViewById(R.id.humanities);
        //Test Minas
        toMinas = (ImageButton)findViewById(R.id.toMinas);

        //Button for sorting
        sort_starting_point = (Button) findViewById(R.id.starting_point);
        sort_destinationi = (Button) findViewById(R.id.destination);

        //Button for Requesting new job
        add_request = (ImageButton) findViewById(R.id.add_request);

        //run Sort_starting_point on default\
        //new getQuest(true,"인사동",0,true).execute("http://143.248.132.156:8080/api/quest");
        //new getQuest(true,"북측기숙사",0,true).execute("http://143.248.132.156:8080/api/quest");
        //new getQuest(true,"서측기숙사",0,true).execute("http://143.248.132.156:8080/api/quest");
        //new getQuest(true,"쪽문",0,true).execute("http://143.248.132.156:8080/api/quest");
        //new getQuest(true,"창의관",0,true).execute("http://143.248.132.156:8080/api/quest");
        //new getQuest(false,"인사동",0,true).execute("http://143.248.132.156:8080/api/quest");
        //new getQuest(false,"북측기숙사",0,true).execute("http://143.248.132.156:8080/api/quest");
        //new getQuest(false,"서측",0,true).execute("http://143.248.132.156:8080/api/quest");
        //new getQuest(false,"쪽문",0,true).execute("http://143.248.132.156:8080/api/quest");
        //new getQuest(false,"창의관",0,true).execute("http://143.248.132.156:8080/api/quest");

        //Get profile info on default
        new getQuest(true,"인사동",1,true).execute("http://143.248.132.156:8080/api/quest");
        new getQuest(true,"북측기숙사",1,false).execute("http://143.248.132.156:8080/api/quest");
        new getQuest(true,"서측기숙사",2,true).execute("http://143.248.132.156:8080/api/quest");


        sort_starting_point.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                new getQuest(true,"인사동",0,true).execute("http://143.248.132.156:8080/api/quest");
                new getQuest(true,"북측기숙사",0,true).execute("http://143.248.132.156:8080/api/quest");
                new getQuest(true,"서측기숙사",0,true).execute("http://143.248.132.156:8080/api/quest");
                new getQuest(true,"쪽문",0,true).execute("http://143.248.132.156:8080/api/quest");
                new getQuest(true,"창의관",0,true).execute("http://143.248.132.156:8080/api/quest");

            }

        });

        sort_destinationi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //Sort by destination
                new getQuest(false,"인사동",0,true).execute("http://143.248.132.156:8080/api/quest");
                new getQuest(false,"북측기숙사",0,true).execute("http://143.248.132.156:8080/api/quest");
                new getQuest(false,"서측",0,true).execute("http://143.248.132.156:8080/api/quest");
                new getQuest(false,"쪽문",0,true).execute("http://143.248.132.156:8080/api/quest");
                new getQuest(false,"창의관",0,true).execute("http://143.248.132.156:8080/api/quest");
            }
        });

        //Buttons for accessing quest  :   west_dorm,west_gate,north_dorm,creative_building,humanities;

        north_dorm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this,QuestList.class);
                if(start) {
                    i.putExtra("strJSON", starting_north_dorm);
                }
                else{
                    i.putExtra("strJSON", destination_north_dorm);
                }
                startActivity(i);
            }
        });

        west_dorm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this,QuestList.class);
                if(start) {
                    i.putExtra("strJSON", starting_west_dorm);
                }
                else{
                    i.putExtra("strJSON", destination_west_dorm);
                }
                startActivity(i);
            }
        });
        west_gate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this,QuestList.class);
                if(start) {
                    i.putExtra("strJSON", starting_west_gate);
                }
                else{
                    i.putExtra("strJSON", destination_west_gate);
                }
                startActivity(i);
            }
        });
        humanities.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this,QuestList.class);
                if(start) {
                    i.putExtra("strJSON", starting_humanities);
                }
                else{
                    i.putExtra("strJSON", destination_humanities);
                }
                startActivity(i);
            }
        });

        creative_building.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this,QuestList.class);
                if(start) {
                    i.putExtra("strJSON", starting_creative_building);
                }
                else{
                    i.putExtra("strJSON", destination_creative_building);
                }
                startActivity(i);
            }
        });


        add_request.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Intent intent = new Intent(MainActivity.this, AddQuest.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }

        });

        toMinas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Intent intent = new Intent(MainActivity.this, MyPage.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                //startActivity(intent);
                //finish();
                intent.putExtra("strJSON", destination_creative_building);
                intent.putExtra("acceptedQuest",accepted_quest);
                intent.putExtra("uploadedQuestPending",uploaded_quest_pending);
                intent.putExtra("uploadedQuestMatched",uploaded_quest_matched);
                startActivity(intent);
                finish();
            }

        });
    }

    public class getQuest extends AsyncTask<String, String, String> {

        String location_name = "";

        public getQuest(boolean starting_point, String location,int get_type_input,boolean matched_input)
        {
            get_type = get_type_input;
            matched = matched_input;
            if(starting_point){
                start=  true;
                location_name = location;
            }
            else{
                start = false;
                location_name = location;
            }
        }

        @Override
        protected String doInBackground(String... urls) {
            try {

                HttpURLConnection con = null;
                BufferedReader reader = null;
                String location_string;
                try{
                    if(start){
                        location_string = "startPoint";
                    }
                    else{
                        location_string = "destination";
                    }
                    if(get_type ==0) {
                        urls[0] = urls[0] + "?" + location_string + "=" + location_name + "&state=1";
                    }
                    //uploaded quest request
                    else if(get_type==1) {
                        if (matched) {
                            urls[0] = urls[0] + "?state=2&from=" + kakaoID;
                        }
                        else {
                            urls[0] = urls[0] + "?state=1&from=" + kakaoID;
                        }

                    //accepted quest request
                    }
                    else if (get_type==2){
                        urls[0] = urls[0] + "?state=2&to="+kakaoID;
                    }
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.connect();//연결 수행

                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();

                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳
                    StringBuffer buffer = new StringBuffer();

                    //line별 스트링을 받기 위한 temp 변수
                    String line = "";

                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까
                    return buffer.toString();

                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        //버퍼를 닫아준다.
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//finally 부분
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try{
                if(get_type ==0) {
                    if (start) {
                        if (location_name == "북측기숙사") {
                            starting_north_dorm = result;
                            north_dorm.setText(Integer.toString(new JSONArray(starting_north_dorm).length()));
                        } else if (location_name == "인사동") {
                            starting_humanities = result;
                            humanities.setText(Integer.toString(new JSONArray(starting_humanities).length()));
                        } else if (location_name == "서측기숙사") {
                            starting_west_dorm = result;
                            west_dorm.setText(Integer.toString(new JSONArray(starting_west_dorm).length()));
                        } else if (location_name == "쪽문") {
                            starting_west_gate = result;
                            west_gate.setText(Integer.toString(new JSONArray(starting_west_gate).length()));
                        } else if (location_name == "창의관") {
                            starting_creative_building = result;
                            creative_building.setText(Integer.toString(new JSONArray(starting_creative_building).length()));
                        }
                    } else {
                        if (location_name == "북측기숙사") {
                            destination_north_dorm = result;
                            north_dorm.setText(Integer.toString(new JSONArray(destination_north_dorm).length()));
                        } else if (location_name == "인사동") {
                            destination_humanities = result;
                            humanities.setText(Integer.toString(new JSONArray(destination_humanities).length()));
                        } else if (location_name == "서측기숙사") {
                            destination_west_dorm = result;
                            west_dorm.setText(Integer.toString(new JSONArray(destination_west_dorm).length()));
                        } else if (location_name == "쪽문") {
                            destination_west_gate = result;
                            west_gate.setText(Integer.toString(new JSONArray(destination_west_gate).length()));
                        } else if (location_name == "창의관") {
                            destination_creative_building = result;
                            creative_building.setText(Integer.toString(new JSONArray(destination_creative_building).length()));
                        }
                    }
                }
                else if(get_type==1){
                    if(matched){
                        uploaded_quest_matched = result;
                        Log.d("Check main activity for quest get",uploaded_quest_matched);
                    }
                    else
                    {
                        uploaded_quest_pending = result;
                        Log.d("Check main activity for quest get",uploaded_quest_pending);
                    }
                }
                else if(get_type ==2)
                {
                    accepted_quest = result;
                    Log.d("Check main activity for quest get",accepted_quest);
                }
                Log.d("Check main activity for quest get",result);
            }
            catch(JSONException e) {
                return;
            }
        }


    }
}
