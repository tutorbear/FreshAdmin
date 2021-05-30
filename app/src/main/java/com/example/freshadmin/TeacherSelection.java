package com.example.freshadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freshadmin.databinding.ActivityTeacherSelectionBinding;
import com.example.freshadmin.utils.ArtLayoutAdapter;
import com.example.freshadmin.utils.ColorEx;
import com.example.freshadmin.utils.ParseErrorHandler;
import com.example.freshadmin.utils.ZoomCenterCardLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.parse.ParseException.CONNECTION_FAILED;
import static com.parse.ParseException.OBJECT_NOT_FOUND;

public class TeacherSelection extends AppCompatActivity {

    List<Object> mixedList;

    ActivityTeacherSelectionBinding binding;
    StudentTeacherApplicationsAdapter customAdapter;
    List<String> idList = new ArrayList<>();
    int count = 0;
    String jobId;
    private HashMap<String, Object> params = new HashMap<>();
    private ArtLayoutAdapter artLayoutAdapter;
    boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        jobId = getIntent().getStringExtra("id");
        setToolbar();
        fetchData();

        String note = getIntent().getStringExtra("note");
        if(note!=null && note.length()!=0){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(note);
            alertDialogBuilder.setPositiveButton("Okay", (dialogInterface, i) -> {
            });
            alertDialogBuilder.show();
        }
    }


    private void setToolbar() {
        binding.recyclerView.setLayoutManager(new ZoomCenterCardLayoutManager(this));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            verticalOffset = verticalOffset * -1;
            float x = (float) (binding.appBarLayout.getTotalScrollRange() / 2.0);
            if (verticalOffset <= x) {
                binding.collapsingToolbarChildContainer.setAlpha((float) (1.0 - (verticalOffset / x)));
            }

        });

        binding.appBarLayout.setExpanded(false, false);

        binding.collapsingToolbar.setExpandedTitleColor(ColorEx.TRANSPARENT);
        binding.extendedFab.setExtended(false);
        binding.extendedFab.hide();
    }

    //Broadcast listener

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(mMessageReceiver, new IntentFilter("teacherAlert"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!loading) {
                binding.frameLayout.setVisibility(View.VISIBLE);
                fetchData();
            }
        }
    };

    private void fetchData() {
        loading = true;

        HashMap<String, String> params = new HashMap<>();
        params.put("id", jobId);
        ParseCloud.callFunctionInBackground("fetchTeachers", params, (FunctionCallback<List<Object>>)
                (objects, e) -> {
                    if (e == null) {
                        if (objects.isEmpty()) {
                            noTutorLayout();
                        } else {
                            if (objects.get(0) instanceof Boolean) {
                                requestDoneLayout();
                                binding.countChip.setText("Teacher Selection Complete");
                            } else {
                                mixedList = objects;
                                display();
                            }
                        }
                    } else {
                        if (e.getCode() == OBJECT_NOT_FOUND) {
                            noJobLayout();
                        } else if (e.getCode() == CONNECTION_FAILED) {
                            noInternetLayout();
                        } else {
                            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    binding.frameLayout.setVisibility(View.GONE);
                    loading = false;
                });
    }


    private void noJobLayout() {
        params.remove("heading");
        params.put("body", "This tuition is no longer available");
        params.remove("button");
        params.put("image", R.drawable.no_post_fail);
        artLayoutAdapter = new ArtLayoutAdapter(params);
        binding.recyclerView.setAdapter(artLayoutAdapter);
    }


    private void noTutorLayout() {
        params.remove("heading");
        params.put("body", "It appears that no tutors have applied to your post yet");
        params.put("button", "Refresh");
        params.put("image", R.drawable.no_post_fail);
        artLayoutAdapter = new ArtLayoutAdapter(params);
        binding.recyclerView.setAdapter(artLayoutAdapter);
    }

    private void noInternetLayout() {
        params.put("heading", "You are offline");
        params.put("body", "Please check your internet connection");
        params.put("button", "Retry");
        params.put("image", R.drawable.no_internet_fail);
        artLayoutAdapter = new ArtLayoutAdapter(params);
        binding.recyclerView.setAdapter(artLayoutAdapter);
    }

    public void change(View view) {
        if (!loading) {
            view.animate().setDuration(300).alpha(0f);
            new Handler().postDelayed(() -> {
                view.setVisibility(View.GONE);
                artLayoutAdapter.getSpinKitView().setVisibility(View.VISIBLE);
                artLayoutAdapter.getSpinKitView().setAlpha(0f);
                artLayoutAdapter.getSpinKitView().animate().alpha(1).setDuration(300);
            }, 300);

            new Handler().postDelayed(this::fetchData, 300);
        }
    }

    private void display() {
        customAdapter = new StudentTeacherApplicationsAdapter(this, mixedList);
        binding.recyclerView.setAdapter(customAdapter);
        binding.recyclerView.setItemViewCacheSize(10);
    }

    BottomSheetDialog dialog;

    public void request(View view) {

        //set loading to true
        if (count == 0) {
            binding.extendedFab.shrink(new ExtendedFloatingActionButton.OnChangedCallback() {
                @Override
                public void onShrunken(ExtendedFloatingActionButton extendedFab) {
                    binding.extendedFab.hide();
                }
            });
        } else {
            if (!loading) {

                View x = getLayoutInflater().inflate(R.layout.bottom_confirm_teacher_dialog, null);
                dialog = new BottomSheetDialog(this);
                dialog.setContentView(x);

                //YES button
                Button yes = x.findViewById(R.id.btn_yes);

                yes.setOnClickListener(view1 -> {
                    dialog.dismiss();
                    loading = true;
                    binding.frameLayout.setVisibility(View.VISIBLE);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("idList", idList);
                    map.put("objId", jobId);

                    ParseCloud.callFunctionInBackground("requestTeachersAdmin", map, (isLocked, e) -> {
                        if (e == null) {
                            requestDoneLayout();
                            toggleSupportActionBar(0);
                            binding.countChip.setText(count + " Teachers Selected");

                            int pos = getIntent().getIntExtra("pos",-1);
                            setResult(RESULT_OK,new Intent().putExtra("pos",pos));
                            finish();
                        } else {
                            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            loading = false;
                        }
                        binding.frameLayout.setVisibility(View.GONE);
                    });
                });

                //No button
                Button no = x.findViewById(R.id.btn_cancel);
                no.setOnClickListener(view12 -> dialog.dismiss());


                if (count > 1)
                    ((TextView) x.findViewById(R.id.header)).setText(count + " Teachers Selected");
                else
                    ((TextView) x.findViewById(R.id.header)).setText(count + " Teacher Selected");

                ((TextView) x.findViewById(R.id.sub_header)).setText("Request them for an interview?");

                dialog.show();

            } else {
                Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void requestDoneLayout() {
        params.remove("heading");
        params.put("body", "Your Tutor selection for this post is complete");
        params.remove("button");
        params.put("image", R.drawable.selected_teachers_fail);
        artLayoutAdapter = new ArtLayoutAdapter(params);
        binding.recyclerView.setAdapter(artLayoutAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (count != 0) {
                customAdapter.fuckYou();
                idList.clear();
                count = 0;
                toggleSupportActionBar(count);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void toggleSupportActionBar(int count) {
        if (count == 0) {
            binding.collapsingToolbar.setTitle("Applied Teachers");
            getSupportActionBar().setHomeAsUpIndicator(null);
            binding.extendedFab.shrink(new ExtendedFloatingActionButton.OnChangedCallback() {
                @Override
                public void onShrunken(ExtendedFloatingActionButton extendedFab) {
                    binding.extendedFab.hide();
                }
            });
        } else {
            binding.collapsingToolbar.setTitle(count + "/3" + " Selected");

            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_clear_24);

            binding.extendedFab.show(new ExtendedFloatingActionButton.OnChangedCallback() {
                @Override
                public void onShown(ExtendedFloatingActionButton extendedFab) {
                    binding.extendedFab.extend();
                }
            });
        }
        if (count == 1) {
            binding.countChip.setText(count + " Teacher Selected");
        } else
            binding.countChip.setText(count + " Teachers Selected");
    }

    public void chipClicked(View view, String id) {
        Chip chip = (Chip) view;
        if (chip.isChecked()) {
            count++;
            if (count > 3) {
                count--;
                Toast.makeText(this, "Teacher selection limit reached", Toast.LENGTH_SHORT).show();
                chip.setChecked(false);
            } else {
                idList.add(id);
            }

        } else {
            for (int j = 0; j < idList.size(); j++) {
                if (idList.get(j).equals(id)) {
                    idList.remove(j);
                    break;
                }
            }
            count--;
        }

        toggleSupportActionBar(count);

    }
}