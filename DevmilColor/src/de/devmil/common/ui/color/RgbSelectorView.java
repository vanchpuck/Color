/*
 * Copyright (C) 2011 Devmil (Michael Lamers) 
 * Mail: develmil@googlemail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.devmil.common.ui.color;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class RgbSelectorView extends LinearLayout {

	/*
	 * Member classes
	 */
	
	/**
	 * Colors editor action listener. 
	 * Changes result color according to the editor input value.
	 * @author Vanchpuck
	 *
	 */
	private class EditorListener implements OnEditorActionListener {

		private ProgressBar bar;

		EditorListener(ProgressBar pBar) {
			bar = pBar;
		}

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				bar.setProgress(Integer.valueOf(v.getText().toString()));
				setPreviewImage();
				onColorChanged();
				return true;
			}
			return false;
		}
	}

	/*
	 * Class fields
	 */
	
	private SeekBar seekRed;
	private SeekBar seekGreen;
	private SeekBar seekBlue;

	private ImageView imgPreview;

	private EditText valueRed;
	private EditText valueGreen;
	private EditText valueBlue;

	private OnColorChangedListener listener;

//	private OnEditorActionListener editorListener;

	/*
	 * Constructors
	 */
	
	public RgbSelectorView(Context context) {
		super(context);
		init();
	}

	public RgbSelectorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/*
	 * Methods
	 */
	
	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rgbView = inflater.inflate(R.layout.color_rgbview, null);

		addView(rgbView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				setPreviewImage();
				onColorChanged();
			}
		};

		seekRed = (SeekBar) rgbView.findViewById(R.id.color_rgb_seekRed);
		seekRed.setOnSeekBarChangeListener(listener);
		seekGreen = (SeekBar) rgbView.findViewById(R.id.color_rgb_seekGreen);
		seekGreen.setOnSeekBarChangeListener(listener);
		seekBlue = (SeekBar) rgbView.findViewById(R.id.color_rgb_seekBlue);
		seekBlue.setOnSeekBarChangeListener(listener);

		imgPreview = (ImageView) rgbView
				.findViewById(R.id.color_rgb_imgpreview);

		valueRed = (EditText) rgbView.findViewById(R.id.color_rgb_value_red);
		valueRed.setOnEditorActionListener(new EditorListener(seekRed));
		
		valueGreen = (EditText) rgbView.findViewById(R.id.color_rgb_value_green);
		valueGreen.setOnEditorActionListener(new EditorListener(seekGreen));
		
		valueBlue = (EditText) rgbView.findViewById(R.id.color_rgb_value_blue);
		valueBlue.setOnEditorActionListener(new EditorListener(seekBlue));

		setColor(Color.BLACK);
	}

	private void setPreviewImage() {
		Bitmap preview = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		preview.setPixel(0, 0, getColor());

		imgPreview.setImageBitmap(preview);
	}

	public int getColor() {
		return Color.rgb(seekRed.getProgress(), seekGreen.getProgress(),
				seekBlue.getProgress());
	}

	public void setColor(int color) {
		seekRed.setProgress(Color.red(color));
		seekGreen.setProgress(Color.green(color));
		seekBlue.setProgress(Color.blue(color));

		valueRed.setText(String.valueOf(Color.red(color)));
		valueGreen.setText(String.valueOf(Color.green(color)));
		valueBlue.setText(String.valueOf(Color.blue(color)));

		setPreviewImage();
	}

	private void onColorChanged() {
		if (listener != null)
			listener.colorChanged(getColor());
	}

	public void setOnColorChangedListener(OnColorChangedListener listener) {
		this.listener = listener;
	}

	public interface OnColorChangedListener {
		public void colorChanged(int color);
	}
}
