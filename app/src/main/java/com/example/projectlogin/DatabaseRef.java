package com.example.projectlogin;

import com.google.firebase.database.DatabaseReference;

public class DatabaseRef {
    private static DatabaseReference databaseReference;

    public DatabaseRef() { }

    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public static void setDatabaseReference(DatabaseReference databaseReference) {
        DatabaseRef.databaseReference = databaseReference;
    }
}

