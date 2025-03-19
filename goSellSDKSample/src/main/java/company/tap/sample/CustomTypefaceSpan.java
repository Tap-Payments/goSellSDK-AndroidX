package company.tap.sample;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class CustomTypefaceSpan extends TypefaceSpan {

    private final Typeface newType;

    public CustomTypefaceSpan(String family, Typeface type) {
        super(family);
        newType = type;
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        applyCustomTypeFace(paint, newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        int oldStyle = paint.getTypeface() != null ? paint.getTypeface().getStyle() : 0;
        Typeface newTypeface = tf != null ? tf : Typeface.DEFAULT;
        paint.setTypeface(newTypeface);
        paint.setFlags(paint.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
