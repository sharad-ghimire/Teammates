//package com.sharad.teammates.activities;
//
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.sharad.teammates.R;
//
//
//public class ChangePhotoDialog extends DialogFragment {
//
//    private static final String TAG = "ChangePhotoDialog";
//
//    public static final int  CAMERA_REQUEST_CODE = 5467;//random number
//    public static final int PICKFILE_REQUEST_CODE = 8352;//random number
//
//    public interface OnPhotoReceivedListener{
//        //this Uri is for Pick file object
//        public void getImagePath(Uri imagePath);
//        //this bitmap is for camera objectc
//        public void getImageBitmap(Bitmap bitmap);
//    }
//
//    OnPhotoReceivedListener mOnPhotoReceived;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.dialogue_open_camera, container, false);
//
//        //Initialize the textview for choosing an image from memory
//        TextView selectPhoto =  view.findViewById(R.id.openFromFiles);
//        selectPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: accessing phones memory.");
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
//            }
//        });
//
//        //Initialize the textview for choosing an image from memory
//        TextView takePhoto =  view.findViewById(R.id.openCamera);
//        takePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: starting camera");
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
//            }
//        });
//
//
//        return view;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        /*
//        Results when selecting new image from phone memory
//         */
//        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
//            Uri selectedImageUri = data.getData();
//
//            //send the bitmap and fragment to the interface
//            mOnPhotoReceived.getImagePath(selectedImageUri);
//            getDialog().dismiss();
//
//        }
//
//        else if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
//
//
//            Bitmap bitmap;
//            bitmap = (Bitmap) data.getExtras().get("data");
//
//            mOnPhotoReceived.getImageBitmap(bitmap);
//            getDialog().dismiss();
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        try{
//            mOnPhotoReceived = (OnPhotoReceivedListener) getActivity();
//        }catch (ClassCastException e){
//            Log.e(TAG, "onAttach: ClassCastException", e.getCause() );
//        }
//        super.onAttach(context);
//    }
//}
