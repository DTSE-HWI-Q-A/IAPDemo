package com.example.iapexample.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.iapexample.R;
import com.example.iapexample.utilities.Constants;
import com.example.iapexample.utilities.IapApiCallback;
import com.example.iapexample.utilities.IapRequestHelper;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.IsEnvReadyResult;
import com.huawei.hms.iap.entity.OrderStatusCode;
import com.huawei.hms.iap.util.IapClientHelper;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.api.client.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ADX2099MainActivity";
    private Button enterConsumablesTheme;
    public static final int IS_LOG = 1;
    //login
    public static final int REQUEST_SIGN_IN_LOGIN = 1002;
    //login by code
    public static final int REQUEST_SIGN_IN_LOGIN_CODE = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryIsReady();
    }

    private void queryIsReady() {
        IapClient mClient = Iap.getIapClient(this);
        IapRequestHelper.isEnvReady(mClient, new IapApiCallback<IsEnvReadyResult>() {

            @Override
            public void onSuccess(IsEnvReadyResult result) {
                int accountFlag = result.getReturnCode();
                Log.d(TAG,"account Flag " + accountFlag);
                initView();
            }

            @Override
            public void onFail(Exception e) {
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    Status status = apiException.getStatus();
                    if (status.getStatusCode() == OrderStatusCode.ORDER_HWID_NOT_LOGIN) {
                        // HUAWEI ID is not signed in.
                        Log.d(TAG, "HUAWEI ID is not signed in.");
                        if (status.hasResolution()) {
                            try {
                                // 6666 is a constant defined by yourself.
                                // Open the sign-in screen returned.
                                status.startResolutionForResult(MainActivity.this, 6666);
                            } catch (IntentSender.SendIntentException exp) {
                            }
                        }
                    } else if (status.getStatusCode() == OrderStatusCode.ORDER_ACCOUNT_AREA_NOT_SUPPORTED) {
                        // The current location does not support HUAWEI IAP.
                    }
                } else {
                    // Other external errors.
                }
            }
        });

    }

    private void initView() {
        setContentView(R.layout.activity_main);
        enterConsumablesTheme = (Button) findViewById(R.id.enter_consumables_scene);
        enterConsumablesTheme.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.enter_consumables_scene:
                intent = new Intent(this, ConsumptionActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_IN_LOGIN) {
            //login success
            //get user message by parseAuthResultFromIntent
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                AuthAccount authAccount = authAccountTask.getResult();
                Log.i(TAG, authAccount.getDisplayName() + " signIn success ");
                Log.i(TAG, "AccessToken: " + authAccount.getAccessToken());
            } else {
                Log.i(TAG, "signIn failed: " + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }

        if (requestCode == Constants.REQ_CODE_LOGIN) {
            int returnCode = IapClientHelper.parseRespCodeFromIntent(data);
            Log.i(TAG,"onActivityResult, returnCode: " + returnCode);
            if (returnCode == OrderStatusCode.ORDER_STATE_SUCCESS) {
                initView();
            } else if(returnCode == OrderStatusCode.ORDER_ACCOUNT_AREA_NOT_SUPPORTED) {
                Toast.makeText(MainActivity.this, "This is unavailable in your country/region.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "User cancel login.", Toast.LENGTH_LONG).show();
            }
            return;
        }

        if (requestCode == REQUEST_SIGN_IN_LOGIN_CODE) {
            //login success
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                AuthAccount authAccount = authAccountTask.getResult();
                Log.i(TAG, "signIn get code success.");
                Log.i(TAG, "ServerAuthCode: " + authAccount.getAuthorizationCode());

                /**** english doc:For security reasons, the operation of changing the code to an AT must be performed on your server. The code is only an example and cannot be run. ****/
                /**********************************************************************************************/
            } else {
                Log.i(TAG, "signIn get code failed: " + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }

        if (requestCode == 6666) {
            if (data != null) {
                // Call the parseRespCodeFromIntent method to obtain the result of the API request.
                //int returnCode = IapClientHelper.parseRespCodeFromIntent(data);
                // Call the parseAccountFlagFromIntent method to obtain the account type returned by the API.
                //int accountFlag = IapClientHelper.parseAccountFlagFromIntent(data);
            }
        }

    }

}