package com.workflow.util.jna;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.workflow.service.impl.EditorServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
public class MouseLLHook {

    // 鼠标钩子函数里判断按键类型的常数  
    public static final int WM_LBUTTONUP = 514;
    public static final int WM_LBUTTONDOWN = 513;
    public static final int WM_RBUTTONUP = 517;
    public static final int WM_RBUTTONDOWN = 516;
    public static final int WM_MOUSEHWHEEL = 526;
    public static final int WM_MOUSEWHEEL = 522;
    public static final int WM_MOUSEMOVE = 512;
    public static double x;
    public static double y;

    static HHOOK mouseHHK, keyboardHHK;  // 鼠标、键盘钩子的句柄
    static LowLevelMouseProc mouseHook;              // 鼠标钩子函数
    static LowLevelKeyboardProc keyboardHook;           // 键盘钩子函数  

    // 安装钩子  
    static void setHook() {
        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        mouseHHK = User32.INSTANCE.SetWindowsHookEx(WinUser.WH_MOUSE_LL,
                mouseHook, hMod, 0);
        keyboardHHK = User32.INSTANCE.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL,
                keyboardHook, hMod, 0);
    }

    // 卸载钩子  
    static void unhook() {
        User32.INSTANCE.UnhookWindowsHookEx(keyboardHHK);
        User32.INSTANCE.UnhookWindowsHookEx(mouseHHK);
    }

    public static void mainF() {

        mouseHook = new LowLevelMouseProc() {

            @SneakyThrows
            @Override
            // 该函数参数的意思参考：http://msdn.microsoft.com/en-us/library/windows/desktop/ms644986(v=vs.85).aspx  
            public LRESULT callback(int nCode, WPARAM wParam,
                                    MOUSEHOOKSTRUCT lParam) {
                switch (wParam.intValue()) {
                    case WM_LBUTTONDOWN:
                        log.debug("mouse left button down:" + "(" + lParam.pt.x + "," + lParam.pt.y + ")");
                        x = lParam.pt.x;
                        y = lParam.pt.y;
                        EditorServiceImpl.captureFinished = false;
                        break;
                    case WM_LBUTTONUP:

                        log.debug("mouse left button up" + "(" + lParam.pt.x + "," + lParam.pt.y + ")");
                        EditorServiceImpl.captureFinished = true;
                        break;
                    default:
                        break;
                }
                return User32.INSTANCE.CallNextHookEx(mouseHHK, nCode, wParam, lParam.getPointer());
            }
        };

        setHook();

        int result;
        MSG msg = new MSG();
        // 消息循环  
        // 实际上while循环一次都不执行，这些代码的作用我理解是让程序在GetMessage函数这里阻塞，不然程序就结束了。  
        while ((result = User32.INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                System.err.println("error in GetMessage");
                unhook();
                break;
            } else {
                User32.INSTANCE.TranslateMessage(msg);
                User32.INSTANCE.DispatchMessage(msg);
            }
        }
        unhook();
    }

}  