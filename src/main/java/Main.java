import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.validator.UrlValidator;

import static spark.Spark.*;
import spark.template.freemarker.FreeMarkerEngine;
import spark.ModelAndView;


public class Main {

  public static String getURLContent(String url) throws IOException {
    //better testing could be done.
    if (url == null || url.length() == 0) { return ""; }
    
    String[] schemes = {"http","https"};
    UrlValidator urlValidator = new UrlValidator(schemes);
    if (urlValidator.isValid(url)) {
      
    } else {
       return "URL is invalid.  Make sure to choose protocol (http:// or https://)";
    }
    
    String responseBody = "";
    
    HttpClient client = new HttpClient();

    // Create a method instance.
    GetMethod method = new GetMethod(url);
    
    // Provide custom retry handler is necessary
    /*
    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
            new DefaultHttpMethodRetryHandler(2, false));
     */
    try {
      // Execute the method.
      int statusCode = client.executeMethod(method);

      if (statusCode != HttpStatus.SC_OK) {
        return("Method failed: " + method.getStatusLine());
      }

      // Read the response body.
      /*
      InputStreamReader breader = new InputStreamReader(method.getResponseBodyAsStream());
      StringBuffer sb = new StringBuffer();
      int c;
      while ((c = breader.read()) != -1) {
          sb.append(c);
      }
       responseBody = sb.toString();
       */
      responseBody = method.getResponseBodyAsString();
    } catch (HttpException e) {

      return("Fatal protocol violation: " + e.getMessage());
    } catch (IOException e) {
      return("Fatal transport error: " + e.getMessage());
    } finally {
      // Release the connection.
      method.releaseConnection();
    }
    return responseBody;
  }
  public static void main(String[] args) {

    port(Integer.valueOf(System.getenv("PORT")));
    staticFileLocation("/public");

    post("/", (request, response) -> {
      String url = request.queryParams("address");
      String responseText = "";
      
      try {
        responseText = getURLContent(url);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      HTMLTagParse hp = new HTMLTagParse();
      responseText = hp.replaceTagsString(responseText);
      //responseText = hp.replaceTags(responseText);
      Map<String, Object> attributes = new HashMap<>();
      attributes.put("count", hp.getTagCount());
      attributes.put("address", url);
      attributes.put("source", responseText);

      return new ModelAndView(attributes, "home.ftl");
    }, new FreeMarkerEngine());

    get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            
            attributes.put("address", "");
            attributes.put("source", "");
            
            return new ModelAndView(attributes, "home.ftl");
        }, new FreeMarkerEngine());
    
  }

}
