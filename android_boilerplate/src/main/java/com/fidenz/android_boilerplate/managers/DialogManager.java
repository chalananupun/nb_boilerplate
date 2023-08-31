package com.fidenz.android_boilerplate.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.fidenz.android_boilerplate.R;
import com.fidenz.android_boilerplate.listeners.PopupOnClickListener;
import com.fidenz.android_boilerplate.utility.StringUtility;

public class DialogManager {

    private static final String TAG = "DialogManager";


    public static void preparePopupDialog(Dialog dialog, int layout) {


//        if (style != 0) {
//            dialog = new Dialog(context, style);
//            dialog.getWindow().getAttributes().windowAnimations = style;
//        }else{
//            dialog = new Dialog(context);
//        }
        dialog.setContentView(layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setCanceledOnTouchOutside(false);
    }


    public static void showWarningAlertDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void showInfoAlertDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    public static void showInfoAlertDialogNoIcon(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    public static void showAlertDialog(Context context,
                                       String title,
                                       String message,
                                       String positiveBtnText,
                                       String negativeBtnText,
                                       final DialogInterface.OnClickListener positiveCallBack,
                                       final DialogInterface.OnClickListener negativeCallBack) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        positiveCallBack.onClick(dialog, which);
                    }
                })
                .setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        negativeCallBack.onClick(dialog, which);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    public static void showAlertDialog(Context context,
                                       String title,
                                       String message,
                                       final String positiveBtnText,
                                       final DialogInterface.OnClickListener positiveCallBack) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        positiveCallBack.onClick(dialog, which);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    public static void showAlertDialogNoIcon(Context context,
                                             String title,
                                             String message,
                                             final String positiveBtnText,
                                             final DialogInterface.OnClickListener positiveCallBack) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        positiveCallBack.onClick(dialog, which);
                    }
                })
                .show();
    }


    public static Dialog getDialog(Activity activity, String message, String textPositive, final PopupOnClickListener positiveBtn, String textNegative, final PopupOnClickListener negativeBtn) {


        Log.d(TAG, "prepareDialog: ");

        Dialog alertDialog = createDialog(activity);

        TextView lblMessage = alertDialog.findViewById(R.id.lbl_message);

        alertDialog.findViewById(R.id.ll_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.findViewById(R.id.ll_title).setVisibility(View.GONE);

        lblMessage.setText(message);

        Button btnPositive = alertDialog.findViewById(R.id.btn_positive);
        btnPositive.setText(textPositive);

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positiveBtn.onClick();
            }
        });


        final Button btnNegative = alertDialog.findViewById(R.id.btn_negative);


        if (StringUtility.isNotNull(textNegative)) {
            btnNegative.setText(textNegative);
            btnNegative.setVisibility(View.VISIBLE);

            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    negativeBtn.onClick();
                }
            });

        } else {
            btnNegative.setVisibility(View.GONE);
        }


        return alertDialog;

    }

    public static Dialog getDialog(Activity activity, String title, String message, String textPositive, final PopupOnClickListener positiveBtn, String textNegative, final PopupOnClickListener negativeBtn) {


        Log.d(TAG, "prepareDialog: ");

        Dialog alertDialog = new Dialog(activity);
        alertDialog.setContentView(R.layout.popup_dialog_layout);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView lblTitle = alertDialog.findViewById(R.id.lbl_title);
        TextView lblMessage = alertDialog.findViewById(R.id.lbl_message);
        alertDialog.findViewById(R.id.ll_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        lblTitle.setText(title);
        lblMessage.setText(message);

        Button btnPositive = alertDialog.findViewById(R.id.btn_positive);
        btnPositive.setText(textPositive);

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positiveBtn.onClick();
            }
        });


        final Button btnNegative = alertDialog.findViewById(R.id.btn_negative);


        if (StringUtility.isNotNull(textNegative)) {
            btnNegative.setText(textNegative);
            btnNegative.setVisibility(View.VISIBLE);

            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    negativeBtn.onClick();
                }
            });

        } else {
            btnNegative.setVisibility(View.GONE);
        }


        return alertDialog;

    }


    public static Dialog getDialogWithCloseIcon(Activity activity, String title, String message, String textPositive, final PopupOnClickListener positiveBtn, String textNegative, final PopupOnClickListener negativeBtn) {


        Log.d(TAG, "prepareDialog: ");

        Dialog alertDialog = new Dialog(activity);
        alertDialog.setContentView(R.layout.popup_dialog_layout);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView lblTitle = alertDialog.findViewById(R.id.lbl_title);
        TextView lblMessage = alertDialog.findViewById(R.id.lbl_message);

        alertDialog.findViewById(R.id.ll_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        lblTitle.setText(title);
        lblMessage.setText(message);

        Button btnPositive = alertDialog.findViewById(R.id.btn_positive);
        btnPositive.setText(textPositive);

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positiveBtn.onClick();
            }
        });


        final Button btnNegative = alertDialog.findViewById(R.id.btn_negative);


        if (StringUtility.isNotNull(textNegative)) {
            btnNegative.setText(textNegative);
            btnNegative.setVisibility(View.VISIBLE);

            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    negativeBtn.onClick();
                }
            });

        } else {
            btnNegative.setVisibility(View.GONE);
        }


        return alertDialog;

    }


    private static Dialog createDialog(Activity activity) {
        Dialog alertDialog = new Dialog(activity);
        alertDialog.setContentView(R.layout.popup_dialog_layout);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }


    public static Dialog createCustomDialog(Activity activity, int layout) {
        Dialog alertDialog = new Dialog(activity, R.style.PopupDialogTheme);
        alertDialog.setContentView(layout);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }


    public static Dialog getDialog(Activity activity, String message, String textPositive, final PopupOnClickListener positiveBtn) {


        Log.d(TAG, "prepareDialog: ");

        Dialog alertDialog = createDialog(activity);


        TextView lblMessage = alertDialog.findViewById(R.id.lbl_message);
        TextView lblTitle = alertDialog.findViewById(R.id.lbl_title);

        lblMessage.setText(message);
        lblTitle.setVisibility(View.GONE);

        Button btn = alertDialog.findViewById(R.id.btn_dismiss);
        btn.setText(textPositive);
        btn.setVisibility(View.VISIBLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positiveBtn.onClick();
            }
        });


        alertDialog.findViewById(R.id.ll_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.findViewById(R.id.btn_positive).setVisibility(View.GONE);
        alertDialog.findViewById(R.id.btn_negative).setVisibility(View.GONE);


        return alertDialog;

    }


    public static Dialog getDialog(Activity activity, String title, String message, String textPositive, final PopupOnClickListener positiveBtn) {


        Log.d(TAG, "prepareDialog: ");

        Dialog alertDialog = createDialog(activity);

        TextView lblMessage = alertDialog.findViewById(R.id.lbl_message);


        TextView lblTitle = alertDialog.findViewById(R.id.lbl_title);
        lblTitle.setText(title);

        lblMessage.setText(message);

        Button btn = alertDialog.findViewById(R.id.btn_dismiss);
        btn.setText(textPositive);
        btn.setVisibility(View.VISIBLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positiveBtn.onClick();
            }
        });


        alertDialog.findViewById(R.id.ll_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.findViewById(R.id.btn_positive).setVisibility(View.GONE);
        alertDialog.findViewById(R.id.btn_negative).setVisibility(View.GONE);


        return alertDialog;

    }

    public void hideDialog(Dialog alertDialog) {
        if (alertDialog != null) {
            alertDialog.dismiss();
//            if(alertDialog.isShowing()){
//            }
        }
    }

}
