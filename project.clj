(defproject making-change "0.1.0"
  :description "Making Change"
  :url "http://craftsmanship.sv.cmu.edu/katas/making-change"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :target-path "target/%s"
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.6.2"]]}
             :uberjar {:aot :all}})
