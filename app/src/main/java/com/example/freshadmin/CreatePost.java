package com.example.freshadmin;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.freshadmin.databinding.ActivityCreatePostBinding;
import com.example.freshadmin.utils.AutoCompleteTextViewError;
import com.example.freshadmin.utils.ChipHelper;
import com.example.freshadmin.utils.ParseErrorHandler;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreatePost extends AppCompatActivity {
    ActivityCreatePostBinding binding;
    HashMap<String, Object> params = new HashMap<>();
    int value = -1;
    ArrayList<String> cityList;
    List<String> locationList;

    ArrayAdapter[] spinnerAdapter;


    HashMap<String, Integer> mapping = new HashMap<>();
    ArrayList<ParseObject> ParseObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbarInitialization();

        //Make it smoother
        new Handler().postDelayed(() -> {
            init();
            setChipOnClickListeners();
            setSpinnerListeners();
            fetchSpinnerData();
        }, 500);
    }

    private void setSpinnerListeners() {

        binding.cityAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            int j = mapping.get(parent.getItemAtPosition(position).toString().trim());

            locationList = ParseObjects.get(j).getList("location");

            spinnerAdapter[2] = new ArrayAdapter(this, R.layout.center_color_spinner_layout_dark_blue, locationList);

            binding.locationAutoComplete.setAdapter(spinnerAdapter[2]);
            binding.locationAutoComplete.setText(spinnerAdapter[2].getItem(0).toString().trim(), false);

            if (!binding.locationExpand.isExpanded()) {
                binding.locationExpand.toggle();
            }
        });

        binding.std1ClassAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            binding.std1ClassAutoComplete.setSelection(0);
        });

        binding.std2ClassAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            binding.std2ClassAutoComplete.setSelection(0);
        });

        binding.locationAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            binding.locationAutoComplete.setSelection(0);
        });

    }

    private void setChipOnClickListeners() {
        binding.expandStudent1.setExpanded(false);
        binding.expandStudent2.setExpanded(false);

        binding.numberOne.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.expandStudent1.collapse(true);
                binding.expandStudent2.collapse(true);
            }
        });

        binding.numberTwo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.expandStudent1.expand(true);
                binding.expandStudent2.expand(true);
            }
        });
    }

    private void toolbarInitialization() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Tuition");
    }

    private void init() {

        binding.expandStudent1.setExpanded(false);
        binding.expandStudent2.setExpanded(false);

        binding.locationExpand.setExpanded(false, false);


        binding.linearLayout.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                getCurrentFocus().clearFocus();
                hideKeyboard(binding.linearLayout);
            }
        });

        binding.cityAutoComplete.setKeyListener(null);
        binding.locationAutoComplete.setKeyListener(null);
        binding.std1ClassAutoComplete.setKeyListener(null);
        binding.std2ClassAutoComplete.setKeyListener(null);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void fetchSpinnerData() {

        ParseCloud.callFunctionInBackground("getLocationS", new HashMap<>(),
                (FunctionCallback<List<Object>>) (objects, e) -> {
                    if (e == null) {
                        ParseObjects = (ArrayList) objects.get(0);

                        int i = 0;
                        cityList = new ArrayList<>();

                        for (ParseObject obj : ParseObjects) {
                            String cityName = obj.getString("cityName");
                            mapping.put(cityName, i);
                            cityList.add(cityName);
                            i++;
                        }

                        ParseObject temp = (ParseObject) objects.get(1);
                        List<Object> classList = temp.getList("list");

                        spinnerAdapter = new ArrayAdapter[3];

                        spinnerAdapter[0] = new ArrayAdapter(this, R.layout.center_color_spinner_layout_dark_blue, classList);
                        spinnerAdapter[1] = new ArrayAdapter(this, R.layout.center_color_spinner_layout_dark_blue, cityList);
                        spinnerAdapter[2] = null;

                        binding.std1ClassAutoComplete.setAdapter(spinnerAdapter[0]);
                        binding.std2ClassAutoComplete.setAdapter(spinnerAdapter[0]);
                        binding.cityAutoComplete.setAdapter(spinnerAdapter[1]);
                        binding.locationAutoComplete.setAdapter(spinnerAdapter[2]);

                    } else {
                        if (e.getCode() == ParseException.CONNECTION_FAILED) {
                            retry();
                        } else {
                            ParseErrorHandler.handleParseError(e, this, "getLocationS");
                        }
                    }
                });
    }

    private void retry() {
        Toast.makeText(this, "pls retry", Toast.LENGTH_SHORT).show();
    }


    public void getTime(final View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                String amPm;
                if (hourOfDay == 12) {
                    amPm = "PM";
                } else if (hourOfDay > 12) {
                    amPm = "PM";
                    hourOfDay = hourOfDay - 12;
                } else if (hourOfDay == 0) {
                    hourOfDay = 12;
                    amPm = "AM";
                } else {
                    amPm = "AM";
                }
                if (view.getId() == R.id.txt_chooseTime1) {
                    binding.txtChooseTime1.setError(null);
                    binding.txtChooseTime1.setText(String.format("%02d:%02d", hourOfDay, minutes) + " " + amPm);
                } else {
                    binding.txtChooseTime2.setError(null);
                    binding.txtChooseTime2.setText(String.format("%02d:%02d", hourOfDay, minutes) + " " + amPm);
                }
            }
        }, 12, 0, false);
        timePickerDialog.show();
    }


    public void submitPost(View view) {
        if (spinnerAdapter == null) {
            //Haha, spam as much as you want
        } else {
            checkingForEmptyFields(Integer.parseInt(ChipHelper.create().getCheckedChipTextFromGroup(
                    binding.numberOfStudentsChipGroup, this
            )));
        }

    }


    private void checkingForEmptyFields(int i) {

        //Checking Salary Input
        String salary = binding.etxtSalary.getText().toString().trim();
        builder = new StringBuilder();

        boolean flag = false;

        for (int x = 0; x < salary.length(); x++) {
            if (salary.charAt(x) != '0') {
                flag = true;
            }

            if (flag) {
                builder.append(salary.charAt(x));
            }
        }

        //------------------------------->


        boolean empty = false;

        if (i == 2) {
            if (TextUtils.isEmpty(binding.etxtStd2Sub.getText())) {
                setErrorAndFocus(binding.etxtStd2Sub);
            } else if (binding.std2ClassAutoComplete.getText().toString().trim().equals("")) {
                AutoCompleteTextViewError.create(binding.std2ClassTextInputLayout, binding.scrollView);
                Toast.makeText(this, "Please select a class", Toast.LENGTH_SHORT).show();
            } else {
                empty = true;
            }
        } else {
            empty = true;
        }

        if (empty) {
            if (TextUtils.isEmpty(binding.phone.getText())) {
                setErrorAndFocus(binding.phone);
            }else if (binding.phone.getText().toString().trim().length()!=11) {
                Toast.makeText(this, "Invalid Mobile number", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(binding.name.getText())) {
                setErrorAndFocus(binding.name);
            } else if (TextUtils.isEmpty(binding.email.getText())) {
                setErrorAndFocus(binding.email);
            } else if (TextUtils.isEmpty(binding.etxtStd1Sub.getText())) {
                setErrorAndFocus(binding.etxtStd1Sub);
            } else if (binding.std1ClassAutoComplete.getText().toString().trim().equals("")) {
                AutoCompleteTextViewError.create(binding.std1ClassTextInputLayout, binding.scrollView);
                Toast.makeText(this, "Please select a class!", Toast.LENGTH_SHORT).show();
            } else if (binding.cityAutoComplete.getText().toString().trim().equals("")) {
                AutoCompleteTextViewError.create(binding.cityInputLayout, binding.scrollView);
                Toast.makeText(this, "Please select a city!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.etxtAddress.getText())) {
                setErrorAndFocus(binding.etxtAddress);
            } else if (TextUtils.isEmpty(binding.txtChooseTime1.getText())) {
                setErrorAndFocus(binding.txtChooseTime1);
            } else if (TextUtils.isEmpty(binding.txtChooseTime2.getText())) {
                setErrorAndFocus(binding.txtChooseTime2);
            } else if (TextUtils.isEmpty(binding.etxtSalary.getText())) {
                setErrorAndFocus(binding.etxtSalary);
            } else if (builder.toString().length() < 4) {
                setErrorAndFocus(binding.etxtSalary, 0);
            } else if (builder.toString().length() > 5) {
                setErrorAndFocus(binding.etxtSalary, 1);
            } else {
                if (i == 1) {
                    value = 1;
                } else {
                    value = 2;
                }
                putValue(null);
            }
        }
    }


    StringBuilder builder = new StringBuilder();

    public void setErrorAndFocus(EditText editText, int x) {
        if (x == 0) {
            editText.setError("Salary too low");
        } else {
            editText.setError("Salary too high!");
        }
        editText.requestFocus();
    }

    public void setErrorAndFocus(EditText editText) {
        editText.setError("Empty Field");
        editText.requestFocus();
    }


    public void setErrorAndFocus(TextView Textview) {
        Textview.setError("");
    }


    public void putValue(View view) {
        if (value == 2) {
            params.put("numberOfStudents", 2);
            params.put("subject2", binding.etxtStd2Sub.getText().toString().trim());
            params.put("class2", binding.std2ClassAutoComplete.getText().toString().trim());
            params.put("gender2", ChipHelper.create().getCheckedChipTextFromGroup(
                    binding.std2GenderChipGroup, this
            ));

        } else {
            params.put("numberOfStudents", 1);
        }

        params.put("phone", binding.phone.getText().toString().trim());
        params.put("gName", binding.name.getText().toString().trim());
        params.put("email", binding.email.getText().toString().trim());

        params.put("curriculum", ChipHelper.create().getCheckedChipTextFromGroup(
                binding.curriculumChipGroup, this
        ));

        params.put("subject1", binding.etxtStd1Sub.getText().toString().trim());
        params.put("class1", binding.std1ClassAutoComplete.getText().toString().trim());
        params.put("gender1", ChipHelper.create().getCheckedChipTextFromGroup(
                binding.std1GenderChipGroup, this
        ));

        params.put("city", binding.cityAutoComplete.getText().toString().trim());
        params.put("location", binding.locationAutoComplete.getText().toString().trim());
        params.put("address", binding.etxtAddress.getText().toString().trim());
        params.put("daysInWeek", Integer.parseInt(
                ChipHelper.create().getCheckedChipTextFromGroup(
                        binding.daysInWeekChipGroup, this
                )
        ));


        params.put("negotiable", ChipHelper.create().getCheckedChipTextFromGroup(
                binding.negotiableChipGroup, this
                ).equals("Yes")
        );


        params.put("time", binding.txtChooseTime1.getText().toString().trim() + " - " + binding.txtChooseTime2.getText().toString().trim());
        params.put("salary", Integer.parseInt(builder.toString()));
        params.put("teacherGender", ChipHelper.create().getCheckedChipTextFromGroup(
                binding.teacherGenderChipGroup, this
        ));

        params.put("tuitionType", ChipHelper.create().getCheckedChipTextFromGroup(
                binding.tuitionTypeChipGroup, this
        ));

        if(!TextUtils.isEmpty(binding.etxtNote.getText()))
            params.put("note", binding.etxtNote.getText().toString().trim());

        //Dialog
        new AlertDialog.Builder(this)
                //set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
                //set title
                .setTitle("Create Tuition")
                //set message
                .setMessage("Are you sure?")
                //set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        save();
                    }
                })
                //set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                    }
                })
                .show();

    }


    private void save() {

        binding.btnSubmit.setEnabled(false);

        ParseCloud.callFunctionInBackground("jobPost", params, (FunctionCallback<Boolean>) (isCreated, e) -> {
            if (e == null) {
                Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                if (e.getCode() == ParseException.CONNECTION_FAILED) {
                    Toast.makeText(this, "Can't connect to servers", Toast.LENGTH_SHORT).show();
                } else if (e.getMessage().equals("Max limit exceeded")) {
                    Toast.makeText(this, "Max Limit exceeded", Toast.LENGTH_SHORT).show();
                } else if (e.getMessage().equals("banned")) {
                    Toast.makeText(this, "banned", Toast.LENGTH_SHORT).show();
                } else if (e.getMessage().equals("locked")) {
                    Toast.makeText(this, "locked", Toast.LENGTH_SHORT).show();
                } else {
                    ParseErrorHandler.handleParseError(e, this, "jobPost");
                }

                binding.btnSubmit.setEnabled(true);
            }
        });
    }


}
