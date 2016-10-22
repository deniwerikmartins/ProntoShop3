package com.okason.prontoshop.ui.transaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.okason.prontoshop.R;
import com.okason.prontoshop.core.listeners.OnTransactionSelectedListener;
import com.okason.prontoshop.models.SalesTransaction;
import com.okason.prontoshop.util.Formatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Valentine on 4/24/2016.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<SalesTransaction> mTransactions;
    private OnTransactionSelectedListener mListener;



    public TransactionAdapter(List<SalesTransaction> transactions, OnTransactionSelectedListener listener){
        mTransactions = transactions;
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mTransactions != null){
            SalesTransaction selectedTransaction = mTransactions.get(position);
            holder.paidCheckbox.setChecked(selectedTransaction.isPaid() ? true : false);
            holder.customerName.setText(mListener.getCustomer(selectedTransaction.getId()).getCustomerName());
            holder.paymentMethod.setText(selectedTransaction.getPaymentType());
            holder.dateOfTransaction.setText(Formatter.formatDate(selectedTransaction.getTransactionDate()));
            holder.totalAmount.setText(Formatter.formatCurrency(selectedTransaction.getTotalAmount()));
        }
    }

    @Override
    public int getItemCount() {
        if (mTransactions != null){
            return mTransactions.size();
        }else {
            return 0;
        }
    }

    public void replaceData(List<SalesTransaction> transactions){
        mTransactions = transactions;
        notifyDataSetChanged();
    }

    public void clearData(){
        int size = this.mTransactions.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mTransactions.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        @BindView(R.id.checkout_paid_status)  CheckBox paidCheckbox;
        @BindView(R.id.text_view_customers_name) TextView customerName;
        @BindView(R.id.text_view_payment_method) TextView paymentMethod;
        @BindView(R.id.text_view_total_amount) TextView totalAmount;
        @BindView(R.id.text_view_date_of_transaction) TextView dateOfTransaction;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SalesTransaction selectedTransaction = mTransactions.get(getLayoutPosition());
            mListener.onSelectTransaction(selectedTransaction);
        }
    }





}
