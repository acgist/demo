#include <tchar.h>
#include <stdio.h>
#include <windows.h>

/**
 * C Win32API
 * gcc .\Win32.c "-finput-charset=UTF-8" "-fexec-charset=GBK"
 */
int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, PSTR szCmdLine, int iCmdShow) {
//	printf("测试\n");
//	printf(L"测试\n");
//	printf(U"测试\n");
//	printf(_T("测试\n"));
	MessageBox(NULL, "你好", "标题", MB_OK);
	return 0;
}
