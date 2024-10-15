#include <omp.h>

#include <iostream>

/**
 * https://learn.microsoft.com/zh-cn/cpp/parallel/openmp/reference/openmp-directives?view=msvc-170
 */

int main() {
    int width  = 1280;
    int height = 1280;
    // float *imageBuffer = new float[3 * width * height];
    // gcc Thread.cpp -fopenmp
    #pragma omp parallel num_threads(3)
    {
        
        #pragma omp critical
        {
            std::cout << "----";
            std::cout << omp_get_thread_num() << '=';
            std::cout << omp_get_num_threads() << '\n';
        }
        // int tid = omp_get_thread_num();
        // for (int i = 0; i < width * height; i++)
        // {
        //     imageBuffer[i] = 0;
        //     imageBuffer[width * height + i] = 255;
        //     imageBuffer[width * height * 2 + i] = 0;
        // }
    }
    return 0;
}
