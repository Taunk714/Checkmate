package com.teamred.checkmate.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Source;

import java.util.Map;

public class FireStoreDataSource {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final static String TAG_SUCCESS = "Add firebase data";
    private final static String TAG_FAIL = "Firebase add data fail";

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

    public static String getKey(String type){
        Source source = Source.SERVER;
        DocumentSnapshot result = db.collection("config").document("key").get(source).getResult();
        Map<String, Object> data = result.getData();
        return (String) data.get(type);

    }



}
