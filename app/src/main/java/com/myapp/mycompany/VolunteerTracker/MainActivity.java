package net.empoweringtechnology.volunteertracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {
    public EditText organization;
    public EditText activity;
    public EditText activitydate;
    public EditText volunteerHours;
    public net.empoweringtechnology.volunteertracker.MyDBHandler dbHandler;
    public int idValue;
    public String mCurrentPhotoPath;
    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final String VolunteerFolder = "MyVoluntering";
    public static int event_year=0;
    public static int event_month=0;
    public static int event_day=0;
    static final int DATE_DIALOG_ID = 0;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent myIntent = getIntent(); // gets the previously created intent
        idValue = myIntent.getIntExtra("idValue", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        organization = (EditText) findViewById(R.id.editText_Organization);
        activity = (EditText) findViewById(R.id.editText_Activity);
        activitydate = (EditText) findViewById(R.id.editText_EventDate);
        volunteerHours = (EditText) findViewById(R.id.editText_Hours);

        dateFormatter = new SimpleDateFormat("mm/dd/yyyy",Locale.US);

        if (idValue > 0) {
            organization.setText(myIntent.getStringExtra("orgValue"));
            activity.setText(myIntent.getStringExtra("actValue"));
            activitydate.setText(myIntent.getStringExtra("dateValue"));
            volunteerHours.setText(myIntent.getStringExtra("hoursValue"));
        }
        dbHandler = new net.empoweringtechnology.volunteertracker.MyDBHandler(this, null, null, 1);

        activitydate.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                if(v == activitydate)
                    showDialog(DATE_DIALOG_ID);
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* method to add or save activity data */
    public void Record(View view) {
        Hours hours = new Hours(organization.getText().toString(), activity.getText().toString(), activitydate.getText().toString(), volunteerHours.getText().toString());

        /* validations on user iput data */
        if (organization.getText().toString().length() == 0) {
            organization.setError("Organization is required!");
            return;
        }
        if (activity.getText().toString().length() == 0) {
            activity.setError("Activity is required!");
            return;
        }
        if (activitydate.length() == 0) {
           activitydate.setError("Activity date is required!");
            return;
        }
        if (volunteerHours.getText().toString().length() == 0) {
            volunteerHours.setError("Volunteer time is required!");
            return;
        }
        if (Float.parseFloat(volunteerHours.getText().toString()) > 100) {
            volunteerHours.setError("Volunteer time is limited to less than 100 hours!");
            return;
        }

        /* Did we arrive here from an edit of an existing activity or a new activity data save?
           The iDVal will be greater than 0 if this is an edit of an existing activity data
         */

        if (idValue > 0)
            dbHandler.UpdateHours(hours, idValue);
        else
            dbHandler.SaveHours(hours);
        Clear();
    }

    /* method to clear the inout fields in the Save Activity page */
    public void Clear() {
        organization.setText("");
        activity.setText("");
        //activitydate.setText("");
        volunteerHours.setText("");
    }

    /* When user clicks cancel, we need to clear all input data */
    public void Cancel(View view) {
        Clear();
    }

    /* method to navigate to Home page */
    public void GoHome(View view) {
        Intent intent = new Intent(this, net.empoweringtechnology.volunteertracker.HomePage.class);
        startActivity(intent);
    }

    /* method to navigate to Reports page */
    public void GoReport(View view) {
        Intent intent = new Intent(this, net.empoweringtechnology.volunteertracker.Reports.class);
        startActivity(intent);
    }

    /* method to take a picture of volunteering and save to gallery */
    public void TakePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                /* add the picture to gallery */
                galleryAddPic();
            }
        }
    }

    /* method to create the picture file location */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES + File.separator + VolunteerFolder);
        File image = null;
        try {

            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        } catch (Exception ex) {
            ex.getMessage();
        }
        return image;
    }

    /* method to add picture to gallery */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,mDateSetListener,  cyear, cmonth, cday);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        // onDateSet method
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date_selected = String.valueOf(monthOfYear+1)+" /"+String.valueOf(dayOfMonth)+" /"+String.valueOf(year);
            //Toast.makeText(getApplicationContext(), "Selected Date is ="+date_selected, Toast.LENGTH_SHORT).show();
            activitydate.setText(date_selected);
        }
    };
}

