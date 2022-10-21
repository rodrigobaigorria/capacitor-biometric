package com.tech.plugin.biometric;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.biometric.BiometricManager;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.PluginResult;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.util.Arrays;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;

@CapacitorPlugin(name = "Biometria")
public class BiometriaPlugin extends Plugin {

  public BiometricManager biometricManager;


  private Biometria implementation = new Biometria();
  private static final String DIALOG_FRAGMENT_TAG = "FpAuthDialog";

  KeyguardManager mKeyguardManager;
  private FingerprintManager mFingerPrintManager;
  private static int mCurrentMode;
  public static PluginCall mCallbackContext;
  private String mToEncrypt;
  public String key;
  public String password;
  public String tipo;
  public int intentos = 1;
  PluginCall call;
  private Executor executor;

  public static PluginResult mPluginResult;

  @PluginMethod
  public void has(PluginCall call) {
    String key = call.getString("key");

    JSObject ret = new JSObject();
    /*ret.put("value", implementation.has(value));
    call.resolve(ret);*/

    SharedPreferences sharedPref = getActivity().getSharedPreferences("TechBio", Context.MODE_PRIVATE);
    String enc = sharedPref.getString(key, null);
    if (!"".equals(enc) && enc != null) {
      ret.put("value", implementation.has(key));
      call.resolve(ret);
    } else {
      ret.put("value", implementation.has("NO"));
      call.resolve(ret);
    }
  }

    @PluginMethod
  public void isAvailable(PluginCall call) {
    String value = call.getString("value");

    JSObject ret = new JSObject();


    if (Build.VERSION.SDK_INT > 30) {

      PackageManager packageManager = getContext().getPackageManager();
      try {
        biometricManager = BiometricManager.from(getContext());
        String data = "";
        switch (biometricManager.canAuthenticate()) {
          case BiometricManager.BIOMETRIC_SUCCESS:
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_FACE))
              data = "Face";
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_IRIS))
              data = "Iris";
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT))
              data = "Finger";

            ret.put("value", implementation.isAvailable(data));
            call.resolve(ret);
            break;
          case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            ret.put("value", implementation.isAvailable("NO"));
            call.resolve(ret);

            break;
          case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            ret.put("value", implementation.isAvailable("NO tiene hardware"));
            call.resolve(ret);
            break;
          case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
            ret.put("value", implementation.isAvailable("NO esta enrrolado"));
            call.resolve(ret);
            break;
        }
      } catch (Exception e) {
        ret.put("value", implementation.isAvailable("error"));
        call.resolve(ret);
      }
    } else {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        mFingerPrintManager =
          getActivity().getApplicationContext().getSystemService(FingerprintManager.class);
        if (mFingerPrintManager.isHardwareDetected()) {
          biometricManager = BiometricManager.from(getContext());
          switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
              ret.put("value", implementation.isAvailable("Finger"));
              call.resolve(ret);
              Log.d("intentos", String.valueOf(BiometricManager.BIOMETRIC_SUCCESS));

              break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
              ret.put("value", implementation.isAvailable("NO esta enrolado"));
              call.resolve(ret);
              break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
              ret.put("value", implementation.isAvailable("NO tiene hardware"));
              call.resolve(ret);
              break;
          }
        } else {
          ret.put("value", implementation.isAvailable("No"));
          call.resolve(ret);
        }
      }
    }
  }

  @PluginMethod
  public void verify(PluginCall call) {
    key = call.getString("key");
    mToEncrypt  = call.getString("message");

   Intent intent = new Intent(this.getActivity().getBaseContext(), com.tech.plugin.biometric.Prompt.class);
    intent.putExtra("key", key);
    intent.putExtra("mode", Cipher.DECRYPT_MODE);
    intent.putExtra("type", "verify");
    saveCall(call);
    startActivityForResult(call, intent, "verifyResult");

  }

  @PluginMethod
  public void save(PluginCall call) {

      String key = call.getString("key");
      String password = call.getString("password");

      Intent intent = new Intent(this.getActivity().getBaseContext(), com.tech.plugin.biometric.Prompt.class);
      intent.putExtra("key", key);
      intent.putExtra("mode", Cipher.ENCRYPT_MODE);
      intent.putExtra("pass", password);
      intent.putExtra("type", "save");
    saveCall(call);
    startActivityForResult(call, intent, "verifyResult");


  }

  @PluginMethod
  public void getUser(PluginCall call) {
    JSObject ret = new JSObject();

    SharedPreferences sharedPref = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
    String enc = sharedPref.getString("name", null);
    if (!"".equals(enc) && enc != null) {
      ret.put("value", implementation.getUser(enc));
      call.resolve(ret);
    } else {
      ret.put("value", implementation.getUser(null));
      call.resolve(ret);
    }
  }

  @PluginMethod
  public void saveUser(PluginCall call) {
    Log.d("datos", String.valueOf(call.getString("name")));

    String name = call.getString("name");

      JSObject ret = new JSObject();
      try {
        SharedPreferences sharedPref = getContext().getSharedPreferences("User",Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPref.edit();
      editor.putString("name", name);
      editor.commit();

      ret.put("value", implementation.getUser("OK"));
      call.resolve(ret);

      } catch (Exception e) {

        ret.put("value", implementation.getUser("error"));
      call.resolve(ret);
      }
  }
  @PluginMethod
  public void saveDataUser(PluginCall call) {
    Log.d("datos", String.valueOf(call.getString("dataUser")));

    String name = call.getString("dataUser");

    JSObject ret = new JSObject();
    try {
      SharedPreferences sharedPref = getContext().getSharedPreferences("dataUser",Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPref.edit();
      editor.putString("dataUser", name);
      editor.commit();

      ret.put("value", implementation.saveDataUser("OK"));
      call.resolve(ret);

    } catch (Exception e) {

      ret.put("value", implementation.saveDataUser("error"));
      call.resolve(ret);
    }
  }

  @PluginMethod
  public void getDataUser(PluginCall call) {
    JSObject ret = new JSObject();

    SharedPreferences sharedPref = getActivity().getSharedPreferences("dataUser", Context.MODE_PRIVATE);
    String enc = sharedPref.getString("dataUser", null);
    if (!"".equals(enc) && enc != null) {
      ret.put("value", implementation.getDataUser(enc));
      call.resolve(ret);
    } else {
      ret.put("value", implementation.getDataUser(null));
      call.resolve(ret);
    }
  }

  @PluginMethod
  public void cleanAll(PluginCall call) {
    JSObject ret = new JSObject();

    PreferenceManager.getDefaultSharedPreferences(getContext()).edit().clear().commit();
      ret.put("value", implementation.getDataUser("OK"));
      call.resolve(ret);
  }

  @ActivityCallback
  private void verifyResult(PluginCall call, ActivityResult result){
    JSObject ret = new JSObject();

    if(result.getResultCode() == Activity.RESULT_OK){
      Intent data = result.getData();
      if(data.hasExtra("result")){
        switch (data.getStringExtra("result")){
          case "success":
            ret.put("value", "OK");
            call.resolve(ret);
            break;
          case "failed":
            ret.put("value", "No");
            call.resolve(ret);
            break;
          default:
            ret.put("value", "No");
            call.resolve(ret);
            break;
        }
      }
    }
  }



}
