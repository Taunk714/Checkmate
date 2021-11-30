package com.teamred.checkmate.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Source;
import com.teamred.checkmate.SyncHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FireStoreDataSource {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private static FirebaseDatabase mdb = FirebaseDatabase.getInstance();

    private final static String TAG_SUCCESS = "Add firebase data";
    private final static String TAG_FAIL = "Firebase add data fail";
    private final static String TAG = "Firebase";

    private static void addData(String collection, String document, Object data){
        db.collection(collection).document(document).set(data);
    }

    private static void addData(String collection,  Object data){
        db.collection(collection).add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG_SUCCESS, "Document ID " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG_FAIL, e.getMessage());
            }
        });
    }

    public static void getKey(HashMap<String, String> map, String type){
        Source source = Source.SERVER;
        Task<DocumentSnapshot> documentSnapshotTask = db.collection("config").document("key").get(source);
        documentSnapshotTask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    SyncHelper.release();
                    Map<String, Object> data = task.getResult().getData();
                    map.put("search", (String) data.get("search"));
                    map.put("admin", (String) data.get("admin"));
                    AlgoliaDataSource.getInstance(null);
                    Log.i(TAG, "Fetch algolia key");
//                    AlgoliaDataSource.
                }else{
                    map.put("error", "algolia login fail");
                    Log.i(TAG, "Fetch algolia key fail");
                }

            }
        });
//        try {
//            Tasks.await(documentSnapshotTask);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Map<String, Object> data = result.getData();
//        return (String) data.get(type);

    }



}