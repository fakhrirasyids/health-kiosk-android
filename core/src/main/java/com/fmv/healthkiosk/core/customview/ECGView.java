package com.fmv.healthkiosk.core.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.fmv.healthkiosk.core.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//
//public class ECGView extends View {
//    private Paint paintECG, paintGrid, paintText;
//    private float[] ecgBuffer;
//    private int bufferIndex = 0;
//    private static final int BUFFER_SIZE = 500; // Simpan 500 titik terakhir
//    private static final float MAX_Y = 500, MIN_Y = -500; // Rentang ECG dalam mikrovolt
//    private static final float TIME_STEP = 10f; // Jarak antar titik ECG dalam px
//    private static final float Y_AXIS_WIDTH = 80f; // Lebar area angka di kiri
//    private static final float STEP_Y = 250; // Langkah untuk label Y
//
//    public ECGView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    private void init() {
//        setBackgroundColor(Color.TRANSPARENT);
//
//        paintECG = new Paint();
//        paintECG.setColor(Color.parseColor("#58FFCF"));
//        paintECG.setStrokeWidth(4);
//        paintECG.setAntiAlias(true);
//
//        paintGrid = new Paint();
//        paintGrid.setColor(Color.parseColor("#CC34414E"));
//        paintGrid.setStrokeWidth(2);
//
//        paintText = new Paint();
//        paintText.setColor(Color.parseColor("#CC34414E"));
//        paintText.setTextSize(20);
//        paintText.setAntiAlias(true);
//
//        ecgBuffer = new float[BUFFER_SIZE]; // Inisialisasi buffer ECG
//        Arrays.fill(ecgBuffer, 0); // Inisialisasi dengan nol
//    }
//
//    /** Menambahkan data ECG ke dalam buffer */
//    public void setEcgData(List<Float> data) {
//        ecgBuffer = new float[BUFFER_SIZE]; // Gunakan BUFFER_SIZE
//        bufferIndex = 0;
//
//        for (int i = 0; i < data.size() && i < BUFFER_SIZE; i++) {
//            ecgBuffer[i] = data.get(i);
//            bufferIndex = (bufferIndex + 1) % BUFFER_SIZE;
//        }
//        invalidate();
//    }
//
//
//    /** Menghapus data ECG */
//    public void resetECG() {
//        Arrays.fill(ecgBuffer, 0);
//        bufferIndex = 0;
//        invalidate();
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        drawGrid(canvas);
//        drawECG(canvas);
//        drawYAxisLabels(canvas);
//    }
//
//    private void drawGrid(Canvas canvas) {
//        float width = getWidth();
//        float height = getHeight();
//        float gridSpacingX = 100;
//        float gridSpacingY = height / ((MAX_Y - MIN_Y) / STEP_Y);
//
//        for (float x = Y_AXIS_WIDTH; x < width; x += gridSpacingX) {
//            canvas.drawLine(x, 0, x, height, paintGrid);
//        }
//
//        for (float y = 0; y <= height; y += gridSpacingY) {
//            canvas.drawLine(Y_AXIS_WIDTH, y, width, y, paintGrid);
//        }
//    }
//
//    private void drawECG(Canvas canvas) {
//        float x = Y_AXIS_WIDTH;
//        for (int i = 1; i < BUFFER_SIZE; i++) {
//            float y1 = mapECGValue(ecgBuffer[(bufferIndex + i - 1) % BUFFER_SIZE]);
//            float y2 = mapECGValue(ecgBuffer[(bufferIndex + i) % BUFFER_SIZE]);
//            canvas.drawLine(x, y1, x + TIME_STEP, y2, paintECG);
//            x += TIME_STEP;
//            if (x > getWidth()) break; // Hindari menggambar di luar layar
//        }
//    }
//
//    private void drawYAxisLabels(Canvas canvas) {
//        float height = getHeight();
//        float numSteps = (MAX_Y - MIN_Y) / STEP_Y;
//        float spacingY = height / numSteps;
//
//        for (int i = 0; i <= numSteps; i++) {
//            float value = MIN_Y + (i * STEP_Y);
//            float yPos = height - (i * spacingY);
//            canvas.drawText(String.valueOf((int) value), 10, yPos, paintText);
//        }
//    }
//
//    private float mapECGValue(float value) {
//        return getHeight() - ((value - MIN_Y) / (MAX_Y - MIN_Y) * getHeight());
//    }
//}


public class ECGView extends View {
    private Paint paintECG, paintGrid, paintText;
    private List<Float> ecgData;
    private float maxY = 200, minY = 0; // Rentang nilai ECG
    private float timeStep = 10f; // Jarak antar titik ECG dalam px
    private float yAxisWidth = 80f; // Lebar area angka di kiri
    private float stepY = 50; // Langkah untuk label Y (bisa diubah)

    public ECGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);

        // Garis ECG
        paintECG = new Paint();
        paintECG.setColor(Color.parseColor("#58FFCF"));
        paintECG.setStrokeWidth(4);
        paintECG.setAntiAlias(true);

        // Garis Grid
        paintGrid = new Paint();
        paintGrid.setColor(Color.parseColor("#CC34414E"));
        paintGrid.setStrokeWidth(2);

        // Text Label (angka di kiri)
        paintText = new Paint();
        paintText.setColor(Color.parseColor("#CC34414E"));
        paintText.setTextSize(20);
        paintText.setAntiAlias(true);

        ecgData = new ArrayList<>();
    }

    public void addECGData(float value) {
        ecgData.add(value);

        // Batasi jumlah data agar tidak terlalu panjang
        if (ecgData.size() > (getWidth() - yAxisWidth) / timeStep) {
            ecgData.remove(0);
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGrid(canvas);
        drawECG(canvas);
        drawYAxisLabels(canvas);
    }

    private void drawGrid(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        float gridSpacingX = 100;
        float gridSpacingY = height / ((maxY - minY) / stepY); // Sesuai stepY

        // Gambar garis vertikal (mulai setelah yAxisWidth)
        for (float x = yAxisWidth; x < width; x += gridSpacingX) {
            canvas.drawLine(x, 0, x, height, paintGrid);
        }

        // Gambar garis horizontal (termasuk atas & bawah)
        for (float y = 0; y <= height; y += gridSpacingY) {
            canvas.drawLine(yAxisWidth, y, width, y, paintGrid);
        }
    }

    private void drawECG(Canvas canvas) {
        if (ecgData.size() < 2) return;

        float x = yAxisWidth;
        for (int i = 1; i < ecgData.size(); i++) {
            float y1 = mapECGValue(ecgData.get(i - 1));
            float y2 = mapECGValue(ecgData.get(i));

            canvas.drawLine(x, y1, x + timeStep, y2, paintECG);
            x += timeStep;
        }
    }

    private void drawYAxisLabels(Canvas canvas) {
        float height = getHeight();
        float numSteps = (maxY - minY) / stepY; // Total label
        float spacingY = height / numSteps; // Jarak antar label

        for (int i = 0; i <= numSteps; i++) {
            float value = minY + (i * stepY); // Nilai label (0, 50, 100, dst.)
            float yPos = height - (i * spacingY); // Dari bawah ke atas
            canvas.drawText(String.valueOf((int) value), 10, yPos, paintText);
        }
    }

    private float mapECGValue(float value) {
        return getHeight() - ((value - minY) / (maxY - minY) * getHeight());
    }
}
