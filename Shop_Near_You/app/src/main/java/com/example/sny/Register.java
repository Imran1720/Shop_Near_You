package com.example.sny;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.sny.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {


    boolean mverified=false,emverified=false;
    RadioButton rb;
    String uid;

    ActivityRegisterBinding binding;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    PhoneAuthProvider.ForceResendingToken token;

    String id;
    Uri uri;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register);

        pd = new ProgressDialog(this);
        pd.setMessage("Registering...");
        pd.setProgress(ProgressDialog.STYLE_SPINNER);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SNY").child("USERS");
        storageReference = FirebaseStorage.getInstance().getReference().child("SNY_USER_IMAGES/"+ UUID.randomUUID().toString());





        auth = FirebaseAuth.getInstance();
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                id = s;
                token = forceResendingToken;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                signPhoneAuth(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                    Toast.makeText(Register.this, "AUTHENTICATION FAILED", Toast.LENGTH_SHORT).show();

            }
        };

        ActivityResultLauncher<String> mgetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                uri = result;

                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        binding.uphoto.setImageBitmap(bitmap);
                        binding.uphoto.setBackground(null);

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

            }
        });

        binding.uphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mgetContent.launch("image/*");
            }
        });



    }
    private void signPhoneAuth(PhoneAuthCredential phoneAuthCredential)
    {

        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    binding.mvw.setVisibility(View.VISIBLE);
                    binding.miw.setVisibility(View.GONE);
                    mverified = true;
                }
                else {
                    binding.miw.setVisibility(View.VISIBLE);
                    binding.mvw.setVisibility(View.GONE);
                    mverified =false;
                }
            }
        });

    }


    public void sendotp(View view) {

        if(binding.phone.getText().toString().isEmpty())
        {
            binding.phone.setError("ENTER A NUMBER");
        }
        else {
            String num = binding.phone.getText().toString();
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth).
                    setPhoneNumber("+91" + num).
                    setTimeout(60L, TimeUnit.SECONDS).
                    setActivity(this).setCallbacks(callbacks).build();
            PhoneAuthProvider.verifyPhoneNumber(options);
            Toast.makeText(this, "OTP SENT", Toast.LENGTH_SHORT).show();
        }
    }

    public void resend(View view) {
        String num = binding.phone.getText().toString();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber("+91"+num).
                setTimeout(60L,TimeUnit.SECONDS).
                setActivity(this).setCallbacks(callbacks).
                setForceResendingToken(token).
                build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        Toast.makeText(this, "OTP RESENT", Toast.LENGTH_SHORT).show();

    }

    public void verify(View view) {
        String otp = binding.otp.getText().toString().trim();
        if(otp.isEmpty())
        {
            binding.otp.setError("ENTER OTP");
        }
        else
        {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id,otp);
            signPhoneAuth(credential);
        }
    }


    public void registeration(View view) {

        String un, em, ph, di, vi, hn, ar, lm, st, actype,address;


        un = binding.uname.getText().toString().toUpperCase();
        em = binding.email.getText().toString();
        ph = binding.phone.getText().toString();
        di = binding.district.getText().toString().toUpperCase();
        vi = binding.village.getText().toString().toUpperCase();
        hn = binding.hno.getText().toString();
        ar = binding.area.getText().toString().toUpperCase();
        lm = binding.lmark.getText().toString().toUpperCase();
        st = binding.state.getSelectedItem().toString().toUpperCase();
        int id = binding.rg.getCheckedRadioButtonId();
        rb = findViewById(id);
        address = hn+","+lm+","+ar+","+vi+","+di+","+st;
        actype = rb.getText().toString().toUpperCase();

        if( un.isEmpty() & di.isEmpty() & vi.isEmpty() & hn.isEmpty() & ar.isEmpty() & lm.isEmpty() & em.isEmpty() & binding.pass.getText().toString().isEmpty() & binding.cpass.getText().toString().isEmpty())
        {
            Toast.makeText(this, "PLEASE FILL ALL THE FIELDS", Toast.LENGTH_SHORT).show();
            binding.uname.setError("ENTER USERNAME");
            binding.district.setError("ENTER DISTRICT");
            binding.village.setError("ENTER VILLAGE");
            binding.hno.setError("ENTER HOUSE NUMBER");
            binding.area.setError("ENTER COLONY");
            binding.lmark.setError("ENTER LANDMARK");
        }
        else  if (un.isEmpty()) {
            binding.uname.setError("ENTER USERNAME");
        } else if (di.isEmpty()) {
            binding.district.setError("ENTER DISTRICT");
        } else if (vi.isEmpty()) {
            binding.village.setError("ENTER VILLAGE");
        } else if (hn.isEmpty()) {
            binding.hno.setError("ENTER HOUSE NUMBER");
        } else if (ar.isEmpty()) {
            binding.area.setError("ENTER COLONY");
        } else if (lm.isEmpty()) {
            binding.lmark.setError("ENTER LANDMARK");
        } 

        else {

            if(emverified==true && mverified==true)
            {
                pd.show();
                storageReference.putFile(uri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String url = uri.toString();

                                MyModel myModel = new MyModel(url, un, em, ph, actype, st, di, vi,address,uid);
                                databaseReference.child(uid).setValue(myModel);
                                Toast.makeText(Register.this, "REGISTRATION COMPLETE", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                                startActivity(new Intent(Register.this, MainActivity.class));


                            }


                        });

                    }
                });
            }
            else{
                Toast.makeText(this, "EMAIL/MOBILE NOT VERIFIED"+emverified+" "+mverified, Toast.LENGTH_SHORT).show();
            }


        }
    }






    public void verifyemail(View view) {

        if(!binding.cpass.getText().toString().equals(binding.pass.getText().toString()))
        {
            binding.cpass.setError("PASSWORD NOT SAME");
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }

        else if(binding.email.getText().toString().isEmpty())
        {
            binding.email.setError("ENTER EMAIL");
        }
        else if(binding.pass.getText().toString().isEmpty())
        {
            binding.pass.setError("ENTER EMAIL");
        }
        else if(binding.cpass.getText().toString().isEmpty())
        {
            binding.cpass.setError("ENTER EMAIL");
        }
        else {

            auth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.cpass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        binding.emv.setVisibility(View.VISIBLE);
                        binding.emi.setVisibility(View.GONE);
                        emverified =true;
                        uid = auth.getCurrentUser().getUid();
                    } else {

                        binding.emv.setVisibility(View.GONE);
                        binding.emi.setVisibility(View.VISIBLE);
                        emverified=false;
                    }
                }
            });
        }

    }



}