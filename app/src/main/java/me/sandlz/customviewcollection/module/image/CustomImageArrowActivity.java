package me.sandlz.customviewcollection.module.image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewTreeObserver;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import me.sandlz.customviewcollection.R;
import me.sandlz.customviewcollection.base.BaseActivity;
import me.sandlz.customviewcollection.util.PublicUtils;
import me.sandlz.customviewcollection.widget.CustomArrowImage;

@ContentView(R.layout.activity_custom_image_arrow)
public class CustomImageArrowActivity extends BaseActivity {

    @ViewInject(R.id.image_custom_view)
    CustomArrowImage image;
    @ViewInject(R.id.image_custom_view2)
    CustomArrowImage image2;

    private List<Bitmap> bitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        // 用于获取ImageView的宽高 计算组头像
        ViewTreeObserver vto2 = image.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                image.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                final int width= image.getWidth();

                // 406
                String a = "http://t1.mmonly.cc/uploads/tu/201611/89/u=297188298,2258692078&fm=21&gp=0.jpg";
                // 200
                String b = "http://t1.mmonly.cc/uploads/tu/201611/89/24.png";
                // 100
                String c = "http://p5.gexing.com/GSF/touxiang/20161126/14/33clm9bfhie2lhrs47cgzamub.jpg@!200x200_3?recache=20131108";
                String d = "http://t1.mmonly.cc/uploads/tu/201611/89/23.png";

                List<String> images = new ArrayList<String>();
                images.add(a);
                images.add(b);
                images.add(c);
//                images.add(d);
                PublicUtils.loadMutiImageSource(CustomImageArrowActivity.this,image2,width,images);
            }
        });




    }

}
