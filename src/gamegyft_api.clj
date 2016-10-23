(ns gamegyft-api
  (:gen-class)
  (:require [clj-http.client :as client]
            [clojure.java.io :as io]
            [cheshire.core :as json]
            [ring.adapter.jetty :as jetty]))


(def auth {:headers {"Accept" "application/json"}
           :keystore (io/resource "keystore.jks")
           :keystore-type "jks" ; default: jks
           :keystore-pass "password"
           :basic-auth ["B9JJ4W8N6MDLTT479YJB2187QaZaU8m_7F7qcPI3ny6hgmy8E"
                        "LDAtRu2iCebyUZ53IEpzA0xnzojZ57VIgOR"]})


; first leg of visa direct transaction
(defn pull-funds [] 
  (client/post
      "https://sandbox.api.visa.com/visadirect/fundstransfer/v1/pullfundstransactions"
      (merge auth
             {:content-type :json
              :body 
              (json/generate-string
                {"acquirerCountryCode" "840"
                 "acquiringBin" "408999"
                 "amount" "10.00"
                 "businessApplicationId" "AA"
                 "cardAcceptor" {"address" {"country" "USA"
                                            "county" "San Mateo"
                                            "state" "CA"
                                            "zipCode" "94404"}
                                 "idCode" "ABCD1234ABCD123"
                                 "name" "A Marqueta Gift Card"
                                 "terminalId" "ABCD1234"}
                    
                  "cavv" "0700100038238906000013405823891061668252"
                  "localTransactionDateTime" "2016-10-23T07:24:04"
                  "retrievalReferenceNumber" "330000550000"
                  "senderCardExpiryDate" "2020-10"
                  "senderCurrencyCode" "USD"
                  "senderPrimaryAccountNumber" "4895142232120006"
                  "surcharge" "0.00"
                  "systemsTraceAuditNumber" "451001"})})))

; (def response (pull-funds))


(defn parse-pull-transaction-id [response]
  (-> response 
      :body 
      json/parse-string 
      (get "transactionIdentifier")))

; (parse-pull-transaction-id response)


; second leg of visa direct transaction
(defn push-funds [transaction-id] 
  (client/post
      "https://sandbox.api.visa.com/visadirect/fundstransfer/v1/pushfundstransactions"
      (merge auth
             {:content-type :json
              :body 
              (json/generate-string
                {"acquirerCountryCode" "840"
                 "acquiringBin" "408999"
                 "amount" "10.00"
                 "businessApplicationId" "AA"
                 "cardAcceptor" {"address" {"country" "USA",
                                            "county" "San Mateo"
                                            "state" "CA"
                                            "zipCode" "94404"}
                                 "idCode" "CA-IDCode-77765"
                                 "name" "GameGyft Inc."
                                 "terminalId" "TID-9999"}
                 "localTransactionDateTime" "2016-10-23T08:04:01"
                 "merchantCategoryCode" "6012"
                 "pointOfServiceData" {"motoECIIndicator" "0"
                                       "panEntryMode" "90"
                                       "posConditionCode" "00"}

                 "recipientName" "Josie"
                 "recipientPrimaryAccountNumber" "4957030420210496"
                 "retrievalReferenceNumber" "412770451018"
                 "senderAccountNumber" "4653459515756154"
                 "senderAddress" "901 Metro Center Blvd"
                 "senderCity" "Foster City"
                 "senderCountryCode" "124"
                 "senderName" "Shawn Tuttle"
                 "senderReference" ""
                 "senderStateCode" "CA"
                 "sourceOfFundsCode" "05"
                 "systemsTraceAuditNumber" "451018"
                 "transactionCurrencyCode" "USD"
                 "transactionIdentifier" transaction-id})})))


; (push-funds 234234322342343)



; processes a full visa direction tranction funding the
; marqueta gift card
(defn fund-gift []
  (-> (pull-funds)
      (parse-pull-transaction-id)
      (push-funds)))

; (def final-result (fund-gift))


(defn is-visa-direct-submission [request]
  (and (= (:uri request) "/visadirect")
       (= (:request-method request) :post)))
       
; (is-visa-direct-submission 
;   {:request-method :post
;    :uri "/visadirect"})
 


(defn handler [request]
  (if-not (is-visa-direct-submission request)
    {:status 404 :body "not found"}
    (fund-gift)))




(defn -main [& args]
  (jetty/run-jetty handler {:port 8080}))
