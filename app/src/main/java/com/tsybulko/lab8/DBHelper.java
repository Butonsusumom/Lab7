package com.tsybulko.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class DBHelper extends SQLiteOpenHelper {

    public final Context fContext;

    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "groupsDb";

    public static final String TABLE_STUDENTS = "students";
    public static final String TABLE_GROUPS = "groups";

    public static final String KEY_ID = "_id";
    public static final String KEY_GROUP_ID = "group_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_GRADE = "grade";

    public static final String KEY_GROUP = "group_number";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        fContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_STUDENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_GROUP_ID + " INTEGER,"
                + KEY_NAME + " TEXT,"
                + KEY_GRADE + " FLOAT" + ")");

        db.execSQL("CREATE TABLE " + TABLE_GROUPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_GROUP + " TEXT"+ ")");

        fillGroupTable(db);
        fillStudentsTable(db);
        db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " ORDER BY " + KEY_NAME + " ASC", null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);

        onCreate(db);
    }

    public void fillGroupTable(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        Resources res = fContext.getResources();
        XmlResourceParser _xml = res.getXml(R.xml.groups_record);
        try {
            int eventType = _xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if ((eventType == XmlPullParser.START_TAG)
                        && (_xml.getName().equals("record"))) {
                    String groups_record = _xml.getAttributeValue(0);
                    values.put(KEY_GROUP, groups_record);
                    db.insert(TABLE_GROUPS, null, values);
                }
                eventType = _xml.next();
            }
        }
        catch (XmlPullParserException | IOException e) {
            Log.e("Test", e.getMessage(), e);
        } finally {
            _xml.close();
        }
    }

    public void fillStudentsTable(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        Resources res = fContext.getResources();
        XmlResourceParser _xml = res.getXml(R.xml.students_record);
        try {
            int eventType = _xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if ((eventType == XmlPullParser.START_TAG)
                        && (_xml.getName().equals("record"))) {
                    String grade_record = _xml.getAttributeValue(0);
                    String group_record= _xml.getAttributeValue(1);
                    String name_record = _xml.getAttributeValue(2);

                    values.put(KEY_GRADE, grade_record);
                    values.put(KEY_GROUP_ID, group_record);
                    values.put(KEY_NAME, name_record);

                    db.insert(TABLE_STUDENTS, null, values);
                }
                eventType = _xml.next();
            }
        }
        catch (XmlPullParserException | IOException e) {
            Log.e("Test", e.getMessage(), e);
        } finally {
            _xml.close();
        }
    }
}
