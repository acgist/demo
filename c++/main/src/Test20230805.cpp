// #include <array>
#include <string>
#include <iostream>

int main(int argc, char const *argv[]) {
    // std::array<int, 3> a = { 1, 2, 3 };
    // const int nums[] = { 1, 2, 3 };
    // const int nums[4] = { 1, 2, 3 };
    // int nums[] {};
    int nums[4] { 1, 2, 3 };
    std::cout << nums[0] << std::endl;
    std::cout << nums[1] << std::endl;
    std::cout << nums[2] << std::endl;
    std::cout << nums[3] << std::endl;
    std::cout << nums[10] << std::endl;
    int nums1[2];
    int nums2[2] = {};
    int nums3[2] = { 0 };
    int nums4[2] = { 1 };
    std::cout << nums1[0] << " - " << nums1[1] << std::endl;
    std::cout << nums2[0] << " - " << nums2[1] << std::endl;
    std::cout << nums3[0] << " - " << nums3[1] << std::endl;
    std::cout << nums4[0] << " - " << nums4[1] << std::endl;
    std::cout << "1234" "5678"
    "90" << std::endl;
    char* sc = "1" "2";
    printf("c = %s\n", sc);
    // std::cout << "c = " << sc << std::endl;
    std::string scpp = "1" "2";
    std::cout << "c++ = " << scpp << std::endl;
    char v[10] = "123456";
    std::cout << v << std::endl;
    // v[6] = '1';
    // v[7] = '1';
    // v[8] = '1';
    // v[9] = '1';
    // std::cout << v << std::endl;
    v[2] = '\0';
    std::cout << v << std::endl;
    std::string* vv = new std::string("1234");
    std::cout << vv << std::endl;
    std::cout << *vv << std::endl;
    return 0;
}
