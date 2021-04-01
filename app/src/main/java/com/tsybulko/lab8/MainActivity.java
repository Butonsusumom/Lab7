package com.tsybulko.lab8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    Cursor cursorList;
    ListView listView;
    DBHelper dbh;
    SQLiteDatabase sqdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);
        listView = findViewById(R.id.listView);

        fillSpinnerValues();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Cursor cursor =(Cursor) spinner.getSelectedItem();
                long rowid = cursor.getLong(cursor.getColumnIndex(DBHelper.KEY_ID));
                fillListViewValues(String.valueOf(rowid));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(pos);
                float grade = cursor.getFloat(cursor.getColumnIndex(DBHelper.KEY_GRADE));
                Toast.makeText(getApplicationContext(), String.valueOf(grade), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition( position );
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME));
                Toast.makeText(getApplicationContext(), name , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillSpinnerValues(){
        dbh = new DBHelper(this);
        sqdb = dbh.getReadableDatabase();

        Cursor cursor = sqdb.rawQuery("select " + DBHelper.KEY_ID  + ", " +  DBHelper.KEY_GROUP + " from "+ DBHelper.TABLE_GROUPS , null);

        String[] from = new String[]{DBHelper.KEY_GROUP};
        int[] to = new int[]{android.R.id.text1};
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor, from, to);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mAdapter);

        sqdb.close();
        dbh.close();
    }

    private void fillListViewValues(String group){
        dbh = new DBHelper(this);
        sqdb = dbh.getReadableDatabase();

        listView = findViewById(R.id.listView);

        Cursor cursor = sqdb.rawQuery("select * from "+ DBHelper.TABLE_STUDENTS + " where " +  DBHelper.KEY_GROUP_ID + "=" + group, null);

        String[] headers = new String[] {DBHelper.KEY_NAME, DBHelper.KEY_GRADE};
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                cursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        listView.setAdapter(mAdapter);

        sqdb.close();
        dbh.close();
    }
}