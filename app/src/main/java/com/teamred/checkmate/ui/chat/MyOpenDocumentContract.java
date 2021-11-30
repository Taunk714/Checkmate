package com.teamred.checkmate.ui.chat;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

public class MyOpenDocumentContract extends ActivityResultContracts.OpenDocument {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, @NonNull String[] input) {
        Intent intent = super.createIntent(context, input);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        return intent;
    }
}
