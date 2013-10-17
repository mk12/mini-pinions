(defproject mini-pinions "0.1.0-SNAPSHOT"
  :description "ICS4U first summative project"
  :url "http://github.com/mk12/mini-pinions"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [quil "1.6.0"]]
  :main mini-pinions.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
