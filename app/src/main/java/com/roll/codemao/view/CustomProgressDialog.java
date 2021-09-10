package com.roll.codemao.view;

/**************************************************************************************
 * [Project]
 *       MyProgressDialog
 * [Package]
 *       com.lxd.widgets
 * [FileName]
 *       CustomProgressDialog.java
 * [Copyright]
 *       Copyright 2012 LXD All Rights Reserved.
 * [History]
 *       Version          Date              Author                        Record
 *--------------------------------------------------------------------------------------
 *       1.0.0           2012-4-27         lxd (rohsuton@gmail.com)        Create
 **************************************************************************************/

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.roll.codemao.R;


public class CustomProgressDialog extends Dialog {
	private Context context = null;
	private static CustomProgressDialog customProgressDialog = null;

	public CustomProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static CustomProgressDialog createDialog(Context context) {
		customProgressDialog = new CustomProgressDialog(context,
				R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.custom_progressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return customProgressDialog;
	}

	/**
	 * 
	 * [Summary] setTitile 标题
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public CustomProgressDialog setTitile(String strTitle) {
		return customProgressDialog;
	}

	/**
	 * 
	 * [Summary] setMessage 提示内容
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public CustomProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.loading_msg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		return customProgressDialog;
	}
}
