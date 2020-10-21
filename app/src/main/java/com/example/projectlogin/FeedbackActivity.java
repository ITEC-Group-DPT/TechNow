package com.example.projectlogin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;

public class FeedbackActivity extends MainLayout {
    private TextInputEditText feedback_text;
    private RatingBar ratingBar;
    private float num = 0;
    private String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setTitle("Feedback");
        feedback_text = findViewById(R.id.feedback_text);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                num = ratingBar.getRating();
            }
        });
    }

    public void confirm_feedback(View view) {
        text = feedback_text.getText().toString();
        if (text.isEmpty()) {
            Toast.makeText(this, "Please submit your feedback", Toast.LENGTH_SHORT).show();
        }
        else if (num == 0) {
            Toast.makeText(this, "Please rate us", Toast.LENGTH_SHORT).show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Your feedback has been recorded!");
            builder.setMessage("Thank you for your feedback!");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    feedback_text.setText("");
                    ratingBar.setRating(0);
                    onBackPressed();
                }
            });
            AlertDialog showNote = builder.create();
            showNote.show();
        }
    }

    public void Back_feedback(View view) {
        onBackPressed();
    }
}