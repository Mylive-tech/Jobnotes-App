package com.jobnotes.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jobnotes.Adapter.NotesAdapter;
import com.jobnotes.DBHolder;
import com.jobnotes.HomeActivity;
import com.jobnotes.MyGalleryActivity;
import com.jobnotes.Pojo.Notes;
import com.jobnotes.Pojo.Property;
import com.jobnotes.Pojo.ReportInfo;
import com.jobnotes.Pojo.Reports;
import com.jobnotes.R;
import com.jobnotes.ReportForms;
import com.jobnotes.ReportListActivity;
import com.jobnotes.Utility.CustomHttpClient;
import com.jobnotes.Utility.Fonts;
import com.jobnotes.Utility.FragmentEntry;
import com.jobnotes.Utility.ImageLoadingUtils;
import com.jobnotes.Utility.Message;
import com.jobnotes.Utility.MultipartUtility;
import com.jobnotes.Utility.NetworkConnection;
import com.jobnotes.Utility.SPDomain;
import com.jobnotes.Utility.SPUser;
import com.jobnotes.Utility.WebUrl;
import com.jobnotes.imageloader1.imageloader.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A placeholder fragment containing a simple view.
 */
public class PropertyViewDetaillFragment extends Fragment {

    public static final int MEDIA_TYPE_IMAGE = 2;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "JobNotes";
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public String serverImageName;
    ArrayList<Property> alProperty;
    Context context;
    NetworkConnection networkconnection;
    ImageLoader imageLoader;
    private Uri fileUri;
    private String selectedImagePath;
    private LayoutInflater inflater;
    private Activity activity;
    private ListView lvProperty;
    private ProgressDialog progressDialog;
    private Property property;
    private AQuery aq;
    private TextView tvPriortyTitle;
    private TextView tvAddressTitle;
    private TextView tvAddress;
    private TextView tvPhoneTitle;
    private TextView tvPhone;
    private TextView tvGpsTitle;
    private TextView tvGps;
    private TextView tvSpecialNotesDescriptionTitle;
    private TextView tvSpecialNotesDescription;
    private ImageView imPropertyMap;
    private Typeface boldFonts;
    private Typeface mediumFonts;
    private Typeface regularFonts;
    private TextView tvStautsDetails;
    private TextView tvPropertyNameTitle;
    private TextView tvPropertyName;
    private TextView tvStatusProgress;
    private TextView tvStatusCompleted;
    private String status = "";
    private LinearLayout llHorizontalScrollContainer;
    private TextView tvTakeShot;
    private DBHolder dbHolder;
    private TextView tvAddNotes;
    private LinearLayout llGps;
    private ArrayList<String> alImages;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    private LinearLayout llAddress;
    private String[] arrUserGallery;
    private TextView tvPropertyReport;
    private TextView tvPlowingStatus;
    private ArrayList<Notes> alNotes;
    private Cursor cursorNotes;
    private Calendar c;
    private SimpleDateFormat df;
    private LinearLayout llList;
    private ArrayList<ReportInfo> alReportInfo;
    private ArrayList<Reports> alReports;
    private ImageLoadingUtils utils;

