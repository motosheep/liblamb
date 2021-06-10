package com.north.light.liblame;

import android.util.Log;

import java.io.File;
import java.io.Serializable;

/**
 * Created by lzt
 * time 2021/6/10 11:43
 *
 * @author lizhengting
 * 描述：lame工具类
 */
public class LameUtils implements Serializable {
    /**
     * 是否压缩中的标识
     */
    private volatile boolean isCompress = false;

    static {
        System.loadLibrary("lame-lib");
    }


    private static class SingHolder {
        static LameUtils mInstance = new LameUtils();
    }

    public static LameUtils getInstance() {
        return SingHolder.mInstance;
    }

    /**
     * java层调用方法--获取版本号
     */
    public String getVersion() {
        return getLameVersion();
    }

    /**
     * java层调用方法--wav转换mp3
     */
    public void trainToMp3(String wav, String mp3, boolean deleteSource) throws Exception {
        if (isCompress) {
            throw new Exception("正在压缩仲");
        }
        isCompress = true;
        if (!new File(wav).exists()) {
            throw new Exception("源文件不存在");
        }
        if (!new File(mp3).exists()) {
            throw new Exception("目标文件不存在");
        }
        convertToMp3(wav, mp3);
        if (deleteSource) {
            new File(wav).delete();
        }
        isCompress = false;
    }

    /**
     * java层调用方法--wav转换mp3
     */
    public void trainToMp3(String wav, String mp3) throws Exception {
        trainToMp3(wav, mp3, false);
    }

    //native -------------------------------------------------------------------------------------

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    private native String getLameVersion();

    /**
     * wav转换成mp3的本地方法
     *
     * @param wav
     * @param mp3
     */
    private native void convertToMp3(String wav, String mp3);

    /**
     * 设置进度条的进度，提供给C语言调用
     *
     * @param progress
     */
    public void setConvertProgress(long progress, long total, String path) {
        Log.d(this.getClass().getSimpleName(), "回调的进度:" + progress + "----总进度:" + total + "----path:" + path);
    }

}
