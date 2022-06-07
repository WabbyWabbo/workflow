package com.workflow.util.jna;

import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HOOKPROC;

interface LowLevelMouseProc extends HOOKPROC {  
    LRESULT callback( int nCode, WPARAM wParam, MOUSEHOOKSTRUCT lParam );
}  