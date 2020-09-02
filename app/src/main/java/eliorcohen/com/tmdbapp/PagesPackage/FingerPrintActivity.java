package eliorcohen.com.tmdbapp.PagesPackage;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import eliorcohen.com.tmdbapp.R;

public class FingerPrintActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mHeadingLabel, paraLabel;
    private ImageView mFingerprintImage, imageView;
    private TextView mParaLabel;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";
    private Button btnBack;
    private MediaPlayer sPassword, sFinger, sCancel;

    // All the codes add up to confirmation by fingerprinting
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);

        initUI();
        initListeners();
        fingerAuth();
    }

    private void initUI() {
        mHeadingLabel = findViewById(R.id.headingLabel);
        mFingerprintImage = findViewById(R.id.fingerprintImage);
        mParaLabel = findViewById(R.id.paraLabel);

        btnBack = findViewById(R.id.btnBack);

        sPassword = MediaPlayer.create(FingerPrintActivity.this, R.raw.access_sound);
        sFinger = MediaPlayer.create(FingerPrintActivity.this, R.raw.good_access_sound);
        sCancel = MediaPlayer.create(FingerPrintActivity.this, R.raw.cancel_and_move_sound);
    }

    private void initListeners() {
        btnBack.setOnClickListener(this);
    }

    private void fingerAuth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                mParaLabel.setText("Fingerprint Scanner not detected in Device");
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                mParaLabel.setText("Permission not granted to use Fingerprint Scanner");
            } else if (!keyguardManager.isKeyguardSecure()) {
                mParaLabel.setText("Add Lock to your Phone in Settings");
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                mParaLabel.setText("You should add at least 1 Fingerprint to use this Feature");
            } else {
                mParaLabel.setText("Place your Finger on Scanner to Access the App.");
                generateKey();
                if (cipherInit()) {
                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                    fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

        private Context context;

        FingerprintHandler(Context context) {
            this.context = context;
        }

        void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            this.update("There was an Auth Error. " + errString, false);
        }

        @Override
        public void onAuthenticationFailed() {
            this.update("Auth Failed. ", false);

            sPassword.start();  // Play sound
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            this.update("Error: " + helpString, false);
        }

        // After the access of the FingerPrint this activity pass to SplashActivity
        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            this.update("You can now access the app.", true);

            sFinger.start();  // Play sound

            Intent intentFinger = new Intent(FingerPrintActivity.this, SplashActivity.class);
            startActivity(intentFinger);
        }

        private void update(String s, boolean b) {
            paraLabel = ((Activity) context).findViewById(R.id.paraLabel);
            imageView = ((Activity) context).findViewById(R.id.fingerprintImage);
            paraLabel.setText(s);
            if (!b) {
                paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            } else {
                paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                imageView.setImageResource(R.mipmap.action_done);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                sCancel.start();  // Play sound

                onBackPressed();
                break;
        }
    }

}
