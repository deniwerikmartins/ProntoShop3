package com.okason.prontoshop.ui.customers;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.okason.prontoshop.core.listeners.OnCustomerSelectedListener;
import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.ui.addCustomer.AddCustomerDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerListFragment extends Fragment implements
        CustomerListContract.View, OnCustomerSelectedListener{

    private View mRootView;
    private CustomerListAdapter mAdapter;
    private CustomerListContract.Actions mCustomerPresenter;


    @BindView(R.id.customer_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.empty_text) TextView mEmptyTextView;
    @BindView(R.id.fab ) FloatingActionButton mFab;


    public CustomerListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_customer_list, container, false);
        ButterKnife.bind(this, mRootView);
        mCustomerPresenter = new CustomerListPresenter(this);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomerPresenter.onAddCustomerButtonClicked();
            }
        });

        //setup RecyclerView
        List<Customer> tempCustomers = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new CustomerListAdapter(tempCustomers, this, getContext() );
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCustomerPresenter.loadCustomers();
    }

    @Override
    public void showCustomers(List<Customer> customers) {
        mAdapter.replaceData(customers);
    }

    @Override
    public void showAddCustomerForm() {
        AddCustomerDialogFragment mAddCustomerFragment = new AddCustomerDialogFragment();
        mAddCustomerFragment.show(getActivity().getFragmentManager(), "Dialog");

    }

    @Override
    public void showDeleteCustomerPrompt(final Customer customer) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Delete Customer?");
        dialog.setMessage("Are you delete " + customer.getCustomerName() + " ?");
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCustomerPresenter.deleteCustomer(customer);
            }
        });
        dialog.show();
    }

    @Override
    public void showEditCustomerForm(Customer customer) {
        AddCustomerDialogFragment fragment = AddCustomerDialogFragment.newInstance(customer.getId());
        fragment.show(getActivity().getFragmentManager(), "Dialog");
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
    public void onSelectCustomer(Customer customer) {
        mCustomerPresenter.onCustomerSelected(customer);
    }

    @Override
    public void onLongClickProduct(Customer clickedCustomer) {
        showCustomerContextMenu(clickedCustomer);
    }

    private void showCustomerContextMenu(final Customer clickedCustomer) {
        final  String[] sortOptions = { getString(R.string.edit), getString(R.string.delete), getString(R.string.select_customer)};

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);

        View titleView = (View)inflater.inflate(R.layout.dialog_title, null);
        TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
        if (clickedCustomer.getCustomerName() != null) {
            titleText.setText(clickedCustomer.getCustomerName());
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
                        mCustomerPresenter.onEditCustomerButtonClicked(clickedCustomer);
                        dialog.dismiss();
                        break;
                    case 1:
                        mCustomerPresenter.onDeleteCustomerButtonClicked(clickedCustomer);
                        dialog.dismiss();
                        break;
                    case 2:
                        mCustomerPresenter.onCustomerSelected(clickedCustomer);
                        dialog.dismiss();
                        break;
                }
            }
        });

    }
}
