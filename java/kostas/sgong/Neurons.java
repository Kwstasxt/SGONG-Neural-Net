package kostas.sgong;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Neurons extends BitmapRead {
    public  int neurons = 0;

    private int X_axis;
    private int Y_axis;
    private double [][] Temp_Weights;
    private double normalize,normalize2;
    private  int[] Weight;

    private  int Winner_neuron;
    private  int Second_neuron_pos;
    private  double e1, r1;
    private  double[] e2;
    private  double[] sum;
    public  int epochs = 1;

    private  double[] E1, E2; //Represents the learning rate for W1 and W2.
    private  int N[]; // Represents the  refresh of each neuron.  [neurons][epochs]

    public  double m1 = 0.1, m2 = 0.2;  //thresholds for neuron adding/removing.
    private  int Samples;

    private  double e1max = 0.4;//0.019995; //0.599995;
    private  double e1min = 0.005; //0.01;
    private  int r1max = 400;  //0.5
    private int At_Least_epochs = 50; //How many epochs must at least pass even if neurons have trained all. Usefull for first epochs that we have only 2 neurons.

    private  boolean Hibernate[];

    private int [] gray;
    private int counter = 0;  //for masks.
    public  int Nidle = 6000; //Choosen by the user.
    private  int Beta = 10;  //if a neuron is not winner neuron for beta epochs then it is considered idle neuron.
    public  int alpha = 300;
    private int[] Sapling;

    private  int idle_neuron[];    //Variable that stores for each neuron that it was not selected a winner neuron for current epoch.
    private  int Nq, q;
    private double    E2q;
    private  double Max_b, Min_a; //for finding the max ratio and min and use it to remove unimportant neurons.
    private  double S[][];  //Array used to display neural connections. For example if S[i][j] = X the i and j have euclid distance X*

    private  int nei[];
    public  int Max_neurons = 15;
    public  int Max_epochs = 600;
    public  int epoch_Threshold = 400;
    //if you want to deactivate this feature, set this equal or bigger of Max_epochs.
//More info about epoch_Threshold. After this epoch the number of neurons does not change.
    private  int a_position;
    private boolean okay = false;
    private boolean Gray_okay = false;

    private int [] N_Single_epoch;
    public  boolean Median_Enabled = false, Gradient_Enabled = false, Contrast_Enabled = false, Sobel_Enabled = false, LBP_Enabled = false;

    private  boolean isWinner[];
    private ArrayList<Integer> Exists = new ArrayList<Integer>(); //Shows which neurons are currently in the network.
    private ArrayList<Integer> Removed_Neuron = new ArrayList<Integer>();  //Shows which neurons are removed.
    public Uri img_uri;
    public int[][] image;
    private double [] Median_Samples,Median_Neuron;
    private int [] LBP_Samples,LBP_Neuron;
    private int [] Sobel_Samples,Sobel_Neuron;
    private int [] Gradient_Samples,Gradient_Neuron;
    private double [] Contrast_Samples,Contrast_Neuron;


    public void Initialize_neurons(int samples) {  //Initializing neurons Weights.

        //Check for user input of Features
        if (Main_Menu.Applied_feat) {
            Median_Enabled = Main_Menu.Median;
            Contrast_Enabled = Main_Menu.Contrast;
            LBP_Enabled = Main_Menu.LBP;
            Gradient_Enabled = Main_Menu.Gradient;
            Sobel_Enabled = Main_Menu.Sobel;
        }

        //Check for user input of Values.
        if (Main_Menu.Applied_values) {
            Max_neurons = Main_Menu.Max_neurons;
            Max_epochs = Main_Menu.epochs;
            m1 = Main_Menu.m1;
            m2 = Main_Menu.m2;
            Nidle = Main_Menu.Nidle;
            alpha = Main_Menu.Alphaa;
            epoch_Threshold = Main_Menu.epoch_thresholdd;
            r1max = Main_Menu.Rmax;
            e1max = Main_Menu.e1max;
            e1min = Main_Menu.e1min;

        }

        X_axis = getWidth();
        Y_axis = getHeight();
        image = readImagevalues();

        Initialize_features(samples);




        Temp_Weights = new double[Max_neurons+1][3];
        Weight = new int[Max_neurons + 1];



        Random rand = new Random(); //Creates an instance of random method.
        int n_x = rand.nextInt(X_axis);  //Represents a random value for x axis of image.
        int n_y = rand.nextInt(Y_axis);
        neurons++;

        Exists.add(1);


        Weight[neurons] = (image[n_x][n_y]);
        int r,g,b;
        r = (Weight[1]>>16)& 0xff;
        g = (Weight[1]>>8)& 0xff;
        b = (Weight[1])& 0xff;

        Temp_Weights[1][0] = r;
        Temp_Weights[1][1] = g;
        Temp_Weights[1][2] = b;

        if (Median_Enabled) {
            Mask_3x3_Updated(n_x,n_y);
          //  Mask_3x3(image[n_x][n_y]);

            Median_Neuron[neurons] = Median();
        }
        int gray = find_Gray(image[n_x][n_y]);
        create_Sample_Masks(image[n_x][n_y],gray,n_x,n_y);
        if (LBP_Enabled) {

            LBP_Neuron[neurons] = LBP(gray);
        }
        if (Sobel_Enabled) {

            Sobel_Neuron[neurons] = Sobel();
        }
        if (Contrast_Enabled) {

            Contrast_Neuron[neurons] = Contrast();
        }
        if (Gradient_Enabled) {

            Gradient_Neuron[neurons] = Gradient();
        }






        Continue();


    }

    //This method adds a second neuron and initializes it with a variable.
    public void Continue() {

        Random rand = new Random(); //Creates an instance of random method.
        int n_x = rand.nextInt(X_axis);  //Represents a random value for x axis of image.
        int n_y = rand.nextInt(Y_axis);
        neurons++;

        Weight[neurons] = (image[n_x][n_y]);
        int r,g,b;
        r = (Weight[2]>>16)& 0xff;
        g = (Weight[2]>>8)& 0xff;
        b = (Weight[2])& 0xff;

        Temp_Weights[2][0] = r;
        Temp_Weights[2][1] = g;
        Temp_Weights[2][2] = b;
        if (Median_Enabled) {
            Mask_3x3_Updated(n_x,n_y);
           // Mask_3x3(image[n_x][n_y]);

            Median_Neuron[neurons] = Median();
        }
        int gray = find_Gray(image[n_x][n_y]);
        create_Sample_Masks(image[n_x][n_y],gray,n_x,n_y);
        if (LBP_Enabled) {

            LBP_Neuron[neurons] = LBP(gray);
        }
        if (Sobel_Enabled) {

            Sobel_Neuron[neurons] = Sobel();
        }
        if (Contrast_Enabled) {

            Contrast_Neuron[neurons] = Contrast();
        }
        if (Gradient_Enabled) {

            Gradient_Neuron[neurons] = Gradient();
        }

        while (Weight[2] == Weight[1]) {
            n_x = rand.nextInt(X_axis);
            n_y = rand.nextInt(Y_axis);
            Weight[2] = image[n_x][n_y];

            r = (Weight[2]>>16)& 0xff;
            g = (Weight[2]>>8)& 0xff;
            b = (Weight[2])& 0xff;

            Temp_Weights[2][0] = r;
            Temp_Weights[2][1] = g;
            Temp_Weights[2][2] = b;
            if (Median_Enabled) {
                Mask_3x3_Updated(n_x,n_y);
                //Mask_3x3(image[n_x][n_y]);

                Median_Neuron[neurons] = Median();
            }
             gray = find_Gray(image[n_x][n_y]);
            create_Sample_Masks(image[n_x][n_y],gray,n_x,n_y);
            if (LBP_Enabled) {

                LBP_Neuron[neurons] = LBP(gray);
            }
            if (Sobel_Enabled) {

                Sobel_Neuron[neurons] = Sobel();
            }
            if (Contrast_Enabled) {

                Contrast_Neuron[neurons] = Contrast();
            }
            if (Gradient_Enabled) {

                Gradient_Neuron[neurons] = Gradient();
            }
        }

        Exists.add(2);


        isWinner = new boolean[Max_neurons + 1];

        isWinner[1] = false;
        isWinner[2] = false;


    }


    public Neurons(int samples) {





        Initialize_neurons(samples);





        Samples = samples;



        Initialize_All();
        //Two neurons first.
        S[1][2] = 0;
        S[2][1] = 0;

        Error_accumulated_elimination();


        One_Method_to_Rule_Them_All();


    }

    public void One_Method_to_Rule_Them_All() {



        int Image;

        int i;
        Set_S();
         Sapling = Sampling(image, Samples);

         create_features_values(Sapling,Samples);


        for (epochs = 1; epochs <= Max_epochs; epochs++) {
            Error_accumulated_elimination();
            Set_S();
            for (i = 0; i < Samples; i++) {
                Image = Sapling[i];

                winner_neuron(Image,i);
                Second_Neuron();

                local_error(Image,i, Winner_neuron, Second_neuron_pos);
                Update_e1_e2();
                Weights_Update(Image,i);

                winner_neighboor_update_connection();


            }
            Well_trained_2();




        }




        break_program();



    }


    public void Initialize_features(int samples){
        boolean isOk = false;
        if (Median_Enabled) {

            Median_Samples = new double[samples];
            Median_Neuron = new double[Max_neurons+1];
        }

        if (Sobel_Enabled) {
            isOk = true;
            Sobel_Samples = new int[samples];
            Sobel_Neuron = new int[Max_neurons+1];
        }
        if (Contrast_Enabled) {
            isOk = true;
            Contrast_Samples = new double[samples];
            Contrast_Neuron = new double[Max_neurons+1];
        }
        if (Gradient_Enabled) {
            isOk = true;
            Gradient_Samples = new int[samples];
            Gradient_Neuron = new int[Max_neurons+1];
        }

        if (LBP_Enabled) {
            isOk = true;
            LBP_Samples = new int[samples];
            LBP_Neuron = new int[Max_neurons+1];
        }

        if (isOk){

            int [][] Red = readRedvalues();
            int [][] green = readGreenvalues();
            int [][] blue = readBluevalues();

            int [][] gray = grayscale_image(Red,green,blue); //creates Grayscale Image
        }



    }





    //Step 0.
    private void Initialize_All() {
        E1 = new double[Max_neurons + 1];
        E2 = new double[Max_neurons + 1];

        S = new double[Max_neurons + 1][Max_neurons + 1];
       nei = new int[Max_neurons + 1];
        N = new int[Max_neurons + 1];

        sum = new double[Max_neurons + 1];
        idle_neuron = new int[Max_neurons + 1];

        e2 = new double[Max_neurons + 1];
        Hibernate = new boolean[Max_neurons + 1];
        N_Single_epoch = new int[Max_neurons+1];
        for (int i = 1; i <= Max_neurons; i++) {
            Hibernate[i] = false;
        }
    }


    //Step 1.
    public void Error_accumulated_elimination() { //Method for error elimination. Everytime we advance an epoch

        for (int i = 1; i <= Max_neurons; i++) {
            E1[i] = 0;
            E2[i] = 0;
            N_Single_epoch[i] = 0;
        }

    }




    //Step 2.
    public int winner_neuron(int data,int Samp) {  // |Xk-Xw1| < |Xk-Wi|


        int Red_Data = (data >> 16) & 0xff;
        int Green_Data = (data >> 8) & 0xff;
        int Blue_Data = (data) & 0xff;
        //Additional features
        int  LBP_Data = 0, Gradient = 0, Sobel = 0;
        int  LBP_Neuron = 0, Gradient_Neuron = 0, Sobel_Neuron = 0, Gray_neuron = 0;
        double Contrast = 0, Contrast_Neuron = 0;
        double median = 0,median_Neuron = 0;

        if (counter==1) {
            if (Median_Enabled == true) {

                median = Median_Samples[Samp];
            }

            if (LBP_Enabled == true) {

                LBP_Data = LBP_Samples[Samp];
            }

            if (Contrast_Enabled == true) {

                Contrast = Contrast_Samples[Samp];
            }

            if (Gradient_Enabled == true) {

                Gradient = Gradient_Samples[Samp];
            }

            if (Sobel_Enabled == true) {

                Sobel = Sobel_Samples[Samp];
            }
        }



    int index,k;

        for ( index=0; index<Exists.size();index++) {
            k = Exists.get(index);



                sum[k] = 0;

            double	Red_neuron = Temp_Weights[k][0];
            double	Green_neuron = Temp_Weights[k][1];
            double	Blue_neuron = Temp_Weights[k][2];


           if (counter==1) {
               if (Median_Enabled == true) {


                   median_Neuron = Median_Neuron[k];
               }

               if (LBP_Enabled == true) {


                   LBP_Neuron = this.LBP_Neuron[k];

               }

               if (Contrast_Enabled == true) {

                   Contrast_Neuron = this.Contrast_Neuron[k];
               }

               if (Gradient_Enabled == true) {

                   Gradient_Neuron = this.Gradient_Neuron[k];
               }

               if (Sobel_Enabled == true) {

                   Sobel_Neuron = this.Sobel_Neuron[k];
               }
           }



                 sum[k] = (int) (sum[k] + (Red_Data - Red_neuron)*(Red_Data - Red_neuron) + (Green_Data - Green_neuron)*(Green_Data - Green_neuron) + (Blue_Data - Blue_neuron)*(Blue_Data - Blue_neuron));
                    //sum[k] = Math.sqrt(sum[k]);

            //	Neurons.sum[k] = (int) Math.pow(sum[k],0.5);

            //Sum with the additional features
            sum[k] = (int) (sum[k] + (median - median_Neuron)*(median - median_Neuron) + (Contrast - Contrast_Neuron)*(Contrast - Contrast_Neuron) + (Sobel - Sobel_Neuron)*(Sobel - Sobel_Neuron) + (Gradient - Gradient_Neuron)*(Gradient - Gradient_Neuron) + (LBP_Data - LBP_Neuron)*(LBP_Data - LBP_Neuron));

            //Neurons.sum[k] = (int) (sum[k] + Math.pow(median - median_Neuron, 2) + Math.pow(Contrast - Contrast_Neuron, 2) + Math.pow(Sobel - Sobel_Neuron, 2) + Math.pow(Gradient - Gradient_Neuron, 2) + Math.pow(LBP_Data - LBP_Neuron, 2));

                //	Neurons.sum[k] = (int) Math.pow(sum[k],0.5);
            }


        double min = Integer.MAX_VALUE;

        for ( index=0; index<Exists.size();index++) {
            k = Exists.get(index);
                if (sum[k] < min) {
                    min = sum[k];
                    Winner_neuron = k;
                }
            }

        isWinner[Winner_neuron] = true;

        return Winner_neuron;


    }


    //Step 2.
    public int Second_Neuron() {

        double first, second;
        first = second = Integer.MAX_VALUE;

        int i,index;
        for ( index=0; index<Exists.size();index++) {
             i = Exists.get(index);
                if (sum[i] < first) {
                    second = first;
                    first = sum[i];

                } else if (sum[i] < second && sum[i] != first) {
                    second = sum[i];

                }
            }


        for ( index=0; index<Exists.size();index++) {
            i = Exists.get(index);
                if (sum[i] == second)
                    Second_neuron_pos = i;
            }


        //debug
        if (Second_neuron_pos == Winner_neuron) {
            System.out.println("Mega Error ----  Second neuron = Winner");
            Second_neuron_pos = 2;

        }



        return Second_neuron_pos;
    }

    //Step 3.
    public void local_error(int data,int Sample, int Winner_neuron, int Second_neuron_pos) {    //E1,E2 for winner and second neuron.
       double a = 0;
        double b = 0;

        a = a + RGB_Diff_data(data,Sample, Winner_neuron);
        b = b + RGB_Diff_data(data,Sample, Second_neuron_pos);


        E1[Winner_neuron] = E1[Winner_neuron] + a;
        E2[Winner_neuron] = E2[Winner_neuron] + b;
        N[Winner_neuron] = N[Winner_neuron] + 1;
        N_Single_epoch[Winner_neuron] = N_Single_epoch[Winner_neuron] + 1;
        // System.out.printf("E1 is now %d \n",E1[Winner_neuron]); //Debug message
        // System.out.printf("E2 is now %d \n",E2[Winner_neuron]); //Debug message


    }


    //Step 4.				//e1,e2 are the Learning Rates.
    public void Update_e1_e2() {   //Sgong need to change the values of e1,e2 throughout the program.
        double ratio1 = e1max / e1min;
        double ratio2 = (double) N[Winner_neuron] / Nidle;
        double ratio3, e1m;
        int index,i;
        if (ratio2 <= 1) {
            e1 = (double) (e1max + e1min - e1min * Math.pow(ratio1, ratio2));

        } else {

            e1 = e1min;
        }

        boolean method= true;
        if (method) {
            for (index = 0; index < Exists.size(); index++) {
                i = Exists.get(index);
                if (S[Winner_neuron][i]>=0){

                    ratio3 = (double) N[i] / Nidle;
                    if (ratio3 <= 1) {
                        r1 = (double) (r1max + 1 - r1max * Math.pow(1 / r1max, ratio3));
                        e1m = (double) (e1max + e1min - e1min * Math.pow(ratio1, ratio3));

                        e2[i] = (double) e1m * (1 / r1);             // 0.001;
                    } else {
                        e2[i] = 0;
                    }
                }
            }
        }else{
            if (ratio2<=1){
                r1 = (double) (r1max + 1 - r1max * Math.pow(1 / r1max, ratio2));
                e1m = (double) (e1max + e1min - e1min * Math.pow(ratio1, ratio2));

                e2[Winner_neuron] = (double) e1m * (1 / r1);             // 0.001;
            }else{
                e2[Winner_neuron] = 0;
            }

        }



    }




    //Step 5. Update neuron weights.
    public void Weights_Update(int data,int Samp) {

        int r, g, b;

        if (Hibernate[Winner_neuron] == false) {






        r = (data >> 16) & 0xff;
        g = (data >> 8) & 0xff;
        b = (data) & 0xff;


            Temp_Weights[Winner_neuron][0] = Temp_Weights[Winner_neuron][0] + e1*(r-Temp_Weights[Winner_neuron][0]);
            Temp_Weights[Winner_neuron][1] = Temp_Weights[Winner_neuron][1] + e1*(g-Temp_Weights[Winner_neuron][1]);
            Temp_Weights[Winner_neuron][2] = Temp_Weights[Winner_neuron][2] + e1*(b-Temp_Weights[Winner_neuron][2]);
            if (counter==1) {  //if any feature is on check this first.
                if (Median_Enabled) {
                    Median_Neuron[Winner_neuron] = (int) Math.round(Median_Neuron[Winner_neuron] + e1 * (Median_Samples[Samp] - Median_Neuron[Winner_neuron]));
                }

                if (LBP_Enabled) {
                    LBP_Neuron[Winner_neuron] = (int) Math.round(LBP_Neuron[Winner_neuron] + e1 * (LBP_Samples[Samp] - LBP_Neuron[Winner_neuron]));
                }
                if (Sobel_Enabled) {
                    Sobel_Neuron[Winner_neuron] = (int) Math.round(Sobel_Neuron[Winner_neuron] + e1 * (Sobel_Samples[Samp] - Sobel_Neuron[Winner_neuron]));
                }
                if (Gradient_Enabled) {
                    Gradient_Neuron[Winner_neuron] = (int) Math.round(Gradient_Neuron[Winner_neuron] + e1 * (Gradient_Samples[Samp] - Gradient_Neuron[Winner_neuron]));
                }
                if (Contrast_Enabled) {
                    Contrast_Neuron[Winner_neuron] = (int) Math.round(Contrast_Neuron[Winner_neuron] + e1 * (Contrast_Samples[Samp] - Contrast_Neuron[Winner_neuron]));
                }
            }
           // RGBw1 = 0xff000000 | (Red << 16) | (Green << 8) | Blue;

          //  Weight[Winner_neuron] = RGBw1;
        }




        int index,i;

        for (index = 0; index < Exists.size(); index++) {
            i = Exists.get(index);
            if (S[Winner_neuron][i] >= 0) {
                if (Hibernate[i]==false) {




                    r = (data >> 16) & 0xff;
                    g = (data >> 8) & 0xff;
                    b = (data) & 0xff;

                    Temp_Weights[i][0] = Temp_Weights[i][0] + e2[i]*(r-Temp_Weights[i][0]);
                    Temp_Weights[i][1] = Temp_Weights[i][1] + e2[i]*(g-Temp_Weights[i][1]);
                    Temp_Weights[i][2] = Temp_Weights[i][2] + e2[i]*(b-Temp_Weights[i][2]);

                    if (counter==1) {
                        if (Median_Enabled) {
                            Median_Neuron[i] = (int) Math.round(Median_Neuron[i] + e2[i] * (Median_Samples[Samp] - Median_Neuron[i]));
                        }

                        if (LBP_Enabled) {
                            LBP_Neuron[i] = (int) Math.round(LBP_Neuron[i] + e2[i] * (LBP_Samples[Samp] - LBP_Neuron[i]));
                        }
                        if (Sobel_Enabled) {
                            Sobel_Neuron[i] = (int) Math.round(Sobel_Neuron[i] + e2[i] * (Sobel_Samples[Samp] - Sobel_Neuron[i]));
                        }
                        if (Gradient_Enabled) {
                            Gradient_Neuron[i] = (int) Math.round(Gradient_Neuron[i] + e2[i] * (Gradient_Samples[Samp] - Gradient_Neuron[i]));
                        }
                        if (Contrast_Enabled) {
                            Contrast_Neuron[i] = (Contrast_Neuron[index] + e2[i] * (Contrast_Samples[Samp] - Contrast_Neuron[i]));


                        }
                    }


                        }

                }
            }


    }




    //Step 6. Increase connection age of the winner neuron and its neighboors.
    public void winner_neighboor_update_connection() {
 if (epochs>1) {
     S[Winner_neuron][Second_neuron_pos] = 0;        //The connection of these two neurons is refreshed.
     S[Second_neuron_pos][Winner_neuron] = 0;

     int index, i;

     for (index = 0; index < Exists.size(); index++) {
         i = Exists.get(index);
         if (Winner_neuron != i) {

             if (S[Winner_neuron][i] >= 0 && i != Second_neuron_pos) {

                 S[Winner_neuron][i]++;
                 S[i][Winner_neuron]++;
             }
         }
     }
 }


    }


    //Step 7. After all Samples there is a processing of neurons.
    public void Well_trained_2() {
        boolean istrained = false;
        int M = 0;
        int index,j;

        for (index = 0; index < Exists.size(); index++) {
            j = Exists.get(index);
            if (N[j] >= Nidle) {
                Hibernate[j] = true;
                M++;
            }
        }
        if (M == neurons) {

            if (epochs >= At_Least_epochs) {
                istrained = true;

                   // System.out.println("Neurons trained All");

            //    break_program();
                epochs = Max_epochs;  //Skip training and go to Main Menu class
            }
        }
        if (istrained == false) {
            check_age();
        }
        if (epochs <= epoch_Threshold) {
            if (istrained==false) {
                Idle_neurons();
                Neuron_add();

                Remove_unimportant_neurons();
            }
        }

        for (int i1 = 1; i1 <=Max_neurons; i1++) {
            isWinner[i1] = false;
        }


    }





//Neuron add-Remove Methods.
//----------------------------------------------------------------------------//
//----------------------------------------------------------------------------\\

    //Method for Neuron_remove. Calls Neuron_remove
    //Method that finds idle neurons after Beta epochs.
    public void Idle_neurons() {
        int index,i;

        for (index = 0; index < Exists.size(); index++) {
            i = Exists.get(index);
                if (isWinner[i] == false) {
                    idle_neuron[i]++;
                }
            }

        if (neurons > 2) {
            Neuron_remove();
        }
    }


    //Method for adding new neuron.
    public void Neuron_add() {

        if ((neurons) < Max_neurons) {

            Max_error();
            double ratio = E2q / Nq;


            double sum = 0;
            int index,index2,i,j;

            for (index = 0; index < Exists.size(); index++) {
                i = Exists.get(index);
                for (index2 = 0; index2 < Exists.size(); index2++) {
                    j = Exists.get(index2);
                    if (i!=j) {
                        sum = sum + Math.abs(Temp_Weights[i][0] - Temp_Weights[j][0]);
                        sum = sum + Math.abs(Temp_Weights[i][1] - Temp_Weights[j][1]);
                        sum = sum + Math.abs(Temp_Weights[i][2] - Temp_Weights[j][2]);
                        sum = sum + RGB_Diff(i, j);
                    }
                        }
                    }





            double no = (double) 2 / (neurons * (neurons - 1));
            double threshhold = sum * m1 * no;
            if (ratio >= threshhold) {
                if (Removed_Neuron.isEmpty()) {
                    int p = Max_nei_q();        //neurons is n. The new position of n is the new neuron created.
                   neurons++;
                                       //Weight[p][1] + Weight[q][1];
                    Temp_Weights[neurons][0] =(Temp_Weights[p][0]+Temp_Weights[q][0])/2;
                    Temp_Weights[neurons][1] =(Temp_Weights[p][1]+Temp_Weights[q][1])/2;
                    Temp_Weights[neurons][2] =(Temp_Weights[p][2]+Temp_Weights[q][2])/2;
                    if (counter==1) {
                        if (Median_Enabled) {
                            Median_Neuron[neurons] = Features_double_add(Median_Neuron[p], Median_Neuron[q]);
                        }
                        if (Sobel_Enabled) {
                            Sobel_Neuron[neurons] = Features_int_add(Sobel_Neuron[p], Sobel_Neuron[q]);
                        }
                        if (LBP_Enabled) {
                            LBP_Neuron[neurons] = Features_int_add(LBP_Neuron[p], LBP_Neuron[q]);
                        }
                        if (Contrast_Enabled) {
                            Contrast_Neuron[neurons] = Features_double_add(Contrast_Neuron[p], Contrast_Neuron[q]);
                        }
                        if (Gradient_Enabled) {
                            Gradient_Neuron[neurons] = Features_int_add(Gradient_Neuron[p], Gradient_Neuron[q]);
                        }
                    }




                    S[p][q] = -1;
                    S[q][p] = -1;

                    Exists.add(neurons);
                    S[neurons][p] = 0;
                    S[p][neurons] = 0;

                    S[neurons][q] = 0;
                    S[q][neurons] = 0;


                    S[neurons][neurons] = -1;
                    N[neurons] = 0;
                    N_Single_epoch[neurons] = 0;
                    Hibernate[neurons] = false;
                    N[p] = 0;
                    N_Single_epoch[p] = 0;

                    N[q] = 0;
                    N_Single_epoch[q] = 0;
                    //System.out.printf("Neurons are now %d \n", neurons); //Debug mEssag
                } else {
                    int k = Collections.min(Removed_Neuron);

                    int p = Max_nei_q();        //neurons is n. The new position of n is the new neuron created.
                    if (p == -1)
                        return;            //q has no neightboors.


                    neurons++;
                    //Weight[p][1] + Weight[q][1];
                    Temp_Weights[k][0] =(Temp_Weights[p][0]+Temp_Weights[q][0])/2;
                    Temp_Weights[k][1] =(Temp_Weights[p][1]+Temp_Weights[q][1])/2;
                    Temp_Weights[k][2] =(Temp_Weights[p][2]+Temp_Weights[q][2])/2;
                    if (counter==1) {
                        if (Median_Enabled) {
                            Median_Neuron[k] = Features_double_add(Median_Neuron[p], Median_Neuron[q]);
                        }
                        if (Sobel_Enabled) {
                            Sobel_Neuron[k] = Features_int_add(Sobel_Neuron[p], Sobel_Neuron[q]);
                        }
                        if (LBP_Enabled) {
                            LBP_Neuron[k] = Features_int_add(LBP_Neuron[p], LBP_Neuron[q]);
                        }
                        if (Contrast_Enabled) {
                            Contrast_Neuron[k] = Features_double_add(Contrast_Neuron[p], Contrast_Neuron[q]);
                        }
                        if (Gradient_Enabled) {
                            Gradient_Neuron[k] = Features_int_add(Gradient_Neuron[p], Gradient_Neuron[q]);
                        }
                    }


                    Hibernate[k] = false;

                    S[p][q] = -1;
                    S[q][p] = -1;

                    S[k][p] = 0;
                    S[p][k] = 0;

                    S[k][q] = 0;
                    S[q][k] = 0;

                    Exists.add(k);
                    Removed_Neuron.removeAll(Arrays.asList(k));



                    S[k][k] = -1;
                    N[k] = 0;
                    N_Single_epoch[k] = 0;
                    N[p] = 0;
                    N_Single_epoch[p] = 0;

                    N[q] = 0;
                    N_Single_epoch[q] = 0;
                   // System.out.printf("Neurons are now %d \n", neurons);


                }
            }


        }
    }


    //Remove unimportant neurons.
    public void Remove_unimportant_neurons() {
        if (neurons > 2) {
            Min_Max_ratio2();
            double k = Min_a - m2 * Max_b;
            if (k <= 0) {
                if (Exists.contains(a_position)) {
                    ArrayList<Integer> a_nei = nei_finder(a_position);

                    int closest = check_minimum_distance(a_position, a_nei);        //k closest to a of nei(a).
                    if (a_nei.isEmpty()) {
                     //   System.out.println("The a neuron has no neightboors. Method aborted");
                        return;        //if the a neuron has no neightboors avoid this method.
                    }

                    for (int z=0; z<Exists.size(); z++){
                        int i = Exists.get(z);
                        if (S[a_position][i] >= 0) {
                            S[closest][i] = 0;
                            S[i][closest] = 0;
                        }


                    }
                    for (int z=0; z<Exists.size(); z++){
                        int i=Exists.get(z);
                        S[a_position][i] = -1;
                        S[i][a_position] = -1;
                    }
                    N[closest] = 0;
                    N[a_position] = 0;


                    e2[a_position] = 0;

                 //   System.out.printf("The neuron removed is %d . \n", a_position); //Debug message

                    Exists.removeAll(Arrays.asList(a_position));
                    Removed_Neuron.add(a_position);



                    neurons--;
               //     System.out.printf("Neurons are now %d . \n", neurons); //Debug message

                }
            }
        }
    }


    //Method that removes inactive neurons.
    public void Neuron_remove() {

        int index,i,k,index2;
        for (index = 0; index < Exists.size(); index++) {
            i = Exists.get(index);
                if (idle_neuron[i] == Beta) {
                    System.out.printf("The idle neuron is %d \n", i); //debug message.
                    int j = check_minimum_distance(i);
                    move_connections(j, i);

                    Removed_Neuron.add(i);
                    Exists.removeAll(Arrays.asList(i));


                    N[i] = 0;
                    E1[i] = 0;
                    E2[i] = 0;
                    e2[i] = 0;
                    N_Single_epoch[i]=0;

                    for (index2 = 0; index2 < Exists.size(); index2++) {
                        k = Exists.get(index2);
                        S[i][k] = -1;
                        S[k][i] = -1;
                    }
                    idle_neuron[i] = 0;
                    neurons--;
             //       System.out.printf("Neurons are now %d \n", neurons);

                }
            }



    }


//Buffer methods for Neuron add-Removal.
//--------------------------------------------------------------------\\
//--------------------------------------------------------------------//

    //for Neuron_add. Buffer Method.
    public void Max_error() {

        double max;

        int index,i;
        max = Integer.MIN_VALUE;

        for (index = 0; index < Exists.size(); index++) {
            i = Exists.get(index);

                if (E1[i] > max) {
                    max = E1[i];
                    Nq = N_Single_epoch[i];
                                  //The Nq represents the number of inputs classified to the Neuron q in a Single epoch.
                    E2q = E2[i];            //Very important for qualification for entry of a new neuron. Close to
                    q = i;
                }                                    //the neuron with the maximum error.
            }



    }

    //For neuron_remove. Buffer Method.
    public void Min_Max_ratio() {            //Neuron_remove ->criteria for unimportant neuron.
//Min max ratio 2 is right.

        Min_a = Integer.MAX_VALUE;
        Max_b = Integer.MIN_VALUE;

        a_position = 1;
        int index,i;

        for (index = 0; index < Exists.size(); index++) {
            i = Exists.get(index);
                if (N[i] >= 1) {    //It means that there was time for Samples to classified to  neuron.
                    if ((E2[i] / N[i]) > Max_b) {
                        Max_b = E2[i] / N[i];

                    }
                    if (E2[i] / N[i] < Min_a) {
                        Min_a = E2[i] / N[i];

                        a_position = i;
                    }
                }
            }

    }


    //Method that calculates minimum distance between neurons.
//Needed when we remove a neuron and we need to
//find the closer neuron to the neuron that it will be removed.
    public int check_minimum_distance(int i, ArrayList<Integer> k) {
       // int feat = 1;
        double	Red_neuron = Temp_Weights[i][0];
        double	Green_neuron = Temp_Weights[i][1];
        double	Blue_neuron = Temp_Weights[i][2];
        double distance;
        double min = Integer.MAX_VALUE;
        int closest_neuron = 0;  //False
        int index;
        double median=0,sobel=0,lbp=0,gradient=0;
        double contrast = 0;
        for (int j = 0; j < k.size(); j++) {
            index = k.get(j);
            double	Red = Temp_Weights[index][0];
            double	Green = Temp_Weights[index][1];
            double	Blue = Temp_Weights[index][2];
            if (counter==1) {
                if (Median_Enabled) {
                    median = (Median_Neuron[i] - Median_Neuron[index]) * (Median_Neuron[i] - Median_Neuron[index]);

                }
                if (Sobel_Enabled) {
                    sobel = (Sobel_Neuron[i] - Sobel_Neuron[index]) * (Sobel_Neuron[i] - Sobel_Neuron[index]);


                }
                if (LBP_Enabled) {
                    lbp = (LBP_Neuron[i] - LBP_Neuron[index]) * (LBP_Neuron[i] - LBP_Neuron[index]);


                }
                if (Contrast_Enabled) {
                    contrast = (Contrast_Neuron[i] - Contrast_Neuron[index]) * (Contrast_Neuron[i] - Contrast_Neuron[index]);

                }
                if (Gradient_Enabled) {
                    gradient = (Gradient_Neuron[i] - Gradient_Neuron[index]) * (Gradient_Neuron[i] - Gradient_Neuron[index]);

                    //gradient = Math.pow(Gradient_Samples[i]-Gradient_Neuron[index],2);
                }
            }
            distance = (int) ((Red - Red_neuron)*(Red - Red_neuron) + (Green - Green_neuron)*(Green - Green_neuron) + (Blue - Blue_neuron)*(Blue - Blue_neuron));
            // distance = (int) (Math.pow(Red - Red_neuron, 2) + Math.pow(Green - Green_neuron, 2) + Math.pow(Blue - Blue_neuron, 2));
            distance = distance + median + sobel + lbp + contrast + gradient;
            //distance = (int) Math.pow(distance, 0.5);


            if (distance < min) {
                min = distance;
                closest_neuron = index;
            }


        }
        if (closest_neuron == 0) System.out.println("Error in minimum distance");

        return closest_neuron;

    }

    public int check_minimum_distance(int i) { //for inactive neurons.
        //int feat = 1;
        double	Red_neuron = Temp_Weights[i][0];
        double	Green_neuron = Temp_Weights[i][1];
        double	Blue_neuron = Temp_Weights[i][2];
        double distance;
        double min = Integer.MAX_VALUE;
        int closest_neuron = 0;  //False

        double median=0,sobel=0,lbp=0,gradient=0;
        double contrast = 0;
        int index,j;

        for (index = 0; index < Exists.size(); index++) {
            j = Exists.get(index);
                if (i != j) {
                    double	Red = Temp_Weights[j][0];
                    double	Green = Temp_Weights[j][1];
                    double	Blue = Temp_Weights[j][2];
                    if (counter==1) {
                        if (Median_Enabled) {
                            median = (Median_Neuron[i] - Median_Neuron[j]) * (Median_Neuron[i] - Median_Neuron[j]);

                        }
                        if (Sobel_Enabled) {
                            sobel = (Sobel_Neuron[i] - Sobel_Neuron[j]) * (Sobel_Neuron[i] - Sobel_Neuron[j]);

                        }
                        if (LBP_Enabled) {
                            lbp = (LBP_Neuron[i] - LBP_Neuron[j]) * (LBP_Neuron[i] - LBP_Neuron[j]);

                        }
                        if (Contrast_Enabled) {
                            contrast = (Contrast_Neuron[i] - Contrast_Neuron[j]) * (Contrast_Neuron[i] - Contrast_Neuron[j]);

                        }
                        if (Gradient_Enabled) {
                            gradient = (Gradient_Neuron[i] - Gradient_Neuron[j]) * (Gradient_Neuron[i] - Gradient_Neuron[j]);

                        }
                    }
                    distance =  ((Red - Red_neuron)*(Red - Red_neuron) + (Green - Green_neuron)*(Green - Green_neuron) + (Blue - Blue_neuron)*(Blue - Blue_neuron));

                    //distance =  (Math.pow(Red - Red_neuron, 2) + Math.pow(Green - Green_neuron, 2) + Math.pow(Blue - Blue_neuron, 2));
                    distance = distance + median + sobel + lbp + contrast + gradient;
                    // distance = (int) Math.pow(distance, 0.5);

                    if (distance < min) {
                        min = distance;
                        closest_neuron = j;
                    }


                }


        }
        return closest_neuron;
    }


    //Method for neuron remove.
    public void move_connections(int j, int i) {        //Move connections of a removed neuron to theclosest one;
        //i is the removed neuron. j is the closest one.

        int index,k;
        for (index = 0; index < Exists.size(); index++) {
            k = Exists.get(index);
            if (S[i][k] >= 0) {
                S[j][k] = S[i][k];
                S[k][j] = S[k][i];

            }


        }
    }

//General Buffer Methods.
//-------------------------------------------------------------------------------\\
//-------------------------------------------------------------------------------//



    //Checks if the connection of two neurons is greater of Alpha then it removes this connection.
    public void check_age() {


        int index,index2,i,j;
        for (index = 0; index < Exists.size(); index++) {
            i = Exists.get(index);
            for (index2 = 0; index2 < Exists.size(); index2++) {
                j = Exists.get(index2);

                    if (S[i][j] >= alpha) {
                        S[i][j] = -1;
                        S[j][i] = -1;
                        // System.out.printf("Connection between %d and %d removed because is over %d  \n", i, j, alpha);
                    }

            }

        }

    }

    public double RGB_Diff_data(int i,int Sample, int j) {
        int Red_Data1 = (i >> 16) & 0xff;
        int Green_Data1 = (i >> 8) & 0xff;
        int Blue_Data1 = (i) & 0xff;

        int	Red_Data2 = (int)Math.round(Temp_Weights[j][0]);
        int	Green_Data2 = (int)Math.round( Temp_Weights[j][1]);
        int	Blue_Data2 = (int) Math.round(Temp_Weights[j][2]);
        double sum = 0;
        int sobel = 0,LBP = 0,gradient = 0;
        double contrast = 0;
        double median = 0;
        if (counter==1) {
            if (Median_Enabled) {
                median = Math.abs(Median_Samples[Sample] - Median_Neuron[j]);
            }
            if (Sobel_Enabled) {
                sobel = Math.abs(Sobel_Samples[Sample] - Sobel_Neuron[j]);
            }
            if (LBP_Enabled) {
                LBP = Math.abs(LBP_Samples[Sample] - LBP_Neuron[j]);
            }
            if (Contrast_Enabled) {
                contrast = (int) Math.abs(Contrast_Samples[Sample] - Contrast_Neuron[j]);
            }
            if (Gradient_Enabled) {
                gradient = Math.abs(Gradient_Samples[Sample] - Gradient_Neuron[j]);
            }
        }



        sum = Math.abs(Red_Data1 - Red_Data2) + Math.abs(Green_Data1 - Green_Data2) + Math.abs(Blue_Data1 - Blue_Data2);
        sum = sum + median + sobel + LBP + contrast + gradient;
        return sum;

    }


    public int RGB_Diff(int i, int j) {

        int median = 0,sobel = 0,LBP = 0,contrast = 0,gradient = 0;
        if (counter==1) {
            if (Median_Enabled) {
                median = (int) Math.abs(Median_Neuron[i] - Median_Neuron[j]);
            }
            if (Sobel_Enabled) {
                sobel = Math.abs(Sobel_Neuron[i] - Sobel_Neuron[j]);
            }
            if (LBP_Enabled) {
                LBP = Math.abs(LBP_Neuron[i] - LBP_Neuron[j]);
            }
            if (Contrast_Enabled) {
                contrast = (int) Math.round(Contrast_Neuron[i] - Contrast_Neuron[j]);
                contrast = Math.abs(contrast);
            }
            if (Gradient_Enabled) {
                gradient = Math.abs(Gradient_Neuron[i] - Gradient_Neuron[j]);
            }
        }
        int sum = 0;
       // sum = Math.abs(Red_Data1 - Red_Data2) + Math.abs(Green_Data1 - Green_Data2 ) + Math.abs(Blue_Data1 - Blue_Data2);
        sum = sum + median + sobel + LBP + contrast + gradient;

        return sum;

    }


    //Normalize dynamically for Gradient and Sobel Feature
    public void Dynamic_Normalization() {
        //double normalize;
        if( Sobel_Enabled) {
            int max = -1;
            for (int i=0; i<Samples; i++) {
                if (Sobel_Samples[i]>max) {
                    max = Sobel_Samples[i];
                }
            }
            normalize = max/255;
            for (int i=0; i<Samples;i++) {
                Sobel_Samples[i] = (int) Math.round(Sobel_Samples[i]/normalize);
            }
            Sobel_Neuron[1] = (int)Math.round(Sobel_Neuron[1]/normalize);
            Sobel_Neuron[2] = (int)Math.round  (Sobel_Neuron[2]/normalize);
        }

        if (Gradient_Enabled) {
            int max2 = -1;
            for (int i=0; i<Samples; i++) {
                if (Gradient_Samples[i]>max2) {
                    max2 = Gradient_Samples[i];
                }
            }
            normalize2 = max2/255;
            for (int i=0; i<Samples; i++) {
                Gradient_Samples[i] = (int)Math.round(Gradient_Samples[i]/normalize2);
            }
            Gradient_Neuron[1] = (int)Math.round(Gradient_Neuron[1]/normalize2);
            Gradient_Neuron[2] = (int)Math.round(Gradient_Neuron[2]/normalize2);
        }

    }


    public int Diff_Img_Weights(int x, int y) {
        int Red_Data1 = (x >> 16) & 0xff;
        int Green_Data1 = (x >> 8) & 0xff;
        int Blue_Data1 = (x) & 0xff;

        int Red_Data2 = (y >> 16) & 0xff;
        int Green_Data2 = (y >> 8) & 0xff;
        int Blue_Data2 = (y) & 0xff;

        int diff;

        diff = (int) (Math.pow(Red_Data1 - Red_Data2, 2) + Math.pow(Green_Data1 - Green_Data2, 2) + Math.pow(Blue_Data1 - Blue_Data2, 2));
        diff = (int) Math.sqrt(diff);
        return diff;
    }

    public void Create_Weight_color() {
        for (int index=0; index<Exists.size();index++) {
            int i = Exists.get(index);
            int tempRed = (int)Math.round(Temp_Weights[i][0]);
            int tempGreen =  (int)Math.round(Temp_Weights[i][1]);
            int tempBlue =  (int)Math.round(Temp_Weights[i][2]);
            if (tempRed>255) {
                tempRed = 255;
            }
            if (tempGreen>255) {
                tempGreen = 255;
            }
            if (tempBlue>255) {
                tempBlue = 255;
            }
            int RGB  = 0xff000000 | (tempRed << 16) | (tempGreen << 8) | tempBlue;
            Weight[i] = RGB;

        }
    }

    public void Write_to_Final_Image() {
        int x, y;
        Create_Weight_color();
        System.out.println("Write Final Image Method");    //debug
        x = bm.getWidth();
        y = bm.getHeight();
        Final_img = bm.copy(Bitmap.Config.ARGB_8888, true);
            //ARGB_8888
        //Final_img = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
        // Final_image = new int [x][y];
        double min; //= find_min();
        int pos;
        int diff,k;
        int Features=0;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                min = Integer.MAX_VALUE; // 1000000000;
                pos = 1;
                if (counter==1) {
                    Mask_3x3_GrayScale_Updated(i, j);
                }

                for (int kk=0; kk<Exists.size(); kk++){
                    k = Exists.get(kk);

                        diff = Diff_Img_Weights(myimage[i][j], Weight[k]);
                        Features =  Diff_Img_Features(i,j,k);
                        diff = diff + Features;
                        if (diff < min) {
                            min = diff;
                            pos = k;
                        }

                }

                Final_img.setPixel(i, j, Weight[pos]);


            }
        }

    }

    public int Diff_Img_Features(int i, int j, int k){

        int feat = 0;
        int temp,diff;

            if (Median_Enabled){
               // Mask_3x3_Updated(i,j);
                temp = Median_IMG();
                diff = (int) Math.round((temp - Median_Neuron[k])*(temp-Median_Neuron[k]));
                diff = (int) Math.sqrt(diff);
                feat  = feat + diff;
            }
        if (counter==1){

          //  Mask_3x3_GrayScale_Updated(i,j);
            if (Gradient_Enabled){
                temp = (int)Math.round(Gradient()/normalize2);
                diff = (int) Math.round(temp - Gradient_Neuron[k])*(temp-Gradient_Neuron[k]);
                diff = (int) Math.sqrt(diff);
                feat = feat + diff;
            }
            if (Sobel_Enabled){
                temp = (int)Math.round(Sobel()/normalize);
                diff = (temp - Sobel_Neuron[k])*(temp - Sobel_Neuron[k]);
                diff = (int)Math.sqrt(diff);
                feat = feat + diff;
            }
            if (Contrast_Enabled){
                temp = (int)Math.round (Contrast());
                diff = (int)Math.round((temp - Contrast_Neuron[k])*(temp-Contrast_Neuron[k]));
                diff = (int) Math.sqrt(diff);
                feat = feat + diff;
            }
            if (LBP_Enabled){
                int gray = find_Gray(myimage[i][j]);
                temp = LBP(gray);
                diff = (temp - LBP_Neuron[k])* (temp - LBP_Neuron[k]);
                diff = (int) Math.sqrt(diff);
                feat = feat + diff;
            }

        }


        return feat;

    }

    //Checks if one at least feature is enabled so creates the gray.
    public int check_Features_Data(int Red_Data, int Green_Data, int Blue_Data) {
        boolean ok = false;
        boolean Gray_ok = false;
        int Gray = 0;
        if (Sobel_Enabled) {
            Gray_ok = true;
        }
        if (Gradient_Enabled) {

            Gray_ok = true;
        }
        if (LBP_Enabled) {
            Gray_ok = true;
        }
        if (Median_Enabled) {
            ok = true;
        }
        if (Contrast_Enabled) {
            Gray_ok = true;
        }
        if (Gray_ok) {
            Gray = Single_Value_grayScale(Red_Data, Green_Data, Blue_Data);
        }

        return Gray;

    }

    public void create_features_values(int [] Sampling,int samples){
        int x,y;
        if (Median_Enabled) {
            for (int i=0;i<samples;i++) {
                x = Samp_X_pos[i];
                y = Samp_Y_pos[i];
                //Mask_3x3(Sampling[i]);

                Mask_3x3_Updated(x,y);



                Median_Samples[i] = Median();
            }
        }
        int gray;
        if (counter==1) {
            for (int i=0;i<samples;i++) {
                x = Samp_X_pos[i];
                y = Samp_Y_pos[i];
                Mask_3x3_GrayScale_Updated(x,y);


               // create_Sample_Masks(Sampling[i],gray);

                if (LBP_Enabled) {
                    gray = find_Gray(Sampling[i]);
                    LBP_Samples[i] = LBP(gray);
                }
                if (Sobel_Enabled) {

                    Sobel_Samples[i] = Sobel();
                }
                if (Contrast_Enabled) {

                    Contrast_Samples[i] = Contrast();
                }
                if (Gradient_Enabled) {

                    Gradient_Samples[i] = Gradient();
                }

            }
        Dynamic_Normalization();
        }

    }

    public int Features_int_add(int i,int j) {

        int features = Math.round((i+j)/2);

        return features;
    }


    public double Features_double_add(double i,double j) {
        double features = (i+j)/2;

        return features;

    }

    public void create_Sample_Masks(int data, int gray,int x,int y) {

        if (counter==0) { //make sure that you check it once.
            if (Sobel_Enabled) {

                counter =1;
            }
            if (Gradient_Enabled) {
                counter = 1;

            }
            if(LBP_Enabled) {
                counter = 1;

            }

            if (Contrast_Enabled) {
                counter = 1;

            }
        }

        if (counter==1) {
            Mask_3x3_GrayScale_Updated(x,y);
            //Mask_3x3_GrayScale(gray);
        }
    }

    public void Min_Max_ratio2() {			//Neuron_remove ->criteria for unimportant neuron.


        Min_a = Integer.MAX_VALUE;
        Max_b = Integer.MIN_VALUE;

        a_position = 1;


        for (int i=0; i<Exists.size();i++) {
            int index = Exists.get(i);//Calculate a position first. If its included
            if (N[index]>=1) { 	//It means that there was time for Samples to classified to  neuron.
                if ((E2[index]/N_Single_epoch[index])>Max_b) {

                    Max_b = E2[index]/N_Single_epoch[index];

                }
                if(E2[index]/N_Single_epoch[index]<Min_a) {

                    Min_a = E2[index]/N_Single_epoch[index];
                    a_position = index;
                }

            }
        }
    }






    //if we have the same neuron , there is no connection.
    public void Set_S() {
        int i;
        for (i = 1; i <=Max_neurons; i++) {
            S[i][i] = -1;
        }
    }


