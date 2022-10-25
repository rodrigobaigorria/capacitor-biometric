package com.tech.plugin.biometric;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import java.util.concurrent.Executor;


public class Prompt extends AppCompatActivity {

  private static final String TAG = "Prompt";
  private Integer mCurrentMode;
  private String mToEncrypt;
  public String key;
  public String password;
  public String tipo;
  public int intentos = 1;





  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle extras = getIntent().getExtras();
    key = extras.getString("key");
    mCurrentMode = extras.getInt("mode");
    mToEncrypt = extras.getString("pass");
    tipo = extras.getString("type");




    Executor executor;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
      executor = Prompt.this.getMainExecutor();
    } else {
      final Handler handler = new Handler(Looper.getMainLooper());
      executor = new Executor() {
        @Override
        public void execute(@NonNull Runnable command) {
          handler.post(command);
        }
      };
    }


    BiometricPrompt biometricPrompt = new BiometricPrompt(Prompt.this, executor, new BiometricPrompt.AuthenticationCallback() {
      @Override
      public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);

          onAuthenticated(false);
          finishActivity("ERROR");
      }

      @Override
      public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);


        Log.d("resultado", String.valueOf(result));
        onAuthenticated(true);

      }

      @Override
      public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        if (intentos < 3) {
          intentos +=1;
          Log.d("intentos", String.valueOf(intentos));

        }else{
          finishActivity("ERROR");

          finish();
        }

      }
    });
    BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
      .setTitle("Autenticación biométrica")
      //.setSubtitle("Set the subtitle to display.")
      //.setDescription("Set the description to display")
      .setNegativeButtonText("Cancelar")
      //.setDeviceCredentialAllowed(true)
      .setConfirmationRequired(false)
      .build();

    biometricPrompt.authenticate(promptInfo);

  }


  public  void onAuthenticated(boolean withFingerprint) {
    String result = "";
    String errorMessage = "";

    try {

      if (withFingerprint) {

        SharedPreferences sharedPref = getSharedPreferences("TechBio",Context.MODE_PRIVATE);
        if(mCurrentMode == 2){
          String enc = sharedPref.getString(key, null);
          Log.d("Face", "string de shared -> " + enc);
          result = enc;
          finishActivity("success");


        } else if (mCurrentMode == 1){
          SharedPreferences.Editor editor = sharedPref.edit();
          editor.putString(key, mToEncrypt);
          editor.commit();
          mToEncrypt = "";
          result = "success";
          finishActivity("success");

        }
      }
    } catch (Exception e) {
      errorMessage = "Failed to encrypt the data with the generated key: " +
        "IllegalBlockSizeException: " + e.getMessage();
      Log.e("error en autenticate", errorMessage);
    }
    // MiPlugin succes = new MiPlugin();
    /*if (result != "") {
      mCallbackContext.success(result);
      mPluginResult = new PluginResult(PluginResult.Status.OK);
      finish();
    } else {
      mCallbackContext.error(errorMessage);
      mPluginResult = new PluginResult(PluginResult.Status.ERROR);
      finish();
    }
    mCallbackContext.sendPluginResult(mPluginResult);*/
  }
  void finishActivity(String result) {
    Intent intent = new Intent();
    intent.putExtra("result", result);
    setResult(RESULT_OK, intent);
    finish();
  }
}

