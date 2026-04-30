#include <map>
#include <vector>
#include <string>

namespace acgist {

/**
 * @param data 数据
 * @param col  列
 * @param pos  偏移
 * 
 * @return 列统计
 */
std::map<std::string, size_t> countCols(const std::vector<std::vector<std::string>>& data, const int& col, const int& pos = 0);

/**
 * @param data 数据
 * @param col  列
 * @param pos  偏移
 * 
 * @return 列选择
 */
std::vector<std::string> selectCols(const std::vector<std::vector<std::string>>& data, const int& col, const int& pos = 0);

/**
 * @param map 数据
 */
void plotPie(const std::map<std::string, size_t>& map);

/**
 * @param vector 数据
 */
void plotHist(const std::vector<std::string>& vector);

}