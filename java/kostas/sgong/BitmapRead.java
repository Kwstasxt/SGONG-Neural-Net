package kostas.sgong;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Arrays;
import java.util.Random;

public class BitmapRead extends Main_Menu {


    public int[][] myimage;
   public Bitmap bm;
   public static Bitmap Final_img;
    public static int[][] grayScale;
   public int [][] Mask;
    public int[][][] Mask_data;
    //public int[][][] Mask_data_gray;
    public int [] Samp_X_pos;
    public int [] Samp_Y_pos;






    public  BitmapRead(Bitmap image){

        try {
            bm = image;
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

       // return bm;
    }

    public BitmapRead() {
        try{
            bm = Main_Menu.bitmap;
        }   catch (Exception e) {
              Log.e("Exception", e.getMessage());
        }
    }



    public int getWidth() {
        int x = bm.getWidth();
        return x;
    }

    public int getHeight() {
        int y = bm.getHeight();
        return y;
    }




    public int[][] readImagevalues() {
        int x, y, i, j;


        x = bm.getWidth();
        y = bm.getHeight();
         myimage = new int[x][y];


        for (i = 0; i < x; ++i) {
            for (j = 0; j < y; ++j) {

                myimage[i][j] = bm.getPixel(i, j);

            }
        }
        return myimage;

    }

    public int[][] readRedvalues() {
        int x,y,i,j;
        int[][] Red;
        x = bm.getWidth();
        y = bm.getHeight();
        Red = new int[x][y];
        for (i=0; i<x; ++i) {
            for (j=0; j<y; ++j) {
                Red[i][j] = (myimage[i][j] >> 16) & 0xff  ;   //0xff0000
            }
        }
        return Red;
    }

    public int[][] readBluevalues() {
        int x,y,i,j;
        x = bm.getWidth();
        y = bm.getHeight();
        int [][] Blue = new int[x][y];
        for (i=0; i<x; ++i) {
            for (j=0; j<y; ++j) {
                Blue[i][j] = (myimage[i][j]) & 0xff; ;
            }
        }
        return Blue;
    }

    public int[][] readGreenvalues() {
        int x,y,i,j;
        x = bm.getWidth();
        y = bm.getHeight();
        int [][] Green = new int[x][y];
        for (i=0; i<x; ++i) {
            for (j=0; j<y; ++j) {
                Green[i][j] = (myimage[i][j]>> 8) & 0xff ; //0xff00
            }
        }
        return Green;
    }







    public int[] Sampling(int data[][],int Samples) {   //
        Random rand = new Random();
        int x;
        int y;
        int i;
        int rw = bm.getWidth();
        int rh = bm.getHeight();
        Samp_X_pos = new int[Samples];
        Samp_Y_pos = new int[Samples];
        //gray = new int [Samples+1];
      int []  SAMrray = new int[Samples+1];
        for (i=0; i<Samples; i++) {
            x = rand.nextInt(rw);
            y = rand.nextInt(rh);
            SAMrray[i] = data[x][y];
            Samp_X_pos[i] = x;
            Samp_Y_pos[i] = y;
        }

        return SAMrray;

    }

    public  int[][] grayscale_image(int[][] r,int[][] g,int[][] b){

        int x = bm.getWidth();
        int y = bm.getHeight();
        int i,j;
        grayScale = new int[x][y];

        //System.out.println("This is ok");
        //System.out.printf("the value of red is %d ",j);

        for ( i=0; i<x; i++) {
            for ( j=0; j<y; j++) {
                //System.out.printf("the value of red is %d ",r[i][j]);
                grayScale[i][j] = (int) Math.floor( ((r[i][j] +g[i][j]+b[i][j])/3));

            }
        }
        return grayScale;

    }
//Find Mask 3x3 for a pixel with x,y position.
    public void Mask_3x3_Updated(int X_pos,int Y_pos){
        int x,y,i,j;
        x = bm.getWidth();
        y = bm.getHeight();
        Mask = new int[3][3];
        if (X_pos>0 && X_pos<x-1) {
            if (Y_pos > 0 && Y_pos < y-1) {
                Mask[0][0] = (myimage[X_pos - 1][Y_pos - 1]);
                Mask[0][1] = (myimage[X_pos - 1][Y_pos]);
                Mask[0][2] = (myimage[X_pos - 1][Y_pos + 1]);
                Mask[1][0] = (myimage[X_pos][Y_pos - 1]);
                Mask[1][1] = (myimage[X_pos][Y_pos]);
                Mask[1][2] = (myimage[X_pos][Y_pos + 1]);
                Mask[2][0] = (myimage[X_pos + 1][Y_pos - 1]);
                Mask[2][1] = (myimage[X_pos + 1][Y_pos]);
                Mask[2][2] = (myimage[X_pos + 1][Y_pos + 1]);
                return;

            }
        }else{
            if (X_pos==0) {
                if (Y_pos==0) {
                    Mask[0][0] = 0;
                    Mask[0][1] = 0;
                    Mask[0][2] = 0;
                    Mask[1][0] = 0;
                    Mask[1][1] = (myimage[X_pos][Y_pos]);
                    Mask[1][2] = (myimage[X_pos][Y_pos+1]);
                    Mask[2][0] = 0;
                    Mask[2][1] = (myimage[X_pos+1][Y_pos]);
                    Mask[2][2] = (myimage[X_pos+1][Y_pos+1]);
                    return;


                }
                else	if (Y_pos<y-1 ) {
                    Mask[0][0] = 0;
                    Mask[0][1] = 0;
                    Mask[0][2] = 0;
                    Mask[1][0] = (myimage[X_pos][Y_pos-1]);
                    Mask[1][1] = (myimage[X_pos][Y_pos]);
                    Mask[1][2] = (myimage[X_pos][Y_pos+1]);
                    Mask[2][0] = (myimage[X_pos+1][Y_pos-1]);
                    Mask[2][1] = (myimage[X_pos+1][Y_pos]);
                    Mask[2][2] = (myimage[X_pos+1][Y_pos+1]);
                    return;


                }
                else	if (Y_pos==y-1) {
                    Mask[0][0] = 0;
                    Mask[0][1] = 0;
                    Mask[0][2] = 0;
                    Mask[1][0] = (myimage[X_pos][Y_pos-1]);
                    Mask[1][1] = (myimage[X_pos][Y_pos]);
                    Mask[1][2] = 0;
                    Mask[2][0] = (myimage[X_pos+1][Y_pos-1]);
                    Mask[2][1] = (myimage[X_pos+1][Y_pos]);
                    Mask[2][2] = 0;
                    return;


                }
            }
            else	if (X_pos<=x-1) {
                if (Y_pos==0) {
                    Mask[0][0] =  0;
                    Mask[0][1] = (myimage[X_pos-1][Y_pos]);
                    Mask[0][2] = (myimage[X_pos-1][Y_pos+1]);
                    Mask[1][0] = 0;
                    Mask[1][1] = (myimage[X_pos][Y_pos]);
                    Mask[1][2] = (myimage[X_pos][Y_pos+1]);
                    Mask[2][0] = 0;
                    Mask[2][1] = 0;
                    Mask[2][2] = 0;
                    return;


                }
                else	if (Y_pos<y-1 ) {
                    Mask[0][0] = (myimage[X_pos-1][Y_pos-1]);
                    Mask[0][1] = (myimage[X_pos-1][Y_pos]);
                    Mask[0][2] = (myimage[X_pos-1][Y_pos+1]);
                    Mask[1][0] = (myimage[X_pos][Y_pos-1]);
                    Mask[1][1] = (myimage[X_pos][Y_pos]);
                    Mask[1][2] = (myimage[X_pos][Y_pos+1]);
                    Mask[2][0] = 0;
                    Mask[2][1] = 0;
                    Mask[2][2] = 0;
                    return;


                }
                else	if (Y_pos==y-1) {
                    Mask[0][0] = (myimage[X_pos-1][Y_pos-1]);
                    Mask[0][1] = (myimage[X_pos-1][Y_pos]);
                    Mask[0][2] = 0;
                    Mask[1][0] = (myimage[X_pos][Y_pos-1]);
                    Mask[1][1] = (myimage[X_pos][Y_pos]);
                    Mask[1][2] = 0;
                    Mask[2][0] = 0;
                    Mask[2][1] = 0;
                    Mask[2][2] = 0;
                    return;


                }
            }


            return;




        }


    }










    public void Mask_3x3(int data) {
        int x,y,i,j;
        x = bm.getWidth();
        y = bm.getHeight();
        boolean inBounds_x;
        boolean inBounds_y;
        Mask = new int[3][3];
        for (i=0; i<x; ++i) {
            for (j=0; j<y; ++j) {
                if ((myimage[i][j])==data) {
                    inBounds_y = (i-1>=0) && (i+1<x);
                    inBounds_x = (j-1>=0) && (j+1<y);
                    if (inBounds_x == true && inBounds_y == true) {
                        Mask[0][0] = (myimage[i-1][j-1]);
                        Mask[0][1] = (myimage[i-1][j]);
                        Mask[0][2] = (myimage[i-1][j+1]);
                        Mask[1][0] = (myimage[i][j-1]);
                        Mask[1][1] = (myimage[i][j]);
                        Mask[1][2] = (myimage[i][j+1]);
                        Mask[2][0] = (myimage[i+1][j-1]);
                        Mask[2][1] = (myimage[i+1][j]);
                        Mask[2][2] = (myimage[i+1][j+1]);
                        return;

                    }else {
                        //	System.out.println("Sample mask out of Bounds");//debug
                        //Use this mask instead.

                        //We apply zero values to the pixels of the mask that are out of bounds.
                        if (i==0) {
                            if (j==0) {
                                Mask[0][0] = 0;
                                Mask[0][1] = 0;
                                Mask[0][2] = 0;
                                Mask[1][0] = 0;
                                Mask[1][1] = (myimage[i][j]);
                                Mask[1][2] = (myimage[i][j+1]);
                                Mask[2][0] = 0;
                                Mask[2][1] = (myimage[i+1][j]);
                                Mask[2][2] = (myimage[i+1][j+1]);
                                return;


                            }
                            else	if (j<x ) {
                                Mask[0][0] = 0;
                                Mask[0][1] = 0;
                                Mask[0][2] = 0;
                                Mask[1][0] = (myimage[i][j-1]);
                                Mask[1][1] = (myimage[i][j]);
                                Mask[1][2] = (myimage[i][j+1]);
                                Mask[2][0] = (myimage[i+1][j-1]);
                                Mask[2][1] = (myimage[i+1][j]);
                                Mask[2][2] = (myimage[i+1][j+1]);
                                return;


                            }
                            else	if (j==x) {
                                Mask[0][0] = 0;
                                Mask[0][1] = 0;
                                Mask[0][2] = 0;
                                Mask[1][0] = (myimage[i][j-1]);
                                Mask[1][1] = (myimage[i][j]);
                                Mask[1][2] = 0;
                                Mask[2][0] = (myimage[i+1][j-1]);
                                Mask[2][1] = (myimage[i+1][j]);
                                Mask[2][2] = 0;
                                return;


                            }
                        }
                        else	if (i<=y) {
                            if (j==0) {
                                Mask[0][0] =  0;
                                Mask[0][1] = (myimage[i-1][j]);
                                Mask[0][2] = (myimage[i-1][j+1]);
                                Mask[1][0] = 0;
                                Mask[1][1] = (myimage[i][j]);
                                Mask[1][2] = (myimage[i][j+1]);
                                Mask[2][0] = 0;
                                Mask[2][1] = 0;
                                Mask[2][2] = 0;
                                return;


                            }
                            else	if (j<x ) {
                                Mask[0][0] = (myimage[i-1][j-1]);
                                Mask[0][1] = (myimage[i-1][j]);
                                Mask[0][2] = (myimage[i-1][j+1]);
                                Mask[1][0] = (myimage[i][j-1]);
                                Mask[1][1] = (myimage[i][j]);
                                Mask[1][2] = (myimage[i][j+1]);
                                Mask[2][0] = 0;
                                Mask[2][1] = 0;
                                Mask[2][2] = 0;
                                return;


                            }
                            else	if (j==x) {
                                Mask[0][0] = (myimage[i-1][j-1]);
                                Mask[0][1] = (myimage[i-1][j]);
                                Mask[0][2] = 0;
                                Mask[1][0] = (myimage[i][j-1]);
                                Mask[1][1] = (myimage[i][j]);
                                Mask[1][2] = 0;
                                Mask[2][0] = 0;
                                Mask[2][1] = 0;
                                Mask[2][2] = 0;
                                return;


                            }
                        }


                        return;




                    }


                }

            }
        }

    }



    public void Mask_3x3_GrayScale_Updated(int i,int j) {
        int x,y;
        x = bm.getWidth();
        y = bm.getHeight();
        boolean inBounds_x;
        boolean inBounds_y;
        Mask = new int[3][3];

                    //System.out.printf("The data found at i = %d and j = %d \n",i,j); //debug message.
                    inBounds_y = (i-1>=0) && (i+1<x);
                    inBounds_x = (j-1>=0) && (j+1<y);
                    if (inBounds_x == true && inBounds_y == true) {
                        Mask[0][0] = grayScale[i-1][j-1];
                        Mask[0][1] = grayScale[i-1][j];
                        Mask[0][2] = grayScale[i-1][j+1];
                        Mask[1][0] = grayScale[i][j-1];
                        Mask[1][1] = grayScale[i][j];
                        Mask[1][2] = grayScale[i][j+1];
                        Mask[2][0] = grayScale[i+1][j-1];
                        Mask[2][1] = grayScale[i+1][j];
                        Mask[2][2] = grayScale[i+1][j+1];
                        return;
                    }else {
                        //	System.out.println("Sample mask out of Bounds");//debug
                        //Use this mask instead.

                        //We apply zero values to the pixels of the mask that are out of bounds.
                        if (i==0) {
                            if (j==0) {
                                Mask[0][0] = 0;
                                Mask[0][1] = 0;
                                Mask[0][2] = 0;
                                Mask[1][0] = 0;
                                Mask[1][1] = grayScale[i][j];
                                Mask[1][2] = grayScale[i][j+1];
                                Mask[2][0] = 0;
                                Mask[2][1] = grayScale[i+1][j];
                                Mask[2][2] = grayScale[i+1][j+1];
                                return;

                            }
                            else	if (j<y-1 ) {
                                Mask[0][0] = 0;
                                Mask[0][1] = 0;
                                Mask[0][2] = 0;
                                Mask[1][0] = grayScale[i][j-1];
                                Mask[1][1] = grayScale[i][j];
                                Mask[1][2] = grayScale[i][j+1];
                                Mask[2][0] = grayScale[i+1][j-1];
                                Mask[2][1] = grayScale[i+1][j];
                                Mask[2][2] = grayScale[i+1][j+1];
                                return;

                            }
                            else if (j==y-1) {
                                Mask[0][0] = 0;
                                Mask[0][1] = 0;
                                Mask[0][2] = 0;
                                Mask[1][0] = grayScale[i][j-1];
                                Mask[1][1] = grayScale[i][j];
                                Mask[1][2] = 0;
                                Mask[2][0] = grayScale[i+1][j-1];
                                Mask[2][1] = grayScale[i+1][j];
                                Mask[2][2] = 0;
                                return;
                            }
                        }
                        else if (i<=x-1) {
                            if (j==0) {
                                Mask[0][0] =  0;
                                Mask[0][1] = grayScale[i-1][j];
                                Mask[0][2] = grayScale[i-1][j+1];
                                Mask[1][0] = 0;
                                Mask[1][1] = grayScale[i][j];
                                Mask[1][2] = grayScale[i][j+1];
                                Mask[2][0] = 0;
                                Mask[2][1] = 0;
                                Mask[2][2] = 0;
                                return;

                            }
                            else	if (j<y-1 ) {
                                Mask[0][0] = grayScale[i-1][j-1];
                                Mask[0][1] = grayScale[i-1][j];
                                Mask[0][2] = grayScale[i-1][j+1];
                                Mask[1][0] = grayScale[i][j-1];
                                Mask[1][1] = grayScale[i][j];
                                Mask[1][2] = grayScale[i][j+1];
                                Mask[2][0] = 0;
                                Mask[2][1] = 0;
                                Mask[2][2] = 0;
                                return;

                            }
                            else	if (j==y-1) {
                                Mask[0][0] = grayScale[i-1][j-1];
                                Mask[0][1] = grayScale[i-1][j];
                                Mask[0][2] = 0;
                                Mask[1][0] = grayScale[i][j-1];
                                Mask[1][1] = grayScale[i][j];
                                Mask[1][2] = 0;
                                Mask[2][0] = 0;
                                Mask[2][1] = 0;
                                Mask[2][2] = 0;
                                return;

                            }
                        }



                    }


                }












    public void Mask_3x3_GrayScale(int data) {
        int x,y,i,j;
        x = bm.getWidth();
        y = bm.getHeight();
        boolean inBounds_x;
        boolean inBounds_y;
        Mask = new int[3][3];
        for (i=0; i<x; ++i) {
            for (j=0; j<y; ++j) {
                if (grayScale[i][j]==data) {
                    //System.out.printf("The data found at i = %d and j = %d \n",i,j); //debug message.
                    inBounds_y = (i-1>=0) && (i+1<x);
                    inBounds_x = (j-1>=0) && (j+1<y);
                    if (inBounds_x == true && inBounds_y == true) {
                        Mask[0][0] = grayScale[i-1][j-1];
                        Mask[0][1] = grayScale[i-1][j];
                        Mask[0][2] = grayScale[i-1][j+1];
                        Mask[1][0] = grayScale[i][j-1];
                        Mask[1][1] = grayScale[i][j];
                        Mask[1][2] = grayScale[i][j+1];
                        Mask[2][0] = grayScale[i+1][j-1];
                        Mask[2][1] = grayScale[i+1][j];
                        Mask[2][2] = grayScale[i+1][j+1];
                        return;
                    }else {
                        //	System.out.println("Sample mask out of Bounds");//debug
                        //Use this mask instead.

                        //We apply zero values to the pixels of the mask that are out of bounds.
                        if (i==0) {
                            if (j==0) {
                                Mask[0][0] = 0;
                                Mask[0][1] = 0;
                                Mask[0][2] = 0;
                                Mask[1][0] = 0;
                                Mask[1][1] = grayScale[i][j];
                                Mask[1][2] = grayScale[i][j+1];
                                Mask[2][0] = 0;
                                Mask[2][1] = grayScale[i+1][j];
                                Mask[2][2] = grayScale[i+1][j+1];
                                return;

                            }
                            else	if (j<x ) {
                                Mask[0][0] = 0;
                                Mask[0][1] = 0;
                                Mask[0][2] = 0;
                                Mask[1][0] = grayScale[i][j-1];
                                Mask[1][1] = grayScale[i][j];
                                Mask[1][2] = grayScale[i][j+1];
                                Mask[2][0] = grayScale[i+1][j-1];
                                Mask[2][1] = grayScale[i+1][j];
                                Mask[2][2] = grayScale[i+1][j+1];
                                return;

                            }
                            else if (j==x) {
                                Mask[0][0] = 0;
                                Mask[0][1] = 0;
                                Mask[0][2] = 0;
                                Mask[1][0] = grayScale[i][j-1];
                                Mask[1][1] = grayScale[i][j];
                                Mask[1][2] = 0;
                                Mask[2][0] = grayScale[i+1][j-1];
                                Mask[2][1] = grayScale[i+1][j];
                                Mask[2][2] = 0;
                                return;
                            }
                        }
                        else if (i<=y) {
                            if (j==0) {
                                Mask[0][0] =  0;
                                Mask[0][1] = grayScale[i-1][j];
                                Mask[0][2] = grayScale[i-1][j+1];
                                Mask[1][0] = 0;
                                Mask[1][1] = grayScale[i][j];
                                Mask[1][2] = grayScale[i][j+1];
                                Mask[2][0] = 0;
                                Mask[2][1] = 0;
                                Mask[2][2] = 0;
                                return;

                            }
                            else	if (j<x ) {
                                Mask[0][0] = grayScale[i-1][j-1];
                                Mask[0][1] = grayScale[i-1][j];
                                Mask[0][2] = grayScale[i-1][j+1];
                                Mask[1][0] = grayScale[i][j-1];
                                Mask[1][1] = grayScale[i][j];
                                Mask[1][2] = grayScale[i][j+1];
                                Mask[2][0] = 0;
                                Mask[2][1] = 0;
                                Mask[2][2] = 0;
                                return;

                            }
                            else	if (j==x) {
                                Mask[0][0] = grayScale[i-1][j-1];
                                Mask[0][1] = grayScale[i-1][j];
                                Mask[0][2] = 0;
                                Mask[1][0] = grayScale[i][j-1];
                                Mask[1][1] = grayScale[i][j];
                                Mask[1][2] = 0;
                                Mask[2][0] = 0;
                                Mask[2][1] = 0;
                                Mask[2][2] = 0;
                                return;

                            }
                        }



                    }


                }
            }
        }

    }



    public int Median() {
        //Mask_3x3(data);
        //int [][] mask  = Mask;

        int median;

        int [] vector = inputSpace(Mask);
        Arrays.sort(vector);
        //0 1 2 3 4 5 6 7 8 for example -> Median is number 4.
        median = vector[4];
        int	Red_Data = (median>> 16) & 0xff  ;
        int	Green_Data = (median  >> 8) & 0xff  ;
        int	Blue_Data = (median ) & 0xff  ;
        median = Math.round((Red_Data + Green_Data + Blue_Data)/3);

        return median;
    }

    public int Median_IMG() {
        //Mask_3x3(data);
        //int [][] mask  = Mask;

        int median;

        int [] vector = inputSpace(Mask);
        Arrays.sort(vector);
        //0 1 2 3 4 5 6 7 8 for example -> Median is number 4.
        median = vector[4];
       // int	Red_Data = (median>> 16) & 0xff  ;
        //int	Green_Data = (median  >> 8) & 0xff  ;
        //int	Blue_Data = (median ) & 0xff  ;
       // median = Math.round((Red_Data + Green_Data + Blue_Data)/3);

        return median;
    }







    public int[] inputSpace(int data[][]) {
        int [] vector ;
        int x,y,i,j;
        int pointer = 0;
        x = 3;    // img.getWidth();
        y = 3;    // img.getHeight();
        vector = new int[9];
        for (i=0; i<x; i++) {
            for (j=0; j<y; j++) {
                vector[pointer] = data[i][j];
                pointer++;
            }
        }

        return vector;

    }

    public int[] inputSpace_Neurons(int [][] Mask1,int data) {
        int [] vector ;
        int x,y,i,j;
        int pointer = 0;
        x = 3;    // img.getWidth();
        y = 3;    // img.getHeight();
        vector = new int[9];
        for (i=0; i<x; i++) {
            for (j=0; j<y; j++) {
                vector[pointer] = Mask1[i][j];
                pointer++;
            }
        }

        return vector;

    }







    public int find_Gray(int data) {

        int	Red_Data = (data>> 16) & 0xff  ;
        int	Green_Data = (data  >> 8) & 0xff  ;
        int	Blue_Data = (data ) & 0xff  ;
        int gray = (int) Math.floor ((Red_Data + Green_Data + Blue_Data)/3);

        return gray;
    }




    public int LBP(int data) {
        //Mask_3x3_GrayScale(data);
        //int [][] mask = Mask;
        int Lbp,sum,power;
        sum = 0;
        power = 0;

       // power++;
        if (data<Mask[1][0]) {

            sum =(int) (sum + Math.pow(2,power));
        }
        power++;
        if (data<Mask[2][0]) {

            sum =(int) (sum + Math.pow(2,power));
        }
        power++;
        if (data<Mask[2][1]) {

            sum =(int) (sum + Math.pow(2,power));
        }
        power++;
        if (data<Mask[2][2]) {

            sum =(int) (sum + Math.pow(2,power));
        }
        power++;
        if (data<Mask[1][2]) {

            sum =(int) (sum + Math.pow(2,power));
        }
        power++;
        if (data<Mask[0][2]) {

            sum =(int) (sum + Math.pow(2,power));
        }
        power++;
        if (data<Mask[0][1]) {

            sum =(int) (sum + Math.pow(2,power));
        }
        power++;
        if (data<Mask[0][0]) {

            sum =(int) (sum + Math.pow(2,power));
        }



        Lbp = sum;
        return Lbp;
    }










    public int Gradient() {




        int Gradient ;
        int GR= 0, Gc = 0;

        GR = Mask[1][2] + Mask[0][2] + Mask[2][2] - Mask[0][0] - Mask[1][0] - Mask[2][0];
        Gc = Mask[1][2] + 2*Mask[1][2] + Mask[2][2] - Mask[0][0] - 2*Mask[1][0] - Mask[2][0];



        Gradient = (int) (Math.pow(GR, 2) + Math.pow(Gc, 2));
        Gradient = (int) Math.sqrt(Gradient);
       // Gradient = (int) Math.round(Gradient/5); //Max = 255 , min = 0
        return Gradient;
    }


    public double Contrast() {
        double contrast;
        //Mask_3x3_GrayScale(data);
        int max,min;
//	int [][] mask = Mask;
        max = Mask[0][0];
        min = Mask[0][0];
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (Mask[i][j] > max) {
                    max = Mask[i][j];
                }
                if (Mask[i][j]<min) {
                    min = Mask[i][j];
                }
            }
        }

