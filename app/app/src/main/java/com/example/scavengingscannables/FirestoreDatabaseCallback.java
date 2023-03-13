package com.example.scavengingscannables;

/**
 * Interface for callback functions from FirestoreDatabaseController
 */
public interface FirestoreDatabaseCallback {
    default <T> void OnDataCallback(T data){}

    default void OnDocumentExists(){}

    default void OnDocumentDoesNotExist(){}

}
