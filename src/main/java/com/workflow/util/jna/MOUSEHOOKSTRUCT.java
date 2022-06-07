package com.workflow.util.jna;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser.POINT;

import java.util.Arrays;
import java.util.List;

public class MOUSEHOOKSTRUCT extends Structure {
    @Override
    protected List getFieldOrder() {
        return Arrays.asList("pt","hwnd","wHitTestCode","dwExtraInfo");
    }

    public class ByReference extends MOUSEHOOKSTRUCT implements
            Structure.ByReference {
    };
      
    public POINT        pt;  
    public HWND         hwnd;  
    public int          wHitTestCode;  
    public ULONG_PTR    dwExtraInfo;  
}  