    public PropertyViewDetaillFragment() {


    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {
        Log.e("before directory", "yes");
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            Log.e("storage directory", "yes");
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        Log.e("Create file", "yes");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        Log.e("mediaFile", mediaFile + "");
        return mediaFile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_veiw_detail,
                container, false);
        activity = getActivity();

        this.inflater = inflater;
        imageLoader = new ImageLoader(activity);
        WebUrl.current_frag="veiw_detail";
        initComponents(view);
        initListeners();
        view.setClickable(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        llList.removeAllViews();
        setDataSideMenu(SPUser.getString(activity, SPUser.REPORTS));


        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


        new CustomHttpClient(activity).executeHttp(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.DISPLAY_NOTES + "&pid=" + property.getId(), params, null, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {
                    setNotesOnSucessInList(result);
                    updateData(1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null, CustomHttpClient.Method.GET, true);
     //   Toast.makeText(activity,"On Resume Fragment Detail",Toast.LENGTH_LONG).show();
    }

    private void initComponents(View view) {
        try {
            utils = new ImageLoadingUtils(activity);
            dbHolder = new DBHolder(activity, activity);

            Bundle bundle = this.getArguments();
            networkconnection = new NetworkConnection();
            property = (Property) bundle.getSerializable("property");
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading ...");
            progressDialog.setCancelable(false);
            boldFonts = Fonts.getBoldFont(activity);
            mediumFonts = Fonts.getMediumFont(activity);
            regularFonts = Fonts.getRegularFont(activity);

            aq = new AQuery(view);
            tvPriortyTitle = (TextView) view.findViewById(R.id.tv_view_property_title);
            tvAddressTitle = (TextView) view.findViewById(R.id.tv_view_property_address_title);
            tvAddress = (TextView) view.findViewById(R.id.tv_view_property_address);
            tvPhoneTitle = (TextView) view.findViewById(R.id.tv_view_property_phone_number_title);
            tvPhone = (TextView) view.findViewById(R.id.tv_view_property_phone_number);
            tvGpsTitle = (TextView) view.findViewById(R.id.tv_view_property_gps_title);
            tvGps = (TextView) view.findViewById(R.id.tv_view_property_gps);
            tvPropertyNameTitle = (TextView) view.findViewById(R.id.tv_view_property_property_name_title);
            tvPropertyName = (TextView) view.findViewById(R.id.tv_view_property_property_name);
            tvSpecialNotesDescriptionTitle = (TextView) view.findViewById(R.id.tv_view_property_special_notes_blue);
            tvSpecialNotesDescription = (TextView) view.findViewById(R.id.tv_view_property_special_notes_description);
            imPropertyMap = (ImageView) view.findViewById(R.id.im_view_property_map);
            tvStautsDetails = (TextView) view.findViewById(R.id.tv_view_property_work_completed_detail);
            tvStatusProgress = (TextView) view.findViewById(R.id.tv_view_property_work_status);
            tvStatusCompleted = (TextView) view.findViewById(R.id.tv_view_property_work_completed);
            llHorizontalScrollContainer = (LinearLayout) view.findViewById(R.id.ll_horizontal_scroll_container);
            tvTakeShot = (TextView) view.findViewById(R.id.tv_view_property_take_shot);
            tvPlowingStatus = (TextView) view.findViewById(R.id.tv_view_property_plowing_status);

            tvAddNotes = (TextView) view.findViewById(R.id.tv_view_property_add_note);
            llGps = (LinearLayout) view.findViewById(R.id.ll_view_property_gps);
            llAddress = (LinearLayout) view.findViewById(R.id.ll_view_property_address);
            tvPropertyReport = (TextView) view.findViewById(R.id.tv_view_property_reports);
            llList = (LinearLayout) view.findViewById(R.id.ll_report_list);
            inflater = activity.getLayoutInflater();

            tvAddNotes.setTypeface(boldFonts);

            tvPriortyTitle.setTypeface(mediumFonts);
            tvAddressTitle.setTypeface(boldFonts);
            tvAddress.setTypeface(mediumFonts);
            tvPhoneTitle.setTypeface(boldFonts);
            tvPhone.setTypeface(mediumFonts);
            tvGpsTitle.setTypeface(boldFonts);
            tvGps.setTypeface(mediumFonts);
            tvPropertyNameTitle.setTypeface(boldFonts);
            tvPropertyName.setTypeface(mediumFonts);
            tvSpecialNotesDescriptionTitle.setTypeface(mediumFonts);
            tvPlowingStatus.setTypeface(mediumFonts);
            tvSpecialNotesDescription.setTypeface(mediumFonts);
            tvStautsDetails.setTypeface(mediumFonts);
            tvStatusProgress.setTypeface(boldFonts);
            tvStatusCompleted.setTypeface(boldFonts);

            tvPriortyTitle.setText(property.getJob_listing());
            tvAddress.setText(property.getLocation_address());
            tvPhone.setText(property.getOnsite_contact_person()+"\n"+property.getPhn_no() + "");
            tvGps.setText(property.getLat() + "\n" + property.getLag());
            tvPropertyName.setText(property.getJob_listing());
            setDataSideMenu(SPUser.getString(activity, SPUser.REPORTS));

            c = Calendar.getInstance();
            System.out.println("Current time => "+c.getTime());

            //  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

            String formattedDate = df.format(c.getTime());
           // WebUrl.ShowLog("formattedDate=="+formattedDate);

            setNotes();


            tvStautsDetails.setText(property.getCompletion_date());

           // aq.id(imPropertyMap).progress(R.id.view_property_progressBar).image(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.IMAGEPATH + property.getMap_widget(), true, true);

            Glide
                    .with(activity)
                    .load(Uri.parse(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.IMAGEPATH + property.getMap_widget()))
                    .into(imPropertyMap);

            imPropertyMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> alTmp = new ArrayList<String>();
                    alTmp.add(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.IMAGEPATH + property.getMap_widget()+"");
                 //   startActivity(new Intent(activity, MyGalleryActivity.class).putStringArrayListExtra("image", alTmp));
                    startActivity(new Intent(activity, MyGalleryActivity.class).putExtra("image", alTmp));

                }
            });
       setProgress();

       if(property.getUser_gallery().length()==0){
                    llHorizontalScrollContainer.setVisibility(View.GONE);
                }
                else{
                    llHorizontalScrollContainer.setVisibility(View.VISIBLE);
                }


            arrUserGallery = property.getUser_gallery().split(",");
            alImages = new ArrayList<String>();
            for (int i = 0; i < arrUserGallery.length; i++) {
                alImages.add(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.IMAGEPATH + arrUserGallery[i]);
                DisplayMetrics dm = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

                RelativeLayout rl = new RelativeLayout(activity);
                RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
                // rlp.setMargins(5, 5, 5, 5);
                rl.setLayoutParams(rlp);
                rl.setPadding(5, 5, 5, 5);


                RelativeLayout.LayoutParams plp = new RelativeLayout.LayoutParams(dm.widthPixels / 12, dm.widthPixels / 12);
                plp.addRule(RelativeLayout.CENTER_IN_PARENT);
                //  plp.setMargins(5, 5, 5, 5);

                final ProgressBar progressBar = new ProgressBar(activity);
                progressBar.setLayoutParams(plp);
                rl.addView(progressBar);

                RelativeLayout.LayoutParams vp = new RelativeLayout.LayoutParams(dm.widthPixels / 4, dm.widthPixels / 4);
                 vp.addRule(RelativeLayout.CENTER_IN_PARENT);
                // vp.setMargins(5, 5, 5, 5);
                final SubsamplingScaleImageView image = new SubsamplingScaleImageView(activity);
                image.setLayoutParams(vp);
                int gj = SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP;
                image.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);

                 //image.set(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                // imageLoader.DisplayImage(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.IMAGEPATH + arrUserGallery[i], R.mipmap.blank, image);
                Glide.with(activity)
                        .load(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.IMAGEPATH + arrUserGallery[i])
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                //  progressBar.setVisibility(View.GONE);
                                image.setImage(ImageSource.bitmap(bitmap));
                                image.setZoomEnabled(false);

                            }
                        });
                image.setTag(i+"");
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //   startActivity(new Intent(activity, MyGalleryActivity.class).putStringArrayListExtra("image", alImages).putExtra("index",(String)v.getTag()));
                        startActivity(new Intent(activity, MyGalleryActivity.class).putExtra("image", alImages).putExtra("index",(String)v.getTag()));
                    }
                });
                rl.addView(image);
                llHorizontalScrollContainer.addView(rl);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setNotes() {
        try {
            alNotes = new ArrayList<>();
            alNotes.addAll(property.getNotes());
            //    alNotes=property.getNotes();


        //     alNotes=property.getNotes();
        int n =alNotes.size();
        for (int i = 0; i < n; i++) {
            if (alNotes.get(i).getNote().toString().equalsIgnoreCase("none")||alNotes.get(i).getNote().toString().equalsIgnoreCase("null")||alNotes.get(i).getNote().toString().equalsIgnoreCase("")||alNotes.get(i).getNote().toString()==null||alNotes.get(i).getNote().toString().equalsIgnoreCase(" ")) {
                alNotes.remove(i);
                i--;
                --n;
            }
        }

         /*append data in list from local DB below*/
        cursorNotes = dbHolder.GetPropertyNotes(property.getId());
        if (cursorNotes.getCount() > 0) {
            while (cursorNotes.moveToNext()) {
                WebUrl.ShowLog("cursorNotes=="+cursorNotes.getString(1)+"==="+cursorNotes.getString(3));
                Notes notes = new Notes();
                notes.setname("");
                notes.setDate(df.format(c.getTime()));
                notes.setNote(cursorNotes.getString(3));
                alNotes.add(notes);

            }
            cursorNotes.close();
        }

        if (alNotes.size()==0){
            tvSpecialNotesDescription.setText("No notes are available");
        }
        else if (alNotes.size()>1) {
            String notesShown = alNotes.get(0).getNote();
            tvSpecialNotesDescription.setText(Html.fromHtml(notesShown));
            if (alNotes.get(0).getNote().equalsIgnoreCase("none")||alNotes.get(0).getNote().toString().equalsIgnoreCase("null")||alNotes.get(0).getNote().toString().equalsIgnoreCase(""))
            {
                tvSpecialNotesDescription.setText(alNotes.get(1).getNote());
            }
        }
        else {
            tvSpecialNotesDescription.setText("No notes are available");
        }
        if (alNotes.size()==0){
            tvSpecialNotesDescription.setText("No notes are available");
        }else {
            String notesShown = alNotes.get(0).getNote() + "<br>" + "<b> Posted on : </b> " + alNotes.get(0).getDate() + "</br> <br>" + "<b> Posted by : </b> " + alNotes.get(0).getname() + "<br/>";
            tvSpecialNotesDescription.setText(Html.fromHtml(notesShown));
        }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

  /*  if you get starting_date null than button should show start
    [1:51:49 PM] Kundan Singh Rathore: if starting_date is there and pausing_date is null than show pause and complete button
    [1:52:28 PM] Kundan Singh Rathore: if staring_date is there, pausing _date is there than you need to show start(unpause)
            [1:52:42 PM] Kundan Singh Rathore: if closing_date is there than need to show restart*/


    private void setProgress() {
        if (property.getProgress() == 0) {
        // tvStatusProgress.setText("Start Job");
        // tvStatusProgress.setText(Html.fromHtml("<html><body><font size=5 color=red>Hello </font> <br><font size=3 color=blue> (World)</font></br></body><html>"));
        SpannableString span1 = new SpannableString("Start Job");

        span1.setSpan(new RelativeSizeSpan(1.0f),  0, span1.length(),0);


        tvStatusProgress.setText(TextUtils.concat(span1));
        //   WebUrl.ShowLog(tvStatusProgress.getText().toString());
        tvStatusCompleted.setVisibility(View.GONE);
        tvStautsDetails.setText("");
    } else if (property.getProgress() == 1) {
        //  tvStatusProgress.setText("Stop Job (Pause)");

          //  if (SPUser.getString(activity, SPUser.USER_ID).equalsIgnoreCase(property.getStarted_by()+"")||SPUser.getString(activity, SPUser.USER_ID).equalsIgnoreCase(property.getClosed_by()+"")){


        SpannableString span1 = new SpannableString("Stop Job");
        SpannableString span2 = new SpannableString("(Pause)");

        span1.setSpan(new RelativeSizeSpan(1.0f),  0, span1.length(),0);
        span2.setSpan(new RelativeSizeSpan(0.75f),  0, span2.length(), 0);

        tvStatusProgress.setText(TextUtils.concat(span1,"\n" ,span2));
        tvStatusCompleted.setText("Complete Job");
        tvStatusCompleted.setVisibility(View.VISIBLE);
        tvStautsDetails.setText("Start Date:\t" + property.getStart_date());
           /* }else {
                SpannableString span1 = new SpannableString("Start Job");

                span1.setSpan(new RelativeSizeSpan(1.0f),  0, span1.length(),0);


                tvStatusProgress.setText(TextUtils.concat(span1));
                //   WebUrl.ShowLog(tvStatusProgress.getText().toString());
                tvStatusCompleted.setVisibility(View.GONE);
                tvStautsDetails.setText("");
            }*/

        }
        else if (property.getProgress() == 2)
        {
           /* if (SPUser.getString(activity, SPUser.USER_ID).equalsIgnoreCase(property.getStarted_by()+"")&&SPUser.getString(activity, SPUser.USER_ID).equalsIgnoreCase(property.getClosed_by()+""))
            {*/
               // if (SPUser.getString(activity, SPUser.USER_ID).equalsIgnoreCase(property.getStarted_by() + "") && SPUser.getString(activity, SPUser.USER_ID).equalsIgnoreCase(property.getClosed_by() + "")) {
                    tvStatusProgress.setText("Restart Job");

                    tvStatusCompleted.setVisibility(View.GONE);
                    tvStautsDetails.setText("Completion Date:\t" + property.getCompletion_date());
       //       }
            /*}
            else {
                SpannableString span1 = new SpannableString("Start Job");

                span1.setSpan(new RelativeSizeSpan(1.0f),  0, span1.length(),0);


                tvStatusProgress.setText(TextUtils.concat(span1));
                //   WebUrl.ShowLog(tvStatusProgress.getText().toString());
                tvStatusCompleted.setVisibility(View.GONE);
                tvStautsDetails.setText("");
            }*/
    } else if (property.getProgress() == 3) {
        //  tvStatusProgress.setText("Start Job (Unpause)");
          /*  if (SPUser.getString(activity, SPUser.USER_ID).equalsIgnoreCase(property.getStarted_by()+"")||SPUser.getString(activity, SPUser.USER_ID).equalsIgnoreCase(property.getClosed_by()+""))
            {*/
                SpannableString span1 = new SpannableString("Start Job");
                SpannableString span2 = new SpannableString("(Unpause)");

                span1.setSpan(new RelativeSizeSpan(1.0f), 0, span1.length(), 0);
                span2.setSpan(new RelativeSizeSpan(0.75f), 0, span2.length(), 0);
                tvStatusProgress.setText(TextUtils.concat(span1, "\n", span2));

                tvStatusCompleted.setVisibility(View.GONE);
                tvStautsDetails.setText("Pause Date:\t" + property.getPause_date());
           /* }
            else {
                SpannableString span1 = new SpannableString("Start Job");

                span1.setSpan(new RelativeSizeSpan(1.0f),  0, span1.length(),0);


                tvStatusProgress.setText(TextUtils.concat(span1));
                //   WebUrl.ShowLog(tvStatusProgress.getText().toString());
                tvStatusCompleted.setVisibility(View.GONE);
                tvStautsDetails.setText("");
            }*/
    }
    }






    private void initListeners() {

        tvStatusProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvStatusProgress.getText().toString().equalsIgnoreCase("Start Job") || tvStatusProgress.getText().toString().equalsIgnoreCase("Restart Job")) {
                    //tvStatusProgress.setText("Stop Job (Pause)");
                    SpannableString span1 = new SpannableString("Stop Job");
                    SpannableString span2 = new SpannableString("(Pause)");

                    span1.setSpan(new RelativeSizeSpan(1.0f),  0, span1.length(),0);
                    span2.setSpan(new RelativeSizeSpan(0.75f),  0, span2.length(), 0);
                    tvStatusProgress.setText(TextUtils.concat(span1,"\n" ,span2));
                    tvStatusCompleted.setText("Complete Job");
                    tvStatusCompleted.setVisibility(View.VISIBLE);

                    status = "1"; //start
                } else if (tvStatusProgress.getText().toString().equalsIgnoreCase("Stop Job\n(Pause)")) {
                  //  tvStatusProgress.setText("Start Job (Unpause)");

                    SpannableString span1 = new SpannableString("Start Job");
                    SpannableString span2 = new SpannableString("(Unpause)");

                    span1.setSpan(new RelativeSizeSpan(1.0f),  0, span1.length(),0);
                    span2.setSpan(new RelativeSizeSpan(0.75f),  0, span2.length(), 0);
                    tvStatusProgress.setText(TextUtils.concat(span1,"\n" ,span2));
                    tvStatusCompleted.setVisibility(View.GONE);
                    status = "3";  //pause
                } else if (tvStatusProgress.getText().toString().equalsIgnoreCase("Start Job\n(Unpause)")) {
                   // tvStatusProgress.setText("Stop Job (Pause)");
                    SpannableString span1 = new SpannableString("Stop Job");
                    SpannableString span2 = new SpannableString("(Pause)");

                    span1.setSpan(new RelativeSizeSpan(1.0f),  0, span1.length(),0);
                    span2.setSpan(new RelativeSizeSpan(0.75f),  0, span2.length(), 0);
                    tvStatusProgress.setText(TextUtils.concat(span1,"\n" ,span2));
                    tvStatusCompleted.setText("Complete Job");
                    tvStatusCompleted.setVisibility(View.VISIBLE);
                    status = "1";
                } else if (tvStatusProgress.getText().toString().equalsIgnoreCase("Complete Job")) {
                    tvStatusProgress.setText("Restart Job");
                    tvStatusCompleted.setVisibility(View.GONE);
                    status = "1";
                }
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

                new CustomHttpClient(activity).executeHttp(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.UPDATE_STATUS + "&status=" + status + "&pid=" + property.getId() + "&uid=" + SPUser.getString(activity, SPUser.USER_ID) + "", params, progressDialog, new CustomHttpClient.OnSuccess() {
                    @Override
                    public void onSucess(String result) {
                        try {
                            //((HomeActivity) activity).refreshList(0);
                            updateData(1);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new CustomHttpClient.OnFailure() {
                    @Override
                    public void onFailure(String result) {
                        dbHolder.addWorkStatus(property.getId() + "", SPUser.getString(activity, SPUser.USER_ID) + "", status + "");
                    }
                }, CustomHttpClient.Method.GET, true);
            }

        });
        tvStatusCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "2";    //Complete
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
                try {
                    new CustomHttpClient(activity).executeHttp(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.UPDATE_STATUS + "&status=" + status + "&pid=" + property.getId() + "&uid=" + SPUser.getString(activity, SPUser.USER_ID) + "" + "&date=" + URLEncoder.encode(timeStamp + "", "UTF-8"), params, progressDialog, new CustomHttpClient.OnSuccess() {
                        @Override
                        public void onSucess(String result) {
                            try {

                                //((HomeActivity) activity).refreshList(0);
                                updateData(1);
                                tvStatusProgress.setText("Restart Job");
                                tvStatusCompleted.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new CustomHttpClient.OnFailure() {
                        @Override
                        public void onFailure(String result) {
                            dbHolder.addWorkStatus(property.getId() + "", SPUser.getString(activity, SPUser.USER_ID) + "", status + "");
                            tvStatusProgress.setText("Restart Job");
                            tvStatusCompleted.setVisibility(View.GONE);

                        }
                    }, CustomHttpClient.Method.GET, true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


        tvTakeShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  CharSequence colors[] = new CharSequence[]{"Gallery",
                        "Camera"};

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Upload photo from");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                selectImage();
                                break;
                            case 1:
                                captureImage();
                                break;

                            default:
                                break;
                        }
                    }
                });
                builder.show();*/
                captureImage();
            }
        });


        tvAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.add_notes_dialog);

                TextView tvDialogTitle = (TextView) dialog.findViewById(R.id.tv_add_notes_dialog_title);
                final EditText etNotes = (EditText) dialog.findViewById(R.id.et_add_notes_dialog);
                TextView tvSubmit = (TextView) dialog.findViewById(R.id.tv_add_notes_dialog_submit);
                tvDialogTitle.setTypeface(Fonts.getMediumFont(activity));
                etNotes.setTypeface(Fonts.getMediumFont(activity));
                tvSubmit.setTypeface(Fonts.getBoldFont(activity));

                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etNotes.getText().length() != 0) {
                            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


                            try {
                                new CustomHttpClient(activity).executeHttp(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.ADD_NOTES + "&pid=" + property.getId() + "&uid=" + SPUser.getString(activity, SPUser.USER_ID) + "&note=" + URLEncoder.encode(etNotes.getText().toString(), "UTF-8"), params, progressDialog, new CustomHttpClient.OnSuccess() {
                                    @Override
                                    public void onSucess(String result) {
   /* try {
                                            setNotesOnSucessInList(result);
                                            updateData(1);


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }*/
                            try {
                                        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


                                        new CustomHttpClient(activity).executeHttp(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.DISPLAY_NOTES + "&pid=" + property.getId(), params, progressDialog, new CustomHttpClient.OnSuccess() {
                                            @Override
                                            public void onSucess(String result) {
                                                try {
                                                    setNotesOnSucessInList(result);
                                                    updateData(1);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, null, CustomHttpClient.Method.GET, true);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    }
                                }, new CustomHttpClient.OnFailure() {
                                    @Override
                                    public void onFailure(String result) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            if (jsonObject.optString("message").equals("Please Provide Internet.")) {
                                                dbHolder.addNotes(property.getId() + "", SPUser.getString(activity, SPUser.USER_ID) + "", etNotes.getText().toString());
                                                Toast.makeText(activity, "Notes Saved to DataBase", Toast.LENGTH_LONG).show();
                                                setNotes();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, CustomHttpClient.Method.GET, true);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();

                        } else {
                            Toast.makeText(activity, "Notes is required", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.show();
            }
        });
        llGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUri = "http://maps.google.com/maps?q=loc:" + property.getLat() + "," + property.getLag() + " (" + property.getLocation_name() + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                activity.startActivity(intent);
            }
        });

        tvSpecialNotesDescriptionTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (property.getNotes().size()!=0) {
                    property.setHide(true);
                    if (property.getNotes().size()!=0) {
                        property.setHide(true);
                        if (!(tvSpecialNotesDescription.getText().toString().equalsIgnoreCase("none")||tvSpecialNotesDescription.getText().toString().equalsIgnoreCase("No notes are available")||tvSpecialNotesDescription.getText().toString().equalsIgnoreCase(""))) {
                        //    WebUrl.ShowLog(tvSpecialNotesDescription.getText().toString());
                            ((HomeActivity) activity).changeFragment(FragmentEntry.SPECIAL_NOTES, property);
                        }else {
                            Message.show(activity,"No notes are available");
                        }

                    }
                }
            }
        });
        tvSpecialNotesDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (property.getNotes().size()!=0) {
                    property.setHide(true);
                    if (!(tvSpecialNotesDescription.getText().toString().equalsIgnoreCase("none")||tvSpecialNotesDescription.getText().toString().equalsIgnoreCase("No notes are available")||tvSpecialNotesDescription.getText().toString().equalsIgnoreCase(""))) {
                      //  WebUrl.ShowLog(tvSpecialNotesDescription.getText().toString());
                        ((HomeActivity) activity).changeFragment(FragmentEntry.SPECIAL_NOTES, property);
                    }else {
                        Message.show(activity,"No notes are available");
                    }

                }
            }
        });
        tvPropertyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, ReportListActivity.class).putExtra("property", property));

            }
        });

    }

    /*Set data after Adding new notes (ONLINE) onSucess*/
    private void setNotesOnSucessInList(String result) {


        try {

            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getBoolean("status")){


                if (jsonObject.optJSONArray("result").length() > 0&&jsonObject.optJSONArray("result")!=null&&jsonObject.optJSONArray("result").toString().equalsIgnoreCase("null")) {

                    alNotes.clear();
                    alNotes=new ArrayList<Notes>();
                    JSONArray jsonArray=jsonObject.optJSONArray("result");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.optJSONObject(i);
                        Notes notes=new Notes();
                        notes.setDate(jsonObject1.optString("date"));
                        notes.setname(jsonObject1.optString("staff_name"));
                        notes.setNote(jsonObject1.optString("note"));
                        alNotes.add(notes);
                    }
                    //  alNotes = new Gson().fromJson(jsonObject.optJSONArray("result").toString(), new TypeToken<List<Notes>>() {}.getType());
 /*append data in list from local DB below*/
                    cursorNotes = dbHolder.GetPropertyNotes(property.getId());
                    if (cursorNotes.getCount() > 0) {
                        while (cursorNotes.moveToNext()) {
                            WebUrl.ShowLog("cursorNotes=="+cursorNotes.getString(1)+"==="+cursorNotes.getString(3));
                            Notes notes = new Notes();
                            notes.setname("");
                            notes.setDate(df.format(c.getTime()));
                            notes.setNote(cursorNotes.getString(3));
                            alNotes.add(notes);

                        }
                        cursorNotes.close();
                    }
                   // setNotes();
                    updateInLocal();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void updateInLocal() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("uname", SPUser.getString(activity, SPUser.USER_NAME)));
        params.add(new BasicNameValuePair("password", SPUser.getString(activity, SPUser.USER_PASSWORD)));

                 /*   params.add(new BasicNameValuePair("device_id", SPDevice.getValue(activity, SPDevice.DEVICE_ID)));
                    params.add(new BasicNameValuePair("device_token", SPGCM.getValue(activity, SPGCM.GCM_TOKEN) + "123456"));*/
        new CustomHttpClient(activity).executeHttp(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.LOGIN, params, null, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {

                    JSONObject jObject = new JSONObject(result);
                    JSONObject jsonObject = jObject.optJSONObject("login_info");
                    if (jsonObject.optString("auto_login").equalsIgnoreCase("1")) {
                        SPUser.setString(activity, SPUser.USER_ID, jsonObject.optString("id"));
                    }
                    SPUser.setString(activity, SPUser.PROPERTIES, jObject.optJSONObject("properites") + "");
                    SPUser.setString(activity, SPUser.REPORTS, jObject.optJSONObject("reports") + "");
                    SPUser.setString(activity, SPUser.REPORT_INFO, jObject.optJSONArray("report_info").toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(4000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            setNotes();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null, CustomHttpClient.Method.POST, true);
    }


    private void selectImage() {
        if (((HomeActivity) activity).isReadWriteGranded()) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            try {

                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"), 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ((HomeActivity) activity).permission(1);
        }

    }

    /*
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() {
        if (((HomeActivity) activity).isCameraGranded()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            try {
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ((HomeActivity) activity).permission(0);
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // successfully captured the image
                // bimatp factory
                try {


                    BitmapFactory.Options options = new BitmapFactory.Options();

                    // downsizing image as it throws OutOfMemory Exception for
                    // larger
                    // images
                    options.inSampleSize = 8;

                    final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);


                    selectedImagePath = fileUri.getPath();



                    DisplayMetrics dm = new DisplayMetrics();
                    activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
                    final SubsamplingScaleImageView image = new SubsamplingScaleImageView(activity);
                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(dm.widthPixels / 4, dm.widthPixels / 4);
                    // vp.setMargins(5, 5, 5, 5);
                    image.setLayoutParams(vp);
                    new ImageCompressionAsyncTask(false,image).execute(selectedImagePath + "");
                    if(bitmap!=null) {
                        image.setImage(ImageSource.bitmap(bitmap));
                        image.setZoomEnabled(false);
                        llHorizontalScrollContainer.addView(image);
                    }
                    else{
                        Toast.makeText(activity,"image null",Toast.LENGTH_SHORT).show();
                    }

                    if (networkconnection.isOnline(activity)) {

                        new PitureUploadTask().execute();

                    } else {
                        dbHolder.addPhotos(property.getId() + "", selectedImagePath);
                        Toast.makeText(activity, "Images saved Locally.", Toast.LENGTH_LONG).show();
                        Toast.makeText(activity, "Internet connection required.", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                // //

            } else if (resultCode == Activity.RESULT_CANCELED) {

            } else {
                // failed to capture image
                Toast.makeText(activity,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    Uri selectedImageUri = data.getData();

                    selectedImagePath = getPath(selectedImageUri);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPurgeable = true;
                    options.inSampleSize = 8;
                    final Bitmap bitmap = BitmapFactory.decodeFile(
                            selectedImagePath, options);


                    //selectedImagePath = fileUri.getPath();


                    if (networkconnection.isOnline(activity)) {

                        new PitureUploadTask().execute();

                    } else {

                        dbHolder.addPhotos(property.getId() + "", selectedImagePath);
                        Toast.makeText(activity, "Images saved Locally.", Toast.LENGTH_LONG).show();
                        Toast.makeText(activity, "Internet connection required.", Toast.LENGTH_LONG).show();
                    }

                    DisplayMetrics dm = new DisplayMetrics();
                    activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
                    final SubsamplingScaleImageView image = new SubsamplingScaleImageView(activity);
                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(dm.widthPixels / 4, dm.widthPixels / 4);
                    // vp.setMargins(5, 5, 5, 5);
                    image.setLayoutParams(vp);
                    //image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    // image.setImageBitmap(bitmap);

                    if(bitmap!=null) {
                        image.setImage(ImageSource.bitmap(bitmap));
                        image.setZoomEnabled(false);
                        llHorizontalScrollContainer.addView(image);
                    }
                    else{
                        Toast.makeText(activity,"image null",Toast.LENGTH_SHORT).show();
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }


    private class PitureUploadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         //   progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                MultipartUtility mu = new MultipartUtility(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.UPLOAD_PHOTOS,
                        "UTF-8");
                mu.addFormField("pid", property.getId() + "");
                mu.addFormField("uid",  SPUser.getString(activity, SPUser.USER_ID) + "");
                if (selectedImagePath != null) {
                    mu.addFilePart("0", selectedImagePath);
                }

                serverImageName = mu.finish();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("httpUser Exception", e.toString());
                serverImageName = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
           // progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(serverImageName + "");
                if (jsonObject.optBoolean("status")) {
                    Toast.makeText(activity, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                    updateData(0);
                    //((HomeActivity) activity).refreshList();
                } else {
                    Toast.makeText(activity, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    void updateData(final int i) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("uname", SPUser.getString(activity, SPUser.USER_NAME)));
        params.add(new BasicNameValuePair("password", SPUser.getString(activity, SPUser.USER_PASSWORD)));
        new CustomHttpClient(activity).executeHttp(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.LOGIN, params, null, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {

                    JSONObject jObject = new JSONObject(result);
                    JSONObject jsonObject = jObject.optJSONObject("login_info");
                    if (jsonObject.optString("auto_login").equalsIgnoreCase("1")) {
                        SPUser.setString(activity, SPUser.USER_ID, jsonObject.optString("id"));
                    }
                    SPUser.setString(activity, SPUser.PROPERTIES, jObject.optJSONObject("properites") + "");
                    SPUser.setString(activity, SPUser.REPORTS, jObject.optJSONObject("reports") + "");
                    SPUser.setString(activity, SPUser.REPORT_INFO, jObject.optJSONArray("report_info").toString());

                    setDataSideMenu(SPUser.getString(activity, SPUser.REPORTS));
                    if (i == 1) {
                        getUpdatedData(SPUser.getString(activity, SPUser.PROPERTIES));
                    }

                    try {
                        alNotes = new ArrayList<>();
                        alNotes.addAll(property.getNotes());
                        //    alNotes=property.getNotes();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    //     alNotes=property.getNotes();
                    int n =alNotes.size();
                    for (int i = 0; i < n; i++) {
                        if (alNotes.get(i).getNote().toString().equalsIgnoreCase("none")||alNotes.get(i).getNote().toString().equalsIgnoreCase("null")||alNotes.get(i).getNote().toString().equalsIgnoreCase("")||alNotes.get(i).getNote().toString().equalsIgnoreCase("null")||alNotes.get(i).getNote().toString()==null) {
                            alNotes.remove(i);
                            i--;
                            --n;
                        }
                    }

                    if (alNotes.size()==0){
                        tvSpecialNotesDescription.setText("No notes are available");
                    }
                    else if (alNotes.size()>1) {
                        String notesShown = alNotes.get(0).getNote();
                        tvSpecialNotesDescription.setText(Html.fromHtml(notesShown));
                        if (alNotes.get(0).getNote().equalsIgnoreCase("none")||alNotes.get(0).getNote().toString().equalsIgnoreCase("null")||alNotes.get(0).getNote().toString().equalsIgnoreCase(""))
                        {
                            tvSpecialNotesDescription.setText(alNotes.get(1).getNote());
                        }
                    }
                    else {
                        tvSpecialNotesDescription.setText("No notes are available");
                    }
                    if (alNotes.size()==0){
                        tvSpecialNotesDescription.setText("No notes are available");
                    }else {
                        String notesShown = alNotes.get(0).getNote() + "<br>" + "<b> Posted on : </b> " + alNotes.get(0).getDate() + "</br> <br>" + "<b> Posted by : </b> " + alNotes.get(0).getname() + "<br/>";
                        tvSpecialNotesDescription.setText(Html.fromHtml(notesShown));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null, CustomHttpClient.Method.POST, true);

    }


    private void getUpdatedData(String p) {
        try {
            if ((!p.equalsIgnoreCase("")) && (!p.equals(null)) && (!p.equalsIgnoreCase("null"))) {
                JSONObject jsonObject = new JSONObject(p);
                ArrayList<Property> alLocation = new ArrayList<>();
                if (jsonObject.optJSONArray("property").length() > 0) {

                    alLocation = new Gson().fromJson(jsonObject.optJSONArray("property").toString(), new TypeToken<List<Property>>() {
                    }.getType());

                }


                for (int i = 0; i < alLocation.size(); i++) {
                    if (alLocation.get(i).getId() == property.getId()) {
                        property = alLocation.get(i);
                     //   tvSpecialNotesDescription.setText(Message.toString(property.getNotes()));
                        setProgress();
                        break;
                    }
                }
 }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void setDataSideMenu(String string) {
        try {
            llList.removeAllViews();
            JSONObject jsonObject = new JSONObject(string);

            if ((!string.equalsIgnoreCase("")) && (!string.equals(null)) && (!string.equalsIgnoreCase("null"))) {

                if (jsonObject.optJSONArray("report").length() > 0) {

                    alReports = new ArrayList<Reports>();
                    for (int i = 0; i < jsonObject.optJSONArray("report").length(); i++) {
                        JSONObject jsonObject1 = jsonObject.optJSONArray("report").optJSONObject(i);
                        Reports pojo = new Reports();
                        pojo.setReport_id(jsonObject1.optLong("report_id"));
                        pojo.setSite_id(jsonObject1.optLong("site_id"));
                        pojo.setReport_name(jsonObject1.optString("report_name"));
                        pojo.setForm_description(jsonObject1.optString("form_description"));
                        pojo.setSend_to(jsonObject1.optString("send_to"));
                        pojo.setMail_subject(jsonObject1.optString("mail_subject"));
                        pojo.setSubmit_button_text(jsonObject1.optString("submit_button_text"));
                        pojo.setProperty_box_label(jsonObject1.optString("property_box_label"));
                        pojo.setSubmissions(jsonObject1.optString("submissions"));
                        pojo.setDate_added(jsonObject1.optString("date_added"));
                        pojo.setStatus(jsonObject1.optInt("status"));
                        pojo.setForm_body(jsonObject1.optString("form_body"));
                        if (pojo.getReport_id() != 0) {
                            if (("," + property.getEnabled_reports() + ",").contains("," + pojo.getReport_id() + ",")) {
                                alReports.add(pojo);
                            } else {
                                System.out.println("alMasterData name === " + property.getLocation_name());
                            }
                        }

                    }
                    alReportInfo = new Gson().fromJson(SPUser.getString(activity, SPUser.REPORT_INFO), new TypeToken<List<ReportInfo>>() {
                    }.getType());
                    for (int i = 0; i < alReports.size(); i++) {
                        View revportView = inflater.inflate(R.layout.row_avaliable_property_reports, null);

                        TextView tvReportName = (TextView) revportView.findViewById(R.id.tv_row_avaliable_property_reports_name);
                        TextView tvAction = (TextView) revportView.findViewById(R.id.tv_row_avaliable_property_reports_action);
                        tvReportName.setText(alReports.get(i).getReport_name());
                      /*  Cursor cursor=  dbHolder.GetPropertyRecord("5", "1","81");

                        if (cursor.getCount()>0){
                            while (cursor.moveToNext()){
                                WebUrl.ShowLog("cursor GetPropertyRecord=="+cursor.getString(1)+"==="+cursor.getString(3));
                                tvAction.setText("Reported"+"\n"+"Last on"+"\n"+cursor.getString(5));

                            }
                        }*/
                        tvAction.setText("Not Reported");

                        for (int j = 0; j < alReportInfo.size(); j++) {

                            if (alReportInfo.get(j).getProperty_id() == property.getId() && alReportInfo.get(j).getReport_id() == alReports.get(i).getReport_id()) {

                                System.out.println("getReport_id()=="+ alReportInfo.get(j).getReport_id()+"");
                                System.out.println("alReportInfo.get(j).getProperty_id()=="+ alReportInfo.get(j).getProperty_id()+"");
                                tvAction.setText("Reported" + "\n" + "Last on" + "\n" + alReportInfo.get(j).getSubmission_date());


                                break;
                            }

                     /*      tvAction.setTag(alReportInfo.get(finalJ).getProperty_id()+"");
                            tvReportName.setTag(alReportInfo.get(j).getProperty_id()+"");*/


                        }


                        Cursor cursor1=dbHolder.GetPropertyRecord(alReports.get(i).getReport_id()+"",  property.getLocation_id()+"", property.getId()+"");
                        System.out.println("cursor1=="+cursor1.getCount()+"");
                        if (cursor1.getCount()>0){
                            while (cursor1.moveToNext()){
                                System.out.println("cursor1.getString(5)=="+cursor1.getString(5)+"");
                                tvAction.setText("Reported"+"\n"+"Last on"+"\n"+cursor1.getString(5));
                            }
                        }

                        cursor1.close();
                        final int finalI = i;
                        tvAction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(activity, ReportForms.class).putExtra("report", alReports.get(finalI)).putExtra("location_id",property.getLocation_id()).putExtra("property_id",property.getId()));
                            }
                        });
                        tvReportName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(activity, ReportForms.class).putExtra("report", alReports.get(finalI)).putExtra("location_id",property.getLocation_id()).putExtra("property_id",property.getId()));
                            }
                        });

                        llList.addView(revportView);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //Image compression

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
        private final SubsamplingScaleImageView imageView;
        private boolean fromGallery;

        public ImageCompressionAsyncTask(boolean fromGallery,SubsamplingScaleImageView imageView) {
            this.fromGallery = fromGallery;
            this.imageView=imageView;
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = compressImage(params[0]);
            return filePath;
        }

        public String compressImage(String imageUri) {
            try {

                String filePath = getRealPathFromURI(imageUri);
                Bitmap scaledBitmap = null;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

                int actualHeight = options.outHeight;
                int actualWidth = options.outWidth;
                float maxHeight = 816.0f;
                float maxWidth = 612.0f;
                float imgRatio = actualWidth / actualHeight;
                float maxRatio = maxWidth / maxHeight;

                if (actualHeight > maxHeight || actualWidth > maxWidth) {
                    if (imgRatio < maxRatio) {
                        imgRatio = maxHeight / actualHeight;
                        actualWidth = (int) (imgRatio * actualWidth);
                        actualHeight = (int) maxHeight;
                    } else if (imgRatio > maxRatio) {
                        imgRatio = maxWidth / actualWidth;
                        actualHeight = (int) (imgRatio * actualHeight);
                        actualWidth = (int) maxWidth;
                    } else {
                        actualHeight = (int) maxHeight;
                        actualWidth = (int) maxWidth;

                    }
                }

                options.inSampleSize = utils.calculateInSampleSize(options,
                        actualWidth, actualHeight);
                options.inJustDecodeBounds = false;
                options.inDither = false;
                options.inPurgeable = true;
                options.inInputShareable = true;
                options.inTempStorage = new byte[16 * 1024];
                options.inSampleSize=8;

                try {
                    bmp = BitmapFactory.decodeFile(filePath, options);
                } catch (OutOfMemoryError exception) {
                    exception.printStackTrace();

                    return imageUri;
                }
                try {
                    scaledBitmap = Bitmap.createBitmap(actualWidth,
                            actualHeight, Bitmap.Config.ARGB_8888);
                } catch (OutOfMemoryError exception) {
                    exception.printStackTrace();
                }

                float ratioX = actualWidth / (float) options.outWidth;
                float ratioY = actualHeight / (float) options.outHeight;
                float middleX = actualWidth / 2.0f;
                float middleY = actualHeight / 2.0f;

                Matrix scaleMatrix = new Matrix();
                scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

                Canvas canvas = new Canvas(scaledBitmap);
                canvas.setMatrix(scaleMatrix);
                canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY
                        - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

                ExifInterface exif;
                try {
                    exif = new ExifInterface(filePath);

                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, 0);
                    Log.d("EXIF", "Exif: " + orientation);
                    Matrix matrix = new Matrix();
                    if (orientation == 6) {
                        matrix.postRotate(90);
                        Log.d("EXIF", "Exif: " + orientation);
                    } else if (orientation == 3) {
                        matrix.postRotate(180);
                        Log.d("EXIF", "Exif: " + orientation);
                    } else if (orientation == 8) {
                        matrix.postRotate(270);
                        Log.d("EXIF", "Exif: " + orientation);
                    }
                    scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                            scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                            matrix, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream out = null;
                String filename = getFilename();
                try {
                    out = new FileOutputStream(filename);
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                return filename;

            } catch (Exception e) {
                e.printStackTrace();

            }
            catch (Error e) {
                e.printStackTrace();

            }
            return imageUri;

        }

        private String getRealPathFromURI(String contentURI) {
            Uri contentUri = Uri.parse(contentURI);
            Cursor cursor = activity.getContentResolver().query(contentUri, null, null,
                    null, null);
            if (cursor == null) {
                return contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor
                        .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(idx);
            }
        }

        public String getFilename() {

            File file = activity.getApplicationContext().getCacheDir();
            if (!file.exists()) {
                file.mkdirs();
            }
            String uriSting = (file.getAbsolutePath() + "/"
                    + System.currentTimeMillis() + ".jpg");
            return uriSting;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPurgeable = true;
                options.inSampleSize = 8;
                final Bitmap bitmap = BitmapFactory.decodeFile(
                        result/* fileUri.getPath() */, options);
                imageView.setVisibility(View.VISIBLE);

               // imageView.setImageBitmap(bitmap);
                Log.e("Compresed async----",result+"");

                imageView.setTag(result);
                selectedImagePath=result;

            }else{
                Toast.makeText(activity, "Low Memory",
                        Toast.LENGTH_LONG).show();
            }
            if (fromGallery) {
               /* Bundle bundle = new Bundle();
                bundle.putString("FILE_PATH", result);
                showDialog(1, bundle);*/
            } else {
				/*
				 * database.insertUri(result); cursor = database.getallUri();
				 * adapter.changeCursor(cursor);
				 */
            }
        }

    }


}
