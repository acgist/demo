#include <string>
#include <iostream>

using namespace std;

int strStr(string haystack, string needle) {
    if(haystack.size() < needle.size()) {
        return -1;
    }
    int j;
    int i;
    int pos;
    for(i = 0; i < haystack.size(); ++i) {
        if(haystack.size() - i < needle.size()) {
            return -1;
        }
        for(j = 0, pos = 0; j < needle.size(); ++j, ++pos) {
            if(haystack.at(i + pos) != needle.at(j)) {
                // cout << i << pos << " = " << j << " - " << haystack.at(i + pos) << needle.at(j) << endl;
                break;
            }
        }
        if(pos == needle.size()) {
            return i;
        }
    }
    return -1;
}

int strStrEx(string haystack, string needle) {
    int hSzie = haystack.size();
    int nSize = needle.size();
    if(hSzie < nSize) {
        return -1;
    }
    int j;
    int i;
    bool match;
    for(i = 0; i < hSzie; ++i) {
        if(hSzie - i < nSize) {
            return -1;
        }
        match = true;
        for(j = 0; j < nSize; ++j) {
            if(haystack.at(i + j) != needle.at(j)) {
                match = false;
                // cout << i << pos << " = " << j << " - " << haystack.at(i + pos) << needle.at(j) << endl;
                break;
            }
        }
        if(match) {
            return i;
        }
    }
    return -1;
}

int main() {
    cout << strStr("sadbutsad", "sad") << endl;
    cout << strStr("xsadbutsad", "sad") << endl;
    cout << strStr("sxadbutsad", "sad") << endl;
    cout << strStr("sxadbutszad", "sad") << endl;
    cout << strStr("mississippi", "a") << endl;
    cout << strStrEx("sadbutsad", "sad") << endl;
    cout << strStrEx("xsadbutsad", "sad") << endl;
    cout << strStrEx("sxadbutsad", "sad") << endl;
    cout << strStrEx("sxadbutszad", "sad") << endl;
    cout << strStrEx("mississippi", "a") << endl;
    return 0;
}