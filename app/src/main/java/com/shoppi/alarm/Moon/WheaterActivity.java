package com.shoppi.alarm.Moon;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shoppi.roomdatabase_sample.R;

import java.nio.channels.AsynchronousChannelGroup;

public class WheaterActivity{

    static Context mMain;
    private TextView tv_outPut;

    public static void InitContext(Context main){
        mMain = main;
    }

    public void Start(){
        tv_outPut = ((TextView)((Activity)mMain).findViewById(R.id.fastest_alam_text));

        //URL 설정
        String service_key = "T%2F5A5PGTWGyFuPpvVBjhohVeH5fUbfrfJcFQF%2BMO1cxY%2FsfWii%2FHvDgMODMKnQPK8hgRNlwTSgwXWeYAYzSkfw%3D%3D";
        String num_of_rows = "10";
        String page_no = "1";
        String date_type = "JSON";
        String base_date = "20210628";
        String base_time = "0630";
        String nx = "55";
        String ny = "127";

        final String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0"+
                "serviceKey="+service_key+
                "&numOfRows="+num_of_rows+
                "&pageNo="+page_no+
                "&dataType="+date_type+
                "&base_date="+base_date+
                "&base_time="+base_time+
                "&nx="+nx+
                "&ny="+ny;

        // AsyncTask를 통해 HttpURLConnection을 수행
        NetworkTask networkTask = new NetworkTask(url, null);
        networkTask.execute();
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params){
            String result;
            //result안에 어떤 값이 담겨야 할까?
            RequestHttpConnection requestHttpConnection = new RequestHttpConnection();
            result = requestHttpConnection.request(url, values);

            //여기서 반환된 값이 onPostExecute로 전달
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            tv_outPut.setText( result);
            Log.d("onPostEx", "출력 값: "+result);
        }
    }
}