package com.xyh.camera;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.xyh.camera.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Surface frontSurface;
    private Surface backSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.binding.getRoot());
        this.binding.front.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                MainActivity.this.frontSurface = new Surface(MainActivity.this.binding.front.getSurfaceTexture());
                MainActivity.this.preview(CameraCharacteristics.LENS_FACING_FRONT, MainActivity.this.frontSurface);
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
            }
        });
        this.binding.back.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                MainActivity.this.backSurface = new Surface(MainActivity.this.binding.back.getSurfaceTexture());
                MainActivity.this.preview(CameraCharacteristics.LENS_FACING_BACK, MainActivity.this.backSurface);
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
            }
        });
    }

    @SuppressLint({"MissingPermission"})
    private void preview(int type, Surface surface) {
        final HandlerThread previewThread = new HandlerThread("Preview" + type);
        previewThread.start();
        final Handler previewHandler = new Handler(previewThread.getLooper());
        previewHandler.post(() -> {
            final CameraManager cameraManager = this.getSystemService(CameraManager.class);
            try {
                String cameraId = null;
                final String[] cameraIdList = cameraManager.getCameraIdList();
                for (String id : cameraIdList) {
                    final CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);
                    if(cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == type) {
                        cameraId = id;
                        break;
                    } else {
                        // TODO：截屏
                    }
                }
                cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                    @Override
                    public void onOpened(@NonNull CameraDevice cameraDevice) {
                        try {
                            cameraDevice.createCaptureSession(new SessionConfiguration(
                                SessionConfiguration.SESSION_REGULAR,
                                List.of(new OutputConfiguration(surface)),
                                Runnable::run,
                                new CameraCaptureSession.StateCallback() {
                                    @Override
                                    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                                        try {
                                            final CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//                                          builder.set(CaptureRequest.JPEG_QUALITY, (byte) 100);
//                                          builder.set(CaptureRequest.JPEG_ORIENTATION, 90);
                                            builder.addTarget(surface);
                                            cameraCaptureSession.setRepeatingRequest(builder.build(), new CameraCaptureSession.CaptureCallback() {
                                                @Override
                                                public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                                                    super.onCaptureStarted(session, request, timestamp, frameNumber);
                                                }
                                            }, null);
                                        } catch (CameraAccessException e) {
                                            Log.e(MainActivity.class.getSimpleName(), "摄像头打开异常", e);
                                        }
                                    }

                                    @Override
                                    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                                    }
                                }
                            ));
                        } catch (CameraAccessException e) {
                            Log.e(MainActivity.class.getSimpleName(), "摄像头打开异常", e);
                        }
                    }

                    @Override
                    public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                    }

                    @Override
                    public void onError(@NonNull CameraDevice cameraDevice, int error) {
                    }
                }, null);
            } catch (CameraAccessException e) {
                Log.e(MainActivity.class.getSimpleName(), "摄像头打开异常", e);
            }
        });
    }

}