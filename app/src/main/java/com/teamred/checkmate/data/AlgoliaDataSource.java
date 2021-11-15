package com.teamred.checkmate.data;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.teamred.checkmate.data.model.Note;
import com.teamred.checkmate.ui.search.SearchFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlgoliaDataSource {

    private Client adminClient;
    private static AlgoliaDataSource _instance = null;

    private AlgoliaDataSource() {
        // This is bad. A better way is to store the info in the firebase and fetch everytime.
        // I will modify this later.
        // default is search only. No much sense at this time.
        adminClient = new Client("M7T1CSJRGZ", FireStoreDataSource.getKey("search"));
    }

    private AlgoliaDataSource(int admin) {
        // admin
        adminClient = new Client("M7T1CSJRGZ", FireStoreDataSource.getKey("admin"));
    }

//    private
    public void initIndex(String index){
        adminClient.initIndex(index);
    }

    public static AlgoliaDataSource getInstance(){
        if (_instance == null){
            _instance = new AlgoliaDataSource(1);
        }
        return _instance;
    }

    public void addRecord(String index, String jsonString){
        Index target = adminClient.getIndex(index);
        try {
            target.addObjectAsync(new JSONObject(jsonString), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void noteSetting(String index) throws JSONException {
        Index target = adminClient.getIndex(index);
        JSONObject jsonObject = new JSONObject()
                .put("searchableAttributes", "tag")
                .put("searchableAttributes", "title")
                .put("searchableAttributes", "content");
        target.setSettingsAsync(jsonObject, null);
    }

    public void searchNote(SearchFragment fragment, String index, String keywords){
        CompletionHandler completionHandler = new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                // [...]
                if (content == null){
                    return;
                }
                System.out.println(content);
                try {
                    JSONArray hits = content.getJSONArray("hits");
                    int size = hits.length();
                    Note[] noteList = new Note[size];
                    for (int i = 0; i < hits.length(); i++) {
                        Note note = new Note();
                        JSONObject hitObj = hits.getJSONObject(i).getJSONObject("_highlightResult");
                        note.setContent(hitObj.getJSONObject("content").getString("value"));
                        note.setTitle(hitObj.getJSONObject("title").getString("value"));
                        JSONArray arr = hitObj.getJSONArray("tags");
                        List<String> tagList = new ArrayList<>();
                        for (int j = 0; j < arr.length(); j++) {
                            tagList.add(arr.getJSONObject(i).getString("value"));
                        }
                        note.setTags(tagList.toArray(new String[0]));
                        noteList[i] = note;
//                        fragment
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Index searchIndex = adminClient.getIndex(index);
        searchIndex.searchAsync(new Query(keywords), completionHandler);

    }
//    {"hits":
//    [{"content":"So happy the first note is succesfully uploaded to the algolia. By the way all the notes are currently stored under demo index. OK so many words! I readlly want copy and emm.\nQueries contain keywords, terms that are especially indicative of what a user is looking for. However, these keywords are not universal. They vary based on your catalog and products. For a clothing company, the word “red” in a query likely indicates that a user is searching for red articles. For a film database however, “red” is probably unrelated to a color category.\n\nOnce you determine the intended effect of specific query terms or phrases, you can dynamically change your users’ results when their search matches those terms. Algolia lets you configure this using Rules.\n ","tags":["thoughts"," work"," diary"],"title":"So happy the first note uploaded successfully!","objectID":"22482411001","_highlightResult":{"content":{"value":"So happy the first note is succesfully uploaded to the algolia. By the way all the notes are currently stored under demo index. OK so many words! I readlly want <em>copy<\/em> and emm.\nQueries contain keywords, terms that are especially indicative of what a user is looking for. However, these keywords are not universal. They vary based on your catalog and products. For a clothing company, the word “red” in a query likely indicates that a user is searching for red articles. For a film database however, “red” is probably unrelated to a color category.\n\nOnce you determine the intended effect of specific query terms or phrases, you can dynamically change your users’ results when their search matches those terms. Algolia lets you configure this using Rules.\n ","matchLevel":"full","fullyHighlighted":false,"matchedWords":["copy"]},"tags":[{"value":"thoughts","matchLevel":"none","matchedWords":[]},{"value":" work","matchLevel":"none","matchedWords":[]},{"value":" diary","matchLevel":"none","matchedWords":[]}],"title":{"value":"So happy the first note uploaded successfully!","matchLevel":"none","matchedWords":[]}}},
//     {"content":"hello! This this the first note! Welecome to checkmate. It's really hard to generate a test note because there is really nothing to say. And I don;t know how to copy and paste the text in my PC to this emulator. So sad everyting I type again.\nHello Hello Hello. \nLet me think what I need to present all the functions. Well the color in the title should't be here be cause I want to show the importance.\nAnyway, upload!","tags":["diary"," morning"],"title":"today is a blue day","objectID":"22521818000","_highlightResult":{"content":{"value":"hello! This this the first note! Welecome to checkmate. It's really hard to generate a test note because there is really nothing to say. And I don;t know how to <em>copy<\/em> and paste the text in my PC to this emulator. So sad everyting I type again.\nHello Hello Hello. \nLet me think what I need to present all the functions. Well the color in the title should't be here be cause I want to show the importance.\nAnyway, upload!","matchLevel":"full","fullyHighlighted":false,"matchedWords":["copy"]},"tags":[{"value":"diary","matchLevel":"none","matchedWords":[]},{"value":" morning","matchLevel":"none","matchedWords":[]}],"title":{"value":"today is a blue day","matchLevel":"none","matchedWords":[]}}},
//     {"content":"Remember Terminator 2? Guns were nearly useless against the murderous T-1000, played by Robert Patrick. Bullets fired at the “liquid metal” robot resulted only in a chrome-looking bullet splash that momentarily staggered the killing machine. The effects were done by Stan Winston, who died in 2008, but a video and short blurb shared by the Stan Winston School of Character Arts revealed, to our surprise and delight, that the bullet impact effects were not CGI.\n\nHow was this accomplished? First of all, Winston and his team researched the correct “look” for the splash impacts by firing projectiles into mud and painstakingly working to duplicate the resulting shapes. These rea


    }
