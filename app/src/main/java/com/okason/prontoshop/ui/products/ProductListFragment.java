package com.okason.prontoshop.ui.products;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.okason.prontoshop.R;
import com.okason.prontoshop.core.listeners.OnProductSelectedListener;
import com.okason.prontoshop.models.Product;
import com.okason.prontoshop.ui.addProduct.AddProductDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends Fragment
        implements ProductListContract.View,
        OnProductSelectedListener {

    private View mRootView;
    private ProductListAdapter mAdapter;
    private ProductListContract.Actions mProductPresenter;
    private AddProductDialogFragment mAddProductDialogFragment;



    @BindView(R.id.product_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.empty_text) TextView mEmptyTextView;
    @BindView(R.id.fab) FloatingActionButton mFab;


    public ProductListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_products_list, container, false);
        ButterKnife.bind(this, mRootView);
        mProductPresenter = new ProductListPresenter(this);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductPresenter.onAddProductButtonClicked();
            }
        });


        //setup RecyclerView
        List<Product> tempProducts = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ProductListAdapter(tempProducts, getContext(), this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mProductPresenter.loadProducts();
    }


    @Override
    public void showProducts(List<Product> products) {
        mAdapter.replaceData(products);

    }


    @Override
    public void showAddProductForm() {
        mAddProductDialogFragment = AddProductDialogFragment.newInstance(0);
        mAddProductDialogFragment.show(getActivity().getFragmentManager(), "Dialog");

    }

    @Override
    public void showEditProductForm(Product product) {
        AddProductDialogFragment dialog = AddProductDialogFragment.newInstance(product.getId());
        dialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    @Override
    public void showDeleteProductPrompt(final Product product) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View titleView = (View)inflater.inflate(R.layout.dialog_title, null);
        TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
        titleText.setText("Delete Product?");
        alertDialog.setCustomTitle(titleView);

        alertDialog.setMessage("Delete " + product.getProductName());
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProductPresenter.deleteProduct(product);
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void showGoogleSearch(Product product) {
        Uri uri = Uri.parse("http://www.google.com/#q=" + product.getProductName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void showEmptyText() {
        mEmptyTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
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


    private void showToastMessage(String message){
        Snackbar.make(mRootView.getRootView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSelectProduct(Product selectedProduct) {
        mProductPresenter.onAddToCartButtonClicked(selectedProduct);
    }

    @Override
    public void onLongClickProduct(Product clickedProduct) {
        showProductContextMenu(clickedProduct);
    }

    private void showProductContextMenu(final Product clickedProduct) {
        final  String[] sortOptions = { getString(R.string.edit), getString(R.string.delete), getString(R.string.add_to_cart), getString(R.string.google_search)};

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);

        View titleView = (View)inflater.inflate(R.layout.dialog_title, null);
        TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
        if (clickedProduct.getProductName() != null) {
            titleText.setText(clickedProduct.getProductName());
        }
        alertDialog.setCustomTitle(titleView);

        ListView dialogList = (ListView) convertView.findViewById(R.id.dialog_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(),android.R.layout.simple_list_item_1, sortOptions);
        dialogList.setAdapter(adapter);

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final Dialog dialog = alertDialog.create();
        dialog.show();
        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        mProductPresenter.onEditProductButtonClicked(clickedProduct);
                        dialog.dismiss();
                        break;
                    case 1:
                        mProductPresenter.onDeleteProductButtonClicked(clickedProduct);
                        dialog.dismiss();
                        break;
                    case 2:
                        mProductPresenter.onAddToCartButtonClicked(clickedProduct);
                        dialog.dismiss();
                        break;
                    case 3:
                        mProductPresenter.onGoogleSearchButtonClicked(clickedProduct);
                        dialog.dismiss();
                        break;
                }
            }
        });

    }
}
