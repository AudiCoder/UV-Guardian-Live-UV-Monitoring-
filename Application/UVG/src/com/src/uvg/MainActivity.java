package com.src.uvg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	protected static final Context LocalServiceController = null;
	protected static final Context This = null;
	String month = "6",
	        day = "1",
	        year = "2013",
	        place = "Los Angeles",
	        interval = "30",
	        state = "CA";
	        String url ="http://aa.usno.navy.mil/cgi-bin/aa_altazw.pl";
	        
	        
	        double build_width=72.1385; //to be calculated by lat and long
	    double lat1=34.0658238 ,lon1=-118.4455914 ,lat2= 34.0651751 ,lon2= -118.4456018 ; //mytracks
	   
	    String x1;
	   
	   String y1; 
	   double shadmag;
	 //  double shadowx;
	//   double shadowy;
	   double sidedist=1.324;
	   String responseString;
	   double angleDazimuth;
	   double angleDalti;
	   int shadow_counter;
	   double dist_sunny;
	   double dist2pts;
	   double speed = 1.5; // mytracks
	   double uv_avg;
	   double dist1,dist2;
	//"http://aa.usno.navy.mil/cgi-bin/aa_altazw.pl?FFX=1&obj=10&sun_us=10&xxy_us="+year+"&xxy="+year+"&xxm="+month+"&xxm_us="+month+"&xxd="+day+"&xxd_us="+day+"&xxi="+interval+"&xxi_us="+interval+"&st="+state+"&place="+place+"&placeus="+place;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final int type=1;
		final int uvvalue = 10;
		//Button btn1 = (Button)findViewById(R.id.btn);
	//	btn1.setOnClickListener(
	//		new OnClickListener(){
		//		@Override
			//	public void onClick(View arg0) {
					
		Button button= (Button) findViewById(R.id.btn1);
		button.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        remindersunscreen();
		    }
		});
		 
		
		Button btn = (Button)findViewById(R.id.btn);
		btn.setOnClickListener(
			new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					
					 
					//startService(new Intent(MainActivity.this,LocalService.class));
					
		           TextView ed = (TextView)findViewById(R.id.lbl);
		           dist2pts = getDistanceFromLatLonInm(lat1 ,lon1 ,lat2 ,lon2); //building
		           dist1 = getDistanceFromLatLonInm(lat2 ,lon1 ,lat2 ,lon2);
		           dist2 = getDistanceFromLatLonInm(lat1 ,lon1 ,lat2 ,lon1);
		           try {
						
				//	double time_vitd  =	alarm(uvvalue,type);
						responseString = new SiteSubmitAsyncTask().execute(url).get();
					   //ed.setText(responseString);
						String[] array = responseString.split("\n");
						int count = array.length;
						
					// Frist data entry at 24
						int i;
						for(i=0;i<=61;i++)
						{
						if ((array[i]).equalsIgnoreCase("Pacific Daylight Time"))
						{
						    //do something
							
							i=i+6;
							break;
						}
						
						}
						
						//ed.setText(array[i]);
						int f= 62-6;
						ed.setText(array[f]);
						String[][] table =new String[f-i+1][3];
						int x,p;
						p=i;
						
						for(x=0;x<=(f-i);x++)
						{
							String final1[]=array[p].split("\\s+");
							table[x][0]=final1[0];
							table[x][1]=final1[1];
							table[x][2]=final1[2];
							p++;
						}
						int b;
						Time today = new Time(Time.getCurrentTimezone());
						today.setToNow();
						int hour=today.hour;
						int minute=today.minute;
						
						String timer=timesupplied(minute,hour);
						
						for(b=0;b<=(f-i);b++)
						{
						if ((table[b][0]).equalsIgnoreCase(timer))
						{
						    //do something
							break;
					
						}
						}
						
					       double d1 = Double.parseDouble(table[b][1]);
					        double d2 = Double.parseDouble(table[b][2]);
						angleDazimuth=d2;
						angleDalti=d1;
						
						String angle= angleDazimuth+ "      "+angleDalti;
						shadowCalc();
						double timestamp0 = 0,timestamp1 =1.45;
						double distpath =distpath(speed,timestamp0,timestamp1);
					distpath=146.3;
						shadow_counter= castshadow(sidedist,shadmag,Double.parseDouble(x1),Double.parseDouble(y1),lat1,lat2,lon1,lon2); 
					//shadow_counter=castshadow(sidedist,shadmag);
						double build_len= shadow_dir(sidedist,shadmag,Double.parseDouble(x1),Double.parseDouble(y1),lat1,lat2,lon1,lon2)-sidedist;
						//double build_len= 17.5;
						double shadow_total= build_width+ build_len;
					dist_sunny =dist_sunny(shadow_total,shadow_counter,distpath);
					double uv_exp=uv_experienced(speed,dist_sunny,build_width,shadow_counter);
				/*	double building_angle =Math.atan2(dist1,dist2);
					building_angle=(building_angle*180)/Math.PI;
				//	double time_vitd  =	alarm(uv_avg,type);
					
					double shadow_angle =Math.atan2(Double.parseDouble(y1),Double.parseDouble(x1));
					shadow_angle=(shadow_angle*180)/Math.PI;
					double angle2= shadow_angle-building_angle;
				//angle2=(angle2*180)/Math.PI;

					*/
					
					ed.setText(Double.toString(shadow_counter));
					//ed.setText(angle);
						
						//ed.setText(Double.toString(time_vitd)+" min");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		);
	}

	
	private class SiteSubmitAsyncTask extends AsyncTask<String, String, String>{
		String month = "7",
		        day = "9",
		        year = "2013",
		        place = "Los Angeles",
		        interval = "30",
		        state = "CA";
		        String url ="http://aa.usno.navy.mil/cgi-bin/aa_altazw.pl";
		
	    @Override
	    protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(url);
	        String responseString="NOTHING";
	     // Execute HTTP Post Request
	        try{
	        	// Add your data
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(14);
	            nameValuePairs.add(new BasicNameValuePair("FFX", "1"));
	            nameValuePairs.add(new BasicNameValuePair("obj", "10"));
	            nameValuePairs.add(new BasicNameValuePair("sun_us", "10"));
	            nameValuePairs.add(new BasicNameValuePair("xxy_us", "2013"));
	            nameValuePairs.add(new BasicNameValuePair("xxy", "2013"));
	            nameValuePairs.add(new BasicNameValuePair("xxm_us", "7"));
	            nameValuePairs.add(new BasicNameValuePair("xxm", "7"));
	            nameValuePairs.add(new BasicNameValuePair("xxd_us", "9"));
	            nameValuePairs.add(new BasicNameValuePair("xxd", "9"));
	            nameValuePairs.add(new BasicNameValuePair("xxi_us", "30"));
	            nameValuePairs.add(new BasicNameValuePair("xxi", "30"));
	            nameValuePairs.add(new BasicNameValuePair("st", "CA"));
	            nameValuePairs.add(new BasicNameValuePair("place", "Los Angeles"));
	            nameValuePairs.add(new BasicNameValuePair("placeus", "Los Angeles"));
	
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            
	        	HttpResponse response = httpclient.execute(httppost);
	            
	            StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	                Log.v("JERRID",responseString);
	                //..more logic
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                Log.v("JERRID","I did not work");
	                responseString = "I did not work";
	                
	                throw new IOException(statusLine.getReasonPhrase());
	            }
	       } catch (ClientProtocolException e) {
	           // TODO Auto-generated catch block
	       } catch (IOException e) {
	           // TODO Auto-generated catch block
	       }
            return responseString;
	    }
	
	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        //Do anything with response..
	    }
	}
	 public void shadowCalc()
     {
		
	//	 angleDazimuth = 189.6;// angle in degree (need to be taken from table automatically)
		 double angleRazimuth = angleDazimuth * Math.PI / 180.0;
     
     //	angleDalti = 77.9;// angle in degree (need to be taken from table automatically)
     	double angleRalti = angleDalti * Math.PI / 180.0;
     
     	double angleD180 = 180.0;// angle in degree
     	double angleR180 = angleD180 * Math.PI / 180.0;
     
     	double h=33.28; //height 
     
     	double x=Math.sin(angleRazimuth-angleR180)/Math.tan(angleRalti)*h;
     	double y=Math.cos(angleRazimuth-angleR180)/Math.tan(angleRalti)*h;
     shadmag=h/Math.tan(angleRalti);
     
     	x1= Double.toString(x);
     	y1=Double.toString(y);
     
     	TextView textx = (TextView) findViewById(R.id.lblx);
		textx.setText("x="+x1);
		
		TextView texty = (TextView) findViewById(R.id.lbly);
		texty.setText("y="+y1);
		
     }
	 public String timesupplied(int min,int hr)
	 {
		 String s;
		 if(min<=15)
		 {
			
			 min=00;
		 }
		 else if (min>15 && min <=30)
		 {
			 min=30;
		 }
		 else if (min>30 && min <=45)
		 {
			min=30 ;
		 }
		 else if (min>45 && min <=59)
		 {
			 hr=hr+1;
			 min=00;
		 }
		 if(hr>9 && min==30)
		 {
		 s=hr+":"+min;
		 }
		 else if(hr>9 && min==0)
		 {
			 
			 s=hr+":"+min+"0";
		 }
		 else if(hr<=9 && min==30)
		 {
			 s="0"+hr+":"+min;
		 }
		 else
			 s="0"+hr+":"+min+"0";
		 return s;
	 }

	 public double alarm(double uvvalue,int type)
	 {
		 double time;
		 int maxvalue=12;
		 time=10*type*maxvalue/uvvalue; //Person Will acquire Sufficient  IU for a day .(Building not considered)
		 time=time*60*100;
		// Display user with notification
		 
		 AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

		 dlgAlert.setMessage("Your Timer Is started.Will inform you,when you acquire the suffcient VitD");
		 dlgAlert.setTitle("Timer Started");
		 dlgAlert.setPositiveButton("OK", null);
		 dlgAlert.setCancelable(true);
		dlgAlert.create().show();
		final Handler handler = new Handler();
		 final AlertDialog.Builder dlgAlert1  = new AlertDialog.Builder(this);
		handler.postDelayed(new Runnable() {
			
		  @Override
		  
		  public void run() {
		    //Do something after 100ms
			  TextView ed1 = (TextView)findViewById(R.id.lbl);
	        	// ed1.setText("ssss");
	        	

	  		 dlgAlert1.setMessage("Done :)");
	  		 dlgAlert1.setTitle("Timer Finised");
	    		 dlgAlert1.setPositiveButton("OK", null);
	    	 dlgAlert1.setCancelable(true);
	    		dlgAlert1.create().show();
		  }
		}, (long) time);

		
		 // After time (for eg 20min) we have to blow the Noitfication again informing the user about the time.For this we need to start the service
		 //Which starts at starting and stops after time 20min according to this case .
		 
		 return time ;
		 
	 }
	 
	 double getDistanceFromLatLonInm(double lat1 ,double lon1 ,double lat2 ,double lon2) {
		  double R = 6371; // Radius of the earth in km
		  double dLat = deg2rad(lat2-lat1);  // deg2rad below
		  double dLon = deg2rad(lon2-lon1); 
		  double a = 
		    Math.sin(dLat/2) * Math.sin(dLat/2) +
		    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
		    Math.sin(dLon/2) * Math.sin(dLon/2)
		    ; 
		  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		  double d = R * c; // Distance in km
		  return (d*1000);
		}

		double deg2rad(double deg) {
		  return deg * (Math.PI/180);
		}
		    
	  
			int castshadow(double sidedist,double shadow_mag,double shadowx,double shadowy,double lat1,double lat2,double lon1,double lon2) {
				
				double shadow_angle =Math.atan2(shadowy,shadowx);
				shadow_angle=(shadow_angle*180)/Math.PI;
				double building_angle =Math.atan2(dist1,dist2);
				building_angle=(building_angle*180)/Math.PI;
				
				double angle= shadow_angle-building_angle;
			//	angle=(angle*180)/Math.PI;
				double shadow_length=shadow_mag*Math.cos(Math.toRadians(angle));
				
				if(shadow_length>=sidedist)
				{	
					return 1;
				}
				else
				{
					return 0;
				}
			
			//	return shadow_length;
			
			}
			double shadow_dir(double sidedist,double shadow_mag,double shadowx,double shadowy,double lat1,double lat2,double lon1,double lon2) {
				
				double shadow_angle =Math.atan2(shadowy,shadowx);
				shadow_angle=(shadow_angle*180)/Math.PI;
				double building_angle =Math.atan2(dist1,dist2);
				building_angle=(building_angle*180)/Math.PI;
				
				double angle= shadow_angle-building_angle;
			//	angle=(angle*180)/Math.PI;
				double shadow_length_indir=shadow_mag*Math.cos(Math.toRadians(angle));
				
				return shadow_length_indir;
			
			//	return shadow_length;
			
			}
		
		double dist_sunny(double shadow_total,int shadow_counter,double distpath)
		{
			double distancesunny;
			if(shadow_counter==0)
			{
				distancesunny=distpath;
			}
			else
			{
				distancesunny=distpath-shadow_total;
			}
			
			return distancesunny;
		}
		double uv_experienced(double speed,double dist_sunny,double build_width,int shadow_counter)
		{
			double uv_exp;
			int uv_sun=10; // sensor
			int uv_building=1; //sensor
			double time= dist_sunny/speed;
			double timebuild=0;
			if(shadow_counter==1)
			{
				uv_exp= uv_sun*time;
			}
			else
			{
				timebuild= build_width/speed;
				uv_exp= uv_sun*time+uv_building*timebuild;
			}
			uv_avg= uv_exp/(time+timebuild);
			return uv_exp;
		}
		double distpath(double speed, double timestamp0,double timestamp1)
		{
			double dist1=0;
			double totaltime=timestamp1-timestamp0;
			dist1=speed*totaltime;
		
			return dist1;
		}

	void remindersunscreen()
	{
		 double time;
	
		 time=2*60*60*1000;
		// Display user with notification
		 
		 AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

		 dlgAlert.setMessage("You will be notified after 2hours");
		 dlgAlert.setTitle("Timer Started");
		 dlgAlert.setPositiveButton("OK", null);
		 dlgAlert.setCancelable(true);
		dlgAlert.create().show();
		final Handler handler = new Handler();
		 final AlertDialog.Builder dlgAlert1  = new AlertDialog.Builder(this);
		handler.postDelayed(new Runnable() {
			
		  @Override
		  
		  public void run() {
		    //Do something after 100ms
			  TextView ed1 = (TextView)findViewById(R.id.lbl);
	        	// ed1.setText("ssss");
	        	

	  		 dlgAlert1.setMessage(" time to apply sunscreen to protect Damage");
	  		 dlgAlert1.setTitle("Timer Finised");
	    		 dlgAlert1.setPositiveButton("OK", null);
	    	 dlgAlert1.setCancelable(true);
	    		dlgAlert1.create().show();
		  }
		}, (long) time);

		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
