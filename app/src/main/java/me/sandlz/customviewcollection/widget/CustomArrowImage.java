package me.sandlz.customviewcollection.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.InputStream;

import me.sandlz.customviewcollection.R;

/**
 * Created by liuzhu on 2016/11/29.
 * Description : 自定义图片样式 (在圆形图片的基础上 增加一个icon 可指定icon的位置 )
 * 开启旋转后 只需针对右上角设计资源图即可 会根据position位置自动旋转icon
 * Usage :
 * arrowSrc      -      资源ID
 * fixRate       -      缩放比例   默认1
 * position      -      位置      默认右上角1
 * allowRotation -      启用旋转   默认不启用
 */
public class CustomArrowImage extends ImageView {

    private Paint paint ;
    // 角图片资源ID
    private int arrowId;
    private int oldArrowId;
    // 角图片缩放比例
    private float fixRate;
    // 角图片位置 (左上0 右上1 左下2 右下3)
    private int position;
    // 旋转
    private boolean allowRotation;
    // 边框
    private int mBorderThickness = 2;
    private int radius = 0;
    private int defaultWidth = 0;
    private int defaultHeight = 0;
    private int color;



    public CustomArrowImage(Context context) {
        super(context);
        paint = new Paint();
    }

    public CustomArrowImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        // 获取自定义属性值

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.CustomArrowImage, 0, 0);
        try {
            int arrowId  = a.getResourceId(R.styleable.CustomArrowImage_arrowSrc,0);
            int position = a.getInteger(R.styleable.CustomArrowImage_position,1);
            float fixRate  = a.getFloat(R.styleable.CustomArrowImage_fixRate,1);
            boolean allowRotation = a.getBoolean(R.styleable.CustomArrowImage_allowRotation,false);
            int borderWidth = a.getInt(R.styleable.CustomArrowImage_borderWidth,0);
            int color = a.getColor(R.styleable.CustomArrowImage_borderColor,Color.BLUE);
            this.mBorderThickness = borderWidth;
            this.color = color;
            this.position = position;
            this.arrowId = arrowId;
            this.oldArrowId = arrowId;
            this.fixRate = fixRate;
            this.allowRotation = allowRotation;
        } finally {
            a.recycle();
        }
    }

    public CustomArrowImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (defaultWidth == 0) {
            defaultWidth = getWidth();
        }
        if (defaultHeight == 0) {
            defaultHeight = getHeight();
        }
        // 画边框
        radius = (defaultWidth < defaultHeight ? defaultWidth
                : defaultHeight) / 2 - 2 * mBorderThickness;
        if (mBorderThickness > 0) {
            drawCircleBorder(canvas, radius + mBorderThickness / 2,
                    color);
        }
        Drawable drawable = getDrawable();
        if (null != drawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap b = getCircleBitmap(bitmap, 14);
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0,0,getWidth(),getHeight());
            paint.reset();
            // 绘制右上角箭头
            if (arrowId != 0) {
                InputStream is = getResources().openRawResource(arrowId);
                Bitmap mBitmap = BitmapFactory.decodeStream(is);
                // 圆形照片半径
                double image_r = b.getWidth()/2;
                // 照片对角线一半长度
                double image_squre_r = image_r*sqrt(2)/2;
                // 角落正方形对角线长度
                double arrrow_squre_r = image_squre_r - image_r/2;
                // 角落圆形半直径
                double arrow_r = arrrow_squre_r/sqrt(2);
                // 获取应绘制箭头的宽高 按缩放比例
                double a_width = arrow_r;
                if (fixRate > 0) {
                    a_width = arrow_r*fixRate;
                }
                int left = (int) (getWidth()- a_width);
                // 区域
                final Rect rectSrcA = new Rect(0, 0,(int)a_width, (int)a_width);
                // 位置
                final Rect rectDestA = new Rect(left,0,getWidth(),(int)a_width);

                switch (position){
                    case 0:
                        // 左上
                        rectDestA.left = 0;
                        rectDestA.top = 0;
                        rectDestA.right = (int)a_width;
                        rectDestA.bottom = (int)a_width;
                        // 顺时针270度
                        if (allowRotation) {
                            mBitmap = adjustRotation(mBitmap,270);
                        }
                        break;
                    case 1:
                        // 默认 右上
                        break;
                    case 2:
                        // 左下
                        rectDestA.left = 0;
                        rectDestA.top = getHeight()-(int)a_width;
                        rectDestA.right = (int)a_width;
                        rectDestA.bottom = getHeight();
                        // 顺时针180度
                        if (allowRotation) {
                            mBitmap = adjustRotation(mBitmap,180);
                        }
                        break;
                    case 3:
                        // 右下
                        rectDestA.left = left;
                        rectDestA.top = getHeight()-(int)a_width;
                        rectDestA.right = getWidth();
                        rectDestA.bottom = getHeight();
                        // 顺时针90度
                        if (allowRotation) {
                            mBitmap = adjustRotation(mBitmap,90);
                        }
                        break;
                }
                // 角落图形
                canvas.drawBitmap(mBitmap, rectSrcA,rectDestA, paint);
            }
            // 圆形图形
//            canvas.drawBitmap(b, rectSrc, rectDest, paint);
            canvas.drawBitmap(b, defaultWidth / 2 - radius, defaultHeight
                    / 2 - radius, null);
        } else {
            super.onDraw(canvas);
        }
    }

    public void setArrowVisibility(boolean hide) {
        if (hide) {
            arrowId = 0;
        }else {
            arrowId = oldArrowId;
        }
        invalidate();
    }

    public void setArrowResource(int resourceId) {
        arrowId = resourceId;
        invalidate();
    }

    /**
     * 获取圆形图片
     * @param bmp
     * @param pixels
     * @return Bitmap
     * @author caizhiming
     */
    private Bitmap getCircleBitmap(Bitmap bmp, int pixels) {
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;

        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth = 0, squareHeight = 0;
        int x = 0, y = 0;
        Bitmap squareBitmap;
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else {
            squareBitmap = bmp;
        }

        if (squareBitmap.getWidth() != diameter
                || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
                    diameter, true);

        } else {
            scaledSrcBmp = squareBitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
                scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
                paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
        return output;
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
//                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//
//        final int color = 0xff424242;
//
//        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//        int x = bitmap.getWidth();
//        canvas.drawCircle(x / 2, x / 2, x / 2, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rect, paint);
//        return output;
    }

    private void drawCircleBorder(Canvas canvas, int radius, int color) {
        Paint paint = new Paint();
        /* 去锯齿 */
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(color);
        /* 设置paint的　style　为STROKE：空心 */
        paint.setStyle(Paint.Style.STROKE);
        /* 设置paint的外框宽度 */
        paint.setStrokeWidth(mBorderThickness);
        canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, paint);
    }

    /**
     * 旋转图片
     * @param bm
     * @param orientationDegree
     * @return
     */
    private Bitmap adjustRotation(Bitmap bm, final int orientationDegree) {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else if (orientationDegree == 180){
            targetX =  bm.getWidth();
            targetY = bm.getHeight();
        }else if (orientationDegree == 270) {
            targetX = 0;
            targetY = bm.getHeight();
        }else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }
        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];

        m.postTranslate(targetX - x1, targetY - y1);
        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);
        return bm1;
    }

    /**
     * 平方根
     * @param r
     * @return
     */
    private double sqrt(double r) {
        return Math.sqrt(r);
    }
}
