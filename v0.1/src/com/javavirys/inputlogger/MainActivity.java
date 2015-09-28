/*
   Copyright 2015 javavirys

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.javavirys.inputlogger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity implements View.OnClickListener,Runnable{

	MainActivity main = this;
	boolean thread_flag;
	String my_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)this.findViewById(R.id.button1);
        button.setOnClickListener(this);
        button = (Button)this.findViewById(R.id.button2);
        button.setOnClickListener(this);
        
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.button1:
			thread_flag=true;
			Thread t1 = new Thread(this);
			t1.start();
			break;
		case R.id.button2:
			thread_flag=false;
			break;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		EditText edit = (EditText)main.findViewById(R.id.pathEditText);
		String path = edit.getText().toString();
		System.out.println("path: "+path);
		InputStream in = null;
		my_text = new String();
		while(thread_flag)
		{
			try{
				Process process = Runtime.getRuntime().exec(new String[]{"su","-c","chmod 0655 "+path});
				process.waitFor();
				System.out.println("process terminate");
				in = new BufferedInputStream(new FileInputStream(path));
				int code;
				while((code=in.read())!=-1  && thread_flag)
				{
					System.out.println("Symbol: "+(char)code+"&&code: "+code);
					my_text = "Symbol: "+(char)code+"&&code: "+code+"\n";
					main.runOnUiThread(new Runnable(){
						public void run(){
							EditText edit = (EditText)main.findViewById(R.id.editText1);
							edit.append(my_text);
						}
					});
				}
				Thread.sleep(5);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				my_text = "Error: "+e.toString();
				System.out.println(my_text);
				thread_flag = false;
				main.runOnUiThread(new Runnable(){
					public void run(){
						EditText edit = (EditText)main.findViewById(R.id.editText1);
						edit.append(my_text);
					}
				});
				
			}finally{
				
				if(in!=null)
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
		}
	}
}
