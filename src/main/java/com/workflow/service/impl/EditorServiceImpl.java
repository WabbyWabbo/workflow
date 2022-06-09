package com.workflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.workflow.pojo.json.All;
import com.workflow.service.EditorService;
import com.workflow.util.*;
import com.workflow.util.jna.MouseLLHook;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.*;

@Service
@Log
public class EditorServiceImpl implements EditorService {

    private String mImagesPath = "C:\\pic_temp";
    private Set<String> pictureNamesInTempFloder = new HashSet<>();

    public static volatile boolean captureFinished = false;
    public static volatile boolean captureCanceled = false;

    @SneakyThrows
    @Override
    public Result viewScript(HashMap<String, String> map) {
        String scriptDir = map.get("scriptsPath") + "\\" + map.get("name") + ".sikuli";
        String codeContent = FileUtil.getScriptCode(scriptDir);

        // 清空临时文件夹
        boolean b = FileUtil.deleteDir(mImagesPath);
        if (!b)
            log.info("清空临时文件夹失败");
        pictureNamesInTempFloder.clear();

        // temp包装成url返回给前端
        List<String> urls = new ArrayList<>();

        // 将改脚本里的所有图片拷贝至temp目录,并添加至urls
        List<File> pictures = FileUtil.getPictures(scriptDir);
        for (File picture : pictures) {
            String pictureName = picture.getName(); // 1652349177524.png
            File tempPicture = new File(mImagesPath + "\\" + pictureName);
            FileCopyUtils.copy(picture, tempPicture);
            pictureNamesInTempFloder.add(pictureName);
            urls.add("http://localhost:6630/images/" + pictureName);
        }

        return Result.success(new Object[]{codeContent, urls});
    }

