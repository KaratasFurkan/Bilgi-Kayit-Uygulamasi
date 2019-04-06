package com.karatas.furkan.fk17011614;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class FormActivity extends AppCompatActivity {

    private TextView dateOfBirthTV;
    EditText[] editTexts;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Input inputs;
    private ImageView profilePicture;

    Bitmap bitmap;
    private static final int GALLERY = 1;
    private static final int CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Objects.requireNonNull(getSupportActionBar()).hide();

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_picture_icon);
        profilePicture = findViewById(R.id.iv_profile_picture);
        dateOfBirthTV = findViewById(R.id.et_date_of_birth);
        editTexts = new EditText[]{
                findViewById(R.id.et_name),
                findViewById(R.id.et_surname),
                findViewById(R.id.et_place_of_birth),
                findViewById(R.id.et_id_number),
                findViewById(R.id.et_phone_number),
                findViewById(R.id.et_email_address)};
        inputs = new Input(this, editTexts);
        inputs.setTextViews(new TextView[]{dateOfBirthTV});
        inputs.setInvisibleText((TextView) findViewById(R.id.tv_blank_lines_alert));

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                String date = day + "/" + month + "/" + year;
                dateOfBirthTV.setText(date);
                inputs.clearWarnings();
            }
        };

        dateOfBirthTV.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = 1990, month = 0, day = 1;
                DatePickerDialog dialog = new DatePickerDialog(
                        FormActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day
                );
                Objects.requireNonNull(dialog.getWindow())
                        .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        findViewById(R.id.btn_clear).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputs.empty();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.profile_picture_icon);
                profilePicture.setImageBitmap(bitmap);
            }
        });

        findViewById(R.id.btn_save).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputs.warnForBlanks()) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    sendInformation(intent);
                    startActivity(intent);
                }
            }
        });

        profilePicture.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMultiplePermissions();
                showPictureDialog();
            }
        });

        for (EditText editText : editTexts) {
            editText.addTextChangedListener(inputs);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int i = 0;
        for (EditText editText : editTexts) {
            outState.putString(String.valueOf(i++), editText.getText().toString());
        }
        outState.putString(String.valueOf(i++), dateOfBirthTV.getText().toString());

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();
        outState.putByteArray(String.valueOf(i), byteArray);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int i = 0;
        for (EditText editText : editTexts) {
            editText.setText(savedInstanceState.getString(String.valueOf(i++)));
        }
        dateOfBirthTV.setText(savedInstanceState.getString(String.valueOf(i++)));
        byte[] byteArray = savedInstanceState.getByteArray(String.valueOf(i));
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        profilePicture.setImageBitmap(bitmap);
    }

    public void sendInformation(Intent intent) {
        for (EditText editText : editTexts) {
            intent.putExtra(
                    getResources().getResourceEntryName(editText.getId()),
                    editText.getText().toString());
        }
        intent.putExtra(
                getResources().getResourceEntryName(dateOfBirthTV.getId()),
                dateOfBirthTV.getText().toString());

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();
        intent.putExtra("IMAGE", byteArray);
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    saveImage(bitmap);
                    profilePicture.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_LONG).show();
                }
            }

        } else if (requestCode == CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");
            profilePicture.setImageBitmap(bitmap);
            saveImage(bitmap);
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + "/images");
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are " +
                            //"granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
