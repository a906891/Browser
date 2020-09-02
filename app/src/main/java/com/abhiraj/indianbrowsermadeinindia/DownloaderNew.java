package com.abhiraj.indianbrowsermadeinindia;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class DownloaderNew extends AppCompatActivity implements ItemClickListener {

    private static final int PERMISSION_REQUEST_CODE = 101;
    DownloadAdapaterNew downloadAdapater;
    List<DownloadModelNew> downloadModels = new ArrayList<>();
    Realm realm;
    String urlsss = "",filename="";

    private static final int RESULT_DEFAULT = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader_new);

        urlsss = getIntent().getStringExtra("urlsss");
        filename = getIntent().getStringExtra("filenames");

        realm = Realm.getDefaultInstance();
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        Button add_download_list = (Button) findViewById(R.id.add_download_list);
        RecyclerView data_list = (RecyclerView) findViewById(R.id.data_list);

        add_download_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        List<DownloadModelNew> downloadModellocal = getAllDownload();
        if (downloadModellocal != null) {
            if (downloadModellocal.size() > 0) {
                downloadModels.addAll(downloadModellocal);
                for (int i = 0; i < downloadModels.size(); i++) {
                    if (downloadModels.get(i).getStatus().equalsIgnoreCase("Pending") || (downloadModels.get(i).getStatus().equalsIgnoreCase("Running") || (downloadModels.get(i).getStatus().equalsIgnoreCase("Downloading")))) {
                        DownloadStatusTask downloadStatusTask = new DownloadStatusTask(downloadModels.get(i));
                        runTask(downloadStatusTask, "" + downloadModels.get(i).getDownloadID());
                    }
                }
            }
        }
        downloadAdapater = new DownloadAdapaterNew(DownloaderNew.this, downloadModels, DownloaderNew.this);
        data_list.setLayoutManager(new LinearLayoutManager(DownloaderNew.this));
        data_list.setAdapter(downloadAdapater);

        Intent intent=getIntent();
        if(intent!=null){
            String action=intent.getAction();
            String type=intent.getType();
            if(Intent.ACTION_SEND.equals(action) && type!=null){
                if(type.equalsIgnoreCase("text/plain")){
                    handleTextData(intent);
                }
                else if(type.startsWith("image/")){
                    handleImage(intent);
                }
                else if(type.equalsIgnoreCase("application/pdf")){
                    handlePdfFile(intent);
                }
            }
            else if(Intent.ACTION_SEND_MULTIPLE.equals(action) && type!=null){
                if(type.startsWith("image/")){
                    handleMultipleImage(intent);
                }
            }
        }
        if(urlsss != null && filename != null)
        {
            downloadFile(urlsss,filename);
        }

        setResult(RESULT_DEFAULT);

    }

    private void handlePdfFile(Intent intent) {
        Uri pdffile=intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if(pdffile!=null) {
            Log.d("Pdf File Path : ", "" + pdffile.getPath());
        }
    }

    private void handleImage(Intent intent) {
        Uri image=intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if(image!=null) {
            Log.d("Image File Path : ", "" + image.getPath());
        }
    }

    private void handleTextData(Intent intent) {
        String textdata=intent.getStringExtra(Intent.EXTRA_TEXT);
        if(textdata!=null) {
            Log.d("Text Data : ", "" + textdata);
        }
    }

    private void handleMultipleImage(Intent intent) {
        ArrayList<Uri> imageList=intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if(imageList!=null) {
            for (Uri uri : imageList) {
                Log.d("Path ",""+uri.getPath());
            }
        }
    }



    private void showInputDialog() {
        AlertDialog.Builder al = new AlertDialog.Builder(DownloaderNew.this);
        View view = getLayoutInflater().inflate(R.layout.input_dialog_n, null);
        al.setView(view);


        final EditText edittext = view.findViewById(R.id.input);
        Button paste = view.findViewById(R.id.paste);

        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                try {
                    CharSequence charSequence = clipboardManager.getPrimaryClip().getItemAt(0).getText();
                    edittext.setText(charSequence);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        al.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadFile(urlsss,filename);
            }
        });
        al.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        al.show();
    }

    private void downloadFile(String url, String filename) {

        String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        File file = new File(downloadPath, filename);

        DownloadManager.Request request = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request = new DownloadManager.Request(Uri.parse(url))
                    .setTitle(filename)
                    .setDescription("Downloading")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setDestinationUri(Uri.fromFile(file))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);
        } else {
            request = new DownloadManager.Request(Uri.parse(url))
                    .setTitle(filename)
                    .setDescription("Downloading")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setDestinationUri(Uri.fromFile(file))
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);
        }

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);

        ///downloads counting
        Number currentnumber = realm.where(DownloadModelNew.class).max("id");
        int nextId;

        if (currentnumber == null) {
            nextId = 1;
        } else {
            nextId = currentnumber.intValue() + 1;
        }


        final DownloadModelNew downloadmodel = new DownloadModelNew();
        downloadmodel.setId(nextId);
        downloadmodel.setStatus("Downloading");
        downloadmodel.setTitle(filename);
        downloadmodel.setFile_size("0");
        downloadmodel.setProgress("0");
        downloadmodel.setIs_paused(false);
        downloadmodel.setDownloadID(downloadId);
        downloadmodel.setFile_path("");

        downloadModels.add(downloadmodel);
        downloadAdapater.notifyItemInserted(downloadModels.size() - 1);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(downloadmodel);
            }
        });


        DownloadStatusTask downloadStatusTask = new DownloadStatusTask(downloadmodel);
        runTask(downloadStatusTask, "" + downloadId);
    }

    @Override
    public void onClickitem(String file_path) {
        Log.d("File Path", "" + file_path);
        openFile(file_path);
    }

    public class DownloadStatusTask extends AsyncTask<String, String, String> {

        DownloadModelNew downloadModel;

        public DownloadStatusTask(DownloadModelNew downloadModel) {
            this.downloadModel = downloadModel;

        }

        @Override
        protected String doInBackground(String... strings) {
            downloadFileProcess(strings[0]);
            return null;
        }

        private void downloadFileProcess(String downloadId) {
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            boolean downloading = true;
            while (downloading) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(Long.parseLong(downloadId));
                Cursor cursor = downloadManager.query(query);
                cursor.moveToFirst();

                int bytes_download = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int total_size = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false;
                }

                int progress = (int) ((bytes_download * 100L) / total_size);
                String status = getStatusMessage(cursor);
                publishProgress(new String[]{String.valueOf(progress), String.valueOf(bytes_download), status});
                cursor.close();
            }
        }

        @Override
        protected void onProgressUpdate(final String... values) {
            super.onProgressUpdate(values);

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {


                    downloadModel.setFile_size(bytesIntoHumanReadable(Long.parseLong(values[1])));
                    downloadModel.setProgress(values[0]);
                    if (!downloadModel.getStatus().equalsIgnoreCase("PAUSE") && !downloadModel.getStatus().equalsIgnoreCase("RESUME")) {
                        downloadModel.setStatus(values[2]);
                    }
                    downloadAdapater.changeITem(downloadModel.getDownloadID());
                }
            });

        }

    }

    private String getStatusMessage(Cursor cursor) {
        String msg = "-";
        switch (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                msg = "Failed";
                break;
            case DownloadManager.STATUS_PAUSED:
                msg = "Paused";
                break;
            case DownloadManager.STATUS_RUNNING:
                msg = "Running";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Completed";
                break;
            case DownloadManager.STATUS_PENDING:
                msg = "Pending";
                break;
            default:
                msg = "Unknown";
                break;

        }
        return msg;
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            boolean comp = downloadAdapater.ChangeItemWithStatus("Completed", id);

            if (comp) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);

                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(id));
                cursor.moveToFirst();

                String download_path = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                downloadAdapater.setChangeItemPath(download_path, id);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back and back", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void runTask(DownloadStatusTask downloadStatusTask, String id) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                downloadStatusTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{id});
            } else {
                downloadStatusTask.execute(new String[]{id});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String bytesIntoHumanReadable(long bytes) {
        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;
        long gigabyte = megabyte * 1024;
        long terabyte = gigabyte * 1024;

        if ((bytes >= 0) && (bytes < kilobyte)) {
            return bytes + " B";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return (bytes / kilobyte) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return (bytes / megabyte) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return (bytes / gigabyte) + " GB";

        } else if (bytes >= terabyte) {
            return (bytes / terabyte) + " TB";

        } else {
            return bytes + " Bytes";
        }
    }

    private RealmResults<DownloadModelNew> getAllDownload() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(DownloadModelNew.class).findAll();
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(DownloaderNew.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(DownloaderNew.this, "Please Give Permission to Upload File", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(DownloaderNew.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(DownloaderNew.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(DownloaderNew.this, "Permission Successfull", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DownloaderNew.this, "Permission Failed", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void openFile(String fileurl) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
                Toast.makeText(this, "Please Allow Permission to Open File", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        try {
            fileurl = PathUtil.getPath(DownloaderNew.this, Uri.parse(fileurl));

            File file = new File(fileurl);
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String ext = mimeTypeMap.getFileExtensionFromUrl(file.getName());
            String type = mimeTypeMap.getMimeTypeFromExtension(ext);

            if (type == null) {
                type = "*/*";
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contne = FileProvider.getUriForFile(DownloaderNew.this, "com.abhiraj.indianbrowsermadeinindia", file);
                intent.setDataAndType(contne, type);

            } else {
                intent.setDataAndType(Uri.fromFile(file), type);
            }
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "Unable to Open File", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    
}