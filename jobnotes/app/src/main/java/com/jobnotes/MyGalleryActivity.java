package com.jobnotes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.util.Progress;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by lipl-1111 on 5/9/16.
 */
public class MyGalleryActivity extends Activity {

    private ArrayList<String> _images;
    private GalleryPagerAdapter _adapter;
    private ViewPager pager;
    private TextView tvBack;
    private int position=0;
 AQuery aq;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gallery);
        pager=(ViewPager)findViewById(R.id.pager);
        tvBack=(TextView)findViewById(R.id.tv_advance_header);

        aq = new AQuery(this);

      //_images = (ArrayList<String>) getIntent().getSerializableExtra("image");
        try {
            //   _images = (ArrayList<String>) getIntent().getStringArrayListExtra("image"); //old
            _images = (ArrayList<String>) getIntent().getExtras().getSerializable("image");

            //String p = getIntent().getStringExtra("index");//old
            String p = getIntent().getExtras().getString("index");
            if (p != null)
            {
                position  = Integer.parseInt(p);
            }
        }catch (Exception e){position=0;}
     //   _images = (ArrayList<String>) getIntent().getSerializableExtra("image");


      //  Assert.assertNotNull(_images);

       _adapter = new GalleryPagerAdapter(this);
       pager.setAdapter(_adapter);
       pager.setOffscreenPageLimit(8); // how many images to load into memory
        pager.setCurrentItem(position);


        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MyGalleryActivity", "Close clicked");
                finish();
            }
        });
    }

  class GalleryPagerAdapter extends PagerAdapter {

        Context _context;
        LayoutInflater _inflater;

        public GalleryPagerAdapter(Context context) {
            _context = context;
            _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return _images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((FrameLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = _inflater.inflate(R.layout.pager_gallery_item, container, false);
            container.addView(itemView);


            final SubsamplingScaleImageView imageView =
                    (SubsamplingScaleImageView) itemView.findViewById(R.id.image);
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
            // Asynchronously load the image and set the thumbnail and pager view
         Glide.with(_context)
                    .load(_images.get(position))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                          progressBar.setVisibility(View.GONE);
                          imageView.setImage(ImageSource.bitmap(bitmap));


                        }
                    });


            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((FrameLayout) object);
        }
    }
}