        contrast =(double) (max - min)/(max + min);
        contrast = contrast *255; //Make it take the same range of values as RGB.

        return contrast;

    }
    public int Single_Value_grayScale(int r,int g,int b) {
        int grayScale;

        grayScale = (int) Math.floor((r+g+b)/3);



        return grayScale;


    }

    public int Sobel() {
        //Mask_3x3_GrayScale(data);
        //int [] vector = inputSpace(Mask);
        int [][] Sobel_GR;
        int [][] Sobel_Gc;
        int Gc,GR,G;
        Sobel_GR = new int [][] {{-1,0,1},{-2,0,2},{-1,0,1}};
        Sobel_Gc = new int [][] {{1,2,1},{0,0,0},{-1,-2,-1}};
        //Convolution mask and Sobel GR,Gc
        Gc = applyConvolution(Mask, 0, 0, Sobel_Gc, 3, 3);
        GR= applyConvolution(Mask, 0, 0, Sobel_GR, 3, 3);
        G = (int) (Math.pow(Gc, 2) + Math.pow(GR, 2));
        G = (int)  Math.sqrt(G);
       // G = (int)Math.round(G/4.2392);
        return G;


    }
    //--------------------------------------------------------------------------------------------------------
//Convolution Methods.
    public static int[][] convolution(int [][] input, int width, int height,int[][] kernel,int kernelWidth,
                                      int kernelHeight)
    {
        int smallWidth = width - kernelWidth + 1;
        int smallHeight = height - kernelHeight + 1;
        int[][] output = new int[smallWidth][smallHeight];
        for (int i = 0; i < smallWidth; ++i) {
            for (int j = 0; j < smallHeight; ++j) {
                output[i][j] = 0;
            }
        }
        for (int i = 0; i < smallWidth; ++i) {
            for (int j = 0; j < smallHeight; ++j) {
                output[i][j] = singlePixelConvolution(input, i, j, kernel,
                        kernelWidth, kernelHeight);
            }
        }
        return output;
    }


    public static int applyConvolution(int [][] input, int x, int y, int [][] k, int kernelWidth, int kernelHeight){
        int output = 0;
        for(int i=0;i<kernelWidth;++i){
            for(int j=0;j<kernelHeight;++j){
                output = output + (int) Math.round(input[x+i][y+j] * k[i][j]);
            }
        }
        return output;
    }


    public static int singlePixelConvolution(int [][] input,
                                             int x, int y,
                                             int [][] k,
                                             int kernelWidth,
                                             int kernelHeight){
        int output = 0;
        for(int i=0;i<kernelWidth;++i){
            for(int j=0;j<kernelHeight;++j){
                output = output + (input[x+i][y+j] * k[i][j]);
            }
        }
        return output;
    }
































}
