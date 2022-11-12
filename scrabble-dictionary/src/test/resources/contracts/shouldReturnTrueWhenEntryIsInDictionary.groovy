package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return true when value is in dictionary"
    request {
        method GET()
        url(value(consumer(regex('/api/dictionary/lookup/(polski|gol|rynna|nity|zew)'))))
    }
    response {
        status 200
        body("true")
        headers {
            contentType(applicationJson())
        }
    }
}