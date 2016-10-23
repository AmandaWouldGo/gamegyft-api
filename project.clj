(defproject gamegyft-api "0.1.0-SNAPSHOT"
  :description "The GameGyft API"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.3.0"]
                 [cheshire "5.6.3"]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-jetty-adapter "1.5.0"]]

  :repl-options {:port 9090}
  
  :main gamegyft-api)
