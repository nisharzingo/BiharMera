package tv.merabihar.app.merabihar.Util;

import java.util.concurrent.Executor;

/**
 * Created by ZingoHotels Tech on 31-10-2018.
 */

public class ThreadExecuter implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
