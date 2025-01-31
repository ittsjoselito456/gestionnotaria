package jortega.proyecto.gestionnotaria.utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import jortega.proyecto.gestionnotaria.R;

public class NotificationUtils {
    public static final String CHANNEL_ID = "cita_channel";
    public static final String NEW_CHANNEL_ID = "nuevo_cita_channel";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Cita Channel";
            String description = "Canal para notificaciones de citas";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NEW_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // ⚠️ Importante: Configurar sonido en el canal
            Uri alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification_sound);
            channel.setSound(alarmSound, new android.media.AudioAttributes.Builder()
                    .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showCitaNotification(Context context, String title, String message) {
        // Usa un sonido personalizado en formato MP3
        Uri alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification_sound);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NEW_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo) // Cambia esto al nuevo ícono de tu notificación
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(alarmSound)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
