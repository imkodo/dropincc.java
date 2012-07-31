package com.github.pfmiles.dropincc.impl.hotcompile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.util.ByteAppender;

/**
 * Load hot compiled code from default directory.
 * 
 * @author pf-miles
 * 
 */
public class HotCompileClassLoader extends ClassLoader {

    public HotCompileClassLoader(ClassLoader parent) {
        super(parent);
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b = loadClassData(name);
        return defineClass(name, b, 0, b.length);
    }

    // read class data from default hot compilation directory
    private byte[] loadClassData(String name) {
        File src = new File(HotCompileConstants.TARGETDIR + File.separator + name.replaceAll("\\.", File.separator) + ".class");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(src);
            ByteAppender ba = new ByteAppender();
            byte[] buf = new byte[1024];
            int count = fis.read(buf);
            while (count != -1) {
                ba.write(buf, 0, count);
                count = fis.read(buf);
            }
            return ba.toByteArray();
        } catch (Exception e) {
            throw new DropinccException(e);
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                throw new DropinccException(e);
            }
        }
    }
}