package com.okason.prontoshop.ui.checkout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.okason.prontoshop.R;
import com.okason.prontoshop.core.listeners.CartActionsListener;
import com.okason.prontoshop.models.LineItem;
import com.okason.prontoshop.util.Formatter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Valentine on 4/10/2016.
 */
public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private List<LineItem> mLineItems;
    private CartActionsListener mListener;
    private Activity mActivity;


    public CheckoutAdapter(List<LineItem> lineitems, Context context, CartActionsListener listener){
        mLineItems = lineitems;
        mActivity = (Activity) context;
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shopping_cart_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mLineItems != null){
            try {
                final LineItem lineitem = mLineItems.get(position);
                Picasso.with(mActivity)
                        .load(lineitem.getImagePath())
                        .fit()
                        .placeholder(R.drawable.default_image)
                        .into(holder.lineitemImage);
                holder.lineitemName.setText(lineitem.getProductName());
                holder.lineitemPrice.setText(Formatter.formatCurrency(lineitem.getSalePrice()));
                holder.qtyEditText.setText(String.valueOf(lineitem.getQuantity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mLineItems != null){
            return mLineItems.size();
        }else {
            return 0;
        }
    }

    public void replaceData(List<LineItem> lineitems){
        mLineItems = lineitems;
        notifyDataSetChanged();
    }

    public void clearData(){
        int size = this.mLineItems.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mLineItems.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.product_image) ImageView lineitemImage;
        @BindView(R.id.text_view_product_name) TextView lineitemName;
        @BindView(R.id.text_view_price) TextView lineitemPrice;
        @BindView(R.id.edit_text_qty)EditText qtyEditText;
        @BindView(R.id.button_delete) Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            deleteButton.setOnClickListener(this);
            qtyEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LineItem item = mLineItems.get(getLayoutPosition());
                    updateQtyDialog(item);
                }
            });

        }

        @Override
        public void onClick(View v) {
           LineItem selectedLineItem = mLineItems.get(getLayoutPosition());
           mListener.onItemDeleted(selectedLineItem);
        }
    }



    private void updateQtyDialog(final LineItem item){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();

        View rootView = inflater.inflate(R.layout.dialog_enter_item_qty, null);
        dialog.setView(rootView);

        View titleView = inflater.inflate(R.layout.dialog_title, null);
        TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
        titleText.setText(item.getProductName());
        dialog.setCustomTitle(titleView);

        final EditText qtyEditText = (EditText)rootView.findViewById(R.id.edit_text_item_qty);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!qtyEditText.getText().toString().isEmpty()) {
                    int qtyEntered = Integer.parseInt(qtyEditText.getText().toString());
                    mListener.onItemQtyChange(item, qtyEntered);
                } else {
                    Toast.makeText(mActivity, "Invalid Qty", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();



    }





}
