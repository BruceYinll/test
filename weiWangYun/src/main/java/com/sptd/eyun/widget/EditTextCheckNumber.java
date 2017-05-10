package com.sptd.eyun.widget;


import com.sptd.eyun.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class EditTextCheckNumber extends LinearLayout{

	private  EditText editText;
	private Button button;
	
	public EditTextCheckNumber(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public EditTextCheckNumber(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view=LayoutInflater.from(context).inflate(R.layout.edittext_checknumber
				, this, true);
		editText=(EditText) view.findViewById(R.id.edittext);
		button=(Button) view.findViewById(R.id.button);
		
	
	}

	public EditText getEditText() {
		return editText;
	}

	public void setEditText(EditText editText) {
		this.editText = editText;
	}

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}
	
	
	    
    
}
