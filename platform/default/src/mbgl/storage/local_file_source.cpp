#include <mbgl/storage/local_file_source.hpp>
#include <mbgl/storage/file_source_request.hpp>
#include <mbgl/storage/local_file_request.hpp>
#include <mbgl/storage/response.hpp>
#include <mbgl/util/string.hpp>
#include <mbgl/util/thread.hpp>
#include <mbgl/util/url.hpp>

namespace {

const std::string fileProtocol = "file://";
// add by chinagiser.net 20190726
const std::string mbtilesProtocol = "mbtiles://";
// add by chinagiser.net 20190729
const std::string esriBundle2Protocol = "esribundle2://";
const std::string esriBundle3Protocol = "esribundle3://";

} // namespace

namespace mbgl {

class LocalFileSource::Impl {
public:
    Impl(ActorRef<Impl>) {}

    void request(const std::string& url, ActorRef<FileSourceRequest> req) {
        if (!acceptsURL(url)) {
            Response response;
            response.error = std::make_unique<Response::Error>(Response::Error::Reason::Other,
                                                               "Invalid file URL");
            req.invoke(&FileSourceRequest::setResponse, response);
            return;
        }

        // Cut off the protocol and prefix with path.
        const auto path = mbgl::util::percentDecode(url.substr(fileProtocol.size()));
        requestLocalFile(path, std::move(req));
    }
};

LocalFileSource::LocalFileSource()
    : impl(std::make_unique<util::Thread<Impl>>("LocalFileSource")) {
}

LocalFileSource::~LocalFileSource() = default;

std::unique_ptr<AsyncRequest> LocalFileSource::request(const Resource& resource, Callback callback) {
    auto req = std::make_unique<FileSourceRequest>(std::move(callback));

    impl->actor().invoke(&Impl::request, resource.url, req->actor());

    return std::move(req);
}

bool LocalFileSource::acceptsURL(const std::string& url) {
    return 0 == url.rfind(fileProtocol, 0);
}

// add by chinagiser.net 20190726
bool LocalFileSource::acceptsMbtilesURL(const std::string& url) {
    return 0 == url.rfind(mbtilesProtocol, 0);
}
// add by chinagiser.net 20190729
bool LocalFileSource::acceptsEsriBundle2URL(const std::string& url){
    return 0 == url.rfind(esriBundle2Protocol, 0);
}
bool LocalFileSource::acceptsEsriBundle3URL(const std::string& url){
    return 0 == url.rfind(esriBundle3Protocol, 0);
}

} // namespace mbgl
