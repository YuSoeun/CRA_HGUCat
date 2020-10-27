package com.example.CRA_HGUCat.CameraCapture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.View;

import com.example.CRA_HGUCat.CatCommunity.CommunityAdd;
import com.example.CRA_HGUCat.R;
import com.google.android.material.snackbar.Snackbar;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CaptureCat extends AppCompatActivity {

    private TextureView cameraView;
    private Size imageDimension;
    CameraCaptureSession session;
    CaptureRequest.Builder builder;
    OrientationListener oriListen;
    CameraDevice cameradevice;
    File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_cat);

        cameraView = findViewById(R.id.cameraView);
        assert cameraView != null;
        cameraView.setRotation(-90f);

        cameraView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                openCamera();
                // 텍스쳐뷰가 사용가능하면 카메라를 실행
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

            }
        });

        oriListen = new OrientationListener(this);
    }

    @Override
    public void onStart() {
        oriListen.enable();
        super.onStart();
    }

    @Override
    public void onStop() {
        oriListen.disable();
        super.onStop();
    }

    public class OrientationListener extends OrientationEventListener {

        int rotation = 0;

        public OrientationListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if ((orientation < 35 || orientation > 325) && rotation != 0) {
                rotation = 0;
                cameraView.setRotation(-90f);
            }
            else if (orientation > 145 && orientation < 215 && rotation != 2) {
                rotation = 2;
                cameraView.setRotation(-90f);
            }
            else if (orientation > 55 && orientation < 125 && rotation != 3) {
                rotation = 3;
                cameraView.setRotation(180f);
            }
            else if (orientation > 235 && orientation < 305 && rotation != 1) {
                rotation = 1;
                cameraView.setRotation(180f);
            }
        }
        // 가로와 세로의 기울기에 따른 텍스쳐뷰 회전으로, 가로인 경우 더 넓은 화면을 지원
    }

    public void onClick(View v)
    {
        UploadPicture2Directory();
    }

    void UploadPicture2Directory() {
        final File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/Hannyangmanyo");
        // 최초의 directory에서 documents의 Hannyangmanyo 폴더에 접근
        if (!dir.exists())
            dir.mkdir();
        // 만약 Hannyangmanyo 폴더가 없는 경우 폴더를 추가한다

        new Thread() {
            @Override
            public void run() {
            Matrix rotation = new Matrix();
            rotation.postRotate(-90);
            Bitmap bitmap = cameraView.getBitmap();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotation, false);

            try {
                imgFile = new File(dir, new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".png");
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 95, outputStream);
                byte[] data = outputStream.toByteArray();

                FileOutputStream fos = new FileOutputStream(imgFile);
                fos.write(data);
                fos.flush();
                fos.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            }
        }.start();
        // main Thread에서 모든 작업을 수행하면 폰이 버티지 못할 수 있으므로,
        // textureView와 관련된 UI작업은 새로이 Thread를 만들어서 처리한다.
        onPopup();
    }

    void onPopup() {
        Intent popup = new Intent(this, SavePopup.class);
        startActivityForResult(popup,1);
    }

    /*void UploadPicture2Server() {
        final String Username = "";
        final String UploadUri = "";
        final int port = 0;
        //TODO 유저이름, URI, PORT 추가하기 - 2
        new Thread() {
            public void run(){
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(Username,UploadUri,port);
                session.setPassword("");
                //TODO 비밀번호 추가하기 - 3
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect();

                Channel channel = session.openChannel("sftp");
                channel.connect();
                ChannelSftp channelSftp = (ChannelSftp) channel;

                Matrix rotation = new Matrix();
                rotation.postRotate(90);
                Bitmap bitmap = cameraView.getBitmap();
                bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),rotation,false);
                // 같은 크기의 90도 회전된 이미지로 수정
                // 맥북 기준으로 캠이 가로형인 탓인지 세로로 촬영해도 가로로 출력되는 오류 발생
                ByteArrayOutputStream BitmapOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,95,BitmapOutputStream);

                byte[] data = BitmapOutputStream.toByteArray();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
                // 비트맵의 outputStream을 inputStream으로 전환
                channelSftp.put(inputStream,"/home/cat/CaptureCat2Analyze/CatPicture.png");
                session.disconnect();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            }
        }.start();
    }*/

    void cameraPreview() {
        try {
            SurfaceTexture tx = cameraView.getSurfaceTexture();
            assert tx != null;
            // 텍스트뷰가 있는지 확인
            tx.setDefaultBufferSize(imageDimension.getWidth(),imageDimension.getHeight());
            Surface surface = new Surface(tx);
            builder = cameradevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            builder.addTarget(surface);
            builder.set(CaptureRequest.CONTROL_MODE,CaptureRequest.CONTROL_MODE_AUTO);
            cameradevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if(cameradevice == null) return;
                    session = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(CaptureCat.this,"Changed",Toast.LENGTH_SHORT).show();
                }
            },null);
        }
        catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        if(cameradevice == null) Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        builder.set(CaptureRequest.CONTROL_MODE,CaptureRequest.CONTROL_MODE_AUTO);
        try {
            session.setRepeatingRequest(builder.build(),null, null);
            // 카메라 세션을 텍스트뷰에 출력
        }
        catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    static final int PERMISSION_CODE = 1000;

    void openCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        //카메라 기능 사용
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            //사용 가능한 카메라 중 첫번째 카메라 id
            CameraCharacteristics cameraChars = cameraManager.getCameraCharacteristics(cameraId);
            // 카메라 실행
            StreamConfigurationMap map = cameraChars.get(cameraChars.SCALER_STREAM_CONFIGURATION_MAP);
            //카메라 출력 확인
            assert map != null;
            //안되면 오류 출력

            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission,PERMISSION_CODE);
                return;
            }
            cameraManager.openCamera(cameraId, Callback, null);
            // 사용 가능한 카메라에서 카메라를 실행 후 카메라가 실행되면 그 화면을 출력(cameraPreview())
        }
        catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    CameraDevice.StateCallback Callback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            cameradevice = cameraDevice;
            cameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            Toast.makeText(CaptureCat.this,"Disconnected", Toast.LENGTH_LONG).show();
            cameradevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            Toast.makeText(CaptureCat.this,"Error!! " + i, Toast.LENGTH_LONG).show();
            cameradevice.close();
            cameradevice = null;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
                // 권한을 받은 경우 카메라를 실행
            }
            else {
                Toast.makeText(this,"Permission failed", Toast.LENGTH_SHORT).show();
                finish();
                // 카메라 기능과 내부 저장소 권한이 없는 경우 자동으로 창에서 나가짐
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode==RESULT_OK) {
                String Result = data.getStringExtra("saveSelect");
                if(Result.equals("아니요.")) ;
                else if(Result.equals("네")) {
                    /*while(!imgFile.exists()) {
                        Snackbar loadingBar = Snackbar.make(findViewById(R.id.capture_cat_activity_layout),
                                "사진을 저장하고 있습니다.", Snackbar.LENGTH_INDEFINITE);
                        ProgressBar loadingCircle = findViewById(R.id.loading_circle);
                        loadingCircle.setVisibility(View.VISIBLE);
                        loadingBar.show();
                    }*/
                    if((!imgFile.exists()))
                        findViewById(R.id.loading_circle).setVisibility(View.VISIBLE);

                    // 이미지파일 생성이 생각보다 느려서 커뮤니티에 더 빠르게 들어가려고 하면 EOF 에러가 나옴(들어갈 당시에는 사진이 없었기 때문에).
                    Intent community = new Intent(getBaseContext(), CommunityAdd.class);
                    community.putExtra("captureData", imgFile.getPath());
                    startActivity(community);
                    finish();
                }
            }
        }
    }

}