package com.acgist.resampling;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.acgist.resampling.databinding.ActivityMainBinding;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainActivity extends AppCompatActivity {

    private Resampling resampling;
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
        this.resampling = new Resampling();
        this.resampling();
    }

    private void resampling() {
        final Path pcmPath = Paths.get(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath(),
            "a.pcm"
        );
        final Path outPath = Paths.get(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath(),
            "z.pcm"
        );
        try {
            if(outPath.toFile().exists()) {
                Files.delete(outPath);
            }
            final byte[] src = new byte[160];
            final byte[] bytes = Files.readAllBytes(pcmPath);
            for (int i = 0; i < bytes.length; i += 160) {
                if(i + 160 >= bytes.length) {
                    break;
                }
                System.arraycopy(bytes, i, src, 0, 160);
                final byte[] dst = this.resampling.resampling(src, 8000, 48000);
                Files.write(outPath, dst, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            Log.e(MainActivity.class.getSimpleName(), "读取文件异常", e);
        } finally{
            Log.i(MainActivity.class.getSimpleName(), "完成");
        }
    }

}