package com.example.mohammeduzair.androidapp.ThrdPage;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.telecom.Call;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.mohammeduzair.androidapp.Home.Homepage;
import com.example.mohammeduzair.androidapp.Home.heartrate;
import com.example.mohammeduzair.androidapp.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.FacebookSdkNotInitializedException;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class settings3 extends AppCompatActivity {
    Button shareLinkBtn;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.darktheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        printHashKey();
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        shareLinkBtn = (Button) findViewById(R.id.sharelink);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote("")
                        .setContentUrl(Uri.parse("https://www.google.com"))
                        .build();
                if (ShareDialog.canShow(ShareLinkContent.class)){
                    shareDialog.show(linkContent);
                }
            }
        }
        );
    }


    //used to get hashkey to enable Facebook SDK for sharing
    private void printHashKey() {
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.example.mohammeduzair.androidapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures){
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(settings3.this, menu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    
}

