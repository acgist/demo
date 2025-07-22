#include "jshare/jshare.hpp"

#include <fstream>
#include <iostream>
#include <filesystem>

jshare::Client::Client(const std::string& path) : path(path) {
    this->udp = std::make_shared<jshare::Udp>([this](const uint8_t type, const char* data, const int data_length, const sockaddr_in& addr) {
        this->udp_callback(type, data, data_length, addr);
    });
    this->udp->broadcast();
}

jshare::Client::~Client() {
}

void jshare::Client::udp_callback(const uint8_t type, const char* data, const int data_length, const sockaddr_in& addr) {
    static std::shared_ptr<std::ofstream> stream{ nullptr };
    std::cout << (int) type << std::endl;
    if(type == jshare::type_file_send) {
        if(stream) {
            return;
        }
        std::string filename(data, data_length);
        auto filepath = std::filesystem::path(this->path);
        auto old = filepath / filename;
        auto idx = 0;
        while(std::filesystem::exists(old)) {
            old = std::to_string(idx) + "_" + filename;
            ++idx;
        }
        filepath = old;
        stream = std::make_shared<std::ofstream>(filepath, std::ios::out | std::ios::binary);
        if(!stream->is_open()) {
            stream = nullptr;
            return;
        }
        if(this->send_callback) {
            this->send_callback(data, 0, false);
        }
    } else if(type == jshare::type_file_stop) {
        if(stream) {
            stream->close();
            stream = nullptr;
        }
        if(this->recv_callback) {
            this->recv_callback(data, 0, true);
        }
    } else if(type == jshare::type_file_data) {
        if(stream) {
            stream->write(data, data_length);
        }
        if(this->recv_callback) {
            this->recv_callback("文件名称", 0, false);
        }
    } else if(type == jshare::type_broadcast) {
        jshare::Remote remote{
            .host = "192.168.12.123", //inet_ntoa(addr.sin_addr),
            .port = addr.sin_port
        };
        if(std::find(this->remote.begin(), this->remote.end(), remote) == this->remote.end()) {
            this->remote.push_back(remote);
        }
    } else {
        // -
    }
}

bool jshare::Client::recv(std::function<void(const std::string&, int, bool)> callback) {
    this->recv_callback = callback;
    return true;
}

bool jshare::Client::send(const Remote& remote, std::function<void(const std::string&, int, bool)> callback) {
    this->send_callback = callback;
    if(this->file.empty()) {
        return true;
    }
    sockaddr_in addr;
    addr.sin_family      = AF_INET;
    addr.sin_port        = htons(18888);
    addr.sin_addr.s_addr = inet_addr(remote.host.c_str());
    for(const auto& value : file) {
        auto filepath = std::filesystem::path(value);
        if(!(std::filesystem::exists(filepath) && std::filesystem::is_regular_file(filepath))) {
            continue;
        }
        std::ifstream stream(filepath, std::ios::in | std::ios::binary);
        if(!stream.is_open()) {
            continue;
        }
        this->udp->sync(addr);
        {
            const std::string name = filepath.filename().string();
            uint16_t data_length = name.length();
            std::vector<char> buffer(HEADER_SIZE + data_length);
            buffer[0] = jshare::version;
            buffer[1] = jshare::type_file_send;
            std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
            std::memcpy(buffer.data() + 6, name.data(), data_length);
            this->udp->send(buffer, addr, true, true);
            callback(value, 0, false);
        }
        {
            const uint16_t max_length = UDP_SIZE - HEADER_SIZE;
            std::vector<char> buffer(HEADER_SIZE + max_length);
            while(true) {
                stream.read(buffer.data() + HEADER_SIZE, max_length);
                uint16_t data_length = stream.gcount();
                if(data_length == 0) {
                    break;
                }
                buffer[0] = jshare::version;
                buffer[1] = jshare::type_file_data;
                std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
                if(data_length != max_length) {
                    buffer.resize(HEADER_SIZE + data_length);
                }
                this->udp->send(buffer, addr);
                buffer.resize(HEADER_SIZE + max_length);
            }
            callback(value, 0, false);
        }
        {
            const std::string name = filepath.filename().string();
            uint16_t data_length = name.length();
            std::vector<char> buffer(HEADER_SIZE + data_length);
            buffer[0] = jshare::version;
            buffer[1] = jshare::type_file_stop;
            std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
            std::memcpy(buffer.data() + 6, name.data(), data_length);
            this->udp->send(buffer, addr, true, true);
            callback(value, 0, true);
        }
    }
    return true;
}

std::vector<jshare::Remote> jshare::Client::remote_list() {
    return this->remote;
}

bool jshare::Client::file_put(const std::string& file) {
    if(std::find(this->file.begin(), this->file.end(), file) == this->file.end()) {
        this->file.push_back(file);
    }
    return true;
}

bool jshare::Client::file_cls() {
    this->file.clear();
    return true;
}

std::vector<std::string> jshare::Client::file_list() {
    return this->file;
}

bool jshare::Client::reset() {
    this->mode = Mode::NONE;
    this->file.clear();
    this->remote.clear();
    return true;
}

bool jshare::Client::close() {
    return true;
}
