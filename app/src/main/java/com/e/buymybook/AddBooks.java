package com.e.buymybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AddBooks extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText BookName,AuthorName,BookEdition,BookPrintedPrice;
    RadioGroup BookCondition,PriceOptions;
    RadioButton RadioBookConditionBtn,RadioPriceOptionsBtn;
    Spinner BookCategory;
    Button FormDoneBtn,btnChoose,btnTakePhoto;
    String URL;
    ImageView imageView;

    Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);

        BookName = findViewById(R.id.BookName);
        AuthorName = findViewById(R.id.AuthorName);
        BookEdition = findViewById(R.id.BookEdition);
        BookPrintedPrice = findViewById(R.id.BookPrintedPrice);

        btnTakePhoto = findViewById(R.id.TakePhoto);

        BookCondition = findViewById(R.id.BookCondition);
        PriceOptions = findViewById(R.id.PriceOptions);

        BookCategory = findViewById(R.id.BookCategory);

        FormDoneBtn = findViewById(R.id.FormDoneBtn);




        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");


        //Initialize Views
        btnChoose = findViewById(R.id.btnChoose);
        imageView = findViewById(R.id.imgView);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(i);
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        FormDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myRef.child("BookName").setValue(BookName.getText().toString());
                myRef.child("AuthorName").setValue(AuthorName.getText().toString());
                myRef.child("BookEdition").setValue(BookEdition.getText().toString());
                myRef.child("BookPrintedPrice").setValue(BookPrintedPrice.getText().toString());

                int BookConditionSelectedId = BookCondition.getCheckedRadioButtonId();
                RadioBookConditionBtn =  findViewById(BookConditionSelectedId);
                myRef.child("BookCondition").setValue(RadioBookConditionBtn.getText());

                int PriceOptionsSelectedId = PriceOptions.getCheckedRadioButtonId();
                RadioPriceOptionsBtn =  findViewById(PriceOptionsSelectedId);
                myRef.child("BookPriceOptions").setValue(RadioPriceOptionsBtn.getText());

                BookCategory = findViewById(R.id.BookCategory);
                String BookCategorytoDataBase = BookCategory.getSelectedItem().toString();
                myRef.child("BookCategory").setValue(BookCategorytoDataBase);


                uploadImage();
            }
        });




    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                   URL = downloadUrl.toString();
                                    myRef.child("URL").setValue(URL);
                                }
                            });
                            Toast.makeText(AddBooks.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddBooks.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    }

