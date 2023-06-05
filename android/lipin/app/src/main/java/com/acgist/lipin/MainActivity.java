package com.acgist.lipin;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.acgist.lipin.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainActivity extends AppCompatActivity {

    private Lipin lipin;
    private ActivityMainBinding binding;

    static {
        System.loadLibrary("avutil");
        System.loadLibrary("rnnoise");
        System.loadLibrary("swresample");
        System.loadLibrary("resampling");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.binding.getRoot());
        this.lipin = new Lipin();
        this.lipin();
    }

    private void lipin() {
        final Path pcmPath = Paths.get(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath(),
            "a.pcm"
        );
        final Path outPath = Paths.get(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath(),
            "z.pcm"
        );
        this.lipin.init(16, 160, 8000, 16000);
        try (
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            ) {
            if(outPath.toFile().exists()) {
                Files.delete(outPath);
            }
            final byte[] src = new byte[160];
            final byte[] bytes = Files.readAllBytes(pcmPath);
            long a = System.currentTimeMillis();
            for (int i = 0; i < bytes.length; i += 160) {
                if(i + 160 >= bytes.length) {
                    break;
                }
                System.arraycopy(bytes, i, src, 0, 160);
                final byte[] dst = this.lipin.rnnoise(src);
//              final byte[] dst = this.lipin.resample(src);
                output.write(dst);
            }
            long z = System.currentTimeMillis();
            Log.i(MainActivity.class.getSimpleName(), "处理时间：" + (z - a));
            Files.write(outPath, output.toByteArray(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            Log.e(MainActivity.class.getSimpleName(), "读取文件异常", e);
        } finally{
            this.lipin.release();
            Log.i(MainActivity.class.getSimpleName(), "完成");
        }
    }

}