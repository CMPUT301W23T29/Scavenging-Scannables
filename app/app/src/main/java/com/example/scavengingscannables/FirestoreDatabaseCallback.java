package com.example.scavengingscannables;

public interface FirestoreDatabaseCallback {
    default <T> void OnDataCallback(T data){};
    default void OnDocumentExists(){};
    default void OnDocumentDoesNotExist(){};

}
