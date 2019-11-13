#pragma once

#include <string>

namespace mbgl {
namespace util {

std::string compress(const std::string& raw);
std::string decompress(const std::string& raw);

// add by chinagiser.net 20190818
std::string decompressWithHeader(const std::string &raw);

} // namespace util
} // namespace mbgl
