package com.example.qrallye;

import android.location.Location;
import android.util.Log;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class DatabaseMGR {
    private final String TAG = "DatabaseMGR";
    private static final DatabaseMGR ourInstance = new DatabaseMGR();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference adminCollections = db.collection("Administrators");
    private CollectionReference teamCollections = db.collection("Groups");
    private CollectionReference quizzesCollections = db.collection("Quizzes");
    private Team team;
    public static DatabaseMGR getInstance() {
        return ourInstance;
    }

    private MyCallback callbackTeam = new MyCallback() {
        @Override
        public void callbackCall(Team teamRetrieve) {
            SessionMGR.team = teamRetrieve;
            Log.d(TAG, "callbackCall: sessionTeam color"+team.getColor());
        }
    };

    private DatabaseMGR() {
    }

    public void getAdmin(){
        Log.d(TAG, "getAdmin: Création");
        DocumentReference adminDocument = adminCollections.document();
        Log.d(TAG, "getAdmin: admin =" +adminDocument.toString());
        adminCollections.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "onSuccess: snapshot " + queryDocumentSnapshots.toString());
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Administrators administrators = queryDocumentSnapshot.toObject(Administrators.class);
                    Log.d(TAG, "onSuccess: " + administrators.getUsername());
                }
            }
        });
    }

    public void getTeam(final String teamName ){
        Log.d(TAG, "getTeam: Recherche de la team");
        teamCollections.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if(documentSnapshot.getId().compareTo(teamName) == 0){
                        GeoPoint pos = documentSnapshot.getGeoPoint("pos");
                        Location position = new Location("");
                        position.setLongitude(pos.getLongitude());
                        position.setLatitude(pos.getLatitude());
                        team = new Team(teamName,
                                documentSnapshot.getLong("password"),
                                position,
                                null,
                                documentSnapshot.getString("color"),
                                null,null
                                );
                        callbackTeam.callbackCall(team);
                        Log.d(TAG, "onSuccess: teamcolor "+ team.getColor());

                    }
                }
            }
        });

    }
    public FirebaseFirestore getDb() {
        return db;
    }
    public interface MyCallback {
        void callbackCall(Team teamRetrieve);
    }
}
