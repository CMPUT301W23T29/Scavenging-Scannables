package com.example.scavengingscannables.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();

        // quick test code to check if database works
        // should see {TestField=TestValue} instead of "This is home fragment" if successful
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("TestCollection").document("TestID");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    mText.setValue(String.valueOf(document.getData()));
                }else{
                    mText.setValue("This is home fragment");
                }
            }
        });
    }

    public LiveData<String> getText() {
        return mText;
    }
}