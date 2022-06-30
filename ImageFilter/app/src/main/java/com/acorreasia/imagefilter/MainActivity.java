package com.acorreasia.imagefilter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.acorreasia.imagefilter.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public final int PICK_IMAGE = 1;
    public String[] filterList ={"NORMAL","EXTRA","BLUR9","SHARPEN","GRAYSCALE","SEPIA","BINARY","INVERT","ALPHA BLUE","ALPHA PINK","RED","YELLOW","GREEN"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.button.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri selectedImage = data.getData();
            try {
                //convert uri to bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                binding.image.setImageBitmap(bitmap);
                binding.filters.removeAllViews();
                //get filters list and assign filter to images
                for(int i =0; i<filterList.length; i++){
                    View vi = LayoutInflater.from(this).inflate(R.layout.item_filter_image,null);
                    ImageView imageView = vi.findViewById(R.id.filters_image);
                    switch (filterList[i]){
                        case "EXTRA":
                            imageView.setImageBitmap(getFilterdBitmap(bitmap,getExtra()));
                            imageView.setOnClickListener(v->{binding.image.setImageBitmap(getFilterdBitmap(bitmap,getExtra()));});
                            break;
                        case "BLUR9":
                            imageView.setImageBitmap(getBlur9FillterBitmap(bitmap));
                            imageView.setOnClickListener(v->{binding.image.setImageBitmap(getBlur9FillterBitmap(bitmap));});
                            break;
                        case "SHARPEN":
                            float[] value ={0, -1,  0,-1, 5, -1, 0, -1, 0};
                            imageView.setImageBitmap(getSharpenFilterBitmap(bitmap,value));
                            imageView.setOnClickListener(v->{binding.image.setImageBitmap(getSharpenFilterBitmap(bitmap,value));});
                            break;
                        case "GRAYSCALE":
                            imageView.setImageBitmap(getFilterdBitmap(bitmap,getGrayscale()));
                            imageView.setOnClickListener(v->{binding.image.setImageBitmap(getFilterdBitmap(bitmap,getGrayscale()));});
                            break;
                        case "SEPIA":
                            imageView.setImageBitmap(getFilterdBitmap(bitmap,getSepia()));
                            imageView.setOnClickListener(v->{binding.image.setImageBitmap(getFilterdBitmap(bitmap,getSepia()));});
                            break;
                        case "BINARY":
                            imageView.setImageBitmap(getFilterdBitmap(bitmap,getBinary()));
                            imageView.setOnClickListener(v->{binding.image.setImageBitmap(getFilterdBitmap(bitmap,getBinary()));});
                            break;
                        case "INVERT":
                            imageView.setImageBitmap(getFilterdBitmap(bitmap,getInvert()));
                            imageView.setOnClickListener(v->{binding.image.setImageBitmap(getFilterdBitmap(bitmap,getInvert()));});
                            break;
                        case "ALPHA BLUE":
                            imageView.setImageBitmap(getFilterdBitmap(bitmap,getAlphablue()));
                            imageView.setOnClickListener(v->{binding.image.setImageBitmap(getFilterdBitmap(bitmap,getAlphablue()));});
                            break;
                        case "ALPHA PINK":
                            imageView.setImageBitmap(getFilterdBitmap(bitmap,getAlphapink()));
                            imageView.setOnClickListener(v->{binding.image.setImageBitmap(getFilterdBitmap(bitmap,getAlphapink()));});
                            break;
                        case "RED":
                            imageView.setImageBitmap(getRedFilterdBitmap(bitmap));
                            imageView.setOnClickListener(v->{binding.image.setImageBitmap(getRedFilterdBitmap(bitmap));});
                            break;
                        case "YELLOW":
                            imageView.setImageBitmap(getYellowFilterdBitmap(bitmap));
                            imageView.setOnClickListener(v->{binding.image.setImageBitmap(getYellowFilterdBitmap(bitmap));});
                            break;
                        case "GREEN":
                            imageView.setImageBitmap(getGreenFilterdBitmap(bitmap));
                            imageView.setOnClickListener(v->{binding.image.setImageBitmap(getGreenFilterdBitmap(bitmap));});
                            break;
                        default:
                            imageView.setImageBitmap(bitmap);
                            imageView.setOnClickListener(v->{binding.image.setImageBitmap(bitmap);});
                    }
                    binding.filters.addView(vi);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getFilterdBitmap(Bitmap bitmap, ColorMatrix colorMatrix){
        Bitmap image = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap,0,0,paint);
        return image;
    }

    private Bitmap getRedFilterdBitmap(Bitmap bitmap){
        Bitmap image = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setColorFilter(new LightingColorFilter(Color.RED, 0));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return image;
    }

    private Bitmap getYellowFilterdBitmap(Bitmap bitmap){
        Bitmap image = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setColorFilter(new LightingColorFilter(Color.YELLOW, 0));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return image;
    }

    private Bitmap getGreenFilterdBitmap(Bitmap bitmap){
        Bitmap image = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setColorFilter(new LightingColorFilter(Color.GREEN, 0));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return image;
    }

    private Bitmap getSharpenFilterBitmap(Bitmap original, float[] coefficients ) {
        Bitmap bitmap = Bitmap.createBitmap(
                original.getWidth(), original.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript rs = RenderScript.create(this);

        Allocation allocIn = Allocation.createFromBitmap(rs, original);
        Allocation allocOut = Allocation.createFromBitmap(rs, bitmap);

        ScriptIntrinsicConvolve3x3 convolution
                = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));
        convolution.setInput(allocIn);
        convolution.setCoefficients(coefficients);
        convolution.forEach(allocOut);

        allocOut.copyTo(bitmap);
        rs.destroy();
        return bitmap;
    }

    private Bitmap  getBlur9FillterBitmap(Bitmap original) {
        Bitmap bitmap = Bitmap.createBitmap(
                original.getWidth(), original.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript rs = RenderScript.create(this);

        Allocation allocIn = Allocation.createFromBitmap(rs, original);
        Allocation allocOut = Allocation.createFromBitmap(rs, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, Element.U8_4(rs));
        blur.setInput(allocIn);
        blur.setRadius(9);
        blur.forEach(allocOut);

        allocOut.copyTo(bitmap);
        rs.destroy();
        return bitmap;
    }


    //get grayscale color matrix
    private ColorMatrix getGrayscale(){
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        return  colorMatrix;
    }

    //get grayscale color matrix
    private ColorMatrix getExtra(){
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(8);
        return  colorMatrix;
    }

    //get sepia color matrix
    private ColorMatrix getSepia(){
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        ColorMatrix colorScale = new ColorMatrix();
        colorScale.setScale(1, 1, 0.8f, 1);

        // Convert to grayscale, then apply brown color
        colorMatrix.postConcat(colorScale);

        return colorMatrix;
    }

    //get binary color matrix
    private ColorMatrix getBinary() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        float m = 255f;
        float t = -255*128f;
        ColorMatrix threshold = new ColorMatrix(new float[] {
                m, 0, 0, 1, t,
                0, m, 0, 1, t,
                0, 0, m, 1, t,
                0, 0, 0, 1, 0
        });

        // Convert to grayscale, then scale and clamp
        colorMatrix.postConcat(threshold);

        return colorMatrix;
    }

    //get Invert color matrix
    private ColorMatrix getInvert() {
        return new ColorMatrix(new float[] {
                -1,  0,  0,  0, 255,
                0, -1,  0,  0, 255,
                0,  0, -1,  0, 255,
                0,  0,  0,  1,   0
        });
    }

    //get alpha blue color matrix
    private ColorMatrix getAlphablue() {
        return new ColorMatrix(new float[] {
                0,    0,    0, 0,   0,
                0.3f,    0,    0, 0,  50,
                0,    0,    0, 0, 255,
                0.2f, 0.4f, 0.4f, 0, -30
        });
    }

    //get alpha pink color matrix
    private ColorMatrix getAlphapink() {
        return new ColorMatrix(new float[] {
                0,    0,    0, 0, 255,
                0,    0,    0, 0,   0,
                0.2f,    0,    0, 0,  50,
                0.2f, 0.2f, 0.2f, 0, -20
        });
    }
}