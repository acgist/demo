#include "torch/data.h"

#ifndef LFT_SAMPLER
#define LFT_RND_SAMPLER torch::data::samplers::RandomSampler
#define LFT_SEQ_SAMPLER torch::data::samplers::SequentialSampler
#endif

class Dataset : public torch::data::Dataset<Dataset> {

private:
    std::vector<torch::Tensor> labels;   // 标签
    std::vector<torch::Tensor> features; // 特征

public:
    Dataset() = default;
    Dataset(const Dataset& ) = default;
    Dataset(      Dataset&&) = default;
    Dataset& operator=(const Dataset& ) = delete;
    Dataset& operator=(      Dataset&&) = delete;
    /**
     * @param labels   标签
     * @param features 特征
     */
    Dataset(
        std::vector<torch::Tensor>& labels,
        std::vector<torch::Tensor>& features
    ) : labels(std::move(labels)), features(std::move(features)) {
    }

public:
    torch::optional<size_t> size() const override {
        return this->labels.size();
    };

    torch::data::Example<> get(size_t index) override {
        return {
            this->features[index],
            this->labels  [index]
        };
    };

};
