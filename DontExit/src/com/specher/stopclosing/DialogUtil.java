package com.specher.stopclosing;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.WindowManager;

/**
 * �Ի��򹤾�
 */
public class DialogUtil {
    /**
     * ��ʾһ���Ի���
     *
     * @param context                    �����Ķ�����ø�Activity��������Ҫandroid.permission.SYSTEM_ALERT_WINDOW
     * @param title                      ����
     * @param message                    ��Ϣ
     * @param confirmButton              ȷ�ϰ�ť
     * @param confirmButtonClickListener ȷ�ϰ�ť���������
     * @param centerButton               �м䰴ť
     * @param centerButtonClickListener  �м䰴ť���������
     * @param cancelButton               ȡ����ť
     * @param cancelButtonClickListener  ȡ����ť���������
     * @param onShowListener             ��ʾ������,���Ի������show()������ʱ�򴥷���
     * @param cancelable                 �Ƿ�����ͨ��������ذ�ť���ߵ���Ի���֮���λ�ùرնԻ���
     * @param onCancelListener           ȡ��������,���Ի������dismiss()������ʱ�򴥷���
     * @param onDismissListener          ���ټ�����,���Ի������cancel()������ʱ�򴥷���
     * @return �Ի���
     */
    public static AlertDialog showAlert(Context context, String title, String message, String confirmButton, DialogInterface.OnClickListener confirmButtonClickListener, String centerButton, DialogInterface.OnClickListener centerButtonClickListener, String cancelButton, DialogInterface.OnClickListener cancelButtonClickListener, DialogInterface.OnShowListener onShowListener, boolean cancelable, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog.Builder promptBuilder = new AlertDialog.Builder(context);
        if (title != null) {
            promptBuilder.setTitle(title);
        }
        if (message != null) {
            promptBuilder.setMessage(message);
        }
        if (confirmButton != null) {
            promptBuilder.setPositiveButton(confirmButton,
                    confirmButtonClickListener);
        }
        if (centerButton != null) {
            promptBuilder.setNeutralButton(centerButton,
                    centerButtonClickListener);
        }
        if (cancelButton != null) {
            promptBuilder.setNegativeButton(cancelButton,
                    cancelButtonClickListener);
        }
        promptBuilder.setCancelable(cancelable);
        if (cancelable) {
            promptBuilder.setOnCancelListener(onCancelListener);
        }
        AlertDialog alertDialog = promptBuilder.create();
        if (!(context instanceof Activity)) {
            alertDialog.getWindow()
                    .setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        alertDialog.setOnDismissListener(onDismissListener);
        alertDialog.setOnShowListener(onShowListener);
        alertDialog.show();
        return alertDialog;
    }


    /**
     * ��ʾһ��ȷ�Ϻ�ȡ����ť�ĶԻ���
     *
     * @param context                    �����Ķ�����ø�Activity��������Ҫandroid.permission.SYSTEM_ALERT_WINDOW
     * @param title                      ����
     * @param message                    ��Ϣ
     * @param confirmButton              ȷ�ϰ�ť
     * @param confirmButtonClickListener ȷ�ϰ�ť���������
     * @param cancelButton               ȡ����ť
     * @param cancelButtonClickListener  ȡ����ť���������
     * @return �Ի���
     */
    public static AlertDialog showAlertTwo(Context context, String title, String message, String confirmButton, DialogInterface.OnClickListener confirmButtonClickListener, String cancelButton, DialogInterface.OnClickListener cancelButtonClickListener) {
        return showAlert(context, title, message, confirmButton,
                confirmButtonClickListener, null, null, cancelButton,
                cancelButtonClickListener, null, true, null, null);
    }


    /**
     * ��ʾֻ��һ����ť����ʾ��
     *
     * @param context       �����Ķ�����ø�Activity��������Ҫandroid.permission.SYSTEM_ALERT_WINDOW
     * @param message       ��ʾ����Ϣ
     * @param confirmButton ȷ����ť������
     */
    public static AlertDialog showPrompt(Context context, String title, String message, String confirmButton) {
        return showAlert(context, title, message, confirmButton, null, null, null, null, null, null, true, null, null);
    }

    /**
     * ��ʾ������ߺ��ұߵİ�ť����ʾ��
     *
     * @param context       �����Ķ�����ø�Activity��������Ҫandroid.permission.SYSTEM_ALERT_WINDOW
     * @param message       ��ʾ����Ϣ
     * @param confirmButton ȷ����ť������
     */
    public static AlertDialog showAlertlr(Context context, String title, String message, String confirmButton, DialogInterface.OnClickListener confirmButtonClickListener, String centerButton, DialogInterface.OnClickListener centerButtonClickListener) {
        return showAlert(context, title, message, confirmButton, confirmButtonClickListener, centerButton, centerButtonClickListener, null, null, null, false, null, null);
    }
}