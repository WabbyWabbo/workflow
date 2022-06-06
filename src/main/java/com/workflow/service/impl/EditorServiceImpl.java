package com.workflow.service.impl;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.workflow.service.EditorService;
import com.workflow.util.*;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import vo.Result;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.*;
import java.util.List;

@Service
@Log
public class EditorServiceImpl implements EditorService {

    private String mImagesPath = "C:\\pic_temp";
    private Set<String> pictureNames = new HashSet<>();

    public static volatile boolean captureFinished = false;
    public static volatile boolean captureCanceled = false;

    @SneakyThrows
    @Override
    public Result getCodeContent(HashMap<String, String> map) {
        String scriptDir = map.get("scriptsPath") + "\\" + map.get("name") + ".sikuli";
        String codeContent = FileUtil.getScriptCode(scriptDir);

        // 清空临时文件夹
        boolean b = FileUtil.deleteDir(mImagesPath);
        if (!b)
            log.info("清空临时文件夹失败");
        pictureNames.clear();
        // 将改脚本里的所有图片拷贝至temp目录
        List<File> pictures = FileUtil.getPictures(scriptDir);
        for (File picture : pictures) {
            String pictureName = picture.getName(); // 1652349177524.png
            File tempPicture = new File(mImagesPath + "\\" + pictureName);
            FileCopyUtils.copy(picture, tempPicture);
            pictureNames.add(pictureName);
        }
        return Result.success(codeContent);
    }

    @Override
    public Result saveContent(HashMap<String, String> map) {
        String scriptDir = map.get("scriptsPath") + "\\" + map.get("scriptName") + ".sikuli\\" + map.get("scriptName") + ".py";
        String content = map.get("editorContent");

        boolean b = FileUtil.saveScript(scriptDir, content);
        if (b)
            return Result.success("ok");
        else
            return Result.fail(500, "写入文件失败");
    }

    @SneakyThrows
    @Override
    public Result capture(HashMap<String, String> map) {
        // 最小化浏览器
        RobotUtil.pressMultipleKeyByNumber(KeyEvent.VK_ALT, KeyEvent.VK_SPACE, KeyEvent.VK_N);

        // 用windows自带的截图工具,存在问题：程序跑的过快，读取剪贴板时，截图动作还未完成，导致保存的图是上次截取的
        RobotUtil.pressMultipleKeyByNumber(KeyEvent.VK_WINDOWS, KeyEvent.VK_SHIFT, KeyEvent.VK_S);

        // 使用Snipaste
//        Command.exeCmd("Snipaste\\Snipaste.exe snip -o clipboard");

        // 生成图片全路径名
        String scriptDir = map.get("scriptsPath") + "\\" + map.get("scriptName") + ".sikuli\\";
        String fileName = String.valueOf(System.currentTimeMillis()) + ".png";

        // 监听鼠标事件
        captureFinished = false;
        captureCanceled = false;
        // 等待鼠标松开 或者 取消本次截图
        while (!captureFinished && !captureCanceled) {
            // 超过10秒，强制结束等待
            long time = System.currentTimeMillis() - Long.parseLong(fileName.replace(".png", ""));
            if (time > 10000) {
                log.info("超过10秒");
                break;
            }
        }

        // 截图完毕，判断用户是否截图完成
        if (captureCanceled) {
            log.info("按Esc取消了截图");
            return Result.fail(500, "截图失败");
        }

        // 从剪贴板获取图片
        Thread.sleep(1000);
        Image imageFromClipboard = ClipboardOperate.getImageFromClipboard();

        if (imageFromClipboard == null) {
            log.warning("剪贴板中没有截图");
            return Result.fail(500, "截图失败");
        }


        // 保存新截得图到该脚本目录下
        File file = new File(scriptDir + fileName);
        ImageIO.write((RenderedImage) imageFromClipboard, "png", file);
        log.info("截图保存成功");

        // 新截的图也要放到temp目录下，以便于预览
        File newPicture = new File(mImagesPath + "\\" + fileName);
        ImageIO.write((RenderedImage) imageFromClipboard, "png", newPicture);
        pictureNames.add(fileName);

        // 返回浏览器
        RobotUtil.pressMultipleKeyByNumber(KeyEvent.VK_ALT, KeyEvent.VK_TAB);

        return Result.success(fileName);
    }

    @Override
    public Result previewPicture(HashMap<String, String> map) {
        String selectedText = map.get("selectedText");
        String picName = selectedText + ".png";
        String url;
        // 判断是否存在对应的图片
        if (pictureNames.contains(picName)) {
            url = "http://localhost:6630/images/" + picName;
            return Result.success(url);
        } else {
            return Result.fail(404, "找不到该图片");
        }
    }

    @SneakyThrows
    @Override
    public Result createScript(HashMap<String, String> map) {
        String path = map.get("scriptsPath") + "\\" + map.get("scriptName") + ".sikuli";
        // 新建.sikuli文件夹
        File dir = new File(path);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdir();
        } else {
            return Result.fail(400, "保存失败，该脚本已存在！");
        }
        // 从temp.sikuli拷贝到该文件夹
        File temp = new File("C:\\script_temp\\temp.sikuli");
        FileSystemUtils.copyRecursively(temp, dir);
        // 改名py文件
        File tempPythonFile = new File(path + "\\" + "temp.py");
        File realPythonFile = new File(path + "\\" + map.get("scriptName") + ".py");
        tempPythonFile.renameTo(realPythonFile);

        return Result.success("ok");
    }

    @SneakyThrows
    @Override
    public Result newScript() {
        // 清空temp.sikuli目录
        FileUtil.deleteDir("C:\\script_temp\\temp.sikuli");
        // 重新生成.py文件
        File script = new File("C:\\script_temp\\temp.sikuli\\temp.py");
        script.createNewFile();

        return Result.success("empty temp.sikuli folder: ok");
    }

    @Override
    public Result saveScriptToTemp(HashMap<String, String> map) {
        String content = map.get("editorContent");
        // 写入temp.py中
        boolean b = FileUtil.saveScript("C:\\script_temp\\temp.sikuli\\temp.py", content);
        if (b)
            return Result.success("ok");
        else
            return Result.fail(500, "写入temp.py失败");
    }
}
