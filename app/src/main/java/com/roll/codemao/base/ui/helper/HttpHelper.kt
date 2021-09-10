package com.roll.codemao.base.ui.helper

import android.text.TextUtils
import com.roll.codemao.R
import com.roll.codemao.app.data.api.model.Result
import com.roll.codemao.base.ui.RootActivity
import com.roll.codemao.utils.ResUtils
import com.roll.codemao.utils.RxUtils
import com.google.gson.JsonParseException
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

class HttpHelper(private var rootActivity: RootActivity<*>) {
    private var mCompositeDisposable: CompositeDisposable? = null

    companion object {
        /**
         * 统一处理数据
         */
        fun <T> doRealStart(rootActivity: RootActivity<*>,
                            observable: Observable<T>?,
                            success: (data: T) -> Unit,
                            fail: (msg: String, isError: Boolean) -> Unit = { errMsg, isErr -> },
                            complete: () -> Unit = { },
                            ignoreError: Boolean = false,
                            ignoreToast: Boolean = false,
                            showDialog: Boolean = true,
                            dialogCancelable: Boolean = true): Disposable? {
            if (showDialog) {
                rootActivity.showDialog(cancelable = dialogCancelable)
            }
            return doRealEnd(rootActivity, observable?.compose(rootActivity.bindToLifecycle())
                    ?.compose(RxUtils.applySchedulers()), success, fail, complete, ignoreError, ignoreToast)

        }

        fun <T> doRealEnd(rootActivity: RootActivity<*>,
                          observable: Observable<T>?,
                          success: (data: T) -> Unit,
                          fail: (msg: String, isError: Boolean) -> Unit = { errMsg, isErr -> },
                          complete: () -> Unit = { },
                          ignoreError: Boolean = false,
                          ignoreToast: Boolean = false): Disposable? {
            return observable?.subscribe({ result: T ->
                //只有正常的请求才会到这里
                if (result is Result<*>) {
                    if (!(result as Result<*>).isSuccess) {
                        if (!ignoreToast)
                            rootActivity?.showToast((result as Result<*>).msg)
                        fail((result as Result<*>).msg, false)
                    } else {
                        success(result)
                    }
                } else {
                    success(result)
                }
                complete()
                rootActivity?.showContentView()
                rootActivity?.stopDialog()
            }, {
                var isError = false
                val msg =
                        if (it is HttpException) {
                            val httpException = it
                            if (httpException.code() > 500) {
                                //服务器异常
                                ResUtils.getString(R.string.err_http_server_exception)
                            } else {
                                ResUtils.getString(R.string.common_request_err)
                                //在这里我们当做是成功的 但是
                            }
                        } else if (it is JsonParseException || it is JSONException
                                || it is ParseException) {
                            ResUtils.getString(R.string.err_http_json_error)
                        } else if (it is ConnectException || it is SocketTimeoutException
                                || it is UnknownHostException) {
                            //这里确实是出错了 需要显示error
                            if (!ignoreError)
                                rootActivity?.showErrorView()

                            isError = true

                            ""
//                            ResUtils.getString(R.string.err_http_network_err)
                        } else if (it is NumberFormatException) {
                            ResUtils.getString(R.string.err_http_num_error)
                        } else {
                            ResUtils.getString(R.string.common_request_err)
                        }

                rootActivity?.stopDialog()
                fail(msg, isError)
                if (!ignoreToast && !TextUtils.isEmpty(msg)) {
                    rootActivity?.showToast(ResUtils.getString(R.string.common_request_err))
                }
                complete()
            })
        }
    }

    fun bind(mCompositeDisposable: CompositeDisposable) {
        this.mCompositeDisposable = mCompositeDisposable
    }

    /**
     * 通用网络请求
     */
    fun <T> doNetWork(observable: Observable<T>?,
                      success: (data: T) -> Unit,
                      fail: (msg: String, isError: Boolean) -> Unit = { errMsg, isErr -> },
                      complete: () -> Unit = { },
                      ignoreError: Boolean = false,
                      ignoreToast: Boolean = false,
                      showDialog: Boolean = true,
                      dialogCancelable: Boolean = true): Disposable? {
        val doReal = doRealStart(rootActivity, observable, success, fail, complete, ignoreError, ignoreToast, showDialog, dialogCancelable)
        if (mCompositeDisposable != null) {
            mCompositeDisposable?.add(doReal!!)
        }
        return doReal
    }
}

fun <T> Observable<T>.handlerResult(rootActivity: RootActivity<*>?, success: (data: T) -> Unit,
                                    fail: (msg: String, isError: Boolean) -> Unit = { errMsg, isErr -> },
                                    complete: () -> Unit = {}, ignoreError: Boolean = false,
                                    ignoreToast: Boolean = false) {
    if (rootActivity != null) {
        HttpHelper.doRealEnd(rootActivity, this, success, fail, complete, ignoreError, ignoreToast)
    }
}