package event.secure.secureyourevent.Activity.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

import event.secure.secureyourevent.Activity.Fragments.DatePickerFrag;
import event.secure.secureyourevent.Activity.Model.Events;
import event.secure.secureyourevent.R;


/**
 * Created by priyankakhatta on 2018-02-15.
 */

public class AddNewEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private EditText eventTitleEditText,dateEditText,eventPeriodEditText, uniformEditText, openEventTillEditText,jobDescriptionEditText,
            payEditText, numberOfGuardsEditText, addressOfEventEditText, instructionsEditText;
    private ImageButton startDateImageButton,endDateImageButton;
    private RadioGroup preferenceradioGroup;
    private RadioButton companyGuardRadioButton,IndividualGuardRadioButton, bothRadioButton;
    private Button saveEventButton;
    private DatabaseReference dbEvent;
    private  String currentDateString;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_event);
        dbEvent = FirebaseDatabase.getInstance().getReference("Events");

        eventTitleEditText = (EditText)findViewById(R.id.eventTitleEditText);
        payEditText = (EditText)findViewById(R.id.payEditText);
        numberOfGuardsEditText = (EditText)findViewById(R.id.numberOfGuardsEditText);
        addressOfEventEditText = (EditText)findViewById(R.id.addressOfEventEditText);
        dateEditText = (EditText)findViewById(R.id.dateEditText);
        eventPeriodEditText = (EditText)findViewById(R.id.eventPeriodEditText);
        uniformEditText = (EditText)findViewById(R.id.uniformEditText);
        openEventTillEditText = (EditText)findViewById(R.id.openEventTillEditText);
        jobDescriptionEditText = (EditText)findViewById(R.id.jobDescriptionEditText);
        instructionsEditText = (EditText)findViewById(R.id.instructionsEditText);
        endDateImageButton = (ImageButton)findViewById(R.id.endDateImageButton);
        saveEventButton = (Button) findViewById(R.id.saveEventButton);


        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                addEvent();

            }
        });


        endDateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            DialogFragment datePicker = new DatePickerFrag();
            datePicker.show(getSupportFragmentManager(), "DatePicker");

            }
        });

    }



    private void addEvent() {

        String eventTitleEditTextStr = eventTitleEditText.getText().toString();
        String payEditTextStr = payEditText.getText().toString();
        String numberOfGuardsEditTextStr = numberOfGuardsEditText.getText().toString();
        String addressOfEventEditTextStr = addressOfEventEditText.getText().toString();
        String dateEditTextStr = dateEditText.getText().toString();
        String eventPeriodEditTextStr = eventPeriodEditText.getText().toString();
        String uniformEditTextStr = uniformEditText.getText().toString();
        String openEventTillEditTextStr = openEventTillEditText.getText().toString();
        String jobDescriptionEditTextStr = jobDescriptionEditText.getText().toString();
        String instructionsEditTextStr = instructionsEditText.getText().toString();

        if((!TextUtils.isEmpty(eventTitleEditTextStr)) || (!TextUtils.isEmpty(payEditTextStr))||(!TextUtils.isEmpty(numberOfGuardsEditTextStr))
                || (!TextUtils.isEmpty(openEventTillEditTextStr))|| (!TextUtils.isEmpty(addressOfEventEditTextStr)) || (!TextUtils.isEmpty(dateEditTextStr))
                ||(!TextUtils.isEmpty(eventPeriodEditTextStr)) || (!TextUtils.isEmpty(jobDescriptionEditTextStr))||(!TextUtils.isEmpty(uniformEditTextStr))
                || (!TextUtils.isEmpty(instructionsEditTextStr))){

            String id = dbEvent.push().getKey();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Events newEvent = new Events(id, uid, eventTitleEditTextStr, dateEditTextStr, eventPeriodEditTextStr, uniformEditTextStr, openEventTillEditTextStr,jobDescriptionEditTextStr,
                    payEditTextStr, numberOfGuardsEditTextStr, addressOfEventEditTextStr, instructionsEditTextStr);

            dbEvent.child(id).setValue(newEvent);

            Toast.makeText(getApplicationContext(),"Event Added Successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AddNewEvent.this, MainActivity.class);
            startActivity(intent);
            finish();


        }
        else {
            Toast.makeText(getApplicationContext(),"All fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

         currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
         dateEditText.setText(currentDateString);

    }
}