//Nei finder Methods.
//------------------------------------------------------------------\\
//------------------------------------------------------------------//


    //Method that calculates the neightboorhood of a  single neuron.
    //But basically we need it for the Winner neuron.
    public ArrayList<Integer> nei_finder(int j) {

        if (epochs == 1) {
            //S[Winner_neuron][Second_neuron_pos] = 1;
            //S[Second_neuron_pos][Winner_neuron] = 1;
            S[Winner_neuron][Winner_neuron] = -1;
            S[Second_neuron_pos][Second_neuron_pos] = -1;
        }
        int i;
        ArrayList<Integer> neight = new ArrayList<Integer>();
        //for (i = 1; i <= neurons; i++) {
        for (int ii=0; ii<Exists.size();ii++){
            i = Exists.get(ii);
           // if (Exists.contains(i)) {
                if (S[i][j] >= 0 && i != j) {
                    neight.add(i);
                }
          //  }
        }

        return neight;

    }


    //Method used for finding the Max E1 in the neightboorhood of q.
    public int Max_nei_q() {
        ArrayList<Integer> nei_q = nei_finder(q);
        //System.out.printf("The q is %d \n", q);    //Debug message
        int Size = nei_q.size();
        int nei;
        double max = -1000000;
        int pos = -1;
        for (int i = 1; i <= Size; i++) {
            nei = nei_q.get(i - 1);
            if (E1[nei] > max) {
                max = E1[nei];
                pos = nei;   //i;
            }

        }
        if (pos == -1)
            System.out.println("Error in finding the neightboor neuron of  Max error of q \n");

        return pos;

    }


    //Exit Method. Writes to image the final results.
    public void break_program() {
        //Checks if the parameters have exceeded the user input.Nidle,Max neurons etc
        Write_to_Final_Image();


    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public Uri getImgUri() {
        return img_uri;
    }


    public static Bitmap getBitmap() {
        return Final_img;
    }

    public  int getNumberColors(){
        return neurons;
    }



}

