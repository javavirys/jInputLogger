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

import java.io.DataInputStream;
import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity implements View.OnClickListener,Runnable{

	MainActivity main = this;
	boolean thread_flag;
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
			Toast.makeText(main.getApplicationContext(), "stop", Toast.LENGTH_LONG).show();
			break;
		case R.id.button2:
			thread_flag=false;
			Toast.makeText(main.getApplicationContext(), "stop", Toast.LENGTH_LONG).show();
			break;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
			try{
				Process process = Runtime.getRuntime().exec(new String[]{"su","-c","getevent"});// -l"});
				//process.waitFor();
				
				DataInputStream dis = new DataInputStream(process.getInputStream());
				
				String _data;
				System.out.println("Start run()");
				while((_data=dis.readLine())!= null  && thread_flag)
				{
					//TODO Here come all the events
					System.out.println("Event: "+_data);
					Thread.sleep(3);
				}
				System.out.println("break");
				Thread.sleep(5);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error: "+e.toString());
				thread_flag = false;
				
			}
	}
}
