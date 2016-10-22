package com.okason.prontoshop.ui.customers;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.okason.prontoshop.R;
import com.okason.prontoshop.core.listeners.OnCustomerSelectedListener;
import com.okason.prontoshop.models.Customer;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Valentine on 4/7/2016.
 */
public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {

    private List<Customer> mCustomerList;
    private final OnCustomerSelectedListener mListener;
    private Context mContext;
    private boolean shouldHighLightSelectedRow = false;
    private int selectedPosition = 0;

    public CustomerListAdapter(List<Customer> attendants, OnCustomerSelectedListener listener, Context context){
        mCustomerList = attendants;
        mListener = listener;
        mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Customer selectedCustomer = mCustomerList.get(position);

        holder.customerName.setText(selectedCustomer.getCustomerName());
        holder.customerEmail.setText(selectedCustomer.getEmailAddress());
        if (selectedCustomer.getProfileImagePath().isEmpty()){
            selectedCustomer.setProfileImagePath("empty");
        }
        Picasso.with(mContext)
                .load(selectedCustomer.getProfileImagePath())
                .fit()
                .placeholder(R.drawable.profile_icon)
                .into(holder.customerHeadShot);
        if (shouldHighLightSelectedRow){
            if (selectedPosition == position){
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary_light));
            }else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        if (mCustomerList != null) {
            return mCustomerList.size();
        } else {
            return 0;
        }
    }

    public void replaceData(List<Customer> customers){
        mCustomerList = checkNotNull(customers);
        shouldHighLightSelectedRow = false;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        @BindView(R.id.image_view_customer_head_shot)ImageView customerHeadShot;
        @BindView(R.id.text_view_customers_name) TextView customerName;
        @BindView(R.id.text_view_customers_email) TextView customerEmail;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            shouldHighLightSelectedRow = true;
            selectedPosition = getLayoutPosition();
            Customer selectedCustomer = mCustomerList.get(selectedPosition);
            mListener.onSelectCustomer(selectedCustomer);
            notifyDataSetChanged();
        }

        @Override
        public boolean onLongClick(View v) {
            Customer selectedCustomer = mCustomerList.get(getLayoutPosition());
            mListener.onLongClickProduct(selectedCustomer);
            return true;
        }
    }







}
