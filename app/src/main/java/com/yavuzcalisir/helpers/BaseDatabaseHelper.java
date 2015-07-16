package com.yavuzcalisir.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yavuz.calisir on 6/15/2015.
 */
public class BaseDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "BaseDatabaseHelper";

    private final String name;


    protected BaseDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.name = name;
    }

    protected BaseDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.name = name;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }


    public void initDatabase(Context context) {
        copyDatabaseToInternal(context, name);

    }


    // Basic FileUtils
    // =============================
    public static void copyDatabaseToExternal(Context context, String name) {
        try {
            FileInputStream inputFile = new FileInputStream(context.getDatabasePath(name));

            String outputPath = Environment.getExternalStorageDirectory().getPath() + "/" + name;
            File fileOutput = new File(outputPath);
            copyToFile(inputFile, fileOutput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * database'i assetten internal storage'e tasimak gerekirse diye
     *
     * @param context
     */
    private static void copyDatabaseToInternal(Context context, String name) {
        try {
            InputStream fileInput = context.getAssets().open(name);
            File fileOutput = context.getDatabasePath(name);
            copyToFile(fileInput, fileOutput);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            if (destFile.exists()) {
//                destFile.delete();
                return true;
            }

            FileOutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while (( bytesRead = inputStream.read(buffer) ) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.flush();
                try {
                    out.getFD().sync();
                } catch (IOException e) {
                }
                out.close();
            }
            return true;

        } catch (IOException e) {
            Log.e(TAG, "error : copyToFile(" + inputStream + ", " + destFile + ")");
            return false;
        }
    }


    // Query executors ----------------------------------------
    protected synchronized <T extends Object> List<T> getArray(String query, String[] values, ObjectCreator<T> creator) {

        long begin = System.currentTimeMillis();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, values);
        List<T> resultList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                resultList.add(creator.createFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        db.close();

        long end = System.currentTimeMillis();

        Log.i(TAG, "getArray(" + ( end - begin ) + "ms, " + query + ")");
        return resultList;
    }

    public <T extends Object> void getArrayInBackground(final String query, final String[] values, final ObjectCreator<T> creator, final OnQueryStartListener startListener, final OnQueryDoneListener<List<T>> doneListener){
        new AsyncTask<String, String, List<T>>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(startListener != null){
                    startListener.onQueryStart();
                }
            }

            @Override
            protected List<T> doInBackground(String... params) {
                return getArray(query, values, creator);
            }

            @Override
            protected void onPostExecute(List<T> ts) {
                super.onPostExecute(ts);
                if(doneListener != null){
                    doneListener.onQueryDone(ts);
                }

            }
        }.execute("");
    }

    protected List<String> getStringArray(String query, String[] values) {
        return getArray(query, values, new ObjectCreator<String>() {
            @Override
            public String createFromCursor(Cursor cursor) {
                return cursor.getString(0);
            }
        });
    }

    protected List<Integer> getIntegerArray(String query, String[] values) {
        return getArray(query, values, new ObjectCreator<Integer>() {
            @Override
            public Integer createFromCursor(Cursor cursor) {
                return cursor.getInt(0);
            }
        });
    }

    protected <T extends Object> T getObject(String query, String[] values, ObjectCreator<T> creator) {
        List<T> array = getArray(query, values, creator);
        if(array == null | array.size() == 0){
            return null;
        }else{
            return array.get(0);
        }
    }


    /** ---   --- **/
//    protected <T extends Object> void insert(T item, ValueCreator<T> valueCreator) {
//        insert(item.getClass().getSimpleName(), item, valueCreator);
//    }

//    protected <T extends Object> void insert(String tableName, T item, ValueCreator<T> valueCreator) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues values = valueCreator.createFromObject(item);
//
//        database.insert(tableName, null, values);
//        database.close();
//    }



    ///Mini helpers
    ///--------------------------
    protected static int[] toArray(List<Integer> input) {
        int[] ret = new int[input.size()];
        for (int i = 0; i < input.size(); i++) {
            ret[i] = input.get(i);
        }
        return ret;
    }

    protected static int[] toArray(Integer[] input) {
        int[] ret = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            ret[i] = input[i];
        }
        return ret;
    }

    public String[] toStringArray(int[] ints) {
        String[] strings = new String[ints.length];
        for(int i = 0; i<strings.length; i++){
            strings[i] = String.valueOf(ints[i]);
        }
        return strings;
    }

    public String[] toStringArray(int value){
        return new String[]{String.valueOf(value)};
    }

    public String[] toStringArray(int value1, int value2) {
        return new String[]{
                String.valueOf(value1),
                String.valueOf(value2)
        };
    }

    public String[] toStringArray(int value1, float value2, float value3, float value4) {
        return new String[]{
                String.valueOf(value1),
                String.valueOf(value2),
                String.valueOf(value3),
                String.valueOf(value4)
        };
    }

    public String[] toStringArray(String value1) {
        return new String[]{ value1 };
    }

    ///--------------------------
    public interface ObjectCreator<T> {
        T createFromCursor(Cursor cursor);
    }

    public interface ValueCreator<T> {
        ContentValues createFromObject(T item);
    }

    public interface OnQueryDoneListener<T>{
        void onQueryDone(T t);
    }

    public interface OnQueryStartListener {
        void onQueryStart();
    }

}
