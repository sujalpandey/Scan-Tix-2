package com.project.scantix;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

class CanvasView extends View {
    private Paint paint;

    public CanvasView(Context context) {
        super(context);
        // Initialize Paint object for drawing
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Use the Canvas object to draw on the View
        // For example, you can draw shapes, lines, text, etc. here
        canvas.drawCircle(100, 100, 50, paint); // Draw a circle
    }
}

public class DashboardActivity extends AppCompatActivity {

    TextView emailtext;
    Button logout;
    FirebaseAuth auth;
    String userEmail, userId;
    FirebaseUser user;
    FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        emailtext = findViewById(R.id.emailText);
        logout = findViewById(R.id.btnLogout);

        auth = FirebaseAuth.getInstance(); // Initialize auth
        fstore = FirebaseFirestore.getInstance();

        user = auth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        userEmail = user.getEmail();
        userId = user.getUid();


        emailtext.setText(userEmail);


        DocumentReference docRef = fstore.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        showToast("data gathered!");
                    } else {
                        Log.d(TAG, "No such document");
                        showToast("Document is not present in the database");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    showToast("Data gathering is unsuccessful");
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });   }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}