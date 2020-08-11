package com.shapor.loftmoney;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class BalanceView extends View {

    private float expenses = 5000;
    private float incomes = 10000;

    private int colorStateExpense;
    private int colorStateIncome;

    private int exp = ContextCompat.getColor(getContext(), R.color.expenseColor);
    private int inc = ContextCompat.getColor(getContext(), R.color.incomeColor);

    private Paint expensePaint = new Paint();
    private Paint incomePaint = new Paint();



    public BalanceView(Context context) {
        super(context);

       // init();
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BalanceView);
        int expColor = a.getColor(R.styleable.BalanceView_colorStateExpenses, ContextCompat.getColor(getContext(), R.color.expenseColor));
        int intColor = a.getColor(R.styleable.BalanceView_colorStateIncome, ContextCompat.getColor(getContext(), R.color.incomeColor));
        initColorState(expColor, intColor);
        a.recycle();
        init();
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BalanceView);
        int expColor = a.getColor(R.styleable.BalanceView_colorStateExpenses, ContextCompat.getColor(getContext(), R.color.expenseColor));
        int intColor = a.getColor(R.styleable.BalanceView_colorStateIncome, ContextCompat.getColor(getContext(), R.color.incomeColor));
        initColorState(expColor, intColor);
        a.recycle();
        init();
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BalanceView);
        int expColor = a.getColor(R.styleable.BalanceView_colorStateExpenses, ContextCompat.getColor(getContext(), R.color.expenseColor));
        int intColor = a.getColor(R.styleable.BalanceView_colorStateIncome, ContextCompat.getColor(getContext(), R.color.incomeColor));
        initColorState(expColor, intColor);
        init();
        a.recycle();
        //init();
    }

    public void update(float expenses, float incomes) {
        this.expenses = expenses;
        this.incomes = incomes;
        invalidate();
    }


    private void initColorState(int expenseColor, int incomeColor) {
        this.colorStateExpense = expenseColor;
        this.colorStateIncome = incomeColor;
    }

    private void init() {
        expensePaint.setColor(colorStateExpense);
        incomePaint.setColor(colorStateIncome);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float total = expenses + incomes;

        float expenseAngle = 360f * expenses / total;
        float incomesAngle = 360f * incomes / total;

        int space = 15;
        int size = Math.min(getWidth(), getHeight()) - space * 2;
        int xMargin = (getWidth() - size) / 2;
        int yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - space, yMargin,
                getWidth() - xMargin - space,
                getHeight() - yMargin, 180 - expenseAngle / 2,
                expenseAngle, true, expensePaint);

        canvas.drawArc(xMargin + space, yMargin,
                getWidth() - xMargin + space,
                getHeight() - yMargin, 360 - incomesAngle / 2,
                incomesAngle, true, incomePaint);
    }
}
