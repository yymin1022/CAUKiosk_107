package com.cecom.caukiosk;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.io.File;

public class DetailActivity extends BaseActivity{
    String selRoom = "";

    TextView tvInfo;
    TextView tvNum ;
    TextView tvPhone;
    TextView tvTitle ;
    ImageView imgViewDetail;

    FrameLayout.LayoutParams mapLayoutParams;
    FrameLayout.LayoutParams roomLayoutParams;
    ImageView mapImage;
    Intent intent;
    LinearLayout imageLayout;
    LinearLayout infoLayout;
    FrameLayout mapLayout;
    LinearLayout numLayout;
    LinearLayout titleLayout;
    View roomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initialView();

        mapImage = findViewById(R.id.detail_image_map);
        roomView = findViewById(R.id.detail_view_room);
        roomView.setVisibility(View.INVISIBLE)                                                                                                                              ;
        imgViewDetail  = findViewById(R.id.detail_imageView);
        tvInfo = findViewById(R.id.detail_text_info);
        tvNum = findViewById(R.id.detail_text_number);
        tvPhone = findViewById(R.id.detail_text_phone);
        tvTitle = findViewById(R.id.detail_text_title);
        imageLayout = findViewById(R.id.detail_layout_image);
        infoLayout = findViewById  (R.id.detail_layout_info);
        mapLayout = findViewById(R.id.detail_layout_map);
        numLayout = findViewById(R.id.detail_layout_number);
        titleLayout = findViewById(R.id.detail_layout_title);
        mapLayoutParams = (FrameLayout.LayoutParams)mapLayout.getLayoutParams();
        roomLayoutParams = (FrameLayout.LayoutParams)roomView.getLayoutParams();

        imageLayout.setVisibility(View.INVISIBLE);
        infoLayout.setVisibility(View.INVISIBLE);
        numLayout.setVisibility(View.INVISIBLE);
        titleLayout.setVisibility(View.INVISIBLE);

        mapLayoutParams.setMargins(getIntent().getIntExtra("marginLeft", 0)+20, getIntent().getIntExtra("marginTop", 0)-50, 0, 0);
        mapLayoutParams.width = getIntent().getIntExtra("width", 0);
        mapLayoutParams.height = getIntent().getIntExtra("height", 0);
        mapLayout.setLayoutParams(mapLayoutParams);

