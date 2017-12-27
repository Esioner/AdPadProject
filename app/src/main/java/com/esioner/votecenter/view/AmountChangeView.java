package com.esioner.votecenter.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esioner.votecenter.R;

/**
 * @author Esioner
 * @date 2017/12/27
 */

public class AmountChangeView extends LinearLayout implements View.OnClickListener {

    private Button btnIncrease;
    private Button btnDecrease;
    private TextView tvAmount;
    private int amount = 0;
    private OnAmountChangeListener amountChangeListener;

    public AmountChangeView(Context context) {
        this(context, null);
    }

    public AmountChangeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AmountChangeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.amount_view_layout, this);

        tvAmount = findViewById(R.id.tv_amount);
        btnDecrease = findViewById(R.id.btn_decrease);
        btnIncrease = findViewById(R.id.btn_increase);

        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);

        TypedArray obtainAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView);
        int btnWidth = obtainAttrs.getDimensionPixelSize(R.styleable.AmountView_btnWidth, LayoutParams.WRAP_CONTENT);
        int tvWidth = obtainAttrs.getDimensionPixelSize(R.styleable.AmountView_tvWidth, 80);
        int tvTextSize = obtainAttrs.getDimensionPixelSize(R.styleable.AmountView_tvTextSize, 12);
        int btnTextSize = obtainAttrs.getDimensionPixelSize(R.styleable.AmountView_btnTextSize, 12);
        obtainAttrs.recycle();

        LayoutParams btnParms = new LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
        btnDecrease.setLayoutParams(btnParms);
        btnIncrease.setLayoutParams(btnParms);
        if (btnTextSize != 0) {
            btnIncrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
            btnDecrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
        }

        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
        tvAmount.setLayoutParams(textParams);
        if (tvTextSize != 0) {
            tvAmount.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvTextSize);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_decrease:
                if (amount > 1) {
                    amount--;
                    tvAmount.setText(amount+"");
                }
                break;
            case R.id.btn_increase:
                amount++;
                tvAmount.setText(amount+"");
                break;
            default:
        }
        if (amountChangeListener != null) {
            amountChangeListener.onAmountChange(this, amount);
        }

    }

    public int getAmount() {
        return amount;
    }

    public void setOnAmountChangeListener(OnAmountChangeListener listener) {
        this.amountChangeListener = listener;
    }


    public interface OnAmountChangeListener {
        void onAmountChange(View view, int amount);
    }
}
