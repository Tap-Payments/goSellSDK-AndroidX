package company.tap.gosellapi;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomTypefaceSpan extends ReplacementSpan {
    private final Typeface customTypeface;

    public CustomTypefaceSpan(Typeface customTypeface) {
        this.customTypeface = customTypeface;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        // Use the default paint size but apply the custom font
        TextPaint textPaint = new TextPaint(paint);
        textPaint.setTypeface(customTypeface);
        return (int) textPaint.measureText(text, start, end);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        // Draw the text with the custom font
        TextPaint textPaint = new TextPaint(paint);
        textPaint.setTypeface(customTypeface);
        canvas.drawText(text, start, end, x, y, textPaint);
    }


}
