package com.teamred.checkmate.ui.search;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.teamred.checkmate.R;

public class FilterDialogFragment extends DialogFragment {

    private String[] groupStatus = new String[]{"Active", "Inactive"};
    private boolean[] groupStatusSelect = new boolean[]{true, true};

    private FilterDialogListener clickListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title).setMultiChoiceItems(groupStatus, groupStatusSelect, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                groupStatusSelect[which] = isChecked;
            }
        })
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        clickListener.onDialogPositiveClick(FilterDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        clickListener.onDialogNegativeClick(FilterDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        clickListener = (FilterDialogListener) context;
    }

    public String[] getGroupStatus() {
        return groupStatus;
    }

    public boolean[] getGroupStatusSelect() {
        return groupStatusSelect;
    }

    public void setGroupStatusSelect(boolean[] groupStatusSelect) {
        this.groupStatusSelect = groupStatusSelect;
    }

    public interface FilterDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);

    }
}