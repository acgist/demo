#include "ggml.h"
#include "ggml-opt.h"

#include <iomanip>
#include <iostream>

int main() {
    std::cout << "1" << std::endl;
    // void* buf_weight = malloc(16 * 1024 * 1024);
    // struct ggml_init_params params = {
    //     .mem_size   = 16 * 1024 * 1024,
    //     .mem_buffer = buf_weight,
    //     .no_alloc   = false,
    // };
    struct ggml_init_params params = {
        .mem_size   = 16 * 1024 * 1024,
        .mem_buffer = NULL,
    };
    ggml_context* ctx = ggml_init(params);
    ggml_tensor * a   = ggml_new_tensor_1d(ctx, GGML_TYPE_F32, 3);
    ggml_tensor * b   = ggml_new_tensor_1d(ctx, GGML_TYPE_F32, 3);
    // ggml_set_param(ctx, a);
    // ggml_set_param(ctx, b);
    // ggml_set_input(a);
    // ggml_set_input(b);
    float* aa = ggml_get_data_f32(a);
    float* bb = ggml_get_data_f32(b);
    aa[0] = 1.0F;
    aa[1] = 2.0F;
    aa[2] = 3.0F;
    bb[0] = 2.0F;
    bb[1] = 2.0F;
    bb[2] = 2.0F;
    // ggml_tensor* c = ggml_add(ctx, a, b);
    // ggml_tensor* c = ggml_mul(ctx, a, b);
    ggml_tensor* c = ggml_mul_mat(ctx, a, b);
    // ggml_set_output(c);
    ggml_cgraph* gf = ggml_new_graph_custom(ctx, 1024, true);
    ggml_build_forward_expand(gf, c);
    // ggml_graph_export(gf, "D:/tmp/test.ggml");
    ggml_graph_compute_with_ctx(ctx, gf, 4);
    float* cc = ggml_get_data_f32(c);
    std::cout.setf(std::ios::fixed);
    std::cout << c->ne[0] << " * " << c->ne[1] << " * " << c->ne[2] << " * " << c->ne[3] << '\n';
    std::cout << std::setprecision(4) << aa[0] << '\n';
    std::cout << std::setprecision(4) << bb[0] << '\n';
    std::cout << std::setprecision(4) << cc[0] << '\n';
    std::cout << std::setprecision(4) << cc[1] << '\n';
    std::cout << std::setprecision(4) << cc[2] << '\n';
    ggml_free(ctx);
    return 0;
}