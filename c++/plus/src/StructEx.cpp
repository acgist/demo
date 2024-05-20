struct P {

int a;
int b;

};

int main() {
    P p1;
    P* p2 = new P{};
    P p3 = *p2;
    return 0;
}
