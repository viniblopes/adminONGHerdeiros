package com.vbllopes.adminappherdeirosdofuturo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends Activity {


    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ImageView imageView;
    private EditText txtNomeItem;
    private Uri imgUri;


    public static final String FB_STORAGE_PATH = "itens/";
    public static final String FB_DATABASE_PATH = "itens";
    public static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Conexão com o Firebase
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        //Instanciar os Objetos que vou usar na Activity
        imageView = (ImageView) findViewById(R.id.imageView);
        txtNomeItem = (EditText) findViewById(R.id.txtNomeItem);

    }

    public void btnProcurar_Click(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.selImagem)), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imageView.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @SuppressWarnings("VisibleForTests")
    public void btnUpload_Click(View v) {
        if (imgUri != null && txtNomeItem != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle(getResources().getString(R.string.dEnviando));
            dialog.show();

            StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + txtNomeItem.getText() + "." + getImageExt(imgUri));

            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.imagemEnviadoSucesso), Toast.LENGTH_SHORT).show();
                    ImageUpload imageUpload = new ImageUpload(txtNomeItem.getText().toString(), taskSnapshot.getDownloadUrl().toString());

                    //Salvar a informação do Item no Banco Firebase
                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(imageUpload);

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage(getResources().getString(R.string.sEnvidado) + (int) progress);
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.SImagem), Toast.LENGTH_SHORT).show();
        }
    }

    public void btMainEvent_Click(View v){
        Intent it = new Intent(MainActivity.this, MainEventActivity.class);
        startActivity(it);
    }
}

