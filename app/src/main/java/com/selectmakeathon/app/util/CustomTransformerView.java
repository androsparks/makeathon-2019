package com.selectmakeathon.app.util;

import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.annotation.Px;
import com.yarolegovich.discretescrollview.transform.DiscreteScrollItemTransformer;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.Pivot.X;
import com.yarolegovich.discretescrollview.transform.Pivot.Y;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
        mv = {1, 1, 13},
        bv = {1, 0, 3},
        k = 1,
        d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u0018B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0010\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u0006H\u0002J\u0018\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0006H\u0002J\u0018\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0006H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019"},
        d2 = {"Lcom/selectmakeathon/app/util/CustomTransformerView;", "Lcom/yarolegovich/discretescrollview/transform/DiscreteScrollItemTransformer;", "()V", "interpolator", "Landroid/view/animation/Interpolator;", "maxElevation", "", "maxMinDiffAlpha", "maxMinDiffScale", "minAlpha", "minElevation", "minScale", "overlapDistance", "pivotX", "Lcom/yarolegovich/discretescrollview/transform/Pivot;", "pivotY", "getMaxTranslation", "item", "Landroid/view/View;", "getTranslationDirection", "position", "getTranslationForOverlapping", "transformItem", "", "Builder", "app_debug"}
)
public final class CustomTransformerView implements DiscreteScrollItemTransformer {
    private Pivot pivotX;
    private Pivot pivotY;
    private float minScale;
    private float maxMinDiffScale;
    private float minAlpha;
    private float maxMinDiffAlpha;
    private float maxElevation = -1.0F;
    private float minElevation = -1.0F;
    private float overlapDistance;
    private Interpolator interpolator = (Interpolator)(new AccelerateInterpolator(1.2F));

    public void transformItem(@NotNull View item, float position) {
        Intrinsics.checkParameterIsNotNull(item, "item");
        Pivot var10000 = this.pivotX;
        if (this.pivotX == null) {
            Intrinsics.throwNpe();
        }

        var10000.setOn(item);
        var10000 = this.pivotY;
        if (this.pivotY == null) {
            Intrinsics.throwNpe();
        }

        var10000.setOn(item);
        float closenessToCenter = 1.0F - Math.abs(position);
        float scale = this.minScale + this.maxMinDiffScale * closenessToCenter;
        float alpha = this.minAlpha + this.maxMinDiffAlpha * closenessToCenter;
        item.setScaleX(scale);
        item.setScaleY(scale);
        item.setAlpha(alpha);
        if (this.overlapDistance > (float)0) {
            item.setTranslationX(this.getTranslationForOverlapping(item, position));
        }

        float elevation = this.minElevation + this.maxElevation * closenessToCenter;
        if (elevation >= (float)0 && VERSION.SDK_INT >= 21) {
            item.setElevation(elevation);
        }

    }

    private final float getTranslationForOverlapping(View item, float position) {
        float maxTranslation = this.getMaxTranslation(item);
        float interpolatedPosition = this.interpolator.getInterpolation(Math.abs(position));
        float translation = maxTranslation * interpolatedPosition;
        return translation * this.getTranslationDirection(position);
    }

    private final float getMaxTranslation(View item) {
        LayoutParams var10000 = item.getLayoutParams();
        if (var10000 == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        } else {
            int var4 = ((MarginLayoutParams)var10000).leftMargin;
            LayoutParams var10001 = item.getLayoutParams();
            if (var10001 == null) {
                throw new TypeCastException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            } else {
                int gapBetweenItems = var4 + ((MarginLayoutParams)var10001).rightMargin;
                float widthDiffBetweenMinAndMaxScale = (float)item.getWidth() * this.maxMinDiffScale;
                return (float)gapBetweenItems + widthDiffBetweenMinAndMaxScale + this.overlapDistance;
            }
        }
    }

    private final float getTranslationDirection(float position) {
        return Math.signum(position) * -1.0F;
    }

    public CustomTransformerView() {
        this.pivotX = X.CENTER.create();
        this.pivotY = Y.CENTER.create();
        this.minScale = 0.8F;
        this.maxMinDiffScale = 1.0F - this.minScale;
        this.minAlpha = 0.4F;
        this.maxMinDiffAlpha = 1.0F - this.minAlpha;
    }

    // $FF: synthetic method
    public static final Pivot access$getPivotX$p(CustomTransformerView $this) {
        return $this.pivotX;
    }

    // $FF: synthetic method
    public static final Pivot access$getPivotY$p(CustomTransformerView $this) {
        return $this.pivotY;
    }

    // $FF: synthetic method
    public static final float access$getOverlapDistance$p(CustomTransformerView $this) {
        return $this.overlapDistance;
    }

