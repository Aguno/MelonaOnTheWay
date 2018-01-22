package kaist.aguno.melona;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by user on 2018-01-21.
 */

public class MyPage extends Activity{
    private ListView ListView1;
    private ListView ListView2;

    private String[] asiaCountries = {"Vietnam", "China", "Japan", "Korea", "India", "Singapore", "Thailand", "Malaysia"};
    private String[] europeCountries = {"France", "Germany", "Sweden", "Denmark", "England", "Spain", "Portugal", "Norway"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);

        //locate Views
        ListView1 = (ListView) findViewById(R.id.listView1);
        ListView2 = (ListView) findViewById(R.id.listView2);
        ImageView iv = (ImageView) findViewById(R.id.image);
        TextView nickname = (TextView) findViewById(R.id.nickname);
        TextView howmuch = (TextView) findViewById(R.id.how_much);

        String imgURL  = "http://k.kakaocdn.net/dn/bemHHl/btqjxqUnf2z/I5E9xtNzQYK6MMjAzw0rj1/profile_110x110c.jpg";

        //set all Listview adapter
        ListView1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, asiaCountries));
        ListView2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, europeCountries));
        nickname.setText("Hwang");
        howmuch.setText("500");


        //set dynmic height for all listviews
        setDynamicHeight(ListView1);
        setDynamicHeight(ListView2);
        new DownLoadImageTask(iv).execute(imgURL);

    }

    /**
     * Set listview height based on listview children
     *
     * @param listView
     */
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

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            Bitmap resized = Bitmap.createScaledBitmap(logo, 300, 300, true);
            return resized;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}