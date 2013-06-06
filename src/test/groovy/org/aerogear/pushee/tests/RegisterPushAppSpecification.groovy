package org.aerogear.pushee.tests;

import groovy.json.JsonBuilder

import org.jboss.arquillian.container.test.api.Deployment
import org.jboss.arquillian.test.api.ArquillianResource
import org.jboss.shrinkwrap.api.ShrinkWrap
import org.jboss.shrinkwrap.api.spec.WebArchive
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter

import spock.lang.Specification

import com.jayway.restassured.RestAssured

class RegisterPushAppSpecification extends Specification {

    @Deployment(testable=false)
    def static WebArchive "create deployment"() {
        return Deployments.unifiedPushServer()
    }

    @ArquillianResource URL root;

    void "Registering a push application"() {

        given: "Application My App is about to be registered......"
                def json = new JsonBuilder()
                def request = RestAssured.given()
                                .contentType("application/json")
                                .header("Accept", "application/json")
                                .body(json {
                                    name: "MyApp"
                                    description: "awesome app"
                                })

        when: "Application is registered"
                def response = RestAssured.given().spec(request).post(root.toString() + "rest/applications")

        then: "Response code 200 is returned"
                response.statusCode() == 200

        and: "Push App Id is not null"
                response.body().path("id") != null
    }


}
