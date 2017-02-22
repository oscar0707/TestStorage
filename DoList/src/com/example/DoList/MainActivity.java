package com.example.DoList;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class MainActivity extends Activity {

	DbHelper mDbHelper;
	SQLiteDatabase mDb;
	EditText mEditNum;
	EditText mEditGrade;
	EditText mEditMemo;
	ListView mListMember;
	Cursor mCursor;
	
	ArrayList<String> mArMember = new ArrayList<String>();
	ArrayAdapter<String> mAdapter;
	int mSelIndex = -1;
	
	AdapterView.OnItemClickListener mItemListener = new
			AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView parent, View view, int position, long id){
					ViewRecord(position);
				}
			};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		mEditNum =(EditText)findViewById(R.id.editNum);
		mEditGrade =(EditText)findViewById(R.id.editGrade);
		mEditMemo =(EditText)findViewById(R.id.editMemo);
		
		mDbHelper = new DbHelper(this);
		mDb = mDbHelper.getWritableDatabase();
		initListView();
		readAllRecords();
		
		 Button buttonAdd = (Button) findViewById(R.id.buttonAdd) ;
	        buttonAdd.setOnClickListener(new Button.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	              onBtnAdd();
	              }
	        }) ;
	        
	        
	     Button buttonDel = (Button) findViewById(R.id.buttonDel) ;
	        buttonDel.setOnClickListener(new Button.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	              onBtnDel();
	              }
	        }) ;
		
         Button buttonUpdate = (Button) findViewById(R.id.buttonUpdate) ;
         	buttonUpdate.setOnClickListener(new Button.OnClickListener() {
	           @Override
	           public void onClick(View view) {
	             onBtnUpdate();
	             }
	       }) ;   
	        
		
		
	}

	public void initListView(){
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mArMember);
		
		mListMember =(ListView)findViewById(R.id.listMember);
		mListMember.setAdapter(mAdapter);
		mListMember.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListMember.setDivider(new ColorDrawable(Color.GRAY));
		mListMember.setDividerHeight(2);		
		mListMember.setOnItemClickListener(mItemListener);
		
	};
	
	public void onBtnAdd(){
		String strNum = mEditNum.getText().toString();
		String strKorean = mEditGrade.getText().toString();
		String strMath = mEditMemo.getText().toString();
		
		
		String strQuery = "insert into Student(name, korean, math) values('"+ strNum +"', " +strKorean +", "+ strMath +");";
		mDb.execSQL(strQuery);
		
		readAllRecords();
		mCursor.moveToLast();
		mSelIndex = mCursor.getInt(0);
	};
	
	public void onBtnDel(){
		mDb.execSQL("delete from Student where _id =" + mSelIndex);
		readAllRecords();
	}
	
	public void onBtnUpdate(){
		String strNum = mEditNum.getText().toString();
		String strKorean = mEditGrade.getText().toString();
		String strMath = mEditMemo.getText().toString();
		
		
		String strQuery ="update Student set name ='"+ strNum + "', korean= "+ strKorean +", math = " +strMath+ " where _id =" + mSelIndex;
		mDb.execSQL(strQuery);
		readAllRecords();
	}
	
	
//	public void onClick(View v){
//		switch(v.getId()){
//		case R.id.buttonAdd:
//			onBtnAdd();
//			break;
//		}
//	};
	
	public void readAllRecords(){
		
		mArMember.clear();
		
		String strQuery ="select _id, name, korean, math from Student";
		mCursor = mDb.rawQuery(strQuery, null);
		
		
		for(int i=0; i < mCursor.getCount(); i++){
			
			mCursor.moveToNext();
			
			int nId = mCursor.getInt(0);
			
			String strNum = mCursor.getString(1);
			
			int strGrade = mCursor.getInt(2);
			
			int strMemo = mCursor.getInt(3);
			
			String strRecord = nId + ":" + strNum + "/" + strGrade +"/" +strMemo;
			
			Log.d("tag", "rec-"+ strRecord);
			
			mArMember.add(strRecord);
			
		}
		
		mAdapter.notifyDataSetChanged();
		
		
	};
	
	
	public void ViewRecord(int nIndex){
		
		mCursor.moveToPosition(nIndex);
		int nId = mCursor.getInt(0);
		String strNum = mCursor.getString(1);
		int strGrade = mCursor.getInt(2);
		int strMemo = mCursor.getInt(3);
		
		 
		mEditNum.setText(strNum);
		mEditGrade.setText(Integer.toString(strGrade));
		mEditMemo.setText(Integer.toString(strMemo));
		mSelIndex = nId;
		
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
 class DbHelper extends SQLiteOpenHelper{
	public DbHelper(Context context){
		super(context, "ReportCard", null, 1);
	}
	
	public void onCreate(SQLiteDatabase db){
		
		db.execSQL("create table Student("+
					"_id integer PRIMARY KEY autoincrement," +
				    "name Text, korean integer, math integer);");
		
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		
		db.execSQL("drop table if exists Student");
		onCreate(db);
	}

	
	
	
}
	
}
