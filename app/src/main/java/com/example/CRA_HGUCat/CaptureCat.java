package com.example.CRA_HGUCat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
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
import android.util.Size;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_cat);

        cameraView = findViewById(R.id.cameraView);
        assert cameraView != null;
        cameraView.setRotation(90f);

        cameraView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                openCamera();
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
                cameraView.setRotation(90f);
            }
            else if (orientation > 145 && orientation < 215 && rotation != 2) {
                rotation = 2;
                cameraView.setRotation(90f);
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

    }

    public void onClick(View v)
    {
        UploadPicture2Server();
    }

    void UploadPicture()
    {
        Matrix rotation = new Matrix();
        rotation.postRotate(90);

        Bitmap bitmap = cameraView.getBitmap();
        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),rotation,false);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReferenceFromUrl("");
        // TODO URL추가 - 1

        StorageReference catRef = storageRef.child(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())+".png");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,95,baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = catRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            Toast.makeText(CaptureCat.this,"Upload failed by "+e ,Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Toast.makeText(CaptureCat.this,"Uploaded",Toast.LENGTH_SHORT).show();
            }
        });
    }

    void UploadPicture2Server()
    {
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
                ByteArrayOutputStream BitmapOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,95,BitmapOutputStream);

                byte[] data = BitmapOutputStream.toByteArray();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
                channelSftp.put(inputStream,"/home/cat/CaptureCat2Analyze/CatPicture.png");
                session.disconnect();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            }
        }.start();
    }

    void cameraPreview()
    {
        try {
            SurfaceTexture tx = cameraView.getSurfaceTexture();
            assert tx != null;
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
            }
            else {
                Toast.makeText(this,"Permission failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}