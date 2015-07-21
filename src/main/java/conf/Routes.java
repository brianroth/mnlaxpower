/**
 * Copyright (C) 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package conf;

import ninja.AssetsController;
import ninja.Router;
import ninja.application.ApplicationRoutes;
import controllers.ApplicationController;
import controllers.CacheController;
import controllers.MyAssetsController;
import controllers.TeamsController;

public class Routes implements ApplicationRoutes {

    @Override
    public void init(Router router) {

        router.GET().route("/division/{divisionId}").with(ApplicationController.class, "index");
        
        router.GET().route("/").with(ApplicationController.class, "index");

        router.GET().route("/faq").with(ApplicationController.class, "faq");

        router.GET().route("/recache").with(CacheController.class, "recache");

        router.GET().route("/recache/{d}").with(CacheController.class, "recache");
        
        router.GET().route("/recalculate").with(CacheController.class, "recalculate");

        router.GET().route("/team/{teamId}").with(TeamsController.class, "show");
        
        // /////////////////////////////////////////////////////////////////////
        // Assets (pictures / javascript)
        // /////////////////////////////////////////////////////////////////////
        router.GET().route("/assets/webjars/{fileName: .*}").with(AssetsController.class, "serveWebJars");
        router.GET().route("/assets/{fileName: .*}").with(MyAssetsController.class, "serveStatic");
    }

}
