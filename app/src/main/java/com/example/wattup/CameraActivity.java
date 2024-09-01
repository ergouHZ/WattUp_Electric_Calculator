package com.example.wattup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = CameraActivity.class.getSimpleName() + " test";
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSession;
    private CaptureRequest.Builder captureRequestBuilder;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageReader imageReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        Button button = findViewById(R.id.button_open);

        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                takePicture();
                try {
                    // 创建Intent来启动新的Activity
                    Intent intent = new Intent(CameraActivity.this, UploadActivity.class);

                    // 启动新的Activity
                    startActivity(intent);

                    // 结束当前Activity
                    finish();
                }catch (Exception e){
                    Log.e(TAG, "onClick: " + e.toString() );
                }
            }
        });

        Button captureButton = findViewById(R.id.captureButton);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                try {

                    Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    Log.e(TAG, "onClick: " + e.toString() );
                }
            }
        });

//        Button openButton = findViewById(R.id.ButtonOpen);
    }

    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            String cameraId = manager.getCameraIdList()[0];
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            Log.e(TAG, "openCamera: " + e.toString());
        }
    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            startCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    private void startCameraPreview() {
        try {
            Surface surface = surfaceHolder.getSurface();
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            imageReader = ImageReader.newInstance(1920, 1080, ImageFormat.JPEG, 1);
            imageReader.setOnImageAvailableListener(imageAvailableListener, null);

            cameraDevice.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    captureSession = session;
                    try {
                        captureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null);
                    } catch (CameraAccessException e) {
                        Log.e(TAG, "startCameraPreview: " + e.toString());
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                }
            }, null);

            Log.d(TAG, "startCameraPreview: starting capture session");
        } catch (CameraAccessException e) {
            Log.e(TAG, "startCameraPreview: " + e.toString());
        }
    }

    private void takePicture() {
        if (cameraDevice == null) return;

        try {
            CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imageReader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, 90); // 这里设置感觉的方向

            captureSession.capture(captureBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    startCameraPreview();

                    Log.d(TAG, "onCaptureCompleted: take successfully");
                }
            }, null);


        } catch (CameraAccessException e) {
            Log.e(TAG, "capturePhoto: " + e.toString());
        }
    }

    private final ImageReader.OnImageAvailableListener imageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = null;
            FileOutputStream output = null;
            try {
                image = reader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);

                File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera", "pic.jpg");
                output = new FileOutputStream(file);
                output.write(bytes);

                //TODO upload function
                Log.d(TAG, "onImageAvailable: upload!");
            } catch (IOException e) {
                Log.e(TAG, "capturePhoto: " + e.toString());
            } finally {
                if (image != null) {
                    image.close();
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        Log.e(TAG, "capturePhoto: " + e.toString());
                    }
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                // 权限被拒绝，处理相应的逻辑
            }
        }
    }

    @Override
    protected void onPause() {
        if (captureSession != null) {
            captureSession.close();
            captureSession = null;
        }
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
        super.onPause();
    }
}