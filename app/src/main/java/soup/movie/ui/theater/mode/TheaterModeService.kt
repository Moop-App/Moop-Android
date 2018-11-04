package soup.movie.ui.theater.mode

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import soup.movie.R
import soup.movie.notification.NotificationSpecs
import soup.movie.ui.theater.mode.brightness.BrightnessModule
import soup.movie.ui.theater.mode.dnd.DndModule
import soup.movie.util.toHexString
import timber.log.Timber

class TheaterModeService : Service() {

    private val dndModule: DndModule = DndModule(this)

    private val brightnessModule: BrightnessModule = BrightnessModule(this) { enabled ->
        Timber.d("BrightnessModule is %s", if (enabled) "enabled" else "disabled")
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Service binding is NOT Supported");
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate: 0x{${hashCode().toHexString()}}")
        dndModule.enable()
        brightnessModule.enable()
    }

    override fun onDestroy() {
        Timber.d("onDestroy: 0x{${hashCode().toHexString()}}")
        dndModule.disable()
        brightnessModule.disable()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_FOREGROUND -> startForegroundService()
            ACTION_STOP_FOREGROUND,
            ACTION_QUIT -> stopForegroundService()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        val notification = NotificationSpecs.createNotificationForTheaterMode(this)
                .setSmallIcon(R.drawable.ic_tile_sample)
                .setContentTitle("Sample")
                .setContentText("This is a on-going notification.")
                .setColor(Color.BLUE)
                .setOngoing(true)
                .setContentIntent(getSettingIntent())
                //.addAction(NotificationCompat.Action(
                //        R.drawable.ic_round_close, "Quit", getQuitIntent()))
                .build()
        startForeground(ID_SAMPLE, notification)
    }

    private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }

    private fun getSettingIntent(): PendingIntent {
        val intent = Intent(applicationContext, TheaterModeTileActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getQuitIntent(): PendingIntent {
        val quitIntent = createIntent(ACTION_QUIT)
        return PendingIntent.getService(this, 0, quitIntent, 0)
    }

    companion object {
        private const val ID_SAMPLE = 2

        private const val ACTION_START_FOREGROUND = "ACTION_START_FOREGROUND"
        private const val ACTION_STOP_FOREGROUND = "ACTION_STOP_FOREGROUND"
        private const val ACTION_QUIT = "ACTION_QUIT"

        fun start(ctx: Context) {
            Timber.d("start")
            ctx.startService(ctx.createIntent(ACTION_START_FOREGROUND))
        }

        fun stop(ctx: Context) {
            Timber.d("stop")
            ctx.startService(ctx.createIntent(ACTION_STOP_FOREGROUND))
        }

        private fun Context.createIntent(action: String): Intent {
            return Intent(applicationContext, TheaterModeService::class.java).also {
                it.action = action
            }
        }
    }
}
