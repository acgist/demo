#define DefineClass(Name) \
class Name {              \
public:                   \
    int age;              \
}                         \

#define DefineImpl(Name) DefineClass(Name##Impl);

DefineImpl(Person);
DefineClass(Person);

int main() {
    Person p;
    p.age = 100;
    PersonImpl impl;
    impl.age = 100;
    return 0;
}
