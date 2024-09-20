#include "matplot/matplot.h"

int main() {
    // matplot::title("acgist");
    // matplot::sgtitle("acgist");
    // matplot::figure()->title("123");
    auto plot = matplot::figure();
    plot->name("acgist");
    plot->number_title(false);
    matplot::hold(matplot::on);
    // ----
    // std::vector<double> x = matplot::linspace(0, 2 * matplot::pi);
    // std::vector<double> y = matplot::transform(x, [](auto x) {
    //     return sin(x);
    // });
    // matplot::plot(x, y, "-");
    // matplot::plot(x, matplot::transform(y, [](auto y) {
    //     return -y;
    // }), "--r");
    // ----
    // auto z = matplot::linspace(-10, 10, 1000);
    // auto x = matplot::transform(z, [](auto v) { return exp(-v / 10) * sin(5 * v); });
    // auto y = matplot::transform(z, [](auto v) { return exp(-v / 10) * cos(5 * v); });
    // auto p = matplot::plot3(x, y, z);
    // p->line_width(3);
    // ----
    // auto z = matplot::linspace(-10, 10, 1000);
    // auto x = matplot::linspace(-10, 10, 1000);
    // auto y = matplot::transform(z, x,  [](auto u, auto v) { return u * u + v; });
    // auto p = matplot::plot3(x, y, z);
    // p->line_width(3);
    // ----
    // auto x = matplot::linspace(-100, 100, 1000);
    // auto y = matplot::transform(x, [](auto v) { return v * 5; });
    // matplot::plot(x, y);
    // ----
    // auto z = matplot::linspace(0., 4. * matplot::pi, 250);
    // auto x = matplot::transform(z, [](double z) { return 2 * cos(z) + matplot::rand(0, 1); });
    // auto y = matplot::transform(z, [](double z) { return 2 * sin(z) + matplot::rand(0, 1); });
    // matplot::scatter3(x, y, z);
    // ----
    std::vector<double> vector { 1.0, 2.0, 1.0, 4.0 };
    matplot::hist(vector);
    matplot::show(plot);
    return 0;
}
