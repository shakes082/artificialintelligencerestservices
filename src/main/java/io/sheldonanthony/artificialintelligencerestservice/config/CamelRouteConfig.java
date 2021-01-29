package io.sheldonanthony.artificialintelligencerestservice.config;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import io.sheldonanthony.artificialintelligencerestservice.dtos.DetectFacesRequest;
import io.sheldonanthony.artificialintelligencerestservice.dtos.DetectFacesResponse;
import io.sheldonanthony.artificialintelligencerestservice.dtos.Ping;
import static org.apache.camel.model.rest.RestParamType.body;
import static org.apache.camel.model.rest.RestParamType.path;

@Configuration
public class CamelRouteConfig extends RouteBuilder{
	
	@Autowired
    private Environment env;
    
    @Value("${camel.component.servlet.mapping.context-path}")
    private String contextPath;

    @Override
    public void configure() throws Exception {

        // @formatter:off
        
        // this can also be configured in application.properties
        restConfiguration()
            .component("servlet")
            .bindingMode(RestBindingMode.json)
            .dataFormatProperty("prettyPrint", "true")
            .enableCORS(true)
            .port(env.getProperty("server.port", "8080"))
            .contextPath(contextPath.substring(0, contextPath.length() - 2))
            // turn on swagger api-doc
            .apiContextPath("/api-doc")
            .apiProperty("api.title", "Artificial Intelligence Services")
            .apiProperty("api.version", "1.0.0");
        
        rest("/serverconfiguration").description("Server Configurations").
        produces("application/json").consumes("application/json").
        	get("/ping").id("ping-server-request").description("Ping Server Request").
        	outType(Ping[].class).responseMessage().code(200).message("Ping").
        	endResponseMessage().to("bean:artificialintelligenceservice?method=ping");
        
        rest("/vision").apiDocs(true).description("Rest Services For Vision").produces("application/json")
        .consumes("application/json").apiDocs(true).enableCORS(true)
        
        .post("/detectFaces").
        id("detect-faces").consumes("application/json").produces("application/json").
        description("Detect Faces In An Image").type(DetectFacesRequest[].class).
        outType(DetectFacesResponse[].class).responseMessage().code(200).message("Detect Faces").
        endResponseMessage().to("bean:artificialintelligenceservice?method=detectFaces");
       
    }
}
