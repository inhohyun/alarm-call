package com.shoppi.alarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shoppi.alarm.db.Alarm;
import com.shoppi.alarm.db.AlarmDatabase;
import com.shoppi.roomdatabase_sample.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
/*
  onBind()를 이용해 ViewHolder에 item을 bind해주고, 현재 position도 따로 저장
  -> editData()메서드로 데이터 수정하기 위함
  Thread를 통해 db에 접근해야하고, dao를 통해 db에 접근해 data update를 함
*/
    private List<Alarm> items = new ArrayList<>();
    private Context mTinme;
    private AlarmDatabase db;

    public RecyclerAdapter(AlarmDatabase db) {
        this.db = db;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Alarm> getItems() {return items;}

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alarm_item, viewGroup, false);
        mTinme = viewGroup.getContext();
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int position) {

        viewHolder.onBind(items.get(position),position);

    }



    public class ViewHolder extends RecyclerView.ViewHolder {



        private TextView alarm_time;
        private int index;

        public ViewHolder(View view) {
            super(view);

            alarm_time = view.findViewById(R.id.alarm_time);



        }

        //데이터베이스에 저장된 정보를 text에 bind
        public void onBind(Alarm alarm, int position){
            index = position;
            alarm_time.setText(alarm.getHour()+":"+alarm.getMinute());
        }

       // 데이터 편집, 보류
        public void editData(int Hour, int Minute){
            new Thread(() -> {
                items.get(index).setHour(Hour);
                items.get(index).setMinute(Minute);
                db.alarmDao().Update(items.get(index));
            }).start();
        }


    }

    //일단 ㅇㅋ
    public void setItem(List<Alarm> data) {
        items = data;
        notifyDataSetChanged();
    }

}
