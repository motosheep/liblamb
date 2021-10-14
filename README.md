在上文中，展示了安卓项目如何构建lame，接下来，我们要使用lame把wav音频转为mp3。
上文回顾：[lame编译](https://www.jianshu.com/p/3bceb702eb73)
项目结构如下图：
![image.png](https://upload-images.jianshu.io/upload_images/13738977-f64dadbe65399788.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

LameUtils主要是用来编写与native-lib交互的方法的，也就是native方法的声明，对于转换这个流程，我们需要声明两个方法，一个转换方法，一个转换进度回调方法。如下图：
```
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
```
然后在native-lib.cpp文件中，也同样声明两个实现方法：
![native-lib实现方法名](https://upload-images.jianshu.io/upload_images/13738977-c1b0a33546ba1981.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


native-lib的转换核心代码如下：
```
 do {
        read = fread(wav_buffer, sizeof(short int) * 2, 8192, fwav);
        total += read * sizeof(short int) * 2;
        fseek(fwavCount, 0, SEEK_END);
        long length = ftell(fwavCount);
        rewind(fwavCount);
        LOGI("converting ....%d", total);
        publishJavaProgress(env, obj, total, length, jmp3);
        // 调用java代码 完成进度条的更新
        if (read != 0) {
            write = lame_encode_buffer_interleaved(lame, wav_buffer, read, mp3_buffer, 8192);
            //把转化后的mp3数据写到文件里
            fwrite(mp3_buffer, sizeof(unsigned char), write, fmp3);
        }
        if (read == 0) {
            lame_encode_flush(lame, mp3_buffer, 8192);
        }
    } while (read != 0);
```
通过fread方法，读取数据流，然后通过调用lame_encode_buffer)interleaved方法，实现流的转换并且赋值给write对象，最后调用fwriter方法写入到文件中，并用lame_encode_flush方法，刷新缓冲区实现文件更新。至此，文件全部写入完成，别忘了最后关闭文件指针哦。

源码地址：
https://gitee.com/motosheep/liblamb

that's all---------------------------------------------------------------------------------------------
