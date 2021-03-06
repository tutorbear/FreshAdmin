package com.example.freshadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FunctionCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ProgressCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class VerT extends AppCompatActivity implements View.OnTouchListener {
    ParseObject obj;
    TextView name, username, gender, currentEdu, curriculum, background, schoolName, uniName, uniProgram, address, phone;
    ImageView proPic, nId, certificate, idCard;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_t);
        init();
        set();
        fetchPic();
    }

    byte[] cer;
    byte[] nid;
    byte[] pic;
    byte[] uni;

    private void fetchPic() {
        progress.bringToFront();
        progress.setVisibility(View.VISIBLE);
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", obj.getObjectId());
        ParseCloud.callFunctionInBackground("fetchPhotoT", params, new FunctionCallback<ArrayList<String>>() {
            @Override
            public void done(ArrayList base64Array, ParseException e) {
                if (e == null) {
                    cer = Base64.decode(base64Array.get(0).toString(), Base64.DEFAULT);
                    nid = Base64.decode(base64Array.get(1).toString(), Base64.DEFAULT);
                    pic = Base64.decode(base64Array.get(2).toString(), Base64.DEFAULT);
                    Glide.with(VerT.this).load(cer).into(certificate);
                    Glide.with(VerT.this).load(nid).into(nId);
                    Glide.with(VerT.this).load(pic).into(proPic);
                    if (base64Array.size() == 4) {
                        byte[] uni = Base64.decode(base64Array.get(3).toString(), Base64.DEFAULT);
                        Glide.with(VerT.this).load(uni).into(idCard);
                    }

                } else {
                    Toast.makeText(VerT.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progress.setVisibility(View.GONE);
            }
        });
    }


    private void init() {
        obj = getIntent().getParcelableExtra("obj");
        //TextViews
        name = findViewById(R.id.nameTP);
        username = findViewById(R.id.usernameTP);
        gender = findViewById(R.id.genderTP);
        currentEdu = findViewById(R.id.cEduTP);
        curriculum = findViewById(R.id.curriculumTP);
        background = findViewById(R.id.backgroundTP);
        schoolName = findViewById(R.id.schoolNameTP);
        uniName = findViewById(R.id.uniNameTP);
        uniProgram = findViewById(R.id.uniProgramTP);
        address = findViewById(R.id.addressTP);
        phone = findViewById(R.id.phoneTP);
        //Image Vies
        proPic = findViewById(R.id.proPicTP);
        nId = findViewById(R.id.nIdTP);
        certificate = findViewById(R.id.certificateTP);
        idCard = findViewById(R.id.idCardTP);

        //progress bar
        progress = findViewById(R.id.pb_verT);

        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }

    private void set() {
        // Text Views
        name.setText("full Name: " + obj.getString("fullName"));
        username.setText("username: " + obj.getString("username"));
        gender.setText("gender: " + obj.getString("gender"));
        currentEdu.setText("currentEducation: " + obj.getString("currentEducation"));
        curriculum.setText("curriculum: " + obj.getString("curriculum"));
        background.setText("background: " + obj.getString("background"));
        schoolName.setText("schoolName: " + obj.getString("school"));
        address.setText("address: " + obj.getString("address"));
        phone.setText(obj.getString("phone"));
        //Images


        if (obj.getString("university") != null) {
            uniName.setText("university: " + obj.getString("university"));
            uniProgram.setText("uniProgram: " + obj.getString("uniProgram"));
        } else {
            uniName.setVisibility(View.GONE);
            uniProgram.setVisibility(View.GONE);
            idCard.setVisibility(View.GONE);
        }
    }

    public void verifyT(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("id", obj.getObjectId());
                params.put("profileClass", "TeacherProfile");
                ParseCloud.callFunctionInBackground("verifyUser", params, new FunctionCallback<Boolean>() {
                    @Override
                    public void done(Boolean bool, ParseException e) {
                        if (e == null) {
                            int pos = getIntent().getIntExtra("pos", -1);
                            setResult(RESULT_OK, new Intent().putExtra("pos", pos));
                            finish();
                        } else {
                            Toast.makeText(VerT.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void callT(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + obj.getString("username")));
        startActivity(intent);
    }

    public void rejectT(View view) {
        final String reason = ((EditText) findViewById(R.id.reason)).getText().toString();
        if (reason.equals("")) {
            Toast.makeText(this, "Write a reason first", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are You Sure ?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("id", obj.getObjectId());
                    params.put("profileClass", "TeacherProfile");
                    params.put("comment", reason);
                    ParseCloud.callFunctionInBackground("verificationFailed", params, new FunctionCallback<Boolean>() {
                        @Override
                        public void done(Boolean bool, ParseException e) {
                            if (e == null) {
                                int pos = getIntent().getIntExtra("pos", -1);
                                setResult(RESULT_OK, new Intent().putExtra("pos", pos));
                                finish();
                            } else {
                                Toast.makeText(VerT.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
    }

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator currentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int shortAnimationDuration;

    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    String savedItemClicked;

    @Override
    public void onBackPressed() {
        ImageView imageView = findViewById(R.id.hiddenView);
        if (imageView.getVisibility() != View.VISIBLE) {
            super.onBackPressed();
        } else {
            imageView.setVisibility(View.GONE);
        }


    }


    public void expand(View view) {
        zoomImageFromThumb(view);

        ImageView imageView = findViewById(R.id.hiddenView);

        imageView.setOnTouchListener(this);
    }

    private void zoomImageFromThumb(final View thumbView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.hiddenView);
        expandedImageView.setImageDrawable(((ImageView) thumbView).getDrawable());


        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub

        ImageView view = (ImageView) v;
        dumpEvent(event);

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix);
        return true;
    }

    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt((x * x + y * y));
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}
