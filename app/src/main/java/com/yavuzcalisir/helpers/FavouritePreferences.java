package com.yavuzcalisir.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.base.Joiner;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 *
 * Save objects into SharedPreferences with basic operations like add, remove, contains, getCount.
 * This class useful for bookmarking or favorite management for any class. When using this, you must
 * implement Gson for serializing.<br><br>
 *
 * Usage:<br><br>
 *
 * 1- Create "Helper Class" for your model which extends this class.<br><br>
 * <code>public class NoteHelper extends FavouritePreferences&lt;Note&gt; { ... }</code><br><br>
 *
 * 2-Call {@link #FavouritePreferences(android.content.Context, Class, String)} function in your
 * construction.<br><br>
 *
 * 3-Override {@link #getTypeToken()} function.<br><br>
 *
 *
 *
 * And result shuld be look like this:<br>
 *  <code>
 public class NoteHelper extends FavouritePreferences<Note> {
    public NoteHelper(Context context) {
        super(context, Note.class, "notes");
    }

    Override
    protected Type getTypeToken() {
        return new TypeToken<ArrayList<Note>>(){}.getType();
    }
 }
 * </code>
 */
public abstract class FavouritePreferences<T>{

    private Type listType;
    protected String PREF_NAME = "favourites";
    protected String LIST_PREF = "list";
    Gson gson;


    SharedPreferences sharedPreferences;

    public FavouritePreferences(Context context, Class<T> type){
        init(context, type);
    }


    /**
     *  usage:<br>
     *  <code>
     *  super(context, Note.class, "notes");
     *  </code>
     *
     * @param context is context.
     * @param type is can be any type for class.
     * @param fileName for shared prefeference.
     */
    public FavouritePreferences(Context context, Class<T> type, String fileName){
        PREF_NAME = fileName;
        init(context, type);
    }

    private void init(Context context, Class<T> type){
        listType = getTypeToken();

        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    /**
     * You must override this like this
     * <code>
    Override protected Type getTypeToken() {
        return new TypeToken<ArrayList<Note>>(){}.getType();
    }
     * </code>
     *
     *
     * @return type of your class in array.
     */
    abstract protected Type getTypeToken();


    /**
     * add item into shared pref.
     * @param t item
     */
    public void add(T t){
        List<T> list = getAll();
        if(list == null){
           list = new ArrayList<>();
        }
        list.add(t);
        String jsonStr = gson.toJson(list);

        sharedPreferences.edit().putString(LIST_PREF, jsonStr).commit();
    }

    /**
     * get all items from shared pref
     * @return
     */
    public List<T> getAll(){
        String jsonStr;
        jsonStr = sharedPreferences.getString(LIST_PREF, null);
        List<T> t = gson.fromJson(jsonStr, listType);
        if(t == null){
            t = new ArrayList<>();
        }
        return t;
    }

    /**
     * remove an item in shared pref. Uses serialization comparing.
     * @param item item
     */
    public void remove(T item){
        String itemStr = gson.toJson(item);

        List<T> all = getAll();

        for (Iterator<T> it = all.iterator(); it.hasNext(); ) {
            T tempItem = it.next();
            String tempStr = gson.toJson(tempItem);
            if (tempStr.equals(itemStr)) {
                it.remove();
                Log.i("Pref", "removed");
                break;
            }
        }

        Joiner.on(",").join(all);



        replaceAll(all);
    }


    /**
     * remove an item with custom comparator.
     *
     * @param target can be null if you want use in comparator as rhs.
     * @param comparator custom your comparator
     */
    public void remove(T target, Comparator<T> comparator){
        List<T> all = getAll();

        for (Iterator<T> it = all.iterator(); it.hasNext(); ) {
            T item = it.next();
            if (comparator.compare(item, target) == 0) {
                it.remove();
                Log.i("Pref", "removed");
            }
        }

        replaceAll(all);
    }

    public void replaceAll(List<T> list){
        if(list == null){
            list = new ArrayList<>();
        }
        String jsonStr = gson.toJson(list);
        sharedPreferences.edit().putString(LIST_PREF, jsonStr).commit();
    }

    public int getCount(){
        return getAll().size();
    }

    /**
     * triggers callback with cont. Operation isnt block ui.
     * @param callBack
     */
    public void getCountAsync(final BackgroundCallBack callBack){
        new AsyncTask<String, String, Integer>(){
            @Override
            protected Integer doInBackground(String... params) {
                return getAll().size();
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                callBack.onDone(integer);
            }


        }.execute("");
    }

    public interface BackgroundCallBack{
        public abstract void onDone(int value);
    }

    public interface BackgroundItemCallBack<T>{
        public abstract <T> void onDone(T value);
    }

    public void contains(final T item, final Comparator<T> comparator, final BackgroundItemCallBack<T> callBack){
        new AsyncTask<String, String, T>(){
            @Override
            protected T doInBackground(String... params) {
                List<T> all = getAll();
                for(T t : all){
                    if(comparator.compare(t, item) == 0){
                        return t;
                    }
                }
                return null;
            }


            @Override
            protected void onPostExecute(T t) {
                super.onPostExecute(t);
                // success: 1 ----- fail: 0
                callBack.onDone(t);
            }
        }.execute("");
    }

}
