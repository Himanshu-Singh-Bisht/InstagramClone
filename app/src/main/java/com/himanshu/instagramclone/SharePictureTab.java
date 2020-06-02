package com.himanshu.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTab extends Fragment implements View.OnClickListener {


    private ImageView imgShare;
    private EditText edtDescription;
    private Button btnShareImage;

    private Bitmap recievedImageBitmap;         // as it is needed in onClickListener of both image view and button
    public SharePictureTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        imgShare = view.findViewById(R.id.imgShare);
        edtDescription = view.findViewById(R.id.edtDescription);
        btnShareImage = view.findViewById(R.id.btnShareImage);

        // creating onClickListener
        imgShare.setOnClickListener(SharePictureTab.this);
        btnShareImage.setOnClickListener(SharePictureTab.this);

        return view;
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.imgShare :
                if(android.os.Build.VERSION.SDK_INT >= 23 &&
                        ActivityCompat.checkSelfPermission(getContext() , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE} , 1000);
                }
                else
                {
                    getChosenImage();
                }

                break;

            case R.id.btnShareImage :

                if(recievedImageBitmap != null)         // image is selected
                {
                    if(edtDescription.getText().toString().equals(""))
                    {
                        FancyToast.makeText(getContext() ,"Error : You must specify the description." , FancyToast.LENGTH_SHORT , FancyToast.ERROR , true).show();
                    }
                    else            // codes to upload an image to the server
                    {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();     // is used to contain the image in the form of array
                                                                                            // (as image contain lot of space do it is needed to be stored inn the array to be uploaded easily).

                        recievedImageBitmap.compress(Bitmap.CompressFormat.PNG , 100 , byteArrayOutputStream);      // storing image after compressing it into byteArrayOutputStream
                        byte[] bytes = byteArrayOutputStream.toByteArray();

                        ParseFile parseFile = new ParseFile("img.PNG" , bytes);     // making parse file

                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture" , parseFile);         // putting the parsFile containing image into picture column of Photo class
                        parseObject.put("image_des" , edtDescription.getText().toString());

                        parseObject.put("username" , ParseUser.getCurrentUser().getUsername());     // setting the username column for this class(Photo).

                        //  MAKING DIALOG BOX WHICH WILL SHOWED WHILE IMAGE IS UPLOADING.
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Loading...");
                        dialog.show();

                        // CHECKING IF THE IMAGE IS UPLOADED OR NOT.
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null)
                                {
                                    FancyToast.makeText(getContext() ,"Done!!" , FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();
                                }
                                else
                                {
                                    FancyToast.makeText(getContext() ,"Unknown Error : " + e.getMessage()  , FancyToast.LENGTH_SHORT , FancyToast.ERROR , true).show();
                                }

                                dialog.dismiss();   // to dismiss dialog box
                            }
                        });
                    }
                }
                else
                {
                    FancyToast.makeText(getContext() , "Error : Image should be selected." , FancyToast.LENGTH_SHORT , FancyToast.ERROR , true).show();
                }
                break;
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1000)
        {
            if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getChosenImage();
            }
        }
    }


    private void getChosenImage()       // when permission is granted
    {
        // FancyToast.makeText(getContext() , "Now , we can access the images" , Toast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);       // TO ACCESS THE IMAGE IN EXTERNAL STORAGE.


        startActivityForResult(intent , 2000);      // AS THE ACTIVITY MAY PI MAY NOT GET STARTED
    }

    // to check result for the getChosenImage() startActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2000)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                try
                {
                    Uri selectedImage = data.getData();


//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                    Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(selectedImage , filePathColumn , null , null , null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);     // as it would be stored at 0th index as it is a single value.
//
//                    String picturePath = cursor.getString(columnIndex);

                    // now cursor is not needed as the value is stored in the picturePath(pathName)
//                    cursor.close();

//                    Bitmap recievedImageBitmap = BitmapFactory.decodeFile(picturePath);



                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    recievedImageBitmap = BitmapFactory.decodeStream(imageStream);
                    imgShare.setImageBitmap(recievedImageBitmap);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
