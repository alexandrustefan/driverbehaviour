package ro.appcamp.driverbehaviour;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MainService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS



//    private static final String ACTION_FOO = "ro.appcamp.driverbehaviour.action.FOO";
//    private static final String ACTION_BAZ = "ro.appcamp.driverbehaviour.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "ro.appcamp.driverbehaviour.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "ro.appcamp.driverbehaviour.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
//    public static void startActionMain(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, MainService.class);
//        intent.setAction(ACTION_FOO);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }

    public MainService() {
        super("MainService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String param1 = intent.getStringExtra(EXTRA_PARAM1);
            handleActionMain(param1);
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionMain(String param1) {
        // TODO: Handle action Foo



        throw new UnsupportedOperationException("Not yet implemented");
    }


}