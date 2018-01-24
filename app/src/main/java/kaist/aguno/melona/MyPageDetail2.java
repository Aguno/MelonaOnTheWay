package kaist.aguno.melona;

/**
 * Created by user on 2018-01-22.
 */


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyPageDetail2 extends AppCompatActivity {

    TextView title;
    TextView where;
    TextView text;
    TextView reward;
    TextView tag;
    TextView state;
    String data;
    String test, test2;
    String[] array;
    String[] title_array;
    String[] title_array2;
    String[] reward_array;
    String[] text_array;
    String[] where_array;
    String[] tag_array;
    String[] _id_array;

    String kakaoID,kakaNickname,kakaoThumbnail,kakaoProfilePicture;

    AlertDialog.Builder builder;
    AlertDialog popup;
    String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page_detail);
        SharedPreferences prefs = getSharedPreferences("kakaoID",MODE_PRIVATE);
        kakaoID = prefs.getString("kakaoID",null);
        kakaNickname = prefs.getString("kakaoNickname",null);
        kakaoThumbnail = prefs.getString("kakaoProfileThumbnail",null);
        kakaoProfilePicture = prefs.getString("kakaoProfilePicture",null);


        title = (TextView) findViewById(R.id.detailTitle);
        where = (TextView) findViewById(R.id.detailWhere);
        text = (TextView) findViewById(R.id.detailText);
        reward = (TextView) findViewById(R.id.detailReward);
        tag = (TextView) findViewById(R.id.detailHashtag);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            data = bundle.getString("toDetail");
            array = data.split(", ");
            test = array[5];
            title_array=test.split("=");
            title.setText(title_array[1]);

            test = array[1];
            where_array=test.split("=");
            where.setText(where_array[1]);

            test = array[0];
            text_array= test.split("=");
            if(text_array.length==1)
                text.setText("퀘스트 설명이 없습니다.");
            else{
                text.setText(text_array[1]);
            }

            test = array[6];
            reward_array=test.split("=");
            test2 = reward_array[1];
            title_array2 = test2.split("\\}");
            reward.setText(title_array2[0]);


            test = array[4];
            tag_array=test.split("=");
            tag.setText(tag_array[1]);

            test = array[3];
            _id_array = test.split("=");
            _id = _id_array[1];
        }

        /*detail창에서 포기버튼용*/
        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                builder = new AlertDialog.Builder(MyPageDetail2.this);
                View mView = getLayoutInflater().inflate(R.layout.giveup_quest_alert,null);
                Button yesButton = mView.findViewById(R.id.yes);
                Button noButton = mView.findViewById(R.id.no);
                builder.setView(mView);
                popup = builder.create();
                popup.show();
                yesButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        // new MyPageDetail.putQuest().execute("http://143.248.36.249:8080/api/withdraw");
                        new giveUp(_id).execute("http://143.248.36.249:8080/api/giveup");
                        popup.cancel();
                        finish();

                    }
                });
                noButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        popup.cancel();
                    }
                });

            }
        });
         /*detail창에서 완료버튼용*/
        Button fab2 = (Button) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                builder = new AlertDialog.Builder(MyPageDetail2.this);
                View mView = getLayoutInflater().inflate(R.layout.get_reward_quest_alert,null);
                Button yesButton = mView.findViewById(R.id.yes);
                Button noButton = mView.findViewById(R.id.no);
                builder.setView(mView);
                popup = builder.create();
                popup.show();
                yesButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        // new MyPageDetail.putQuest().execute("http://143.248.36.249:8080/api/withdraw");
                        new MyPageDetail2.giveUp(_id).execute("http://143.248.36.249:8080/api/reward");
                        popup.cancel();
                        finish();

                    }
                });
                noButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        popup.cancel();
                    }
                });

            }
        });
    }
    /*putQuest 함수 - 퀘스트 수락을 서버에 보내기*/
    public class putQuest extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                SharedPreferences prefs = getSharedPreferences("kakaoID",MODE_PRIVATE);
                //my_kakao_ID = prefs.getString("kakaoID",null);
                jsonObject.accumulate("questId", "5a64369289bbe62c2465971a");
                jsonObject.accumulate("accountId",kakaoID);
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("PUT");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    return buffer.toString();

                    //서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("check result",result);
        }
    }

    public class giveUp extends AsyncTask<String, String, String> {
        String QuestID;

        public giveUp(String QuestID_input) {
            QuestID = QuestID_input;
        }


        @Override
        protected String doInBackground(String... urls) {
            try {

                HttpURLConnection con = null;
                BufferedReader reader = null;
                JSONObject quest = new JSONObject();
                quest.accumulate("questId",QuestID);
                quest.accumulate("accountId",kakaoID);

                try {

                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("PUT");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();

                    //        int length = temparray.length();

                    //             for(int i=0;i<length;i++) {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(quest.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌


                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }


                    return buffer.toString();

                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        //버퍼를 닫아준다.
                        if (reader != null) {
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
            Log.e("check give up result", result);

        }
    }
}