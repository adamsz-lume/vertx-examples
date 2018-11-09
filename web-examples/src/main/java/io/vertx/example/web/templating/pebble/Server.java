package io.vertx.example.web.templating.pebble;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.templ.pebble.PebbleTemplateEngine;

/**
 * This is an example application to showcase the usage of Vert.x Web.
 *
 * In this application you will see the usage of:
 *
 *  * Pebble templates
 *  * Vert.x Web
 *
 * @author <a href="mailto:pmlopes@gmail.com>Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    // To simplify the development of the web components we use a Router to route all HTTP requests
    // to organize our code in a reusable way.
    final Router router = Router.router(vertx);

    // In order to use a template we first need to create an engine
    final PebbleTemplateEngine engine = PebbleTemplateEngine.create(vertx);

    // Entry point to the application, this will render a custom template.
    router.get().handler(ctx -> {
      // we define a hardcoded title for our application
      JsonObject data = new JsonObject()
        .put("name", "Vert.x Web")
        .put("path", ctx.request().path());

      // and now delegate to the engine to render it.
      engine.render(data, "templates/index.peb", res -> {
        if (res.succeeded()) {
          ctx.response().end(res.result());
        } else {
          ctx.fail(res.cause());
        }
      });
    });

    // start a HTTP web server on port 8080
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}
