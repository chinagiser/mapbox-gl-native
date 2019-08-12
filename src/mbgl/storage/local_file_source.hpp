#pragma once

#include <mbgl/storage/file_source.hpp>

namespace mbgl {

namespace util {
template <typename T> class Thread;
} // namespace util

class LocalFileSource : public FileSource {
public:
    LocalFileSource();
    ~LocalFileSource() override;

    std::unique_ptr<AsyncRequest> request(const Resource&, Callback) override;

    static bool acceptsURL(const std::string& url);

    // add by chinagiser.net 20190726
    static bool acceptsMbtilesURL(const std::string& url);
    // add by chinagiser.net 20190729
    static bool acceptsEsriBundle2URL(const std::string& url);
    static bool acceptsEsriBundle3URL(const std::string& url);

private:
    class Impl;

    std::unique_ptr<util::Thread<Impl>> impl;
};

} // namespace mbgl
