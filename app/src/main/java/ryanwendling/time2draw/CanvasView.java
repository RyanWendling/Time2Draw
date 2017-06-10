package ryanwendling.time2draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;
import ryanwendling.time2draw.database.ScoreBaseHelper;

/**
 * Created by wendlir on 5/26/17.
 */

// This class holds our draw calls to a modified view. We use a bitmap to hold the pixels, which this class writes to. We also use a
// drawing primitive like rect or path and we also use a pain to describe colors and styles for drawing.
public class CanvasView extends View {

    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>();

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private float mX, mY;

    //pixels your finger has to move before we start drawing to screen
    private static final float TOLERLIM = 6;
    Context context;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(4f);
    }

    // called at beginning when our view size is rendering
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //creates bitmap of w/h dimensions, makes it mutable
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //apply bitmap to graphic to start drawing
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Path p : paths) {
            canvas.drawPath(p, mPaint);
        }
        canvas.drawPath(mPath, mPaint);
    }

    private void startTouch(float x, float y) {

        undonePaths.clear();
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(x - mY);
        if (dx >= TOLERLIM || dy >= TOLERLIM) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mY = y;
            mX = x;
        }
    }

    public void clearCanvas() {

        paths.clear();
        undonePaths.clear();
        mPath.reset();
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void changeColor() {

        Random rnd = new Random();
        mPaint.setARGB(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void changeBold() {

        float current = mPaint.getStrokeWidth();
        if (current == 4f) {
            mPaint.setStrokeWidth(2 * 4f);
        } else {
            mPaint.setStrokeWidth(4f);
        }
    }

    private void upTouch() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        paths.add(mPath);
        mPath = new Path();
    }

    // corresponds to user's drawing actions
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        // move to point that user just pressed
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
        }
        // user intents to draw
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
        }
        // user is done drawing, push path to our canvas and reset path
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }

        return true;
    }

    public void onClickUndo() {
        if (paths.size()>0)
        {
            undonePaths.add(paths.remove(paths.size()-1));
            invalidate();
        }
    }

    public void onClickRedo(){
        if (undonePaths.size()>0)
        {
            paths.add(undonePaths.remove(undonePaths.size()-1));
            invalidate();
        }
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        int w = 1, h = 1;

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(w, h, conf);
            if (image != null) {
                return BitmapFactory.decodeByteArray(image, 0, image.length);
            }
            else {
                return bmp;
            }
    }

    public void prepImageTwo(ScoreBaseHelper dbHandler, String artName) {

        byte[] currentImage = getBytes(this.mBitmap);
        dbHandler.addUser(artName, currentImage);
    }

    public Bitmap loadImageTwo(ScoreBaseHelper dbHandler, String artName) {

        byte[] foundImage = dbHandler.findUser(artName);
        Bitmap savedImage = getImage(foundImage);

        clearCanvas();
        return savedImage;
    }

    public boolean delImageTwo(ScoreBaseHelper dbHandler, String artName) {
        return dbHandler.deleteUser(artName);
    }
}
