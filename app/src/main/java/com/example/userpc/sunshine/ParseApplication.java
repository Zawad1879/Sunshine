package com.example.userpc.sunshine;

import com.parse.Parse;
import android.app.Application;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "3wUqAX6D1QNWv3yNCuBfR35nN0CVw5H9Y6uVeMQ7", "8hgaUN8HpVQnlwR1RVOqLUTvfxb1XzE9RSXMlSYo");
    }
}
