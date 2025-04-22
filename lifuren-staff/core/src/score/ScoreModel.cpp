#include "lifuren/ScoreModel.hpp"

#include "lifuren/File.hpp"

const static int gru_size    = 128;
const static int num_layers  = 2;
const static int feature_out = 16;
const static float dropout   = 0.0F;

lifuren::score::MozartModuleImpl::MozartModuleImpl() {
    // https://pytorch.org/docs/stable/generated/torch.nn.GRU.html
    // input L N input_size = 乐句长度 批量数量 音符维度
    this->gru_1    = this->register_module("gru_1",    torch::nn::GRU(torch::nn::GRUOptions(gru_size, gru_size).batch_first(true).num_layers(num_layers).dropout(dropout)));
    this->gru_2    = this->register_module("gru_2",    torch::nn::GRU(torch::nn::GRUOptions(gru_size, gru_size).batch_first(true).num_layers(num_layers).dropout(dropout)));
    this->gru_3    = this->register_module("gru_3",    torch::nn::GRU(torch::nn::GRUOptions(gru_size, gru_size).batch_first(true).num_layers(num_layers).dropout(dropout)));
    this->gru_4    = this->register_module("gru_4",    torch::nn::GRU(torch::nn::GRUOptions(gru_size, gru_size).batch_first(true).num_layers(num_layers).dropout(dropout)));
    this->norm_1   = this->register_module("norm_1",   torch::nn::BatchNorm1d(5));
    this->norm_2   = this->register_module("norm_2",   torch::nn::BatchNorm1d(4));
    this->linear_1 = this->register_module("linear_1", torch::nn::Linear(24, gru_size));
    this->linear_2 = this->register_module("linear_2", torch::nn::Linear( 3, gru_size));
    this->linear_3 = this->register_module("linear_3", torch::nn::Linear(gru_size * (5 + 4), 6));
}

lifuren::score::MozartModuleImpl::~MozartModuleImpl() {
    this->unregister_module("gru_1");
    this->unregister_module("gru_2");
    this->unregister_module("gru_3");
    this->unregister_module("gru_4");
    this->unregister_module("norm_1");
    this->unregister_module("norm_2");
    this->unregister_module("linear_1");
    this->unregister_module("linear_2");
    this->unregister_module("linear_3");
    this->gru_1    = nullptr;
    this->gru_2    = nullptr;
    this->gru_3    = nullptr;
    this->gru_4    = nullptr;
    this->norm_1   = nullptr;
    this->norm_2   = nullptr;
    this->linear_1 = nullptr;
    this->linear_2 = nullptr;
    this->linear_3 = nullptr;
}

torch::Tensor lifuren::score::MozartModuleImpl::forward(torch::Tensor input) {
    if(!this->hh1.defined()) {
//  if(!this->hh1.defined() || !this->hh2.defined() || !this->hh3.defined() || !this->hh4.defined()) {
        auto batch_size = input.sizes()[0];
        this->hh1 = torch::zeros({num_layers, batch_size, gru_size}).to(input.device());
        this->hh2 = torch::zeros({num_layers, batch_size, gru_size}).to(input.device());
        this->hh3 = torch::zeros({num_layers, batch_size, gru_size}).to(input.device());
        this->hh4 = torch::zeros({num_layers, batch_size, gru_size}).to(input.device());
    }
    auto ix = input.split({ 24, 3 }, 2);
    auto aa = ix[1].slice(1, 0, 4);
    auto zz = ix[1].slice(1, 1, 5);
    auto i0 = this->linear_1->forward(this->norm_1(ix[0]));
    // auto i1 = this->linear_2->forward(this->norm_2(ix[1]));
    auto i1 = this->linear_2->forward(this->norm_2(zz - aa));
    auto [o1, h1] = this->gru_1->forward(i0, this->hh1);
    auto [o2, h2] = this->gru_2->forward(o1, this->hh2);
    auto [o3, h3] = this->gru_3->forward(i1, this->hh3);
    auto [o4, h4] = this->gru_4->forward(o3, this->hh4);
    return this->linear_3->forward(torch::flatten(torch::cat({
        o2,
        o4
    }, 1), 1, 2));
}

lifuren::score::MozartModel::MozartModel(lifuren::config::ModelParams params) : Model(params) {
}

lifuren::score::MozartModel::~MozartModel() {
}

void lifuren::score::MozartModel::defineDataset() {
    if(lifuren::file::exists(this->params.train_path)) {
        this->trainDataset = lifuren::dataset::score::loadMozartDatasetLoader(this->params.batch_size, this->params.train_path);
    }
    if(lifuren::file::exists(this->params.val_path)) {
        this->valDataset = lifuren::dataset::score::loadMozartDatasetLoader(this->params.batch_size, this->params.val_path);
    }
    if(lifuren::file::exists(this->params.test_path)) {
        this->testDataset = lifuren::dataset::score::loadMozartDatasetLoader(this->params.batch_size, this->params.test_path);
    }
}

void lifuren::score::MozartModel::logic(torch::Tensor& feature, torch::Tensor& label, torch::Tensor& pred, torch::Tensor& loss) {
    pred = this->model->forward(feature);
    loss = this->loss->forward(pred, label);
}
