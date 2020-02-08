package kostas.sgong;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

//import com.example.lib.Neurons;

public class Main_Menu extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    public static int Samples;
    ImageView imageView;
    public static Bitmap bitmap;
   public  int [] Sampling;
    boolean ImageLoaded = false;
   public static boolean Applied_feat = false;
    public static boolean Applied_values = false;
    public static boolean Median,Sobel,LBP,Contrast,Gradient;

    public static int Sampless,epochs,Max_neurons,Nidle,Alphaa,epoch_thresholdd,Rmax;
    public static double m1,m2,e1max,e1min;
    TextView textView;

    TextView textView2;
    private ProgressBar Progress;
    private int progressStatus = 1;
    private Handler myHandler = new Handler();
  //  private boolean Started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main__menu);
            textView = (TextView) findViewById(R.id.PreviewText);
            imageView = (ImageView) findViewById(R.id.imageView);
            textView2 = (TextView) findViewById(R.id.TellColor);



    }

    public void onClick(View view) {
        Intent i = new Intent(Main_Menu.this, SchrollViewSettings.class);
        startActivity(i);

    }

    public void onClickStart(View view) throws MalformedURLException, URISyntaxException, FileNotFoundException {




        if (Additional_Features.Apply_feat) {
            Applied_feat = true;
             Median = Additional_Features.Mediaan;
             LBP = Additional_Features.lbp;
             Sobel = Additional_Features.Sobl;
             Gradient = Additional_Features.Grad;
             Contrast = Additional_Features.contr;

        }



        if (SchrollViewSettings.Apply_val) {
            Applied_values = true;
             Sampless = SchrollViewSettings.Samples;
             epochs = SchrollViewSettings.epochss;   //Max number of epochs.
             m1 = SchrollViewSettings.m1p;
             m2 = SchrollViewSettings.m2p;
             Nidle = SchrollViewSettings.Nidlee;
             Max_neurons = SchrollViewSettings.Max_neuronss;
             Alphaa = SchrollViewSettings.Alphaa;
            epoch_thresholdd = SchrollViewSettings.epoch_thresh;
            Rmax = SchrollViewSettings.rmax;
            e1max = SchrollViewSettings.e1max;
            e1min = SchrollViewSettings.e1min;

        }






        if (ImageLoaded ) {

            BitmapRead Image = new BitmapRead();
        if (Applied_values==false) {        //check if the User enters his values.
            Sampless = 3000;  //default value.
        }




           //In case user repress  Start for training, initialize again the messages on the screen.
           //


           // textView.invalidate();
           // textView.requestLayout();

           //


                Neurons Start = new Neurons(Sampless);





            textView.setText("Final Image");
            Bitmap fin = Neurons.getBitmap();
            Uri result = getUri(this,fin);

            imageView.setImageURI(result);
            int neurons = Start.getNumberColors();
            textView2.setText("Number of colors = "+neurons);


            Toast.makeText(getApplicationContext(),"Training Complete!",Toast.LENGTH_SHORT).show();



        }else{
            Toast.makeText(getApplicationContext(),"Image not Loaded!",Toast.LENGTH_SHORT).show();

        }



    }

    public Uri getUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }





public int[] getSampling(){
        return Sampling;
}

    public void onclick_LoadImage(View view) {
        openGallery();
    }

    public void openFeatures(View view){
        Intent feat = new Intent(Main_Menu.this,Additional_Features.class);
        startActivity(feat);
    }

    public void openGallery(){

        Intent Gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Gallery,PICK_IMAGE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

         if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
             imageUri = data.getData();
             imageView.setImageURI(imageUri);

             ImageLoaded = true;
             try {
                 bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

             } catch (IOException e) {
                 e.printStackTrace();
             }

         }
    }







}







