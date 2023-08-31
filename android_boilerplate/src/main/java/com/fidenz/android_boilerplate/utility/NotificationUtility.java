package com.fidenz.android_boilerplate.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fenchtose.tooltip.Tooltip;
import com.fenchtose.tooltip.TooltipAnimation;
import com.tapadoo.alerter.Alerter;

import de.mateware.snacky.Snacky;


/**
 * @author chalana
 * @contact Chalana.n@fidenz.com | 071 6 359 376
 */

public class NotificationUtility {

    public static void showNoInternetMessage(final Activity activity,String message) {
        if (activity != null) {
            Snacky.builder()
                    .setActivity(activity)
//                .setActionText(activity.getString(R.string.ok))
//                .setActionClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //do something
//                        activity.finish();
//                    }
//                })
                    .setText(message)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .error()
                    .show();
        }
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public static void showBackOnlineMessage(final Activity activity,String message) {
        if (activity != null) {
            Snacky.builder()
                    .setActivity(activity)
                    .setText(message)
                    .setDuration(3000)
                    .success()
                    .show();
        }

    }

    public static void showSnakeBarMessage(Activity activity, AlertType type, String message) {
        if (type == AlertType.WARNING) {
            Snacky.builder()
                    .setActivity(activity)
                    .setText(message)
                    .setDuration(3000)
                    .warning()
                    .show();
        } else if (type == AlertType.ERROR) {
            Snacky.builder()
                    .setActivity(activity)
                    .setText(message)
                    .setDuration(3000)
                    .error()
                    .show();
        } else if (type == AlertType.SUCCESS) {
            Snacky.builder()
                    .setActivity(activity)
                    .setText(message)
                    .setDuration(3000)
                    .success()
                    .show();
        } else if (type == AlertType.INFO) {
            Snacky.builder()
                    .setActivity(activity)
                    .setText(message)
                    .setDuration(3000)
                    .info()
                    .show();
        }
    }


    public static void showPermanentSnakeBarMessage(Activity activity, AlertType type, String message) {
        if (type == AlertType.WARNING) {
            Snacky.builder()
                    .setActivity(activity)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .warning()
                    .show();
        } else if (type == AlertType.ERROR) {
            Snacky.builder()
                    .setActivity(activity)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .error()
                    .show();
        } else if (type == AlertType.SUCCESS) {
            Snacky.builder()
                    .setActivity(activity)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .success()
                    .show();
        } else if (type == AlertType.INFO) {
            Snacky.builder()
                    .setActivity(activity)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .info()
                    .show();
        }
    }

    public static void showSnakeBarMessage(Activity activity, AlertType type, String message, String btnText, View.OnClickListener listener) {
        if (type == AlertType.WARNING) {
            Snacky.builder()
                    .setActivity(activity)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .warning()
                    .setAction(btnText, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onClick(view);
                        }
                    })
                    .show();

        } else if (type == AlertType.ERROR) {
            Snacky.builder()
                    .setActivity(activity)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .error()
                    .setAction(btnText, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onClick(view);
                        }
                    })
                    .show();
        } else if (type == AlertType.SUCCESS) {
            Snacky.builder()
                    .setActivity(activity)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .success()
                    .setAction(btnText, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onClick(view);
                        }
                    })
                    .show();
        } else if (type == AlertType.INFO) {
            Snacky.builder()
                    .setActivity(activity)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .info()
                    .setAction(btnText, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onClick(view);
                        }
                    })
                    .show();
        }
    }

    public static void showSnakeBarMessageWithAction(View view, AlertType type, String message, String btnText, View.OnClickListener listener) {
        if (type == AlertType.WARNING) {
            Snacky.builder()
                    .setView(view)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .warning()
                    .setAction(btnText, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onClick(view);
                        }
                    })
                    .show();

        } else if (type == AlertType.ERROR) {
            Snacky.builder()
                    .setView(view)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .error()
                    .setAction(btnText, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onClick(view);
                        }
                    })
                    .show();
        } else if (type == AlertType.SUCCESS) {
            Snacky.builder()
                    .setView(view)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .success()
                    .setAction(btnText, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onClick(view);
                        }
                    })
                    .show();
        } else if (type == AlertType.INFO) {
            Snacky.builder()
                    .setView(view)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .info()
                    .setAction(btnText, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onClick(view);
                        }
                    })
                    .show();
        }
    }



    public static void showSnakeBarMessage(View view, AlertType type, String message) {
        if (type == AlertType.WARNING) {
            Snacky.builder()
                    .setView(view)
                    .setText(message)
                    .setDuration(3000)
                    .warning()
                    .show();
        } else if (type == AlertType.ERROR) {
            Snacky.builder()
                    .setView(view)
                    .setText(message)
                    .setDuration(3000)
                    .error()
                    .show();
        } else if (type == AlertType.SUCCESS) {
            Snacky.builder()
                    .setView(view)
                    .setText(message)
                    .setDuration(3000)
                    .success()
                    .show();
        } else if (type == AlertType.INFO) {
            Snacky.builder()
                    .setView(view)
                    .setText(message)
                    .setDuration(3000)
                    .info()
                    .show();
        }
    }

    public static void showOtpNotification(Activity activity,int icon,int color, String message) {
        if (activity != null) {
            Alerter.create(activity)
                    // .setTitle("OTP is sent")
                    .setIcon(icon)
                    .setText(message)
                    .setBackgroundColorRes(color) // or setBackgroundColorInt(Color.CYAN)
                    .show();
        }
    }

    public static void topNotification(Activity activity, int icon, Drawable customLayout, String message) {
        if (activity != null) {


            Alerter.create(activity)
                    // .setTitle("OTP is sent")
                    .enableSwipeToDismiss()
                    .enableVibration(true)
                    .setIcon(icon)
                    .setBackgroundDrawable(customLayout)
                    .setText(message)
                    //  .setBackgroundColorRes(R.color.colorPrimary) // or setBackgroundColorInt(Color.CYAN)
                    .show();
        }
    }


//    public static void showCustomTooltip(Activity activity, View anchor, ViewGroup root,int layout) {
//        View content = activity.getLayoutInflater().inflate(R.layout.layout_select_vehicle_tool_tip, null);
//
//        final Tooltip customTooltip = new Tooltip.Builder(activity)
//                .anchor(anchor, Tooltip.BOTTOM)
//                .animate(new TooltipAnimation(TooltipAnimation.SCALE_AND_FADE, 400, true))
//                .autoAdjust(true)
//                .withPadding(5)
//                .content(content)
//                .cancelable(false)
//                .withTip(new Tooltip.Tip(10, 10, activity.getColor(R.color.colorPrimary), 4))
//                .into(root)
//                .debug(true)
//                .show();
//
//        content.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customTooltip.dismiss(true);
//            }
//        });
//    }

    public static Tooltip showHomeTooltip(Activity activity, View anchor, ViewGroup root, int layout,int color,int dismissContainerId) {
        View content = activity.getLayoutInflater().inflate(layout, null);

        Tooltip customTooltip = new Tooltip.Builder(activity)
                .anchor(anchor, Tooltip.BOTTOM)
                .animate(new TooltipAnimation(TooltipAnimation.SCALE_AND_FADE, 400, true))
                .autoAdjust(true)
                .content(content)
                .cancelable(false)
                .withTip(new Tooltip.Tip(30, 30, activity.getColor(color), 10))
                .into(root)
                .debug(true)
                .withPadding(5)
                .show();

        content.findViewById(dismissContainerId).setOnClickListener(view -> {
            customTooltip.dismiss(true);
        });
        return customTooltip;
    }


}
