package com.shoppi.alarm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shoppi.alarm.Moon.WheaterActivity;
import com.shoppi.alarm.db.Alarm;
import com.shoppi.alarm.db.AlarmDao;
import com.shoppi.alarm.db.AlarmDatabase;
import com.shoppi.roomdatabase_sample.R;

import java.util.List;

public class Maintest_Activity extends AppCompatActivity {
    private Button set_button;

    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private Paint p = new Paint();
    private AlarmDatabase db;
  //  private AlarmDao dao;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        기존 코드, 상속받은 AppCompatActivity이외의 코드도 동작함을 의미
//        이 코드를 생략하면 이후로 작성된 코드만 동작함을 의미
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alam_main);
        WheaterActivity.InitContext(this);

        WheaterActivity w = new WheaterActivity();
        w.Start();

        set_button=   (Button)findViewById(R.id.alam_plus_btn);
        recyclerView = (RecyclerView) findViewById(R.id.rv_view);

        //플러스 버튼 클릭시
        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        db = AlarmDatabase.getDatabase(this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(db);
        recyclerView.setAdapter(adapter);

        initSwipe();

        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨), 없으면 recyclerview 안 뜸
        db.alarmDao().getAll().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> data) {
                adapter.setItem(data);
            }
        });
    }

    //스와이프로 삭제
    private void initSwipe() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT /* | ItemTouchHelper.RIGHT */)

        {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                //왼쪽으로 밀었을 때
                if (direction == ItemTouchHelper.LEFT) {

                    //dao를 통해 item을 delete
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            db.alarmDao().Delete(adapter.getItems().get(position));
                        }

                    }).start();

                } else {
                    //오른쪽으로 밀었을때, 알람 전화연결관련기능 만들까 생각중
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        //오른쪽으로 밀었을 때

                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        /*
                         * icon 추가할 수 있음.
                         */
                        //icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_png); //vector 불가!
                        // RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        //c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);





    }


}
