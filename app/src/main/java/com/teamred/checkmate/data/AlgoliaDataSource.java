package com.teamred.checkmate.data;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.alibaba.fastjson.JSON;
import com.teamred.checkmate.Searchable;
import com.teamred.checkmate.SyncHelper;
import com.teamred.checkmate.data.model.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AlgoliaDataSource {
    private final static String TAG = "Algolia";


    private Client adminClient;
    private static volatile AlgoliaDataSource _instance = null;
    private static HashMap<String,String> map = new HashMap<>();
//    static {
//        FireStoreDataSource.getKey(map, "admin");
//        try {
//            Thread.currentThread().join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        _instance = new AlgoliaDataSource(1);
//    }

    private AlgoliaDataSource() {
        // This is bad. A better way is to store the info in the firebase and fetch everytime.
        // I will modify this later.
        // default is search only. No much sense at this time.
//        SyncHelper.acquire();
        FireStoreDataSource.getKey(map, "admin");
        adminClient = new Client("M7T1CSJRGZ", map.get("search"));
    }

    private AlgoliaDataSource(int admin) {
        // admin
        SyncHelper.acquire();
        adminClient = new Client("M7T1CSJRGZ", map.get("admin"));
    }

    public static void initAlgolia(){
        FireStoreDataSource.getKey(map, "admin");
        Log.i(TAG, "get algolia key");
//        if (map.containsKey("error")){
//            Toast.makeText(context, "firebase fetch fail", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(context, "firebase fetch success", Toast.LENGTH_SHORT).show();
//        }
    }

    public static void set_instance(AlgoliaDataSource _instance) {
        AlgoliaDataSource._instance = _instance;
    }

    //    private
    public void initIndex(String index){
        adminClient.initIndex(index);
    }

    public static AlgoliaDataSource getInstance(){
        if (_instance == null){
//            initAlgolia(context);
            _instance = new AlgoliaDataSource(1);
        }
        return _instance;
    }

    /**
     * set custom ranking.
     * @param fragment
     * @param attr even is desc/asc, odd is the attribute name
     */
    public void setCustomRanking(Fragment fragment, String index, String...  attr){
        JSONArray arr = new JSONArray();
        // add custom ranking
        for (int i = 0; i < attr.length; i=i+2) {
            if (attr[i].equalsIgnoreCase("desc")){
                arr.put("desc("+attr[i+1]+")");
            }else if (attr[i].equalsIgnoreCase("asc")){
                arr.put("asc("+attr[i+1]+")");
            }
        }
        // add default ranking
        arr.put("typo")
                .put("geo")
                .put("words")
                .put("filters")
                .put("proximity")
                .put("attribute")
                .put("exact")
                .put("custom");
                // the `asc` and `desc` modifiers must be placed at the top
                // if you are configuring an index for sorting purposes only
        try {
            JSONObject ranking = new JSONObject().put("ranking", arr);
            adminClient.getIndex(index).setSettingsAsync(ranking, new CompletionHandler() {
                @Override
                public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                    Toast.makeText(fragment.getContext(), "Set rank", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * First step. send data to algolia
     * @param index index to search
     * @param jsonString record
     */
    public void addRecord(String index, String jsonString){
        Index target = adminClient.getIndex(index);
        try {
            target.addObjectAsync(new JSONObject(jsonString), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /** second step. set searchable attributes.
     *
     * @param index
     * @param attributes attributes list
     */
    public void noteSetting(String index, String... attributes){
        Index target = adminClient.getIndex(index);

        try {
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < attributes.length; i++) {
                jsonArray.put(attributes[i].toLowerCase());
            }
            JSONObject jsonObject = new JSONObject().put(
                    "searchableAttributes",
                    jsonArray);
            target.setSettings(jsonObject);
        } catch (JSONException | AlgoliaException e) {
            e.printStackTrace();
        }

    }

    /**
     * Third step. search notes by keywords,filters
     * @param fragment
     * @param index index of the target
     * @param keywords  keywords
     * @param type the searchable attributes
     * @param filters filter
     */
    public void search(Searchable fragment, String index, String keywords, String[] type, String filters){
        // callback handler. fill the result into listview
        CompletionHandler completionHandler = new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                // [...]
                fragment.updateSearchResult(content);
            }
        };

        // async search
        adminClient
                .getIndex(index)
                .searchAsync(
                        new Query(keywords) // basic query
                                .setRestrictSearchableAttributes(type) // restrict searchable attributes
                                .setFilters(filters)  // set filter
                        , completionHandler);

    }

    public void updateGroup(Group group){
        CompletionHandler completionHandler = new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                // [...]
                if (error == null){
                    Log.i(TAG, "update group success");
                }else{
                    Log.e(TAG, "update group fail");
                    error.printStackTrace();
                }
            }
        };
        try {
            adminClient.getIndex("group")
                    .partialUpdateObjectAsync(
                            new JSONObject(JSON.toJSONString(group))
                            , group.getId()
                            , completionHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
