package com.workflow.util;

import java.awt.*;

public class RobotUtil {
    /**
     * 模拟按下键盘单个按键，比如文档下一页：PgDn，上一页是PgUp等
     *
     * @param keycode：按键的值,如：KeyEvent.VK_PAGE_UP
     */
    public static final void pressSingleKeyByNumber(int keycode) {
        try {
            /** 创建自动化测试对象  */
            Robot robot = new Robot();
            /**按下按键*/
            robot.keyPress(keycode);
            /**松开按键*/
            robot.keyRelease(keycode);
            /**可以稍作延时处理*/
            robot.delay(500);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按下组合键，如 ctrl + c、ctrl + v、alt + tab 等等
     *
     * @param keycode：组合健数组，如 {KeyEvent.VK_CONTROL,KeyEvent.VK_V}
     */
    public static void pressMultipleKeyByNumber(int... keycode) {
        try {
            Robot robot = new Robot();

            //按顺序按下健
            for (int i = 0; i < keycode.length; i++) {
                robot.keyPress(keycode[i]);
                robot.delay(50);
            }

            //按反序松开健
            for (int i = keycode.length - 1; i >= 0; i--) {
                robot.keyRelease(keycode[i]);
                robot.delay(50);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
