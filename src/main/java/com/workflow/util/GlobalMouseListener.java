package com.workflow.util;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.workflow.service.impl.EditorServiceImpl;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GlobalMouseListener implements NativeMouseInputListener {
//    public void nativeMouseClicked(NativeMouseEvent e) {
//        System.out.println("Mouse Clicked: " + e.getClickCount());
//    }
//
    public void nativeMousePressed(NativeMouseEvent e) {
        log.debug("Mouse Pressed:" + e.getPoint());
        EditorServiceImpl.captureFinished = false;
    }

    public void nativeMouseReleased(NativeMouseEvent e) {
        log.debug("Mouse Released:" + e.getPoint());
        EditorServiceImpl.captureFinished = true;
//        try {
//            GlobalScreen.unregisterNativeHook();
//        } catch (NativeHookException nativeHookException) {
//            nativeHookException.printStackTrace();
//        }
    }

//    public void nativeMouseMoved(NativeMouseEvent e) {
//        System.out.println("Mouse Moved: " + e.getX() + ", " + e.getY());
//    }

//    public void nativeMouseDragged(NativeMouseEvent e) {
//        System.out.println("Mouse Dragged: " + e.getX() + ", " + e.getY());
//    }

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        // Construct the example object.
        GlobalMouseListener example = new GlobalMouseListener();

        // Add the appropriate listeners.
        GlobalScreen.addNativeMouseListener(example);
        GlobalScreen.addNativeMouseMotionListener(example);
    }
}