package com.roll.codemao.base.ui.expand.qmui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContextWrapper
import com.roll.codemao.R
import com.roll.codemao.base.ui.expand.kotlin._main
import com.roll.codemao.utils.ResUtils

import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.IconType
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * author： deemons
 * date:    2018/6/17
 * desc:
 */

/**
 *  创建仅提示文案的 dialog
 */
fun Activity.createTipDialog(msg: CharSequence,
                             @IconType iconType: Int = QMUITipDialog.Builder.ICON_TYPE_NOTHING): QMUITipDialog {
    return QMUITipDialog.Builder(this)
            .setIconType(iconType)
            .setTipWord(msg)
            .create()
}

/**
 *  创建仅一个按钮的 dialog
 */


fun Activity.createSimpleDialog(msg: CharSequence, title: String = "",
                                buttonMsg: CharSequence = ResUtils.getString(R.string.know),
                                clickListener: (Dialog) -> Unit = { dialog -> dialog.dismiss() }): QMUIDialog {
    val builder = QMUIDialog.MessageDialogBuilder(this)
            .setMessage(msg)
            .addAction(0, buttonMsg, QMUIDialogAction.ACTION_PROP_NEGATIVE) { dialog, _ -> clickListener(dialog) }

    if (title.isNotBlank()) {
        builder.setTitle(title)
    }
    return builder.create()

}

/**
 *  创建两个按钮的 Dialog
 *
 *  参数混乱的原因是，默认参数只能添加在最后
 */

@JvmOverloads
fun Activity.createNormalDialog(msg: CharSequence, rightAction: DialogAction, title: String = "", leftAction: DialogAction = DialogAction()): QMUIDialog {
    val builder = QMUIDialog.MessageDialogBuilder(this)
            .setMessage(msg)

    if (rightAction.isSubjectColor) {
        builder.addAction(0, rightAction.buttonMsg, QMUIDialogAction.ACTION_PROP_NEGATIVE) { dialog, index -> rightAction.clickListener(dialog) }
    } else {
        builder.addAction(rightAction.buttonMsg) { dialog, index -> rightAction.clickListener(dialog) }
    }

    if (leftAction.isSubjectColor) {
        builder.addAction(0, leftAction.buttonMsg, QMUIDialogAction.ACTION_PROP_NEGATIVE) { dialog, index -> leftAction.clickListener(dialog) }
    } else {
        builder.addAction(leftAction.buttonMsg) { dialog, index -> leftAction.clickListener(dialog) }
    }

    if (title.isNotBlank()) {
        builder.setTitle(title)
    }

    return builder.create()
}

fun Activity.createCustomDialog(layoutId: Int, rightAction: DialogAction, title: String = "", leftAction: DialogAction?): QMUIDialog {
    val builder = QMUIDialog.CustomDialogBuilder(this)
            .setLayout(layoutId)

    if (rightAction.isSubjectColor) {
        builder.addAction(0, rightAction.buttonMsg, QMUIDialogAction.ACTION_PROP_NEGATIVE) { dialog, index -> rightAction.clickListener(dialog) }
    } else {
        builder.addAction(rightAction.buttonMsg) { dialog, index -> rightAction.clickListener(dialog) }
    }

    leftAction?.let {
        if (leftAction.isSubjectColor) {
            builder.addAction(0, leftAction.buttonMsg, QMUIDialogAction.ACTION_PROP_NEGATIVE) { dialog, index -> leftAction.clickListener(dialog) }
        } else {
            builder.addAction(leftAction.buttonMsg) { dialog, index -> leftAction.clickListener(dialog) }
        }
    }

    if (title.isNotBlank()) {
        builder.setTitle(title)
    }

    return builder.create()
}


/**
 *  创建有多个按钮的 Dialog
 *  顺序是从左往右依次添加
 */
fun Activity.createMessageDialog(msg: CharSequence, vararg action: DialogAction, title: String = ""): QMUIDialog {
    val builder = QMUIDialog.MessageDialogBuilder(this)
            .setMessage(msg)

    for (dialogAction in action) {
        if (dialogAction.isSubjectColor) {
            builder.addAction(0, dialogAction.buttonMsg, QMUIDialogAction.ACTION_PROP_NEGATIVE) { dialog, index -> dialogAction.clickListener(dialog) }
        } else {
            builder.addAction(dialogAction.buttonMsg) { dialog, index -> dialogAction.clickListener(dialog) }
        }
    }


    if (title.isNotBlank()) {
        builder.setTitle(title)
    }

    return builder.create()
}

/**
 *  Dialog 的 Action
 *  buttonMsg: 按钮文案
 *  isSubjectColor: 是否显示主题色
 *  clickListener: 点击监听
 */
data class DialogAction(val buttonMsg: CharSequence = ResUtils.getString(R.string.cancel), val isSubjectColor: Boolean = false, val clickListener: (Dialog) -> Unit = { dialog -> dialog.dismiss() })


fun Dialog.showTime(during: Long = 2000) {
    showTime(during) {}
}


@SuppressLint("CheckResult")
fun Dialog.showTime(during: Long = 2000, dismissListener: () -> Unit?) {
    val activity: RxAppCompatActivity? = when {
        context is RxAppCompatActivity -> context as RxAppCompatActivity
        context is ContextWrapper && (context as ContextWrapper).baseContext is RxAppCompatActivity -> (context as ContextWrapper).baseContext as RxAppCompatActivity
        else -> return
    }

    Observable.timer(during, MILLISECONDS)
            .compose(activity!!.bindToLifecycle())
            .doOnSubscribe {
                try {
                    if (!this.isShowing) {//弹窗不显示的时候
                        show()
                    }
                } catch (e: Exception) {//这里会抛出activity已退出window的异常，并且不会被RX捕获，导致app崩溃
                }
            }
            ._main()
            .doFinally {
                if (this.isShowing) {
                    dismiss()
                }
            }
            .subscribe({
                if (this.isShowing) {
                    dismiss()
                }
                dismissListener()
            }, { it.printStackTrace() })

}


