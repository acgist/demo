
#include <windows.h>
#include <wincrypt.h>

#include <tuple>
#include <string>
#include <vector>
#include <fstream>
#include <cinttypes>
#include <filesystem>

#pragma comment(lib, "Crypt32.lib")

inline std::tuple<bool, std::vector<unsigned char>> read_der_cert_file(const std::string& file) {
    unsigned long size = std::filesystem::file_size(file);
    std::vector<unsigned char> data;
    data.resize(size);
    std::ifstream stream(file, std::ios_base::binary);
    stream.read(reinterpret_cast<char*>(data.data()), size);
    stream.close();
    return {true, data};
}

inline std::tuple<bool, std::vector<unsigned char>> read_pem_cert_file(std::string base64) {
    std::vector<unsigned char> data;
    unsigned long size = 0;
    if (!CryptStringToBinary(
        base64.data(),
        base64.length(),
        CRYPT_STRING_BASE64HEADER,
        NULL,
        &size,
        NULL,
        NULL
    )) {
        return {false, {}};
    }
    data.resize(size);
    if (!CryptStringToBinary(
        base64.data(),
        base64.length(),
        CRYPT_STRING_BASE64HEADER,
        data.data(),
        &size,
        NULL,
        NULL
    )) {
        return {false, {}};
    }
    return {true, data};
}

std::tuple<bool, std::vector<unsigned char>> read_cert_file(const std::string& file) {
    if(!std::filesystem::exists(file)) {
        return read_pem_cert_file(R"(-----BEGIN CERTIFICATE-----
MIIEsTCCAxmgAwIBAgIQX+8PzCfzUzskRfoH3A4EHDANBgkqhkiG9w0BAQsFADBx
MR4wHAYDVQQKExVta2NlcnQgZGV2ZWxvcG1lbnQgQ0ExIzAhBgNVBAsMGmNteEBj
bXhkZU1hY0Jvb2stQWlyLmxvY2FsMSowKAYDVQQDDCFta2NlcnQgY214QGNteGRl
TWFjQm9vay1BaXIubG9jYWwwHhcNMjEwNjIzMDcwMDM5WhcNMzEwNjIzMDcwMDM5
WjBxMR4wHAYDVQQKExVta2NlcnQgZGV2ZWxvcG1lbnQgQ0ExIzAhBgNVBAsMGmNt
eEBjbXhkZU1hY0Jvb2stQWlyLmxvY2FsMSowKAYDVQQDDCFta2NlcnQgY214QGNt
eGRlTWFjQm9vay1BaXIubG9jYWwwggGiMA0GCSqGSIb3DQEBAQUAA4IBjwAwggGK
AoIBgQDZAHdaT3LXJrcY9Q4odITvfckpZ6G/2RbFrKfs46LiV93Y9NnIEdFA4Im1
OCendF+tFWSUxJ2stXfKidpyCU/W9DjEAIGMr1tv5ud2gmfGwfmokN4k+Yhgy5I6
nKSEDjilwjXqZ6nrGDTsHq5E3fCdGHPoHOCk8e2nr4KbZkXDqHPc9W77LDELvm/n
Hou0gq4dqslbuKjKmnoEu2AnBhl5s2V2Lr70A4GRU/iBpW3XXrfh2badhcykzwbh
a1fUARpHC3K6lMAgi2kYaFXdQgd5KQbtWNZNMjJQN6dNYCih5XRNeio81UAqaPTT
n+T6qTkF/3XTbLS8DPh0cFGN4MjPPb3c4VLkobdmeNmJdP0H+uUy6eJtAMPqRUFT
VvATs2KW3dHU2OgMARvHsuxA9nn1C4RSeNE3wOH2HY+VPexvU+jR2b3GZyjd6eUG
zmSA9I0UYQmrudXXR8R9wvj2w9esQWx2nt/LghchQeTRwcp4u8KfEASIehPVMcdM
7sw9pWsCAwEAAaNFMEMwDgYDVR0PAQH/BAQDAgIEMBIGA1UdEwEB/wQIMAYBAf8C
AQAwHQYDVR0OBBYEFDyPXucRwVAU2cuhW11CZsB/Yb2JMA0GCSqGSIb3DQEBCwUA
A4IBgQCHseNa/c+lDdg8z+VZIPMTFDIQM3CpMLBPMb+VDYMcmEK5JkJVked1kM/m
rinYqtqufNLn1lfGcDyugTx1SfRN2qOICttwabsk0relgBe0NU7/StxAySKNVDnG
r9NCAEBSxeMQdjvFI+qW/GwUzU8bMGrZ3fbcD8SmHNP53dLAvBrpUfUG3imzE0+w
p50bsPqxsQSmr6Pbv8jHRjwScvX50UJW/6TSCML/0ixLzvb2yq7sEcigA6eRJ7bX
GEDk+2ySlYeof5iS9KyHM5jkhaA76zPwISKOZnJYkLwUh/EM2KYwbco2U6zBHBdO
B+3F3sTXtw0c4WBIbGtEvKaYnWJuAh2OqmIwJyH35reqNo8KUD1USXHf7U4x3f0U
4piYeUnjGjFaEcD9QlsFMpGNJC4OBK1au2fNmEli1OTAfDuZW3jVSnEBrAT38W3P
bGkqRt2x4Z/+YzBiFUcNMn21kR9y5yWpQ0tHiW42KKjz+z+BVSLLICagO8wKWTe2
jyylHxY=
-----END CERTIFICATE-----)");
    } else if(file.ends_with(".der")) {
        return read_der_cert_file(file);
    } else if(file.ends_with(".crt") || file.ends_with(".pem")) {
        unsigned long size = std::filesystem::file_size(file);
        std::string base64;
        base64.resize(size);
        std::ifstream stream(file, std::ios_base::binary);
        stream.read(base64.data(), size);
        stream.close();
        return read_pem_cert_file(base64);
    } else {
        return {false, {}};
    }
}

bool import_cert(LPCSTR certPath, LPCSTR storeName) {
    auto [ success, cert_data ] = read_cert_file(certPath);
    if(!success) {
        std::printf("证书读取失败：%u\n", GetLastError());
        return false;
    }
    HCERTSTORE store = CertOpenSystemStore(0, storeName);
    if (!store) {
        std::printf("打开存储失败\n");
        return false;
    }
    if (!CertAddEncodedCertificateToStore(
        store,
        X509_ASN_ENCODING | PKCS_7_ASN_ENCODING,
        cert_data.data(),
        cert_data.size(),
        CERT_STORE_ADD_REPLACE_EXISTING,
        NULL
    )) {
        std::printf("证书导入失败：%u\n", GetLastError());
        CertCloseStore(store, 0);
        return false;
    }
    CertCloseStore(store, 0);
    return true;
}

int main(int argc, const char* argv[]) {
    system("chcp 65001");
    // openssl x509 -inform DER -in ca.der -out ca.pem
    // openssl x509 -in .\ca.crt -out ca.der -outform DER
    std::printf("开始导入证书\n");
    const char* file = "ca.crt";
    const char* name = "root";
    if(argc >= 2) {
        file = argv[1];
    }
    if(argc >= 3) {
        name = argv[2];
    }
    if (import_cert(file, name)) {
        std::printf("导入证书成功\n");
        MessageBoxW(NULL, L"证书安装成功", L"证书安装", MB_OK);
    } else {
        std::printf("导入证书失败\n");
        MessageBoxW(NULL, L"证书安装失败", L"证书安装", MB_OK);
    }
    std::printf("输入回车结束\n");
    return 0;
}
