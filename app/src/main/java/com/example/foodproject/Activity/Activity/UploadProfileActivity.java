package com.example.foodproject.Activity.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imageView;
    ProgressBar progressBar;
    FirebaseAuth auth;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile);

        Button button_choose_image = findViewById(R.id.button_choose_image);
        Button button_upload = findViewById(R.id.button_upload);
        progressBar = findViewById(R.id.progress_bar_update);
        imageView = findViewById(R.id.new_update_image);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");

        button_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });

        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPic();
            }
        });
    }

    private void uploadPic() {
        if (imageUri != null) {
            progressBar.setVisibility(View.VISIBLE);
            StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri download = uri;
                            firebaseUser = auth.getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(download).build();
                            firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(UploadProfileActivity.this, "Update success", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(UploadProfileActivity.this, UserProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(UploadProfileActivity.this, "NO FILE SELECTED", Toast.LENGTH_LONG).show();
        }
    }

    private String getFileExtension(Uri imageUri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(imageUri));
    }

    private void openFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
