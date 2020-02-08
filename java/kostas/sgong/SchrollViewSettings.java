package kostas.sgong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SchrollViewSettings extends AppCompatActivity {
    public static int Samples,Nidlee,Max_neuronss,epochss,Alphaa,epoch_thresh,rmax;
    public static double m1p,m2p,e1max,e1min;
    public static boolean Apply_val=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schroll_view_settings);
    }

    public void Pass_Values(View view){

        EditText Nidle = (EditText) findViewById(R.id.Nidle);
        EditText Max_neurons = (EditText) findViewById(R.id.Max_Neurons);
        EditText epochs = (EditText) findViewById(R.id.epochs);
        EditText epochs_threshold = (EditText) findViewById(R.id.epoch_threshold);
        EditText samples = (EditText) findViewById(R.id.Samples);
        EditText m1 = (EditText) findViewById(R.id.m1);
        EditText m2 = (EditText) findViewById(R.id.m2);
        EditText alpha = (EditText) findViewById(R.id.Alpha);
        EditText Rmax = (EditText) findViewById(R.id.Rmax);
        EditText e1max1 = (EditText) findViewById(R.id.e2max);
        EditText e1min1 = (EditText) findViewById(R.id.e1min);



        Nidlee = Integer.parseInt(Nidle.getText().toString());
        //Neurons.Nidle = Nidlee;         //Pass the value.
        Max_neuronss = Integer.parseInt(Max_neurons.getText().toString());
       // Neurons.Max_neurons = Max_neuronss;
        epochss = Integer.parseInt(epochs.getText().toString());
        //Neurons.Max_epochs = epochss;
        Samples = Integer.parseInt(samples.getText().toString());
        Main_Menu.Samples = Samples;
        m1p =  Double.parseDouble(m1.getText().toString());
        //Neurons.m1 = m1p;
        m2p =  Double.parseDouble(m2.getText().toString());
        //Neurons.m2 = m2p;
        Alphaa = Integer.parseInt(alpha.getText().toString());
       // Neurons.alpha = Alphaa;         //Pass the value.
        epoch_thresh = Integer.parseInt(epochs_threshold.getText().toString());
        //Neurons.epoch_Threshold = epoch_thresh;
        rmax = Integer.parseInt(Rmax.getText().toString());
        e1max = Double.parseDouble(e1max1.getText().toString());
        e1min = Double.parseDouble(e1min1.getText().toString());




        Toast.makeText(getApplicationContext(),"Applied!",Toast.LENGTH_SHORT).show();

        Apply_val = true;
    }






}
