package com.example.nfer.assignment5;

        import android.app.Activity;
        import android.content.ContentValues;
        import android.database.Cursor;
        import android.graphics.Color;
        import android.net.Uri;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.CursorAdapter;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    Cursor mCursor;
    CursorAdapter mCursorAdapter;
    Button reset, submit;
    EditText empID, nameIn, emailIn, codeIn, confirmIn ;
    RadioButton button1, button2;
    CheckBox check;
    TextView empIDView, nameView, emailView, codeView, confirmView, genderView;
    String deptVal, genderVal;
    Spinner deptSpin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailView   = (TextView)findViewById(R.id.emailView);
        empIDView   = (TextView)findViewById(R.id.empIDView);
        nameView    = (TextView)findViewById(R.id.nameView);
        genderView  = (TextView)findViewById(R.id.genderView);
        codeView    = (TextView)findViewById(R.id.codeView);
        confirmView = (TextView)findViewById(R.id.confirmView);
        submit      = (Button)findViewById(R.id.button);
        reset       = (Button)findViewById(R.id.button2);
        empID       = (EditText)findViewById(R.id.empIDText);
        nameIn      = (EditText)findViewById(R.id.nameIn);
        emailIn     = (EditText)findViewById(R.id.emailIn);
        codeIn      = (EditText)findViewById(R.id.codeIn);
        confirmIn   = (EditText)findViewById(R.id.confirmIn);
        button1     = (RadioButton)findViewById(R.id.radioButton);
        button2     = (RadioButton)findViewById(R.id.radioButton2);
        check       = (CheckBox)findViewById(R.id.checkBox);
        deptSpin    = (Spinner)findViewById(R.id.spinner);
    }

    public void resetFields (){
        empID.setText("");
        nameIn.setText("");
        emailIn.setText("");
        codeIn.setText("");
        confirmIn.setText("");
        button1.setChecked(false);
        button2.setChecked(false);
        check.setChecked(false);
        empIDView.setTextColor(Color.GRAY);
        nameView.setTextColor(Color.GRAY);
        emailView.setTextColor(Color.GRAY);
        codeView.setTextColor(Color.GRAY);
        confirmView.setTextColor(Color.GRAY);
        genderView.setTextColor(Color.GRAY);

    }

    public void myEventHandler (View v) {
        if (v == reset) {
            resetFields();
        }
        if (v == submit) {
            int isFilled = 0;
            boolean validId = false;
            boolean isDup = false;
            Uri mNewUri;
            String adAccess   = "False";
            String empIdVal = empID.getText().toString();
            String nameVal = nameIn.getText().toString();
            String emailVal = emailIn.getText().toString();
            String codeString = codeIn.getText().toString();
            String confirmVal = confirmIn.getText().toString();

            String mSelectionClause  = MyContentProvider.ID + " = ? ";
            String []mSelectionArgs = new String []  {empIdVal};

            mCursor = getContentResolver().query(MyContentProvider.CONTENT_URI, null,
                    mSelectionClause, mSelectionArgs, null);
            if (mCursor != null ) {
                if (mCursor.getCount() > 0) {
                    isDup = true;
                }
            }

            //Start Invalid input Check
            if (!confirmVal.equals(codeString)) {
                codeView.setTextColor(Color.RED);
                confirmView.setTextColor(Color.RED);
                codeIn.setError("Passwords do not match!");
                confirmIn.setError("Passwords do not match!");
            }

            if (isDup == true){
                empIDView.setTextColor(Color.RED);
                empID.setError("EMPID already exists in Database!");
                isFilled++;
            }

            if (empIdVal.isEmpty()) {
                empIDView.setTextColor(Color.RED);
                empID.setError("This field cannot be blank");
                isFilled++;
            }
            if (nameVal.isEmpty()) {
                nameView.setTextColor(Color.RED);
                nameIn.setError("This field cannot be blank");
                isFilled++;
            }
            if (!button1.isChecked() && !button2.isChecked()) {
                genderView.setTextColor(Color.RED);
            }
            if (emailVal.isEmpty()) {
                emailView.setTextColor(Color.RED);
                emailIn.setError("This field cannot be blank");
                isFilled++;
            }
            if (codeString.isEmpty()) {
                codeView.setTextColor(Color.RED);
                codeIn.setError("This field cannot be blank");
                isFilled++;
            }
            if (confirmVal.isEmpty()) {
                confirmView.setTextColor(Color.RED);
                confirmIn.setError("This field cannot be blank");
                isFilled++;
            }
            //end invalid input check

            //get values for gender and A/D Access
            if (isFilled == 0 && isDup == false) {
                if (button1.isChecked()) {
                    genderVal = "Male";
                }
                if (button2.isChecked()) {
                    genderVal = "Female";
                }

                if (check.isChecked()) {
                    adAccess = "true";
                }

                //Get value of spinner choice
                deptVal = deptSpin.getSelectedItem().toString();


                ContentValues mValues = new ContentValues();

                //If we've made it down to here, go ahead and add data to the db
                mValues.put(MyContentProvider.ID, empIdVal.trim());
                mValues.put(MyContentProvider.NAME, nameVal.trim());
                mValues.put(MyContentProvider.GENDER, genderVal.trim());
                mValues.put(MyContentProvider.EMAIL, emailVal.trim());
                mValues.put(MyContentProvider.CODE, codeString.trim());
                mValues.put(MyContentProvider.DEPT, deptVal.trim());
                mValues.put(MyContentProvider.ACCESS, adAccess.trim());

                //sends data to db
                mNewUri = getContentResolver().insert(MyContentProvider.CONTENT_URI, mValues);

                Toast.makeText(this, "Information Successfully Stored!",Toast.LENGTH_SHORT).show();

                //reset for another submission
                resetFields();
            }
        }
    }
}

