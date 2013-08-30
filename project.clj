(defproject csspool "0.1.0-SNAPSHOT"
  :description "Quick and dirty css library with cross-browser prefixing."
  :url "https://github.com/crooney/csspool"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {:example {:source-paths ["example"]
                       :main example.css}}
  :aliases {"example" ["with-profile" "example" "run"]})
