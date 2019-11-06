package com.websarva.wings.android.kakeibo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class DialogFragment extends android.support.v4.app.DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("タイトル")
                .setMessage("削除しますか")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK button pressed

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }

    @Override
    public void onPause() {
        super.onPause();

        // onPause でダイアログを閉じる場合
        dismiss();
    }
}