    // $FF: synthetic method
    public static final float access$getMaxElevation$p(CustomTransformerView $this) {
        return $this.maxElevation;
    }

    // $FF: synthetic method
    public static final float access$getMinElevation$p(CustomTransformerView $this) {
        return $this.minElevation;
    }

    // $FF: synthetic method
    public static final float access$getMaxMinDiffScale$p(CustomTransformerView $this) {
        return $this.maxMinDiffScale;
    }

    @Metadata(
            mv = {1, 1, 13},
            bv = {1, 0, 3},
            k = 1,
            d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0006\u0010\r\u001a\u00020\u0006J\u0010\u0010\u000e\u001a\u00020\u00002\b\b\u0001\u0010\u000f\u001a\u00020\fJ\u001a\u0010\u000e\u001a\u00020\u00002\b\b\u0001\u0010\u000f\u001a\u00020\f2\b\b\u0001\u0010\u0010\u001a\u00020\fJ\u000e\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u0004J\u000e\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u0004J\u0010\u0010\u0014\u001a\u00020\u00002\b\b\u0001\u0010\u0015\u001a\u00020\fJ\u000e\u0010\u0016\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u0016\u001a\u00020\u00002\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u0019\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\u001bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"},
            d2 = {"Lcom/selectmakeathon/app/util/CustomTransformerView$Builder;", "", "()V", "maxScale", "", "transformer", "Lcom/selectmakeathon/app/util/CustomTransformerView;", "assertAxis", "", "pivot", "Lcom/yarolegovich/discretescrollview/transform/Pivot;", "axis", "", "build", "setElevation", "maxElevation", "minElevation", "setMaxScale", "scale", "setMinScale", "setOverlapDistance", "distance", "setPivotX", "pivotX", "Lcom/yarolegovich/discretescrollview/transform/Pivot$X;", "setPivotY", "pivotY", "Lcom/yarolegovich/discretescrollview/transform/Pivot$Y;", "app_debug"}
    )
    public static final class Builder {
        private final CustomTransformerView transformer = new CustomTransformerView();
        private float maxScale = 1.0F;

        @NotNull
        public final CustomTransformerView.Builder setMinScale(float scale) {
            this.transformer.minScale = scale;
            return this;
        }

        @NotNull
        public final CustomTransformerView.Builder setMaxScale(float scale) {
            this.maxScale = scale;
            return this;
        }

        @NotNull
        public final CustomTransformerView.Builder setPivotX(@NotNull X pivotX) {
            Intrinsics.checkParameterIsNotNull(pivotX, "pivotX");
            Pivot var10001 = pivotX.create();
            Intrinsics.checkExpressionValueIsNotNull(var10001, "pivotX.create()");
            return this.setPivotX(var10001);
        }

        @NotNull
        public final CustomTransformerView.Builder setPivotX(@NotNull Pivot pivot) {
            Intrinsics.checkParameterIsNotNull(pivot, "pivot");
            this.assertAxis(pivot, 0);
            this.transformer.pivotX = pivot;
            return this;
        }

        @NotNull
        public final CustomTransformerView.Builder setPivotY(@NotNull Y pivotY) {
            Intrinsics.checkParameterIsNotNull(pivotY, "pivotY");
            Pivot var10001 = pivotY.create();
            Intrinsics.checkExpressionValueIsNotNull(var10001, "pivotY.create()");
            return this.setPivotY(var10001);
        }

        @NotNull
        public final CustomTransformerView.Builder setPivotY(@NotNull Pivot pivot) {
            Intrinsics.checkParameterIsNotNull(pivot, "pivot");
            this.assertAxis(pivot, 1);
            this.transformer.pivotY = pivot;
            return this;
        }

        @NotNull
        public final CustomTransformerView.Builder setOverlapDistance(@Px int distance) {
            this.transformer.overlapDistance = (float)distance;
            return this;
        }

        @NotNull
        public final CustomTransformerView.Builder setElevation(@Px int maxElevation, @Px int minElevation) {
            this.transformer.maxElevation = (float)maxElevation;
            this.transformer.minElevation = (float)minElevation;
            return this;
        }

        @NotNull
        public final CustomTransformerView.Builder setElevation(@Px int maxElevation) {
            this.transformer.maxElevation = (float)maxElevation;
            this.transformer.minElevation = (float)0;
            return this;
        }

        @NotNull
        public final CustomTransformerView build() {
            this.transformer.maxMinDiffScale = this.maxScale - this.transformer.minScale;
            return this.transformer;
        }

        private final void assertAxis(Pivot pivot, int axis) {
            if (pivot.getAxis() != axis) {
                throw(new IllegalArgumentException("You passed a Pivot for wrong axis."));
            }
        }
    }
}
