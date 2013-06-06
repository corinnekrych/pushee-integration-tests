package org.aerogear.pushee.tests

import com.jayway.restassured.RestAssured
import groovy.json.JsonBuilder
import org.jboss.arquillian.container.test.api.Deployment
import org.jboss.arquillian.test.api.ArquillianResource
import org.jboss.shrinkwrap.api.spec.WebArchive
import spock.lang.Specification

import spock.lang.Shared
import com.jayway.restassured.internal.TestSpecificationImpl

class RegisterMobileVariantsSpecification extends Specification {

    @Deployment(testable=false)
    def static WebArchive"create deployment"() {
        return Deployments.unifiedPushServer()
    }

    @ArquillianResource
    URL root;

    @Shared TestSpecificationImpl testSpec = RestAssured.createTestSpecification()
    @Shared def pushAppId
    void "Registering a push application"() {

        given: "Application My App is about to be registered......"
        def params= [name:"myApp", description:"mine"]
//        JsonBuilder json = new JsonBuilder()
//        json {
//            name "ddd"
//            description "ddd"
//        }
        def request = testSpec.getRequestSpecification()
                .contentType("application/json")
                .header("Accept", "application/json")
                .body(["name":"ddd", description:"ddd"])

        when: "Application is registered"
        def response = testSpec.getRequestSpecification().given().spec(request).post(root.toString() + "rest/applications")

        def maVar = response.body().jsonPath()
        pushAppId = maVar.get("id")

        then: "Response code 200 is returned"
        response.statusCode() == 200

        and: "Push App Id is not null"
        maVar.get("id") != null

        and: "AppName is not null"
        maVar.get("name") == "ddd"

    }


    void "Registering a mobile variant instance"() {
        given: "Application My App is about to be registered......"
        def json = new JsonBuilder()
        def request = testSpec.getRequestSpecification()
                .contentType("application/json")
                .header("Accept", "application/json")
                .body([pushNetworkURL : "http://localhost:7777/endpoint/"])

        when: "Application is registered"
        def response = testSpec.getRequestSpecification().given().spec(request).post(root.toString() + "rest/applications/" + pushAppId + "/simplePush ")

        def maVar = response.body().jsonPath()

        then: "Response code 200 is returned"
        response.statusCode() == 200

        and: "Push App Id is not null"
        maVar.get("id") != null

    }
}

