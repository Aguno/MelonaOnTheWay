package kaist.aguno.melona;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 2018-01-20.
 */

public class Report extends Activity {
    //XML related material
    EditText title_editor,details_editor,contact_editor;
    Button ok,no;
    ArrayAdapter<CharSequence> adapter;
    //Alert Dialogs
    private AlertDialog.Builder builder;
    private AlertDialog popup;
    //
    String title,details, contact;
    String my_kakao_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        title_editor = (EditText)findViewById(R.id.title_editor);
        //starting_point_editor = (EditText)findViewById(R.id.start_editor);
        //destination_editor = (EditText)findViewById(R.id.destination_editor);
        details_editor = (EditText)findViewById(R.id.details_editor);
        contact_editor = (EditText)findViewById(R.id.contact_editor);


        ok = findViewById(R.id.ok_editor);
        no = findViewById(R.id.no_editor);

        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                title= title_editor.getText().toString();
                //starting_point = starting_point_editor.getText().toString();
                //destination= destination_editor.getText().toString();
                details = details_editor.getText().toString();
                contact = contact_editor.getText().toString();

                builder = new AlertDialog.Builder(Report.this);
                View mView = getLayoutInflater().inflate(R.layout.report_quest_alert,null);
                Button yesButton = mView.findViewById(R.id.yes);
                Button noButton = mView.findViewById(R.id.no);
                builder.setView(mView);
                popup = builder.create();
                popup.show();

                yesButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        new Report.postAccount().execute("http://143.248.36.249:8080/api/report");
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
        no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Report.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        }

    public class postAccount extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                SharedPreferences prefs = getSharedPreferences("kakaoID",MODE_PRIVATE);
                my_kakao_ID = prefs.getString("kakaoID",null);
                jsonObject.accumulate("title",title);
                jsonObject.accumulate("from", my_kakao_ID);
                jsonObject.accumulate("text",details);
                jsonObject.accumulate("from", contact);
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");//POST방식으로 보냄
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
}
