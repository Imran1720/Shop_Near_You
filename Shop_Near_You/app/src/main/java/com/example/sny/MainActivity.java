package com.example.sny;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText email,pass;
    FirebaseAuth auth;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        auth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS");

    }

    public void login(View view) {
        String semail = email.getText().toString();
        String pwd = pass.getText().toString();
        if(semail.isEmpty()){email.setError("Please enter an email");}
        else if(pwd.isEmpty()){pass.setError("Please enter password");}
        else{
            auth.signInWithEmailAndPassword(semail,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {


                        databaseReference.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("actype").getValue(String.class).equals("ADMIN"))
                                {
                                    startActivity(new Intent(MainActivity.this,AdminView.class));
                                }
                                else if (snapshot.child("actype").getValue(String.class).equals("CUSTOMER"))
                                {
                                    startActivity(new Intent(MainActivity.this,HomePage.class));
                                }
                                else
                                {

                                    startActivity(new Intent(MainActivity.this,SellerPage.class));
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                    else{
                        Toast.makeText(MainActivity.this, "INVALID EMAIL/PASSWORD!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



    }

    public void register(View view) {
       
        startActivity(new Intent(this,Register.class));
    }

    public void fp(View view) {
        AlertDialog.Builder b =new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.reset,null,false);
        EditText e =v.findViewById(R.id.resetmail);
        b.setView(v);
        b.setCancelable(false);
        b.setPositiveButton("reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = e.getText().toString();
                if(s.isEmpty())
                {
                    e.setError("Enter an email");
                }
                else
                {
                    auth.sendPasswordResetEmail(s).addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this, "reset link sent", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                e.setError("Invalid Email");
                            }
                        }
                    });
                }
            }
        });
        b.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.show();
    }


}