        selRoom = getIntent().getStringExtra("NUM");
        Log.d("selRoom",selRoom);
        File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "club_image" + File.separator, selRoom+ ".png");
        if(imgFile.exists()){
            Bitmap bm = BitmapFactory.decodeFile(imgFile.getPath());
            imgViewDetail.setImageBitmap(bm);
        }


        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator, "building_107.db");
        SQLiteDatabase sampleDB =  SQLiteDatabase.openOrCreateDatabase(file,  null);
        Cursor c = sampleDB.rawQuery("SELECT * FROM total_desc",null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Log.d("roomd_id",c.getString(c.getColumnIndex("id")).toLowerCase());
                    if(c.getString(c.getColumnIndex("id")).toLowerCase().equals(selRoom.toLowerCase())){
                        //File imgFile = new  File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "img_file" +File.separator, c.getString(c.getColumnIndex("img_url")));
                        //if(imgFile.exists()){
                        //    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        //    imgViewDetail.setImageBitmap(myBitmap);
                        //}
                       tvInfo.setText(c.getString(c.getColumnIndex("room_desc")));
                       if(c.getString(c.getColumnIndex("id")).toLowerCase().startsWith("b")){
                           String temp = "나동"+c.getString(c.getColumnIndex("id")).substring(1);
                           tvNum.setText(temp);
                       }
                       else{
                           tvNum.setText(c.getString(c.getColumnIndex("id")));
                       }
                       tvPhone.setText(c.getString(c.getColumnIndex("phone_num")));
                       tvTitle.setText(c.getString(c.getColumnIndex("name")));
                    }
                } while (c.moveToNext());
            }
        }

        sampleDB.close();
        updateFloorButton(selRoom);
        setUpAdmin();
    }

    void initLeftPanel(){
        imageLayout.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.VISIBLE);
        numLayout.setVisibility(View.VISIBLE);
        titleLayout.setVisibility(View.VISIBLE);

        Typeface typefaceSC = Typeface.createFromAsset(getAssets(), "fonts/SCDreamEB.otf");
        tvNum.setTypeface(typefaceSC);
        tvPhone.setTypeface(typefaceSC);
        tvTitle.setTypeface(typefaceSC);

        Typeface typefaceNG = Typeface.createFromAsset(getAssets(), "fonts/NanumBarunGothicR.otf");
        tvInfo.setTypeface(typefaceNG);
    }

    void updateFloorButton(String room){
        Button btnFloor = findViewById(R.id.main_btn_floor_1);
        Button btnFloorDong = findViewById(R.id.main_btn_ga_dong);
        intent = new Intent(getApplicationContext(), FloorActivity.class);
        Handler mHandler = new Handler();
        if(room.toLowerCase().startsWith("b")){
            switch(room.substring(1, 2)){
                case "1":
                    Log.d("input_b","b");
                    if(room.substring(1, 2).equals("1")){
                        Log.d("input_b1","b1");
                        btnFloor = findViewById(R.id.main_btn_floor_b1);
                        btnFloorDong = findViewById(R.id.main_btn_na_dong);
                        mapImage.setImageResource(R.drawable.img_floor_b1);
                        intent.putExtra("Floor", "B1");
                        mapLayoutParams.setMargins(getIntent().getIntExtra("marginLeft", 0), getIntent().getIntExtra("marginTop", 0), 0, 0);
                        mHandler.postDelayed(new Runnable()  {
                            public void run() {
                                runMapAnimationB1();
                            }
                        }, 500); // 0.5초후
                    }
                    break;
                case "2":
                    Log.d("input_b","b");
                    if(room.substring(1, 2).equals("2")){
                        Log.d("input_b2","b2");
                        btnFloor = findViewById(R.id.main_btn_floor_b2);
                        btnFloorDong = findViewById(R.id.main_btn_na_dong);
                        mapImage.setImageResource(R.drawable.img_floor_b2);
                        intent.putExtra("Floor", "B2");
                        mapLayoutParams.setMargins(getIntent().getIntExtra("marginLeft", 0), getIntent().getIntExtra("marginTop", 0), 0, 0);
                        mHandler.postDelayed(new Runnable()  {
                            public void run() {
                                runMapAnimationB1();
                            }
                        }, 500); // 0.5초후
                    }
                    break;
            }
        }
        else{
            Log.d("room_number",room);
            if(room.length() == 3 || room.length() == 5 || room.length() == 4|| room.length() == 6){
                switch(room.substring(0, 1).toLowerCase()) {
                    case "1":
                        btnFloor = findViewById(R.id.main_btn_floor_1);
                        btnFloorDong = findViewById(R.id.main_btn_ga_dong);
                        mapImage.setImageResource(R.drawable.img_floor_1);
                        intent.putExtra("Floor", "1");
                        mapLayoutParams.setMargins(getIntent().getIntExtra("marginLeft", 0), getIntent().getIntExtra("marginTop", 0), 0, 0);
                        mHandler.postDelayed(new Runnable()  {
                            public void run() {
                                runMapAnimationDefault();
                            }
                        }, 500); // 0.5초후
                        break;
                    case "2":
                        btnFloor = findViewById(R.id.main_btn_floor_2);
                        btnFloorDong = findViewById(R.id.main_btn_ga_dong);
                        mapImage.setImageResource(R.drawable.img_floor_2);
                        intent.putExtra("Floor", "2");
                        mapLayoutParams.setMargins(getIntent().getIntExtra("marginLeft", 0), getIntent().getIntExtra("marginTop", 0), 0, 0);
                        mHandler.postDelayed(new Runnable()  {
                            public void run() {
                                runMapAnimationDefault();
                            }
                        }, 500); // 0.5초후
                        break;
                    case "3":
                        btnFloor = findViewById(R.id.main_btn_floor_3);
                        btnFloorDong = findViewById(R.id.main_btn_ga_dong);
                        mapImage.setImageResource(R.drawable.img_floor_3);
                        intent.putExtra("Floor", "3");
                        mapLayoutParams.setMargins(getIntent().getIntExtra("marginLeft", 0), getIntent().getIntExtra("marginTop", 0), 0, 0);
                        mHandler.postDelayed(new Runnable()  {
                            public void run() {
                                runMapAnimation3F();
                            }
                        }, 500); // 0.5초후
                        break;
                    case "4":
                        btnFloor = findViewById(R.id.main_btn_floor_4);
                        btnFloorDong = findViewById(R.id.main_btn_ga_dong);
                        mapImage.setImageResource(R.drawable.img_floor_4);
                        intent.putExtra("Floor", "4");
                        mapLayoutParams.setMargins(getIntent().getIntExtra("marginLeft", 0), getIntent().getIntExtra("marginTop", 0), 0, 0);
                        mHandler.postDelayed(new Runnable()  {
                            public void run() {
                                runMapAnimation3F();
                            }
                        }, 700); // 0.5초후
                        break;
                    case "5":
                        btnFloor = findViewById(R.id.main_btn_floor_5);
                        btnFloorDong = findViewById(R.id.main_btn_ga_dong);
                        mapImage.setImageResource(R.drawable.img_floor_5);
                        intent.putExtra("Floor", "5");
                        mapLayoutParams.setMargins(getIntent().getIntExtra("marginLeft", 0), getIntent().getIntExtra("marginTop", 0), 0, 0);
                        mHandler.postDelayed(new Runnable()  {
                            public void run() {
                                runMapAnimation3F();
                            }
                        }, 500); // 0.5초후
                        break;
                    case "6":
                        btnFloor = findViewById(R.id.main_btn_floor_6);
                        btnFloorDong = findViewById(R.id.main_btn_ga_dong);
                        mapImage.setImageResource(R.drawable.img_floor_6);
                        intent.putExtra("Floor", "6");
                        mapLayoutParams.setMargins(getIntent().getIntExtra("marginLeft", 0), getIntent().getIntExtra("marginTop", 0), 0, 0);
                        mHandler.postDelayed(new Runnable()  {
                            public void run() {
                                runMapAnimation3F();
                            }
                        }, 500); // 0.5초후
                        break;

                }
            }

        }

        btnFloor.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_main_floor_pressed));
        btnFloorDong.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_main_floor_pressed));
        Button btnReturn = findViewById(R.id.detail_btn_return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                finish();
            }
        });
    }





    void runMapAnimationDefault(){
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                mapLayout.animate()
                        .scaleX((float) (500.0/ getIntent().getIntExtra("width", 0)))
                        .scaleY((float) (150.0/ getIntent().getIntExtra("height", 0)))
                        .setDuration(400);
            }
        }, 80); // 0.5초후

        float startLeft = mapLayout.getLeft();
        float startTop = mapLayout.getTop();
        for(int i = 0; i < 500; i++){
            final float tempSL = startLeft;
            final float tempST = startTop;

            mHandler.postDelayed(new Runnable()  {
                public void run() {
                    TranslateAnimation translateAnimation = new TranslateAnimation(
                            tempSL, tempSL,
                            tempST, tempST);

                    translateAnimation.setDuration(10);
                    translateAnimation.setFillAfter(true);
                    mapLayout.startAnimation(translateAnimation);
                }
            }, (int)(100+0.8*(i+1))); // 0.5초후
            startLeft += 0.75;
            startTop += 0.4;
        }

        /*mapLayout.animate()
                .scaleX((float) (510.0/ getIntent().getIntExtra("width", 0)))
                .scaleY((float) (150.0/ getIntent().getIntExtra("height", 0)));
*/

        /*
        for(int i = 0; i < 10; i++){
            mapLayout.animate()
                    .scaleX((float) (50*i/ getIntent().getIntExtra("width", 0)))
                    .scaleY((float) (20*i/ getIntent().getIntExtra("height", 0)));
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable()  {
                public void run() {
                    // 시간 지난 후 실행할 코딩
                }
            }, 1000); // 0.5초후
        }

        int startLeft = getIntent().getIntExtra("marginLeft",0);
        int startTop = getIntent().getIntExtra("marginTop", 0);
        for(int i = 0; i < 20; i++){


            TranslateAnimation translateAnimation = new TranslateAnimation(
                    startLeft, startLeft+8,
                    startTop, startTop+5);
            startLeft += 8;
            startTop += 5;
            translateAnimation.setDuration(1000);
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable()  {
                public void run() {
                    // 시간 지난 후 실행할 코딩
                }
            }, 1000); // 0.5초후
            translateAnimation.setFillAfter(true);
            mapLayout.startAnimation(translateAnimation);
        }
*/

        roomLayoutParams.setMargins(getIntent().getIntExtra("btnMarginLeft", 0)-147, getIntent().getIntExtra("btnMarginTop", 0)- 277, 0, 0);
        roomLayoutParams.width = getIntent().getIntExtra("btnWidth", 0);
        roomLayoutParams.height = getIntent().getIntExtra("btnHeight", 0);
        roomView.setLayoutParams(roomLayoutParams);
        roomView.setRotation(getIntent().getFloatExtra("btnRotation", 0));
        roomView.setVisibility(View.VISIBLE);

        initLeftPanel();
    }

    void runMapAnimationB1(){
        Handler mHandler = new Handler();

        float startLeft = mapLayout.getLeft();
        float startTop = mapLayout.getTop();
        for(int i = 0; i < 500; i++){
            final float tempSL = startLeft-300f;
            final float tempST = startTop;

            mHandler.postDelayed(new Runnable()  {
                public void run() {
                    TranslateAnimation translateAnimation = new TranslateAnimation(
                            tempSL, tempSL,
                            tempST, tempST);

                    translateAnimation.setDuration(10);
                    translateAnimation.setFillAfter(true);
                    mapLayout.startAnimation(translateAnimation);
                }
            }, (int)(100+0.8*(i+1))); // 0.5초후
            startLeft += 0.3;
            startTop += 0.5;
        }

        roomLayoutParams.setMargins(getIntent().getIntExtra("btnMarginLeft", 0)-633, getIntent().getIntExtra("btnMarginTop", 0)- 290, 0, 0);
        Log.d("where",String.valueOf(getIntent().getIntExtra("btnMarginLeft", 0)));
        roomLayoutParams.width = getIntent().getIntExtra("btnWidth", 0);
        roomLayoutParams.height = getIntent().getIntExtra("btnHeight", 0);
        roomView.setLayoutParams(roomLayoutParams);
        roomView.setRotation(getIntent().getFloatExtra("btnRotation", 0));
        roomView.setVisibility(View.VISIBLE);

        initLeftPanel();
    }

    void runMapAnimation3F(){
       Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                mapLayout.animate()
                        .scaleX((float) (510.0/ getIntent().getIntExtra("width", 0)))
                        .scaleY((float) (150.0/ getIntent().getIntExtra("height", 0)))
                        .setDuration(400);
            }
        }, 80); // 0.5초후

        float startLeft = mapLayout.getLeft();
        float startTop = mapLayout.getTop();
        for(int i = 0; i < 500; i++){
            final float tempSL = startLeft;
            final float tempST = startTop;

            mHandler.postDelayed(new Runnable()  {
                public void run() {
                    TranslateAnimation translateAnimation = new TranslateAnimation(
                            tempSL, tempSL,
                            tempST, tempST);

                    translateAnimation.setDuration(10);
                    translateAnimation.setFillAfter(true);
                    mapLayout.startAnimation(translateAnimation);
                }
            }, (int)(100+0.8*(i+1))); // 0.5초후
            startLeft += 0.55;
            startTop += 0.5;
        }

        /*mapLayout.animate()
                .scaleX((float) (510.0/ getIntent().getIntExtra("width", 0)))
                .scaleY((float) (150.0/ getIntent().getIntExtra("height", 0)));
*/

        /*
        for(int i = 0; i < 10; i++){
            mapLayout.animate()
                    .scaleX((float) (50*i/ getIntent().getIntExtra("width", 0)))
                    .scaleY((float) (20*i/ getIntent().getIntExtra("height", 0)));
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable()  {
                public void run() {
                    // 시간 지난 후 실행할 코딩
                }
            }, 1000); // 0.5초후
        }

        int startLeft = getIntent().getIntExtra("marginLeft",0);
        int startTop = getIntent().getIntExtra("marginTop", 0);
        for(int i = 0; i < 20; i++){


            TranslateAnimation translateAnimation = new TranslateAnimation(
                    startLeft, startLeft+8,
                    startTop, startTop+5);
            startLeft += 8;
            startTop += 5;
            translateAnimation.setDuration(1000);
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable()  {
                public void run() {
                    // 시간 지난 후 실행할 코딩
                }
            }, 1000); // 0.5초후
            translateAnimation.setFillAfter(true);
            mapLayout.startAnimation(translateAnimation);
        }
*/

        roomLayoutParams.setMargins(getIntent().getIntExtra("btnMarginLeft", 0)-215, getIntent().getIntExtra("btnMarginTop", 0)- 277, 0, 0);
        roomLayoutParams.width = getIntent().getIntExtra("btnWidth", 0);
        roomLayoutParams.height = getIntent().getIntExtra("btnHeight", 0);
        roomView.setLayoutParams(roomLayoutParams);
        roomView.setRotation(getIntent().getFloatExtra("btnRotation", 0));
        roomView.setVisibility(View.VISIBLE);

        initLeftPanel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(KioskModeApp.isInLockMode == true){
            ActivityManager activityManager = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.moveTaskToFront(getTaskId(), 0);
        }
    }

}