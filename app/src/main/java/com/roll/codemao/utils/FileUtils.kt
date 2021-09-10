package com.roll.codemao.utils

/**
 * Copyright (C), 2015-2021, 编程猫有限公司
 * FileName: FileUtils
 * Author: cretin
 * Date: 2021/5/21 6:06 PM
 * Description:
 */

object FileUtils{
    /**
     * 字节转kb/mb/gb
     * @param size
     * @return
     */
    fun getPrintSize(size: Long): String? {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        var size = size
        size = if (size < 1024) {
            return size.toString() + "B"
        } else {
            size / 1024
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        size = if (size < 1024) {
            return size.toString() + "KB"
        } else {
            size / 1024
        }
        return if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100
            ((size / 100).toString() + "."
                    + (size % 100).toString() + "MB")
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024
            ((size / 100).toString() + "."
                    + (size % 100).toString() + "GB")
        }
    }
}