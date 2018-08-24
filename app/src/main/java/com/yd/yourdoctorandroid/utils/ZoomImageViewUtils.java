package com.yd.yourdoctorandroid.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ZoomImageViewUtils {
    public static void zoomImage(Context context, String urlImage){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.certification_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        PhotoView certification_photo_view = dialog.findViewById(R.id.certification_photo_view);
        Button btn_cancel_from_photo_view = dialog.findViewById(R.id.btn_cancel_from_photo_view);

        Picasso.with(context).load(urlImage).into(certification_photo_view);

        btn_cancel_from_photo_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void loadImageManual(Context context , String url, ImageView imageView){
        if(url != null && !url.isEmpty()){
            Picasso.with(context).load(url).into(imageView);
        }
    }

    public static void loadCircleImage(Context context , String url, ImageView imageView){
        if(url != null && !url.isEmpty()){
            Picasso.with(context).load(url).transform(new CropCircleTransformation()).into(imageView);
        }
    }
}
