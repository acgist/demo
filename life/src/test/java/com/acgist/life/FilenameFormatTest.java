package com.acgist.life;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class FilenameFormatTest {

    @Test
    public void testFormat() {
        final File folder = new File("D:\\download\\noise\\noise");
        final File[] files = folder.listFiles();
        if(files == null || files.length <= 0) {
            return;
        }
        final Map<String, Integer> mapping = new HashMap<>();
        final List<File> list = new ArrayList<>(List.of(files));
        list.sort((a, z) -> a.getName().compareTo(z.getName()));
        for (int index = 0; index < list.size(); index++) {
            final File file = list.get(index);
            final String name = file.getName();
            final int pos = name.indexOf('_') + 1;
            final String prefix = name.substring(0, Math.max(name.indexOf('_', pos), name.indexOf(' ', pos)));
            Integer sn = mapping.getOrDefault(prefix, 0);
            mapping.put(prefix, ++sn);
            final File target = Paths.get(file.getParentFile().getAbsolutePath(), prefix + "_" + String.format("%02d", sn) + ".wav").toFile();
            if(file.renameTo(target)) {
                System.out.println(target.getName());
            } else {
                System.err.println("修改失败：" + file.getName());
            }
        }
    }
    
}
