#include <iostream>
#include <exception>

using namespace std;

int main() {
        int a = 100;
        int b = 10;
        int c = a / b;
        cout << c << endl;
        b = 0;
        try {
                if(b == 0) {
                        throw "��������Ϊ��";
                }
                // �����ǲ��ܲ����
                c = a / b;
        } catch(char const* e) {
                cout << "�쳣��" << e << endl;
        } catch(exception& e) {
                cout << "�쳣��" << e.what() << endl;
        }
        cout << c << endl;
        return 0;
}
