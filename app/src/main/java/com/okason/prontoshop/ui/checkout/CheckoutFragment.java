package com.okason.prontoshop.ui.checkout;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.okason.prontoshop.R;
import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.core.listeners.CartActionsListener;
import com.okason.prontoshop.models.LineItem;
import com.okason.prontoshop.util.Formatter;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutFragment extends Fragment implements
        CheckoutContract.View, CartActionsListener {

    private View mRootView;
    private CheckoutAdapter mAdapter;
    private CheckoutContract.Actions mCartPresenter;




    @BindView(R.id.cart_list_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.empty_text) TextView mEmptyTextView;
    @BindView(R.id.clear_cart_button) Button mClearButton;
    @BindView(R.id.check_out_button) Button mCheckoutButton;
    @BindView(R.id.text_view_sub_total) TextView mSubTotalTextView;
    @BindView(R.id.text_view_total_amount) TextView mTotalTextView;
    @BindView(R.id.text_view_tax_rate) TextView mTotalTaxRate;
    @BindView(R.id.text_view_tax_value) TextView mTotalTaxValue;
    @BindView(R.id.radio_group_payment_type)     RadioGroup mRadioGroup;

    private Bus mBus;



    public CheckoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        ButterKnife.bind(this, mRootView);

        

        List<LineItem> tempProducts = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new CheckoutAdapter(tempProducts, getContext(), this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mBus = ProntoShopApplication.getInstance().getBus();

        mCartPresenter = new CheckoutPresenter(this);

        return mRootView;
    }

    @Override
    public void onResume() {
        mCartPresenter.loadLineItems();
        super.onResume();
        try {
            mBus.register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBus != null){
            try {
                mBus.unregister(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnCheckedChanged(R.id.button_cash) void onCheckedCash(boolean checked){
        if (checked){
            mCartPresenter.setPaymentType(getString(R.string.payment_type_cash));
            mCartPresenter.markAsPaid(false);
        }
    }

    @OnCheckedChanged(R.id.button_card) void onCheckedCard(boolean checked){
        if (checked){
            mCartPresenter.setPaymentType(getString(R.string.payment_type_card));
            mCartPresenter.markAsPaid(true);
        }
    }

    @OnCheckedChanged(R.id.button_paypal) void onCheckedPaypal(boolean checked){
        if (checked){
            mCartPresenter.setPaymentType(getString(R.string.payment_type_paypal));
            mCartPresenter.markAsPaid(true);
        }
    }


    @OnClick(R.id.check_out_button)
    public void onClickCheckoutButton(View view){
        mCartPresenter.onCheckoutButtonClicked();
    }

    @OnClick(R.id.clear_cart_button)
    public void onClickClearCart(View view){
        mCartPresenter.onClearButtonClicked();
    }

    @Override
    public void showLineItem(List<LineItem> lineItems) {
        mAdapter.replaceData(lineItems);
    }

    @Override
    public void showEmptyText() {
        mEmptyTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showCartTotals(double tax, double subtotal, double total) {
        mTotalTextView.setText(Formatter.formatCurrency(total));
        mSubTotalTextView.setText(Formatter.formatCurrency(subtotal));
        mTotalTaxValue.setText(Formatter.formatCurrency(tax));
    }


    @Override
    public void showConfirmCheckout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View titleView = (View)inflater.inflate(R.layout.dialog_title, null);
        TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
        titleText.setText("Complete Checkout?");
        dialog.setCustomTitle(titleView);

        dialog.setMessage("Are you ready to complete Checkout");
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCartPresenter.checkout();

            }
        });
        dialog.show();

    }

    @Override
    public void showConfirmClearCart() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View titleView = (View)inflater.inflate(R.layout.dialog_title, null);
        TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
        titleText.setText("Clear Shopping Cart?");
        dialog.setCustomTitle(titleView);

        dialog.setMessage("Are you ready to clear all items from shopping checkout?");
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCartPresenter.clearShoppingCart();
            }
        });
        dialog.show();

    }

    @Override
    public void hideEmptyText() {
        mEmptyTextView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

    }


    @Override
    public void showMessage(String message) {
        showToastMessage(message);
    }



    @Override
    public void onItemDeleted(LineItem item) {
        mCartPresenter.onDeleteItemButtonClicked(item);
    }

    @Override
    public void onItemQtyChange(LineItem item, int qty) {
        mCartPresenter.onItemQuantityChanged(item, qty);
    }

    private void showToastMessage(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
