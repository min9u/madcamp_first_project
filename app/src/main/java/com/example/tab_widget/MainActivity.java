package com.example.tab_widget;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    DatePicker datePicker;  //  datePicker - 날짜를 선택하는 달력
    TextView viewDatePick;  //  viewDatePick - 선택한 날짜를 보여주는 textView
    EditText edtDiary;   //  edtDiary - 선택한 날짜의 일기를 쓰거나 기존에 저장된 일기가 있다면 보여주고 수정하는 영역
    Button btnSave;   //  btnSave - 선택한 날짜의 일기 저장 및 수정(덮어쓰기) 버튼

    String fileName;   //  fileName - 돌고 도는 선택된 날짜의 파일 이름

    //dialog in tab2
    View dialogView;
    EditText dlgEdtName;

    private final int PERMISSIONS_READ_CONTACTS = 1000;
    private final int PERMISSIONS_READ_EXTERNAL_STORAGE = 1001;
    private final int PERMISSIONS_ACCESS_MEDIA_LOCATION = 1002;
    private boolean isPermission = false;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mLayoutManager;

    Button button1;
    Button button2;

    ArrayList<Item> items = new ArrayList<>();

    private InputMethodManager imm;

    private TextView tmp_name;
    private TextView tmp_phone;
    private ContactListAdapter adapter;
    private ArrayList<ContactItem> contactitems = null;
    private ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Drawable dog = getResources().getDrawable(R.drawable.dog6);
        Bitmap bitmap = ((BitmapDrawable)dog).getBitmap();
        items.add(new Item(bitmap, "dog"));
        Drawable dog1 = getResources().getDrawable(R.drawable.dog4);
        Bitmap bitmap1 = ((BitmapDrawable)dog1).getBitmap();
        items.add(new Item(bitmap1, "dog"));
        Drawable dog2 = getResources().getDrawable(R.drawable.dog3);
        Bitmap bitmap2 = ((BitmapDrawable)dog2).getBitmap();
        items.add(new Item(bitmap2, "dog"));
        Drawable dog3 = getResources().getDrawable(R.drawable.dog7);
        Bitmap bitmap3 = ((BitmapDrawable)dog3).getBitmap();
        items.add(new Item(bitmap3, "dog"));
        Drawable dog4 = getResources().getDrawable(R.drawable.dog1);
        Bitmap bitmap4 = ((BitmapDrawable)dog4).getBitmap();
        items.add(new Item(bitmap4, "dog"));
        Drawable dog5 = getResources().getDrawable(R.drawable.dog10);
        Bitmap bitmap5 = ((BitmapDrawable)dog5).getBitmap();
        items.add(new Item(bitmap5, "dog"));


        TabHost tabHost1 = findViewById(R.id.tabHost1) ;
        tabHost1.setup() ;//setup()함수를 호출하지 않으면 TabWidget이 정상적으로 표시되지 않는다

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
        ts1.setContent(R.id.content1) ;
        ts1.setIndicator("Contacts") ;
        tabHost1.addTab(ts1)  ;

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
        ts2.setContent(R.id.content2) ;
        ts2.setIndicator("Gallery") ;
        tabHost1.addTab(ts2) ;

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3") ;
        ts3.setContent(R.id.content3) ;
        ts3.setIndicator("Diary") ;
        tabHost1.addTab(ts3) ;



        //for tab1
        if (contactitems == null) {
            contactitems = getContactList();

            adapter = new ContactListAdapter(this, contactitems);

            listView = findViewById(R.id.listview1);
            listView.setAdapter(adapter);
        }
        //for search

        EditText editTextFilter = (EditText)findViewById(R.id.editTextFilter) ;
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString();
                if (filterText.length() > 0) {
                    listView.setFilterText(filterText) ;
                } else {
                    listView.clearTextFilter() ;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;

        //for add

        //for tab2

        //buuton1
        button1 = findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        //button2
        button2 = findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long seed = System.nanoTime();
                Collections.shuffle(items, new Random(seed));
                mAdapter = new MyAdapter(items, getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(items, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        //for tab3

        // 뷰에 있는 위젯들 리턴 받아두기
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        viewDatePick = (TextView) findViewById(R.id.viewDatePick);
        edtDiary = (EditText) findViewById(R.id.edtDiary);
        btnSave = (Button) findViewById(R.id.btnSave);

        // 오늘 날짜를 받게해주는 Calender 친구들
        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH);
        int cDay = c.get(Calendar.DAY_OF_MONTH);

        // 첫 시작 시에는 오늘 날짜 일기 읽어주기
        checkedDay(cYear, cMonth, cDay);

        // datePick 기능 만들기
        // datePicker.init(연도,달,일)
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 이미 선택한 날짜에 일기가 있는지 없는지 체크해야할 시간이다
                checkedDay(year, monthOfYear, dayOfMonth);
            }
        });

        // 저장/수정 버튼 누르면 실행되는 리스너
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fileName을 넣고 저장 시키는 메소드를 호출
                saveDiary(fileName);
                imm.hideSoftInputFromWindow(edtDiary.getWindowToken(),0);
            }
        });

        //키보드 올리기 내리기
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

    }

    //연락처 추가화면


    public void OnClickHandler(View v){

        Intent intent = new Intent(MainActivity.this,subactivity.class);
        startActivityForResult(intent, Code.requestCode);
    }

    // 일기 파일 읽기
    private void checkedDay(int year, int monthOfYear, int dayOfMonth) {

        // 받은 날짜로 날짜 보여주는
        viewDatePick.setText(year + " - " + monthOfYear + " - " + dayOfMonth);

        // 파일 이름을 만들어준다. 파일 이름은 "20170318.txt" 이런식으로 나옴
        fileName = year + "" + monthOfYear + "" + dayOfMonth + ".txt";

        // 읽어봐서 읽어지면 일기 가져오고
        // 없으면 catch 그냥 살아? 아주 위험한 생각같다..
        FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);

            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            String str = new String(fileData, "EUC-KR");
            // 읽어서 토스트 메시지로 보여줌
            Toast.makeText(getApplicationContext(), "일기 써둔 날", Toast.LENGTH_SHORT).show();
            edtDiary.setText(str);
            btnSave.setText("수정하기");
        } catch (Exception e) { // UnsupportedEncodingException , FileNotFoundException , IOException
            // 없어서 오류가 나면 일기가 없는 것 -> 일기를 쓰게 한다.
            Toast.makeText(getApplicationContext(), "일기 없는 날", Toast.LENGTH_SHORT).show();
            edtDiary.setText("");
            btnSave.setText("새 일기 저장");
            e.printStackTrace();
        }

    }


    //when tab2 button is clicked
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    final Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시

                    //카드 제목 입력 받음
                    dialogView = (View) View.inflate(MainActivity.this, R.layout.dialog1, null);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("그림명 입력");
                    dlg.setView(dialogView);
                    dlgEdtName = (EditText) dialogView.findViewById(R.id.dlgEdt1);
                    dlg.setPositiveButton("이름 넣기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String tmp = dlgEdtName.getText().toString();
                            items.add(new Item(img, tmp));
                        }
                    });
                    dlg.show();

                    mAdapter = new MyAdapter(items, getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);
                    Log.d("태크", "" + items.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(requestCode == Code.requestCode && resultCode == Code.resultCode) {
            Log.d("ahsdfhsdklfkl","되돌아오긴함");
            ContactItem tmp_contactItem = new ContactItem();
            tmp_contactItem.setUser_Name(data.getStringExtra("name"));
            tmp_contactItem.setUser_phNumber(data.getStringExtra("phone"));
            Log.d("ahsdfhsdklfkl",""+tmp_contactItem.getUser_Name());
            contactitems.add(tmp_contactItem);
            adapter = new ContactListAdapter(this, contactitems);
            listView.setAdapter(adapter);
        }
    }

    // 일기 저장하는 메소드
    @SuppressLint("WrongConstant")
    private void saveDiary(String readDay) {

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS); //MODE_WORLD_WRITEABLE
            String content = edtDiary.getText().toString();

            // String.getBytes() = 스트링을 배열형으로 변환?
            fos.write(content.getBytes());
            //fos.flush();
            fos.close();
            // getApplicationContext() = 현재 클래스.this ?
            Toast.makeText(getApplicationContext(), "일기 저장됨", Toast.LENGTH_SHORT).show();

        } catch (Exception e) { // Exception - 에러 종류 제일 상위 // FileNotFoundException , IOException
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "오류오류", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<ContactItem> getContactList(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +
                " COLLATE LOCALIZED ASC";
        Cursor cursor = getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);
        LinkedHashSet<ContactItem> hashlist = new LinkedHashSet<>();
        if (cursor.moveToFirst()){
            do{
                long photo_id = cursor.getLong(2);
                long person_id = cursor.getLong(3);
                ContactItem contactItem = new ContactItem();
                contactItem.setUser_phNumber(cursor.getString(0));
                contactItem.setUser_Name(cursor.getString(1));
                contactItem.setPhoto_id((photo_id));
                contactItem.setPerson_id((person_id));

                hashlist.add(contactItem);
            }while (cursor.moveToNext());
        }
        ArrayList<ContactItem> contactItems = new ArrayList<>(hashlist);
        for (int i = 0; i < contactItems.size(); i++){
            contactItems.get(i).setId(i);
        }
        return contactItems;

    }


    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_READ_CONTACTS);

        } else {
            isPermission = true;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_READ_EXTERNAL_STORAGE);
        }else{
            isPermission = true;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION}, PERMISSIONS_ACCESS_MEDIA_LOCATION);
        }else{
            isPermission = true;
        }
    }
}