    @Override
    public Result saveContent(HashMap<String, String> map) {
        String scriptsPath = map.get("scriptsPath");
        String scriptName = map.get("scriptName");
        String content = map.get("editorContent");
        String scriptDir = scriptsPath + "\\" + scriptName + ".sikuli\\" + scriptName + ".py";
        boolean b = FileUtil.saveScript(scriptDir, content);
        List<String> deletedPicNames = deleteUselessPic(scriptsPath, scriptName, content);
        if (b)
            return Result.success(deletedPicNames); // 返回已删除的图片名
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
        String userSetPicName = map.get("userSetPicName");
        String fileName;
        long startTime = System.currentTimeMillis();
        if (userSetPicName == null || userSetPicName.isEmpty() || userSetPicName.isBlank()) {
            fileName = startTime + ".png";
        } else {
            fileName = userSetPicName + ".png";
        }

        if (pictureNamesInTempFloder.contains(fileName)) {
            log.info("重名，即将覆盖掉");
        }

        log.info("fileName:" + fileName);

        // 监听鼠标事件
        captureFinished = false;
        captureCanceled = false;
        // 等待鼠标松开 或者 取消本次截图
        while (!captureFinished && !captureCanceled) {
            // 超过10秒，强制结束等待
            long time = System.currentTimeMillis() - startTime;
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
        Image imageFromClipboard = null;
        try {
            // java.lang.IllegalStateException: cannot open system clipboard
            imageFromClipboard = ClipboardOperate.getImageFromClipboard();
        } catch (Exception e) {
            // 返回浏览器
            RobotUtil.pressMultipleKeyByNumber(KeyEvent.VK_ALT, KeyEvent.VK_TAB);
            return Result.fail(500, e.getMessage());
        }

        if (imageFromClipboard == null) {
            log.warning("剪贴板中没有截图");
            // 返回浏览器
            RobotUtil.pressMultipleKeyByNumber(KeyEvent.VK_ALT, KeyEvent.VK_TAB);
            return Result.fail(500, "截图失败");
        }


        // 保存新截得图到该脚本目录下
        File file = new File(scriptDir + fileName);
        ImageIO.write((RenderedImage) imageFromClipboard, "png", file);
        log.info("截图保存成功");

        // 新截的图也要放到temp目录下，以便于预览
        File newPicture = new File(mImagesPath + "\\" + fileName);
        ImageIO.write((RenderedImage) imageFromClipboard, "png", newPicture);
        pictureNamesInTempFloder.add(fileName);

        // 截图的同时保存json文件
        String jsonFileName = fileName.replace(".png", ".json");
        File jsonFile = new File(scriptDir + jsonFileName);
        if (!jsonFile.exists()) {
            jsonFile.createNewFile();
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenX = (int) screenSize.getWidth();
        int screenY = (int) screenSize.getHeight();
        double per_x = MouseLLHook.x / (screenX * 1.00);
        double per_y = MouseLLHook.y / (screenY * 1.00);

//        String jsonStr = FileUtils.readFileToString(jsonFile, "UTF-8");
        JSONObject contain = new JSONObject();
        JSONObject js = new JSONObject();
        js.put("perpos_x", per_x);
        js.put("perpos_y", per_y);
        js.put("screen_x", screenX);
        js.put("screen_y", screenY);
        contain.put(fileName, js);
        String content = contain.toString();
        FileOutputStream fileOutputStream = new FileOutputStream(jsonFile);//实例化FileOutputStream
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");//将字符流转换为字节流
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);//创建字符缓冲输出流对象
        bufferedWriter.write(content);//将格式化的jsonarray字符串写入文件
        bufferedWriter.flush();//清空缓冲区，强制输出数据
        bufferedWriter.close();//关闭输出流

        // 返回浏览器
        Thread.sleep(500);
        RobotUtil.pressMultipleKeyByNumber(KeyEvent.VK_ALT, KeyEvent.VK_TAB);

        return Result.success(fileName);
    }

    @Override
    public Result previewPicture(HashMap<String, String> map) {
        String selectedText = map.get("selectedText");
        String picName = selectedText + ".png";
        String url;
        // 判断是否存在对应的图片
        if (pictureNamesInTempFloder.contains(picName)) {
            url = "http://localhost:6630/images/" + picName;
            return Result.success(url);
        } else {
            return Result.fail(404, "找不到该图片");
        }
    }

    @SneakyThrows
    @Override
    public Result createRealScript(HashMap<String, String> map) {
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
    public Result createTempScript() {
        // 清空temp.sikuli目录
        FileUtil.deleteDir("C:\\script_temp\\temp.sikuli");
        // 重新生成.py文件
        File script = new File("C:\\script_temp\\temp.sikuli\\temp.py");
        script.createNewFile();
        // 清空pic_temp目录
        FileUtil.deleteDir("C:\\pic_temp");
        return Result.success("empty temp.sikuli folder and pic_temp folder: ok");
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

    @Override
    public Result runScript(HashMap<String, String> map) {
        String scriptsPath = map.get("scriptsPath");
        String scriptName = map.get("scriptName");
        String absoluteScriptPath = scriptsPath + "\\" + scriptName;
        // 去data.json中找最近使用的sikuliPath
        All all = FastJsonUtils.readFile("data.json");
        Long recent = all.getSikuli().getRecent();
        String sikuliPath = all.getSikuli().getList().get(Math.toIntExact(recent)).getPath();
        log.info("Found sikuliPath in data.json: " + sikuliPath);
        if (sikuliPath.isEmpty() || sikuliPath.isBlank()) {
            return Result.fail(400, "sikuli路径设置错误");
        }
        File py = new File(absoluteScriptPath + ".sikuli\\" + scriptName + ".py");
        if (null == py || 0 == py.length() || !py.exists()) {
            return Result.fail(300, "脚本内容为空，请先点击保存！");
        }
        // 最小化浏览器
        RobotUtil.pressMultipleKeyByNumber(KeyEvent.VK_ALT, KeyEvent.VK_SPACE, KeyEvent.VK_N);
        // 执行脚本
        log.info("Ready to run: " + absoluteScriptPath);
        com.workflow.resp.data.Result result = Command.execScriptToResult(sikuliPath, absoluteScriptPath);
        // 返回浏览器
        RobotUtil.pressMultipleKeyByNumber(KeyEvent.VK_ALT, KeyEvent.VK_TAB);
        return Result.success(result);
    }

    /**
     * 辅助函数，用于在点击保存按钮后，清空无用的图片，模仿原生sikuli的做法，保存按钮点击过后，将无法恢复！！！
     *
     * @param scriptsPath 脚本目录
     * @param scriptName  脚本名
     * @param content     脚本内容
     * @return 已彻底删除的图片名称列表
     */
    private List<String> deleteUselessPic(String scriptsPath, String scriptName, String content) {
        String fullPath = scriptsPath + "\\" + scriptName + ".sikuli";
        File scriptDir = new File(fullPath);
        // 遍历该脚本的文件夹，形成图片列表picturesInRealFolder
        if (!scriptDir.exists() || !scriptDir.isDirectory()) {
            return new ArrayList<>();
        }
        List<File> picturesInRealFolder = FileUtil.getPictures(fullPath);
        // 脚本中已经不包含该图片名，则从磁盘上删除该图片
        List<String> deletedPictureNames = new ArrayList<>();
        for (File file : picturesInRealFolder) {
            if (!content.contains(file.getName().replace(".png", ""))) {    // 删到图片名时再彻底删除，删.png则不动
                if (file.delete())
                    deletedPictureNames.add(file.getName());
                // 若有同名json文件，一并删除
                File jsonFile = new File(file.getAbsolutePath().replace(".png", ".json"));
                if (jsonFile.exists() && jsonFile.isFile()) {
                    jsonFile.delete();
                }
            }
        }
        return deletedPictureNames;
    }
}
