package com.teamred.checkmate.ui.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.SilentAuthenticationCallback;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;
import com.microsoft.identity.client.exception.MsalUiRequiredException;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.CheckmateKey;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.databinding.FragmentCreatePostBinding;
import com.teamred.checkmate.util.MSGraphRequestWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePostFragment extends Fragment {

    FragmentCreatePostBinding binding;

    final String TAG = "CreatePostFragment";

    private String groupID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String selectedWebURL;
    private String selectedAppURL;

    /* Azure AD Variables */
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private IAccount mAccount;

    public CreatePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param groupID GroupID
     * @return A new instance of fragment CreatePostFragment.
     */
    public static CreatePostFragment newInstance(String groupID) {
        CreatePostFragment fragment = new CreatePostFragment();
        Bundle args = new Bundle();
        args.putString("groupID", groupID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupID = getArguments().getString("groupID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreatePostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String postTitle = binding.postTitle.getText().toString();
                final String subTopic = binding.subTopic.getText().toString();
                final List<String> tags = Arrays.asList(binding.tags.getText().toString().split(",").clone());
                final String content = binding.postContent.getText().toString();


                Map<String, Object> data = new HashMap<>();
                data.put("postTitle", postTitle);
                data.put("subTopic", subTopic);
                data.put("tags", tags);
                data.put("content", content);
                data.put("createdDate", Calendar.getInstance().getTime());
                data.put("author", Constant.getInstance().getCurrentUser().getUid());
                data.put("onenoteWebURL", selectedWebURL);
                data.put("onenoteAppURL", selectedAppURL);

                CollectionReference posts = db.collection(CheckmateKey.GROUP_FIREBASE).document(groupID).collection("posts");
                posts.add(data);

                getParentFragmentManager().popBackStack();
            }
        });

        // Creates a PublicClientApplication object with res/raw/auth_config_single_account.json
        PublicClientApplication.createSingleAccountPublicClientApplication(getContext(),
            R.raw.auth_config_single_account,
            new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                @Override
                public void onCreated(ISingleAccountPublicClientApplication application) {
                    /**
                     * This test app assumes that the app is only going to support one account.
                     * This requires "account_mode" : "SINGLE" in the config json file.
                     **/
                    mSingleAccountApp = application;
                    loadAccount();
                    Log.d(TAG, "onCreated: Loaded account");
                }

                @Override
                public void onError(MsalException exception) {
                    // Do nothing. Maybe display error?
                    Log.e(TAG, "onError: Error in loading account" + exception);
                }
            });

        binding.onenoteLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSingleAccountApp == null) {
                    return;
                }

                mSingleAccountApp.signIn(getActivity(), null, new String[] {"user.read", "notes.read", "notes.read.all"}, getAuthInteractiveCallback());
            }
        });

        binding.onenoteLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSingleAccountApp == null) {
                    return;
                }

                /**
                 * Removes the signed-in account and cached tokens from this app (or device, if the device is in shared mode).
                 */
                mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
                    @Override
                    public void onSignOut() {
                        mAccount = null;
                        updateUI();
                    }

                    @Override
                    public void onError(@NonNull MsalException exception) {
                    }
                });
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // Call OneNote API to get result.
                Log.d(TAG, "onQueryTextSubmit: " + s);
                final String resourceURL = String.format("https://graph.microsoft.com/v1.0//me/onenote/pages?$filter=contains(tolower(title),'%s')", s);
                mSingleAccountApp.acquireTokenSilentAsync(new String[] {"notes.read", "notes.read.all"}, mAccount.getAuthority(), getAuthSilentCallback(resourceURL));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        


        return view;
    }

    /**
     * Load the currently signed-in account, if there's any.
     */
    private void loadAccount() {
        if (mSingleAccountApp == null) {
            return;
        }

        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                // You can use the account data to update your UI or your app database.
                mAccount = activeAccount;
                Log.d(TAG, "onAccountLoaded: Account loaded");
                updateUI();
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
                    //showToastOnSignOut();
                }
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                // displayError(exception);
            }
        });
    }

    /**
     * Callback used in for silent acquireToken calls.
     */
    private SilentAuthenticationCallback getAuthSilentCallback(String resourceURL) {
        return new SilentAuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d(TAG, "Successfully authenticated");

                /* Successfully got a token, use it to call a protected resource - MSGraph */
                callGraphAPI(authenticationResult, resourceURL);
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
                //displayError(exception);

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                } else if (exception instanceof MsalUiRequiredException) {
                    /* Tokens expired or no session, retry with interactive */
                }
            }
        };
    }

    /**
     * Callback used for interactive request.
     * If succeeds we use the access token to call the Microsoft Graph.
     * Does not check cache.
     */
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Token: " + authenticationResult.getAccount().getClaims().get("id_token"));

                /* Update account */
                mAccount = authenticationResult.getAccount();
                updateUI();
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
                //displayError(exception);

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    /**
     * Updates UI based on the current account.
     */
    private void updateUI() {
        if (mAccount != null) {
            // We are logged in
            binding.signedInOnenoteTv.setText(mAccount.getUsername());
            binding.onenoteLoginBtn.setVisibility(View.GONE);
            binding.useralreadysignedinGroup.setVisibility(View.VISIBLE);
        } else {
            // We are logged out
            binding.onenoteLoginBtn.setVisibility(View.VISIBLE);
            binding.useralreadysignedinGroup.setVisibility(View.GONE);
        }
    }

    /**
     * Make an HTTP request to obtain MSGraph data
     */
    private void callGraphAPI(final IAuthenticationResult authenticationResult, String resourceURL) {

        MSGraphRequestWrapper.callGraphAPIUsingVolley(
                getContext(),
                resourceURL,
                authenticationResult.getAccessToken(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /* Successfully called graph, process data and send to UI */
                        Log.d(TAG, "Response: " + response.toString());
                        try {
                            JSONObject firstResult = response.getJSONArray("value").getJSONObject(0);
                            String title = firstResult.getString("title");
                            String clientUrl = firstResult.getJSONObject("links").getJSONObject("oneNoteClientUrl").getString("href");
                            String webUrl = firstResult.getJSONObject("links").getJSONObject("oneNoteWebUrl").getString("href");
                            binding.selectedOnenotePageTitleTV.setText(title);

                            selectedAppURL = clientUrl;
                            selectedWebURL = webUrl;
                            Log.d(TAG, "onResponse: " + title + " " + webUrl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.toString());
                        //displayError(error);
                    }
                });
    }
}