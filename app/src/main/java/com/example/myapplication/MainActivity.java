package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.myapplication.Data.CropContract;
import com.example.myapplication.Data.Crop_db_helper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mauth;
    private static final String TAG = "MainActivity";
    public static final int ERROR_DIALOG_REQUEST=9001;
    ViewFlipper viewFlipper;
    private boolean mCallPermissionGranted=false;
    public static final int CALL_PERMISSION_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mauth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);


        CardView bt1 = (CardView) findViewById(R.id.cropimg);
        CardView bt2 = (CardView) findViewById(R.id.weatherimg);
        CardView bt3 = (CardView) findViewById(R.id.locationimg);
        CardView bt4 = (CardView) findViewById(R.id.phoneimg);
        CardView bt5 = (CardView) findViewById(R.id.calenderimg);
        CardView bt6 = (CardView) findViewById(R.id.cameraimg);
        viewFlipper=(ViewFlipper)findViewById(R.id.flip);

        int images[]={R.drawable.farmer_1,R.drawable.farmer_2,R.drawable.farmer_3};

        for(int image:images)
        {
            flipperImage(image);
        }




        Crop_db_helper dbHelper = new Crop_db_helper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int a = db.delete(CropContract.CropEntry.TABLE_NAME, null, null);


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Crop.class));
                insertCrop();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        MainActivity.this, Weather.class));
            }
        });

        if(isServicesOK()) {
            bt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(
                            MainActivity.this, LocateActivity.class));
                }
            });
        }
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCallPermission();
            }
        });

        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Event.class));
            }
        });

    }

    public boolean isServicesOK()
    {
        Log.d(TAG,"isServicesOK: checking google services version");

        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available== ConnectionResult.SUCCESS){
            //everything is fine
            Log.d(TAG,"isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            //error but can be resolved
            Log.d(TAG,"isServicesOK: an error occured but we can fix it");
            Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this,"You cannot make map requests",Toast.LENGTH_SHORT).show();
        }
        return false;
    }



    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_act,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_logout:
                mauth.signOut();
                startActivity(new Intent(MainActivity.this,activity_login.class));
                return true;

        }

        return false;
    }

    private void getCallPermission(){
        Log.d(TAG, "getLocationPermission: Getting call permissions");
        String[] permissions={Manifest.permission.CALL_PHONE};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                mCallPermissionGranted = true;
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:18001801551"));
            startActivity(callIntent);
        }
        else{
            ActivityCompat.requestPermissions(this,
                    permissions,CALL_PERMISSION_REQUEST_CODE);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mCallPermissionGranted = false;
        Log.d(TAG, "onRequestPermissionsResult: called");

        switch (requestCode) {
            case CALL_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED) {
                            mCallPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission Failed");
                            return;
                        }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mCallPermissionGranted = true;
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:18001801551"));
                    startActivity(callIntent);
                }
            }
    }
    public void insertCrop() {

        Crop_db_helper dbHelper = new Crop_db_helper(getApplicationContext());
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(CropContract.CropEntry.COLUMN_CROP_NAME, "Wheat");
        cv.put(CropContract.CropEntry.COLUMN_CROP_STATE, "Uttar Pradesh, Punjab");
        cv.put(CropContract.CropEntry.COLUMN_CROP_INFO, "Wheat cultivation in India traditionally been dominated by the northern region of India. The northern states of Punjab and Haryana Plains in India have been prolific wheat producers. While this cereal grass has been studied carefully in the past, recent years of painstaking research by India's finest scientific talent has paid off with the development of distinctly superior varieties of Durum Wheat.\n" +
                "\n" +
                "This hard wheat is cultivated in clayey soil and is highly sought after for its physical characteristics. Its high gluten strength and uniform golden colour makes it ideal for bread making and pasta preparation unlike the softer commercially high yielding wheat, which lacks the strength and consistency of durum. Today, India is exporting sufficient quantities of all types of wheat and extensive research efforts are underway for improving its cereals and grain output in the years to come.Wheat cultivation has traditionally been dominated by the northern region of India. The northern states of Punjab and Haryana Plains in India have been prolific wheat producers. While this cereal grass has been studied carefully in the past, recent years of painstaking research by India's finest scientific talent has paid off with the development of distinctly superior varieties of Durum Wheat.With a production reaching ten times in past five years, India is today the second largest wheat producer in the whole world. Various studies and researches show that wheat and wheat flour play an increasingly important role in the management of India’s food economy.");

        ContentValues cv1 = new ContentValues();
        cv1.put(CropContract.CropEntry.COLUMN_CROP_NAME, "Maize");
        cv1.put(CropContract.CropEntry.COLUMN_CROP_STATE, "Karnaraka, Madhya Pradesh");
        cv1.put(CropContract.CropEntry.COLUMN_CROP_INFO, "Maize (Zea mays L.) is one of the most versatile emerging crop shaving wider adaptability under varied agro-climatic conditions. Globally, maize is known as queen of cereals because it has the highest genetic yield potential among the cereals. It is cultivated on nearly 190 m ha in about 165 countries having wider diversity of soil, climate, biodiversity and management practices that contributes 39 % in the global grain production. The United States of America (USA) is the largest producer of maize contributes nearly 36% of the total production in the world and maize is the driver of the US economy. In India, Maize is grown throughout the year. It is predominantly a kharif crop with 85 per cent of the area under cultivation in the season. Maize is the third most important cereal crop in India after rice and wheat. It accounts for around 10 per cent of total food grain production in the country. In addition to staple food for human being and quality feed for animals, maize serves as a basic raw material as an ingredient to thousands of industrial products that includes starch, oil, protein, alcoholic beverages, food sweeteners, pharmaceutical, cosmetic, film, textile, gum, package and paper industries etc.");


        ContentValues cv2 = new ContentValues();
        cv2.put(CropContract.CropEntry.COLUMN_CROP_NAME, "Sugarcane");
        cv2.put(CropContract.CropEntry.COLUMN_CROP_STATE, "Uttar Pradesh, Maharashtra");
        cv2.put(CropContract.CropEntry.COLUMN_CROP_INFO, "\n" +
                "Sugarcane is a long duration crop and requires 10 to 15 and even 18 months to mature, depending upon the geographical conditions. It requires hot and humid climate with average temperature of 21°-27°C and 75-150 cm rainfall.\n" +
                "\n" +
                "In the latter half, temperature above 20°C combined with open sky helps in acquiring juice and its thickening. Too heavy rainfall results in low sugar content and deficiency in rainfall produces fibrous crop. Irrigation is required in areas receiving lesser rainfall than the prescribed limit. Short cool dry winter season during ripening and harvesting is ideal.\n" +
                "\n" +
                "Frost is detrimental to sugarcane. Therefore, it must be harvested before frost season, if it is grown in northern parts of the country where winters are very cold and frost is a common phenomenon. On the other hand, hot dry winds are also inimical to sugarcane.\n" +
                "\n" +
                "It can grow on a variety of soils including loams, clayey loams, black cotton soils, brown or reddish loams and even laterites. In fact, sugarcane can tolerate any kind of soil that can retain moisture. But deep rich loamy soils are ideal for its growth.\n" +
                "\n" +
                "The soil should be rich in nitrogen, calcium and phosphorus but it should not be either too acidic or too alkaline. Sugarcane exhausts the fertility of the soil quickly and extensively and its cultivation requires heavy dose of manures and fertilizers.-Flat plain or level plateau is an advantage for sugarcane cultivation because it facilitates irrigation and transportation of cane to the sugar mills.\n" +
                "\n" +
                "It is a labour intensive cultivation requiring ample human hands at every stage i.e. sowing, hoeing, weeding, irrigating, cutting and carrying sugarcane to the factories. Therefore, cheap abundant labour is a prerequisite for its successful cultivation.");


        ContentValues cv3= new ContentValues();
        cv3.put(CropContract.CropEntry.COLUMN_CROP_NAME, "Rice");
        cv3.put(CropContract.CropEntry.COLUMN_CROP_STATE, "Uttar Pradesh");
        cv3.put(CropContract.CropEntry.COLUMN_CROP_INFO, "Climatic Requirements:-In India rice is grown under widely varying conditions of altitude and climate. Rice cultivation in India extends from 8 to35ºN latitude and from sea level to as high as 3000 meters. Rice crop needs a hot and humid climate. It is best suited to regions which have high humidity, prolonged sunshine and an assured supply of water. The average temperature required throughout the life period of the crop ranges from 21 to 37º C. Maximum temp which the crop can tolerate 400C to 42 0C."+"\n"+"Crop Production Practices-:In India Rice is mainly grown in two types of soils i.e., (i) uplands and (ii) low lands. The method of cultivation of rice in a particular region depends largely on factors such as situation of land, type of soils, irrigation facilities, availability of labourers intensity and distribution of rainfalls. The crop of rice is grown with the following methods\n" +
                "Dry or Semi-dry upland cultivation\n" +
                "Broadcasting the seedSowing the seed behind the plough or drilling"+"\n"+"Wet or lowland cultivation\n" +
                "Transplanting in puddled fields.Broadcasting sprouted seeds in puddled fields.Selection of SeedsThe use of quality seeds in cultivation of rice is an important factor to get better crop yield. Therefore, proper care has to be taken in selecting seeds of the best quality. Much of the success in raising the healthy seedlings depends on the quality of seed. Seeds intended for sowing should satisfy the following requirements\n" +
                "The seed should belong to the proper variety, which is proposed to be grown.The seed should be clean and free from obvious mixtures of other seeds.The seed should be mature, well developed and plump in size.The seed should be free from obvious signs of age or bad storage.The seed should have a high germinating capacity.");

        ContentValues cv4= new ContentValues();
        cv4.put(CropContract.CropEntry.COLUMN_CROP_NAME, "Bajra");
        cv4.put(CropContract.CropEntry.COLUMN_CROP_STATE, "Madhya Pradesh");
        cv4.put(CropContract.CropEntry.COLUMN_CROP_INFO, "Climatic Requirements:-In India rice is grown under widely varying conditions of altitude and climate. Rice cultivation in India extends from 8 to35ºN latitude and from sea level to as high as 3000 meters. Rice crop needs a hot and humid climate. It is best suited to regions which have high humidity, prolonged sunshine and an assured supply of water. The average temperature required throughout the life period of the crop ranges from 21 to 37º C. Maximum temp which the crop can tolerate 400C to 42 0C."+"\n"+"Crop Production Practices-:In India Rice is mainly grown in two types of soils i.e., (i) uplands and (ii) low lands. The method of cultivation of rice in a particular region depends largely on factors such as situation of land, type of soils, irrigation facilities, availability of labourers intensity and distribution of rainfalls. The crop of rice is grown with the following methods\n" +
                "Dry or Semi-dry upland cultivation\n" +
                "Broadcasting the seedSowing the seed behind the plough or drilling"+"\n"+"Wet or lowland cultivation\n" +
                "Transplanting in puddled fields.Broadcasting sprouted seeds in puddled fields.Selection of SeedsThe use of quality seeds in cultivation of rice is an important factor to get better crop yield. Therefore, proper care has to be taken in selecting seeds of the best quality. Much of the success in raising the healthy seedlings depends on the quality of seed. Seeds intended for sowing should satisfy the following requirements\n" +
                "The seed should belong to the proper variety, which is proposed to be grown.The seed should be clean and free from obvious mixtures of other seeds.The seed should be mature, well developed and plump in size.The seed should be free from obvious signs of age or bad storage.The seed should have a high germinating capacity.");

        ContentValues cv5= new ContentValues();
        cv5.put(CropContract.CropEntry.COLUMN_CROP_NAME, "Jowar");
        cv5.put(CropContract.CropEntry.COLUMN_CROP_STATE, "Maharashtra");
        cv5.put(CropContract.CropEntry.COLUMN_CROP_INFO, "Climatic Requirements:-In India rice is grown under widely varying conditions of altitude and climate. Rice cultivation in India extends from 8 to35ºN latitude and from sea level to as high as 3000 meters. Rice crop needs a hot and humid climate. It is best suited to regions which have high humidity, prolonged sunshine and an assured supply of water. The average temperature required throughout the life period of the crop ranges from 21 to 37º C. Maximum temp which the crop can tolerate 400C to 42 0C."+"\n"+"Crop Production Practices-:In India Rice is mainly grown in two types of soils i.e., (i) uplands and (ii) low lands. The method of cultivation of rice in a particular region depends largely on factors such as situation of land, type of soils, irrigation facilities, availability of labourers intensity and distribution of rainfalls. The crop of rice is grown with the following methods\n" +
                "Dry or Semi-dry upland cultivation\n" +
                "Broadcasting the seedSowing the seed behind the plough or drilling"+"\n"+"Wet or lowland cultivation\n" +
                "Transplanting in puddled fields.Broadcasting sprouted seeds in puddled fields.Selection of SeedsThe use of quality seeds in cultivation of rice is an important factor to get better crop yield. Therefore, proper care has to be taken in selecting seeds of the best quality. Much of the success in raising the healthy seedlings depends on the quality of seed. Seeds intended for sowing should satisfy the following requirements\n" +
                "The seed should belong to the proper variety, which is proposed to be grown.The seed should be clean and free from obvious mixtures of other seeds.The seed should be mature, well developed and plump in size.The seed should be free from obvious signs of age or bad storage.The seed should have a high germinating capacity.");


        ContentValues cv6= new ContentValues();
        cv6.put(CropContract.CropEntry.COLUMN_CROP_NAME, "Apple");
        cv6.put(CropContract.CropEntry.COLUMN_CROP_STATE, "Himachal Pradesh");
        cv6.put(CropContract.CropEntry.COLUMN_CROP_INFO, "Climatic Requirements:-In India rice is grown under widely varying conditions of altitude and climate. Rice cultivation in India extends from 8 to35ºN latitude and from sea level to as high as 3000 meters. Rice crop needs a hot and humid climate. It is best suited to regions which have high humidity, prolonged sunshine and an assured supply of water. The average temperature required throughout the life period of the crop ranges from 21 to 37º C. Maximum temp which the crop can tolerate 400C to 42 0C."+"\n"+"Crop Production Practices-:In India Rice is mainly grown in two types of soils i.e., (i) uplands and (ii) low lands. The method of cultivation of rice in a particular region depends largely on factors such as situation of land, type of soils, irrigation facilities, availability of labourers intensity and distribution of rainfalls. The crop of rice is grown with the following methods\n" +
                "Dry or Semi-dry upland cultivation\n" +
                "Broadcasting the seedSowing the seed behind the plough or drilling"+"\n"+"Wet or lowland cultivation\n" +
                "Transplanting in puddled fields.Broadcasting sprouted seeds in puddled fields.Selection of SeedsThe use of quality seeds in cultivation of rice is an important factor to get better crop yield. Therefore, proper care has to be taken in selecting seeds of the best quality. Much of the success in raising the healthy seedlings depends on the quality of seed. Seeds intended for sowing should satisfy the following requirements\n" +
                "The seed should belong to the proper variety, which is proposed to be grown.The seed should be clean and free from obvious mixtures of other seeds.The seed should be mature, well developed and plump in size.The seed should be free from obvious signs of age or bad storage.The seed should have a high germinating capacity.");

        ContentValues cv7= new ContentValues();
        cv7.put(CropContract.CropEntry.COLUMN_CROP_NAME, "Tea");
        cv7.put(CropContract.CropEntry.COLUMN_CROP_STATE, "Assam");
        cv7.put(CropContract.CropEntry.COLUMN_CROP_INFO, "Climatic Requirements:-In India rice is grown under widely varying conditions of altitude and climate. Rice cultivation in India extends from 8 to35ºN latitude and from sea level to as high as 3000 meters. Rice crop needs a hot and humid climate. It is best suited to regions which have high humidity, prolonged sunshine and an assured supply of water. The average temperature required throughout the life period of the crop ranges from 21 to 37º C. Maximum temp which the crop can tolerate 400C to 42 0C."+"\n"+"Crop Production Practices-:In India Rice is mainly grown in two types of soils i.e., (i) uplands and (ii) low lands. The method of cultivation of rice in a particular region depends largely on factors such as situation of land, type of soils, irrigation facilities, availability of labourers intensity and distribution of rainfalls. The crop of rice is grown with the following methods\n" +
                "Dry or Semi-dry upland cultivation\n" +
                "Broadcasting the seedSowing the seed behind the plough or drilling"+"\n"+"Wet or lowland cultivation\n" +
                "Transplanting in puddled fields.Broadcasting sprouted seeds in puddled fields.Selection of SeedsThe use of quality seeds in cultivation of rice is an important factor to get better crop yield. Therefore, proper care has to be taken in selecting seeds of the best quality. Much of the success in raising the healthy seedlings depends on the quality of seed. Seeds intended for sowing should satisfy the following requirements\n" +
                "The seed should belong to the proper variety, which is proposed to be grown.The seed should be clean and free from obvious mixtures of other seeds.The seed should be mature, well developed and plump in size.The seed should be free from obvious signs of age or bad storage.The seed should have a high germinating capacity.");

        ContentValues cv8= new ContentValues();
        cv8.put(CropContract.CropEntry.COLUMN_CROP_NAME, "Coffee");
        cv8.put(CropContract.CropEntry.COLUMN_CROP_STATE, "Mizoram");
        cv8.put(CropContract.CropEntry.COLUMN_CROP_INFO, "Climatic Requirements:-In India rice is grown under widely varying conditions of altitude and climate. Rice cultivation in India extends from 8 to35ºN latitude and from sea level to as high as 3000 meters. Rice crop needs a hot and humid climate. It is best suited to regions which have high humidity, prolonged sunshine and an assured supply of water. The average temperature required throughout the life period of the crop ranges from 21 to 37º C. Maximum temp which the crop can tolerate 400C to 42 0C."+"\n"+"Crop Production Practices-:In India Rice is mainly grown in two types of soils i.e., (i) uplands and (ii) low lands. The method of cultivation of rice in a particular region depends largely on factors such as situation of land, type of soils, irrigation facilities, availability of labourers intensity and distribution of rainfalls. The crop of rice is grown with the following methods\n" +
                "Dry or Semi-dry upland cultivation\n" +
                "Broadcasting the seedSowing the seed behind the plough or drilling"+"\n"+"Wet or lowland cultivation\n" +
                "Transplanting in puddled fields.Broadcasting sprouted seeds in puddled fields.Selection of SeedsThe use of quality seeds in cultivation of rice is an important factor to get better crop yield. Therefore, proper care has to be taken in selecting seeds of the best quality. Much of the success in raising the healthy seedlings depends on the quality of seed. Seeds intended for sowing should satisfy the following requirements\n" +
                "The seed should belong to the proper variety, which is proposed to be grown.The seed should be clean and free from obvious mixtures of other seeds.The seed should be mature, well developed and plump in size.The seed should be free from obvious signs of age or bad storage.The seed should have a high germinating capacity.");

        ContentValues cv9= new ContentValues();
        cv9.put(CropContract.CropEntry.COLUMN_CROP_NAME, "Cotton");
        cv9.put(CropContract.CropEntry.COLUMN_CROP_STATE, "Madhya Pradesh");
        cv9.put(CropContract.CropEntry.COLUMN_CROP_INFO, "Climatic Requirements:-In India rice is grown under widely varying conditions of altitude and climate. Rice cultivation in India extends from 8 to35ºN latitude and from sea level to as high as 3000 meters. Rice crop needs a hot and humid climate. It is best suited to regions which have high humidity, prolonged sunshine and an assured supply of water. The average temperature required throughout the life period of the crop ranges from 21 to 37º C. Maximum temp which the crop can tolerate 400C to 42 0C."+"\n"+"Crop Production Practices-:In India Rice is mainly grown in two types of soils i.e., (i) uplands and (ii) low lands. The method of cultivation of rice in a particular region depends largely on factors such as situation of land, type of soils, irrigation facilities, availability of labourers intensity and distribution of rainfalls. The crop of rice is grown with the following methods\n" +
                "Dry or Semi-dry upland cultivation\n" +
                "Broadcasting the seedSowing the seed behind the plough or drilling"+"\n"+"Wet or lowland cultivation\n" +
                "Transplanting in puddled fields.Broadcasting sprouted seeds in puddled fields.Selection of SeedsThe use of quality seeds in cultivation of rice is an important factor to get better crop yield. Therefore, proper care has to be taken in selecting seeds of the best quality. Much of the success in raising the healthy seedlings depends on the quality of seed. Seeds intended for sowing should satisfy the following requirements\n" +
                "The seed should belong to the proper variety, which is proposed to be grown.The seed should be clean and free from obvious mixtures of other seeds.The seed should be mature, well developed and plump in size.The seed should be free from obvious signs of age or bad storage.The seed should have a high germinating capacity.");


        ContentValues cv10= new ContentValues();
        cv10.put(CropContract.CropEntry.COLUMN_CROP_NAME, "Ginger");
        cv10.put(CropContract.CropEntry.COLUMN_CROP_STATE, "Uttar Pradesh");
        cv10.put(CropContract.CropEntry.COLUMN_CROP_INFO, "Climatic Requirements:-In India rice is grown under widely varying conditions of altitude and climate. Rice cultivation in India extends from 8 to35ºN latitude and from sea level to as high as 3000 meters. Rice crop needs a hot and humid climate. It is best suited to regions which have high humidity, prolonged sunshine and an assured supply of water. The average temperature required throughout the life period of the crop ranges from 21 to 37º C. Maximum temp which the crop can tolerate 400C to 42 0C."+"\n"+"Crop Production Practices-:In India Rice is mainly grown in two types of soils i.e., (i) uplands and (ii) low lands. The method of cultivation of rice in a particular region depends largely on factors such as situation of land, type of soils, irrigation facilities, availability of labourers intensity and distribution of rainfalls. The crop of rice is grown with the following methods\n" +
                "Dry or Semi-dry upland cultivation\n" +
                "Broadcasting the seedSowing the seed behind the plough or drilling"+"\n"+"Wet or lowland cultivation\n" +
                "Transplanting in puddled fields.Broadcasting sprouted seeds in puddled fields.Selection of SeedsThe use of quality seeds in cultivation of rice is an important factor to get better crop yield. Therefore, proper care has to be taken in selecting seeds of the best quality. Much of the success in raising the healthy seedlings depends on the quality of seed. Seeds intended for sowing should satisfy the following requirements\n" +
                "The seed should belong to the proper variety, which is proposed to be grown.The seed should be clean and free from obvious mixtures of other seeds.The seed should be mature, well developed and plump in size.The seed should be free from obvious signs of age or bad storage.The seed should have a high germinating capacity.");

        long a=db.insert(CropContract.CropEntry.TABLE_NAME,null,cv);
        long b=db.insert(CropContract.CropEntry.TABLE_NAME,null,cv1);
        long c=db.insert(CropContract.CropEntry.TABLE_NAME,null,cv2);
        long d=db.insert(CropContract.CropEntry.TABLE_NAME,null,cv3);
        long e=db.insert(CropContract.CropEntry.TABLE_NAME,null,cv4);
        long f=db.insert(CropContract.CropEntry.TABLE_NAME,null,cv5);
        long g=db.insert(CropContract.CropEntry.TABLE_NAME,null,cv6);
        long h=db.insert(CropContract.CropEntry.TABLE_NAME,null,cv7);
        long i=db.insert(CropContract.CropEntry.TABLE_NAME,null,cv8);
        long j=db.insert(CropContract.CropEntry.TABLE_NAME,null,cv9);
        long k=db.insert(CropContract.CropEntry.TABLE_NAME,null,cv10);

    }

    public void flipperImage(int image)
    {
        ImageView imageView=new ImageView(this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
}
