package com.specher.stopclosing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	AppListAdapter appListAdapter;
	private ArrayList<ApplicationInfo> appList = new ArrayList<ApplicationInfo>();
	private ArrayList<ApplicationInfo> filteredAppList = new ArrayList<ApplicationInfo>();
	private String nameFilter;
	private SharedPreferences prefs;
	boolean IsshowAllapps;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		IsshowAllapps = false;
		prefs = getSharedPreferences("com.specher.stopclosing_preferences", Context.MODE_WORLD_READABLE);

		final ListView list = (ListView) findViewById(R.id.lstApps);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Open settings activity when clicking on an application
				String pkgName = ((TextView) view.findViewById(R.id.app_pkg)).getText().toString();
				prefs.edit().putBoolean(pkgName, !prefs.getBoolean(pkgName, false)).
				commit();
				appListAdapter.notifyDataSetChanged();
			}
		});
		
		refreshApps();
		showMoudleState();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.action_refresh:
			IsshowAllapps=!IsshowAllapps;
			refreshApps();
			item.setChecked(IsshowAllapps);
			return true;	
		case R.id.action_about:
			startActivity(new Intent(this, AboutActivity.class));
			return true;
		case R.id.action_exit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	

	private void refreshApps() {
		appList.clear();
		// (re)load the list of apps in the background
		new PrepareAppsAdapter().execute();
	}


	// Handle background loading of apps
	private class PrepareAppsAdapter extends AsyncTask<Void,Void,AppListAdapter> {
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(((ListView) findViewById(R.id.lstApps)).getContext());
			dialog.setMessage(getString(R.string.app_loading));
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected AppListAdapter doInBackground(Void... params) {
			if (appList.size() == 0) {
				loadApps(dialog);
			}
			return null;
		}

		@Override
		protected void onPostExecute(final AppListAdapter result) {
			prepareAppList();

			try {
				dialog.dismiss();
			} catch (Exception e) {

			}
		}
	}

	   private void redirectToXposed() {
		   try{
	        ComponentName componentName = new ComponentName("de.robv.android.xposed.installer",
	                "de.robv.android.xposed.installer.WelcomeActivity");
	        Intent intent = new Intent();
	        intent.setComponent(componentName);
	        startActivity(intent);
		   }catch(Exception e){
			   log("����ʧ�ܣ�����δ��װXposed��������ٶ���Ļ���+Xposed��ܰ�װ��");
		   }
	    }
	
	   private void showMoudleState() {
	        if (!HookMe.isModuleActive())
	            DialogUtil.showAlertlr(this, getString(R.string.tips), getString(R.string.module_active_tips), getString(R.string.ignore), null, getString(R.string.open_xposed), new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialogInterface, int i) {
	                    redirectToXposed();
	                }
	            });
	    }	
	   
	   
	   
	   public void log(String paramString) {
			 Toast.makeText(getApplicationContext(), paramString,Toast.LENGTH_SHORT).show();
		 }

	private class AppListFilter extends Filter {

		private AppListAdapter adapter;

		AppListFilter(AppListAdapter adapter) {
			super();
			this.adapter = adapter;
		}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// NOTE: this function is *always* called from a background thread, and
			// not the UI thread.

			ArrayList<ApplicationInfo> items = new ArrayList<ApplicationInfo>();
			synchronized (this) {
				items.addAll(appList);
			}


			FilterResults result = new FilterResults();
			if (constraint != null && constraint.length() > 0) {
				Pattern regexp = Pattern.compile(constraint.toString(), Pattern.LITERAL | Pattern.CASE_INSENSITIVE);
				for (Iterator<ApplicationInfo> i = items.iterator(); i.hasNext(); ) {
					ApplicationInfo app = i.next();
					if (!regexp.matcher(app.name == null ? "" : app.name).find()
							&& !regexp.matcher(app.packageName).find()) {
						i.remove();
					}
				}
			}
			result.values = items;
			result.count = items.size();
			return result;
		}



		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			// NOTE: this function is *always* called from the UI thread.
			filteredAppList = (ArrayList<ApplicationInfo>) results.values;
			adapter.notifyDataSetChanged();
			adapter.clear();
			for (int i = 0, l = filteredAppList.size(); i < l; i++) {
				adapter.add(filteredAppList.get(i));
			}
			adapter.notifyDataSetInvalidated();
		}
	}

	
	
	
	@SuppressLint("DefaultLocale")
	private void loadApps(ProgressDialog dialog) {
		appList.clear();
		PackageManager pm = getPackageManager();
		List<PackageInfo> pkgs = getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);
		dialog.setMax(pkgs.size());
		int i = 1;
		
