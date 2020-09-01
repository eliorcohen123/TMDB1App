package eliorcohen.com.tmdbapp.OthersPackage;

import android.app.Application;

// Summary of getContext
public class ConApp extends Application {

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
    }

    public static Application getApplication() {
        return application;
    }

}
