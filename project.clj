(defproject mk12/mini-pinions "1.0"
  :description "ICS4U first summative project"
  :url "http://github.com/mk12/mini-pinions"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [quil "2.8.0"]]
  :main ^:skip-aot mini-pinions.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
