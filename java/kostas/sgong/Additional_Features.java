package kostas.sgong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class Additional_Features extends AppCompatActivity {
   public static boolean Apply_feat = false;
    public static boolean Mediaan,Grad,contr,Sobl,lbp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additional__features);
    }


    public void Pass_Values(View view) {

        Switch Median = (Switch) findViewById(R.id.MedianSwitch);
        Switch Gradient = (Switch) findViewById(R.id.GradientSwitch);
        Switch Contrast = (Switch) findViewById(R.id.contrastSwitch);
        Switch Sobel = (Switch) findViewById(R.id.sobelSwitch);
        Switch LBP = (Switch) findViewById(R.id.lbpSwitch);

/*
        Median.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                      Mediaan = true;
                } else {
                    // The toggle is disabled
                }
            }
        });
*/
        // Mediaan = Boolean.parseBoolean(Median.getText().toString());
         Mediaan = Median.isChecked();
        //Neurons.Median_Enabled = Mediaan;

        // Grad = Boolean.parseBoolean(Gradient.getText().toString());
       // Neurons.Gradient_Enabled = Grad;
        Grad =  Gradient.isChecked();

       //  contr = Boolean.parseBoolean(Contrast.getText().toString());
       // Neurons.Contrast_Enabled = contr;
        contr = Contrast.isChecked();

         //Sobl = Boolean.parseBoolean(Sobel.getText().toString());
        //Neurons.Sobel_Enabled = Sobl;
        Sobl = Sobel.isChecked();


         lbp = Boolean.parseBoolean(LBP.getText().toString());
        //Neurons.LBP_Enabled = lbp;
        lbp = LBP.isChecked();


        Apply_feat = true;




        Toast.makeText(getApplicationContext(),"Applied!",Toast.LENGTH_SHORT).show();

    }

    public boolean getStateMedian(){

        return Mediaan;
    }

    public boolean getState(){
        return Apply_feat;
    }



}
