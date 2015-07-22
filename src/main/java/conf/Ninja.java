package conf;

import ninja.Context;
import ninja.NinjaDefault;
import ninja.Result;
import ninja.Results;
import ninja.utils.NinjaConstant;

public class Ninja extends NinjaDefault {

    @Override
    public Result getNotFoundResult(Context context) {
        Result result = Results.notFound()
                .supportedContentTypes(Result.TEXT_HTML, Result.APPLICATION_JSON, Result.APPLICATION_XML)
                .fallbackContentType(Result.TEXT_HTML).render("message", "Oh no")
                .template(NinjaConstant.LOCATION_VIEW_FTL_HTML_NOT_FOUND);

        return result;
    }

    @Override
    public Result onException(Context context, Exception exception) {

        Result result = Results.internalServerError()
                .supportedContentTypes(Result.TEXT_HTML, Result.APPLICATION_JSON, Result.APPLICATION_XML)
                .fallbackContentType(Result.TEXT_HTML).render("message", "Oh no")
                .template(NinjaConstant.LOCATION_VIEW_FTL_HTML_INTERNAL_SERVER_ERROR);

        return result;

    }
}
