package com.himanshu.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener
{
    // UI COMPONENTS
    private Button btnSave;
    private EditText edtName, edtPunchSpeed , edtPunchPower , edtKickSpeed , edtKickPower ;
    private TextView txtGetData;

    private Button btnGetAllData;

    private String allKickBoxers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSave = findViewById(R.id.btnSave);
        edtName = findViewById(R.id.edtName);
        edtPunchSpeed = findViewById(R.id.edtPunchSpeed);
        edtPunchPower = findViewById(R.id.edtPunchPower);
        edtKickSpeed = findViewById(R.id.edtKickSpeed);
        edtKickPower = findViewById(R.id.edtKickPower);


        btnSave.setOnClickListener(SignUp.this);            // instead of using the anonymous class, we will use the implemented interface's abstract method.


        txtGetData = findViewById(R.id.txtGetData);         // to get data back from server.
        txtGetData.setOnClickListener(new View.OnClickListener()            // setting the onClickListener() for txtGetData
        {
            @Override
            public void onClick(View v)
            {
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Kickboxer");    // query ys saved from the Kickboxer class
                parseQuery.getInBackground("EjgX9A88WJ", new GetCallback<ParseObject>()                 // getInBackground used to get only one object at a time.
                {
                    @Override
                    public void done(ParseObject object, ParseException e)
                    {
                        if(object != null && e == null)
                        {
                            txtGetData.setText(object.get("name").toString() + " - " + "Punch Power : " + object.get("punchPower"));
                            // gives the data name and the punchPower from the object of given objectID from the server
                        }
                    }
                });
            }
        });


        btnGetAllData = findViewById(R.id.btnGetAllData);
        btnGetAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                allKickBoxers = "";

                ParseQuery<ParseObject> queryAll = ParseQuery.getQuery("Kickboxer");
                queryAll.findInBackground(new FindCallback<ParseObject>()               // findInBackground is used to get all the objects present under the same class.
                {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e)
                    {
                        if(e == null)
                        {
                            if(objects.size() > 0)
                            {
                                for(ParseObject kickBoxers : objects)
                                {
                                    allKickBoxers = allKickBoxers + kickBoxers.get("name") + "\n";
                                }
//                                FancyToast.makeText(SignUp.this,"Success",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                                FancyToast.makeText(SignUp.this , allKickBoxers , FancyToast.LENGTH_SHORT , FancyToast.SUCCESS ,true).show();
                            }
                            else
                            {
                                FancyToast.makeText(SignUp.this, "Failure , the size of the objects list is 0." , FancyToast.LENGTH_LONG , FancyToast.ERROR ,true).show();
                            }
                        }
                        else
                        {
                            FancyToast.makeText(SignUp.this, e.getMessage() , FancyToast.LENGTH_LONG , FancyToast.ERROR ,true).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onClick(View v)
    {
        try{

        // creating parseobject
            final ParseObject kickBoxer = new ParseObject("Kickboxer");
            kickBoxer.put("name" , edtName.getText().toString());
            kickBoxer.put("punchSpeed" , Integer.parseInt(edtPunchSpeed.getText().toString()));
            kickBoxer.put("punchPower" , Integer.parseInt(edtPunchPower.getText().toString()));
            kickBoxer.put("kickSpeed" , Integer.parseInt(edtKickSpeed.getText().toString()));
            kickBoxer.put("kickPower" , Integer.parseInt(edtKickPower.getText().toString()));

             kickBoxer.saveInBackground(new SaveCallback()
            {
                @Override
                public void done(ParseException e) {
                if(e == null)
                {
//                    Toast.makeText(SignUp.this , kickBoxer.get("name") +
//                            " is saved successfully in parse server" , Toast.LENGTH_SHORT).show();

                    // using fancy toast message after adding external library.
                    FancyToast.makeText(SignUp.this,kickBoxer.get("name") +
                            " is saved successfully in parse server",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                }
                else
                {
//                    Toast.makeText(SignUp.this , e.getMessage(), Toast.LENGTH_SHORT).show();      // e.getMessage() tells us what is the error.

                    // using fancy toast message
                    FancyToast.makeText(SignUp.this, e.getMessage() , FancyToast.LENGTH_LONG , FancyToast.ERROR ,true).show();
                }
            }
         });

        }
        catch(Exception e)
        {
            FancyToast.makeText(SignUp.this, e.getMessage() , FancyToast.LENGTH_LONG , FancyToast.ERROR ,true).show();
        }



    }

//    public void helloWorldIsTapped(View view)
//    {
////        // CREATING PARSE OBJECT
////        ParseObject boxer = new ParseObject("Boxer");
////        boxer.put("punch_speed" , 200);
////        //boxer.saveInBackground();             // this directly try to save the object in the server (without notifying whether it is saved or not).
////
////        boxer.saveInBackground(new SaveCallback() {
////            @Override
////            public void done(ParseException e)   //  e will have some value except null (if object is'nt saved in the server).
////            {
////                if(e == null)
////                {
////                    Toast.makeText(SignUp.this , "boxer object is saved successfully" , Toast.LENGTH_SHORT).show();
////                }
////                else
////                {
////                    Toast.makeText(SignUp.this , "There is some error in saving the object", Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
//
//
//        // CREATING ANOTHER PARSE OBJECT
//        final ParseObject kickboxer = new ParseObject("Kickboxer");
//        kickboxer.put("name" , "John");
//        kickboxer.put("punchSpeed" , 1000);
//        kickboxer.put("punchPower" , 2000);
//        kickboxer.put("kickSpeed" , 3000);
//        kickboxer.put("kickPower" , 4000);
//
//        kickboxer.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null)
//                {
//                    Toast.makeText(SignUp.this , kickboxer.get("name") + " is saved successfully in parse server" , Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
}
