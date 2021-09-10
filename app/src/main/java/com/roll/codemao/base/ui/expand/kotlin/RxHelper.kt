package com.roll.codemao.base.ui.expand.kotlin

import android.app.Activity
import androidx.annotation.StringRes
import android.text.TextUtils
import com.roll.codemao.app.data.api.model.Result
import com.roll.codemao.base.ui.expand.qmui.createTipDialog
import com.roll.codemao.base.ui.expand.qmui.showTime
import com.roll.codemao.utils.ResUtils
import com.roll.codemao.utils.RxUtils
import com.roll.codemao.base.ui.IView
import com.roll.codemao.base.ui.RootActivity
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog.*
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit.SECONDS


/**
 * author： deemons
 * date:    2018/5/7
 * desc:   rx 相关帮助类
 */
fun Disposable.bind(b: CompositeDisposable) {
    b.add(this)
}


fun <T> Observable<T>.io_main(): Observable<T> {
    return io_()._main()
}


fun <T> Observable<T>.io_(): Observable<T> {
    return this.subscribeOn(RxUtils.getSchedulerIO())
}


fun <T> Observable<T>.main_(): Observable<T> {
    return this.subscribeOn(AndroidSchedulers.mainThread())
}


fun <T> Observable<T>._io(): Observable<T> {
    return this.observeOn(RxUtils.getSchedulerIO())
}


fun <T> Observable<T>._main(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.io_main(): Single<T> {
    return this.subscribeOn(RxUtils.getSchedulerIO())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> RxAppCompatActivity.bindUntilEventRx(event: ActivityEvent): LifecycleTransformer<T> {
    return this.bindUntilEvent<T>(event)
}

/**
 *  处理 Response
 *  不成功时，显示错误提示，并取消订阅
 */
fun <T> Observable<Result<T>>.doErrorData(view: IView?): Observable<Result<T>> {
    return this.takeWhile {
        if ((!it.isSuccess) && (!TextUtils.isEmpty(it.msg))) view?.showToast(it.msg)
        return@takeWhile it.isSuccess
    }

}

/**
 *  绑定 loading dialog
 *  在订阅时显示，结束时自动消失
 *  在 2s 后可取消弹窗，同时取消订阅
 */
fun <T> Observable<T>.bindLoadingDialog(root: RootActivity<*>?, @StringRes stringRes: Int = 0): Observable<T> {

    val dialog = root?.creataDialog(if (stringRes == 0) "" else ResUtils.getString(stringRes))
    dialog?.setCancelable(false)
    dialog?.setCanceledOnTouchOutside(false)
    dialog?.showTime(Long.MAX_VALUE)

    Observable.timer(2, SECONDS)
            .compose(root?.bindToLifecycle())
            .subscribe({
                dialog?.setCancelable(true)
                dialog?.setCanceledOnTouchOutside(true)
            }, { it.printStackTrace() })

    val dialogCancelSubject = BehaviorSubject.create<Boolean>()
    dialog?.setOnCancelListener { dialogCancelSubject.onNext(true) }

    return this.doOnSubscribe { dialog?.show() }
            .doFinally { dialog?.dismiss() }
            .takeUntil(dialogCancelSubject)
}

/**
 *  绑定 loading dialog
 *  在订阅时显示，结束时自动消失
 *  在 2s 后可取消弹窗，同时取消订阅
 */
fun <T> Observable<T>.bindLoadingDialogBase(activity: Activity?, @StringRes stringRes: Int = 0): Observable<T> {

    val dialog = activity?.createTipDialog(
            if (stringRes == 0) "" else ResUtils.getString(stringRes),
            Builder.ICON_TYPE_LOADING)
    dialog?.setCancelable(false)
    dialog?.setCanceledOnTouchOutside(false)
    dialog?.showTime(Long.MAX_VALUE)

    Observable.timer(2, SECONDS)
            .subscribe({
                dialog?.setCancelable(true)
                dialog?.setCanceledOnTouchOutside(true)
            }, { it.printStackTrace() })

    val dialogCancelSubject = BehaviorSubject.create<Boolean>()
    dialog?.setOnCancelListener { dialogCancelSubject.onNext(true) }

    return this.doOnSubscribe { dialog?.show() }
            .doFinally { dialog?.dismiss() }
            .takeUntil(dialogCancelSubject)
}