# Routing for Dummies
So you need to add a new route to the application? Great! Here's how to do it properly...

## 1. Imports
Every class will need the Spark imports.
```java
import static spark.Spark.*;
```

## 2. The Init Method
This is where the magic really happens - you register your routes in this method. Example:
```java
@Init
public static void init() {
    // Where methodName is a method which takes the required params. See below.
    get("/url/path/here", ClassName::methodName);
}
```
The Spark docs have all the available routing methods, not just `get`. If you really want, you can have as many @Init
marked methods as you want, but that might be silly. The method name is also not important, just the 'public static'
part and lack of parameters.

## 3. Handling the Request
You'll need a method that Spark can use to actually execute your request. The parameter types are important and must be
exactly the right types!
```java
public static Object methodName(Request req, Response res) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("val", "example");
    map.put("list", new Object[]{"test", "more test"});
    
    TemplateEngine eng = new MustacheTemplateEngine(new DefaultMustacheFactory()));
    return eng.render(eng.modelAndView(map, "yourFileHere.mustache"));
}
```
This example also demonstrates Mustache rendering, where `yourFileHere.mustache` exists in `resources/templates`. The
map is passed as the hash to Mustache for rendering. You might want to look at `[...].common.MustacheDemo` for pointers.

## 4. Profit
No really, that's all you have to do to handle a new route!