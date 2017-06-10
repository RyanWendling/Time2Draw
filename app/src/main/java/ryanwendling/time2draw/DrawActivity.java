package ryanwendling.time2draw;

import android.graphics.Bitmap;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import ryanwendling.time2draw.database.ScoreBaseHelper;


public class DrawActivity extends AppCompatActivity {

    private CanvasView canvasView;
    public EditText nameBox;
    ImageView loadedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        nameBox = (EditText) findViewById(R.id.username);

        canvasView = (CanvasView) findViewById(R.id.canvas);
    }

    public void clearCanvas(View v) {
        loadedImg = (ImageView)findViewById(R.id.imageview);
        loadedImg.setImageResource(0);
        canvasView.clearCanvas();
    }

    public void onClickUndo(View v) {
        canvasView.onClickUndo();
    }

    public void onClickRedo(View v) {
        canvasView.onClickRedo();
    }

    public void onClickColor(View v) {
        canvasView.changeColor();
    }

    public void onClickBold(View v) {
        canvasView.changeBold();
    }

    public void prepImage(View v) {

        ScoreBaseHelper dbHandler = new ScoreBaseHelper(this, null, null, 1);
        canvasView.prepImageTwo(dbHandler, nameBox.getText().toString());
        AlertDialog.Builder a_builder = new AlertDialog.Builder(DrawActivity.this);
        nameBox = (EditText) findViewById(R.id.username);
        a_builder.setMessage("Image saved under name:  " + nameBox.getText());
        AlertDialog alert = a_builder.create();
        alert.setTitle("Result: ");
        alert.show();
    }

    public void delImage(View v) {

        ScoreBaseHelper dbHandler = new ScoreBaseHelper(this, null, null, 1);
        Boolean isDeleted = canvasView.delImageTwo(dbHandler, nameBox.getText().toString());

        if (isDeleted == true) {
            AlertDialog.Builder a_builder = new AlertDialog.Builder(DrawActivity.this);
            nameBox = (EditText) findViewById(R.id.username);
            a_builder.setMessage("Image deleted successfully");
            AlertDialog alert = a_builder.create();
            alert.setTitle("Result: ");
            alert.show();
        } else {
            AlertDialog.Builder a_builder = new AlertDialog.Builder(DrawActivity.this);
            nameBox = (EditText) findViewById(R.id.username);
            a_builder.setMessage("Image not deleted");
            AlertDialog alert = a_builder.create();
            alert.setTitle("Result: ");
            alert.show();
        }
    }

    public void loadImage(View v) {

        ScoreBaseHelper dbHandler = new ScoreBaseHelper(this, null, null, 1);
        Bitmap loadededImg = canvasView.loadImageTwo(dbHandler, nameBox.getText().toString());
        ImageView img=(ImageView)findViewById(R.id.imageview);
        img.setImageBitmap(loadededImg);

        int bitmapByteCount = 0;

        if (loadededImg != null) {
            bitmapByteCount = BitmapCompat.getAllocationByteCount(loadededImg);
        }
        if (bitmapByteCount > 100) {
            AlertDialog.Builder a_builder = new AlertDialog.Builder(DrawActivity.this);
            nameBox = (EditText) findViewById(R.id.username);
            a_builder.setMessage("Image loaded successfully");
            AlertDialog alert = a_builder.create();
            alert.setTitle("Result: ");
            alert.show();
        } else {
            AlertDialog.Builder a_builder = new AlertDialog.Builder(DrawActivity.this);
            nameBox = (EditText) findViewById(R.id.username);
            a_builder.setMessage("No image found");
            AlertDialog alert = a_builder.create();
            alert.setTitle("Result: ");
            alert.show();
        }
    }

}
