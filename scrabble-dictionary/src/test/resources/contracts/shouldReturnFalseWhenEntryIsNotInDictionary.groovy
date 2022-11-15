package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return false when entry is not in dictionary"
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
