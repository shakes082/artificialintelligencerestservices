package io.sheldonanthony.artificialintelligencerestservice.config;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import io.sheldonanthony.artificialintelligencerestservice.dtos.DetectAndTraceFacesRequest;
import io.sheldonanthony.artificialintelligencerestservice.dtos.DetectAndTraceFacesResponse;
import io.sheldonanthony.artificialintelligencerestservice.dtos.DetectFacesRequest;
import io.sheldonanthony.artificialintelligencerestservice.dtos.DetectFacesResponse;
import io.sheldonanthony.artificialintelligencerestservice.dtos.DetectIfFacesAreWearingMasksRequest;
import io.sheldonanthony.artificialintelligencerestservice.dtos.DetectIfFacesAreWearingMasksResponse;
import io.sheldonanthony.artificialintelligencerestservice.dtos.Ping;

/*
 * Copyright (c) 2020, Sheldon Anthony
 * All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the Apache License, Version 2.0 only, as
 * published by the Free Software Foundation.  
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the Apache License, Version 2.0
 * for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the Apache License, Version 2.0
 * along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sheldon Anthony, PostNet Suite 129,
 * Private Bag X 1510, Glenvista, 2058, Johannesburg, South Africa
 * or visit https://shakes082.github.io/sheldonanthonyio.github.io/
 * if you need additional information
 *  or have any questions.
 */

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
        	outType(Ping.class).responseMessage().code(200).message("Ping").
        	endResponseMessage().to("bean:artificialintelligenceservice?method=ping");
        
        rest("/vision").apiDocs(true).description("Rest Services For Vision").produces("application/json")
        .consumes("application/json").apiDocs(true).enableCORS(true)
        
	        .post("/detectFaces").id("detect-faces")
	        .description("Detect faces in a base64 encoded image")
	        .type(DetectFacesRequest.class).outType(DetectFacesResponse.class)
	        .responseMessage().endResponseMessage().to("bean:artificialintelligenceservice?method=detectFaces")
        
	        .post("/detectAndTraceFaces").id("detect-trace-faces")
	        .description("Detect and trace faces in a base64 encoded image")
	        .type(DetectAndTraceFacesRequest.class).outType(DetectAndTraceFacesResponse.class)
	        .responseMessage().endResponseMessage().to("bean:artificialintelligenceservice?method=detectAndTraceFaces")
	        
	        .post("/detectIfFacesAreWearingMasks").id("detect-faces-wearing-masks")
	        .description("Detect if faces are wearing masks in a base64 encoded image")
	        .type(DetectIfFacesAreWearingMasksRequest.class).outType(DetectIfFacesAreWearingMasksResponse.class)
	        .responseMessage().endResponseMessage().to("bean:artificialintelligenceservice?method=detectIfFaceWearingMask");
       
    }
}
