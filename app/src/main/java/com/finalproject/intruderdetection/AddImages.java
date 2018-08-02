package com.finalproject.intruderdetection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddImages extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String username, user_id;
    private ProgressDialog progressDialog;
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 4;
    String adImgLink;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_CHOOSE_PHOTO = 2;
    static final int REQUEST_CHOOSE_PHOTO_2 = 3;
    static final int REQUEST_CHOOSE_PHOTO_3 = 4;
    private Uri photoURI;
    String mCurrentPhotoPath;
    private String mImageURL;
    private boolean imageSet = false;

    @BindView(R.id.img_user1)
    ImageView img1User;

    @BindView(R.id.img_user2)
    ImageView img2User;

    @BindView(R.id.img_user3)
    ImageView img3User;

    @OnClick(R.id.img_user1)
    void onImage1() {
        imageOps();
    }

    @OnClick(R.id.img_user2)
    void onImage2() {
        imageOps2();
    }

    @OnClick(R.id.btn_finish)
    void onFinish() {
        startActivity(new Intent(this, Home.class));
    }

    @OnClick(R.id.img_user3)
    void onImage3() {
        imageOps3();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        ButterKnife.bind(this);

        user_id = getIntent().getStringExtra("user_id");
        username = getIntent().getStringExtra("username");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "avatar.jpg");

            try {
                FileOutputStream fo = new FileOutputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                photo.compress(Bitmap.CompressFormat.PNG, 50, fo);
                fo.write(bos.toByteArray());
                fo.flush();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            imgUserAvatar.setImageBitmap(photo);
            Glide.with(this).load(file).into(img1User);
            uploadImage(file, data.getData());
        }
        if (requestCode == REQUEST_CHOOSE_PHOTO) {
            if (resultCode == -1) {
                photoURI = Matisse.obtainResult(data).get(0);
                Glide.with(this).load(photoURI).centerCrop().into(img1User);
                uploadImage(photoURI);
            }
        }

        if (requestCode == REQUEST_CHOOSE_PHOTO_2) {
            if (resultCode == -1) {
                photoURI = Matisse.obtainResult(data).get(0);
                Glide.with(this).load(photoURI).centerCrop().into(img2User);
                uploadImage(photoURI);
            }
        }

        if (requestCode == REQUEST_CHOOSE_PHOTO_3) {
            if (resultCode == -1) {
                photoURI = Matisse.obtainResult(data).get(0);
                Glide.with(this).load(photoURI).centerCrop().into(img3User);
                uploadImage(photoURI);
            }
        }
    }

    public void imageOps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload photo")
                .setItems(R.array.upload_photo_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
//                                dispatchTakePictureIntent();
                                break;
                            case 1:
                                pickImage();
                                break;

                        }
                    }
                }).create().show();
    }

    public void imageOps2() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload photo")
                .setItems(R.array.upload_photo_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
//                                dispatchTakePictureIntent();
                                break;
                            case 1:
                                pickImage2();
                                break;

                        }
                    }
                }).create().show();
    }

    public void imageOps3() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload photo")
                .setItems(R.array.upload_photo_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
//                                dispatchTakePictureIntent();
                                break;
                            case 1:
                                pickImage3();
                                break;

                        }
                    }
                }).create().show();
    }

    public void pickImage() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CHOOSE_PHOTO);

    }

    public void pickImage2() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CHOOSE_PHOTO_2);

    }

    public void pickImage3() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CHOOSE_PHOTO_3);

    }


    public void uploadImage(File file, Uri fileUri) {
        RequestBody reqFile = null;
        reqFile = RequestBody.create(MediaType.parse(this.getContentResolver().getType(Uri.parse("content://media/external/images/media"))), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

//        Api.OpenfaceApi api = Api.getClient().create(Api.OpenfaceApi.class);
//        Call<ResponseBody> call = api.uploadAvatar(body);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().string());
//                        mImageURL = jsonObject.getString("url");
//                        Glide.with(getApplicationContext()).load(Api.FILE_SERVER_GET_URL + mImageURL).into(img1User);
//                        imageSet = true;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//
//            }
//        });


    }


    public void uploadImage(Uri uri) {
        File file = new File(getRealPathFromUri(this, uri));
        try {
            File compressedFile = compressImage(file);


            RequestBody reqFile = null;
            reqFile = RequestBody.create(MediaType.parse(this.getContentResolver().getType(uri)), compressedFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), reqFile);
            Api.OpenfaceApi api = Api.getClient().create(Api.OpenfaceApi.class);
            RequestBody userId = RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
            RequestBody userName = RequestBody.create(MediaType.parse("multipart/form-data"), username);
            Call<ResponseBody> createAd = api.uploadAvatar(userId, userName, body);
            createAd.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            Toast.makeText(AddImages.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.d("not succcessful", String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Failure", t.getMessage());

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private File compressImage(File file) throws IOException {
        Compressor compressor = new Compressor(this);
        return compressor.compressToFile(file);

    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
