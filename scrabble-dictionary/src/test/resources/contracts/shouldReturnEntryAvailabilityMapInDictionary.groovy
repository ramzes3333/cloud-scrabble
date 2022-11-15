package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return entry availability map in dictionary"
    request {
        method GET()
        url('/api/dictionary/lookup') {
            queryParameters {
                parameter("entries", "polski")
                parameter("entries", "gol")
                parameter("entries", "dg")
            }
        }
    }
    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body("""
                    {
                      "gol": true,
                      "polski": true,
                      "dg": false
                    }
                    """)
    }
}
