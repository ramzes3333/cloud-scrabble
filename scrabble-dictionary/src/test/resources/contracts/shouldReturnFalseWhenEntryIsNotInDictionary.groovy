package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return true when value is in dictionary"
    request {
        method GET()
        url(value(consumer(regex('/api/dictionary/lookup/[a-zA-Z]{2}'))))
    }
    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body("false")
    }
}