//		 ����һ�����ΪCATEGORY_LAUNCHER�ĸð�����Intent
	    Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
	    resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		 // ͨ��getPackageManager()��queryIntentActivities��������,�õ������ܴ򿪵�app��packageName
	    List<ResolveInfo>  resolveinfoList = getPackageManager()
	            .queryIntentActivities(resolveIntent, 0);
	    Set<String> allowPackages=new HashSet();
	    for (ResolveInfo resolveInfo:resolveinfoList){
	        allowPackages.add(resolveInfo.activityInfo.packageName);
	    }
		if(!IsshowAllapps){
	    for (PackageInfo pkgInfo:pkgs) {
	    	dialog.setProgress(i++);
	        if (allowPackages.contains(pkgInfo.packageName)){
	        	ApplicationInfo appInfo = pkgInfo.applicationInfo;
	        	appInfo.name = appInfo.loadLabel(pm).toString();
	        	appInfo.name = appInfo.loadLabel(pm).toString();
	        	appList.add(appInfo);
	        }
	    }
		}else{
		for (PackageInfo pkgInfo : pkgs) {
			dialog.setProgress(i++);
			ApplicationInfo appInfo = pkgInfo.applicationInfo;
			if (appInfo == null)
				continue;
			appInfo.name = appInfo.loadLabel(pm).toString();
			appList.add(appInfo);
		}
		}
		Collections.sort(appList, new Comparator<ApplicationInfo>() {
			@Override
			public int compare(ApplicationInfo lhs, ApplicationInfo rhs) {
				if (lhs.name == null) {
					return -1;
				} else if (rhs.name == null) {
					return 1;
				} else {
					return lhs.name.toUpperCase().compareTo(rhs.name.toUpperCase());
				}
			}
		});
	}

	private void prepareAppList() {
		appListAdapter = new AppListAdapter(this, appList);
		((ListView) findViewById(R.id.lstApps)).setAdapter(appListAdapter);
		appListAdapter.getFilter().filter(nameFilter);
		((SearchView) findViewById(R.id.searchApp)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				nameFilter = query;
				appListAdapter.getFilter().filter(nameFilter);
				((SearchView) findViewById(R.id.searchApp)).clearFocus();
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				nameFilter = newText;
				appListAdapter.getFilter().filter(nameFilter);
				return false;
			}

		});

	}
	
	static class AppListViewHolder {
		TextView app_name;
		TextView app_package;
		//TextView app_ver;
		ImageView app_ico;
		AsyncTask<AppListViewHolder, Void, Drawable> imageLoader;
	}

	class AppListAdapter extends ArrayAdapter<ApplicationInfo> implements SectionIndexer {

		private Map<String, Integer> alphaIndexer;
		private String[] sections;
		private Filter filter;
		private LayoutInflater inflater;


		@SuppressLint("DefaultLocale")
		public AppListAdapter(Context context, List<ApplicationInfo> items) {
			super(context, R.layout.appinfoview, new ArrayList<ApplicationInfo>(items));

			filteredAppList.addAll(items);

			filter = new AppListFilter(this);
			inflater = getLayoutInflater();	
			alphaIndexer = new HashMap<String, Integer>();
			for (int i = filteredAppList.size() - 1; i >= 0; i--) {
				ApplicationInfo app = filteredAppList.get(i);
				String appName = app.name;
				String firstChar;
				if (appName == null || appName.length() < 1) {
					firstChar = "@";
				} else {
					firstChar = appName.substring(0, 1).toUpperCase();
					if (firstChar.charAt(0) > 'Z' || firstChar.charAt(0) < 'A')
						firstChar = "@";
				}

				alphaIndexer.put(firstChar, i);
			}

			Set<String> sectionLetters = alphaIndexer.keySet();

			// create a list from the set to sort
			List<String> sectionList = new ArrayList<String>(sectionLetters);
			Collections.sort(sectionList);
			sections = new String[sectionList.size()];
			sectionList.toArray(sections);
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Load or reuse the view for this row
		
			AppListViewHolder holder;
			if (convertView == null) {
				holder = new AppListViewHolder();
				convertView = inflater.inflate(R.layout.appinfoview, parent, false);
				holder.app_name = (TextView) convertView.findViewById(R.id.app_name);
				holder.app_package = (TextView) convertView.findViewById(R.id.app_pkg);
			//	holder.app_ver = (TextView) row.findViewById(R.id.app_version);
				holder.app_ico = (ImageView)convertView.findViewById(R.id.app_icon);
				convertView.setTag(holder);
			} else {
				holder = (AppListViewHolder) convertView.getTag();
				holder.imageLoader.cancel(true);
			}

			final ApplicationInfo app = filteredAppList.get(position);

			holder.app_name.setText(app.name == null ? "" : app.name);
			
			//holder.app_ver.setText(app.name == null ? "" : packageManager.getPackageInfo(app.packageName, 0).versionName);

			holder.app_package.setTextColor(prefs.getBoolean(app.packageName, false)
					? Color.RED : Color.parseColor("#0099CC"));
			holder.app_package.setText(app.packageName);


			if (app.enabled) {
				holder.app_name.setPaintFlags(holder.app_name.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
				holder.app_package.setPaintFlags(holder.app_package.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
			} else {
				holder.app_name.setPaintFlags(holder.app_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
				holder.app_package.setPaintFlags(holder.app_package.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			}

			holder.imageLoader = new AsyncTask<AppListViewHolder, Void, Drawable>() {

				@Override
				protected Drawable doInBackground(AppListViewHolder... params) {
					return app.loadIcon(getPackageManager());
				}

			}.execute(holder);
			
			try {
				holder.app_ico.setImageDrawable(holder.imageLoader.get());
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			
			return convertView;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public void notifyDataSetInvalidated() {
			alphaIndexer.clear();
			for (int i = filteredAppList.size() - 1; i >= 0; i--) {
				ApplicationInfo app = filteredAppList.get(i);
				String appName = app.name;
				String firstChar;
				if (appName == null || appName.length() < 1) {
					firstChar = "@";
				} else {
					firstChar = appName.substring(0, 1).toUpperCase();
					if (firstChar.charAt(0) > 'Z' || firstChar.charAt(0) < 'A')
						firstChar = "@";
				}
				alphaIndexer.put(firstChar, i);
			}

			Set<String> keys = alphaIndexer.keySet();
			Iterator<String> it = keys.iterator();
			ArrayList<String> keyList = new ArrayList<String>();
			while (it.hasNext()) {
				keyList.add(it.next());
			}

			Collections.sort(keyList);
			sections = new String[keyList.size()];
			keyList.toArray(sections);

			super.notifyDataSetInvalidated();
		}

		@Override
		public int getPositionForSection(int section) {
			if (section >= sections.length)
				return filteredAppList.size() - 1;

			return alphaIndexer.get(sections[section]);
		}

		@Override
		public int getSectionForPosition(int position) {

			// Iterate over the sections to find the closest index
			// that is not greater than the position
			int closestIndex = 0;
			int latestDelta = Integer.MAX_VALUE;

			for (int i = 0; i < sections.length; i++) {
				int current = alphaIndexer.get(sections[i]);
				if (current == position) {
					// If position matches an index, return it immediately
					return i;
				} else if (current < position) {
					// Check if this is closer than the last index we inspected
					int delta = position - current;
					if (delta < latestDelta) {
						closestIndex = i;
						latestDelta = delta;
					}
				}
			}
			return closestIndex;
		}

		@Override
		public Object[] getSections() {
			return sections;
		}

		@Override
		public Filter getFilter() {
			return filter;
		}
	}
}

