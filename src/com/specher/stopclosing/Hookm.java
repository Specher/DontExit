package com.specher.stopclosing;





import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Process;
import android.content.IntentFilter;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hookm implements IXposedHookLoadPackage,IXposedHookZygoteInit{

    private static String PKG_NAME ="com.specher.stopclosing";
    private static String TAG ="Specher";
    private XSharedPreferences global_prefs;

    
    @Override
	public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam paramLoadPackageParam) throws Throwable {
		// TODO Auto-generated method stub
    	final String mpackage = paramLoadPackageParam.packageName;
    	
    	global_prefs.reload();
  
//    	final boolean current_app_tag = global_prefs.getBoolean(mpackage,false);
//    	if (!current_app_tag)
//    	return;
	
    	
    	if (mpackage.equals(PKG_NAME)){
   		 
            XposedHelpers.findAndHookMethod(PKG_NAME+".HookMe", paramLoadPackageParam.classLoader, "isModuleActive", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              
              	   param.setResult(true);
                }
            }); 
  	  }
  	  	
    	
			//XposedBridge.log(TAG+":Loaded "+mpackage);
			

			
//	    	 XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
//	             @Override
//	             protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//	                 ClassLoader cl = ((Context)param.args[0]).getClassLoader();
	                	
	                 
	                XposedHelpers.findAndHookMethod(Activity.class,
	                		"finish",new XC_MethodHook() {
	                	protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
	                
	                		global_prefs.reload();
							//Boolean ison=	global_prefs.getBoolean(mpackage,false);
							Boolean ison=	global_prefs.getBoolean(mpackage,false);;
							if(ison){
								XposedBridge.log(TAG+":��ֹ "+mpackage+"����Acitvity.finish");
								 Object t = new XC_MethodReplacement() {
									@Override
									protected Object replaceHookedMethod(MethodHookParam param)
											throws Throwable {
										// TODO �Զ����ɵķ������
										return null;
									}
								};
							param.setResult(t);
							}else{
								XposedBridge.log(TAG+" "+mpackage+"����Acitvity.finish");
							}
	                		
	                	};
	                	});
	                 
	                XposedHelpers.findAndHookMethod(Runtime.class,
	                		"exit", int.class,new XC_MethodHook() {
	                	protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
	                
	                		global_prefs.reload();
							Boolean ison=	global_prefs.getBoolean(mpackage,false);
							if(ison){
								XposedBridge.log(TAG+":��ֹ "+mpackage+"����System.exit");
								 Object t = new XC_MethodReplacement() {
									@Override
									protected Object replaceHookedMethod(MethodHookParam param)
											throws Throwable {
										// TODO �Զ����ɵķ������
										return null;
									}
								};
							param.setResult(t);
							}else{
								XposedBridge.log(TAG+" "+mpackage+"����System.exit");
							}
	                		
	                	};

								
							});
	                
	                XposedHelpers.findAndHookMethod(Process.class, "killProcess", int.class,new XC_MethodHook() {
	                	protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
	                
	                		global_prefs.reload();
							Boolean ison= global_prefs.getBoolean(mpackage,false);;
							if(ison){
								XposedBridge.log(TAG+":��ֹ "+mpackage+"����killProcess");
								 Object t = new XC_MethodReplacement() {
									@Override
									protected Object replaceHookedMethod(MethodHookParam param)
											throws Throwable {
										param.args[0] = 0;
										return null;
									}
								};
							param.setResult(t);
							}else{
								XposedBridge.log(TAG+" "+mpackage+"����killProcess");
							}
	                		
	                	};
					});
				
	                XposedHelpers.findAndHookMethod(ActivityManager.class, "killBackgroundProcesses", String.class,new XC_MethodHook() {
	                	protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
	                
	                		global_prefs.reload();
							Boolean ison= global_prefs.getBoolean(mpackage,false);
							if(ison){
								XposedBridge.log(TAG+":��ֹ "+mpackage+"����killBackgroundProcesses");
								 Object t = new XC_MethodReplacement() {
									@Override
									protected Object replaceHookedMethod(MethodHookParam param)
											throws Throwable {
										// TODO �Զ����ɵķ������
										return null;
									}
								};
							param.setResult(t);
							}else{
								XposedBridge.log(TAG+" "+mpackage+"����killBackgroundProcesses");
							}
	                		
	                	};
					});
	                
	                XposedHelpers.findAndHookMethod(Process.class, "killProcessQuiet", int.class,new XC_MethodHook() {
	                	protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
	                
	                		global_prefs.reload();
							Boolean ison= global_prefs.getBoolean(mpackage,false);
							if(ison){
								XposedBridge.log(TAG+":��ֹ "+mpackage+"����killProcessQuiet");
								 Object t = new XC_MethodReplacement() {
									@Override
									protected Object replaceHookedMethod(MethodHookParam param)
											throws Throwable {
										// TODO �Զ����ɵķ������
										param.args[0] = 0;
										return null;
									}
								};
							param.setResult(t);
							}else{
								XposedBridge.log(TAG+" "+mpackage+"����killProcessQuiet");
							}
	                		
	                	};
					});
	                
	                XposedHelpers.findAndHookMethod(Service.class, "stopSelf", int.class,new XC_MethodHook() {
	                	protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
	                
	                		global_prefs.reload();
	                		
							Boolean ison= global_prefs.getBoolean(mpackage,false);
							if(ison){
								XposedBridge.log(TAG+":��ֹ "+mpackage+"����stopSelf");
								 Object t = new XC_MethodReplacement() {
									@Override
									protected Object replaceHookedMethod(MethodHookParam param)
											throws Throwable {
										// TODO �Զ����ɵķ������
										param.args[0] = 0;
										return null;
									}
								};
							param.setResult(t);
							}else{
								XposedBridge.log(TAG+" "+mpackage+"����stopSelf");
							}
	                		
	                	};
					});
	                
	                XposedHelpers.findAndHookMethod(ContextWrapper.class, "stopService", Intent.class,new XC_MethodHook() {
	                	protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
	                
	                		global_prefs.reload();
	                		
							Boolean ison= global_prefs.getBoolean(mpackage,false);
							if(ison){
								XposedBridge.log(TAG+":��ֹ "+mpackage+"����stopService");
								 Object t = new XC_MethodReplacement() {
									@Override
									protected Object replaceHookedMethod(MethodHookParam param)
											throws Throwable {
										// TODO �Զ����ɵķ������
										param.args[0] = 0;
										return null;
									}
								};
							param.setResult(t);
							}else{
								XposedBridge.log(TAG+" "+mpackage+"����stopService");
							}
	                		
	                	};
					}); 
	                
//	             }
//	    	 
//	             }
//	             );
	    	 
		
}

	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		// TODO �Զ����ɵķ������
		 global_prefs = new XSharedPreferences(PKG_NAME);
         global_prefs.makeWorldReadable();
		
	}
    
    
    
}