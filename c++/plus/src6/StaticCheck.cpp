int main() {
    static_assert(sizeof(void*) == 8, "64-bit");
    // static_assert(sizeof(void*) == 4, "32-bit");
    return 0;
}